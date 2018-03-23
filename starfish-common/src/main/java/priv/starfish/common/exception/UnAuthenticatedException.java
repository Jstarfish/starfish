package priv.starfish.common.exception;
/**
 * 未认证异常
 * @author koqiui
 *
 */
public class UnAuthenticatedException extends AuthorizationException {

	private static final long serialVersionUID = 4074311146399352965L;

	public UnAuthenticatedException() {
		super();
	}

	public UnAuthenticatedException(String message) {
		super(message);
	}

	public UnAuthenticatedException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnAuthenticatedException(Throwable cause) {
		super(cause);
	}

}
