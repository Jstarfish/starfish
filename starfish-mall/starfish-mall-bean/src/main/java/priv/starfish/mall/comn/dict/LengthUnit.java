package priv.starfish.mall.comn.dict;

import priv.starfish.common.annotation.AsEnumVar;
import priv.starfish.common.annotation.AsSelectList;

@AsSelectList(name = "lengthUnit")
@AsEnumVar()
public enum LengthUnit {
	km("千米"), m("米"), dm("分米"), cm("厘米"), mm("毫米");

	private String text;

	private LengthUnit(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}
}
