package priv.starfish.mall.comn.misc;

import priv.starfish.mall.comn.dict.DataType;

public interface XParam {

	String getCode();

	void setCode(String code);

	String getName();

	void setName(String name);

	DataType getType();

	void setType(DataType type);

	String getValue();

	void setValue(String value);

}