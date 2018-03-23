package priv.starfish.common.sms;


import priv.starfish.common.util.MapContext;

public interface SmsService {

	/**
	 * 发送普通短信（SMS = Short Messaging Service）
	 * 
	 * @param message
	 *            短信内容
	 * @return 发送返回的错误码
	 */
	public SmsErrorCode sendSms(SmsMessage message, MapContext extra);

	/**
	 * 查询普通短信余量
	 * 
	 * @return 剩余短信量,如果出现异常返回Integer.MIN_VALUE
	 */
	public int getSmsRemain();
}
