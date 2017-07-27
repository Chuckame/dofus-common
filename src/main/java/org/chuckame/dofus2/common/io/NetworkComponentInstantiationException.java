package org.chuckame.dofus2.common.io;

public class NetworkComponentInstantiationException extends RuntimeException {
	private static final long serialVersionUID = 2422935057664223250L;

	public NetworkComponentInstantiationException() {
		super();
	}

	public NetworkComponentInstantiationException(String message, Throwable cause) {
		super(message, cause);
	}

	public NetworkComponentInstantiationException(String message) {
		super(message);
	}

	public NetworkComponentInstantiationException(Throwable cause) {
		super(cause);
	}
}