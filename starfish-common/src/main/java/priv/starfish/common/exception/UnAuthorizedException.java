package priv.starfish.common.exception;
/**
 * 未授权异常
 * @author koqiui
 *
 */
public class UnAuthorizedException extends AuthorizationException {

	private static final long serialVersionUID = 4074311146399352965L;

	public UnAuthorizedException() {
		super();
	}

	public UnAuthorizedException(String message) {
		super(message);
	}

	public UnAuthorizedException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnAuthorizedException(Throwable cause) {
		super(cause);
	}

}
