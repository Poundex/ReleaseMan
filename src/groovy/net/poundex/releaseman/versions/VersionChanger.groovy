package net.poundex.releaseman.versions

import com.github.zafarkhaja.semver.Version

/**
 * Created by poundex on 02/12/13.
 */
public interface VersionChanger<M extends ReversioningTarget>
{
	Version getCurrentVersion(M target, File location)
	void changeVersion(M target, File location, Version newVersion)
}