package priv.starfish.mall.xpay.channel.ebdirect.bean;

/**
 * 直联支付请求
 * 
 * @author "WJJ"
 * @date 2016年2月28日 下午1:27:27
 *
 */
public class DpRequest {
	public String channel;
	public ReqData reqData;

	public static DpRequest newOne() {
		return new DpRequest();
	}
}
