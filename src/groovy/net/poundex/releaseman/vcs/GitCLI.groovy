package net.poundex.releaseman.vcs
import net.poundex.releaseman.util.CLIOutput
import org.springframework.util.StringUtils

/**
 * Created by poundera on 21/03/14.
 */
class GitCLI
{
	public static CLIOutput clone(String url, File dir)
	{
		if(!dir.isDirectory())
		{
			dir.mkdir()
		}

		return runGitCommand(dir, "clone", url, ".")
	}

	public static CLIOutput checkout(File dir, String what)
	{
		verifyRepo(dir)
		return runGitCommand(dir, "checkout", what)
	}

	public static CLIOutput commit(File workingCopyLocation, String message)
	{
		verifyRepo(workingCopyLocation)
		return runGitCommand(workingCopyLocation, "commit", "--allow-empty", "-a", "-m", message)
	}

	public static CLIOutput push(File workingCopyLocation, String what = "")
	{
		verifyRepo(workingCopyLocation)
		return runGitCommand(workingCopyLocation, "push", "origin", "master", what)
	}

	public static CLIOutput getCurrentRevision(File workingCopyLocation)
	{
		verifyRepo(workingCopyLocation)
		return runGitCommand(workingCopyLocation, "rev-parse", "HEAD")
	}

	public static CLIOutput tag(File workingCopyLocation, String tagName, String tagMessage)
	{
		verifyRepo(workingCopyLocation)
		return runGitCommand(workingCopyLocation, "tag", "-a", tagName, "-m", tagMessage)
	}

	public static CLIOutput reset(File workingCopyLocation)
	{
		verifyRepo(workingCopyLocation)
		return runGitCommand(workingCopyLocation, "reset", "--hard")
	}

	public static CLIOutput clean(File workingCopyLocation)
	{
		verifyRepo(workingCopyLocation)
		return runGitCommand(workingCopyLocation, "clean", "-f", "-d")
	}

	private static void verifyRepo(File dir)
	{
		if(!dir.isDirectory() || !(new File(dir, ".git").isDirectory()))
		{
			throw new IllegalArgumentException("No git repo at ${dir.absolutePath}")
		}
	}

	private static CLIOutput runGitCommand(File workingCopyLocation, String... args)
	{
		List<String> commandBits = ["git"]
		commandBits.addAll args.findAll { StringUtils.hasText(it) }

		ProcessBuilder pb = new ProcessBuilder(commandBits).directory(workingCopyLocation.absoluteFile)
		Process p = pb.start()
		StringWriter output = new StringWriter()
		p.consumeProcessOutput(output, output)
		int exitCode = p.waitFor()
		return new CLIOutput(code: exitCode, text: output.toString())
	}
}
