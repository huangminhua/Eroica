package eroica.network.utility.service.portproxy;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import eroica.network.utility.domain.portproxy.PortProxyStreamInformation;
import lombok.extern.slf4j.Slf4j;

/**
 * Operations after writing. Dependent on byte count written.
 * 
 * @author Minhua HUANG
 *
 */
@Slf4j
public class WriteCompletionHandler implements CompletionHandler<Integer, PortProxyStreamInformation> {
	private WriteCompletionHandler() {
	}
	
	// Globally only one ReadCompletionHandler object is available.
	// It's thread safe because it has no local variable.
	private static WriteCompletionHandler GLOBAL_INSTANCE = new WriteCompletionHandler();

	public static WriteCompletionHandler getInstance() {
		return GLOBAL_INSTANCE;
	}

	@Override
	public void completed(Integer result, PortProxyStreamInformation streamInfo) {
		ByteBuffer buffer = streamInfo.getBuffer();
		if (buffer.remaining() > 0)// data are not written off yet, continue writing
			streamInfo.getOutputDestination().write(buffer, streamInfo, this);
		else // data are written off, continue to read
//			logger.info("Wrote: " + buffer.toString() + "----" + new String(buffer.array()));
			streamInfo.getInputSource().read((ByteBuffer) buffer.clear(), streamInfo,
					ReadCompletionHandler.getInstance());
	}

	@Override
	public void failed(Throwable exc, PortProxyStreamInformation streamInfo) {
		AsynchronousSocketChannel channel = streamInfo.getOutputDestination();
		try {
			log.info("Failed to write to " + channel.getRemoteAddress() + ", so that the stream is to be shut down.",
					exc);
		} catch (IOException e) {
			log.info("Failed to write (channel info unavailable[" + e.getMessage()
					+ "]), so that the stream is to be shut down.", exc);
		}
		streamInfo.turnOff();
	}
}
