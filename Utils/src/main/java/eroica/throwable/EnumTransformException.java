package eroica.throwable;

import eroica.util.enumeration.EnumUtil;

/**
 * This exception is thrown by {@link EnumUtil}, when a composite enum object is
 * transformed into its primary enum objects.
 * 
 * @author Minhua HUANG
 */
public class EnumTransformException extends RuntimeException {
	private static final long serialVersionUID = -2951456242299581487L;

	public EnumTransformException() {
		super();
	}

	public EnumTransformException(String message, Throwable cause) {
		super(message, cause);
	}

	public EnumTransformException(String message) {
		super(message);
	}

	public EnumTransformException(Throwable cause) {
		super(cause);
	}
}
