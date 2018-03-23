package priv.starfish.common.exception;

/**
 * 非法动作/行动
 * 
 * @author koqiui
 * 
 */
public class InvalidActionException extends XRuntimeException {
	private static final long serialVersionUID = -8749999234649525279L;

	public InvalidActionException() {
		super();
	}

	public InvalidActionException(String message) {
		super(message);
	}

	public InvalidActionException(String message, Throwable cause) {
		super(message, cause);
	}

	public InvalidActionException(Throwable cause) {
		super(cause);
	}
}
