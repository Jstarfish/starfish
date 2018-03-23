package priv.starfish.common.exception;

public class BusinessException extends XRuntimeException {
	private static final long serialVersionUID = 5766407036148183048L;

	public BusinessException() {
		super();
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause);
	}

	public BusinessException(Throwable cause) {
		super(cause);
	}
}
