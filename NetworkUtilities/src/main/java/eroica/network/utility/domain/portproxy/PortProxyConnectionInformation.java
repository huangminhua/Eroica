package eroica.network.utility.domain.portproxy;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;

import org.apache.commons.pool2.ObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import eroica.network.utility.common.ByteBufferFactory;
import eroica.network.utility.domain.StreamType;
import eroica.network.utility.util.ChannelUtils;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * Port proxy connection information. A connection here consists of a down
 * channel from the client to this application and a up channel from this
 * application to destination server.
 * 
 * @author Minhua HUANG
 *
 */
@Getter
@Slf4j
public class PortProxyConnectionInformation {
	static {
		GenericObjectPoolConfig<ByteBuffer> gopc = new GenericObjectPoolConfig<>();
		gopc.setMaxTotal(2048);
		gopc.setMaxIdle(512);
		gopc.setMinIdle(32);
		BYTE_BUFFER_POOL = new GenericObjectPool<>(new ByteBufferFactory(), gopc);
	}
	private static final ObjectPool<ByteBuffer> BYTE_BUFFER_POOL;
	private final PortProxyServerInformation serverInfo;
	// Please don't confuse upBuffer with upChannel and downBuffer with downChannel.
	// the buffer used by upstream operations
	private ByteBuffer upBuffer;
	private boolean upBufferBorrowed = false;
	// the buffer used by downstream operations
	private ByteBuffer downBuffer;
	private boolean downBufferBorrowed = false;
	// the socket connection between this application and the destination server
	private final AsynchronousSocketChannel upChannel;
	// the socket connection between the client and this application
	private final AsynchronousSocketChannel downChannel;
	// the running status of the up stream
	private boolean upStreamOn = false;
	// the running status of the down stream
	private boolean downStreamOn = false;

	public PortProxyConnectionInformation(PortProxyServerInformation serverInfo, AsynchronousSocketChannel downChannel,
			AsynchronousSocketChannel upChannel) {
		this.serverInfo = serverInfo;
		this.downChannel = downChannel;
		this.upChannel = upChannel;
	}

	public void turnStreamOn() {
		this.downStreamOn = true;
		this.upStreamOn = true;
		try {
			downBuffer = BYTE_BUFFER_POOL.borrowObject();
			log.info("Borrowed downBuffer " + downBuffer + " from BYTE_BUFFER_POOL.");
			downBufferBorrowed = true;
		} catch (Exception e) {
			downBufferBorrowed = false;
			log.warn("Fail to borrow downBuffer from BYTE_BUFFER_POOL. To allocate a heap buffer instead.", e);
			downBuffer = ByteBuffer.allocate(ByteBufferFactory.BUFFER_SIZE);
		}
		try {
			upBuffer = BYTE_BUFFER_POOL.borrowObject();
			log.info("Borrowed upBuffer " + upBuffer + " from BYTE_BUFFER_POOL.");
			upBufferBorrowed = true;
		} catch (Exception e) {
			upBufferBorrowed = false;
			log.warn("Fail to borrow upBuffer from BYTE_BUFFER_POOL. To allocate a heap buffer instead.", e);
			upBuffer = ByteBuffer.allocate(ByteBufferFactory.BUFFER_SIZE);
		}
	}

	void turnStreamOff(StreamType streamType) {
		if (streamType == StreamType.DOWN) {
			this.downStreamOn = false;
			if (downBufferBorrowed) {
				try {
					BYTE_BUFFER_POOL.returnObject(downBuffer);
					log.debug("DownBuffer " + downBuffer + " has been returned to BYTE_BUFFER_POOL.");
					downBuffer = null;
				} catch (Exception e) {
					log.warn("DownBuffer " + downBuffer + " cannot be returned to BYTE_BUFFER_POOL.", e);
				}
			}
		} else if (streamType == StreamType.UP) {
			this.upStreamOn = false;
			if (upBufferBorrowed) {
				try {
					BYTE_BUFFER_POOL.returnObject(upBuffer);
					log.debug("UpBuffer " + upBuffer + " has been returned to BYTE_BUFFER_POOL.");
					upBuffer = null;
				} catch (Exception e) {
					log.warn("UpBuffer " + upBuffer + " cannot be returned to BYTE_BUFFER_POOL.", e);
				}
			}
		}
	}

	public void close() {
		ChannelUtils.closeChannel(upChannel);
		ChannelUtils.closeChannel(downChannel);
	}
}
