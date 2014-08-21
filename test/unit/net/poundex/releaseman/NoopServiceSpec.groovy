package net.poundex.releaseman

import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(NoopService)
class NoopServiceSpec extends Specification
{

	def setup()
	{
	}

	def cleanup()
	{
	}

	void "The flow test should append 'Finished' to release name"()
	{
		setup:
		NoopService service = new NoopService()
		Release release = new Release(name: "Test Release")

		when:
		service.noop(release)

		then:
		release.name == "Test Release Finished"
	}
}
