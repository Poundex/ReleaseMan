package net.poundex.releaseman.ci

import net.poundex.releaseman.vcs.git.GitRevision
/**
 * Created by poundera on 18/03/14.
 */
class JenkinsUtil
{
	public static String createRevisionJob(String jobXML, GitRevision revision)
	{
		def root = new XmlParser().parseText(jobXML)
		def branchNode = root.scm.branches.'hudson.plugins.git.BranchSpec'.name[0]
		branchNode.value = revision.revision
		StringWriter writer = new StringWriter()
		def printer = new XmlNodePrinter(new PrintWriter(writer))
		printer.preserveWhitespace = true
		printer.print(root)
		return writer.toString()
	}
}
