package priv.starfish.mall.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.abc.trustpay.client.JSON;
import com.abc.trustpay.client.ebus.PaymentRequest;
import priv.starfish.common.cms.FreeMarkerService;
import priv.starfish.common.model.PayStateType;
import priv.starfish.common.model.Result;
import priv.starfish.common.pay.PayNotifyConfig;
import priv.starfish.common.pay.dict.PayWayType;
import priv.starfish.common.pay.wechatpay.util.WechatpayCore;
import priv.starfish.common.sms.SmsMessage;
import priv.starfish.common.sms.SmsResult;
import priv.starfish.common.sms.SmsService;
import priv.starfish.common.util.DateUtil;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.dao.ecard.UserECardDao;
import priv.starfish.mall.ecard.entity.ECard;
import priv.starfish.mall.ecard.entity.UserECard;
import priv.starfish.mall.dao.member.MemberDao;
import priv.starfish.mall.member.entity.Member;
import priv.starfish.mall.dao.order.ECardOrderDao;
import priv.starfish.mall.dao.order.ECardOrderItemDao;
import priv.starfish.mall.dao.order.ECardOrderRecordDao;
import priv.starfish.mall.dao.order.SaleOrderDao;
import priv.starfish.mall.dao.order.SaleOrderRecordDao;
import priv.starfish.mall.order.dict.OrderAction;
import priv.starfish.mall.order.dict.OrderType;
import priv.starfish.mall.order.entity.ECardOrder;
import priv.starfish.mall.order.entity.ECardOrderItem;
import priv.starfish.mall.order.entity.ECardOrderRecord;
import priv.starfish.mall.order.entity.SaleOrder;
import priv.starfish.mall.order.entity.SaleOrderRecord;
import priv.starfish.mall.order.entity.SaleOrderSvc;
import priv.starfish.mall.service.BaseConst;
import priv.starfish.mall.service.ECardOrderService;
import priv.starfish.mall.service.ECardService;
import priv.starfish.mall.service.GoodsService;
import priv.starfish.mall.service.PayService;
import priv.starfish.mall.service.SaleOrderService;
import priv.starfish.mall.service.SalePrmtService;
import priv.starfish.mall.dao.settle.ECardPayRecDao;
import priv.starfish.mall.dao.settle.PayRefundRecDao;
import priv.starfish.mall.settle.dto.WxPayResultDto;
import priv.starfish.mall.settle.entity.ECardPayRec;
import priv.starfish.mall.settle.entity.PayRefundRec;

import freemarker.template.Template;

@Service("payService")
public class PayServiceImpl extends BaseServiceImpl implements PayService {

	@Resource
	SaleOrderDao saleOrderDao;

	@Resource
	PayRefundRecDao payRefundRecDao;

	@Resource
	SaleOrderRecordDao saleOrderRecordDao;

	@Resource
	ECardPayRecDao eCardPayRecDao;

	@Resource
	ECardOrderDao eCardOrderDao;

	@Resource
	UserECardDao userECardDao;

	@Resource
	ECardOrderItemDao eCardOrderItemDao;

	@Resource
	ECardOrderRecordDao eCardOrderRecordDao;

	@Resource
	MemberDao memberDao;

	@Resource
	FreeMarkerService freeMarkerService;

	@Resource
	SmsService smsService;

	@Resource
	ECardService eCardService;

	@Resource
	ECardOrderService eCardOrderService;

	@Resource
	SalePrmtService salePrmtService;

	@Resource
	GoodsService goodsService;

	@Resource
	SaleOrderService saleOrderService;

