package priv.starfish.mall.comn.dict;

import priv.starfish.common.annotation.AsEnumVar;
import priv.starfish.common.annotation.AsSelectList;

@AsSelectList(name = "appType")
@AsEnumVar()
public enum Period {
	// cloud("云平台"),
	day("天"), week("周"), month("月"), year("年");

	private String text;

	private Period(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}
}
