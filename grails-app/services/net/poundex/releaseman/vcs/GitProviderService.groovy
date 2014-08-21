package net.poundex.releaseman.vcs

import net.poundex.releaseman.exception.VCSException
import net.poundex.releaseman.util.CLIOutput
import net.poundex.releaseman.vcs.git.GitRepo
import net.poundex.releaseman.vcs.git.GitRevision
import net.poundex.releaseman.vcs.git.GitWorkingCopy

class GitProviderService implements VersionControlProvider<GitRepo, GitRevision, GitWorkingCopy>
{

	@Override
	GitWorkingCopy checkout(GitRepo sourceRepository, GitRevision revision, File workingCopyLocation)
	{
		log.info("Cloning from ${sourceRepository.url} into ${workingCopyLocation.absolutePath}")
		CLIOutput clone = GitCLI.clone(sourceRepository.urlWithAuth(), workingCopyLocation)
		assertSuccess(clone, "Git clone of ${sourceRepository.url} into ${workingCopyLocation.absolutePath} failed")

		GitCLI.checkout(workingCopyLocation, revision.hash)

		GitWorkingCopy workingCopy = new GitWorkingCopy(
				sourceRepository: sourceRepository,
				location: workingCopyLocation,
				revision: revision
		)
		return workingCopy
	}

	@Override
	GitRevision checkin(GitWorkingCopy workingCopy, String message)
	{
		log.info("Checking in working copy from ${workingCopy.location.absolutePath}")
		CLIOutput commit = GitCLI.commit(workingCopy.location.absoluteFile, message)
		assertSuccess(commit, "Git commit failed")
		
		CLIOutput push = GitCLI.push(workingCopy.location.absoluteFile)
		assertSuccess(push, "Git push failed")

		CLIOutput revHash = GitCLI.getCurrentRevision(workingCopy.location.absoluteFile)
		assertSuccess(revHash, "Couldn't get current revision hash")

		return currentRevision(workingCopy).save()
	}

	@Override
	GitRevision tag(GitWorkingCopy workingCopy, String tagName, String tagMessage)
	{
		log.info("Tagging working copy at ${workingCopy.location.absolutePath} as ${tagName}")

		CLIOutput tag = GitCLI.tag(workingCopy.location.absoluteFile, tagName, tagMessage)
		assertSuccess(tag, "Tagging failed")

		CLIOutput push = GitCLI.push(workingCopy.location.absoluteFile, tagName)
		assertSuccess(push, "Pushing tag failed")

		return currentRevision(workingCopy).save()

	}

	@Override
	GitRevision currentRevision(GitWorkingCopy workingCopy)
	{
		CLIOutput revHash = GitCLI.getCurrentRevision(workingCopy.location.absoluteFile)
		assertSuccess(revHash, "Couldn't get current revision hash")
		return new GitRevision(revision: revHash.text.trim(),
				sourceRepository: workingCopy.sourceRepository)
	}

	@Override
	void clean(GitWorkingCopy workingCopy)
	{
		assertSuccess(GitCLI.reset(workingCopy.location.absoluteFile), "Couldn't reset Git working copy")
		assertSuccess(GitCLI.clean(workingCopy.location.absoluteFile), "Couldn't clean Git working copy")
		assertSuccess(GitCLI.checkout(workingCopy.location.absoluteFile, "master"), "Couldn't point Git working copy to master")
	}

	@Override
	GitRevision getRevision(GitRepo sourceRepository)
	{
		return new GitRevision(sourceRepository: sourceRepository)
	}

	private void assertSuccess(CLIOutput output, String orError)
	{
		if(output.code != 0)
		{
			throw new VCSException(orError, output.text)
		}
	}
}
