package eroica.network.utility.util;

import java.nio.channels.Channel;

import lombok.extern.slf4j.Slf4j;

/**
 * Channel utils.
 * 
 * @author Minhua HUANG
 *
 */
@Slf4j
public class ChannelUtils {
	
	public static void closeChannel(Channel channel) {
		if (channel != null && channel.isOpen())
			try {
				channel.close();
			} catch (Throwable t) {
				log.warn("Close channel[" + channel + "] error. But nothing to remedy.", t);
			}
	}
}
