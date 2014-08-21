package net.poundex.releaseman.util
import net.poundex.releaseman.Release
import org.springframework.integration.annotation.Gateway
/**
 * Created by poundex on 29/11/13.
 */
public interface ReleaseServiceGateway
{
	/**
	 * Make a release by pushing the configured Release into the make release flow
	 * @param release The release to make
	 */
	@Gateway(requestChannel = "makeReleaseChannel")
	void makeRelease(Release release)
}
