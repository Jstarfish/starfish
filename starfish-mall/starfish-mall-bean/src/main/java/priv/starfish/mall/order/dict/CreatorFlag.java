package priv.starfish.mall.order.dict;

import priv.starfish.common.annotation.AsEnumVar;

/**
 * 创建者标记
 * 
 * @author 邓华锋
 * @date 2016年1月20日 下午5:51:41
 *
 */
@AsEnumVar()
public enum CreatorFlag {
	system("系统", -1), user("用户自己", 0), shop("店铺人员", 1), wxshop("合作/分销店", 2);

	private String text;

	private int value;

	private CreatorFlag(String text, int value) {
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
