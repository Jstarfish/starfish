package priv.starfish.common.base;

public enum IOMode {
	In("输入"), //
	Out("输出"), //
	InOut("输入和输出"), //
	None("N/A");

	private String text;

	IOMode(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}
}
