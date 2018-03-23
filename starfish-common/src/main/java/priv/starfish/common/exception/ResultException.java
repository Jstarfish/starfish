package priv.starfish.common.exception;

/**
 * 认证异常
 * 
 * @author koqiui
 * 
 */
public class ResultException extends XRuntimeException {
	private static final long serialVersionUID = -8749999234649525279L;

	public ResultException() {
		super();
	}

	public ResultException(String message) {
		super(message);
	}

	public ResultException(String message, Throwable cause) {
		super(message, cause);
	}

	public ResultException(Throwable cause) {
		super(cause);
	}
}
