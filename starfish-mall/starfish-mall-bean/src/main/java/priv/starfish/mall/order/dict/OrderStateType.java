package priv.starfish.mall.order.dict;

import priv.starfish.common.annotation.AsEnumVar;

@AsEnumVar()
public enum OrderStateType {

	unhandled("未处理"), processing("处理中"), cancelled("已取消"), finished("已完成");

	private String text;

	private OrderStateType(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

}
