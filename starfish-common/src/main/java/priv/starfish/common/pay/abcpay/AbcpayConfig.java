package priv.starfish.common.pay.abcpay;

public class AbcpayConfig {

	public static final String IP_ADDR = "127.0.0.1";// 银企通服务器地址
	public static final int PORT = 15999;// 服务器端口号
	public static final String whyUse = "转账";// 用途
	public static final String postscript = "测试转账";// 附言
	public static final String conFlag = "0";//  核验贷方户名标志?续查标志? 0:否、1:是 
	public static final String urgencyFlag = "1";//  N:不加急 Y加急 
	public static final String opNo = "0001";// 操作员编号
	
	/**************正式**************/
	public static final String corpNo = "6612502080387646";// 客户号
	public static final String dbAccNo = "121201040016832";// 付款方账号
	public static final String dbAccName = "亿车汇（天津）电子商务有限公司";// 付款方户名
	
	/**************测试**************/
//	public static final String corpNo = "6612502046408529";// 客户号
//	public static final String dbAccNo = "121201040012419";// 付款方账号
//	public static final String dbAccName = "中硕物产（天津）有限公司";// 付款方户名

	/**************测试对公账号**************/
	String crAccNo = "121201040016832";// 收款方账号
	String crAccName = "亿车汇（天津）电子商务有限公司";// 收款方户名
	String crBankName = "中国农业银行股份有限公司天津经济技术开发区支行";// 收款方开户行名
	String crBankNo = "103110012123";// 收款方开户行号
}
