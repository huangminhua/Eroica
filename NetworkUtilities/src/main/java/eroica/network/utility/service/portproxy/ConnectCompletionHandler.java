package eroica.network.utility.service.portproxy;

import java.nio.channels.CompletionHandler;

import eroica.network.utility.entity.StreamType;
import eroica.network.utility.entity.portproxy.PortProxyConnectionInformation;
import eroica.network.utility.entity.portproxy.PortProxyStreamInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Operations after connection. Read from both down channel and up channel.
 * 
 * @author Minhua HUANG
 *
 */
public class ConnectCompletionHandler implements CompletionHandler<Void, PortProxyConnectionInformation> {
	private static final Logger logger = LoggerFactory.getLogger(ConnectCompletionHandler.class);
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
		logger.error("Failed to connect to " + connInfo.getPortProxyInformation().getConnectAddress() + ":"
				+ connInfo.getPortProxyInformation().getConnectPort() + ", so that the connection is to be closed.",
				exc);
		connInfo.close();
	}

}
