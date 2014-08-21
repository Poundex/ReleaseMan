package net.poundex.releaseman

import net.poundex.releaseman.util.DirtyObjectException
import net.poundex.releaseman.util.DirtyObjectGuardian
import org.grails.datastore.gorm.GormInstanceApi
import org.springframework.integration.Message
import org.springframework.integration.MessageChannel
import spock.lang.Shared
import spock.lang.Specification

/**
 * Created by poundex on 29/11/13.
 */
class DirtyObjectGuardianSpec extends Specification
{
	@Shared final DirtyObjectGuardian guardian = new DirtyObjectGuardian()

	@Shared MessageChannel messageChannel = Stub(MessageChannel)

	def setup()
	{

	}

	def "Not a domain class should return message intact"()
	{
		setup:
		Object object = new Object()
		Message message = Stub(Message)
		message.getPayload() >> object

		expect:
		guardian.preSend(message, messageChannel) == message
	}

	def "Clean object should return message intact"()
	{
		setup:
		GormInstanceApi testInstance = Stub(GormInstanceApi)
		testInstance.getDirtyPropertyNames() >> []
		Message message = Stub(Message)
		message.getPayload() >> testInstance

		expect:
		guardian.preSend(message, messageChannel) == message
	}

	def "Dirty object should throw DirtyObjectException"()
	{
		setup:
		GormInstanceApi testInstance = Stub(GormInstanceApi)
		testInstance.getDirtyPropertyNames() >> ["dirty", "properties"]
		testInstance.getDirtyPropertyNames(_) >> ["dirty", "properties"]
		Message<GormInstanceApi> message = Stub(Message)
		message.getPayload() >> testInstance

		when:
		def result = guardian.preSend(message, messageChannel)

		then:
		final DirtyObjectException doex = thrown()
		result != message
	}

}
