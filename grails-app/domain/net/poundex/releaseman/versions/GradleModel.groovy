package net.poundex.releaseman.versions

class GradleModel extends ReversioningTarget
{

	String modelLocation

	static constraints = {
		modelLocation(validator: { it.endsWith("build.gradle") })
	}

	@Override
	public String toString()
	{
		return "Gradle @ ${modelLocation}"
	}
}
