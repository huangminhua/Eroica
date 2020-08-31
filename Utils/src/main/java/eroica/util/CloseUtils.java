package eroica.util;

import org.apache.commons.lang3.ArrayUtils;

public class CloseUtils {
	public static void closeQuietly(AutoCloseable... closeables) {
		if (ArrayUtils.isEmpty(closeables))
			return;
		for (int i = 0; i < closeables.length; i++) {
			try {
				closeables[i].close();
			} catch (Exception e) {
			}
		}
	}
}