	@Override
	public void payFinishedOperation(String no, String subject, String body, String amount, String tradeNo, String tradeStatus, String payTime, String extraParam) {

		// 判断是销售订单还是e卡订单
		if (no.startsWith("S")) {// 销售订单
			// 判断是否已经处理过订单
			PayRefundRec prr = payRefundRecDao.selectByOrderNo(no);
			// 没处理
			if (null == prr) {
				SaleOrder saleOrder = saleOrderDao.selectByNo(no);

				// 1、根据订单号，更改订单状态为已支付
				saleOrder.setPayState(PayStateType.paid.name());
//				saleOrder.setPayWay(PayWayType.alipay.toString());
				if (saleOrder.getSvcPackId() != null) {
					saleOrder.setFinished(true);
					saleOrder.setFinishTime(new Date());
				}
				saleOrderDao.updateForPayFinished(saleOrder);

				if (null != saleOrder) {
					// 2、在总支付记录表中插入 一条记录 （根据订单号，查找出userid）。
					PayRefundRec payRefundRec = new PayRefundRec();
					payRefundRec.setOrderId(saleOrder.getId());
					payRefundRec.setNo(no);
					payRefundRec.setUserId(saleOrder.getUserId());
					payRefundRec.setSubject(subject);
					payRefundRec.setOrderDesc(body);
					payRefundRec.setTotalFee(amount);
					payRefundRec.setPayWayName(PayWayType.alipay.name());
					payRefundRec.setTradeNo(tradeNo);
					payRefundRec.setTradeStatus(tradeStatus);
					payRefundRec.setPayTime(payTime);
					Date canRefundDay = DateUtil.addDays(15); // TODO 这个15，之后配置
					payRefundRec.setCanRefundDay(canRefundDay);
					payRefundRecDao.insert(payRefundRec);

					// 3、在销售订单记录出，插入一条数据, 动作
					SaleOrderRecord saleOrderRecord = new SaleOrderRecord();
					saleOrderRecord.setOrderId(saleOrder.getId());
					saleOrderRecord.setAction(OrderAction.pay.name());
					saleOrderRecord.setActorId(saleOrder.getUserId());
					saleOrderRecord.setActRole("会员");
					saleOrderRecord.setActorName("客户");
					saleOrderRecord.setExtraInfo("买家已付款，请等待享受服务！");
					saleOrderRecord.setTs(new Date());
					saleOrderRecordDao.insert(saleOrderRecord);

					// 4、给用户，发送服务完成确认码。
					MapContext dataModel = MapContext.newOne();
					dataModel.put(BaseConst.TplModelVars.CODE, saleOrder.getDoneCode());
					dataModel.put(BaseConst.TplModelVars.SALE_ORDER_NO, no);
					this.sendSmsText(saleOrder.getPhoneNo(), BaseConst.SmsCodes.SALE_ORDER, dataModel);

					// 5、增加用户购买商品数量
					goodsService.updateGoodsBuySumByOrderNoAndUserId(no, saleOrder.getUserId());

					// 6、(可能)生成服务套餐票
					saleOrderService.saveUserSvcPackTicket(saleOrder);

					// 7、
				}

			}
		} else {// E卡订单
				// 判断是否已经处理过订单
			ECardPayRec eCardPayR = eCardPayRecDao.selectByNo(no);
			if (null == eCardPayR) {
				// 1、根据订单号，更改订单状态为已支付
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("no", no);
				map.put("paid", true);
				map.put("payTime", payTime);
//				map.put("payWay", PayWayType.alipay.toString());
				eCardOrderDao.updateByNo(map);
				//
				ECardOrder eCardOrder = eCardOrderDao.selectByNo(no);
				if (null != eCardOrder) {
					// 2、在购买e卡支付记录表中插入 一条记录 （根据订单号，查找出userid）。
					ECardPayRec eCardPayRec = new ECardPayRec();
					eCardPayRec.setNo(no);
					eCardPayRec.setUserId(eCardOrder.getUserId());
					eCardPayRec.setSubject(subject);
					eCardPayRec.setOrderDesc(body);
					eCardPayRec.setTotalFee(amount);
					eCardPayRec.setPayWayName(PayWayType.alipay.name());
					eCardPayRec.setTradeNo(tradeNo);
					eCardPayRec.setTradeStatus(tradeStatus);
					eCardPayRec.setPayTime(payTime);
					eCardPayRecDao.insert(eCardPayRec);

					// 3、新增用户e卡(手机端的话，截取body)
					String[] split = null;
					if (null != extraParam && !extraParam.equals("")) {
						split = extraParam.split("\\|");
					} else {
						split = body.split("\\|");
					}
					String cardCode = null;
					String shopId = null;
					String shopName = null;
					if (split.length > 1) {
						cardCode = split[0];
						shopId = split[1];
						shopName = split[2];
					} else {
						cardCode = split[0];
					}

					ECard eCard = eCardService.getECardById(cardCode);
					Integer eCardQuantity = eCardOrder.getQuantity();
					for (int i = 0; i < eCardQuantity; i++) {
						UserECard userECard = new UserECard();

						if (shopId != null && !shopId.equals("null")) {
							userECard.setShopId(Integer.valueOf(shopId));
							userECard.setShopName(shopName);
						}
						userECard.setUserId(eCardOrder.getUserId());
						userECard.setUserName(eCardOrder.getUserName());
						userECard.setCardCode(cardCode);
						if (null != eCard) {
							userECard.setFaceValue(eCard.getFaceVal());
							userECard.setRemainVal(eCard.getFaceVal());
						} else {
							userECard.setFaceValue(new BigDecimal("0.00"));
							userECard.setRemainVal(new BigDecimal("0.00"));
						}

						// String newCardNo = eCardService.newCardNo(ECardType.valueOf(cardCode));
						String newCardNo = eCardService.newCardNo();
						userECard.setCardNo(newCardNo);
						userECardDao.insert(userECard);

						// 4、e卡订单项
						ECardOrderItem eCardOrderItem = new ECardOrderItem();
						eCardOrderItem.setOrderId(eCardOrder.getId());
						eCardOrderItem.setCardNo(newCardNo);
						if (null != eCard) {
							eCardOrderItem.setPrice(eCard.getPrice());
							eCardOrderItem.setFaceValue(eCard.getFaceVal());
						} else {
							eCardOrderItem.setPrice(new BigDecimal("0.00"));
							eCardOrderItem.setFaceValue(new BigDecimal("0.00"));
						}

						eCardOrderItemDao.insert(eCardOrderItem);

					}

					// 5、在e卡订单记录出，插入一条数据, 动作
					ECardOrderRecord eCardOrderRecord = new ECardOrderRecord();
					eCardOrderRecord.setOrderId(eCardOrder.getId());
					eCardOrderRecord.setAction(OrderAction.finish.toString());
					eCardOrderRecord.setActorId(-1);
					eCardOrderRecord.setActorName("平台");
					eCardOrderRecordDao.insert(eCardOrderRecord);

					// 6、给用户发送短信
					MapContext dataModel = MapContext.newOne();
					dataModel.put(BaseConst.TplModelVars.ECARD_ORDER_NO, no);
					this.sendSmsText(eCardOrder.getPhoneNo(), BaseConst.SmsCodes.ECARD_ORDER, dataModel);

					// 7.首单赠送优惠券
					salePrmtService.saveEcardActGift(eCardOrder.getUserId(), eCard.getCode(), eCardQuantity, eCardOrder.getAmount());

					// 8、增加用户购买商品数量
					goodsService.updateGoodsBuySumByOrderNoAndUserId(no, eCardOrder.getUserId());

					// 9、更新用户rank
					Member member = memberDao.selectById(eCardOrder.getUserId());
					if (null == member.getRank() || eCard.getRank() > member.getRank()) {
						memberDao.updateRankById(eCard.getRank(), member.getId());
					}

				}
			}
		}

	}

