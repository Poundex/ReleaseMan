package net.poundex.releaseman.vcs
import grails.test.mixin.TestFor
import net.poundex.releaseman.vcs.git.GitRepo
import spock.lang.Shared
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(VCSService)
class VCSServiceSpec extends Specification
{
	@Shared
	GitProviderService gitService = new GitProviderService()

	def setup()
	{
		Map providerMap = ["Git":  gitService]
		service.setVCSProviderMap(providerMap)
	}

	def cleanup()
	{
	}

	void "Correct vcs service is returned"()
	{
		setup:
		GitRepo testRepo = new GitRepo()

		when:
		def vcsService = service.vcsServiceForRepo(testRepo)

		then:
		vcsService == gitService
	}
}
