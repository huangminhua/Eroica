package eroica.util.exception;

public class MoreThanOneElementException extends RuntimeException {
	private static final long serialVersionUID = 1201620420146073214L;

	public MoreThanOneElementException() {
		super();
	}

	public MoreThanOneElementException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public MoreThanOneElementException(String message, Throwable cause) {
		super(message, cause);
	}

	public MoreThanOneElementException(String message) {
		super(message);
	}

	public MoreThanOneElementException(Throwable cause) {
		super(cause);
	}

}