	/**
	 * 发短信，copy胡哥baseController模板。
	 * 
	 * @author "WJJ"
	 * @date 2015年12月15日 下午2:35:28
	 * 
	 * @param phoneNo
	 * @param smsTplCode
	 * @param dataModel
	 * @return
	 */
	protected SmsResult sendSmsText(String phoneNo, String smsTplCode, MapContext dataModel) {
		Template template = freeMarkerService.getTemplate("sms", smsTplCode);
		//
		if (dataModel == null) {
			dataModel = MapContext.newOne();
		}
		//
		String smsText = freeMarkerService.renderContent(template, dataModel);
		//
		SmsMessage message = new SmsMessage();
		message.setReceiverNumber(phoneNo);
		message.setText(smsText);
		//
		SmsResult result = SmsResult.newOne();
		result.smsText = smsText;
		result.errCode = smsService.sendSms(message, dataModel);
		//
		return result;
	}

	@Override
	public void payFinishedOperationByAbc(String noForAbc, String amount, String tradeNo) {

		String subject = "";// 订单(商品)名称 必填
		String body = "";// 订单（商品）描述
		String payTime = DateUtil.toStdDateTimeStr(new Date());

		String no = noForAbc.substring(0, noForAbc.length() - 3);

		// 判断是销售订单还是e卡订单
		if (no.startsWith("S")) {// 销售订单
			// 判断是否已经处理过订单
			PayRefundRec prr = payRefundRecDao.selectByOrderNo(no);
			// 没处理
			if (null == prr) {
				SaleOrder saleOrder = saleOrderDao.selectByNo(no);

				// 1、根据订单号，更改订单状态为已支付
				saleOrder.setPayState(PayStateType.paid.name());
//				saleOrder.setPayWay(PayWayType.abcAsUnionpay.toString());
				if (saleOrder.getSvcPackId() != null) {
					saleOrder.setFinished(true);
					saleOrder.setFinishTime(new Date());
				}
				saleOrderDao.updateForPayFinished(saleOrder);

				if (null != saleOrder) {

					List<SaleOrderSvc> saleOrderSvcs = saleOrderService.getSaleOrderSvcsByOrderId(saleOrder.getId());

					if (saleOrderSvcs.size() != 0) {
						for (SaleOrderSvc saleOrderSvc : saleOrderSvcs) {
							body += saleOrderSvc.getSvcName() + ",";
						}
						subject = saleOrderSvcs.get(0).getSvcName();
						body = body.substring(0, body.length() - 1);
					} else {
						subject = no;
						body = no;
					}

					// 2、在支付记录表中插入 一条记录 （根据订单号，查找出userid）。
					PayRefundRec payRefundRec = new PayRefundRec();
					payRefundRec.setOrderId(saleOrder.getId());
					payRefundRec.setNo(no);
					payRefundRec.setNoForAbc(noForAbc);
					payRefundRec.setUserId(saleOrder.getUserId());
					payRefundRec.setSubject(subject);
					payRefundRec.setOrderDesc(body);
					payRefundRec.setTotalFee(amount);
					payRefundRec.setPayWayName(PayWayType.abcAsUnionpay.name());
					payRefundRec.setTradeNo(tradeNo);
					payRefundRec.setTradeStatus("TRADE_SUCCESS");
					payRefundRec.setPayTime(payTime);
					Date canRefundDay = DateUtil.addDays(15); // TODO 这个15，之后配置
					payRefundRec.setCanRefundDay(canRefundDay);
					payRefundRecDao.insert(payRefundRec);

					// 3、在销售订单记录出，插入一条数据, 动作
					SaleOrderRecord saleOrderRecord = new SaleOrderRecord();
					saleOrderRecord.setOrderId(saleOrder.getId());
					saleOrderRecord.setAction(OrderAction.pay.name());
					saleOrderRecord.setActorId(saleOrder.getUserId());
					saleOrderRecord.setActRole("会员");
					saleOrderRecord.setActorName("客户");
					saleOrderRecord.setExtraInfo("买家已付款，请等待享受服务！");
					saleOrderRecord.setTs(new Date());
					saleOrderRecordDao.insert(saleOrderRecord);

					// 4、给用户，发送服务完成确认码。
					MapContext dataModel = MapContext.newOne();
					dataModel.put(BaseConst.TplModelVars.CODE, saleOrder.getDoneCode());
					dataModel.put(BaseConst.TplModelVars.SALE_ORDER_NO, no);
					this.sendSmsText(saleOrder.getPhoneNo(), BaseConst.SmsCodes.SALE_ORDER, dataModel);

					// 5、增加用户购买商品数量
					goodsService.updateGoodsBuySumByOrderNoAndUserId(no, saleOrder.getUserId());

					// 6、(可能)生成服务套餐票
					saleOrderService.saveUserSvcPackTicket(saleOrder);
				}

			}
		} else {// E卡订单
				// 判断是否已经处理过订单
			ECardPayRec eCardPayR = eCardPayRecDao.selectByNo(no);
			if (null == eCardPayR) {
				// 1、根据订单号，更改订单状态为已支付
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("no", no);
				map.put("paid", true);
				map.put("payTime", payTime);
//				map.put("payWay", PayWayType.abcAsUnionpay.toString());
				eCardOrderDao.updateByNo(map);
				//
				ECardOrder eCardOrder = eCardOrderDao.selectByNo(no);
				if (null != eCardOrder) {

					subject = eCardOrder.getCardName();
					body = eCardOrder.getCardName() + "," + eCardOrder.getQuantity() + "张";

					// 2、在e卡支付记录表中插入 一条记录 （根据订单号，查找出userid）。
					ECardPayRec eCardPayRec = new ECardPayRec();
					eCardPayRec.setNo(no);
					eCardPayRec.setUserId(eCardOrder.getUserId());
					eCardPayRec.setSubject(subject);
					eCardPayRec.setOrderDesc(body);
					eCardPayRec.setTotalFee(amount);
					eCardPayRec.setPayWayName(PayWayType.abcAsUnionpay.name());
					eCardPayRec.setTradeNo(tradeNo);
					eCardPayRec.setTradeStatus("TRADE_SUCCESS");
					eCardPayRec.setPayTime(payTime);
					eCardPayRecDao.insert(eCardPayRec);

					// 3、新增用户e卡(手机端的话，截取body)
					String cardCode = eCardOrder.getCardCode();
					Integer shopId = eCardOrder.getShopId();
					String shopName = eCardOrder.getShopName();

					ECard eCard = eCardService.getECardById(cardCode);
					Integer eCardQuantity = eCardOrder.getQuantity();
					for (int i = 0; i < eCardQuantity; i++) {
						UserECard userECard = new UserECard();

						if (shopId != null && !shopId.equals("null")) {
							userECard.setShopId(shopId);
							userECard.setShopName(shopName);
						}
						userECard.setUserId(eCardOrder.getUserId());
						userECard.setUserName(eCardOrder.getUserName());
						userECard.setCardCode(cardCode);
						if (null != eCard) {
							userECard.setFaceValue(eCard.getFaceVal());
							userECard.setRemainVal(eCard.getFaceVal());
						} else {
							userECard.setFaceValue(new BigDecimal("0.00"));
							userECard.setRemainVal(new BigDecimal("0.00"));
						}

						// String newCardNo = eCardService.newCardNo(ECardType.valueOf(cardCode));
						String newCardNo = eCardService.newCardNo();
						userECard.setCardNo(newCardNo);
						userECardDao.insert(userECard);

						// 4、e卡订单项
						ECardOrderItem eCardOrderItem = new ECardOrderItem();
						eCardOrderItem.setOrderId(eCardOrder.getId());
						eCardOrderItem.setCardNo(newCardNo);
						if (null != eCard) {
							eCardOrderItem.setPrice(eCard.getPrice());
							eCardOrderItem.setFaceValue(eCard.getFaceVal());
						} else {
							eCardOrderItem.setPrice(new BigDecimal("0.00"));
							eCardOrderItem.setFaceValue(new BigDecimal("0.00"));
						}

						eCardOrderItemDao.insert(eCardOrderItem);

					}

					// 5、在e卡订单记录出，插入一条数据, 动作
					ECardOrderRecord eCardOrderRecord = new ECardOrderRecord();
					eCardOrderRecord.setOrderId(eCardOrder.getId());
					eCardOrderRecord.setAction(OrderAction.finish.toString());
					eCardOrderRecord.setActorId(-1);
					eCardOrderRecord.setActorName("平台");
					eCardOrderRecordDao.insert(eCardOrderRecord);

					// 6、给用户发送短信
					MapContext dataModel = MapContext.newOne();
					dataModel.put(BaseConst.TplModelVars.ECARD_ORDER_NO, no);
					this.sendSmsText(eCardOrder.getPhoneNo(), BaseConst.SmsCodes.ECARD_ORDER, dataModel);

					// 7.首单赠送优惠券
					salePrmtService.saveEcardActGift(eCardOrder.getUserId(), eCard.getCode(), eCardQuantity, eCardOrder.getAmount());

					// 8、增加用户购买商品数量
					goodsService.updateGoodsBuySumByOrderNoAndUserId(no, eCardOrder.getUserId());

					// 9、更新用户rank
					Member member = memberDao.selectById(eCardOrder.getUserId());
					if (null == member.getRank() || eCard.getRank() > member.getRank()) {
						memberDao.updateRankById(eCard.getRank(), member.getId());
					}
				}
			}
		}

	}

