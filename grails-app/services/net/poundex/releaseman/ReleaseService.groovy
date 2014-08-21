package net.poundex.releaseman
import net.poundex.releaseman.ci.*
import net.poundex.releaseman.exception.BuildException
import net.poundex.releaseman.util.Loggable
import net.poundex.releaseman.vcs.Revision
import net.poundex.releaseman.vcs.VersionControlProvider
import net.poundex.releaseman.vcs.WorkingCopy
import net.poundex.releaseman.versions.VersionChanger
import org.springframework.integration.MessagingException

import java.lang.reflect.UndeclaredThrowableException

class ReleaseService
{
	def buildService
	def VCSService
	def versionService
	def releaseServiceGateway

	def pushRelease(final Release release)
	{
		Thread.start {
			Release.withNewSession {
				Release makeThis = Release.get(release.id)
				releaseServiceGateway.makeRelease(makeThis)
			}
		}
		return
	}

    public Release makeRelease(Release release)
    {
	    return release
    }

	public Release prepareBuildJobs(Release release)
	{
		for(JobConfiguration jobConfiguration: release.project.buildJobs)
		{
			BuildServerProvider buildServerProvider = buildService.buildServiceForJob(jobConfiguration)
			JobConfiguration configuredJob = buildServerProvider.createJobFor(jobConfiguration, release.revision)
			release.buildJobs << configuredJob
		}

		release.save()
	}

	public Release sanityBuild(Release release)
	{
		log.info("Sanity build for ${release}")
		for(JobConfiguration jobConfiguration: release.buildJobs)
		{
			BuildServerProvider buildServerProvider = buildService.buildServiceForJob(jobConfiguration)
			CIBuild build = buildServerProvider.build(jobConfiguration)
			build.save(flush: true, failOnError: true)
			release.addToSanityBuilds(build)
			release.save()
			if(build.result != CIResult.SUCCESS)
			{
				throw new BuildException(release, build)
			}
		}
		return release
	}

	public Release deployToQA(Release release)
	{
		log.info "deployToQA(${release})"
		JobSet deployJobSet = release.project.qaDeployment
		BuildServerProvider buildServerProvider = buildService.buildServiceForJob(deployJobSet.jobConfiguration)
		List<JobConfiguration> deployJobs = buildServerProvider.
				getAllJobs(deployJobSet.jobConfiguration.serverInstance).
				findAll { it =~ deployJobSet.jobConfiguration.name }

		deployJobs.each { job ->
			release.sanityBuilds.each { build ->
				CIBuild deployment = buildServerProvider.deployJobWithArtefacts(job, build.artefacts, deployJobSet.jobConfiguration)
				release.deployJobs << deployment
				release.save()
				if(deployment.result != CIResult.SUCCESS)
				{
					throw new BuildException(release, deployment)
				}
			}
		}
		return release
	}

	public Release runQAJobs(Release release)
	{
		log.info "runQAJobs(${release})"
		for(JobConfiguration jobConfiguration: release.project.qaJobs)
		{
			BuildServerProvider buildServerProvider = buildService.buildServiceForJob(jobConfiguration)
			CIBuild build = buildServerProvider.build(jobConfiguration)
			release.addToQABuilds(build)
			release.save()
			if(build.result != CIResult.SUCCESS)
			{
				throw new BuildException(release, build)
			}
		}
		return release
	}

	public Release checkoutSource(Release release)
	{
		log.info "checkoutSource(${release})"
		VersionControlProvider service = VCSService.vcsServiceForRepo(release.project.sourceRepository)

		File workingCopyDir = File.createTempDir()
		workingCopyDir.deleteOnExit()

		WorkingCopy workingCopy = service.checkout(release.project.sourceRepository, release.revision, workingCopyDir)
		release.setWorkingCopy(workingCopy)
		return release
	}

