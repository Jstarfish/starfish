package priv.starfish.common.exception;

/**
 * 认证异常
 * 
 * @author koqiui
 * 
 */
public class AuthenticationException extends XRuntimeException {
	private static final long serialVersionUID = -8749999234649525279L;

	public AuthenticationException() {
		super();
	}

	public AuthenticationException(String message) {
		super(message);
	}

	public AuthenticationException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthenticationException(Throwable cause) {
		super(cause);
	}
}
