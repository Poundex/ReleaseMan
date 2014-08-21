package net.poundex.releaseman.versions

class POMModel extends ReversioningTarget
{
	String modelLocation

	static constraints = {
		modelLocation(validator: { it.endsWith("pom.xml") })
	}

	@Override
	public String toString()
	{
		return "POM @ ${modelLocation}"
	}
}
