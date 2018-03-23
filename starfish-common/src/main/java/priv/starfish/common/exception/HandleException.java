package priv.starfish.common.exception;

public class HandleException extends RuntimeException {

	private static final long serialVersionUID = -6664631917389178185L;

	public HandleException() {
		super();
	}

	public HandleException(String message) {
		super(message);
	}

	public HandleException(String message, Throwable cause) {
		super(message, cause);
	}

	public HandleException(Throwable cause) {
		super(cause);
	}

}
