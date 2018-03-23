package priv.starfish.mall.portal.controller;

import com.abc.trustpay.client.JSON;
import com.abc.trustpay.client.TrxException;
import com.abc.trustpay.client.ebus.PaymentResult;
import com.unionpay.acp.sdk.SDKConfig;
import com.unionpay.acp.sdk.SDKUtil;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.model.Result;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.pay.PayNotifyConfig;
import priv.starfish.common.pay.alipay.util.AlipayConfig;
import priv.starfish.common.pay.alipay.util.AlipayNotify;
import priv.starfish.common.pay.alipay.util.AlipaySubmit;
import priv.starfish.common.pay.unionpay.util.UnopConfig;
import priv.starfish.common.pay.wechatpay.util.GetWxOrderno;
import priv.starfish.common.pay.wechatpay.util.RequestHandler;
import priv.starfish.common.pay.wechatpay.util.WechatpayConfig;
import priv.starfish.common.pay.wechatpay.util.WechatpayCore;
import priv.starfish.common.util.DateUtil;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.NumUtil;
import priv.starfish.mall.order.entity.ECardOrder;
import priv.starfish.mall.order.entity.SaleOrder;
import priv.starfish.mall.order.entity.SaleOrderGoods;
import priv.starfish.mall.order.entity.SaleOrderSvc;
import priv.starfish.mall.service.*;
import priv.starfish.mall.settle.dto.PayDto;
import priv.starfish.mall.settle.dto.WxPayResultDto;
import priv.starfish.mall.web.base.BaseController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.*;

@Remark("支付Controller")
@Controller
@RequestMapping("/pay")
public class PayController extends BaseController {

	@Resource
	SaleOrderService saleOrderService;

	@Resource
	ECardOrderService eCardOrderService;

	@Resource
	ECardService eCardService;

	@Resource
	PayService payService;

	@Resource
	SettleService settleService;

	@Resource
	SalePrmtService salePrmtService;

	// TODO-------------begin-------------即时到账-----------支付宝alipay-------313809 ------------------------------------------

	//
	@Remark("付款页面")
	@RequestMapping(value = "/page/jsp", method = RequestMethod.GET)
	public String goPageJsp(HttpServletRequest request, PayDto payDto) {
		return "pay/payPage";
	}

	/**
	 * 支付宝即时到账支付
	 * 
	 * @author "WJJ"
	 * @date 2015年11月17日 下午4:37:05
	 * 
	 * @param request
	 * @param response
	 * @param payDto
	 * @return
	 * @throws Exception
	 */
	@Remark("支付宝即时到账支付")
	@RequestMapping(value = "/alipay/pay/by/ali", method = RequestMethod.POST)
	public String goPageJspDirect(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 商户订单号 商户网站订单系统中唯一订单号 必填
		String no = request.getParameter("orderNo");

		String subject = "";// 订单(商品)名称 必填
		String amount = "";// 付款金额 必填 两位小数点
		String body = "";// 订单（商品）描述
		String extraParam = "";// 回传参数

		NumberFormat fenFormat = NumUtil.getNumFormat("#0.00");

		// 判断是销售订单S还是E卡订单E
		if (no.startsWith("S")) {
			// 拼接订单名称与描述
			SaleOrder saleOrder = saleOrderService.getSaleOrderByNo(no);
			if (null != saleOrder) {
				// 1、服务
				List<SaleOrderSvc> saleOrderSvcs = saleOrderService.getSaleOrderSvcsByOrderId(saleOrder.getId());
				// 2、商品
				List<SaleOrderGoods> saleOrderGoods = saleOrderService.getSaleOrderGoodssByOrderNoAndUserId(no, saleOrder.getUserId());
				//
				if (saleOrderSvcs.size() != 0) {
					subject = saleOrderSvcs.get(0).getSvcName() + "等";
					body = saleOrderSvcs.get(0).getSvcName() + "等";
				} else if (saleOrderGoods.size() != 0) {
					subject = saleOrderGoods.get(0).getProductName() + "等";
					body = saleOrderGoods.get(0).getProductName() + "等";
				} else {
					subject = no;
					body = no;
				}

				//
				amount = fenFormat.format(saleOrder.getAmountOuter());
			} else {
				return "pay/alipay/ali_noOrder";// 查找不到对应订单
			}
		} else {
			// 拼接订单名称与描述
			ECardOrder eCardOrder = eCardOrderService.getECardOrderByNo(no);
			if (null != eCardOrder) {
				subject = eCardOrder.getCardName();
				amount = fenFormat.format(eCardOrder.getAmount());
				body = eCardOrder.getCardName() + "," + eCardOrder.getQuantity() + "张";
				if (null != eCardOrder.getShopId()) {
					extraParam = eCardOrder.getCardCode() + "|" + eCardOrder.getShopId() + "|" + eCardOrder.getShopName();
				} else {
					extraParam = eCardOrder.getCardCode();
				}
			} else {
				return "pay/alipay/ali_noOrder";// 查找不到对应订单
			}
		}

		//
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "create_direct_pay_by_user");// 接口服务----即时到账
		sParaTemp.put("partner", AlipayConfig.partner);// 支付宝PID
		sParaTemp.put("_input_charset", AlipayConfig.input_charset);// 统一编码
		sParaTemp.put("payment_type", AlipayConfig.payment_type);// 支付类型
		sParaTemp.put("notify_url", PayNotifyConfig.ali_notify_url);// 异步通知页面
		sParaTemp.put("return_url", PayNotifyConfig.ali_return_url);// 页面跳转同步通知页面
		sParaTemp.put("seller_id", AlipayConfig.seller_id);// 卖家支付宝用户号
		sParaTemp.put("out_trade_no", no);// 商品订单编号
		sParaTemp.put("subject", subject);// 商品名称
		sParaTemp.put("total_fee", amount);// 价格
		sParaTemp.put("body", body);// 订单描述
		sParaTemp.put("extra_common_param", extraParam);// 回传参数

