package eroica.network.utility.service;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import eroica.network.utility.domain.ServerInformation;

/**
 * The basic service of this application. It manages a list of registered
 * servers. Other applications should extend this Class.
 * 
 * @author Minhua HUANG
 *
 */
public class BaseService {
	private static final Queue<ServerInformation> SERVERS = new ConcurrentLinkedQueue<>();

	/**
	 * Register server information to SERVERS queue. Called by extenders.
	 * 
	 * @param server
	 */
	protected void registerServer(ServerInformation server) {
		SERVERS.add(server);
	}

	/**
	 * Find server information from SERVERS queue.
	 * 
	 * @param listenAddress
	 * @param listenPort
	 * @return
	 */
	public ServerInformation findRegisterdServer(InetAddress listenAddress, int listenPort) {
		for (ServerInformation server : SERVERS) {
			if (server.getListenAddress().equals(listenAddress) && server.getListenPort() == listenPort)
				return server;
		}
		return null;
	}

	/**
	 * Unregister server information from SERVERS queue. Called by extenders.
	 * 
	 * @param listenAddress
	 * @param listenPort
	 * @return
	 */
	protected ServerInformation unregisterServer(InetAddress listenAddress, int listenPort) {
		ServerInformation server = findRegisterdServer(listenAddress, listenPort);
		if (server == null)
			return null;
		SERVERS.remove(server);
		return server;
	}

	/**
	 * List servers in SERVERS queue.
	 * 
	 * @return
	 */
	public List<ServerInformation> listRegisteredServers() {
		return new ArrayList<ServerInformation>(SERVERS);
	}

	/**
	 * Count servers in SERVERS queue.
	 * 
	 * @return
	 */
	public int getRegisteredServerCount() {
		return SERVERS.size();
	}
}
