package net.poundex.releaseman.exception

/**
 * Created by poundera on 21/03/14.
 */
class CIException extends RuntimeException
{
	CIException(String s)
	{
		super(s)
	}

	CIException(String s, Throwable throwable)
	{
		super(s, throwable)
	}
}
