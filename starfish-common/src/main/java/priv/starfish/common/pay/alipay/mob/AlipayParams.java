package priv.starfish.common.pay.alipay.mob;

public class AlipayParams {

	// ↓↓↓↓↓↓↓↓↓↓请在这里配置您的基本信息↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	// 合作身份者ID
	public static final String partner = "2088901007106660";//

	// 卖家email(卖家支付宝账号)
	public static final String seller_id = "jingchenggaoke@sina.com";//

	// 商户的私钥
	public static final String private_key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAJlB+i/HttcBDZ3gALVXwnfMB6swvUV+i7DPF01Tufe+bgpBODWatGr6rQJZ54TY4eMN56uiz7qYVVD3ahXvBm0SoCVoc0HgwL3PyyWzXRKPABQgLM3S1PnA2MayS+MO/TF9ZgRIC4LI9u0FVP0uzMByJ9frYfRzFMlOl1HY/dtZAgMBAAECgYAvz3paP4aVqllyH/h4mikrsDXQPQM+08yGWwSc9fq1A+5WOqRrbJjOKuegLSXhtrs5VxQn4kHFp1IuURECSLALCT3iThLVkQqvV5r5EzoYEg8QwEoOVnRaS+4a0PTeqN6Bt1EbsZowB81nBxseiIQjAdJDbEe4SZ/6FuAt0mt7ZQJBAMsGD2c/h73aIF1WcZMFah0yGc6TpOmVl/U7grsefvcld0lB+lE00AqvdADqnUn2tGKIdHt0+itJ5T3lu79MKz8CQQDBP4/Z2HaPVT/SKl46HzH9fdsSyxmNsesZ+xED4/UxwoiooSpF5neeCy8vSXXZNTc5Cj40TAhzSd+8EhrlE0tnAkEAvU5wTHDDrLZYmU/CZTmWQZT21VV8X0XaxJLyCnKcJu1rdgasVs4s8tm5DCW39kCw4HOmUXu4zWaA+B9f2jc21wJAZsqUjd/iUKOb6wK2VFw7jwbe/MQjt4cn8w3lIxiiV/GOF4SCo+PPRLXWR2Tf1sXDtgeIxvCanhhxdex7edYliQJAKw0hk8WoBlovhkpPgDXAt5NDouTL8piztaU80+Brk45zODQoQTgMsRgyQu+IhEZyWaVoaNgMy3utTrwEE/dXTg==";//
	// 商户的公钥
	public static final String public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCZQfovx7bXAQ2d4AC1V8J3zAerML1FfouwzxdNU7n3vm4KQTg1mrRq+q0CWeeE2OHjDeeros+6mFVQ92oV7wZtEqAlaHNB4MC9z8sls10SjwAUICzN0tT5wNjGskvjDv0xfWYESAuCyPbtBVT9LszAcifX62H0cxTJTpdR2P3bWQIDAQAB";//
	// 支付宝的公钥，无需修改该值
	public static final String ali_public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";
	// ↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	// 参数编码字符集
	public static final String input_charset = "UTF-8";

	// 签名方式
	public static final String sign_type = "RSA";

	public static final String service = "mobile.securitypay.pay";

	public static final String payment_type = "1";

	// 服务器异步通知页面路径
	private String notify_url;

	// 商品展示URL
	private String show_url;

	// 商户网站唯一订单号
	private String out_trade_no;

	// 商品名称
	private String subject;

	// 商品总价
	private String total_fee;

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getShow_url() {
		return show_url;
	}

	public void setShow_url(String show_url) {
		this.show_url = show_url;
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

}
