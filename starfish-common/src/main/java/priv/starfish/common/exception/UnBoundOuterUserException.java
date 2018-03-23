package priv.starfish.common.exception;

/**
 * 认证绑定失败（未绑定系统用户）
 * 
 * @author koqiui
 * 
 */
public class UnBoundOuterUserException extends AuthorizationException {
	private static final long serialVersionUID = 6505937529993713092L;

	public UnBoundOuterUserException() {
		super();
	}

	public UnBoundOuterUserException(String message) {
		super(message);
	}

	public UnBoundOuterUserException(String message, Throwable cause) {
		super(message, cause);
	}

	public UnBoundOuterUserException(Throwable cause) {
		super(cause);
	}
}
