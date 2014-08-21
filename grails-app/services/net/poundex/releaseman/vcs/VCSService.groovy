package net.poundex.releaseman.vcs
import net.poundex.releaseman.ci.BuildServerProvider
import org.springframework.beans.factory.annotation.Autowired

import java.lang.reflect.Method
/**
 * Created by poundera on 21/03/14.
 */
class VCSService
{
	private Map<String, BuildServerProvider> vcsProviderMap = [:]

	VersionControlProvider vcsServiceForRepo(SourceRepository sourceRepository)
	{
		Class<? extends SourceRepository> repoClass = sourceRepository.class
		Map matches = vcsProviderMap.findAll { k,v ->
			Method checkoutMethod = v.class.getDeclaredMethods().find { Method m ->
				m.name == "checkout"
			}
			checkoutMethod.getParameterTypes().first() == repoClass
		}

		if(matches.isEmpty())
		{
			throw new RuntimeException("No service knows how to deal with a ${repoClass.name}")
		}
		if(matches.size() > 1)
		{
			throw new RuntimeException("Ambiguous vcs prodiver for ${repoClass.name}, matches ${matches.keySet()}")
		}
		VersionControlProvider provider = matches.values().first()
		log.info("Delegating ${repoClass.name} to ${provider.class.name}")
		return provider
	}

	@Autowired(required = false)
	public void setVCSProviderMap(Map<String, VersionControlProvider> vcsProviderMap)
	{
		log.debug("Setting BuildServerProviderMap (${vcsProviderMap}")
		this.vcsProviderMap = vcsProviderMap
	}
}
