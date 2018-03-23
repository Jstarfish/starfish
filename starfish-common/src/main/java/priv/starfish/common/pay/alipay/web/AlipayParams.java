package priv.starfish.common.pay.alipay.web;

import java.util.HashMap;
import java.util.Map;

import org.springframework.util.Assert;
import priv.starfish.common.pay.alipay.AlipayCore;
import priv.starfish.common.pay.alipay.sign.MD5;


public class AlipayParams {

	// ↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 合作身份者ID
	public static final String partner = "2088901007106660";

	// 密匙
	public static final String key = "9luimc3yulacgkawdx1mgbvmcsdiqfa2";

	// ↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

	// 参数编码字符集
	public static final String input_charset = "UTF-8";

	// 签名方式
	public static final String sign_type = "MD5";

	// 支付宝提供给商户的服务接入网关URL(新)
	public static final String ALIPAY_GATEWAY = "https://mapi.alipay.com/gateway.do?";

	// 接口名称
	private final String service = "create_direct_pay_by_user";

	// 服务器异步通知页面路径
	private String notify_url;

	// 传递请求出错时的通知页面路径error_notify_url（需要联系支付宝开通该参数权限）
	@SuppressWarnings("unused")
	private String error_notify_url;

	// 页面跳转同步通知页面路径
	private String return_url;

	// 卖家email(卖家支付宝账号)
	private String seller_email = "jingchenggaoke@sina.com";

	// 商户网站唯一订单号
	private String out_trade_no;

	// 商品名称
	private String subject;

	// 收款类型
	private String payment_type = "1";

	// 商品总价
	private String total_fee;

	// 商品展示URL
	private String show_url;

	private Map<String, String> getTmpParamMap() {
		Assert.notNull(out_trade_no, "out_trade_no should be not null!");
		Assert.notNull(subject, "subject should be not null!");
		Assert.notNull(total_fee, "total_fee should be not null!");
		Assert.notNull(seller_email, "seller_email should be not null!");
		Assert.notNull(show_url, "show_url should be not null!");

		Map<String, String> tmpParams = new HashMap<String, String>();
		tmpParams.put("service", service);
		tmpParams.put("partner", partner);
		tmpParams.put("_input_charset", input_charset);
		tmpParams.put("payment_type", payment_type);
		tmpParams.put("return_url", return_url);
		tmpParams.put("notify_url", notify_url);
		tmpParams.put("seller_email", seller_email);
		tmpParams.put("out_trade_no", out_trade_no);
		tmpParams.put("subject", subject);
		tmpParams.put("total_fee", total_fee);
		tmpParams.put("show_url", show_url);

		return tmpParams;
	}

	public static String buildRequestSign(Map<String, String> params) {
		// 把数组所有元素，按照“参数=参数值”的模式用“&”字符拼接成字符串
		String paramStr = AlipayCore.createParamString(params);
		return MD5.sign(paramStr, key, input_charset);
	}

	private Map<String, String> buildSignParams(Map<String, String> paramMap) {
		// 除去数组中的空值和签名参数
		Map<String, String> params = AlipayCore.filterParams(paramMap);
		// 生成签名结果
		String mySign = buildRequestSign(params);

		// 签名结果与签名方式加入请求提交参数组中
		params.put("sign", mySign);
		params.put("sign_type", sign_type);
		return params;
	}

	public String buildRequestForm(String strMethod, String strButtonName) {
		Map<String, String> paramMap = buildSignParams(getTmpParamMap());

		StringBuffer sbHtml = new StringBuffer(512);

		sbHtml.append("<form id=\"alipaySubmit\" name=\"alipaySubmit\" action=\"" + ALIPAY_GATEWAY + "_input_charset="
				+ input_charset + "\" method=\"" + strMethod + "\">");

		for (Map.Entry<String, String> entry : paramMap.entrySet()) {
			String name = entry.getKey();
			String value = entry.getValue();

			sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\"" + value + "\"/>");
		}

		// submit按钮控件请不要含有name属性
		sbHtml.append("<input type=\"submit\" value=\"" + strButtonName + "\" style=\"display:none;\"></form>");
		sbHtml.append("<script>document.forms['alipaySubmit'].submit();</script>");

		return sbHtml.toString();
	}

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getReturn_url() {
		return return_url;
	}

	public void setReturn_url(String return_url) {
		this.return_url = return_url;
	}

	public String getOut_trade_no() {
		return out_trade_no;
	}

	public void setOut_trade_no(String out_trade_no) {
		this.out_trade_no = out_trade_no;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getTotal_fee() {
		return total_fee;
	}

	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}

	public String getShow_url() {
		return show_url;
	}

	public void setShow_url(String show_url) {
		this.show_url = show_url;
	}

}
