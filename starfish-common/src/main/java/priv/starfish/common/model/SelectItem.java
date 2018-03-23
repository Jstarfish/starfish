package priv.starfish.common.model;

public class SelectItem {
	private String value;
	private String text;

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String toString() {
		return "SelectItem [value=" + value + ", text=" + text + "]";
	}

	// --------------------------------------------------------------
	public static SelectItem newOne(String value, String text) {
		SelectItem selectItem = new SelectItem();
		selectItem.setValue(value);
		selectItem.setText(text);
		return selectItem;
	}

	public static SelectItem newOne() {
		return new SelectItem();
	}

}
