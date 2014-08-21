package net.poundex.releaseman.ci
import grails.test.mixin.TestFor
import spock.lang.Specification
/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(JenkinsService)
class JenkinsServiceSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

	void "not a test at all"()
	{
		expect:
		true
	}

//    void "not much of a test"()
//    {
//	    setup:
//	    JenkinsService js = new JenkinsService()
//	    def list = js.getAllJobs(new JenkinsServerInstance(serverURL: "http://192.168.0.50:9080/"))
//	    list.each { JenkinsJobConfiguration jc ->
//		    println jc.name
//	    }
//
//	    expect:
//	    true
//
//    }
}
