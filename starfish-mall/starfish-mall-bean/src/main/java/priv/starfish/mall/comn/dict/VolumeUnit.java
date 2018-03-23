package priv.starfish.mall.comn.dict;


import priv.starfish.common.annotation.AsEnumVar;
import priv.starfish.common.annotation.AsSelectList;

@AsSelectList(name = "volumeUnit")
@AsEnumVar()
public enum VolumeUnit {
	m3("立方米", "KL"), dm3("升/立方分米", "L"), cm3("毫升/立方厘米", "ML");

	private String text;
	private String abbr;

	private VolumeUnit(String text, String abbr) {
		this.text = text;
		this.abbr = abbr;
	}

	public String getText() {
		return this.text;
	}

	public String getAbbr() {
		return this.abbr;
	}
}
