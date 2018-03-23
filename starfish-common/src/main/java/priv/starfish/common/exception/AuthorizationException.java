package priv.starfish.common.exception;

/**
 * 授权异常
 * @author koqiui
 * 
 */
public class AuthorizationException extends XRuntimeException {
	private static final long serialVersionUID = -4965816090174695629L;

	public AuthorizationException() {
		super();
	}

	public AuthorizationException(String message) {
		super(message);
	}

	public AuthorizationException(String message, Throwable cause) {
		super(message, cause);
	}

	public AuthorizationException(Throwable cause) {
		super(cause);
	}

}
