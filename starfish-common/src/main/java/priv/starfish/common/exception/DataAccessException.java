package priv.starfish.common.exception;

/**
 * 认证异常
 * 
 * @author koqiui
 * 
 */
public class DataAccessException extends XRuntimeException {
	private static final long serialVersionUID = -8749999234649525279L;

	public DataAccessException() {
		super();
	}

	public DataAccessException(String message) {
		super(message);
	}

	public DataAccessException(String message, Throwable cause) {
		super(message, cause);
	}

	public DataAccessException(Throwable cause) {
		super(cause);
	}
}
