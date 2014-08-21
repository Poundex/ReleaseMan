package net.poundex.releaseman.exception

/**
 * Created by poundera on 21/03/14.
 */
class VCSException extends RuntimeException
{
	String details

	VCSException(String message, String details)
	{
		super(message)
		this.details = details
	}

	VCSException(String message, String details, Throwable throwable)
	{
		super(message, throwable)
		this.details = details
	}
}
