package priv.starfish.mall.comn.dict;

import priv.starfish.common.annotation.AsEnumVar;
import priv.starfish.common.annotation.AsSelectList;

@AsSelectList(name = "dataType")
@AsEnumVar()
public enum DataType {
	Text("单行文本"), MlText("多行文本"), Bool("布尔"), Int("整数"), Float("浮点"), Date("日期"), DateTime("日期时间"), Enum("枚举选择");

	private String text;

	private DataType(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}
}