	@Override
	public JSON createReqDataForAbcpay(String orderNo, String linkType, String subject, String total_fee, String orderDate, String orderTime) {

		// 跳银联的时候，订单号最后加3个随机数
		orderNo = orderNo + String.valueOf(Math.random() * 1000).substring(0, 3);

		// 1.订单对象
		PaymentRequest tPaymentRequest = new PaymentRequest();
		tPaymentRequest.dicOrder.put("PayTypeID", "ImmediatePay"); // 设定交易类型 直接支付 必须
		tPaymentRequest.dicOrder.put("OrderDate", orderDate); // 设定订单日期 （必要信息 - YYYY/MM/DD）
		tPaymentRequest.dicOrder.put("OrderTime", orderTime); // 设定订单时间 （必要信息 - HH:MM:SS）
		tPaymentRequest.dicOrder.put("OrderNo", orderNo); // 设定订单编号 （必要信息）
		tPaymentRequest.dicOrder.put("CurrencyCode", "156"); // 设定交易币种 人民币 必须
		tPaymentRequest.dicOrder.put("OrderAmount", total_fee); // 设定交易金额 必须
		tPaymentRequest.dicOrder.put("InstallmentMark", "0"); // 分期标识 1：分期，0：不分期。 必须
		tPaymentRequest.dicOrder.put("CommodityType", "0101"); // 设置商品种类 0201:虚拟类,0202:传统类,0203:实名类 必须

		// tPaymentRequest.dicOrder.put("ExpiredDate", request.getParameter("ExpiredDate")); // 设定订单保存时间
		// tPaymentRequest.dicOrder.put("orderTimeoutDate", req.getParameter("orderTimeoutDate")); // 设定订单有效期

		// 2.订单明细
		LinkedHashMap orderitem = new LinkedHashMap();
		orderitem.put("ProductName", subject);// 商品名称 必须

		// orderitem.put("SubMerName", "测试二级商户1"); // 设定二级商户名称
		// orderitem.put("SubMerId", "12345"); // 设定二级商户代码
		// orderitem.put("SubMerMCC", "0000"); // 设定二级商户MCC码
		// orderitem.put("SubMerchantRemarks", "测试"); // 二级商户备注项
		// orderitem.put("ProductID", "IP000001");// 商品代码，预留字段
		// orderitem.put("UnitPrice", "1.00");// 商品总价
		// orderitem.put("Qty", "1");// 商品数量
		// orderitem.put("ProductRemarks", "测试商品"); // 商品备注项
		// orderitem.put("ProductType", "充值类");// 商品类型
		// orderitem.put("ProductDiscount", "0.9");// 商品折扣
		// orderitem.put("ProductExpiredDate", "10");// 商品有效期

		tPaymentRequest.orderitems.put(1, orderitem);

		// 3、生成支付请求对象
		String paymentType = "6";
		tPaymentRequest.dicRequest.put("PaymentType", paymentType); // 设定支付类型 6:银联跨行支付

		tPaymentRequest.dicRequest.put("PaymentLinkType", linkType);
		if (linkType.equals("2")) {
			tPaymentRequest.dicRequest.put("UnionPayLinkType", "0");
		}

		tPaymentRequest.dicRequest.put("NotifyType", "1"); // 设定通知方式 0:URL通知，1：服务器通知
		tPaymentRequest.dicRequest.put("ResultNotifyURL", PayNotifyConfig.abc_notify_url_pc); // 设定通知URL地址
		tPaymentRequest.dicRequest.put("IsBreakAccount", "0"); // 设定交易是否分账 必须设定，0:否；1:是
		// tPaymentRequest.dicRequest.put("MerchantRemarks", MerchantRemarks); // 附言，非必须，小于100字符串

		JSON json = tPaymentRequest.postRequest();

		return json;
	}

