package net.poundex.releaseman

class Module
{
	String name

	static belongsTo = Project

	static constraints = {
	}

	@Override
	public String toString()
	{
		return name
	}
}
