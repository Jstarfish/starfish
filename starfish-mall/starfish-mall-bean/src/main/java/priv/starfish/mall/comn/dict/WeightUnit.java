package priv.starfish.mall.comn.dict;

import priv.starfish.common.annotation.AsEnumVar;
import priv.starfish.common.annotation.AsSelectList;

@AsSelectList(name = "weightUnit")
@AsEnumVar()
public enum WeightUnit {
	kg("千克"), g("克"), mg("毫克");// t("吨")

	private String text;

	private WeightUnit(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}
}
