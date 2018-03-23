package priv.starfish.common.pay.alipay.util;

public class AlipayConfig {

	// ↓↓↓↓↓↓↓↓↓↓亿车汇↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
	public static String partner = "2088121370063427";
	public static String seller_id = "2088121370063427";
	public static String seller_email = "glzx@etcarbar.com";
	public static String key = "ozsfg9ui1n65syvf319l2n04w2n7r473";
	public static String account_name = "亿车汇（天津）电子商务有限公司";
	// ↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

	// 调试用，创建TXT日志文件夹路径
	public static String log_path = "D:\\";

	// 字符编码格式 目前支持 gbk 或 utf-8
	public static String input_charset = "utf-8";

	// 签名方式 不需修改
	public static String sign_type = "MD5";

	// *******begin*******自己添加*********************

	// 支付——支付类型 不能修改 必填
	public static String payment_type = "1";
	// 退款理由
	public static String refundReason = "协商退款";
	// 转账说明
	public static String transferExplain = "周期结算";
	// 转账成功原因
	public static String successReason = "转账正常";
	// 转账成功标识
	public static String settle_success = "S";
	// 转账失败标识
	public static String settle_fail = "F";

	// *******end*******自己添加*********************.

	// TODO ******************支付宝********mobile******************************

	// 亿车汇
	// 商户的私钥,使用支付宝自带的openssl工具生成。
	public static String private_key = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBALAUEQUj0rYlDQyuYTECftpa36FqVRQBHLVNFx34zbdS30tgP8bsaeJHhW9OMBZPPwqWNIjj/qvdL6NoN0CeuHRjjrMPYL8XlPe/dWX2R0XLlQtRk8v8JyT674LoUBhS8TkwjRmr818evJQVAieprCEOIhdjcNBkdCheOprbF/wFAgMBAAECgYB+4ob9WDpQ7dRAji0Vv0GgopnC3ThgCQkiEWKWQ6Qi9oJY9Awhkl4fJQEgG40FlqfMPj+vYfU07liU/dXLKFWsv5EzUdBzlzUKDMrZ6fekesAf+3iwFDulGZgjfjScGuQ59iLIs21Re4PV+v3NiWGEMHPCOcBTgFYLr0ge6PSfwQJBANzrpF90vmhVlGQ7zjNYTjMWRd36IK328QVaE851afTMe7/k/2enYwwUuqBhlcwB5O64OlyPRauxM0Ui/mY7rrUCQQDMCZr9PjLp+KXD7485lob2t/zK/lD/iVaFGroM1reKyptJw9ikvZMMkm4KlavlDZH4KFn209p6LWyB25ZmtxoRAkAmg0nvfimnhKvO4YIr/0v6qfpaHT3PNgqEdVSYnG1xSKiWJnTD/DFPLNnwFbMIkpf3adR6yFtL+CNI/TW+Ws81AkEAnfd10ki8fF5wYCDxXyGGPi81/ScmKKD4pjaKAnSeR0sTLOn+qZTH6zzXC0TTf5OhBmKfPDTK+jp+vo72g3GRMQJAIL4CIdSyyo4zBh1TpvfUesprbC+AXmyBojtQNhH4sBHD5O7Kh3g5K8adeUqbFl5I+tLgWMO4lDh8QauIPXRIDQ==";
	// 支付宝的公钥，无需修改该值
	public static String ali_public_key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	// ↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑
	public static final String SIGN_ALGORITHMS = "SHA1WithRSA";

	// 签名方式 不需修改
	public static String sign_type_mobile = "RSA";

}
