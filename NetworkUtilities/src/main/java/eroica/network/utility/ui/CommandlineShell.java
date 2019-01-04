package eroica.network.utility.ui;

import java.net.InetAddress;
import java.util.Date;

import eroica.network.utility.entity.ServerInformation;
import eroica.network.utility.entity.portproxy.PortProxyServerInformation;
import eroica.network.utility.service.portproxy.PortProxyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

/**
 * CLI of this application.
 * 
 * @author Minhua HUANG
 *
 */
@ShellComponent
public class CommandlineShell {

	private final PortProxyService portProxyService;

	@Autowired
	public CommandlineShell(PortProxyService portProxyService) {
		this.portProxyService = portProxyService;
	}

	@ShellMethod("Add a port proxy.")
	public String portproxyadd(@ShellOption() InetAddress listenaddress, @ShellOption() int listenport,
			@ShellOption() InetAddress connectaddress, @ShellOption() int connectport) {
		ServerInformation server = portProxyService.findRegisterdServer(listenaddress, listenport);
		if (server == null) {
			PortProxyServerInformation newService = new PortProxyServerInformation(listenaddress, listenport, connectaddress,
					connectport, new Date());
			try {
				portProxyService.addServer(newService);
				return "A new port proxy service added:\n" + newService;
			} catch (Throwable e) {
				return "An error occured. " + e.getMessage();
			}
		} else
			return listenaddress + ":" + listenport + " is already bound. You shall delete it first.\n" + server;
	}

	@ShellMethod("Delete a port proxy.")
	public String portproxydelete(@ShellOption() InetAddress listenaddress, @ShellOption() int listenport) {
		ServerInformation server = portProxyService.deleteServer(listenaddress, listenport);
		if (server == null)
			return "There is no server listening " + listenaddress + ":" + listenport + ". Nothing is done.";
		return "Successfully to remove server on " + listenaddress + ":" + listenport + ".";
	}

	@ShellMethod("List servers.")
	public String list() {
		if (portProxyService.getRegisteredServerCount() == 0)
			return "There is no service running currently.";
		StringBuilder sb = new StringBuilder("Current running services are as below:");
		for (ServerInformation service : portProxyService.listRegisteredServers()) {
			sb.append("\n").append(service);
		}
		return sb.toString();
	}
}
