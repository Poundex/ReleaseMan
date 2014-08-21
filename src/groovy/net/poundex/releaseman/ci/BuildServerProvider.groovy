package net.poundex.releaseman.ci

import net.poundex.releaseman.vcs.Revision

/**
 * Created by poundex on 02/12/13.
 */
public interface BuildServerProvider<M extends JobConfiguration, N extends CIBuild, P extends CIServerInstance>
{
	List<M> getAllJobs(P instance)
	N build(M job)
	N updateBuild(N build)
	M createJobFor(M jobConfiguration, Revision revision)
	void deleteJob(M jobConfiguration)
	N deployJobWithArtefacts(M m, List<String> artefacts)
}