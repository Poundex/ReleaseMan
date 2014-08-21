package net.poundex.releaseman
import grails.validation.Validateable
import net.poundex.releaseman.vcs.SourceRepository

/**
 * Created by poundera on 27/03/14.
 */
@Validateable
class ConfigureReleaseCommand
{
	long id
	Project project
	String name
	SourceRepository repo
	String revision
	String releaseVersion
	String nextSnapshotVersion

	static constraints = {
		project(nullable: false)
		name(nullable: true)
		repo(nullable: false)
		revision(nullable: false)
		revision(nullable: false)
		releaseVersion(nullable: false)
		nextSnapshotVersion(nullable: false)
	}
}
