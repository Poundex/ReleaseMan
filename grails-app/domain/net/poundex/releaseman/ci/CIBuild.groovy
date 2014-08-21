package net.poundex.releaseman.ci

public abstract class CIBuild
{
	CIServerInstance ciServerInstance
	JobConfiguration jobConfiguration
	CIResult result
	List<String> artefacts = []

    static mapping = {
	    tablePerHierarchy(false)
    }

	static constraints = {

	}

	@Override
	public String toString()
	{
		return "Build for ${jobConfiguration}"
	}

}
