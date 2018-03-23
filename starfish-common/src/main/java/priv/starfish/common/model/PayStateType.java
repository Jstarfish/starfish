package priv.starfish.common.model;

public enum PayStateType {

	unpaid("未支付"), paid("已支付"), refundApplied("已申请退款"), refunded("已退款"),refundFail("退款失败");

	private String text;

	private PayStateType(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}
}
