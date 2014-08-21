package net.poundex.releaseman.versions

import org.springframework.beans.factory.annotation.Autowired

import java.lang.reflect.Method

class VersionService
{
	private Map<String, VersionChanger> versionChangerMap = [:]

	VersionChanger versionChangerForTarget(ReversioningTarget reversioningTarget)
	{
		Class<? extends ReversioningTarget> targetClass = reversioningTarget.class
		Map matches = versionChangerMap.findAll { k,v ->
			Method reversionMethod = v.class.getDeclaredMethods().find { Method m ->
				m.name == "changeVersion"
			}
			reversionMethod.getParameterTypes().first() == targetClass
		}

		if(matches.isEmpty())
		{
			throw new RuntimeException("No service knows how to reversion a ${targetClass.name}")
		}
		if(matches.size() > 1)
		{
			throw new RuntimeException("Ambiguous version changer for ${targetClass.name}, matches ${matches.keySet()}")
		}
		VersionChanger versionChanger = matches.values().first()
		log.info("Delegating ${targetClass.name} to ${versionChanger.class.name}")
		return versionChanger
	}

	@Autowired(required = false)
	public void setVersionChangerMap(Map<String, VersionChanger> versionChangerMap)
	{
		log.debug("Setting VersionChangerMap (${versionChangerMap}")
		this.versionChangerMap = versionChangerMap
	}
}
