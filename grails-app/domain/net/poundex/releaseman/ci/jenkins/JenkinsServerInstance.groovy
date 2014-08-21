package net.poundex.releaseman.ci.jenkins

import com.offbytwo.jenkins.JenkinsServer
import net.poundex.releaseman.ci.CIServerInstance

class JenkinsServerInstance extends CIServerInstance
{
	String serverURL
	String username
	char[] password

	static constraints = {
	}

	JenkinsServer createClient()
	{
		return new JenkinsServer(
				serverURL.toURI(),
				username,
				password as String
		)
	}

	@Override
	public String toString()
	{
		return "Jenkins \"${super.toString()}\" @ ${serverURL}"
	}
}
