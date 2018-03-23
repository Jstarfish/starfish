package priv.starfish.mall.market.dict;

import priv.starfish.common.annotation.AsEnumVar;
import priv.starfish.common.annotation.AsSelectList;

/**
 * 优惠券类型
 * 
 * @author koqiui
 * @date 2015年10月26日 上午12:25:49
 *
 */
@AsSelectList(name = "couponType")
@AsEnumVar()
public enum CouponType {
	nopay("免付券", "M"), // (M no pay) 无需用户支付 （关联具体商品或服务，持此券可免付相关商品或服务）

	sprice("特价券", "T"), // (T special price) 特价 （关联具体商品或服务，持此特价券购买相关商品和服务可以享受特价）

	deduct("抵金券", "D");// (D deduct) （关联具体商品、分类或服务，持此券可以抵扣相应的金额）

	private CouponType(String text, String abbr) {
		this.text = text;
		this.abbr = abbr;
	}

	private String text;
	private String abbr;

	public String getText() {
		return text;
	}

	public String getAbbr() {
		return abbr;
	}

}
