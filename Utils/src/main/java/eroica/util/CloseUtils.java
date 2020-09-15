package eroica.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import eroica.util.exception.CompositeException;

/**
 * Tools for autoCloseables.
 * 
 * @author Minhua HUANG
 *
 */
public class CloseUtils {
	private static void closeQuietly(boolean inReverseOrder, AutoCloseable... closeables) {
		if (ArrayUtils.isEmpty(closeables))
			return;
		if (inReverseOrder)
			for (int i = closeables.length - 1; i >= 0; i--)
				try {
					if (closeables[i] != null)
						closeables[i].close();
				} catch (Throwable e) {
				}
		else
			for (int i = 0; i < closeables.length; i++)
				try {
					if (closeables[i] != null)
						closeables[i].close();
				} catch (Throwable e) {
				}
	}

	/**
	 * Close the closeables quietly.
	 * 
	 * @param closeables
	 */
	public static void closeQuietly(AutoCloseable... closeables) {
		closeQuietly(false, closeables);
	}

	/**
	 * Close the closeables. If multiple exceptions are thrown by the close method,
	 * this method will compose them into one CompositeException and throws it.
	 * 
	 * @param closeables
	 * @throws Exception
	 */
	public static void close(AutoCloseable... closeables) throws Exception {
		if (closeables.length == 1)
			closeables[0].close();
		else {
			List<Exception> es = new ArrayList<Exception>();
			for (AutoCloseable c : closeables)
				try {
					c.close();
				} catch (Exception e) {
					es.add(e);
				}
			if (es.size() == 1)
				throw es.get(0);
			else if (es.size() > 1)
				throw new CompositeException(es.toArray(new Exception[0]));
		}
	}

	/**
	 * Compose the closeables into one AutoCloseable object.
	 * 
	 * @param closeables
	 * @return
	 */
	public static CompositeAutoCloseable compose(AutoCloseable... closeables) {
		return new CompositeAutoCloseable(closeables);
	}

	/**
	 * Compose the closeables into one AutoCloseable object.
	 * 
	 * @author Minhua HUANG
	 *
	 */
	public static class CompositeAutoCloseable implements AutoCloseable {
		private AutoCloseable[] closeables;

		private CompositeAutoCloseable(AutoCloseable... closeables) {
			super();
			this.closeables = closeables == null ? null : Arrays.copyOf(closeables, closeables.length);
		}

		/**
		 * Get the original closeables.
		 * 
		 * @return
		 */
		public AutoCloseable[] getCloseables() {
			return closeables == null ? null : Arrays.copyOf(closeables, closeables.length);
		}

		@Override
		public void close() throws Exception {
			CloseUtils.close(closeables);
		}

		public void closeQuietly() {
			CloseUtils.closeQuietly(closeables);
		}
	}
}
