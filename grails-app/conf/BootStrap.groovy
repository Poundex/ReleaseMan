import net.poundex.releaseman.Project
import net.poundex.releaseman.Release
import net.poundex.releaseman.ci.JobSet
import net.poundex.releaseman.ci.jenkins.JenkinsJobConfiguration
import net.poundex.releaseman.ci.jenkins.JenkinsServerInstance
import net.poundex.releaseman.vcs.git.GitRepo
import net.poundex.releaseman.vcs.git.GitRevision
import net.poundex.releaseman.versions.GradleModel
import net.poundex.releaseman.versions.POMModel
import net.poundex.releaseman.versions.ReversioningTarget
import net.poundex.releaseman.versions.SimpleVersion

class BootStrap {

    def init = { servletContext ->
	    GitRepo hvdRepo = new GitRepo(name: "TestProject-Services.git",
	            url: "http://@[U]:@[P]@hv-repo/git/Utils.git",
			    username: "hudson",
			    password: "password" as char[]
	    )
	    save(hvdRepo)

	    GitRevision anyRevision = new GitRevision()
	    anyRevision.sourceRepository = hvdRepo
//	    anyRevision.setRevision("51c2c3fcb5c60c0532205347e9469585baab1a67")
	    anyRevision.setRevision("6de89c6c2665b3f5ad5258642967e4cf1f2685f7")
	    save(anyRevision)

	    JenkinsServerInstance jenkinsServerInstance =
			    new JenkinsServerInstance(name: "Main Jenkins",
			    serverURL: "http://jenkins/",
				username: "hudson",
					    password: "password" as char[])
	    save(jenkinsServerInstance)

	    JenkinsJobConfiguration jobConfiguration =
			    new JenkinsJobConfiguration(name: "TestProjectUtils", //name: "Does Nothing",
			    serverInstance: jenkinsServerInstance)
	    jobConfiguration.setArtefactPattern(/\.zip$/)
	    save(jobConfiguration)

	    JenkinsJobConfiguration qaDeployPattern = new JenkinsJobConfiguration(name: /QA - Server - Deploy.*/,
	        serverInstance: jenkinsServerInstance)
	    save(qaDeployPattern)

	    JobSet qaDeployJobs = new JobSet(jobConfiguration: qaDeployPattern)
	    save(qaDeployJobs)

	    JenkinsJobConfiguration qaJob1 = new JenkinsJobConfiguration(name: "Ranorex - Functionality", serverInstance: jenkinsServerInstance);
	    save(qaJob1)

	    ReversioningTarget reversioningTarget = new GradleModel(modelLocation: "/build.gradle")
	    save(reversioningTarget)

	    Project project = new Project(name: "TestProject Server",
	        sourceRepository: hvdRepo,
	    )
//	    project.setArtefactPattern(/(.*)?Service(.*)\.zip$/)
	    project.addToBuildJobs(jobConfiguration)
	    project.setQaDeployment(qaDeployJobs)
	    project.addToQaJobs(qaJob1)
	    project.setReversioningTarget(reversioningTarget)
	    save(project)

	    SimpleVersion release1 = new SimpleVersion(major: 1, minor: 0, patch: 1)
	    SimpleVersion snap1 = new SimpleVersion(major: 1, minor: 1, patch: 0, preRelease: "SNAPSHOT")
	    save(release1)
	    save(snap1)

	    Release release = new Release(name: "Test Release 1",
			    project: project,
			    revision: anyRevision,
			    releaseVersion: release1,
			    nextSnapshotVersion: snap1
	    )
	    save(release)

	    GitRepo guiRepo = new GitRepo(name: "GUI2.git",
			    url: "http://@[U]:@[P]@hv-repo/git/GUI2.git",
			    username: "hudson",
			    password: "password" as char[]
	    )
	    save(guiRepo)

	    JenkinsJobConfiguration guiBuild =
			    new JenkinsJobConfiguration(name: "GUI2-Git",
					    serverInstance: jenkinsServerInstance)
	    jobConfiguration.setArtefactPattern(/^com\.thecitysecret\.midas\.TestHarness\/bin\/Debug\/.*/)
	    save(guiBuild)

	    JenkinsJobConfiguration qaDeployPatternGUI = new JenkinsJobConfiguration(name: /QA - Deploy GUI to Ranorex/,
			    serverInstance: jenkinsServerInstance)
	    save(qaDeployPatternGUI)

	    JobSet qaDeployJobsGUI = new JobSet(jobConfiguration: qaDeployPatternGUI)
	    save(qaDeployJobsGUI)

	    ReversioningTarget reversioningTargetGUI = new POMModel(modelLocation: "/build/pom.xml")
	    save(reversioningTargetGUI)



	    Project gui = new Project()
	    gui.name = "TestProject GUI"
	    gui.sourceRepository = guiRepo
	    gui.addToBuildJobs(guiBuild)
	    gui.qaDeployment = qaDeployJobsGUI
	    gui.addToQaJobs(qaJob1)
	    gui.reversioningTarget = reversioningTargetGUI
	    save(gui)




    }

	def save = {
		it.save(flush: true, failOnError: true)
	}

	def destroy = {
	}
}
