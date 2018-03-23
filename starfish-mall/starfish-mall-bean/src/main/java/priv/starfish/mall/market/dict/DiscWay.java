package priv.starfish.mall.market.dict;

import priv.starfish.common.annotation.AsEnumVar;
import priv.starfish.common.annotation.AsSelectList;

/**
 * 扣减方式
 * 
 * @author koqiui
 * @date 2015年10月26日 上午12:25:49
 *
 */
@AsSelectList(name = "discWay")
@AsEnumVar()
public enum DiscWay {
	coupon("优惠券"), activity("活动"), pack("套餐"), other("其他");

	private DiscWay(String text) {
		this.text = text;
	}

	private String text;

	public String getText() {
		return this.text;
	}

}
