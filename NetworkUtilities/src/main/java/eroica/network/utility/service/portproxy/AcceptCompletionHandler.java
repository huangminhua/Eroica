package eroica.network.utility.service.portproxy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import eroica.network.utility.domain.portproxy.PortProxyConnectionInformation;
import eroica.network.utility.domain.portproxy.PortProxyServerInformation;
import eroica.network.utility.util.ChannelUtils;

import lombok.extern.slf4j.Slf4j;

/**
 * Operations after acception. Connect to destination server.
 * 
 * @author Minhua HUANG
 *
 */
@Slf4j
public class AcceptCompletionHandler
		implements CompletionHandler<AsynchronousSocketChannel, PortProxyServerInformation> {
	private AcceptCompletionHandler() {
	}

	// Globally only one AcceptCompletionHandler object is available.
	// It's thread safe because it has no local variable.
	private static AcceptCompletionHandler GLOBAL_INSTANCE = new AcceptCompletionHandler();

	public static AcceptCompletionHandler getInstance() {
		return GLOBAL_INSTANCE;
	}

	@Override
	public void completed(AsynchronousSocketChannel downChannel, PortProxyServerInformation serverInfo) {
		try {
			log.info("Accepted a new connection from " + downChannel.getRemoteAddress());
		} catch (IOException e) {
		}
		// accept another connection
		serverInfo.getServerChannel().accept(serverInfo, this);
		// open an asynchronous socket channel to the destination server
		AsynchronousSocketChannel upChannel = null;
		try {
			upChannel = AsynchronousSocketChannel.open();
			upChannel.bind(null);
		} catch (Throwable t) {
			log.info("An error occured when connecting the target server.", t);
			ChannelUtils.closeChannel(upChannel);
			ChannelUtils.closeChannel(downChannel);
			return;
		}
		InetSocketAddress upSocketAddress = new InetSocketAddress(serverInfo.getConnectAddress(),
				serverInfo.getConnectPort());
		PortProxyConnectionInformation connectionInfo = new PortProxyConnectionInformation(serverInfo, downChannel,
				upChannel);
		upChannel.connect(upSocketAddress, connectionInfo, ConnectCompletionHandler.getInstance());
	}

	@Override
	public void failed(Throwable exc, PortProxyServerInformation portProxyInformation) {
		log.info("Failed to accept from " + portProxyInformation.getListenAddress() + ":"
				+ portProxyInformation.getListenPort() + ". The server maybe shutdown.", exc);
	}

}
