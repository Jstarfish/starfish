package priv.starfish.mall.comn.dict;

import priv.starfish.common.annotation.AsEnumVar;
import priv.starfish.common.annotation.AsSelectList;

@AsSelectList(name = "appType")
@AsEnumVar()
public enum AppType {
	// cloud("云平台"),
	web("Web站点"), wap("Wap站点"), andApp("android app"), iosApp("ios app"), uknApp("未知 app"),;

	private String text;

	private AppType(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}
}
