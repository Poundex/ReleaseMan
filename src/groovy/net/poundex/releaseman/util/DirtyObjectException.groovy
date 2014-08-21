package net.poundex.releaseman.util

import org.springframework.integration.Message
import org.springframework.integration.MessageChannel

/**
 * Created by poundex on 29/11/13.
 */
public class DirtyObjectException extends RuntimeException
{
	public DirtyObjectException(Message<?> message, MessageChannel channel)
	{
		super("Message:\n${message}\nCarrying Payload:\n${message.payload}\n" +
				"was passed in a dirty state into channel:\n${channel}")
	}
}
