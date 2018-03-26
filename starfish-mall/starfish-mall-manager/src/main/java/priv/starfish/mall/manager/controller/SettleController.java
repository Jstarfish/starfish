package priv.starfish.mall.manager.controller;

import com.abc.trustpay.client.JSON;
import com.abc.trustpay.client.ebus.BatchRefundRequest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.model.*;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.pay.alipay.util.AlipayNotify;
import priv.starfish.common.pay.alipay.util.AlipaySubmit;
import priv.starfish.common.pay.wechatpay.util.GetWxOrderno;
import priv.starfish.common.pay.wechatpay.util.RequestHandler;
import priv.starfish.common.pay.wechatpay.util.WechatpayConfig;
import priv.starfish.common.pay.wechatpay.util.WechatpayCore;
import priv.starfish.common.pay.wechatpay.util.http.ClientCustomSSL;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.NumUtil;
import priv.starfish.common.util.TypeUtil;
import priv.starfish.mall.web.base.BaseController;
import priv.starfish.mall.order.dict.OrderAction;
import priv.starfish.mall.order.entity.SaleOrder;
import priv.starfish.mall.service.PayService;
import priv.starfish.mall.service.SaleOrderService;
import priv.starfish.mall.service.SettleService;
import priv.starfish.mall.settle.dto.WxPayResultDto;
import priv.starfish.mall.settle.entity.*;
import priv.starfish.mall.xpay.channel.ebdirect.bean.DpRequest;
import priv.starfish.mall.xpay.channel.ebdirect.bean.RspData;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.*;

/**
 * 结算相关
 * 
 * @author "WJJ"
 * @date 2015年10月18日 下午4:07:05
 *
 */
@Controller
@RequestMapping("/settle")
public class SettleController extends BaseController {

	@Resource
	SettleService settleService;

	@Resource
	PayService payService;

	@Resource
	SaleOrderService saleOrderService;

