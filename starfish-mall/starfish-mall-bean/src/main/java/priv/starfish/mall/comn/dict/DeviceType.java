package priv.starfish.mall.comn.dict;

import priv.starfish.common.annotation.AsEnumVar;
import priv.starfish.common.annotation.AsSelectList;

@AsSelectList(name = "deviceType")
@AsEnumVar()
public enum DeviceType {
	phone("手机"), pad("平板"), pc("PC");

	private String text;

	private DeviceType(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}
}
