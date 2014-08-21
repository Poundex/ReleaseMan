package net.poundex.releaseman.versions
import com.github.zafarkhaja.semver.Version
import grails.test.mixin.TestFor
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(GradleVersionChangerService)
class GradleVersionChangerServiceSpec extends Specification
{
	def setup()
	{
	}

	def cleanup()
	{
	}

	void "Correct version is returned"()
	{
		setup:
		File tempFile = File.createTempFile("forTest", "build.gradle")
		tempFile.text = """
			foo = "bar"
			version = "2.3.4"
"""
		GradleModel testGradleModel = new GradleModel(modelLocation: tempFile.name)
		GradleVersionChangerService service = new GradleVersionChangerService()

		expect:
		service.getCurrentVersion(
				testGradleModel,
				tempFile.getParentFile()) == Version.forIntegers(2, 3, 4)
	}

	void "Version is changed correctly in file"()
	{
		setup:
		File tempFile = File.createTempFile("forTest", "build.gradle")
		tempFile.text = """
			notVersion = "1.2.3"
			version = "2.3.4"
			notVersionEither = "3.4.5"
"""
		GradleModel testGradleModel = new GradleModel(modelLocation: tempFile.name)
		GradleVersionChangerService service = new GradleVersionChangerService()

		when:
		service.changeVersion(
				testGradleModel,
				tempFile.getParentFile(),
				Version.forIntegers(7, 8, 9))

		then: "Things that aren't version remain unchanged, new version number is set correctly"
		String resultingModel = tempFile.text
		resultingModel.contains("notVersion = \"1.2.3\"")
		resultingModel.contains("notVersionEither = \"3.4.5\"")
		resultingModel.contains("version = \"7.8.9\"")
	}
}
