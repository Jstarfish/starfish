package priv.starfish.common.pay.dict;

public enum PaySubjectType {

	saleOrder("销售订单");

	private String text;

	private PaySubjectType(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}
	
}
