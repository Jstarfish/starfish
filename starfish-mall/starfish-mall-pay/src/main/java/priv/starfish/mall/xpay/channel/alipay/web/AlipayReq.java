package priv.starfish.mall.xpay.channel.alipay.web;

import priv.starfish.common.pay.PayNotifyConfig;
import priv.starfish.common.pay.alipay.util.AlipayConfig;
import priv.starfish.mall.xpay.channel.alipay.AlipaySubmit;

import java.util.HashMap;
import java.util.Map;


public class AlipayReq {

	/**
	 * 支付宝-web-扫码支付请求
	 * 
	 * @author "WJJ"
	 * @date 2016年3月7日 上午11:27:11
	 * 
	 * @param no
	 *            订单号
	 * @param subject
	 *            商品名称
	 * @param body
	 *            商品描述
	 * @param amount
	 *            支付金额
	 * @param extraParam
	 *            回传参数(支付宝原样返回)
	 * @return
	 */
	public static String reqPay(String no, String subject, String body, String amount, String extraParam) {
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", AlipayParams.KEY_SERVICE);// 接口服务----即时到账
		sParaTemp.put("partner", AlipayParams.KEY_PARTNER_ID);// 支付宝PID
		sParaTemp.put("_input_charset", AlipayParams.KEY_INPUT_CHARSET);// 统一编码
		sParaTemp.put("payment_type", AlipayParams.KEY_PAYMENT_TYPE);// 支付类型
		sParaTemp.put("notify_url", AlipayParams.getAsyncNotifyUrl());// 异步通知页面
		sParaTemp.put("return_url", AlipayParams.getSyncNotifyUrl());// 页面跳转同步通知页面
		sParaTemp.put("seller_id", AlipayParams.KEY_SELLER_ID);// 卖家支付宝用户号
		sParaTemp.put("out_trade_no", no);// 商品订单编号
		sParaTemp.put("subject", subject);// 商品名称
		sParaTemp.put("total_fee", amount);// 价格
		sParaTemp.put("body", body);// 订单描述
		sParaTemp.put("extra_common_param", extraParam);// 回传参数

		String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "post", "确认");
		return sHtmlText;
	}

	/**
	 * 支付宝-批量退款
	 * 
	 * @author "WJJ"
	 * @date 2016年3月7日 下午2:42:13
	 * 
	 * @param refund_date
	 * @param batch_no
	 * @param batch_num
	 * @param detail_data
	 * @return
	 */
	public static String reqRefund(String refund_date, String batch_no, String batch_num, String detail_data) {
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "refund_fastpay_by_platform_pwd");
		sParaTemp.put("partner", AlipayParams.KEY_PARTNER_ID);
		sParaTemp.put("_input_charset", AlipayParams.KEY_INPUT_CHARSET);
		sParaTemp.put("notify_url", AlipayParams.getRefundUrl());
		sParaTemp.put("seller_user_id", AlipayParams.KEY_SELLER_ID);
		sParaTemp.put("refund_date", refund_date);
		sParaTemp.put("batch_no", batch_no);
		sParaTemp.put("batch_num", batch_num);
		sParaTemp.put("detail_data", detail_data);

		String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "post", "确认");
		return sHtmlText;
	}

	public static String reqTransfer(String pay_date,String batch_no,String batch_fee,String batch_num,String detail_data) {
		// 把请求参数打包成数组
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "batch_trans_notify");
		sParaTemp.put("partner", AlipayConfig.partner);
		sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("notify_url", PayNotifyConfig.ali_transfer_notify_url);
		sParaTemp.put("email", AlipayConfig.seller_email);
		sParaTemp.put("account_name", AlipayConfig.account_name);// 必填
		sParaTemp.put("pay_date", pay_date);
		sParaTemp.put("batch_no", batch_no);
		sParaTemp.put("batch_fee", batch_fee);
		sParaTemp.put("batch_num", batch_num);
		sParaTemp.put("detail_data", detail_data);
		
		String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "post", "确认");
		return sHtmlText;
	}

}
