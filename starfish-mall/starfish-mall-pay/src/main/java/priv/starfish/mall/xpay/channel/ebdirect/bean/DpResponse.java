package priv.starfish.mall.xpay.channel.ebdirect.bean;

/**
 * 直联支付请求
 * 
 * @author "WJJ"
 * @date 2016年2月28日 下午1:27:27
 *
 */
public class DpResponse {
	public String channel;
	public RspData rspData;

	public static DpResponse newOne() {
		return new DpResponse();
	}
}
