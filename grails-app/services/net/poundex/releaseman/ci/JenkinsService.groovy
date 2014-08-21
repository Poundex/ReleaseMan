package net.poundex.releaseman.ci
import com.offbytwo.jenkins.JenkinsServer
import com.offbytwo.jenkins.model.*
import net.poundex.releaseman.ci.jenkins.JenkinsBuild
import net.poundex.releaseman.ci.jenkins.JenkinsJobConfiguration
import net.poundex.releaseman.ci.jenkins.JenkinsServerInstance
import net.poundex.releaseman.exception.CIException
import net.poundex.releaseman.vcs.Revision

class JenkinsService implements BuildServerProvider<JenkinsJobConfiguration, JenkinsBuild, JenkinsServerInstance>
{
	@Override
	List<JenkinsJobConfiguration> getAllJobs(JenkinsServerInstance instance)
	{
		List<JenkinsJobConfiguration> retval = []
		JenkinsServer server = instance.createClient()
		server.jobs.each { String jobName, Job jobDetails ->
			retval << new JenkinsJobConfiguration(
					serverInstance: instance,
					name: jobName,
			)
		}
		return retval
	}

	@Override
	JenkinsBuild build(JenkinsJobConfiguration job)
	{
		JenkinsServerInstance jenkins = job.serverInstance
		JenkinsServer endpoint = jenkins.createClient()

		List<Build> originalBuilds = endpoint.getJob(job.name).builds
		endpoint.getJob(job.name).build()

		Build myBuild = waitForBuildEntity(jenkins, job, originalBuilds)
		waitForBuild(myBuild)

		JenkinsBuild retval = new JenkinsBuild(ciServerInstance: jenkins, jobConfiguration: job, result: genericResultFor(myBuild.details().result))
		retval.setLogText(myBuild.getLogText())
		retval.setBuildNumber(myBuild.number)
		List<Artifact> matchingArtefacts = myBuild.details().artifacts.findAll { it.fileName =~ job.artefactPattern }
		retval.artefacts.addAll(matchingArtefacts.collect { "${myBuild.url}artifact/${it.relativePath}" })
		retval.save(flush: true, failOnError: true)
		return retval
	}

	@Override
	JenkinsBuild updateBuild(JenkinsBuild build)
	{
		return null
	}

	private Build waitForBuildEntity(JenkinsServerInstance jenkins, JenkinsJobConfiguration job, List<Build> originalBuilds)
	{
		Build retval = null
		JenkinsServer endpoint = jenkins.createClient()

		for(int i=0; i < 3; i++)
		{
			log.info "Waiting for build to appear for ${job} attempt ${i + 1} of 3"
			Thread.sleep(16000) // Twice the quiet period - should be smarter
			List<Build> refreshedBuilds = endpoint.getJob(job.name).builds

			if(refreshedBuilds.size() == originalBuilds.size() + 1)
			{
				retval = refreshedBuilds.first()
				log.info("Build is ${job.name}/${retval.number}")
				break;
			}
		}

		if(!retval)
		{
			throw new RuntimeException("Tried to build ${job} on ${jenkins} but it did not appear to start")
		}

		return retval
	}

	@Override
	JenkinsJobConfiguration createJobFor(JenkinsJobConfiguration jobConfiguration, Revision revision)
	{
		JenkinsServerInstance jenkins = jobConfiguration.serverInstance
		JenkinsServer endpoint = jenkins.createClient()
		String origXML = endpoint.getJobXml(jobConfiguration.name)
		String newXML = JenkinsUtil.createRevisionJob(origXML, revision)
		String newJobName = "${jobConfiguration.name} - ${revision.revision}"

		try {
			endpoint.deleteJob(newJobName)
		} catch(final Exception ex) { }

		endpoint.createJob(newJobName, newXML);
		List<Build> buildsForJob = endpoint.getJob(newJobName).builds
		if(buildsForJob.size() != 0)
		{
			throw new CIException("Newly created job ${newJobName} on ${jobConfiguration.serverInstance} should not have any builds")
		}

		JenkinsJobConfiguration retval = new JenkinsJobConfiguration(
				name: newJobName,
				serverInstance: jobConfiguration.serverInstance,
				automaticallyCreated: true
		)
		retval.setArtefactPattern(jobConfiguration.artefactPattern)
		retval.save()
		return retval
	}

	@Override
	void deleteJob(JenkinsJobConfiguration jobConfiguration)
	{
		JenkinsServerInstance jenkins = jobConfiguration.serverInstance
		JenkinsServer endpoint = jenkins.createClient()
		endpoint.deleteJob(jobConfiguration.name)
	}

	@Override
	JenkinsBuild deployJobWithArtefacts(JenkinsJobConfiguration job, List<String> artefacts, JenkinsJobConfiguration outerJob = null)
	{
		JenkinsServerInstance jenkins = job.serverInstance

		ConfigObject artefactsURLs = new ConfigObject();
		artefactsURLs.httpAuthNoChallenge = true
		artefactsURLs.httpAuthUsername = jenkins.username
		artefactsURLs.httpAuthPassword = jenkins.password as String
		List<String> urls = []
		artefacts.each { urls << "'${it}'" }
		artefactsURLs.urls = urls

		File artefactsURLsFile = File.createTempFile("artefacts", ".urls")
		artefactsURLsFile.deleteOnExit()
		artefactsURLsFile.withWriter { Writer w ->
			w.write(artefactsURLs)
		}

		JenkinsServer endpoint = jenkins.createClient()
		JobWithDetails jenkinsJob = endpoint.getJob(job.name)

		jenkinsJob.client.post_file(jenkinsJob.url + "buildWithParameters", artefactsURLsFile)
		Build deployBuild = waitForBuildEntity(jenkins, job, jenkinsJob.builds)
		waitForBuild(deployBuild)

		JenkinsBuild retval = new JenkinsBuild(ciServerInstance: jenkins, jobConfiguration: outerJob ?: job, result: genericResultFor(deployBuild.details().result))
		retval.setLogText(deployBuild.getLogText())
		retval.save()
		return retval
	}

	private void waitForBuild(Build build)
	{
		log.info("Jenkins: Waiting for build ${build}")
		BuildWithDetails buildWithDetails = build.details()
		while(buildWithDetails.building || (buildWithDetails.result == null
				|| buildWithDetails.result == BuildResult.BUILDING
				|| buildWithDetails.result == BuildResult.REBUILDING))
		{
			Thread.sleep(500)
			buildWithDetails = build.details()
		}
		log.info("Jenkins: Done waiting for ${build}, result was ${buildWithDetails.result}")
	}

	private CIResult genericResultFor(BuildResult buildResult)
	{
		try {
			return CIResult.valueOf(CIResult, buildResult.toString())
		} catch(final IllegalArgumentException|NullPointerException ex)
		{
			return CIResult.UNKNOWN
		}
	}
}
