package net.poundex.releaseman.ci
import org.springframework.beans.factory.annotation.Autowired

import java.lang.reflect.Method

class BuildService
{
	private Map<String, BuildServerProvider> buildServerProviderMap = [:]

	def buildServiceForJob(JobConfiguration jobConfiguration)
	{
		Class<? extends JobConfiguration> jobClass = jobConfiguration.class
		Map matches = buildServerProviderMap.findAll { k,v ->
			Method buildMethod = v.class.getDeclaredMethods().find { Method m ->
				m.name == "build"
			}
			buildMethod.getParameterTypes().first() == jobClass
		}

		if(matches.isEmpty())
		{
			throw new RuntimeException("No service knows how to deal with a ${jobClass.name}")
		}
		if(matches.size() > 1)
		{
			throw new RuntimeException("Ambiguous build server prodiver for ${jobClass.name}, matches ${matches.keySet()}")
		}
		BuildServerProvider provider = matches.values().first()
		log.info("Delegating ${jobClass.name} to ${provider.class.name}")
		return provider
	}

	@Autowired(required = false)
	public void setBuildServerProviderMap(Map<String, BuildServerProvider> buildServiceProviderMap)
	{
		log.debug("Setting BuildServerProviderMap (${buildServiceProviderMap}")
		this.buildServerProviderMap = buildServiceProviderMap
	}
}
