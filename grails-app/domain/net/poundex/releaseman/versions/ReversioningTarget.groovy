package net.poundex.releaseman.versions

public abstract class ReversioningTarget
{
    static mapping = {
	    tablePerHierarchy: false
    }

	@Override
	public String toString()
	{
		return "ReversioningTarget #${id}"
	}
}
