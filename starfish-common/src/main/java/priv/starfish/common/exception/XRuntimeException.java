package priv.starfish.common.exception;

public class XRuntimeException extends RuntimeException {
	private static final long serialVersionUID = -1675019712744041742L;

	public XRuntimeException() {
		super();
	}

	public XRuntimeException(String message) {
		super(message);
	}

	public XRuntimeException(String message, Throwable cause) {
		super(message, cause);
	}

	public XRuntimeException(Throwable cause) {
		super(cause);
	}
}
