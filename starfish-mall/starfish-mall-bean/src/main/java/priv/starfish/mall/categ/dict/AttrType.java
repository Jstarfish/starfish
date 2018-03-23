package priv.starfish.mall.categ.dict;

import priv.starfish.common.annotation.AsEnumVar;
import priv.starfish.common.annotation.AsSelectList;

@AsSelectList(name = "attrType")
@AsEnumVar()
public enum AttrType {
	Text("单行文本"), MlText("多行文本"), Bool("布尔"), Int("整数"), Float("浮点"), Date("日期"), DateTime("日期时间"), Enum("枚举选择");

	private String text;

	private AttrType(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}
}
