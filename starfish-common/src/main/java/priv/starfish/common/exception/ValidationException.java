package priv.starfish.common.exception;

/**
 * 认证异常
 * 
 * @author koqiui
 * 
 */
public class ValidationException extends XRuntimeException {
	private static final long serialVersionUID = -8749999234649525279L;

	public ValidationException() {
		super();
	}

	public ValidationException(String message) {
		super(message);
	}

	public ValidationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ValidationException(Throwable cause) {
		super(cause);
	}
}
