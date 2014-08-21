package net.poundex.releaseman.ci

public abstract class CIServerInstance
{
	String name

	static constraints = {
	}

	@Override
	public String toString()
	{
		return name
	}

}
