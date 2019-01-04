package eroica.network.utility.entity.portproxy;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

import eroica.network.utility.entity.StreamType;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Port Proxy (UP/DOWN) Stream Information.
 * 
 * @author Minhua HUANG
 *
 */
@Getter
@Slf4j
public class PortProxyStreamInformation {
	private PortProxyConnectionInformation connInfo;
	private StreamType streamType;

	public PortProxyStreamInformation(PortProxyConnectionInformation connInfo, StreamType streamType) {
		this.connInfo = connInfo;
		this.streamType = streamType;
	}

	/**
	 * Get the input source of this stream.
	 * 
	 * @return
	 */
	public AsynchronousSocketChannel getInputSource() {
		if (StreamType.UP == this.getStreamType())
			return this.getConnInfo().getDownChannel();
		else if (StreamType.DOWN == this.getStreamType())
			return this.getConnInfo().getUpChannel();
		else
			return null;
	}

	/**
	 * Get the output destination of this stream.
	 * 
	 * @return
	 */
	public AsynchronousSocketChannel getOutputDestination() {
		if (StreamType.UP == this.getStreamType())
			return this.getConnInfo().getUpChannel();
		else if (StreamType.DOWN == this.getStreamType())
			return this.getConnInfo().getDownChannel();
		else
			return null;
	}

	/**
	 * Get the ByteBuffer used by this stream.
	 * 
	 * @return
	 */
	public ByteBuffer getBuffer() {
		if (StreamType.UP == this.getStreamType())
			return this.getConnInfo().getUpBuffer();
		else if (StreamType.DOWN == this.getStreamType())
			return this.getConnInfo().getDownBuffer();
		else
			return null;
	}

	/**
	 * Turn off this route of stream (the pair of input/output stream). If both
	 * streams are shutdown, the channels are to be closed.
	 */
	public void turnOff() {
		connInfo.turnStreamOff(this.getStreamType());
		if (this.getInputSource() != null && this.getInputSource().isOpen())
			try {
				this.getInputSource().shutdownInput();
			} catch (IOException e) {
				log.warn("Shutdown input [" + this.getInputSource() + "] error. But nothing to remedy.", e);
			}
		if (this.getOutputDestination() != null && this.getOutputDestination().isOpen())
			try {
				this.getOutputDestination().shutdownOutput();
			} catch (IOException e) {
				log.warn("Shutdown output [" + this.getOutputDestination() + "] error. But nothing to remedy.", e);
			}
		if (!connInfo.isDownStreamOn() && !connInfo.isUpStreamOn())
			connInfo.close();
	}
}
