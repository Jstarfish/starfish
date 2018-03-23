package priv.starfish.common.pay.dict;

public enum PayWayType {

	alipay("支付宝"), wechatpay("微信"), tenpay("财付通"), ecardpay("E卡"), abcpay("农行"), abcAsUnionpay("农行转银联"), pos("POS刷卡"),
	unionpay("银联在线"), yeepay("易宝"), balanpay("余额"), coupon("优惠券"), chinapay("银联电子");

	private String text;

	private PayWayType(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}
	
}
