package priv.starfish.mall.order.dict;

import java.util.HashMap;
import java.util.Map;

import priv.starfish.common.annotation.AsEnumVar;
import priv.starfish.common.annotation.AsSelectList;

/**
 * 订单类型
 * 
 * @author koqiui
 * @date 2016年2月1日 下午2:50:39
 *
 */
@AsSelectList(name = "orderType")
@AsEnumVar()
public enum OrderType {
	unknown("未知订单", -1), saleOrder("销售订单", 0), ecardOrder("e卡订单", 1);

	private String text;

	private int value;

	private OrderType(String text, int value) {
		this.text = text;
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public int getValue() {
		return value;
	}

	//
	private static Map<Integer, OrderType> valueMap = new HashMap<Integer, OrderType>();

	static {
		valueMap.put(unknown.getValue(), unknown);
		valueMap.put(saleOrder.getValue(), saleOrder);
		valueMap.put(ecardOrder.getValue(), ecardOrder);
	}

	public static OrderType getByValue(int value) {
		return valueMap.get(value);
	}
}
