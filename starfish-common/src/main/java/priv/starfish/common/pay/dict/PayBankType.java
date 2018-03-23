package priv.starfish.common.pay.dict;

public enum PayBankType {

	alipay("支付宝"), abc("农业银行"), manual("人工手动");

	private String text;

	private PayBankType(String text) {
		this.text = text;
	}

	public String getText() {
		return this.text;
	}
	
}