		// 建立请求
		try {
			String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "post", "确认");
			request.setAttribute("sHtmlText", sHtmlText);
			return "pay/alipay/ali_html";
		} catch (Exception e) {
			return "pay/alipay/ali_reqfail";
		}
	}

	/**
	 * 支付宝主动通知调用的页面（服务器异步通知页面） 不能执行任何页面跳转
	 * 
	 * @author "WJJ"
	 * @date 2015年10月22日 下午1:55:01
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@Remark("支付宝服务器异步通知页面路径")
	@RequestMapping(value = "/aliapi/ali/async/notify", method = RequestMethod.POST)
	public void returnNotify(HttpServletRequest request, HttpServletResponse response) throws IOException {

		Map<String, String> params = new HashMap<String, String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "UTF-8");
			params.put(name, valueStr);
		}
		// 获取支付宝的通知返回参数，可参考技术文档中页面跳转同步通知参数列表(以下仅供参考)//

		String no = request.getParameter("out_trade_no");// 订单号
		String subject = request.getParameter("subject");// 商品名称
		String body = request.getParameter("body");// 商品描述
		String amount = request.getParameter("total_fee");// 交易金额
		String tradeStatus = request.getParameter("trade_status");// 交易状态
		String tradeNo = request.getParameter("trade_no");// 支付宝交易号
		String payTime = request.getParameter("gmt_payment");// 付款时间
		String extraParam = request.getParameter("extra_common_param");// 公共回传参数

		// System.out.println("---------------------------异步-------------------------------------------");
		// System.out.println(params.toString());

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		//
		if (AlipayNotify.verify(params)) {// 验证成功
			if (tradeStatus.equals("TRADE_SUCCESS") || tradeStatus.equals("TRADE_FINISHED")) {
				try {
					// 支付完成后的一系列业务操作
					payService.payFinishedOperation(no, subject, body, amount, tradeNo, tradeStatus, payTime, extraParam);
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					// 返回
					out.print("success");// 给支付宝返回success 不能修改
				}
			}
			out.print("fail");
		} else {// 验证失败
			out.print("fail");
		}

	}

	/**
	 * 支付宝付完款后跳转的页面
	 * 
	 * @author "WJJ"
	 * @date 2015年10月22日 下午2:09:20
	 * 
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	@Remark("支付宝页面跳转同步通知页面路径")
	@RequestMapping(value = "/aliapi/ali/sync/notify", method = RequestMethod.GET)
	public String goReturnJsp(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 只做交易是否成功的判断，处理业务逻辑在异步通知里面做
		Map<String, String> params = new HashMap<String, String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			// 乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
			// TODO 服务器端配置 自己电脑给注释了，服务器上给打开
			// valueStr = new String(valueStr.getBytes("ISO-8859-1"), "UTF-8");
			params.put(name, valueStr);
		}

		// System.out.println("---------------------------同步-------------------------------------------");
		// System.out.println(params.toString());

		String no = request.getParameter("out_trade_no");// 订单号
		String tradeStatus = request.getParameter("trade_status");// 交易状态

		if (AlipayNotify.verify(params)) {// 验证成功
			if (tradeStatus.equals("TRADE_SUCCESS") || tradeStatus.equals("TRADE_FINISHED")) {

				if (no.startsWith("S")) {
					// 把订单号、商品名称、交易金额、商品描述等传到页面展现
					// request.setAttribute("no", no);
					return "pay/alipay/ali_return_url";// 支付成功通知页面
				} else {
					// 把订单号、商品名称、交易金额、商品描述等传到页面展现
					// request.setAttribute("no", no);
					return "pay/alipay/ali_return_url_ecard";// 支付成功通知页面
				}
			}
			return "pay/alipay/ali_payFail";// 支付失败页面
		} else {// 验证失败
			return "pay/alipay/ali_verifyFail"; // 验证失败页面
		}

	}

	// ---------------end-----------即时到账-----------支付宝alipay-------313809------------------------------------------

	// TODO-------------begin-------------网银支付-----未用------支付宝alipay------------未用-------------------------------------

	/**
	 * 支付宝 网银支付
	 * 
	 * @author "WJJ"
	 * @date 2015年11月17日 下午4:37:55
	 * 
	 * @param request
	 * @param response
	 * @param payDto
	 * @return
	 * @throws Exception
	 */
	@Remark("支付宝网银支付")
	@RequestMapping(value = "/alipay/pay/by/bankpay", method = RequestMethod.POST)
	public String goPageJspBankPay(HttpServletRequest request, HttpServletResponse response, PayDto payDto) throws Exception {
		NumberFormat fenFormat = NumUtil.getNumFormat("#0.00");

		// 商户订单号 商户网站订单系统中唯一订单号 必填
		String no = payDto.getOrderNo();
		// 订单(商品)名称 必填
		String subject = payDto.getSubject();
		// 付款金额 必填
		String amount = fenFormat.format(payDto.getAmount());
		// 订单（商品）描述
		String body = payDto.getBody();
		// 商品展示地址 需以http://开头的完整路径，例如：http://www.xxx.com/myorder.html
		// String show_url = payDto.getShowUrl();

		// 默认支付方式 必填
		String paymethod = "bankPay";
		// 默认网银 必填，银行简码请参考接口技术文档
		String defaultbank = payDto.getBkcode();

		//
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "create_direct_pay_by_user");// 接口服务----网银支付
		sParaTemp.put("partner", AlipayConfig.partner);// 支付宝PID
		sParaTemp.put("_input_charset", AlipayConfig.input_charset);// 统一编码
		sParaTemp.put("payment_type", AlipayConfig.payment_type);// 支付类型
		sParaTemp.put("notify_url", PayNotifyConfig.ali_notify_url);// 异步通知页面
		sParaTemp.put("return_url", PayNotifyConfig.ali_return_url);// 页面跳转同步通知页面
		sParaTemp.put("seller_id", AlipayConfig.seller_id);// 卖家支付宝账号
		sParaTemp.put("out_trade_no", no);// 商品订单编号
		sParaTemp.put("subject", subject);// 商品名称
		sParaTemp.put("paymethod", paymethod);
		sParaTemp.put("defaultbank", defaultbank);

		sParaTemp.put("total_fee", amount);// 价格
		sParaTemp.put("body", body);// 订单描述

		// 建立请求
		try {
			String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "post", "确认");
			request.setAttribute("sHtmlText", sHtmlText);
			return "pay/alipay/ali_html";
		} catch (Exception e) {
			return "pay/alipay/ali_reqfail";
		}
	}

	// ---------------end----------网银支付-----------支付宝alipay-------------------------------------------------

	// TODO-------------begin-------------微信扫码支付------------------------------------------------------------

	@Remark("微信扫码支付")
	@RequestMapping(value = "/wechatpay/pay", method = RequestMethod.POST)
	public String goWechatpayJsp(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//
		return "pay/wechatpay/qrcode_html";
	}

	@Remark("ajax返回code_url")
	@RequestMapping(value = "/wechatpay/create/code/url/do", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<String> createCodeUrl(HttpServletRequest request, @RequestBody MapContext requestData) {
		NumberFormat fenFormat = NumUtil.getNumFormat("#0.00");

		// 商户订单号 商户网站订单系统中唯一订单号 必填
		String no = requestData.getTypedValue("orderNo", String.class);
		//
		Result<String> result = Result.newOne();
		try {
			//
			String body = "";// 订单(商品)名称 必填
			String total_fee = "";// 总金额以分为单位，不带小数点
			String detail = "";// 订单（商品）详细描述
			String attach = "";// 回传参数
			String nonce_str = WechatpayCore.getNonceStr();// 随机字符串
			String notify_url = PayNotifyConfig.wx_notify_url_pc;// 异步通知
			String trade_type = "NATIVE";// 交易类型，JSAPI，NATIVE，APP
			String spbill_create_ip = "123.118.12.223";// 调用微信支付API的机器IP TODO 怎么获取
			String product_id = "9527";// 商品ID，trade_type=NATIVE，此参数必传。此id为二维码中包含的商品ID，商户自行定义 TODO 先随便写死，再商量

			// 判断是销售订单S还是E卡订单E
			if (no.startsWith("S")) {
				// 拼接订单名称与描述
				SaleOrder saleOrder = saleOrderService.getSaleOrderByNo(no);
				if (null != saleOrder) {
					// 1、服务
					List<SaleOrderSvc> saleOrderSvcs = saleOrderService.getSaleOrderSvcsByOrderId(saleOrder.getId());
					// 2、商品
					List<SaleOrderGoods> saleOrderGoods = saleOrderService.getSaleOrderGoodssByOrderNoAndUserId(no, saleOrder.getUserId());
					//
					if (saleOrderSvcs.size() != 0) {
						body = saleOrderSvcs.get(0).getSvcName() + "等";
						detail = saleOrderSvcs.get(0).getSvcName() + "等";
					} else if (saleOrderGoods.size() != 0) {
						body = saleOrderGoods.get(0).getProductName() + "等";
						detail = saleOrderGoods.get(0).getProductName() + "等";
					} else {
						body = no;
						detail = no;
					}
					//
					total_fee = WechatpayCore.getMoneyY2F(fenFormat.format(saleOrder.getAmountOuter()));
				} else {
					result.type = Type.warn;
					result.message = "微信支付异常，请选择其他支付方式!";
					return result;// 查找不到对应订单
				}
			} else {
				// 拼接订单名称与描述
				ECardOrder eCardOrder = eCardOrderService.getECardOrderByNo(no);
				if (null != eCardOrder) {
					body = eCardOrder.getCardName();
					total_fee = WechatpayCore.getMoneyY2F(fenFormat.format(eCardOrder.getAmount()));
					detail = eCardOrder.getCardName() + "," + eCardOrder.getQuantity() + "张";
					if (null != eCardOrder.getShopId()) {
						attach = eCardOrder.getCardCode() + "|" + eCardOrder.getShopId() + "|" + eCardOrder.getShopName();
					} else {
						attach = eCardOrder.getCardCode();
					}
				} else {
					result.type = Type.warn;
					result.message = "微信支付异常，请选择其他支付方式!";
					return result;// 查找不到对应订单
				}
			}

			SortedMap<String, String> packageParams = new TreeMap<String, String>();
			packageParams.put("appid", WechatpayConfig.appid_pc); // 公众号ID
			packageParams.put("mch_id", WechatpayConfig.partner_pc);// 商户号
			packageParams.put("nonce_str", nonce_str);
			packageParams.put("body", body);
			packageParams.put("detail", detail);
			packageParams.put("attach", attach);
			packageParams.put("out_trade_no", no);
			packageParams.put("total_fee", total_fee);
			packageParams.put("spbill_create_ip", spbill_create_ip);
			packageParams.put("notify_url", notify_url);
			packageParams.put("trade_type", trade_type);
			packageParams.put("product_id", product_id);

			RequestHandler reqHandler = new RequestHandler(null, null);
			reqHandler.init(WechatpayConfig.appid_pc, WechatpayConfig.appsecret_pc, WechatpayConfig.partnerkey_pc);

			String sign = reqHandler.createSign(packageParams);
			String xml = "<xml>" + "<appid>" + WechatpayConfig.appid_pc + "</appid>" + "<mch_id>" + WechatpayConfig.partner_pc + "</mch_id>" + "<nonce_str>" + nonce_str + "</nonce_str>" + "<sign>" + sign + "</sign>" + "<product_id>"
					+ product_id + "</product_id>"
					// + "<body><![CDATA[" + body + "]]></body>"
					+ "<body>" + body + "</body>" + "<detail>" + detail + "</detail>" + "<out_trade_no>" + no + "</out_trade_no>" + "<attach>" + attach + "</attach>" + "<total_fee>" + total_fee + "</total_fee>" + "<spbill_create_ip>"
					+ spbill_create_ip + "</spbill_create_ip>" + "<notify_url>" + notify_url + "</notify_url>" + "<trade_type>" + trade_type + "</trade_type>" + "</xml>";

			String code_url = GetWxOrderno.getCodeUrl(WechatpayConfig.createOrderURL, xml);
			result.data = code_url;
			result.message = "二维码url为空了";
		} catch (Exception e) {
			result.type = Type.warn;
			result.message = "微信支付异常，请选择其他支付方式!";
			e.printStackTrace();
		}
		//
		return result;
	}

	/**
	 * 微信支付——扫码支付成功后，异步通知
	 * 
	 * @author "WJJ"
	 * @date 2016年1月15日 下午6:42:09
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@Remark("微信服务器异步通知页面路径")
	@RequestMapping(value = "/wechatpay/async/notify", method = RequestMethod.POST)
	public void returnNotifyAsWechatpay(HttpServletRequest request, HttpServletResponse response) throws IOException {

		String inputLine;
		String notityXml = "";
		String resXml = "";

		try {
			while ((inputLine = request.getReader().readLine()) != null) {
				notityXml += inputLine;
			}
			request.getReader().close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println("接收到的报文：" + notityXml);

		Map m = WechatpayCore.parseXmlToList2(notityXml);
		WxPayResultDto wpr = new WxPayResultDto();
		String return_code = m.get("return_code").toString();
		if ("SUCCESS".equals(return_code)) {

			wpr.setReturnCode(return_code);// 返回状态码
			wpr.setReturn_msg(WechatpayCore.mapToString(m.get("return_msg")));
			wpr.setAppid(WechatpayCore.mapToString(m.get("appid")));// 公众账号ID
			wpr.setMchId(WechatpayCore.mapToString(m.get("mch_id")));// 商户号
			wpr.setNonceStr(WechatpayCore.mapToString(m.get("nonce_str")));// 随机字符串
			wpr.setSign(WechatpayCore.mapToString(m.get("sign")));// 签名
			wpr.setResultCode(WechatpayCore.mapToString(m.get("result_code")));// 业务结果
			wpr.setTradeType(WechatpayCore.mapToString(m.get("trade_type")));// 交易类型
			wpr.setBankType(WechatpayCore.mapToString(m.get("bank_type")));// 付款银行
			wpr.setTotalFee(WechatpayCore.mapToString(m.get("total_fee")));// 总金额 int
			wpr.setCashFee(WechatpayCore.mapToString(m.get("cash_fee")));// 现金支付金额
			wpr.setOutTradeNo(WechatpayCore.mapToString(m.get("out_trade_no")));// 商户订单号
			wpr.setTimeEnd(WechatpayCore.mapToString(m.get("time_end")));// 支付完成时间
			wpr.setTransactionId(WechatpayCore.mapToString(m.get("transaction_id")));// 微信支付订单号
			//
			wpr.setOpenid(WechatpayCore.mapToString(m.get("openid")));// 用户标识
			wpr.setIsSubscribe(WechatpayCore.mapToString(m.get("is_subscribe")));// 是否关注公众账号 否
			wpr.setFeeType(WechatpayCore.mapToString(m.get("fee_type")));// 货币种类 否
			wpr.setAttach(WechatpayCore.mapToString(m.get("attach")));// 回传参数 否
			wpr.setDevice_info(WechatpayCore.mapToString(m.get("device_info")));// 设备型号 否
			wpr.setErr_code(WechatpayCore.mapToString(m.get("err_code")));// 错误码 否
			wpr.setErr_code_des(WechatpayCore.mapToString(m.get("err_code_des")));// 错误描述 否

			String cash_fee_type = WechatpayCore.mapToString(m.get("cash_fee_type"));// 现金支付货币类型 否
			String coupon_fee = WechatpayCore.mapToString(m.get("coupon_fee"));// 代金券或立减优惠金额 否
			String coupon_count = WechatpayCore.mapToString(m.get("coupon_count"));// 代金券或立减优惠使用数量 否
			String coupon_id_$n = WechatpayCore.mapToString(m.get("coupon_id_$n"));// 代金券或立减优惠ID 否
			String coupon_fee_$n = WechatpayCore.mapToString(m.get("ccoupon_fee_$n"));// 单个代金券或立减优惠支付金额 否

		}
		System.out.println(wpr.toString());
		//
		if ("SUCCESS".equals(wpr.getResultCode())) {
			try {
				// 支付完成后的一系列业务操作
				payService.payfinishedOperationByWechatpay(wpr);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				// 支付成功
				resXml = "<xml>" + "<return_code><![CDATA[SUCCESS]]></return_code>" + "<return_msg><![CDATA[OK]]></return_msg>" + "</xml> ";
			}
		} else {
			resXml = "<xml>" + "<return_code><![CDATA[FAIL]]></return_code>" + "<return_msg><![CDATA[报文为空]]></return_msg>" + "</xml> ";
		}

		BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
		out.write(resXml.getBytes());
		out.flush();
		out.close();

	}

	// ---------------end----------微信扫码支付-------------------------------------------------

	// TODO-------------begin------------------------银联unionpay------------未使用，使用的servlet-------------------------------------

	@Remark("银联支付")
	@RequestMapping(value = "/unionpay/pay", method = RequestMethod.POST)
	public String goUnionpayPayJsp(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// 商户订单号 商户网站订单系统中唯一订单号 必填
		/*
		 * String no = payDto.getOrderId();
		 * 
		 * // 订单(商品)名称 必填 String subject = ""; // 付款金额 必填 两位小数点 String amount = ""; // 订单（商品）描述 String body = ""; // 回传参数 String extraParam = "";
		 * 
		 * // 判断是销售订单S还是E卡订单E if (no.startsWith("S")) { // 拼接订单名称与描述 SaleOrder saleOrder = saleOrderService.getSaleOrderByNo(no); List<SaleOrderSvc> saleOrderSvcs =
		 * saleOrderService.getSaleOrderSvcsByOrderId(saleOrder.getId());
		 * 
		 * if (saleOrderSvcs.size() != 0) { for (SaleOrderSvc saleOrderSvc : saleOrderSvcs) { body += saleOrderSvc.getSvcName() + ","; } subject = saleOrderSvcs.get(0).getSvcName(); body =
		 * body.substring(0, body.length() - 1); } else { subject = no; body = no; } amount = fenFormat.format(saleOrder.getAmount()); } else { // 拼接订单名称与描述 ECardOrder eCardOrder =
		 * eCardOrderService.getECardOrderByNo(no); subject = eCardOrder.getCardName(); amount = fenFormat.format(eCardOrder.getAmount()); body = eCardOrder.getCardName() + "," +
		 * eCardOrder.getQuantity() + "张"; extraParam = eCardOrder.getCardCode() + "|" + eCardOrder.getShopId() + "|" + eCardOrder.getShopName(); }
		 */

		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("version", UnopConfig.version);// 版本号
		paramMap.put("encoding", UnopConfig.encoding);// 字符集编码 默认"UTF-8"
		paramMap.put("signMethod", "01");// 签名方法 01 RSA
		paramMap.put("txnType", "01");// 交易类型 01-消费
		paramMap.put("txnSubType", "01");// 交易子类型 01:自助消费 02:订购 03:分期付款
		paramMap.put("bizType", "000201");// 业务类型 000201 B2C网关支付
		paramMap.put("channelType", "07");// 渠道类型 07-互联网渠道

		paramMap.put("backUrl", UnopConfig.backUrl);// 商户/收单后台接收地址 必送
		paramMap.put("frontUrl", UnopConfig.frontUrl);
		paramMap.put("accessType", "0");// 接入类型:商户接入填0 0- 商户 ， 1： 收单， 2：平台商户
		paramMap.put("merId", UnopConfig.merId);// 商户号码
		paramMap.put("orderId", UnopConfig.getOrderId());
		// paramMap.put("txnTime", DateUtil.getCurrentTimes());// 订单发送时间 格式： YYYYMMDDhhmmss 商户发送交易时间，根据自己系统或平台生成
		// paramMap.put("txnAmt", String.valueOf(payDto.getAmount().multiply(new BigDecimal(100))));// 交易金额 分
		paramMap.put("txnAmt", "1");// 交易金额 分
		paramMap.put("currencyCode", "156");// 交易币种
		paramMap.put("reqReserved", "回传参数");
		// paramMap.put("encryptCertId", "");// 加密证书ID 根据需求选送 参考接口规范 有报文域加密时，该字段必须上送
		// paramMap.put("customerIp", "");// 持卡人ip 根据需求选送 参考接口规范 防钓鱼用
		// 证书ID 调用 SDK 可自动获取并赋值
		// paramMap.put("certId", "31692114440333550101559775639582427889");
		// 签名 调用 SDK 可自动运算签名并赋值
		// paramMap.put("signature", "");
		try {
			SDKConfig config = SDKConfig.getConfig();
			config.loadPropertiesFromSrc();
			String frontRequestUrl = config.getFrontRequestUrl();
			System.out.println(frontRequestUrl);
			String html = SDKUtil.createAutoFormHtml(frontRequestUrl, SDKUtil.signData(paramMap, SDKUtil.encoding_UTF8), SDKUtil.encoding_UTF8);
			request.setAttribute("html", html);
			return "pay/unionpay";
		} catch (Exception e) {
			return null;
		}
	}

	// ---------------end----------------------银联unionpay-------unoppay7.09MB------------------------------------------

	// TODO-------------begin------------------------农行天津------------网关支付-----跳银联--------------------------------

	/**
	 * 农行网关跳银联收款
	 * 
	 * @author "WJJ"
	 * @date 2016年1月14日 下午1:39:03
	 * 
	 * @param req
	 * @param resp
	 * @return
	 * @throws Exception
	 */
	@Remark("农行接口，跳转银联支付")
	@RequestMapping(value = "/abc/pay", method = RequestMethod.POST)
	public String goABCPayJsp(HttpServletRequest req, HttpServletResponse resp) throws Exception {
		NumberFormat fenFormat = NumUtil.getNumFormat("#0.00");

		// req.setCharacterEncoding("GBK");
		String orderNo = req.getParameter("orderNo");
		String linkType = "1";

		String subject = "";// 订单(商品)名称
		String total_fee = "";// 付款金额 两位小数点
		String OrderDate = "";// 订单日期
		String OrderTime = "";// 订单时间

		// 判断是销售订单S还是E卡订单E
		if (orderNo.startsWith("S")) {
			// 拼接订单名称与描述
			SaleOrder saleOrder = saleOrderService.getSaleOrderByNo(orderNo);
			if (null != saleOrder) {
				List<SaleOrderSvc> saleOrderSvcs = saleOrderService.getSaleOrderSvcsByOrderId(saleOrder.getId());
				List<SaleOrderGoods> saleOrderGoods = saleOrderService.getSaleOrderGoodssByOrderNoAndUserId(orderNo, saleOrder.getUserId());
				//
				if (saleOrderSvcs.size() != 0) {
					subject = saleOrderSvcs.get(0).getSvcName() + "等";
				} else if (saleOrderGoods.size() != 0) {
					subject = saleOrderGoods.get(0).getProductName() + "等";
				} else {
					subject = orderNo;
				}
				total_fee = fenFormat.format(saleOrder.getAmountOuter());
				Date createTime = saleOrder.getCreateTime();
				OrderDate = DateUtil.DATE_DIR_FORMAT.format(createTime);
				OrderTime = DateUtil.STD_TIME_FORMAT.format(createTime);
			} else {
				// resp.sendRedirect("/web-front/pay/abcpay/req/fail");// 查找不到对应订单
				return "pay/abcpay/abc_payFail";
			}
		} else {
			// 拼接订单名称与描述
			ECardOrder eCardOrder = eCardOrderService.getECardOrderByNo(orderNo);
			if (null != eCardOrder) {
				subject = eCardOrder.getCardName();
				total_fee = fenFormat.format(eCardOrder.getAmount());
				Date createTime = eCardOrder.getCreateTime();
				OrderDate = DateUtil.DATE_DIR_FORMAT.format(createTime);
				OrderTime = DateUtil.STD_TIME_FORMAT.format(createTime);
			} else {
				// resp.sendRedirect("/web-front/pay/abcpay/req/fail");// 查找不到对应订单
				return "pay/abcpay/abc_payFail";
			}
		}
		//
		JSON json = payService.createReqDataForAbcpay(orderNo, linkType, subject, total_fee, OrderDate, OrderTime);

		String ReturnCode = json.GetKeyValue("ReturnCode");
		String ErrorMessage = json.GetKeyValue("ErrorMessage");
		String PaymentURL = json.GetKeyValue("PaymentURL");

		resp.setHeader("Cache-Control", "no-cache");
		if (ReturnCode.equals("0000")) {
			// 请求成功
			System.out.println("=======================成功=============================");
			System.out.println(ReturnCode);
			System.out.println(ErrorMessage);
			System.out.println(PaymentURL);

			resp.sendRedirect(PaymentURL);
			return null;
		} else {
			req.setAttribute("ReturnCode", ReturnCode);
			req.setAttribute("ErrorMessage", ErrorMessage);

			System.out.println("======================失败==============================");
			System.out.println(ReturnCode);
			System.out.println(ErrorMessage);
			System.out.println(PaymentURL);

			// 请求错误页面
			// resp.sendRedirect("/web-front/pay/abcpay/req/fail");
			return "pay/abcpay/abc_payFail";
		}
	}

	/**
	 * 未用，选择的服务器通知方式
	 * 
	 * @author "WJJ"
	 * @date 2016年1月14日 下午1:38:37
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@Remark("农行跳银联-页面跳转同步通知页面路径-页面通知方式")
	@RequestMapping(value = "/abc/page/sync/notify", method = RequestMethod.POST)
	public String goReturnJspByAbcAsPage(HttpServletRequest request, HttpServletResponse response) throws IOException {

		// 1、取得MSG参数，并利用此参数值生成支付结果对象
		String msg = request.getParameter("MSG");
		PaymentResult tResult;
		try {
			tResult = new PaymentResult(msg);

			// 2、判断支付结果状态，进行后续操作
			if (tResult.isSuccess()) {
				// 3、支付成功并且验签、解析成功
				String trxType = tResult.getValue("TrxType");//
				String no = tResult.getValue("OrderNo");// 订单编号
				String amount = tResult.getValue("Amount");// 订单金额
				String batchNo = tResult.getValue("BatchNo");// 交易批次号
				String voucherNo = tResult.getValue("VoucherNo");// 交易凭证号（建议使用 iRspRef 作为对账依据）
				String hostDate = tResult.getValue("HostDate");// 银行交易日期（YYYY/MM/DD）
				String hostTime = tResult.getValue("HostTime");// 银行交易时间（HH:MM:SS）
				String merchantRemarks = tResult.getValue("MerchantRemarks");// 商户备注信息（商户在支付请求时所提交的信息）
				String payType = tResult.getValue("PayType");// 消费者支付方式
				String notifyType = tResult.getValue("NotifyType");// 支付结果通知方式
				String iRspRef = tResult.getValue("iRspRef");// 银行返回交易流水号

				if (no.startsWith("S")) {
					request.setAttribute("no", no);
					return "pay/abcpay/abc_return_url";// 支付成功通知页面
				} else {
					request.setAttribute("no", no);
					return "pay/abcpay/abc_return_url_ecard";// 支付成功通知页面
				}

			} else {
				// 4、支付成功但是由于验签或者解析报文等操作失败
				String returnCode = tResult.getReturnCode();
				String errorMessage = tResult.getErrorMessage();
				return "pay/alipay/ali_payFail";// 支付失败页面
			}

		} catch (TrxException e) {
			return "pay/alipay/ali_verifyFail";// 支付失败页面
		}

	}

	/**
	 * 农行跳银联-页面跳转同步通知页面路径-服务器通知方式
	 * 
	 * @author "WJJ"
	 * @date 2016年1月14日 下午1:39:30
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws IOException
	 */
	@Remark("农行跳银联-页面跳转同步通知页面路径-服务器通知方式")
	@RequestMapping(value = "/abc/server/sync/notify", method = RequestMethod.POST)
	public String goReturnJspByAbcAsServer(HttpServletRequest request, HttpServletResponse response) throws IOException {

		// 0、设定商户结果显示页面
		String tMerchantPage = "";

		// 1、取得MSG参数，并利用此参数值生成支付结果对象
		String msg = request.getParameter("MSG");
		PaymentResult tResult;
		try {
			tResult = new PaymentResult(msg);

			String no = tResult.getValue("OrderNo");// 订单编号
			String tradeNo = tResult.getValue("iRspRef");// 银行返回交易流水号
			String amount = tResult.getValue("Amount");// 订单金额

			String trxType = tResult.getValue("TrxType");// 交易种类
			String batchNo = tResult.getValue("BatchNo");// 交易批次号
			String voucherNo = tResult.getValue("VoucherNo");// 交易凭证号（建议使用 iRspRef 作为对账依据）
			String hostDate = tResult.getValue("HostDate");// 银行交易日期（YYYY/MM/DD）
			String hostTime = tResult.getValue("HostTime");// 银行交易时间（HH:MM:SS）
			// String merchantRemarks = tResult.getValue("MerchantRemarks");// 商户备注信息（商户在支付请求时所提交的信息）
			String payType = tResult.getValue("PayType");// 消费者支付方式
			String notifyType = tResult.getValue("NotifyType");// 支付结果通知方式

			// 2、支付完成，处理业务逻辑
			payService.payFinishedOperationByAbc(no, amount, tradeNo);
			no = no.substring(0, no.length() - 3);

			// 2、判断支付结果处理状态，进行后续操作
			if (tResult.isSuccess()) {
				// 3、支付成功并且验签、解析成功
				tMerchantPage = PayNotifyConfig.abc_return_url_pc_s + "?no=" + no;
				// 把需要给商户展示的页面传入
				request.setAttribute("tMerchantPage", tMerchantPage);
			} else {
				// 4、支付成功但是由于验签或者解析报文等操作失败
				tMerchantPage = PayNotifyConfig.abc_return_url_pc_f + "?no=" + no;
				// 把需要给商户展示的页面传入
				request.setAttribute("tMerchantPage", tMerchantPage);
			}

			return "pay/abcpay/abc_notify";
		} catch (TrxException e) {
			e.printStackTrace();
			return "pay/abcpay/abc_errorFail"; // 程序报错失败页面
		}

	}

	/**
	 * 农行跳银联-支付成功-服务器通知：跳转给商户的前台页面
	 * 
	 * @author "WJJ"
	 * @date 2016年1月14日 下午1:42:08
	 * 
	 * @param request
	 * @return
	 */
	@Remark("农行跳银联-支付成功-服务器通知")
	@RequestMapping(value = "/abc/pay/success/notify", method = RequestMethod.GET)
	public String goPageForAbcAsSuccess(HttpServletRequest request) {
		//
		String no = request.getParameter("no");
		// 把订单号、商品名称、交易金额、商品描述等传到页面展现
		request.setAttribute("no", no);
		//
		if (no.startsWith("S")) {
			// 判断，如果是服务套餐票，则跳转到payresult.jsp。
			return "pay/abcpay/abc_return_url";// 支付成功通知页面
		} else {
			return "pay/abcpay/abc_return_url_ecard";// 购买E卡支付成功通知页面
		}

	}

	/**
	 * 农行跳银联-支付失败-服务器通知：跳转给商户的前台页面
	 * 
	 * @author "WJJ"
	 * @date 2016年1月14日 下午1:44:04
	 * 
	 * @param request
	 * @return
	 */
	@Remark("农行跳银联-支付失败-服务器通知")
	@RequestMapping(value = "/abc/pay/fail/notify", method = RequestMethod.GET)
	public String goPageForAbcAsFail(HttpServletRequest request) {
		return "pay/abcpay/abc_payFail";
	}

	@Remark("农行跳银联-请求失败-未找到订单或者请求出错")
	@RequestMapping(value = "/pay/abcpay/req/fail", method = RequestMethod.GET)
	public String goPageForAbcAsReqFail(HttpServletRequest request) {
		return "pay/abcpay/abc_payFail";
	}

	// -------------end------------------------农行天津------------网关支付-------------------------------------

}
