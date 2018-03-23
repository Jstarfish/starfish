package priv.starfish.mall.order.dict;

import priv.starfish.common.annotation.AsEnumVar;
import priv.starfish.common.annotation.AsSelectList;

/**
 * 处理标记
 * 
 * @author 邓华锋
 * @date 2016年1月27日 上午9:37:52
 *
 */
@AsSelectList(name = "handleFlag")
@AsEnumVar()
public enum HandleFlag {
	untreated("初始（未处理）", 0), unhandled("不予处理", -1), treated("已处理", 1);
	
	private String text;

	private int value;

	private HandleFlag(String text, int value) {
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
