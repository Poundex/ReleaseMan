package net.poundex.releaseman.vcs.git

import net.poundex.releaseman.vcs.SourceRepository

class GitRepo extends SourceRepository
{
	String url
	String username
	char[] password

	public String urlWithAuth()
	{
		String urlWithAuth = this.url.replace("@[U]", username)

		urlWithAuth = urlWithAuth.replace("@[P]", new String(password))
		return urlWithAuth
	}

    static constraints = {
    }
}
