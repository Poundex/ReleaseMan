package net.poundex.releaseman

import net.poundex.releaseman.ci.JobConfiguration
import net.poundex.releaseman.ci.JobSet
import net.poundex.releaseman.vcs.SourceRepository
import net.poundex.releaseman.versions.ReversioningTarget

class Project
{
	String name
	SourceRepository sourceRepository
	JobSet qaDeployment
	ReversioningTarget reversioningTarget

	static hasMany = [
			modules: Module,
			releases: Release,
			buildJobs: JobConfiguration,
			qaJobs: JobConfiguration
	]

	static constraints = {
		name()
		modules()
		buildJobs()
		qaJobs()
		sourceRepository()
		reversioningTarget()
		releases()
		qaDeployment()
	}

	static mapping = {
		buildJobs(lazy: false)
		qaJobs(lazy: false)
	}

	@Override
	public String toString()
	{
		return name
	}
}
