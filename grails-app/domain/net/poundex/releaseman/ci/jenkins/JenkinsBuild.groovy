package net.poundex.releaseman.ci.jenkins
import net.poundex.releaseman.ci.CIBuild

class JenkinsBuild extends CIBuild
{
	long buildNumber
	String logText

    static constraints = {
    }

	static mapping = {
		logText(type: "text")
	}

	@Override
	public String toString()
	{
		return super.toString() + " (#${buildNumber})"
	}
}
