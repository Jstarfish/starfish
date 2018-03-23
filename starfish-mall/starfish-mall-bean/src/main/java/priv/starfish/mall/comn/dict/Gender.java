package priv.starfish.mall.comn.dict;


import priv.starfish.common.annotation.AsEnumVar;
import priv.starfish.common.annotation.AsSelectList;

@AsSelectList(name = "gender")
@AsEnumVar()
public enum Gender {
	X("保密"), M("男"), F("女");

	private String text;

	private Gender(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}
}
