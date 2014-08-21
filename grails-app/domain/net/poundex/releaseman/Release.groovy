package net.poundex.releaseman

import com.google.common.base.Throwables
import net.poundex.releaseman.ci.CIBuild
import net.poundex.releaseman.ci.JobConfiguration
import net.poundex.releaseman.util.Loggable
import net.poundex.releaseman.vcs.Revision
import net.poundex.releaseman.vcs.WorkingCopy
import net.poundex.releaseman.versions.SimpleVersion

class Release implements Loggable
{
	String name
	Revision revision
	Project project
	List<JobConfiguration> buildJobs = []
	List<JobConfiguration> releaseJobs = []
	List<LogEntry> logEntries = []
	WorkingCopy workingCopy

	SimpleVersion releaseVersion
	SimpleVersion nextSnapshotVersion

	Revision releaseRevision
	Revision snapshotRevision

	static embedded = ['releaseVersion', 'nextSnapshotVersion']

	static belongsTo = Project

	static hasMany = [sanityBuilds: CIBuild, deployJobs: CIBuild, QABuilds: CIBuild, releaseBuilds: CIBuild]

    static constraints = {
		project()
	    name()
	    revision()
	    releaseVersion()
	    nextSnapshotVersion()
	    releaseRevision(nullable: true, editable: false)
	    snapshotRevision(nullable: true, editable: false)
    }

	static transients = ['workingCopy']

	static mapping = {
		revision(lazy: false)
	}

	@Override
	public String toString()
	{
		return name
	}

	@Override
	void add(String message)
	{
		def le = new LogEntry(entry: message)
		le.save(flush: true, failOnError: true)
		logEntries << le
	}

	@Override
	void add(Throwable throwable)
	{
		String entry = throwable.getMessage() ?: "Unknown error"
		entry << "\n\n"
		throwable.getStackTrace().each {
			entry << it
		}
		entry << "\n\n"
		Throwables.getCausalChain(throwable).
				findAll { it.hasProperty("details") }.
				each {
					entry << throwable.details
					entry << "\n\n"
				}
		add(entry)
	}
}
