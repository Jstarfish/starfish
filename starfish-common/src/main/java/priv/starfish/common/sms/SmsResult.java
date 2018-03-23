package priv.starfish.common.sms;

public class SmsResult {
	public SmsErrorCode errCode;
	public String smsText;

	@Override
	public String toString() {
		return "SmsResult [errCode=" + errCode + ", smsText=" + smsText + "]";
	}

	public static SmsResult newOne() {
		return new SmsResult();
	}
}
