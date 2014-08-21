package net.poundex.releaseman.versions

import com.github.zafarkhaja.semver.Version
import spock.lang.Specification

class MavenVersionChangerServiceTest extends Specification
{
	void setup()
	{

	}

	void cleanup()
	{

	}

	void "Correct version is returned"()
	{
		setup:
		File tempFile = File.createTempFile("forTest", "pom.xml")
		tempFile.text = """<?xml version="1.0" encoding="utf-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <version>2.3.4</version>
</project>
"""
		POMModel testMavenModel = new POMModel(modelLocation: tempFile.name)
		MavenVersionChangerService service = new MavenVersionChangerService()

		expect:
		service.getCurrentVersion(
				testMavenModel,
				tempFile.getParentFile()) == Version.forIntegers(2, 3, 4)
	}

	void "Version is changed correctly in file"()
	{
		setup:
		File tempFile = File.createTempFile("forTest", "pom.xml")
		tempFile.text = """<?xml version="1.0" encoding="utf-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <version>2.3.4</version>
</project>
"""
		POMModel testMavenModel = new POMModel(modelLocation: tempFile.name)
		MavenVersionChangerService service = new MavenVersionChangerService()

		when:
		service.changeVersion(
				testMavenModel,
				tempFile.getParentFile(),
				Version.forIntegers(7, 8, 9))

		then: "Things that aren't version remain unchanged, new version number is set correctly"
		String resultingModel = tempFile.text
		resultingModel.contains("<modelVersion>4.0.0</modelVersion>");
		resultingModel.contains("<version>7.8.9</version>")
	}
}