	@Override
	public Result<String> createWechatpayQrcode(String no, Result<String> result) {

		return null;
	}

	@Override
	public void payfinishedOperationByWechatpay(WxPayResultDto wpr) {
		String subject = "";// 订单(商品)名称 必填
		String body = "";// 订单（商品）描述
		//
		String no = wpr.getOutTradeNo();

		// 判断是销售订单还是e卡订单
		if (no.startsWith("S")) {// 销售订单
			// 判断是否已经处理过订单
			PayRefundRec prr = payRefundRecDao.selectByOrderNo(no);
			// 没处理
			if (null == prr) {
				SaleOrder saleOrder = saleOrderDao.selectByNo(no);

				// 1、根据订单号，更改订单状态为已支付
				saleOrder.setPayState(PayStateType.paid.toString());
//				saleOrder.setPayWay(PayWayType.wechatpay.toString());
				if (saleOrder.getSvcPackId() != null) {
					saleOrder.setFinished(true);
					saleOrder.setFinishTime(new Date());
				}
				saleOrderDao.updateForPayFinished(saleOrder);

				if (null != saleOrder) {
					// 通知已成功
					xOrderMessageProxy.sendActionMessage(OrderType.saleOrder, saleOrder.getId(), no, saleOrder.getUserId(), saleOrder.getShopId(), OrderAction.pay, true);
					//
					List<SaleOrderSvc> saleOrderSvcs = saleOrderService.getSaleOrderSvcsByOrderId(saleOrder.getId());
					if (saleOrderSvcs.size() != 0) {
						for (SaleOrderSvc saleOrderSvc : saleOrderSvcs) {
							body += saleOrderSvc.getSvcName() + ",";
						}
						subject = saleOrderSvcs.get(0).getSvcName();
						body = body.substring(0, body.length() - 1);
					} else {
						subject = no;
						body = no;
					}
					// 2、在总支付记录表中插入 一条记录 （根据订单号，查找出userid）。
					PayRefundRec payRefundRec = new PayRefundRec();
					payRefundRec.setOrderId(saleOrder.getId());
					payRefundRec.setNo(no);
					payRefundRec.setUserId(saleOrder.getUserId());
					payRefundRec.setSubject(subject);
					payRefundRec.setOrderDesc(body);
					payRefundRec.setTotalFee(WechatpayCore.getMoneyF2Y(wpr.getTotalFee()));
					payRefundRec.setPayWayName(PayWayType.wechatpay.name());
					payRefundRec.setTradeNo(wpr.getTransactionId());
					payRefundRec.setTradeStatus("TRADE_SUCCESS");
					payRefundRec.setTradeType(wpr.getTradeType());
					payRefundRec.setBankType(wpr.getBankType());
					payRefundRec.setOpenId(wpr.getOpenid());
					payRefundRec.setPayTime(DateUtil.fromCmpDateTimeToStdDateTimeStr(wpr.getTimeEnd()));
					Date canRefundDay = DateUtil.addDays(15); // TODO 这个15，之后配置
					payRefundRec.setCanRefundDay(canRefundDay);
					payRefundRecDao.insert(payRefundRec);

					// 3、在销售订单记录出，插入一条数据, 动作
					SaleOrderRecord saleOrderRecord = new SaleOrderRecord();
					saleOrderRecord.setOrderId(saleOrder.getId());
					saleOrderRecord.setAction(OrderAction.pay.name());
					saleOrderRecord.setActorId(saleOrder.getUserId());
					saleOrderRecord.setActRole("会员");
					saleOrderRecord.setActorName("客户");
					saleOrderRecord.setExtraInfo("买家已付款，请等待享受服务！");
					saleOrderRecord.setTs(new Date());
					saleOrderRecordDao.insert(saleOrderRecord);

					// 4、给用户，发送服务完成确认码。
					MapContext dataModel = MapContext.newOne();
					dataModel.put(BaseConst.TplModelVars.CODE, saleOrder.getDoneCode());
					dataModel.put(BaseConst.TplModelVars.SALE_ORDER_NO, no);
					this.sendSmsText(saleOrder.getPhoneNo(), BaseConst.SmsCodes.SALE_ORDER, dataModel);

					// 5、增加用户购买商品数量
					goodsService.updateGoodsBuySumByOrderNoAndUserId(no, saleOrder.getUserId());

					// 6、(可能)生成服务套餐票
					saleOrderService.saveUserSvcPackTicket(saleOrder);
				}

			}
		} else {// E卡订单
				// 判断是否已经处理过订单
			ECardPayRec eCardPayR = eCardPayRecDao.selectByNo(no);
			if (null == eCardPayR) {
				// 1、根据订单号，更改订单状态为已支付
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("no", no);
				map.put("paid", true);
				map.put("payTime", DateUtil.fromCmpDateTimeToStdDateTimeStr(wpr.getTimeEnd()));
//				map.put("payWay", PayWayType.wechatpay.toString());
				eCardOrderDao.updateByNo(map);
				//
				ECardOrder eCardOrder = eCardOrderDao.selectByNo(no);
				if (null != eCardOrder) {
					// 通知已成功
					xOrderMessageProxy.sendActionMessage(OrderType.ecardOrder, eCardOrder.getId().longValue(), no, eCardOrder.getUserId(), eCardOrder.getShopId(), OrderAction.pay, true);
					//
					subject = eCardOrder.getCardName();
					body = eCardOrder.getCardName() + "," + eCardOrder.getQuantity() + "张";
					// 2、在购买e卡支付记录表中插入 一条记录 （根据订单号，查找出userid）。
					ECardPayRec eCardPayRec = new ECardPayRec();
					eCardPayRec.setNo(no);
					eCardPayRec.setUserId(eCardOrder.getUserId());
					eCardPayRec.setSubject(subject);
					eCardPayRec.setOrderDesc(body);
					eCardPayRec.setTotalFee(WechatpayCore.getMoneyF2Y(wpr.getTotalFee()));
					eCardPayRec.setPayWayName(PayWayType.wechatpay.name());
					eCardPayRec.setTradeNo(wpr.getTransactionId());
					eCardPayRec.setTradeStatus("TRADE_SUCCESS");
					eCardPayRec.setTradeType(wpr.getTradeType());
					eCardPayRec.setBankType(wpr.getBankType());
					eCardPayRec.setOpenId(wpr.getOpenid());
					eCardPayRec.setPayTime(DateUtil.fromCmpDateTimeToStdDateTimeStr(wpr.getTimeEnd()));
					eCardPayRecDao.insert(eCardPayRec);

					// 3、新增用户e卡(手机端的话，截取body)
					String[] split = null;
					if (null != wpr.getAttach() && !wpr.getAttach().equals("")) {
						split = wpr.getAttach().split("\\|");
					} else {
						// split = body.split("\\|");//TODO 看手机端、公众端，是传哪个截取哪个
					}
					String cardCode = null;
					String shopId = null;
					String shopName = null;
					if (split.length > 1) {
						cardCode = split[0];
						shopId = split[1];
						shopName = split[2];
					} else {
						cardCode = split[0];
					}

					ECard eCard = eCardService.getECardById(cardCode);
					Integer eCardQuantity = eCardOrder.getQuantity();
					for (int i = 0; i < eCardQuantity; i++) {
						UserECard userECard = new UserECard();

						if (shopId != null && !shopId.equals("null")) {
							userECard.setShopId(Integer.valueOf(shopId));
							userECard.setShopName(shopName);
						}
						userECard.setUserId(eCardOrder.getUserId());
						userECard.setUserName(eCardOrder.getUserName());
						userECard.setCardCode(cardCode);
						if (null != eCard) {
							userECard.setFaceValue(eCard.getFaceVal());
							userECard.setRemainVal(eCard.getFaceVal());
						} else {
							userECard.setFaceValue(new BigDecimal("0.00"));
							userECard.setRemainVal(new BigDecimal("0.00"));
						}

						// String newCardNo = eCardService.newCardNo(ECardType.valueOf(cardCode));
						String newCardNo = eCardService.newCardNo();
						userECard.setCardNo(newCardNo);
						userECardDao.insert(userECard);

						// 4、e卡订单项
						ECardOrderItem eCardOrderItem = new ECardOrderItem();
						eCardOrderItem.setOrderId(eCardOrder.getId());
						eCardOrderItem.setCardNo(newCardNo);
						if (null != eCard) {
							eCardOrderItem.setPrice(eCard.getPrice());
							eCardOrderItem.setFaceValue(eCard.getFaceVal());
						} else {
							eCardOrderItem.setPrice(new BigDecimal("0.00"));
							eCardOrderItem.setFaceValue(new BigDecimal("0.00"));
						}

						eCardOrderItemDao.insert(eCardOrderItem);

					}

					// 5、在e卡订单记录出，插入一条数据, 动作
					ECardOrderRecord eCardOrderRecord = new ECardOrderRecord();
					eCardOrderRecord.setOrderId(eCardOrder.getId());
					eCardOrderRecord.setAction(OrderAction.finish.toString());
					eCardOrderRecord.setActorId(-1);
					eCardOrderRecord.setActorName("平台");
					eCardOrderRecordDao.insert(eCardOrderRecord);

					// 6、给用户发送短信
					MapContext dataModel = MapContext.newOne();
					dataModel.put(BaseConst.TplModelVars.ECARD_ORDER_NO, no);
					this.sendSmsText(eCardOrder.getPhoneNo(), BaseConst.SmsCodes.ECARD_ORDER, dataModel);

					// 7.首单赠送优惠券
					salePrmtService.saveEcardActGift(eCardOrder.getUserId(), eCard.getCode(), eCardQuantity, eCardOrder.getAmount());

					// 8、增加用户购买商品数量
					goodsService.updateGoodsBuySumByOrderNoAndUserId(no, eCardOrder.getUserId());

					// 9、更新用户rank
					Member member = memberDao.selectById(eCardOrder.getUserId());
					if (null == member.getRank() || eCard.getRank() > member.getRank()) {
						memberDao.updateRankById(eCard.getRank(), member.getId());
					}
				}
			}
		}

	}

	@Override
	public PayRefundRec getPayRecById(Long id) {
		return payRefundRecDao.selectById(id);
	}

}
