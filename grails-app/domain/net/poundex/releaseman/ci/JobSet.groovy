package net.poundex.releaseman.ci

class JobSet
{
	JobConfiguration jobConfiguration

	static constraints = {
	}

	@Override
	public String toString()
	{
		return "Jobs matching ${jobConfiguration?.name} on ${jobConfiguration?.serverInstance}"
	}
}
