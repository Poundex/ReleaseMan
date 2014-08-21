package net.poundex.releaseman.util

import org.springframework.integration.Message
import org.springframework.integration.MessageChannel
import org.springframework.integration.channel.interceptor.ChannelInterceptorAdapter

/**
 * Created by poundex on 29/11/13.
 */
public class DirtyObjectGuardian extends ChannelInterceptorAdapter
{
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel)
	{
		try
		{
			List dirtyProperties = message.payload.getDirtyPropertyNames()
			if(!dirtyProperties.isEmpty())
			{
				throw new DirtyObjectException(message, channel)
			}
		}
		catch(MissingMethodException mmex)
		{
			//Do nothing
		}
		return message
	}
}
