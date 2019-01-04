package eroica.network.utility.entity.portproxy;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

import eroica.network.utility.entity.StreamType;
import eroica.network.utility.util.ChannelUtils;

import lombok.Getter;

/**
 * Port proxy connection information. A connection here consists of a down
 * channel from the client to this application and a up channel from this
 * application to destination server.
 * 
 * @author Minhua HUANG
 *
 */
@Getter
public class PortProxyConnectionInformation {
	private static final int BUFFER_SIZE = 1024 * 128;// the size of ByteBuffer
	private PortProxyServerInformation portProxyInformation;
	// Please don't confuse upBuffer with upChannel and downBuffer with downChannel.
	// the buffer used by upstream operations
	private final ByteBuffer upBuffer = ByteBuffer.allocate(BUFFER_SIZE);
	// the buffer used by downstream operations
	private final ByteBuffer downBuffer = ByteBuffer.allocate(BUFFER_SIZE);
	// the socket connection between this application and the destination server
	private AsynchronousSocketChannel upChannel;
	// the socket connection between the client and this application
	private AsynchronousSocketChannel downChannel;
	// the running status of the up stream
	private boolean upStreamOn = false;
	// the running status of the down stream
	private boolean downStreamOn = false;

	public PortProxyConnectionInformation(PortProxyServerInformation portProxyInformation,
			AsynchronousSocketChannel downChannel, AsynchronousSocketChannel upChannel) {
		this.portProxyInformation = portProxyInformation;
		this.downChannel = downChannel;
		this.upChannel = upChannel;
	}

	public void turnStreamOn() {
		this.upStreamOn = true;
		this.downStreamOn = true;
	}

	void turnStreamOff(StreamType streamType) {
		if (streamType == StreamType.DOWN)
			this.downStreamOn = false;
		else if (streamType == StreamType.UP)
			this.upStreamOn = false;
	}
	
	public void close() {
		ChannelUtils.closeChannel(upChannel);
		ChannelUtils.closeChannel(downChannel);
	}
}
