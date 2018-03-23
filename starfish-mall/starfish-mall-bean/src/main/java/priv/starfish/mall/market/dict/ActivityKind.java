package priv.starfish.mall.market.dict;

import priv.starfish.common.annotation.AsSelectList;

/**
 * 活动种类（已经分拆为 e卡、服务、商品）
 * 
 * @author koqiui
 * @date 2015年10月26日 上午12:25:49
 *
 */
@AsSelectList(name = "activityKind")
@Deprecated
public enum ActivityKind {
	ecard("e卡"), svc("服务"), goods("商品");

	private ActivityKind(String text) {
		this.text = text;
	}

	private String text;

	public String getText() {
		return this.text;
	}

}
