package priv.starfish.common.exception;

public class FieldValidationException extends ValidationException {

	private static final long serialVersionUID = 2658869174035516936L;

	private String fieldName;

	public FieldValidationException(String fieldName, String message) {
		super(message);
		//
		this.fieldName = fieldName;
	}

	public String getFieldName() {
		return fieldName;
	}
}
