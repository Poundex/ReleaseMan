package net.poundex.releaseman.versions

import com.github.zafarkhaja.semver.Version

/**
 * Although I have purged the server of all Maven nastiness, it seems
 * the client uses a "fake" Maven project to push stuff into Nexus, and
 * so the POM is where we need to update the version number for the C# GUI
 * TODO - Investigate better ways (Native .NET support? "Fake" Gradle project instead?)
 */
class MavenVersionChangerService implements VersionChanger<POMModel>
{
	@Override
	Version getCurrentVersion(POMModel target, File location)
	{
		File model = getModel(location, target)

		def root = new XmlParser().parseText(model.text)
		def versionNode = root.version[0]
		def currentVersion = versionNode.value()[0]

		return Version.valueOf(currentVersion)
	}

	@Override
	void changeVersion(POMModel target, File location, Version newVersion)
	{
		File model = getModel(location, target)
		def root = new XmlParser().parseText(model.text)
		def versionNode = root.version[0]
		versionNode.value = newVersion.toString()
		StringWriter writer = new StringWriter()
		def printer = new XmlNodePrinter(new PrintWriter(writer))
		printer.preserveWhitespace = true
		printer.print(root)
		model.text = writer.toString()
	}

	private File getModel(File location, POMModel target)
	{
		File model = new File(location, target.modelLocation)
		if(!model.isFile())
		{
			throw new IllegalArgumentException("Couldn't find reversioning target ${model.absolutePath}")
		}
		return model
	}
}
