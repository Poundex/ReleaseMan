package net.poundex.releaseman.exception

import net.poundex.releaseman.Release
import net.poundex.releaseman.ci.CIBuild

/**
 * Created by poundera on 17/03/14.
 */
class BuildException extends RuntimeException
{
	public BuildException(Release release, CIBuild build)
	{
		super("Build failed (${build.result}). Building ${build} for ${release} on ${release.project}")
	}
}
