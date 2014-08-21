package net.poundex.releaseman.vcs
/**
 * Created by poundex on 02/12/13.
 */
public interface VersionControlProvider<M extends SourceRepository, N extends Revision<?>, P extends WorkingCopy>
{
	P checkout(M sourceRepository, N revision, File workingCopyLocation)
	N checkin(P workingCopy, String message)
	N tag(P workingCopy, String tagName, String tagMessage)
	N currentRevision(P workingCopy)
	void clean(P workingCopy)
	N getRevision(M sourceRepository)
}