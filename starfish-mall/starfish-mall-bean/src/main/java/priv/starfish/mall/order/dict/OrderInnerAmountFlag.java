package priv.starfish.mall.order.dict;

import priv.starfish.common.annotation.AsEnumVar;

/**
 * 销售订单内部金额
 * 
 * @author 邓华锋
 * @date 2015年12月21日 下午12:02:02
 *
 */
@AsEnumVar()
public enum OrderInnerAmountFlag {
	ecard("e卡",1), balance("余额",2), integral("积分",3), proCoupon("商品优惠券",4),svcCoupon("服务优惠券",5);

	private String text;

	private int value;

	private OrderInnerAmountFlag(String text, int value) {
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
