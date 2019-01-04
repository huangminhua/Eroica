package eroica.network.utility.service.portproxy;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;

import eroica.network.utility.entity.portproxy.PortProxyConnectionInformation;
import eroica.network.utility.entity.portproxy.PortProxyServerInformation;
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
	public void completed(AsynchronousSocketChannel downChannel, PortProxyServerInformation attachment) {
		// accept another connection
		attachment.getServerChannel().accept(attachment, this);
		// open an asynchronous socket channel to the destination server
		AsynchronousSocketChannel upChannel = null;
		try {
			upChannel = AsynchronousSocketChannel.open();
			upChannel.bind(null);
		} catch (Throwable t) {
			log.error("An error occured when connecting the target server.", t);
			ChannelUtils.closeChannel(upChannel);
			ChannelUtils.closeChannel(downChannel);
			return;
		}
		InetSocketAddress upSocketAddress = new InetSocketAddress(attachment.getConnectAddress(),
				attachment.getConnectPort());
		PortProxyConnectionInformation connectionInfo = new PortProxyConnectionInformation(attachment, downChannel,
				upChannel);
		upChannel.connect(upSocketAddress, connectionInfo, ConnectCompletionHandler.getInstance());
	}

	@Override
	public void failed(Throwable exc, PortProxyServerInformation attachment) {
		log.warn("Failed to accept from " + attachment.getListenAddress() + ":" + attachment.getListenPort()
				+ ". The server maybe shutdown.", exc);
	}

}
