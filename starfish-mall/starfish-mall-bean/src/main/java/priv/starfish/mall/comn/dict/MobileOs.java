package priv.starfish.mall.comn.dict;

import priv.starfish.common.annotation.AsEnumVar;
import priv.starfish.common.annotation.AsSelectList;

@AsSelectList(name = "mobileOs")
@AsEnumVar()
public enum MobileOs {
	// cloud("云平台"),
	andPhone("android手机"), andPad("android平板"), iPhone("iPhone"), //
	iPad("iPad"), winPhone("win手机"), winPad("win平板"), //
	unkPhone("未知手机"), unkPad("未知平板");

	private String text;

	private MobileOs(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}
}
