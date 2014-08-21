package net.poundex.releaseman.versions
import grails.test.mixin.TestFor
import spock.lang.Shared
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(VersionService)
class VersionServiceSpec extends Specification
{
	@Shared
	GradleVersionChangerService gradleVersionChangerService = new GradleVersionChangerService()

	def setup()
	{
		Map providerMap = ["Gradle": gradleVersionChangerService]
		service.setVersionChangerMap(providerMap)
	}

	def cleanup()
	{
	}

	void "Correct version service is returned"()
	{
		setup:
		GradleModel testModel = new GradleModel()

		when:
		def versionService = service.versionChangerForTarget(testModel)

		then:
		versionService == gradleVersionChangerService
	}
}