package org.tamm.client;

public class InitializationException extends Exception {

	private static final long serialVersionUID = 1L;

	public InitializationException(String msg) {
		super(msg);
	}

	public InitializationException(String msg, Throwable t) {
		super(msg, t);
	}

	public InitializationException(Throwable t) {
		super(t);
	}
}
