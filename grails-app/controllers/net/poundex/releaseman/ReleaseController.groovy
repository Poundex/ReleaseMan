package net.poundex.releaseman

import com.sun.corba.se.impl.util.Version
import grails.transaction.Transactional
import net.poundex.releaseman.vcs.Revision
import net.poundex.releaseman.vcs.SourceRepository
import net.poundex.releaseman.versions.SimpleVersion
import sun.security.krb5.Config

import static org.springframework.http.HttpStatus.CREATED
import static org.springframework.http.HttpStatus.NOT_FOUND
import static org.springframework.http.HttpStatus.OK

class ReleaseController
{
	def releaseService
	def VCSService

    static scaffold = true

	def doMakeRelease(final Release release)
	{
		releaseService.pushRelease(release)

		flash.message = "Release started"
		redirect release

	}

	def create()
	{
		respond new ConfigureReleaseCommand()
	}

	@Transactional
	def save(ConfigureReleaseCommand releaseConfiguration)
	{
		if (releaseConfiguration == null) {
			notFound()
			return
		}

		if (releaseConfiguration.hasErrors()) {
			respond releaseConfiguration.errors, view:'configure'
			return
		}

		Release release = new Release()
		release.name = releaseConfiguration.name ?: releaseConfiguration.releaseVersion

		SourceRepository repo = releaseConfiguration.repo
		Revision revision = VCSService.vcsServiceForRepo(repo).getRevision(repo)
		revision.revision = releaseConfiguration.revision
		revision.save flush: true, failOnError: true
		release.revision = revision

		release.project = releaseConfiguration.project
		release.releaseVersion =
				SimpleVersion.from(releaseConfiguration.releaseVersion).save()
		release.nextSnapshotVersion =
				SimpleVersion.from(releaseConfiguration.nextSnapshotVersion).save()

		if(release.hasErrors()) {
			respond release.errors, view: 'configure'
			return
		}

		release.save flush: true, failOnError: true

		request.withFormat {
			form {
				flash.message = message(code: 'default.created.message', args: [message(code: 'release.label', default: 'Release'), release.id])
				redirect release
			}
			'*' { respond release, [status: CREATED] }
		}
	}

	def edit(Release release)
	{
		println release.id
		ConfigureReleaseCommand releaseConfiguration = new ConfigureReleaseCommand()
		releaseConfiguration.id = release.id
		println releaseConfiguration.id
		releaseConfiguration.project = release.project
		releaseConfiguration.name = release.name
		releaseConfiguration.repo = release.revision.sourceRepository
		releaseConfiguration.revision = release.revision.revision
		releaseConfiguration.releaseVersion = release.releaseVersion.toString()
		releaseConfiguration.nextSnapshotVersion = release.nextSnapshotVersion.toString()
		respond releaseConfiguration
	}

	@Transactional
	def update(ConfigureReleaseCommand releaseConfiguration)
	{
		Release releaseInstance = Release.get(releaseConfiguration.id)
		if (releaseInstance == null) {
			notFound()
			return
		}

		if (releaseConfiguration.hasErrors()) {
			respond releaseConfiguration.errors, view:'edit'
			return
		}

		releaseInstance.name = releaseConfiguration.name ?: releaseConfiguration.releaseVersion

		SourceRepository repo = releaseConfiguration.repo
		Revision revision = VCSService.vcsServiceForRepo(repo).getRevision(repo)
		revision.revision = releaseConfiguration.revision
		revision.save flush: true, failOnError: true
		releaseInstance.revision = revision

		releaseInstance.project = releaseConfiguration.project
		releaseInstance.releaseVersion =
				SimpleVersion.from(releaseConfiguration.releaseVersion).save()
		releaseInstance.nextSnapshotVersion =
				SimpleVersion.from(releaseConfiguration.nextSnapshotVersion).save()

		if(releaseInstance.hasErrors()) {
			respond releaseInstance.errors, view: 'configure'
			return
		}

		releaseInstance.save flush: true, failOnError: true

		request.withFormat {
			form {
				flash.message = message(code: 'default.updated.message', args: [message(code: 'Release.label', default: 'Release'), releaseInstance.id])
				redirect releaseInstance
			}
			'*'{ respond releaseInstance, [status: OK] }
		}
	}

	protected void notFound() {
		request.withFormat {
			form {
				flash.message = message(code: 'default.not.found.message', args: [message(code: 'releaseInstance.label', default: 'Release'), params.id])
				redirect action: "index", method: "GET"
			}
			'*'{ render status: NOT_FOUND }
		}
	}
}
