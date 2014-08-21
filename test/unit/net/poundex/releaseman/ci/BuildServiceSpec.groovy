package net.poundex.releaseman.ci
import grails.test.mixin.TestFor
import net.poundex.releaseman.ci.jenkins.JenkinsJobConfiguration
import spock.lang.Shared
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(BuildService)
class BuildServiceSpec extends Specification
{
	@Shared
	JenkinsService jenkinsService = new JenkinsService()

	def setup()
	{
		Map providerMap = ["Jenkins": jenkinsService]
		service.setBuildServerProviderMap(providerMap)
	}

	def cleanup()
	{
	}

	void "Correct build service is returned"()
	{
		setup:
		JenkinsJobConfiguration testJobConfig = new JenkinsJobConfiguration()

		when:
		def buildService = service.buildServiceForJob(testJobConfig)

		then:
		buildService == jenkinsService
	}
}
