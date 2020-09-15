package eroica.util.exception;

import java.io.PrintStream;

public class CompositeException extends Exception {
	private static final long serialVersionUID = 8847377589388899520L;

	private Throwable[] causes = null;

	public CompositeException(String message, boolean enableSuppression, boolean writableStackTrace,
			Throwable... causes) {
		super(message, null, enableSuppression, writableStackTrace);
		this.causes = causes;
	}

	public CompositeException(String message, Throwable... causes) {
		super(message);
		this.causes = causes;
	}

	public CompositeException(Throwable... causes) {
		super();
		this.causes = causes;
	}

	public Throwable[] getCauses() {
		return causes;
	}

	public void printStackTrace(PrintStream s) {
		super.printStackTrace(s);
		for (Throwable t : causes)
			t.printStackTrace(s);
	}
}
