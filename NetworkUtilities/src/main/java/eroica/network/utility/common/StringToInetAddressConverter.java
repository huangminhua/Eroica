package eroica.network.utility.common;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

/**
 * Convert the input parameter from String to InetAddress.
 * @author Minhua HUANG
 *
 */
@Component
public class StringToInetAddressConverter implements Converter<String, InetAddress> {
	@Override
	public InetAddress convert(String source) {
		try {
			return InetAddress.getByName(source);
		} catch (UnknownHostException e) {
			throw new IllegalArgumentException(e);
		}
	}
}