	public Release versionAsRelease(Release release)
	{
		log.info "versionAsRelease(${release})"
		if(release.workingCopy == null)
		{
			throw new RuntimeException("No working copy to reversion")
		}

		VersionChanger versionChanger = versionService.versionChangerForTarget(release.project.reversioningTarget)
		log.info("Current version for ${release.project} is ${versionChanger.getCurrentVersion(release.project.reversioningTarget, release.workingCopy.location)}")
		versionChanger.changeVersion(
				release.project.reversioningTarget,
				release.workingCopy.location,
				release.releaseVersion.asVersion()
		)
		return release
	}

	public Release checkinAndTagReleaseRevision(Release release)
	{
		log.info("tagReleaseRevision(${release})")

		if(release.workingCopy == null)
		{
			throw new RuntimeException("No working copy to tag")
		}

		VersionControlProvider vcs = VCSService.vcsServiceForRepo(release.project.sourceRepository)
		vcs.checkin(release.workingCopy, "Version ${release.releaseVersion.toString()} for Release")

		Revision taggedReleaseRevision = vcs.tag(release.workingCopy, release.releaseVersion.toString(), release.releaseVersion.toString())
		release.releaseRevision = taggedReleaseRevision
		release.save()
		return release
	}

	public Release releaseBuild(Release release)
	{
		log.info("Release build for ${release}")
		for(JobConfiguration jobConfiguration: release.buildJobs)
		{
			BuildServerProvider buildServerProvider = buildService.buildServiceForJob(jobConfiguration)
			JobConfiguration configuredJob = buildServerProvider.createJobFor(jobConfiguration, release.releaseRevision)
			release.releaseJobs << configuredJob

			CIBuild build = buildServerProvider.build(configuredJob)
			release.addToReleaseBuilds(build)
			release.save()
			if(build.result != CIResult.SUCCESS)
			{
				throw new BuildException(release, build)
			}
		}
		return release
	}

	public Release versionAsSnapshot(Release release)
	{
		log.info "versionAsSnapshot(${release})"
		if(release.workingCopy == null)
		{
			throw new RuntimeException("No working copy to reversion")
		}

		VersionControlProvider vcs = VCSService.vcsServiceForRepo(release.project.sourceRepository)
		vcs.clean(release.workingCopy)

		VersionChanger versionChanger = versionService.versionChangerForTarget(release.project.reversioningTarget)
		log.info("Current version for ${release.project} is ${versionChanger.getCurrentVersion(release.project.reversioningTarget, release.workingCopy.location)}")
		versionChanger.changeVersion(
				release.project.reversioningTarget,
				release.workingCopy.location,
				release.nextSnapshotVersion.asVersion()
		)
		return release
	}

	public Release checkinSnapshotRevision(Release release)
	{
		log.info("checkinSnapshotRevision(${release})")

		if(release.workingCopy == null)
		{
			throw new RuntimeException("No working copy to tag")
		}

		VersionControlProvider vcs = VCSService.vcsServiceForRepo(release.project.sourceRepository)
		Revision snapshotRevision = vcs.checkin(release.workingCopy, "Version ${release.nextSnapshotVersion.toString()} for development")

		release.snapshotRevision = snapshotRevision
		release.save()
		return release
	}

	public void cleanup(Release release)
	{
		log.info("cleanup(${release})")
		[release.buildJobs, release.releaseJobs].flatten().findAll { it.automaticallyCreated }.each { JobConfiguration jobConfiguration ->
			BuildServerProvider buildServerProvider = buildService.buildServiceForJob(jobConfiguration)
			buildServerProvider.deleteJob(jobConfiguration)
		}
		return
	}

	public void handleError(final MessagingException mex)
	{
		log.info("handleError(...)")
		Throwable throwable = mex
		Object payloadAtFailure = mex.getFailedMessage()?.payload
		while((throwable instanceof MessagingException
				|| throwable instanceof UndeclaredThrowableException
				) && throwable.getCause() != null)
		{
			throwable = throwable.getCause()
		}

		log.error(throwable.getMessage(), throwable);

		if(payloadAtFailure instanceof Loggable)
		{
			((Loggable)payloadAtFailure).add(throwable)
		}
	}
}
