package priv.starfish.common.sms;

/**
 * 短信
 * 
 * @author guoyanan
 * 
 */
public class SmsMessage {

	/**
	 * 接收者号码
	 */
	private String receiverNumber;

	/**
	 * 短信内容
	 */
	private String text;

	public SmsMessage() {
	}

	public SmsMessage(String receiverNumber, String text) {
		this.receiverNumber = receiverNumber;
		this.text = text;
	}

	public String getReceiverNumber() {
		return receiverNumber;
	}

	public void setReceiverNumber(String receiverNumber) {
		this.receiverNumber = receiverNumber;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
