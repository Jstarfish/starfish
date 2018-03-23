package priv.starfish.common.pay.wechatpay.util;

public class WechatpayConfig {

	// 亿投车吧PC端
	public static String appid_pc = "wx4901f43b5cc30f43";
	public static String appsecret_pc = "cf30452b771a462f67abbc24eaf85703";
	public static String partner_pc = "1288985601";
	public static String partnerkey_pc = "mgRxzDy1zczZ1gbkNdRlgqcyZzhyGq3b";// 微信商户平台-账户设置-安全设置-api安全

	// 亿投车吧APP端
	public static String appid_app = "wxc4d1a8d6d487e129";
	public static String appsecret_app = "65632b9205709f702a4cbee08e5fdecb";
	public static String partner_app = "1308617001";
	public static String partnerkey_app = "mgRxzDy1zczZ1gbkNdRlgqcyZzhyGq3a";// 微信商户平台-账户设置-安全设置-api安全
	
	// 亿投汇友APP端
	public static String appid_huiyou_app = "wx9a9dc85fe7c11ece";
	public static String appsecret_huiyou_app = "86b5af5214eb3509deb41aca813fb14d";
	public static String partner_huiyou_app = "1318210201";
	public static String partnerkey_huiyou_app = "mgRxzDy1zczZ1gbkNdRlgqcyZzhyGq3c";// 微信商户平台-账户设置-安全设置-api安全

	// 微信支付收款接口链接，请勿修改
	public static String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
	// 微信支付退款接口链接，请勿修改
	public static String createRefundURL = "https://api.mch.weixin.qq.com/secapi/pay/refund";
	// 微信支付退款查询接口链接，请勿修改
	public static String createRefundQueryURL = "https://api.mch.weixin.qq.com/pay/refundquery";

}
