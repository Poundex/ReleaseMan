package net.poundex.releaseman.vcs

public abstract class SourceRepository
{
	String name

	static constraints = {
	}

	static mapping = {
		tablePerHierarchy(false)
	}

	@Override
	public String toString()
	{
		return name
	}
}
