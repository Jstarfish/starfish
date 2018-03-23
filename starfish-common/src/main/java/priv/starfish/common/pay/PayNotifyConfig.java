package priv.starfish.common.pay;


import priv.starfish.common.base.AppNodeInfo;

public class PayNotifyConfig {

	/**************** // TODO 服务器端配置 **********************/
	// 支付宝——支付——服务器异步通知页面路径
	public static String ali_notify_url = AppNodeInfo.getCurrent().getAbsBaseUrl() + "/pay/aliapi/ali/async/notify";
	// 支付宝——支付——页面跳转同步通知页面路径
	public static String ali_return_url = AppNodeInfo.getCurrent().getAbsBaseUrl() + "/pay/aliapi/ali/sync/notify";
	// 支付宝——批量退款——服务器异步通知页面路径
	public static String ali_refund_notify_url = AppNodeInfo.getCurrent().getAbsBaseUrl() + "/settle/alipay/batch/refund/notify_url";
	// 支付宝——批量转账到支付宝账户——服务器异步通知页面路径
	public static String ali_transfer_notify_url = AppNodeInfo.getCurrent().getAbsBaseUrl() + "/settle/alipay/batch/transfer/notify_url";
	// 支付宝——移动端——服务器异步通知页面路径
	public static String ali_notify_url_mobile = AppNodeInfo.getCurrent().getAbsBaseUrl() + "/pay/aliapi/ali/async/notify";
	// 支付宝——移动端——页面跳转同步通知页面路径(未用)
	public static String ali_return_url_mobile = AppNodeInfo.getCurrent().getAbsBaseUrl() + "/pay/aliapi/ali/sync/notify";

	// 农行跳银联——支付——页面跳转异步通知页面路径
	public static String abc_notify_url_pc = AppNodeInfo.getCurrent().getAbsBaseUrl() + "/pay/abc/server/sync/notify";
	// 农行跳银联——支付——页面跳转同步通知页面路径——正确
	public static String abc_return_url_pc_s = AppNodeInfo.getCurrent().getAbsBaseUrl() + "/pay/abc/pay/success/notify";
	// 农行跳银联——支付——页面跳转同步通知页面路径——错误
	public static String abc_return_url_pc_f = AppNodeInfo.getCurrent().getAbsBaseUrl() + "/pay/abc/pay/fail/notify";

	// 微信——支付——页面跳转异步通知页面路径
	public static String wx_notify_url_pc = AppNodeInfo.getCurrent().getAbsBaseUrl() + "/pay/wechatpay/async/notify";

	/**************** 本地测试前台版 **********************/
//	public static String ali_notify_url = "http://bawangbieji419.xicp.net:14936/web-front/pay/aliapi/ali/async/notify";
//	public static String ali_return_url = "http://bawangbieji419.xicp.net:14936/web-front/pay/aliapi/ali/sync/notify";
//
//	public static String abc_notify_url_pc = "http://bawangbieji419.xicp.net:14936/web-front/pay/abc/server/sync/notify";
//	public static String abc_return_url_pc_s = "http://bawangbieji419.xicp.net:14936/web-front/pay/abc/pay/success/notify";
//	public static String abc_return_url_pc_f = "http://bawangbieji419.xicp.net:14936/web-front/pay/abc/pay/fail/notify";
//
//	public static String wx_notify_url_pc = "http://bawangbieji419.xicp.net:14936/web-front/pay/wechatpay/async/notify";

	/**************** 本地测试后台版 **********************/
//	public static String ali_notify_url = "http://bawangbieji419.xicp.net:14936/web-back/pay/aliapi/ali/async/notify";
//	public static String ali_return_url = "http://bawangbieji419.xicp.net:14936/web-back/pay/aliapi/ali/sync/notify";
//
//	public static String abc_notify_url_pc = "http://bawangbieji419.xicp.net:14936/web-back/pay/abc/server/sync/notify";
//	public static String abc_return_url_pc_s = "http://bawangbieji419.xicp.net:14936/web-back/pay/abc/pay/success/notify";
//	public static String abc_return_url_pc_f = "http://bawangbieji419.xicp.net:14936/web-back/pay/abc/pay/fail/notify";
//
//	public static String wx_notify_url_pc = "http://bawangbieji419.xicp.net:14936/web-back/pay/wechatpay/async/notify";

	/**************** 本地测试移动端版 **********************/
//	public static String ali_notify_url = "http://bawangbieji419.xicp.net:14936/mob-front/pay/aliapi/ali/async/notify";
//	public static String ali_return_url = "http://bawangbieji419.xicp.net:14936/mob-front/pay/aliapi/ali/sync/notify";
//	
//	public static String abc_notify_url_pc = "http://bawangbieji419.xicp.net:14936/mob-front/pay/abc/server/sync/notify";
//	public static String abc_return_url_pc_s = "http://bawangbieji419.xicp.net:14936/mob-front/pay/abc/pay/success/notify";
//	public static String abc_return_url_pc_f = "http://bawangbieji419.xicp.net:14936/mob-front/pay/abc/pay/fail/notify";
//
//	public static String wx_notify_url_pc = "http://bawangbieji419.xicp.net:14936/mob-front/pay/wechatpay/async/notify";
	
	/**************** 注释服务器端后，必须要有的 **********************/
//	public static String ali_refund_notify_url = "http://bawangbieji419.xicp.net:14936/web-back/settle/alipay/batch/refund/notify_url";
//	public static String ali_transfer_notify_url = "http://bawangbieji419.xicp.net:14936/web-back/settle/alipay/batch/transfer/notify_url";
	
	
	
	
	// TODO ******************银联********手机端******************************
	// 银联电子——支付——页面跳转异步通知页面路径
	public static String union_notify_url_mobile = "http://bawangbieji419.xicp.net:14936/mob-front/pay/unionpay/wap/async/notify";
	// public static String union_notify_url_mobile = "/pay/unionpay/wap/async/notify";
	// 银联电子——支付——页面跳转同步通知页面路径
	public static String union_return_url_mobile = "http://bawangbieji419.xicp.net:14936/mob-front/pay/unionpay/wap/sync/notify";
	// public static String union_return_url_mobile = "/pay/unionpay/wap/sync/notify";

}
