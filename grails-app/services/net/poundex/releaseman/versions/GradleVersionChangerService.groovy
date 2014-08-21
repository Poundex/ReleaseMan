package net.poundex.releaseman.versions

import com.github.zafarkhaja.semver.Version

class GradleVersionChangerService implements VersionChanger<GradleModel>
{

	private static final String GRADLE_VERSION = /(?m)^\s*version\s*=\s*"([0-9A-Za-z.-]+)"\s*?$/



	@Override
	Version getCurrentVersion(GradleModel target, File location)
	{
		File model = getModel(location, target)
		def matches = model.text =~ GRADLE_VERSION
		return Version.valueOf(matches[0][1])
	}

	@Override
	void changeVersion(GradleModel target, File location, Version newVersion)
	{
		File model = getModel(location, target)
		model.text = model.text.replace(getCurrentVersion(target, location).toString(), newVersion.toString())
	}

	private File getModel(File location, GradleModel target)
	{
		File model = new File(location, target.modelLocation)
		if(!model.isFile())
		{
			throw new IllegalArgumentException("Couldn't find reversioning target ${model.absolutePath}")
		}
		return model
	}
}
