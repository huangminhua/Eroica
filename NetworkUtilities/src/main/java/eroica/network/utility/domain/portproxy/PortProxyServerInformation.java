package eroica.network.utility.domain.portproxy;

import java.net.InetAddress;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.Date;

import eroica.network.utility.domain.ServerInformation;
import eroica.network.utility.util.ChannelUtils;

import lombok.Getter;

/**
 * Port proxy server information, namely, the local server socket information
 * and the destination server information.
 * 
 * @author Minhua HUANG
 *
 */
@Getter
public class PortProxyServerInformation extends ServerInformation {
	private final InetAddress connectAddress;
	private final int connectPort;
	private AsynchronousServerSocketChannel serverChannel;

	public PortProxyServerInformation(InetAddress listenAddress, int listenPort, InetAddress connectAddress,
			int connectPort, Date createDate) {
		super(listenAddress, listenPort, createDate);
		this.connectAddress = connectAddress;
		this.connectPort = connectPort;
	}

	public void setServerChannel(AsynchronousServerSocketChannel serverChannel) {
		if (this.serverChannel == null)
			this.serverChannel = serverChannel;
		else
			throw new UnsupportedOperationException("The server channel was already set. You cannot set another.");
	}

	@Override
	public String toString() {
		return "PortProxy[ListenAddress:" + getListenAddress() + ", ListenPort:" + getListenPort() + ", ConnectAddress:"
				+ connectAddress + ", ConnectPort:" + connectPort + ", CreateDate:" + getCreateDate() + "]";
	}

	public void close() {
		ChannelUtils.closeChannel(serverChannel);
	}

}
