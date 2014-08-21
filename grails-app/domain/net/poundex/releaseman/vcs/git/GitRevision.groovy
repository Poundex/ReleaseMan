package net.poundex.releaseman.vcs.git

import net.poundex.releaseman.vcs.Revision

class GitRevision extends Revision<String>
{
	String hash

	static constraints = { }

	@Override
	String getRevision()
	{
		return hash
	}

	@Override
	void setRevision(String revision)
	{
		hash = revision
	}
}
