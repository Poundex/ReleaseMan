package net.poundex.releaseman.versions
import com.github.zafarkhaja.semver.Version
import org.springframework.util.StringUtils

class SimpleVersion implements Comparable<SimpleVersion>
{
	int major
	int minor
	int patch
	String preRelease
	String metadata

	static constraints = {
		major()
		minor()
		patch()
		preRelease(nullable: true, blank: true)
		metadata(nullable: true, blank: true)
	}

	Version asVersion()
	{
		Version.Builder builder = new Version.Builder("${major}.${minor}.${patch}")
		if(StringUtils.hasText(preRelease))
		{
			builder.preReleaseVersion = preRelease
		}
		if(StringUtils.hasText(metadata))
		{
			builder.buildMetadata = metadata
		}
		return builder.build()
	}

	static SimpleVersion from(String versionString)
	{
		return from(Version.valueOf(versionString))
	}

	static SimpleVersion from(Version version)
	{
		return new SimpleVersion(
				major: version.majorVersion,
				minor: version.minorVersion,
				patch: version.patchVersion,
				preRelease: version.preReleaseVersion,
				metadata: version.buildMetadata
		)
	}

	public String toString()
	{
		asVersion().toString()
	}

	@Override
	int compareTo(SimpleVersion o)
	{
		return asVersion().compareTo(o.asVersion())
	}
}