	@Remark("后台人员--待结算列表页--点击选择账户")
	@RequestMapping(value = "/bound/acct/by/settle/do", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<SettleProcess> boundAcctBySettle(HttpServletRequest request, @RequestBody MapContext requestData) {
		//
		Result<SettleProcess> result = Result.newOne();

		try {
			// 1、通过条件商户ID和settleWayCode查询出商户的结算资金账户。2、根据结算单ID，更新此结算单结算方式、账户名、结算账户号
			boolean ok = settleService.updateSettleProcessAcctInfo(requestData);
			result.message = "绑定成功!";

			if (!ok) {
				result.type = Type.warn;
				result.message = "绑定失败!";
			}
		} catch (Exception e) {
			result.type = Type.warn;
			result.message = "绑定异常!";
			e.printStackTrace();
		}
		//
		return result;
	}

	// TODO-------------begin-------------批量退款有密接口-----------支付宝alipay------------------------------------------------

	/**
	 * 后台，退款申请页面
	 * 
	 * @author "WJJ"
	 * @date 2015年11月17日 下午4:39:54
	 * 
	 * @return
	 */
	@Remark("退款申请页面")
	@RequestMapping(value = "/sale/refund/apply/jsp", method = RequestMethod.GET)
	public String goRefundApplyJsp() {
		return "settle/refundApply";
	}

	/**
	 * 分页查询支付记录里，状态为可退款的
	 * 
	 * @author "WJJ"
	 * @date 2015年11月17日 下午4:40:08
	 * 
	 * @param request
	 * @return
	 */
	@Remark("分页查询支付记录里，状态为可退款的")
	@RequestMapping(value = "/sale/refund/apply/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<PayRefundRec> getCanRefundRecs(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<PayRefundRec> paginatedList = settleService.getCanRefundRecsByFilter(paginatedFilter);
		//
		JqGridPage<PayRefundRec> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 后台人员提交退款申请
	 * 
	 * @author "WJJ"
	 * @date 2015年11月17日 下午4:40:28
	 * 
	 * @param request
	 * @param payRefundRec
	 * @return
	 */
	@Remark("后台人员提交退款申请")
	@RequestMapping(value = "/refund/apply/do", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<PayRefundRec> applyRefund(HttpServletRequest request, @RequestBody PayRefundRec payRefundRec) {
		//
		Result<PayRefundRec> result = Result.newOne();
		//
		try {
			PayRefundRec prr = settleService.getSalePayRecById(payRefundRec.getId());
			boolean flag = settleService.judgeSaleOrderSvcPackId(prr.getNo());
			Integer count = saleOrderService.getUsedSvcPackTicketCount(prr.getUserId(), prr.getNo());
			if (flag) {
				if (count == 0) {
					//
					payRefundRec.setApplyRefundTime(new Date());
					boolean ok = settleService.updateRefundStatus(payRefundRec);
					result.message = "申请成功!";

					if (ok) {
						// 插入销售订单 操作记录
						UserContext userContext = getUserContext(request);
						String action = OrderAction.applyRefund.toString();
						saleOrderService.saveSaleOrderRecord(action, userContext, payRefundRec);
					} else {
						result.type = Type.warn;
						result.message = "申请失败!";
					}
				} else {
					result.type = Type.warn;
					result.message = "此订单包含服务套餐票，且已有服务享用，不可退款。";
				}
			} else {
				//
				payRefundRec.setApplyRefundTime(new Date());
				boolean ok = settleService.updateRefundStatus(payRefundRec);
				result.message = "申请成功!";

				if (ok) {
					// 插入销售订单 操作记录
					UserContext userContext = getUserContext(request);
					String action = OrderAction.applyRefund.toString();
					saleOrderService.saveSaleOrderRecord(action, userContext, payRefundRec);
				} else {
					result.type = Type.warn;
					result.message = "申请失败!";
				}
			}
		} catch (Exception e) {
			result.type = Type.warn;
			result.message = "申请异常";
			e.printStackTrace();
		}
		//
		return result;
	}

	/**
	 * 退款审核页面
	 * 
	 * @author "WJJ"
	 * @date 2015年11月17日 下午4:40:42
	 * 
	 * @return
	 */
	@Remark("退款审核页面")
	@RequestMapping(value = "/sale/refund/audit/jsp", method = RequestMethod.GET)
	public String goRefundAuditJsp() {
		return "settle/refundAudit";
	}

	/**
	 * 分页查询支付记录里，提交退款申请后，待审核的记录
	 * 
	 * @author "WJJ"
	 * @date 2015年11月17日 下午4:41:02
	 * 
	 * @param request
	 * @return
	 */
	@Remark("分页查询支付记录里，退款待审核的记录")
	@RequestMapping(value = "/sale/refund/audit/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<PayRefundRec> getRefundAuditRecs(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<PayRefundRec> paginatedList = settleService.getRefundAuditRecs(paginatedFilter);
		//
		JqGridPage<PayRefundRec> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 后台人员提交退款订单的审核结果
	 * 
	 * @author "WJJ"
	 * @date 2015年11月17日 下午4:41:19
	 * 
	 * @param request
	 * @param payRefundRec
	 * @return
	 */
	@Remark("后台人员提交审核结果")
	@RequestMapping(value = "/refund/audit/do", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<PayRefundRec> refundAudit(HttpServletRequest request, @RequestBody PayRefundRec payRefundRec) {
		//
		Result<PayRefundRec> result = Result.newOne();
		payRefundRec.setAuditRefundTime(new Date());

		boolean ok = settleService.updateRefundStatus(payRefundRec);
		result.message = "申请成功!";

		if (ok) {
			// 插入销售订单 操作记录
			UserContext userContext = getUserContext(request);
			String action = "";
			if (payRefundRec.getRefundStatus() == 3) {
				action = OrderAction.agreeRefund.toString();
			} else {
				action = OrderAction.refuseRefund.toString();
			}
			saleOrderService.saveSaleOrderRecord(action, userContext, payRefundRec);
		} else {
			result.type = Type.warn;
			result.message = "申请失败!";
		}
		//
		return result;
	}

	/**
	 * 执行退款操作的页面
	 * 
	 * @author "WJJ"
	 * @date 2015年11月17日 下午4:41:45
	 * 
	 * @return
	 */
	@Remark("退款操作页面")
	@RequestMapping(value = "/sale/refund/jsp", method = RequestMethod.GET)
	public String goRefundJsp() {
		return "settle/refund";
	}

	/**
	 * 分页查询支付记录里，退款申请审核通过,待退款的记录
	 * 
	 * @author "WJJ"
	 * @date 2015年11月17日 下午4:42:02
	 * 
	 * @param request
	 * @return
	 */
	@Remark("分页查询支付记录里，待退款的记录")
	@RequestMapping(value = "/sale/toRefund/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<PayRefundRec> getToRefundRecs(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<PayRefundRec> paginatedList = settleService.getToRefundAuditRecs(paginatedFilter);
		//
		JqGridPage<PayRefundRec> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 后台人员提交退款申请
	 * 
	 * @author "WJJ"
	 * @date 2015年11月17日 下午4:42:21
	 * 
	 * @param request
	 * @param requestData
	 * @return
	 */
	@Remark("后台人员提交退款操作")
	@RequestMapping(value = "/refunding/do", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<PayRefundRec> refundDoing(HttpServletRequest request, @RequestBody MapContext requestData) {
		//
		Result<PayRefundRec> result = Result.newOne();

		UserContext userContext = getUserContext(request);
		boolean ok = settleService.updateRefundStatusToRefunding(userContext, requestData);
		result.message = "申请成功!";

		if (!ok) {
			result.type = Type.warn;
			result.message = "申请失败!";
		}
		//
		return result;
	}

	/**
	 * 后台人员提交取消退款操作
	 * 
	 * @author "WJJ"
	 * @date 2015年11月17日 下午4:42:21
	 * 
	 * @param request
	 * @param requestData
	 * @return
	 */
	@Remark("后台人员提交取消退款操作")
	@RequestMapping(value = "/refund/cancel/do", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<PayRefundRec> cancelRefund(HttpServletRequest request, @RequestBody MapContext requestData) {
		//
		Result<PayRefundRec> result = Result.newOne();

		UserContext userContext = getUserContext(request);
		boolean ok = settleService.updateRefundStatusToRefuseRefund(userContext, requestData);
		result.message = "申请成功!";

		if (!ok) {
			result.type = Type.warn;
			result.message = "申请失败!";
		}
		//
		return result;
	}

	/**
	 * 支付宝--调用支付宝批量退款接口，执行退款
	 * 
	 * @author "WJJ"
	 * @date 2015年11月17日 下午4:42:29
	 * 
	 * @param request
	 * @param response
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@Remark("支付宝--批量退款")
	@RequestMapping(value = "/alipay/batch/refund", method = RequestMethod.POST)
	public String goPageJspRefund(HttpServletRequest request, HttpServletResponse response, String ids) throws Exception {

		// 建立请求
		try {
			// 组装退款请求数据
			Map<String, String> sParaTemp = settleService.createReqRefundData(ids);

			String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "post", "确认");
			request.setAttribute("sHtmlText", sHtmlText);
			return "settle/alipay/ali_html";
		} catch (Exception e) {
			return "settle/alipay/ali_reqfailRefund";
		}
	}

	/**
	 * 支付宝--批量退款服务器异步通知页面 不能执行任何页面跳转
	 * 
	 * @author "WJJ"
	 * @date 2015年10月22日 下午1:55:01
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@Remark("支付宝--批量退款服务器异步通知页面路径")
	@RequestMapping(value = "/alipay/batch/refund/notify_url", method = RequestMethod.POST)
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

		// 批量退款数据中转账成功的笔数
		// String success_num = request.getParameter("success_num");

		// 批次号
		String batch_no = request.getParameter("batch_no");
		// 批量退款数据中的详细信息 交易号^退款金额^处理结果#
		String result_details = request.getParameter("result_details");

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		if (AlipayNotify.verify(params)) {// 验证成功
			//
			try {
				// 退款完成后的操作
				settleService.refundFinishedOperation(result_details, batch_no);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				out.print("success"); // 回调成功后，给支付宝返回success 不能修改
			}
		} else {// 验证失败
			out.print("fail");
		}
	}

	// ---------------end-----------批量退款有密接口-----------支付宝alipay-------313809------------------------------------------

	// ----TODO-----------begin-----------E卡批量退款----------------------------------------------------
	/**
	 * 给用E卡全额支付的订单退款
	 * 
	 * @author "WJJ"
	 * @date 2015年11月24日 下午5:10:41
	 * 
	 * @param request
	 * @param requestData
	 * @return
	 */
	@Remark("E卡批量退款")
	@RequestMapping(value = "/refund/for/eCard/do", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<PayRefundRec> refundDoForECard(HttpServletRequest request, @RequestBody MapContext requestData) {
		//
		Result<PayRefundRec> result = Result.newOne();
		//
		UserContext userContext = getUserContext(request);
		// 执行E卡退款、及后续操作
		try {
			Boolean ok = settleService.refundDoForECard(requestData, userContext);
			result.message = "退款成功!";
			if (!ok) {
				result.type = Type.warn;
				result.message = "退款失败!";
			}
		} catch (Exception e) {
			result.type = Type.warn;
			result.message = "退款异常!";
			e.printStackTrace();
		}

		//
		return result;
	}

	// ---------------end-----------E卡批量退款----------------------------------------------------

	// ----TODO-----------begin-----------微信支付（扫码）-----退款-----------------------------------------------

	@Remark("微信--单笔退款操作--人工ajax")
	@RequestMapping(value = "/wechatpay/single/refund", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<PayRefundRec> refundDoForWechatpayAsSingle(HttpServletRequest request, HttpServletResponse response, @RequestBody MapContext requestData) throws Exception {
		//
		Result<PayRefundRec> result = Result.newOne();
		UserContext userContext = getUserContext(request);
		//
		String id = requestData.getTypedValue("id", String.class);
		PayRefundRec payRefundRec = payService.getPayRecById(Long.valueOf(id));
		// 必须
		// String transaction_id = "";// 微信订单号 支付完成后，微信生成的订单号
		String out_trade_no = payRefundRec.getNo();// 订单号 商户传的
		String total_fee = WechatpayCore.getMoneyY2F(payRefundRec.getTotalFee());// 总金额
		String refund_fee = WechatpayCore.getMoneyY2F(payRefundRec.getTotalFee());// 退款金额

		String appid = WechatpayConfig.appid_pc; // 微信公众号appid
		String mch_id = WechatpayConfig.partner_pc; // 微信商户id
		String nonce_str = WechatpayCore.getNonceStr();// 随机字符串
		String out_refund_no = WechatpayCore.getNonceStr();// 退款单号 商户生成的。
		String op_user_id = WechatpayConfig.partner_pc;// 默认就是MCHID

		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("out_trade_no", out_trade_no);
		packageParams.put("out_refund_no", out_refund_no);
		packageParams.put("total_fee", total_fee);
		packageParams.put("refund_fee", refund_fee);
		packageParams.put("op_user_id", op_user_id);

		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(appid, WechatpayConfig.appsecret_pc, WechatpayConfig.partnerkey_pc);

		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml>" + "<appid>" + appid + "</appid>" + "<mch_id>" + mch_id + "</mch_id>" + "<nonce_str>" + nonce_str + "</nonce_str>" + "<sign><![CDATA[" + sign + "]]></sign>" + "<out_trade_no>" + out_trade_no + "</out_trade_no>"
				+ "<out_refund_no>" + out_refund_no + "</out_refund_no>" + "<total_fee>" + total_fee + "</total_fee>" + "<refund_fee>" + refund_fee + "</refund_fee>" + "<op_user_id>" + op_user_id + "</op_user_id>" + "</xml>";
		try {
			String xmlString = ClientCustomSSL.doRefund(WechatpayConfig.createRefundURL, xml);
			System.out.println(xmlString);
			// Map map = GetWxOrderno.doXMLParse(xmlString);
			Map map = WechatpayCore.parseXmlToList2(xmlString);
			// 必须
			String return_code = WechatpayCore.mapToString(map.get("return_code"));
			// 非必须
			String return_msg = WechatpayCore.mapToString(map.get("return_msg"));
			WxPayResultDto wpr = new WxPayResultDto();
			if ("SUCCESS".equals(return_code)) {
				wpr.setReturnCode(return_code);// 返回状态码
				wpr.setReturn_msg(return_msg);
				wpr.setResultCode(WechatpayCore.mapToString(map.get("result_code")));// 业务结果
				wpr.setAppid(WechatpayCore.mapToString(map.get("appid")));// 公众账号ID
				wpr.setMchId(WechatpayCore.mapToString(map.get("mch_id")));// 商户号
				wpr.setNonceStr(WechatpayCore.mapToString(map.get("nonce_str")));// 随机字符串
				wpr.setSign(WechatpayCore.mapToString(map.get("sign")));// 签名
				wpr.setTransactionId(WechatpayCore.mapToString(map.get("transaction_id")));// 微信支付订单号
				wpr.setOutTradeNo(WechatpayCore.mapToString(map.get("out_trade_no")));// 商户订单号
				wpr.setTotalFee(WechatpayCore.mapToString(map.get("total_fee")));// 总金额 int
				wpr.setCashFee(WechatpayCore.mapToString(map.get("cash_fee")));// 现金支付金额
				wpr.setOut_refund_no(WechatpayCore.mapToString(map.get("out_refund_no")));// 商户退款单号
				wpr.setRefund_id(WechatpayCore.mapToString(map.get("refund_id")));// 微信退款单号
				wpr.setRefund_fee(WechatpayCore.mapToString(map.get("refund_fee")));// 退款金额
				// 非必须
				wpr.setErr_code(WechatpayCore.mapToString(map.get("err_code")));// 错误码 否
				wpr.setErr_code_des(WechatpayCore.mapToString(map.get("err_code_des")));// 错误描述 否
				wpr.setDevice_info(WechatpayCore.mapToString(map.get("device_info")));// 设备型号 否
				wpr.setFeeType(WechatpayCore.mapToString(map.get("fee_type")));// 货币种类 否
				wpr.setRefund_channel(WechatpayCore.mapToString(map.get("refund_channel")));// 退款渠道
				wpr.setCash_refund_fee(WechatpayCore.mapToString(map.get("cash_refund_fee")));// 现金退款金额
			}
			System.out.println(wpr.toString());
			//
			if ("SUCCESS".equals(wpr.getResultCode())) {
				try {
					// 退款申请完成后一系列操作
					Boolean ok = settleService.applyRefundFinishedByWechatpay(wpr, payRefundRec, userContext);
					result.message = "退款申请成功!";
					if (!ok) {
						result.type = Type.warn;
						result.message = "退款申请成功，数据更新失败，请联系技术人员!";
					}
				} catch (Exception e) {
					result.type = Type.warn;
					result.message = "退款申请成功，程序出错导致数据更新失败，请联系技术人员!";
					e.printStackTrace();
					return result;
				}
			} else {
				result.type = Type.warn;
				result.message = "退款失败:" + wpr.getErr_code_des() + "请把错误码(" + wpr.getErr_code() + ")转告给技术人员";
			}

		} catch (Exception e) {
			result.type = Type.warn;
			result.message = "请求出错，请联系技术人员!";
			e.printStackTrace();
		}

		return result;

	}

	/**
	 * 微信支付——退款查询,根据得到的状态，更新已申请退款的状态
	 * 
	 * @author "WJJ"
	 * @date 2016年1月18日 下午3:38:14
	 * 
	 * @param request
	 * @param response
	 * @param requestData
	 * @return
	 * @throws Exception
	 */
	@Remark("微信--退款查询操作--人工ajax")
	@RequestMapping(value = "/wechatpay/query/refund", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<PayRefundRec> refundQueryForWechatpay(HttpServletRequest request, HttpServletResponse response, @RequestBody MapContext requestData) throws Exception {
		//
		Result<PayRefundRec> result = Result.newOne();
		UserContext userContext = getUserContext(request);
		//
		String id = requestData.getTypedValue("id", String.class);
		PayRefundRec payRefundRec = payService.getPayRecById(Long.valueOf(id));
		// 必须
		String appid = WechatpayConfig.appid_pc; // 微信公众号apid
		String mch_id = WechatpayConfig.partner_pc; // 微信商户id
		String nonce_str = WechatpayCore.getNonceStr();// 随机字符串

		// String transaction_id = "";// 微信订单号 支付完成后，微信生成的订单号
		String out_trade_no = payRefundRec.getNo();// 订单号 商户传的
		// String out_refund_no = getNonceStr();// 退款单号 商户生成的。
		// String refund_id = "";//微信退款单号

		SortedMap<String, String> packageParams = new TreeMap<String, String>();
		packageParams.put("appid", appid);
		packageParams.put("mch_id", mch_id);
		packageParams.put("nonce_str", nonce_str);
		packageParams.put("out_trade_no", out_trade_no);

		RequestHandler reqHandler = new RequestHandler(null, null);
		reqHandler.init(appid, WechatpayConfig.appsecret_pc, WechatpayConfig.partnerkey_pc);

		String sign = reqHandler.createSign(packageParams);
		String xml = "<xml>" + "<appid>" + appid + "</appid>" + "<mch_id>" + mch_id + "</mch_id>" + "<nonce_str>" + nonce_str + "</nonce_str>" + "<sign><![CDATA[" + sign + "]]></sign>" + "<out_trade_no>" + out_trade_no + "</out_trade_no>"
				+ "</xml>";
		try {
			String xmlString = GetWxOrderno.getReturnXml(WechatpayConfig.createRefundQueryURL, xml);
			System.out.println(xmlString);
			// Map map = GetWxOrderno.doXMLParse(xmlString);
			Map map = WechatpayCore.parseXmlToList2(xmlString);
			// 必须
			String return_code = WechatpayCore.mapToString(map.get("return_code"));// 返回状态码 SUCCESS/FAIL
			// 非必须
			String return_msg = WechatpayCore.mapToString(map.get("return_msg"));
			WxPayResultDto wpr = new WxPayResultDto();
			if ("SUCCESS".equals(return_code)) {
				wpr.setReturnCode(return_code);// 返回状态码
				wpr.setReturn_msg(return_msg);
				wpr.setResultCode(WechatpayCore.mapToString(map.get("result_code")));// 业务结果
				wpr.setMchId(WechatpayCore.mapToString(map.get("mch_id")));// 商户号
				wpr.setAppid(WechatpayCore.mapToString(map.get("appid")));// 公众账号ID
				wpr.setNonceStr(WechatpayCore.mapToString(map.get("nonce_str")));// 随机字符串
				wpr.setSign(WechatpayCore.mapToString(map.get("sign")));// 签名
				wpr.setTransactionId(WechatpayCore.mapToString(map.get("transaction_id")));// 微信支付订单号
				wpr.setOutTradeNo(WechatpayCore.mapToString(map.get("out_trade_no")));// 商户订单号
				wpr.setTotalFee(WechatpayCore.mapToString(map.get("total_fee")));// 总金额 int
				wpr.setCashFee(WechatpayCore.mapToString(map.get("cash_fee")));// 现金支付金额
				wpr.setOut_refund_no(WechatpayCore.mapToString(map.get("out_refund_no")));// 商户退款单号
				wpr.setRefund_id(WechatpayCore.mapToString(map.get("refund_id")));// 微信退款单号
				wpr.setRefund_fee(WechatpayCore.mapToString(map.get("refund_fee")));// 退款金额
				wpr.setRefund_count(WechatpayCore.mapToString(map.get("refund_count")));// 退款笔数
				// 状态 SUCCESS—退款成功 FAIL—退款失败 PROCESSING—退款处理中 NOTSURE—未确定，需要商户原退款单号重新发起 CHANGE—转入代发，商户线下或者线上转账
				wpr.setRefund_status_$n(WechatpayCore.mapToString(map.get("refund_status_$n")));// 退款状态
				wpr.setRefund_recv_accout_$n(WechatpayCore.mapToString(map.get("refund_recv_accout_$n")));// 退款入账账户 1.{银行名称}{卡类型}{卡尾号} 2.支付用户零钱
				// 非必须
				wpr.setErr_code(WechatpayCore.mapToString(map.get("err_code")));// 错误码 否
				wpr.setErr_code_des(WechatpayCore.mapToString(map.get("err_code_des")));// 错误描述 否
				wpr.setFeeType(WechatpayCore.mapToString(map.get("fee_type")));// 货币种类 否
				wpr.setDevice_info(WechatpayCore.mapToString(map.get("device_info")));// 设备型号 否
				wpr.setRefund_channel(WechatpayCore.mapToString(map.get("refund_channel")));// 退款渠道 ORIGINAL—原路退款 BALANCE—退回到余额
				wpr.setCash_refund_fee(WechatpayCore.mapToString(map.get("cash_refund_fee")));// 现金退款金额
			}
			System.out.println(wpr.toString());
			//
			if ("SUCCESS".equals(wpr.getResultCode())) {
				try {
					// 对退款查询成功，得到的结果的操作
					Boolean ok = settleService.queryRefundFinishedByWechatpay(wpr, payRefundRec, userContext);
					result.message = "退款查询成功!";
					if (!ok) {
						result.type = Type.warn;
						result.message = "退款查询成功，数据更新失败，请联系技术人员!";
					}
				} catch (Exception e) {
					result.type = Type.warn;
					result.message = "退款查询成功，程序出错导致数据更新失败，请联系技术人员!";
					e.printStackTrace();
					return result;
				}
			} else {
				result.type = Type.warn;
				result.message = "退款查询失败:" + wpr.getErr_code_des() + "请把错误码(" + wpr.getErr_code() + ")转告给技术人员";
			}

		} catch (Exception e) {
			result.type = Type.warn;
			result.message = "请求查询出错，请联系技术人员!";
			e.printStackTrace();
		}

		return result;

	}

	// ---------------end-----------微信支付（扫码）-----退款-----------------------------------------------

	// ----TODO-----------begin-----------农行跳银联-----退款-----------------------------------------------

	@Remark("农行--单笔退款--人工ajax")
	@RequestMapping(value = "/abcpay/single/refund", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<PayRefundRec> refundDoForAbcpayAsSingle(HttpServletRequest request, HttpServletResponse response, @RequestBody MapContext requestData) throws Exception {
		//
		Result<PayRefundRec> result = Result.newOne();
		//
		UserContext userContext = getUserContext(request);
		try {
			//
			JSON json = settleService.createReqDataForAbcAsSingleAndManual(requestData, userContext);
			// 3、判断退款结果状态，进行后续操作
			String returnCode = json.GetKeyValue("ReturnCode");
			String errorMessage = json.GetKeyValue("ErrorMessage");
			if (returnCode.equals("0000")) {
				// 4、退款成功
				Boolean ok = settleService.refundFinishedAsAbcpayAndSingle(json, requestData, userContext);
				result.message = "退款成功!";
				if (!ok) {
					result.type = Type.warn;
					result.message = "退款成功，但数据更新失败，请联系技术人员!";
				}

			} else {
				result.type = Type.warn;
				result.message = "退款失败:" + errorMessage + "请把错误码(" + returnCode + ")转告给技术人员";
			}
		} catch (Exception e) {
			result.type = Type.warn;
			result.message = "退款成功，程序出错导致数据更新失败，请联系技术人员!";
			e.printStackTrace();
		}
		return result;
	}

	@Remark("农行--批量退款--未做完")
	@RequestMapping(value = "/abcpay/batch/refund", method = RequestMethod.POST)
	public String goPageJspRefundForAbcpay(HttpServletRequest request, HttpServletResponse response, String ids) throws Exception {

		// 验证输入信息并取得退款所需要的信息

		String[] orderno_arr = null;
		String[] neworderno_arr = null;
		String[] currencycode_arr = null;
		String[] orderamount_arr = null;
		String[] remark_arr = null;

		Integer batchSize = 1;

		if (batchSize == 1) {
			String orderno = request.getParameter("txtOrderNo");// 原订单编号
			String neworderno = request.getParameter("txtNewOrderNo");// 退款订单编号
			String currencycode = request.getParameter("txtCurrencyCode");// 币种
			String orderamount = request.getParameter("txtRefundAmount");// 退款金额
			String remark = request.getParameter("txtRemark");// 附言 非必
			orderno_arr = new String[] { orderno };
			neworderno_arr = new String[] { neworderno };
			currencycode_arr = new String[] { currencycode };
			orderamount_arr = new String[] { orderamount };
			remark_arr = new String[] { remark };
		} else {
			orderno_arr = request.getParameterValues("txtOrderNo");
			neworderno_arr = request.getParameterValues("txtNewOrderNo");
			currencycode_arr = request.getParameterValues("txtCurrencyCode");
			orderamount_arr = request.getParameterValues("txtRefundAmount");
			remark_arr = request.getParameterValues("txtRemark");
		}

		// 1、生成批量退款请求对象
		BatchRefundRequest tBatchRefundRequest = new BatchRefundRequest();
		// 取得明细项
		LinkedHashMap map = null;
		BigDecimal sum = BigDecimal.ZERO;
		for (int i = 0; i < orderno_arr.length; i++) {
			map = new LinkedHashMap();
			map.put("SeqNo", String.valueOf(i + 1));
			map.put("OrderNo", orderno_arr[i]);
			map.put("NewOrderNo", neworderno_arr[i]);
			map.put("CurrencyCode", currencycode_arr[i]);
			map.put("RefundAmount", orderamount_arr[i]);
			map.put("Remark", remark_arr[i]);
			tBatchRefundRequest.dic.put(i + 1, map);
			// 此处必须使用BigDecimal，否则会丢精度
			BigDecimal bd = new BigDecimal(orderamount_arr[i].toString());
			sum = sum.add(bd);
		}
		// 此处必须设定iSumAmount属性
		tBatchRefundRequest.iSumAmount = sum.doubleValue();

		tBatchRefundRequest.batchRefundRequest.put("BatchNo", request.getParameter("txtBatchNo")); // 批量编号 （必要信息）
		tBatchRefundRequest.batchRefundRequest.put("BatchDate", request.getParameter("txtBatchDate")); // 订单日期 （必要信息）
		tBatchRefundRequest.batchRefundRequest.put("BatchTime", request.getParameter("txtBatchTime")); // 订单时间 （必要信息）
		tBatchRefundRequest.batchRefundRequest.put("MerRefundAccountNo", request.getParameter("txtMerRefundAccountNo")); // 商户退款账号
		tBatchRefundRequest.batchRefundRequest.put("MerRefundAccountName", request.getParameter("txtMerRefundAccountName")); // 商户退款名
		tBatchRefundRequest.batchRefundRequest.put("TotalCount", request.getParameter("TotalCount")); // 总笔数 （必要信息）
		tBatchRefundRequest.batchRefundRequest.put("TotalAmount", request.getParameter("TotalAmount")); // 总金额 （必要信息）

		// 2、传送批量退款请求并取得结果
		JSON json = tBatchRefundRequest.postRequest();

		// 3、判断批量退款结果状态，进行后续操作
		String ReturnCode = json.GetKeyValue("ReturnCode");
		String ErrorMessage = json.GetKeyValue("ErrorMessage");
		if (ReturnCode.equals("0000")) {
			// 4、批量退款成功
			// out.println("ReturnCode = [" + ReturnCode + "]<br/>");
			// out.println("ResultMessage = [" + ErrorMessage + "]<br/>");
			// out.println("TrxType = [" + json.GetKeyValue("TrxType") + "]<br/>");
			// out.println("TotalCount = [" + json.GetKeyValue("TotalCount") + "]<br/>");
			// out.println("TotalAmount = [" + json.GetKeyValue("TotalAmount") + "]<br/>");
			// out.println("SerialNumber = [" + json.GetKeyValue("SerialNumber") + "]<br/>");
			// out.println("HostDate = [" + json.GetKeyValue("HostDate") + "]<br/>");
			// out.println("HostTime = [" + json.GetKeyValue("HostTime") + "]<br/>");
		} else {
			// 5、批量退款失败
			// out.println("ReturnCode = [" + ReturnCode + "]<br/>");
			// out.println("ResultMessage = [" + ErrorMessage + "]<br/>");
		}
		return null;
	}

	// ---------------end-----------农行跳银联----------------------------------------------------

	// TODO-------------begin----结算---------批量转账到支付宝账户-----------支付宝alipay------------------------------------------------

	/**
	 * 结算单生成后，商户后台人员确认结算操作———————————没用———————————————
	 * 
	 * @author "WJJ"
	 * @date 2015年11月17日 下午4:43:43
	 * 
	 * @param request
	 * @return
	 */
	@Remark("商户后台人员确认结算操作")
	@RequestMapping(value = "/do/operation/by/merch", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<SettleProcess> settleDoing(HttpServletRequest request, @RequestBody SettleProcess settleProcess) {
		//
		Result<SettleProcess> result = Result.newOne();
		boolean ok = settleService.updateSettleProcessSettleFlag(settleProcess);
		result.message = "操作成功!";

		if (!ok) {
			result.type = Type.warn;
			result.message = "操作失败!";
		}
		//
		return result;
	}

	/**
	 * 结算单生成后，后台人员提交结算操作
	 * 
	 * @author "WJJ"
	 * @date 2015年11月17日 下午4:43:43
	 * 
	 * @param request
	 * @param requestData
	 * @return
	 */
	@Remark("后台人员提交结算操作")
	@RequestMapping(value = "/settling/do", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<String> operationDoByMerch(HttpServletRequest request, @RequestBody MapContext requestData) {
		//
		Result<String> result = Result.newOne();
		//
		UserContext userContext = getUserContext(request);
		//
		try {
			boolean ok = settleService.updateSettleProcessDoing(userContext, requestData);
			result.message = "申请成功!";

			if (!ok) {
				result.type = Type.warn;
				result.message = "申请失败!";
			}
		} catch (Exception e) {
			result.type = Type.warn;
			result.message = "申请异常!";
			e.printStackTrace();
		}
		//
		return result;
	}

	/**
	 * 调用支付宝批量转账接口，对结算单里，选中的数据进行转账
	 * 
	 * @author "WJJ"
	 * @date 2015年11月17日 下午4:44:04
	 * 
	 * @param request
	 * @param response
	 * @param ids
	 * @return
	 * @throws Exception
	 */
	@Remark("支付宝批量转账到支付宝账户")
	@RequestMapping(value = "/alipay/batch/settle", method = RequestMethod.POST)
	public String goPageJspBatchPay(HttpServletRequest request, HttpServletResponse response, String ids) throws Exception {

		UserContext userContext = getUserContext(request);
		// 建立请求
		try {
			// 组装数据
			Map<String, String> sParaTemp = settleService.createReqTransferData(userContext, ids);

			String sHtmlText = AlipaySubmit.buildRequest(sParaTemp, "post", "确认");
			request.setAttribute("sHtmlText", sHtmlText);
			return "settle/alipay/ali_html";
		} catch (Exception e) {
			return "settle/alipay/ali_reqfailTransfer";
		}
	}

	/**
	 * 支付宝--批量转账到支付宝账户服务器异步通知页面 不能执行任何页面跳转
	 * 
	 * @author "WJJ"
	 * @date 2015年10月22日 下午1:55:01
	 * 
	 * @param request
	 * @return
	 * @throws IOException
	 */
	@Remark("支付宝--批量转账到支付宝账户服务器异步通知页面路径")
	@RequestMapping(value = "/alipay/batch/transfer/notify_url", method = RequestMethod.POST)
	public void returnTransferNotify(HttpServletRequest request, HttpServletResponse response) throws IOException {

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

		// 批次号：batch_no、付款账号ID：pay_user_id、付款账号姓名：pay_user_name、付款账号：pay_account_no

		// 批量付款数据中转账成功的详细信息
		// 格式： 流水号^收款方账号^收款账号姓名^付款金额^成功标识(S)^成功原因(null)^支付宝内部流水号^完成时间。 每条记录以“|”间隔
		// 例：0315001^gonglei1@handsome.com.cn^龚本林^20.00^S^null^200810248427067^20081024143652|
		String success_details = request.getParameter("success_details");

		// 批量付款数据中转账失败的详细信息
		// 格式：流水号^收款方账号^收款账号姓名^付款金额^失败标识(F)^失败原因^支付宝内部流水号^完成时间。 每条记录以“|”间隔。
		// 例：0315006^xinjie_xj@163.com^星辰公司1^20.00^F^TXN_RESULT_TRANSFER_OUT_CAN_NOT_EQUAL_IN^200810248427065^20081024143651
		String fail_details = request.getParameter("fail_details");

		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		if (AlipayNotify.verify(params)) {// 验证成功

			try {
				settleService.transferFinishedOperation(success_details, fail_details);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				out.print("success");// 回调成功后，给支付宝返回success 不能修改
			}

		} else {// 验证失败
			out.print("fail");
		}

	}

	// ---------------end----结算-------批量转账到支付宝账户-----------支付宝alipay-------313809------------------------------------------

	// TODO---------------begin----结算-------农行银企直联-----------------------------------------------------

	/**
	 * 后台，点击银企直联结算按钮。
	 * 
	 * @author "WJJ"
	 * @date 2016年1月26日 下午5:01:04
	 * 
	 * @param request
	 * @param requestData
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Remark("后台——点击银企直联结算")
	@RequestMapping(value = "/settle/as/abc/do", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<String> settleDoByMerchAsAbc(HttpServletRequest request, @RequestBody MapContext requestData) {
		//
		Result<String> result = Result.newOne();
		Result<String> resultNew = null;
		//
		UserContext userContext = getUserContext(request);
		//
		try {
			List<String> ids = requestData.getTypedValue("ids", TypeUtil.Types.StringList.getClass());
			Long longId = requestData.getTypedValue("id", Long.class);
			if (null != ids && ids.size() != 0) {
				for (String id : ids) {
					try {
						// 组装请求数据
						DpRequest dpRequest = settleService.createReqTransferDataAsAbc(Long.valueOf(id), userContext);
						// 请求代理、代理发送socket、接受代理返回来的加密数据
						Result<RspData> resultInfo = settleService.sendDataToProxy(dpRequest);
						// 处理返回数据
						// Boolean ok = settleService.sendSocket(reqData, longId);
						resultNew = settleService.updateRspDataAsAbcDirect(resultInfo);
					} catch (Exception e) {
						e.printStackTrace();
						continue;
					}
				}
			} else {
				// 组装请求数据
				DpRequest dpRequest = settleService.createReqTransferDataAsAbc(longId, userContext);
				// 请求代理、代理发送socket、接受代理返回来的加密数据
				Result<RspData> resultInfo = settleService.sendDataToProxy(dpRequest);
				// 处理返回数据
				// Boolean ok = settleService.sendSocket(reqData, longId);
				resultNew  = settleService.updateRspDataAsAbcDirect(resultInfo);
			}
			
			if(resultNew.type == Type.info){
				result.message = "申请成功!";
			}else{
				result.message = resultNew.message;
			}

		} catch (Exception e) {
			result.type = Type.warn;
			result.message = "申请异常!";
			e.printStackTrace();
		}
		//
		return result;
	}

	// ---------------end----结算-------农行银企直联------------------------------------------

	// TODO---------------------------------线下打款，人工点手动结算----------------------------------------------
	@Remark("线下打款，人工点手动结算")
	@RequestMapping(value = "/manual/do", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<SettleProcess> settleDoAsManual(HttpServletRequest request, HttpServletResponse response, @RequestBody SettleProcess settleProcess) throws Exception {
		//
		Result<SettleProcess> result = Result.newOne();
		//
		UserContext userContext = getUserContext(request);
		try {

			Boolean ok = settleService.settleDoAsManual(settleProcess, userContext);
			result.message = "操作成功!";
			if (!ok) {
				result.type = Type.warn;
				result.message = "操作失败";
			}

		} catch (Exception e) {
			result.type = Type.warn;
			result.message = "操作异常";
			e.printStackTrace();
		}
		return result;
	}
	// ---------------end----线下打款，人工点手动结算------------------------------------------

	// TODO--------------------------------------- 支付（退款）记录页面----------------------------------------------

	/**
	 * 购买E卡支付记录页面
	 * 
	 * @author "WJJ"
	 * @date 2015年11月21日 下午5:00:21
	 * 
	 * @return
	 */
	@Remark("E卡支付记录页面")
	@RequestMapping(value = "/pay/rec/eCard/jsp", method = RequestMethod.GET)
	public String toECardPayRec() {
		return "settle/eCardPayRec";
	}

	/**
	 * 分页查询购买E卡的支付记录
	 * 
	 * @author "WJJ"
	 * @date 2015年11月21日 下午5:01:16
	 * 
	 * @param request
	 * @return
	 */
	@Remark("分页查询E卡支付记录")
	@RequestMapping(value = "/eCard/payRec/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<ECardPayRec> getECardPayRecs(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<ECardPayRec> paginatedList = settleService.getECardPayRecsByFilter(paginatedFilter);
		//
		JqGridPage<ECardPayRec> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 商品销售支付记录页面
	 * 
	 * @author "WJJ"
	 * @date 2015年11月17日 下午4:44:57
	 * 
	 * @return
	 */
	@Remark("支付记录页面（对最终客户）")
	@RequestMapping(value = "/pay/rec/sale/jsp", method = RequestMethod.GET)
	public String toSalePayRec() {
		return "settle/salePayRec";
	}

	/**
	 * 分页查询商品销售支付记录
	 * 
	 * @author "WJJ"
	 * @date 2015年11月17日 下午4:45:10
	 * 
	 * @param request
	 * @return
	 */
	@Remark("分页查询支付记录（对最终客户）")
	@RequestMapping(value = "/sale/payRec/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<PayRefundRec> getSalePayRecs(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<PayRefundRec> paginatedList = settleService.getSalePayRecsByFilter(paginatedFilter);
		//
		JqGridPage<PayRefundRec> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 退款记录页面
	 * 
	 * @author "WJJ"
	 * @date 2015年11月17日 下午4:45:21
	 * 
	 * @return
	 */
	@Remark("退款记录页面（对最终客户）")
	@RequestMapping(value = "/refund/rec/sale/jsp", method = RequestMethod.GET)
	public String toSaleRefundRec() {
		return "settle/refundRec";
	}

	/**
	 * 分页查询退款记录
	 * 
	 * @author "WJJ"
	 * @date 2015年11月17日 下午4:45:29
	 * 
	 * @param request
	 * @return
	 */
	@Remark("分页查询退款记录（对最终客户）")
	@RequestMapping(value = "/sale/refundRec/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<PayRefundRec> getSaleRefundRecs(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<PayRefundRec> paginatedList = settleService.getRefundAuditRecs(paginatedFilter);
		//
		JqGridPage<PayRefundRec> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	// TODO-------------暂时未用到--------------------商城后台------ 结算记录 结算对象：比如供应商----------------------------------------------

	@Remark("结算操作页面（对供应商等）")
	@RequestMapping(value = "/for/vendor/jsp/-mall", method = RequestMethod.GET)
	public String toMerchSettleCapital() {
		return "settle/merchSettleCapital";
	}

	@Remark("结算记录页面（对供应商等）")
	@RequestMapping(value = "/rec/for/vendor/jsp/-mall", method = RequestMethod.GET)
	public String toMerchSettleCapitalRec() {
		return "settle/merchSettleCapitalRec";
	}

	@Remark("分页查询结算记录页面（对供应商等）")
	@RequestMapping(value = "/rec/for/vendor/get/-mall", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<SettleRec> getSettleRecs(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<SettleRec> paginatedList = settleService.getSettleRecsByFilter(paginatedFilter);
		//
		JqGridPage<SettleRec> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	// TODO------------------------------商城后台--------- 结算相关 结算对象 比如商户 ----------------------------------------------

	/**
	 * 商城后台——待结算列表页面，在此页面可以 点击：生成结算单
	 * 
	 * @author "WJJ"
	 * @date 2015年12月25日 下午3:49:18
	 * 
	 * @return
	 */
	@Remark("商城后台——待结算列表页面")
	@RequestMapping(value = "/waiting/list/jsp/-mall", method = RequestMethod.GET)
	public String goSettleWaitingJsp() {
		return "settle/waitSettle";
	}

	/**
	 * 商城后台——分页查询待结算信息,在此页面可以 点击：生成结算单
	 * 
	 * @author "WJJ"
	 * @date 2015年11月17日 下午4:46:12
	 * 
	 * @param request
	 * @return
	 */
	@Remark("商城后台——分页查询待结算信息（对所有商户店铺等）")
	@RequestMapping(value = "/sale/settle/waiting/list/get/-mall", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<SettleProcess> getWaitingSettleList(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		MapContext filterItems = paginatedFilter.getFilterItems();
		String settleFlag = (String) filterItems.get("settleFlag");
		PaginatedList<SettleProcess> paginatedList = null;
		if (null == settleFlag || (null != settleFlag && !settleFlag.equals("9"))) {
			paginatedList = settleService.getSettleProcessByWaitingFilter(paginatedFilter);
		} else {
			filterItems.put("settleDay", new Date());
			paginatedList = settleService.getSettleProcessByWaitingLiquidation(paginatedFilter);
		}
		//
		JqGridPage<SettleProcess> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 商城后台——待结算列表，点击按钮：生成清算单
	 * 
	 * @author "WJJ"
	 * @date 2015年12月26日 下午3:52:21
	 * 
	 * @param request
	 * @return
	 */
	@Remark("商城后台——后台人员:生成清算单")
	@RequestMapping(value = "/createSettleInfo/do", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<String> createSettleInfo(HttpServletRequest request) {
		//
		Result<String> result = Result.newOne();

		try {
			settleService.createSettleInfo();
			result.message = "生成成功!";
		} catch (Exception e) {
			result.type = Type.warn;
			result.message = "生成清算单异常";
			e.printStackTrace();
		}
		//
		return result;
	}

	/**
	 * 商城后台——支付宝——待结算列表，点击按钮：更新清算单数据 （比较settleDay与today，更改7为6）
	 * 
	 * @author "WJJ"
	 * @date 2015年12月28日 上午1:49:21
	 * 
	 * @param request
	 * @return
	 */
	@Remark("商城后台——后台人员:更新结算单数据")
	@RequestMapping(value = "/updateSettleInfo/do", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<SettleProcess> updateSettleInfo(HttpServletRequest request, @RequestBody MapContext requestData) {
		//
		Result<SettleProcess> result = Result.newOne();

		try {
			boolean ok = settleService.updateSettleInfo();
			result.message = "更新成功!";

			if (!ok) {
				result.type = Type.warn;
				result.message = "更新失败!";
			}
		} catch (Exception e) {
			result.type = Type.warn;
			result.message = "更新清算单数据异常";
			e.printStackTrace();
		}
		//
		return result;
	}

	/**
	 * 商城后台——支付宝——待结算列表，点击按钮：生成结算单数据 （更改状态由6到3）
	 * 
	 * @author "WJJ"
	 * @date 2015年12月28日 上午1:49:21
	 * 
	 * @param request
	 * @return
	 */
	@Remark("商城后台——后台人员:生成结算单")
	@RequestMapping(value = "/submitSettleInfo/do", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<SettleProcess> submitSettleInfo(HttpServletRequest request) {
		//
		Result<SettleProcess> result = Result.newOne();

		try {
			boolean ok = settleService.submitSettleInfo();
			result.message = "生成成功!";
			if (!ok) {
				result.type = Type.warn;
				result.message = "已全部生成，暂时无新数据!";
			}

		} catch (Exception e) {
			result.type = Type.warn;
			result.message = "生成结算单数据异常";
			e.printStackTrace();
		}
		//
		return result;
	}

	/**
	 * 商城后台——结算操作页面
	 * 
	 * @author "WJJ"
	 * @date 2015年11月17日 下午4:45:57
	 * 
	 * @return
	 */
	@Remark("商城后台——结算操作页面（对商户店铺等）")
	@RequestMapping(value = "/for/merch/jsp/-mall", method = RequestMethod.GET)
	public String toSettleCapital() {
		return "settle/settleForMerch";
	}

	/**
	 * 商城后台——分页查询可结算信息：此页面可去执行结算
	 * 
	 * @author "WJJ"
	 * @date 2015年11月17日 下午4:46:12
	 * 
	 * @param request
	 * @return
	 */
	@Remark("商城后台——分页查询可结算信息：此页面可去点击执行结算按钮（对商户店铺等）")
	@RequestMapping(value = "/sale/settle/list/get/-mall", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<SettleProcess> getSettleList(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<SettleProcess> paginatedList = settleService.getSettleProcessByFilterAsMall(paginatedFilter);
		//
		JqGridPage<SettleProcess> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 分页查询当前账期，每笔结算信息的订单详情
	 * 
	 * @author "WJJ"
	 * @date 2015年11月17日 下午4:46:28
	 * 
	 * @param request
	 * @return
	 */
	@Remark("分页查询当前账期，每笔结算信息的订单详情")
	@RequestMapping(value = "/process/order/deatil/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<SaleOrder> getOrderDeatilListBySettleDay(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<SaleOrder> paginatedList = settleService.getOrderDeatilListBySettleDay(paginatedFilter);
		//
		JqGridPage<SaleOrder> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 商城后台——结算记录列表页面(对商户店铺)
	 * 
	 * @author "WJJ"
	 * @date 2015年11月17日 下午4:46:48
	 * 
	 * @return
	 */
	@Remark("商城后台——结算记录列表页面(对商户店铺)")
	@RequestMapping(value = "/rec/for/merch/jsp/-mall", method = RequestMethod.GET)
	public String toSaleSettleCapitalRec() {
		return "settle/settleRecForMerch";
	}

	/**
	 * 商城后台——分页查询结算记录
	 * 
	 * @author "WJJ"
	 * @date 2015年11月17日 下午4:47:04
	 * 
	 * @param request
	 * @return
	 */
	@Remark("商城后台——分页查询结算记录(对商户店铺)")
	@RequestMapping(value = "/rec/for/merch/get/-mall", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<SaleSettleRec> getSettleListAsSuccess(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<SaleSettleRec> paginatedList = settleService.getSaleSettleRecsByFilter(paginatedFilter);
		//
		JqGridPage<SaleSettleRec> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	// TODO-------------------------商户后台-------商城给商户的结算相关--------------------------

	/**
	 * 商户后台——待结算列表(商户店铺后台)
	 * 
	 * @author "WJJ"
	 * @date 2015年11月17日 下午4:46:48
	 * 
	 * @return
	 */
	@Remark("商户后台——待结算列表(商户店铺后台)")
	@RequestMapping(value = "/by/mall/jsp/-shop", method = RequestMethod.GET)
	public String toSaleSettleByMall() {
		return "merchant/settleByMall";
	}

	/**
	 * 商户后台，分页查询待结算信息
	 * 
	 * @author "WJJ"
	 * @date 2015年11月17日 下午4:46:12
	 * 
	 * @param request
	 * @return
	 */
	@Remark("商户后台，分页查询结算信息（对商户店铺等）")
	@RequestMapping(value = "/sale/settle/info/get/-shop", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<SettleProcess> getSettleInfoAsMerch(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		UserContext userContext = getUserContext(request);
		Integer operatorId = userContext.getUserId();

		MapContext filterItems = paginatedFilter.getFilterItems();
		filterItems.put("merchantId", operatorId);
		//
		String settleFlag = (String) filterItems.get("settleFlag");
		PaginatedList<SettleProcess> paginatedList = null;
		if (null == settleFlag || (null != settleFlag && !settleFlag.equals("9"))) {
			paginatedList = settleService.getSettleProcessByFilterAsMerch(paginatedFilter);
		} else {
			filterItems.put("settleDay", new Date());
			paginatedList = settleService.getSettleProcessByWaitingLiquidation(paginatedFilter);
		}
		//
		JqGridPage<SettleProcess> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 商户后台——结算记录列表页面(商户店铺后台已完成)
	 * 
	 * @author "WJJ"
	 * @date 2015年11月17日 下午4:46:48
	 * 
	 * @return
	 */
	@Remark("商户后台——结算记录列表页面(商户店铺后台已完成)")
	@RequestMapping(value = "/rec/by/mall/jsp/-shop", method = RequestMethod.GET)
	public String toSaleSettleRecMerchByMall() {
		return "merchant/settleRecByMall";
	}

	/**
	 * 商户后台——分页查询结算记录
	 * 
	 * @author "WJJ"
	 * @date 2015年11月17日 下午4:47:04
	 * 
	 * @param request
	 * @return
	 */
	@Remark("商户后台——分页查询商户自己结算记录")
	@RequestMapping(value = "/rec/by/mall/get/-shop", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<SaleSettleRec> getSettleListAsMerch(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		UserContext userContext = getUserContext(request);
		Integer operatorId = userContext.getUserId();

		MapContext filterItems = paginatedFilter.getFilterItems();
		filterItems.put("peerId", operatorId);
		filterItems.put("state", 3);// 3为成功
		//
		PaginatedList<SaleSettleRec> paginatedList = settleService.getSettleListAsMerch(paginatedFilter);
		//
		JqGridPage<SaleSettleRec> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	// TODO -------------------begin---------------------资金统计--------------------------------------------------.

	/**
	 * 商户后台，人员查看资金统计
	 * 
	 * @author "WJJ"
	 * @date 2015年11月26日 下午5:53:26
	 * 
	 * @param request
	 * @return
	 */
	@Remark("商户后台，资金统计")
	@RequestMapping(value = "/count/captil/for/merch/-shop", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<String> countCapitalByMerch(HttpServletRequest request, @RequestBody MapContext requestData) {
		//
		Result<String> result = Result.newOne();

		Date fromDate = requestData.getTypedValue("fromDate", Date.class);
		Date toDate = requestData.getTypedValue("toDate", Date.class);

		UserContext userContext = getUserContext(request);
		Integer operatorId = userContext.getUserId();
		//
		try {
			BigDecimal amount = new BigDecimal("0.00");

			Map<String, Object> params = new HashMap<String, Object>();
			params.put("peerId", operatorId);
			params.put("state", 3);
			params.put("fromDate", fromDate);
			params.put("toDate", toDate);

			List<SaleSettleRec> list = settleService.countCapitalByMerch(params);
			if (null != list && list.size() != 0) {
				for (SaleSettleRec saleSettleRec : list) {
					amount = amount.add(saleSettleRec.getAmount());
				}
			}
			String count = NumUtil.getNumFormat("#0.00").format(amount);
			result.message = "统计成功!";
			result.data = count;
		} catch (Exception e) {
			result.data = "统计异常";
			e.printStackTrace();
		}
		//
		return result;
	}

	// TODO -------------------begin----------商户后台-----------合作店结算--------------------------------------------------.

	/**
	 * 商户后台——结算记录列表页面(商户对合作店的结算记录)
	 * 
	 * @author "WJJ"
	 * @date 2016年2月24日 上午10:46:04
	 * 
	 * @return
	 */
	@Remark("商户后台——结算记录列表页面(商户对合作店的结算记录)")
	@RequestMapping(value = "/rec/by/shop/jsp/-shop", method = RequestMethod.GET)
	public String toSaleSettleRecMerchByShop() {
		return "merchant/settleRecByShop";
	}

	/**
	 * 商户后台——分页查询对合作店的结算记录
	 * 
	 * @author "WJJ"
	 * @date 2016年2月24日 上午10:48:31
	 * 
	 * @param request
	 * @return
	 */
	@Remark("商户后台——分页查询对合作店的结算记录")
	@RequestMapping(value = "/rec/by/shop/get/-shop", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<DistSettleRec> getSettleListForDist(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		UserContext userContext = getUserContext(request);
		Integer shopId = userContext.getScopeEntity().getId();

		MapContext filterItems = paginatedFilter.getFilterItems();
		filterItems.put("shopId", shopId);
		//
		PaginatedList<DistSettleRec> paginatedList = settleService.getDistSettleRecListAsShop(paginatedFilter);
		//
		JqGridPage<DistSettleRec> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 分页查询给合作店结算记录，关联的订单
	 * 
	 * @author "WJJ"
	 * @date 2016年2月26日 下午4:57:00
	 * 
	 * @param request
	 * @return
	 */
	@Remark("分页查询给合作店结算记录，关联的订单")
	@RequestMapping(value = "/dist/rec/orders/deatil/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<SaleOrder> getOrderDeatilListByShop(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<SaleOrder> paginatedList = settleService.getOrderDeatilListByShop(paginatedFilter);
		//
		JqGridPage<SaleOrder> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}
}
