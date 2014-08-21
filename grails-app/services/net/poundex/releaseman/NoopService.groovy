package net.poundex.releaseman

class NoopService
{
	public void noop(Release release)
	{
		release.name += " Finished"
	}
}
