package priv.starfish.mall.market.dict;

import priv.starfish.common.annotation.AsEnumVar;
import priv.starfish.common.annotation.AsSelectList;

/**
 * 馈赠物品类型
 * 
 * @author koqiui
 * @date 2016年1月7日 下午11:59:41
 *
 */
@AsSelectList(name = "giftType")
@AsEnumVar()
public enum GiftType {
	money("金额"), coupon("优惠券"), goods("商品"), svc("服务");

	private GiftType(String text) {
		this.text = text;
	}

	private String text;

	public String getText() {
		return this.text;
	}

}
