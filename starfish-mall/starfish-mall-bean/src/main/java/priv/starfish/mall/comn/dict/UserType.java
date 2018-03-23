package priv.starfish.mall.comn.dict;

import priv.starfish.common.annotation.AsEnumVar;

/**
 * 用户类型 getValue()取数值，name()取英文标识，getText()取出中文含义
 * 
 * @author 邓华锋
 * @date 2016年1月12日 上午11:29:05
 *
 */
@AsEnumVar()
public enum UserType {
	member("会员", 1), shop("商户", 2), agency("代理", 3), mall("平台（商城）", 4), wxshop("卫星店", 5);

	private String text;

	private int value;

	private UserType(String text, int value) {
		this.text = text;
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public int getValue() {
		return value;
	}

}
