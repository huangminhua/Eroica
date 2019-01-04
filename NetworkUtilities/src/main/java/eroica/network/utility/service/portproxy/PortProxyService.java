package eroica.network.utility.service.portproxy;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousServerSocketChannel;

import eroica.network.utility.domain.ServerInformation;
import eroica.network.utility.domain.portproxy.PortProxyServerInformation;
import eroica.network.utility.service.BaseService;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * Port proxy server management.
 * 
 * @author Minhua HUANG
 *
 */
@Slf4j
@Component
public class PortProxyService extends BaseService {

	/**
	 * Establish a port proxy server.
	 * 
	 * @param serverInfo
	 * @return
	 * @throws Throwable
	 */
	public ServerInformation addServer(final PortProxyServerInformation serverInfo) throws Throwable {
		AsynchronousServerSocketChannel serverChannel = null;// server socket
		try {
			// bind the server socket
			InetSocketAddress listenSocketAddress = new InetSocketAddress(serverInfo.getListenAddress(),
					serverInfo.getListenPort());
			serverChannel = AsynchronousServerSocketChannel.open().bind(listenSocketAddress);
			serverInfo.setServerChannel(serverChannel);
			serverChannel.accept(serverInfo, AcceptCompletionHandler.getInstance());
			// register it to the servers queue
			registerServer(serverInfo);
			return serverInfo;
		} catch (Throwable t) {
			log.error("An error occured when adding a port proxy server.", t);
			// If an exception is thrown, the server channel should be closed.
			serverInfo.close();
			throw t;
		}
	}

	/**
	 * Remove a port proxy server.
	 * 
	 * @param listenAddress
	 * @param listenPort
	 * @return
	 */
	public ServerInformation deleteServer(InetAddress listenAddress, int listenPort) {
		// unregister the server and get the information of the server first
		PortProxyServerInformation serverInfo = (PortProxyServerInformation) unregisterServer(listenAddress,
				listenPort);
		// close the server channel
		if (serverInfo != null)
			serverInfo.close();
		return serverInfo;
	}
}
