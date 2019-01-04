package eroica.network.utility.entity;

import java.net.InetAddress;
import java.util.Date;

import lombok.Getter;

/**
 * Basic information of a server. It should be extended by miscellaneous
 * servers.
 * 
 * @author Minhua HUANG
 *
 */
@Getter
public class ServerInformation {
	private final InetAddress listenAddress;
	private final int listenPort;
	private final Date createDate;

	@Override
	public String toString() {
		return "Service:[ListenAddress:" + listenAddress + ", ListenPort:" + listenPort + ", CreateDate:" + createDate
				+ "]";
	}

	public ServerInformation(InetAddress listenAddress, int listenPort, Date createDate) {
		this.listenAddress = listenAddress;
		this.listenPort = listenPort;
		this.createDate = createDate;
	}
}
