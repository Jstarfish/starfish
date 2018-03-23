package priv.starfish.mall.comn.dict;

import priv.starfish.common.annotation.AsEnumVar;
import priv.starfish.common.annotation.AsSelectList;

@AsSelectList(name = "resType")
@AsEnumVar()
public enum ResType {
	url("url"), btn("按钮"), menu("菜单");

	private String text;

	private ResType(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}
}
