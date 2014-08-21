package net.poundex.releaseman.ci

import net.poundex.releaseman.Project

public abstract class JobConfiguration
{
	String name
	CIServerInstance serverInstance
	boolean automaticallyCreated = false
	String artefactPattern = ""

	static belongsTo = Project
	static constraints = {
		name()
		serverInstance()
		artefactPattern(nullable: false, blank: true)
		automaticallyCreated(display: false, editable: false)
	}

	static mapping = {

	}

	@Override
	public String toString()
	{
		return "Build job ${name} on ${serverInstance}"
	}
}
