package net.poundex.releaseman.vcs

public abstract class Revision<T>
{
	SourceRepository sourceRepository

	static mapping = {
		tablePerHierarchy(false)
		sourceRepository(lazy: false)
	}

	static transients = ['revision']
	public abstract T revision

	@Override
	public String toString()
	{
		return "Revision ${this.getProperty("revision")} on ${sourceRepository}"
	}
}
