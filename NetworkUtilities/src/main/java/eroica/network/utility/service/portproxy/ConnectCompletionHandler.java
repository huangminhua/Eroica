package eroica.network.utility.service.portproxy;

import java.nio.channels.CompletionHandler;

import eroica.network.utility.domain.StreamType;
import eroica.network.utility.domain.portproxy.PortProxyConnectionInformation;
import eroica.network.utility.domain.portproxy.PortProxyStreamInformation;
import lombok.extern.slf4j.Slf4j;

/**
 * Operations after connection. Read from both down channel and up channel.
 * 
 * @author Minhua HUANG
 *
 */
@Slf4j
public class ConnectCompletionHandler implements CompletionHandler<Void, PortProxyConnectionInformation> {
	private ConnectCompletionHandler() {
	}

	// Globally only one ConnectCompletionHandler object is available.
	// It's thread safe because it has no local variable.
	private static ConnectCompletionHandler GLOBAL_INSTANCE = new ConnectCompletionHandler();

	public static ConnectCompletionHandler getInstance() {
		return GLOBAL_INSTANCE;
	}

	@Override
	public void completed(Void result, PortProxyConnectionInformation connInfo) {
		// set the status of connInfo to on
		connInfo.turnStreamOn();
		// read from down channel
		connInfo.getDownChannel().read(connInfo.getUpBuffer(), new PortProxyStreamInformation(connInfo, StreamType.UP),
				ReadCompletionHandler.getInstance());
		// read from up channel
		connInfo.getUpChannel().read(connInfo.getDownBuffer(),
				new PortProxyStreamInformation(connInfo, StreamType.DOWN), ReadCompletionHandler.getInstance());
	}

	@Override
	public void failed(Throwable exc, PortProxyConnectionInformation connInfo) {
		log.info("Failed to connect to " + connInfo.getServerInfo().getConnectAddress() + ":"
				+ connInfo.getServerInfo().getConnectPort() + ", so that the connection is to be closed.", exc);
		connInfo.close();
	}

}
