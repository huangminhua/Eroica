package eroica.network.utility.service.portproxy;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import eroica.network.utility.entity.portproxy.PortProxyStreamInformation;
import lombok.extern.slf4j.Slf4j;

/**
 * Operations after reading. Dependent on byte count read.
 * 
 * @author Minhua HUANG
 *
 */
@Slf4j
public class ReadCompletionHandler implements CompletionHandler<Integer, PortProxyStreamInformation> {
	private ReadCompletionHandler() {		
	}

	// Globally only one ReadCompletionHandler object is available.
	// It's thread safe because it has no local variable.
	private static ReadCompletionHandler GLOBAL_INSTANCE = new ReadCompletionHandler();

	public static ReadCompletionHandler getInstance() {
		return GLOBAL_INSTANCE;
	}

	@Override
	public void completed(Integer result, PortProxyStreamInformation streamInfo) {
		if (result.intValue() < 0)
			// the channel has reached end-of-stream, turn off the stream line (the input
			// stream of this side and the output stream of the other side)
			streamInfo.turnOff();
		else if (result.intValue() == 0) // no data read, continue reading
			streamInfo.getInputSource().read(streamInfo.getBuffer(), streamInfo, this);
		else // some data have been read, continue to write
//			logger.info("Read: " + buffer.toString() + "----" + new String(buffer.array()));
			streamInfo.getOutputDestination().write((ByteBuffer) streamInfo.getBuffer().flip(), streamInfo,
					WriteCompletionHandler.getInstance());
	}

	@Override
	public void failed(Throwable exc, PortProxyStreamInformation streamInfo) {
		AsynchronousSocketChannel channel = streamInfo.getInputSource();
		try {
			log.warn("Failed to read from " + channel.getRemoteAddress() + ", so that the stream is to be shut down.",
					exc);
		} catch (IOException e) {
			log.warn("Failed to read (channel info unavailable[" + e.getMessage()
					+ "]), so that the stream is to be shut down.", exc);
		}
		streamInfo.turnOff();
	}
}
