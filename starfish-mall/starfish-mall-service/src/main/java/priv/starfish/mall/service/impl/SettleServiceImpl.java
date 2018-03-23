package priv.starfish.mall.service.impl;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.Socket;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpException;
import org.springframework.stereotype.Service;

import com.abc.trustpay.client.JSON;
import com.abc.trustpay.client.ebus.RefundRequest;
import priv.starfish.common.http.AppUrlPool;
import priv.starfish.common.http.ContentTypes;
import priv.starfish.common.http.HttpClientX;
import priv.starfish.common.http.HttpNameValuePair;
import priv.starfish.common.http.StringResponseHandler;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.model.PayStateType;
import priv.starfish.common.model.Request;
import priv.starfish.common.model.Result;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.model.ResultX;
import priv.starfish.common.pay.PayNotifyConfig;
import priv.starfish.common.pay.abcpay.AbcpayConfig;
import priv.starfish.common.pay.alipay.util.AlipayConfig;
import priv.starfish.common.pay.dict.PayBankType;
import priv.starfish.common.pay.dict.PaySubjectType;
import priv.starfish.common.pay.dict.PayWayType;
import priv.starfish.common.pay.unionpay.util.DateUtils;
import priv.starfish.common.pay.wechatpay.util.WechatpayCore;
import priv.starfish.common.pay.wechatpay.util.XmlUtil;
import priv.starfish.common.repo.FileRepository;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.DateUtil;
import priv.starfish.common.util.JsonUtil;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.NumUtil;
import priv.starfish.common.util.TypeUtil;
import priv.starfish.mall.dao.comn.UserAccountDao;
import priv.starfish.mall.dao.ecard.ECardTransactRecDao;
import priv.starfish.mall.dao.ecard.UserECardDao;
import priv.starfish.mall.dao.merchant.MerchantDao;
import priv.starfish.mall.dao.merchant.MerchantSettleAcctDao;
import priv.starfish.mall.merchant.entity.Merchant;
import priv.starfish.mall.merchant.entity.MerchantSettleAcct;
import priv.starfish.mall.dao.order.ECardOrderRecordDao;
import priv.starfish.mall.dao.order.SaleOrderDao;
import priv.starfish.mall.dao.order.SaleOrderInnerAmountDao;
import priv.starfish.mall.dao.order.SaleOrderRecordDao;
import priv.starfish.mall.order.dict.OrderAction;
import priv.starfish.mall.order.entity.ECardOrderRecord;
import priv.starfish.mall.order.entity.SaleOrder;
import priv.starfish.mall.order.entity.SaleOrderRecord;
import priv.starfish.mall.order.entity.SaleOrderSvc;
import priv.starfish.mall.service.SaleOrderService;
import priv.starfish.mall.service.SettleService;
import priv.starfish.mall.service.util.NoUtil;
import priv.starfish.mall.dao.settle.DistSettleRecDao;
import priv.starfish.mall.dao.settle.ECardPayRecDao;
import priv.starfish.mall.dao.settle.PayRefundRecDao;
import priv.starfish.mall.dao.settle.SaleSettleRecDao;
import priv.starfish.mall.dao.settle.SettleOrderDao;
import priv.starfish.mall.dao.settle.SettleProcessDao;
import priv.starfish.mall.dao.settle.SettleRecDao;
import priv.starfish.mall.dao.settle.SettleWayDao;
import priv.starfish.mall.settle.dto.WxPayResultDto;
import priv.starfish.mall.settle.entity.DistSettleRec;
import priv.starfish.mall.settle.entity.ECardPayRec;
import priv.starfish.mall.settle.entity.PayRefundRec;
import priv.starfish.mall.settle.entity.SaleSettleRec;
import priv.starfish.mall.settle.entity.SettleOrder;
import priv.starfish.mall.settle.entity.SettleProcess;
import priv.starfish.mall.settle.entity.SettleRec;
import priv.starfish.mall.settle.entity.SettleWay;
import priv.starfish.mall.xpay.channel.ebdirect.bean.*;

@Service("settleService")
public class SettleServiceImpl extends BaseServiceImpl implements SettleService {

	@Resource
	PayRefundRecDao payRefundRecDao;

	@Resource
	ECardPayRecDao eCardPayRecDao;

	@Resource
	SettleProcessDao settleProcessDao;

	@Resource
	SettleOrderDao settleOrderDao;

	@Resource
	SettleRecDao settleRecDao;

	@Resource
	DistSettleRecDao distSettleRecDao;

	@Resource
	SaleSettleRecDao saleSettleRecDao;

	@Resource
	SaleOrderDao saleOrderDao;

	@Resource
	UserAccountDao userAccountDao;

	@Resource
	SaleOrderRecordDao saleOrderRecordDao;

	@Resource
	ECardOrderRecordDao eCardOrderRecordDao;

	@Resource
	MerchantDao merchantDao;

	@Resource
	UserECardDao userECardDao;

	@Resource
	ECardTransactRecDao eCardTransactRecDao;

	@Resource
	MerchantSettleAcctDao merchantSettleAcctDao;

	@Resource
	SaleOrderInnerAmountDao saleOrderInnerAmountDao;

	@Resource
	SettleWayDao settleWayDao;

	@Resource
	FileRepository fileRepository;

	@Resource
	SaleOrderService saleOrderService;

	@Override
	public List<SettleWay> getSettleWays(boolean includeDisabled) {
		return settleWayDao.selectAll(includeDisabled);
	}

	@Override
	public PaginatedList<SettleRec> getSettleRecsByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<SettleRec> pagListSettleRecs = settleRecDao.selectByFilter(paginatedFilter);
		return pagListSettleRecs;
	}

	@Override
	public PaginatedList<SaleSettleRec> getSaleSettleRecsByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<SaleSettleRec> paginatedList = saleSettleRecDao.selectByFilter(paginatedFilter);
		//
		List<SaleSettleRec> list = paginatedList.getRows();
		for (SaleSettleRec saleSettleRec : list) {
			SettleProcess settleProcess = settleProcessDao.selectById(saleSettleRec.getSettleProcessId());
			saleSettleRec.setSettleProcess(settleProcess);
		}
		return paginatedList;
	}

	@Override
	public Boolean savePayRefundRec(PayRefundRec payRefundRec) {
		return payRefundRecDao.insert(payRefundRec) > 0;
	}

	@Override
	public PaginatedList<PayRefundRec> getSalePayRecsByFilter(PaginatedFilter paginatedFilter) {
		return payRefundRecDao.selectByFilter(paginatedFilter);
	}

	@Override
	public PayRefundRec getSalePayRecByOrderId(String orderId) {
		return payRefundRecDao.selectByOrderId(orderId);
	}

	@Override
	public Boolean updatePayRefundRecForRefund(PayRefundRec payRefundRec) {
		return payRefundRecDao.updatePayRefundRecForRefund(payRefundRec) > 0;
	}

	@Override
	public PayRefundRec getSalePayRecByTradeNo(String tradeNo) {
		return payRefundRecDao.selectByTradeNo(tradeNo);
	}

	@Override
	public PayRefundRec getSalePayRecByOrderNo(String no) {
		return payRefundRecDao.selectByOrderNo(no);
	}

	@Override
	public SettleProcess getSettleProcessById(Long id) {
		return settleProcessDao.selectById(id);
	}

	@Override
	public Boolean updateSettleProcessForReq(SettleProcess settleProcess) {
		return settleProcessDao.updateForReq(settleProcess) > 0;
	}

	@Override
	public SettleProcess getSettleProcessByReqNo(String reqSerialNumber) {
		return settleProcessDao.selectByReqNo(reqSerialNumber);
	}

	@Override
	public Boolean updateSettleProcessForResp(SettleProcess settleProcess) {
		return settleProcessDao.updateForResp(settleProcess) > 0;
	}

	@Override
	public Boolean updateRefundStatus(PayRefundRec payRefundRec) {
		return payRefundRecDao.updateRefundStatus(payRefundRec) > 0;
	}

	@Override
	public PayRefundRec getSalePayRecById(Long id) {
		return payRefundRecDao.selectById(id);
	}

	@Override
	public Boolean saveSaleSettleRec(SaleSettleRec saleSettleRec) {
		return saleSettleRecDao.insert(saleSettleRec) > 0;
	}

	@Override
	public PaginatedList<SettleProcess> getSettleProcessByFilterAsMall(PaginatedFilter paginatedFilter) {
		PaginatedList<SettleProcess> paginatedList = settleProcessDao.selectByFilterAsMall(paginatedFilter);
		//
		for (SettleProcess settleProcess : paginatedList.getRows()) {
			settleProcess.setSettleAmount(settleProcess.getSettleAmount().substring(0, settleProcess.getSettleAmount().length() - 2));
			// 如果settleFlag为1，且tempDate不为空，比较当前时间与tempDate。
			if (settleProcess.getSettleFlag() == 1 && null != settleProcess.getTempDay()) {
				if (new Date().compareTo(settleProcess.getTempDay()) > 0) {// 如果当前时间大于tempDay，则设置settleProcess为3待结算。且更新结算单状态为3
					settleProcess.setSettleFlag(3);
					settleProcess.setTempDay(null);
					//
					settleProcessDao.updateSettleFlag(settleProcess);
				}
			}

		}
		//
		return paginatedList;
	}

	@Override
	public PaginatedList<SettleProcess> getSettleProcessByFilterAsMerch(PaginatedFilter paginatedFilter) {
		PaginatedList<SettleProcess> paginatedList = settleProcessDao.selectByFilterAsMerch(paginatedFilter);
		//
		for (SettleProcess settleProcess : paginatedList.getRows()) {
			settleProcess.setSettleAmount(settleProcess.getSettleAmount().substring(0, settleProcess.getSettleAmount().length() - 2));
		}
		//
		return paginatedList;
	}

	@Override
	public PaginatedList<PayRefundRec> getToRefundAuditRecs(PaginatedFilter paginatedFilter) {
		return payRefundRecDao.selectByToRefundFilter(paginatedFilter);
	}

	@Override
	public PaginatedList<PayRefundRec> getCanRefundRecsByFilter(PaginatedFilter paginatedFilter) {
		return payRefundRecDao.selectByCanRefundFilter(paginatedFilter);
	}

	@SuppressWarnings("unchecked")
	@Override
	public Boolean updateRefundStatusToRefunding(UserContext userContext, MapContext requestData) {
		Integer refundStatus = requestData.getTypedValue("refundStatus", Integer.class);
		Integer flag = requestData.getTypedValue("flag", Integer.class);

		if (flag == 1) {
			String id = requestData.getTypedValue("id", String.class);
			// Date tempDay = DateUtil.addDays(1);
			PayRefundRec prr = payRefundRecDao.selectById(Long.valueOf(id));
			// 如果状态不为 已退款或者退款失败
			if (prr.getRefundStatus() != 0 && prr.getRefundStatus() != 1) {
				prr.setRefundStatus(refundStatus);
				payRefundRecDao.updateRefundStatus(prr);
			}

			// prr.setTempDay(tempDay);

			// 插入销售订单 操作记录
			String action = OrderAction.refund.toString();
			SaleOrderRecord saleOrderRecord = new SaleOrderRecord();

			saleOrderRecord.setOrderId(prr.getOrderId());
			saleOrderRecord.setAction(action);
			saleOrderRecord.setActRole("财务");
			saleOrderRecord.setActorId(userContext.getUserId());
			saleOrderRecord.setActorName(userContext.getUserName());
			int num = saleOrderRecordDao.insert(saleOrderRecord);

			return num > 0;

		} else {
			List<String> ids = requestData.getTypedValue("ids", TypeUtil.Types.StringList.getClass());
			int count = 0;
			for (String id : ids) {
				PayRefundRec prr = payRefundRecDao.selectById(Long.valueOf(id));
				// Date tempDay = DateUtil.addDays(1);
				// payRefundRec.setTempDay(tempDay);
				if (prr.getRefundStatus() != 0 && prr.getRefundStatus() != 1) {
					prr.setRefundStatus(refundStatus);
					payRefundRecDao.updateRefundStatus(prr);
				}

				count++;
				// 插入销售订单 操作记录
				String action = OrderAction.refund.toString();
				SaleOrderRecord saleOrderRecord = new SaleOrderRecord();
				saleOrderRecord.setOrderId(prr.getOrderId());
				saleOrderRecord.setAction(action);
				saleOrderRecord.setActRole("财务");
				saleOrderRecord.setActorId(userContext.getUserId());
				saleOrderRecord.setActorName(userContext.getUserName());
				saleOrderRecordDao.insert(saleOrderRecord);
			}

			return ids.size() == count;
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public Boolean updateSettleProcessDoing(UserContext userContext, MapContext requestData) {

		Integer settleFlag = requestData.getTypedValue("settleFlag", Integer.class);
		Integer flag = requestData.getTypedValue("flag", Integer.class);

		Date tempDay = DateUtil.addHours(1); // TODO 这里设置延后1小时，才可以再次结算
		if (flag == 1) {
			SettleProcess settleProcess = new SettleProcess();
			settleProcess.setSettleFlag(settleFlag);
			settleProcess.setTempDay(tempDay);
			String id = requestData.getTypedValue("id", String.class);
			settleProcess.setId(Long.valueOf(id));
			//
			return settleProcessDao.updateSettleFlag(settleProcess) > 0;
		} else {
			List<String> ids = requestData.getTypedValue("ids", TypeUtil.Types.StringList.getClass());
			int count = 0;
			for (String id : ids) {
				SettleProcess settleProcess = new SettleProcess();
				settleProcess.setSettleFlag(settleFlag);
				settleProcess.setTempDay(tempDay);
				settleProcess.setId(Long.valueOf(id));
				settleProcessDao.updateSettleFlag(settleProcess);
				count++;
			}

			return ids.size() == count;
		}
	}

	@Override
	public void saveSettleInfo(Date settleDay, Date beforeSettleDay) {
		SettleProcess settleProcess = new SettleProcess();
		settleProcess.setSettleDay(settleDay);
		settleProcess.setSettleFlag(4);// 商户未确认
		// 查询所有订单，settleTime = （finishedTime + T+X ）在本期账单内的。
		List<SaleOrder> list = saleOrderDao.selectByCompareSettleDay(settleDay, beforeSettleDay);
		List<Integer> merchantIds = saleOrderDao.selectMerchantIds(settleDay, beforeSettleDay);
		if (null != merchantIds && merchantIds.size() != 0) {
			for (Integer integer : merchantIds) {
				settleProcess.setMerchantId(integer);
				// 根据商户ID，查询商户结算账户
				List<MerchantSettleAcct> merchantSettleAccts = merchantSettleAcctDao.selectByMerchantId(integer);
				if (null != merchantSettleAccts && merchantSettleAccts.size() != 0) {
					// TODO 目前是选择的第一个，以后根据需求更改，最好是：这个表里就设定一个结算账户。
					// 且是银行卡的账户。不能是支付宝或微信等其他的。
					settleProcess.setAcctName(merchantSettleAccts.get(0).getAcctName());
					settleProcess.setAcctNo(merchantSettleAccts.get(0).getAcctNo());
				}
				//
				BigDecimal settleAmount = new BigDecimal("0.00");

				if (null != list && list.size() != 0) {
					for (SaleOrder saleOrder : list) {
						Integer merchantId = saleOrder.getMerchantId();
						if (merchantId == integer) {
							settleAmount = settleAmount.add(saleOrder.getSettleAmount());
						}
					}
				}

				settleProcess.setSettleAmount(String.valueOf(settleAmount));
				settleProcessDao.insert(settleProcess);

				if (null != list && list.size() != 0) {
					for (SaleOrder saleOrder : list) {
						Integer merchantId = saleOrder.getMerchantId();
						if (merchantId == integer) {
							SettleOrder settleOrder = new SettleOrder();
							settleOrder.setProcessId(settleProcess.getId());
							settleOrder.setNo(saleOrder.getNo());
							settleOrderDao.insert(settleOrder);
						}
					}
				}

			}
		}
	}

	@Override
	public Boolean getSettleProcessBySettleDay(Date settleDay) {
		return settleProcessDao.selectBySettleDay(settleDay) > 0;
	}

	@Override
	public PaginatedList<SettleProcess> getSettleProcessByFilterAsSuccess(PaginatedFilter paginatedFilter) {
		return settleProcessDao.selectByFilterAsSuccess(paginatedFilter);
	}

	@Override
	public PaginatedList<SaleOrder> getOrderDeatilListBySettleDay(PaginatedFilter paginatedFilter) {
		return saleOrderDao.selectByFilterAsSettleDay(paginatedFilter);
	}

	@Override
	public ECardPayRec getECardPayRecByNo(String out_trade_no) {
		return eCardPayRecDao.selectByNo(out_trade_no);
	}

	@Override
	public Boolean saveECardPayRec(ECardPayRec eCardPayRec) {
		return eCardPayRecDao.insert(eCardPayRec) > 0;
	}

	@Override
	public Boolean saveECardOrderRecord(ECardOrderRecord eCardOrderRecord) {
		return eCardOrderRecordDao.insert(eCardOrderRecord) > 0;
	}

	@Override
	public PaginatedList<PayRefundRec> getRefundAuditRecs(PaginatedFilter paginatedFilter) {
		return payRefundRecDao.selectByRefundAuditRecsFilter(paginatedFilter);
	}

	@Override
	public Boolean updatePayRefundRecByNo(PayRefundRec payRefundRec) {
		return payRefundRecDao.updateRefundStatus(payRefundRec) > 0;
	}

	@Override
	public PaginatedList<ECardPayRec> getECardPayRecsByFilter(PaginatedFilter paginatedFilter) {
		return eCardPayRecDao.selectByFilter(paginatedFilter);
	}

	@Override
	public Boolean updateRefundStatusToRefuseRefund(UserContext userContext, MapContext requestData) {
		Integer refundStatus = requestData.getTypedValue("refundStatus", Integer.class);
		String id = requestData.getTypedValue("id", String.class);

		PayRefundRec payRefundRec = new PayRefundRec();
		payRefundRec.setRefundStatus(refundStatus);
		payRefundRec.setId(Long.valueOf(id));
		// 插入销售订单 操作记录
		String action = OrderAction.refuseRefund.toString();
		SaleOrderRecord saleOrderRecord = new SaleOrderRecord();
		PayRefundRec prr = payRefundRecDao.selectById(payRefundRec.getId());

		saleOrderRecord.setOrderId(prr.getOrderId());
		saleOrderRecord.setAction(action);
		saleOrderRecord.setActorId(userContext.getUserId());
		saleOrderRecord.setActorName(userContext.getUserName());
		saleOrderRecordDao.insert(saleOrderRecord);

		return payRefundRecDao.updateRefundStatus(payRefundRec) > 0;
	}

	@Override
	public SaleSettleRec getSaleSettleRecByReqNo(String reqNo) {
		return saleSettleRecDao.selectByReqNo(reqNo);
	}

	@Override
	public Boolean updateSaleSettleRecState(SaleSettleRec saleSettleRec) {
		return saleSettleRecDao.updateForState(saleSettleRec) > 0;
	}

	@Override
	public void payFinished(SaleOrder saleOrder) {
		NumberFormat fenFormat = NumUtil.getNumFormat("#0.00");
		//
		PayRefundRec payRefundRec = new PayRefundRec();
		payRefundRec.setOrderId(saleOrder.getId());
		payRefundRec.setNo(saleOrder.getNo());
		payRefundRec.setUserId(saleOrder.getUserId());
		//
		String subject = "";
		String body = "";
		List<SaleOrderSvc> saleOrderSvcs = saleOrderService.getSaleOrderSvcsByOrderId(saleOrder.getId());
		if (saleOrderSvcs.size() != 0) {
			for (SaleOrderSvc saleOrderSvc : saleOrderSvcs) {
				body += saleOrderSvc.getSvcName() + ",";
			}
			subject = saleOrderSvcs.get(0).getSvcName();
			body = body.substring(0, body.length() - 1);
		} else {
			subject = saleOrder.getNo();
			body = saleOrder.getNo();
		}
		//
		payRefundRec.setSubject(subject);
		payRefundRec.setOrderDesc(body);
		payRefundRec.setTotalFee(fenFormat.format(saleOrder.getAmountInner()));
		payRefundRec.setPayWayName(PayWayType.ecardpay.toString());
		payRefundRec.setTradeStatus("TRADE_SUCCESS");
		payRefundRec.setTradeNo("--无--");
		payRefundRec.setPayTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		Date canRefundDay = DateUtil.addDays(15); // TODO 这个15，之后配置
		payRefundRec.setCanRefundDay(canRefundDay);
		payRefundRecDao.insert(payRefundRec);
		// 3、在销售订单记录出，插入一条数据, 动作
		SaleOrderRecord saleOrderRecord = new SaleOrderRecord();
		saleOrderRecord.setOrderId(saleOrder.getId());
		saleOrderRecord.setAction(OrderAction.pay.toString());
		saleOrderRecord.setActorId(saleOrder.getUserId());
		saleOrderRecord.setActRole("会员");
		saleOrderRecord.setActorName("客户");
		saleOrderRecord.setExtraInfo("买家已付款，请等待享受服务！");
		saleOrderRecord.setTs(new Date());
		saleOrderRecordDao.insert(saleOrderRecord);
	}

	@Override
	public PaginatedList<SaleSettleRec> getSettleListAsMerch(PaginatedFilter paginatedFilter) {
		PaginatedList<SaleSettleRec> paginatedList = saleSettleRecDao.selectByFilter(paginatedFilter);
		//
		List<SaleSettleRec> list = paginatedList.getRows();
		for (SaleSettleRec saleSettleRec : list) {
			SettleProcess settleProcess = settleProcessDao.selectById(saleSettleRec.getSettleProcessId());
			saleSettleRec.setSettleProcess(settleProcess);
		}
		return paginatedList;

	}

	@Override
	public Boolean updateSettleProcessSettleFlag(SettleProcess settleProcess) {
		return settleProcessDao.updateSettleFlag(settleProcess) > 0;
	}

	@Override
	public List<SaleSettleRec> countCapitalByMerch(Map<String, Object> params) {
		return saleSettleRecDao.selectByMerchantIdAsSuccess(params);
	}

	@Override
	public Map<String, String> createReqTransferData(UserContext userContext, String ids) {

		// 付款当天日期 必填，格式：年[4位]月[2位]日[2位]，如：20100801
		String pay_date = new SimpleDateFormat("yyyyMMdd").format(new Date());
		// 批次号 必填，格式：11～32位的数字或字母或数字与字母的组合，且区分大小写。
		String batch_no = NoUtil.newTransferBillNo();

		String[] split = ids.split("\\,");
		int num = split.length;

		// 付款笔数 必填，即参数detail_data的值中，“|”字符出现的数量加1，最大支持1000笔（即“|”字符出现的数量999个）
		String batch_num = String.valueOf(num);

		NumberFormat fenFormat = NumUtil.getNumFormat("#0.00");
		
		// 付款总金额 必填，即参数detail_data的值中所有金额的总和
		BigDecimal count = new BigDecimal("0.00");

		// 付款详细数据 必填，格式：流水号1^收款方帐号1^真实姓名^付款金额1^备注说明1|流水号2^收款方帐号2^真实姓名^付款金额2^备注说明2....
		String detail_data = "";
		for (String id : split) {
			String newTransferBillNo = NoUtil.newTransferBillNo();
			//
			SettleProcess settleProcess = settleProcessDao.selectById(Long.valueOf(id));
			// 结算状态不能为已成功的
			if (null != settleProcess && settleProcess.getSettleFlag() != 0) {
				settleProcess.setBatchNo(batch_no);
				settleProcess.setReqNo(newTransferBillNo);
				settleProcess.setMemo(AlipayConfig.transferExplain);
				settleProcessDao.updateForReq(settleProcess);
				//
				BigDecimal one = new BigDecimal(settleProcess.getSettleAmount());
				count = count.add(one);
				detail_data += settleProcess.getReqNo() + "^" + settleProcess.getAcctNo() + "^" + settleProcess.getAcctName() + "^" + fenFormat.format(one) + "^" + settleProcess.getMemo() + "|";
				// 判断是否有记录，没有的话，插入记录.有的话，更新reqNo.
				SaleSettleRec saleRec = saleSettleRecDao.selecBySettleProcessId(settleProcess.getId());
				if (null == saleRec) {
					//
					SaleSettleRec saleSettleRec = new SaleSettleRec();
					saleSettleRec.setSubject(PaySubjectType.saleOrder.toString());
					saleSettleRec.setSettleProcessId(settleProcess.getId());
					saleSettleRec.setPeerType(0);
					saleSettleRec.setPeerId(settleProcess.getMerchantId());
					saleSettleRec.setPeerName(settleProcess.getMerchantName());
					saleSettleRec.setBankCode(PayBankType.alipay.toString());
					saleSettleRec.setAcctNo(settleProcess.getAcctNo());
					saleSettleRec.setAcctName(settleProcess.getAcctName());
					saleSettleRec.setDirectFlag(1);
					saleSettleRec.setAmount(new BigDecimal(settleProcess.getSettleAmount()));
					//
					Integer operatorId = userContext.getUserId();
					String operatorName = userContext.getUserName();
					//
					saleSettleRec.setOperatorId(operatorId);
					saleSettleRec.setReqNo(newTransferBillNo);
					saleSettleRec.setOperatorName(operatorName);
					saleSettleRec.setConfirmed(false);
					saleSettleRec.setState(1);
					saleSettleRec.setSettleDay(settleProcess.getSettleDay());
					saleSettleRecDao.insert(saleSettleRec);
					// 根据结算单ID，查找settleorder里所有相关的orderNo，更新这些结算记录ID
					List<SettleOrder> settleOrderList = settleOrderDao.selectBySettleProcessId(settleProcess.getId());
					for (SettleOrder settleOrder : settleOrderList) {
						saleOrderDao.updateForRecId(settleOrder.getNo(), saleSettleRec.getId());
					}

				} else {
					saleRec.setReqNo(newTransferBillNo);
					saleSettleRecDao.updateForReqData(saleRec);
				}

			}

		}
		detail_data = detail_data.substring(0, detail_data.length() - 1);
		String batch_fee = fenFormat.format(count);
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

		return sParaTemp;
	}

	@Override
	public void transferFinishedOperation(String success_details, String fail_details) {

		// 转账成功的
		if (null != success_details && !success_details.equals("")) {
			String[] everyDeatils_s = success_details.split("\\|");
			for (String oneDeatil : everyDeatils_s) {
				String[] deatil = oneDeatil.split("\\^");
				String reqNo = deatil[0];
				// String settleFlag = deatil[4];
				// String reason1 = deatil[5];
				String reason = AlipayConfig.successReason;
				String repNo = deatil[6];
				String doneTime = deatil[7];

				// 更新结算流程表里 转账结果数据
				SettleProcess settleProcess = settleProcessDao.selectByReqNo(reqNo);
				settleProcess.setSettleFlag(0);
				settleProcess.setTempDay(null);
				settleProcess.setReason(reason);
				settleProcess.setRepNo(repNo);
				settleProcess.setDoneTime(doneTime);
				settleProcessDao.updateForResp(settleProcess);

				// 更新结算记录的状态
				SaleSettleRec saleSettleRec = saleSettleRecDao.selectByReqNo(reqNo);
				saleSettleRec.setState(3);
				saleSettleRecDao.updateForState(saleSettleRec);
			}
		}
		// 转账失败的
		if (null != fail_details && !fail_details.equals("")) {
			String[] everyDeatils_f = fail_details.split("\\|");
			for (String oneDeatil : everyDeatils_f) {
				String[] deatil = oneDeatil.split("\\^");
				String reqNo = deatil[0];
				// String settleFlag = deatil[4];
				String reason = deatil[5];
				// String reason = AlipayConfig.successReason;
				String repNo = deatil[6];
				String doneTime = deatil[7];

				// 更新结算流程表里 转账结果数据
				SettleProcess settleProcess = settleProcessDao.selectByReqNo(reqNo);
				settleProcess.setSettleFlag(2);
				settleProcess.setTempDay(null);
				settleProcess.setReason(reason);
				settleProcess.setRepNo(repNo);
				settleProcess.setDoneTime(doneTime);
				settleProcessDao.updateForResp(settleProcess);

				// 更新结算记录的状态
				SaleSettleRec saleSettleRec = saleSettleRecDao.selectByReqNo(reqNo);
				saleSettleRec.setState(4);
				saleSettleRecDao.updateForState(saleSettleRec);
			}
		}

	}

	@Override
	public Map<String, String> createReqRefundData(String ids) {

		// 退款当天日期 格式：年[4位]-月[2位]-日[2位] 小时[2位 24小时制]:分[2位]:秒[2位]，如：2007-10-01 13:13:13
		String refund_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		// 批次号 格式：当天日期[8位]+序列号[3至24位]，如：201008010000001
		String batch_no = NoUtil.newTransferBillNo();

		String[] split = ids.split("\\,");
		int num = split.length;

		// 退款笔数
		String batch_num = String.valueOf(num);
		// 退款详细数据 格式:原付款支付宝交易号1^退款总金额1^退款理由1#...
		String detail_data = "";
		for (String id : split) {
			// 根据id，从 支付记录表 中查找出交易号、金额，拼接 加上退款理由
			PayRefundRec payRefundRec = payRefundRecDao.selectById(Long.valueOf(id));

			if (null != payRefundRec) {
				detail_data += payRefundRec.getTradeNo() + "^" + payRefundRec.getTotalFee() + "^" + AlipayConfig.refundReason + "#";

				// 往支付记录表里 更新数据（退款批次号，退款时间）
				// payRefundRec.setBatchNo(batch_no);
				payRefundRec.setRefundTime(refund_date);
				payRefundRecDao.updatePayRefundRecForRefund(payRefundRec);
			}
		}
		// 去掉最后一个 #
		detail_data = detail_data.substring(0, detail_data.length() - 1);
		//
		Map<String, String> sParaTemp = new HashMap<String, String>();
		sParaTemp.put("service", "refund_fastpay_by_platform_pwd");
		sParaTemp.put("partner", AlipayConfig.partner);
		sParaTemp.put("_input_charset", AlipayConfig.input_charset);
		sParaTemp.put("notify_url", PayNotifyConfig.ali_refund_notify_url);
		sParaTemp.put("seller_user_id", AlipayConfig.seller_id);
		sParaTemp.put("refund_date", refund_date);
		sParaTemp.put("batch_no", batch_no);
		sParaTemp.put("batch_num", batch_num);
		sParaTemp.put("detail_data", detail_data);

		return sParaTemp;
	}

	@Override
	public void refundFinishedOperation(String result_details, String batch_no) {

		String[] everyDetail = result_details.split("\\#");
		for (String oneDetail : everyDetail) {
			String[] detail = oneDetail.split("\\^");
			String tradeNo = detail[0];
			String resultCode = detail[2];
			//
			PayRefundRec payRefundRec = payRefundRecDao.selectByTradeNo(tradeNo);

			if (null != payRefundRec) {
				// 判断是否处理过，如果状态不为已退款，则执行更新
				if (payRefundRec.getRefundStatus() != 0) {

					Map<String, Object> map = new HashMap<String, Object>();
					map.put("no", payRefundRec.getNo());
					if (resultCode.equals("SUCCESS") || resultCode.equals("success")) {

						// 1、根据订单号，把支付退款记录表中，退款状态改为 成功/失败。
						payRefundRec.setRefundStatus(0);
						payRefundRec.setBatchNo(batch_no);
						payRefundRec.setTempDay(null);
						payRefundRecDao.updateRefundStatus(payRefundRec);

						// 2、根据交易号，查到订单号，把订单支付状态改为 已退款。
						map.put("payState", PayStateType.refunded.toString());
						saleOrderDao.updatePayStateByNo(map);

						// 3、查询此订单是否使用了内部支付，是的话执行操作（还原E卡、优惠券等）
						saleOrderService.restoreSaleOrderInfo(payRefundRec.getOrderId());

						// 4、判断是否为服务套餐票，是的话，销毁
						boolean flag = this.judgeSaleOrderSvcPackId(payRefundRec.getNo());
						if (flag) {
							saleOrderService.deleteUserSvcPackTicketByUserIdAndOrderNo(payRefundRec.getUserId(), payRefundRec.getNo());
						}
						
						// 5、 退款成功后关闭订单
						saleOrderService.updateForClosed(payRefundRec.getOrderId());

					} else if (resultCode.equals("TRADE_HAS_CLOSED") || resultCode.equals("trade_has_closed")) {// 说明重复退款了，把状态改为已退款
						// 1、根据订单号，把支付退款记录表中，退款状态改为 成功/失败。
						payRefundRec.setRefundStatus(0);
						payRefundRec.setBatchNo(null);
						payRefundRec.setTempDay(null);
						payRefundRecDao.updateRefundStatus(payRefundRec);
						
						// 2、 退款成功后关闭订单
						saleOrderService.updateForClosed(payRefundRec.getOrderId());
						
					} else {
						payRefundRec.setRefundStatus(1);
						payRefundRec.setBatchNo(batch_no);
						// payRefundRec.setTempDay(null);
						payRefundRecDao.updateRefundStatus(payRefundRec);
						//
						map.put("payState", PayStateType.refundFail.toString());
						saleOrderDao.updatePayStateByNo(map);
					}
				}
			}

		}
	}

	@Override
	@SuppressWarnings("unchecked")
	public Boolean refundDoForECard(MapContext requestData, UserContext userContext) {

		Integer refundStatus = requestData.getTypedValue("refundStatus", Integer.class);
		Integer flag = requestData.getTypedValue("flag", Integer.class);
		Boolean ok = true;
		String refund_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

		if (flag == 1) {
			String id = requestData.getTypedValue("id", String.class);

			PayRefundRec payRefundRec = payRefundRecDao.selectById(Long.valueOf(id));
			if (null != payRefundRec) {

				// 退款、及各种退回操作(还原E卡、优惠券等信息)
				saleOrderService.restoreSaleOrderInfo(payRefundRec.getOrderId());
				// 操作、记录
				SaleOrderRecord saleOrderRecord = new SaleOrderRecord();
				saleOrderRecord.setOrderId(payRefundRec.getOrderId());
				saleOrderRecord.setAction(OrderAction.refund.toString());
				saleOrderRecord.setActorId(userContext.getUserId());
				saleOrderRecord.setActorName(userContext.getUserName());
				saleOrderRecordDao.insert(saleOrderRecord);
				// 更改订单状态为已退款
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("no", payRefundRec.getNo());
				map.put("payState", PayStateType.refunded.toString());
				saleOrderDao.updatePayStateByNo(map);

				// 更改状态
				payRefundRec.setRefundStatus(refundStatus);
				payRefundRec.setRefundTime(refund_date);
				payRefundRec.setBatchNo("--无--");
				payRefundRec.setTradeNo("--无--");

				// 4、判断是否为服务套餐票，是的话，销毁
				boolean flag2 = this.judgeSaleOrderSvcPackId(payRefundRec.getNo());
				if (flag2) {
					saleOrderService.deleteUserSvcPackTicketByUserIdAndOrderNo(payRefundRec.getUserId(), payRefundRec.getNo());
				}
				
				// 5、 退款成功后关闭订单
				saleOrderService.updateForClosed(payRefundRec.getOrderId());

				ok = payRefundRecDao.updateRefundStatus(payRefundRec) > 0;
			} else {
				ok = false;
			}
		} else {
			List<String> ids = requestData.getTypedValue("ids", TypeUtil.Types.StringList.getClass());
			int count = 0;
			if (null != ids && ids.size() != 0) {
				for (String id : ids) {
					PayRefundRec payRefundRec = payRefundRecDao.selectById(Long.valueOf(id));
					if (null != payRefundRec) {
						// 退款
						saleOrderService.restoreSaleOrderInfo(payRefundRec.getOrderId());
						// 操作、记录
						SaleOrderRecord saleOrderRecord = new SaleOrderRecord();
						saleOrderRecord.setOrderId(payRefundRec.getOrderId());
						saleOrderRecord.setAction(OrderAction.refund.toString());
						saleOrderRecord.setActorId(userContext.getUserId());
						saleOrderRecord.setActorName(userContext.getUserName());
						saleOrderRecordDao.insert(saleOrderRecord);
						// 更改订单状态为已退款
						Map<String, Object> map = new HashMap<String, Object>();
						map.put("no", payRefundRec.getNo());
						map.put("payState", PayStateType.refunded.toString());
						saleOrderDao.updatePayStateByNo(map);
						// 更改状态
						payRefundRec.setRefundStatus(refundStatus);
						payRefundRec.setRefundTime(refund_date);
						payRefundRec.setBatchNo("--无--");
						payRefundRec.setTradeNo("--无--");
						payRefundRecDao.updateRefundStatus(payRefundRec);
						// 退款成功后关闭订单
						saleOrderService.updateForClosed(payRefundRec.getOrderId());
						count++;
					}
				}
			}

			ok = ids.size() == count;
		}

		return ok;
	}

	@Override
	public Result<String> createSettleInfoByAlipay() throws Exception {

		Result<String> result = Result.newOne();
		Date date = new Date();
		int dateOfMoth = DateUtils.getDateOfMoth();
		int year = DateUtils.getYear(date);
		int month = DateUtils.getMonth(date);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date settleDay = df.parse(String.valueOf(year) + "-" + String.valueOf(month) + "-" + 15);
		Date beforeSettleDay = DateUtils.getBeforeMonthToday(settleDay);
		Date beforeSettleDayTwo = DateUtils.getBeforeMonthToday(beforeSettleDay);
		String strDate = df.format(settleDay);
		String strDate2 = df.format(beforeSettleDay);
		if (dateOfMoth >= 15) {
			boolean flag = settleProcessDao.selectBySettleDay(settleDay) > 0;
			if (flag) {
				result.message = "本期账单：" + strDate + "已经生成过";
				result.type = Type.warn;
			} else {
				this.saveSettleInfo(settleDay, beforeSettleDay);
				result.message = "账期：" + strDate + "的账单生成成功";
			}
		} else {
			boolean flag = settleProcessDao.selectBySettleDay(beforeSettleDay) > 0;
			if (flag) {
				result.message = "本期账单：" + strDate2 + "已经生成过";
				result.type = Type.warn;
			} else {
				this.saveSettleInfo(beforeSettleDay, beforeSettleDayTwo);
				result.message = "账期：" + strDate2 + "的账单生成成功";
			}
		}

		return result;

	}

	@Override
	public void createSettleInfo() {
		// 查询所有启用的商户
		Boolean disabled = false;
		List<Merchant> merList = merchantDao.selectMerchantsAsEnabled(disabled);
		//
		if (null != merList && merList.size() != 0) {

			for (Merchant merchant : merList) {
				// 查询最后一次生成结算单的日期
				Date maxCreateDay = settleProcessDao.selectMaxCreateDay(merchant.getId());
				//
				if (null != maxCreateDay) {// 不为空，则从maxCreateDay开始生成结算单，到当前日期前一天
					// 生成结算数据
					this.createSettleData(merchant, maxCreateDay);
				} else { // 从未生成过结算单，则查询最早完成的订单，从那一天开始统计逐天生成结算单
					// 查询此商户，订单列表中，最早完成的订单的日期
					Date minSettleDay = saleOrderDao.selectMinFinishedDay(merchant.getId());
					// 如果不为空，则生成结算单
					if (null != minSettleDay) {
						this.createSettleData(merchant, minSettleDay);
					}
				}
			}
		}

	}

	public void createSettleData(Merchant merchant, Date date) {
		Date today = new Date();
		// 循环日期，生成结算单
		while (!DateUtil.toStdDateStr(date).equals(DateUtil.toStdDateStr(today))) {// 如果下一天跟今天不同，则开始生成结算单
			// 查询所有订单:merchantId为此商户的、完成日期、还未关联结算记录(settleRecId为空)的、已完成的。
			List<SaleOrder> list = saleOrderDao.selectForCreateSettleInfo(merchant.getId(), date);

			if (null != list && list.size() != 0) {// 当天有订单，则生成结算单
				//
				SettleProcess settleProcess = new SettleProcess();
				settleProcess.setMerchantId(merchant.getId());
				settleProcess.setMerchantName(merchant.getName());

				this.setAcctNoAndSettleFlag(merchant.getId(), date, settleProcess);
				//
				if (null != settleProcess.getSettleFlag()) {// 容错：setAcctNoAndSettleFlag里，那个：否则昨天没有完成的
					// 计算结算金额
					BigDecimal settleAmount = new BigDecimal("0.0000");
					for (SaleOrder saleOrder : list) {
						if (null != saleOrder.getSettleAmount()) {
							settleAmount = settleAmount.add(saleOrder.getSettleAmount());
						}
					}
					settleProcess.setSettleAmount(String.valueOf(settleAmount));
					settleProcess.setCreateDay(new Date());
					//
					settleProcessDao.insert(settleProcess);

					// 插入结算单、订单关联
					for (SaleOrder saleOrder : list) {
						SettleOrder settleOrder = new SettleOrder();
						settleOrder.setProcessId(settleProcess.getId());
						settleOrder.setNo(saleOrder.getNo());
						settleOrderDao.insert(settleOrder);
					}
				}
			}
			// 加一天
			date = DateUtil.addDays(date, 1);
		}
	}

	public void setAcctNoAndSettleFlag(Integer merchantId, Date date, SettleProcess settleProcess) {
		//
		Date settleDay = null;

		// 查询当前启用的结算方式,一般情况下，当前只启用一种。（如果有多种，则依据seqNo为优先级，优先依次生成）
		List<SettleWay> settleWayList = settleWayDao.selectAll(false);
		if (null != settleWayList && settleWayList.size() != 0) {
			//
			if (null != settleProcess.getSettleFlag() && settleProcess.getSettleFlag() == 7) {// 如果已为7，这个是更新结算单时用
				// 判断状态，给settleFlag赋值
				this.updateSettleFlagWhenCRB(settleProcess.getSettleDay(), merchantId, settleProcess);
			} else {// 否则为8，或者空
					// 根据商户ID，查询商户结算账户
				List<MerchantSettleAcct> merchantSettleAccts = merchantSettleAcctDao.selectByMerchantId(merchantId);
				if (null != merchantSettleAccts && merchantSettleAccts.size() != 0) {
					// 临时存储结算方式排序的变量
					Integer temp = 0;
					// 循环结算方式
					for (SettleWay settleWay : settleWayList) {
						//
						for (MerchantSettleAcct merchantSettleAcct : merchantSettleAccts) {
							//
							if (merchantSettleAcct.getSettleWayCode().equals(settleWay.getCode())) {
								// 获取结算单的结算方式，如果为空，则生成。
								// 或者当前的seqNo比已有的小，则覆盖
								if (null == settleProcess.getSettleWay() || settleWay.getSeqNo() < temp) {
									settleProcess.setAcctName(merchantSettleAcct.getAcctName());
									settleProcess.setAcctNo(merchantSettleAcct.getAcctNo());
									settleProcess.setSettleWay(settleWay.getCode());
									settleProcess.setSettleWayCodeX(settleWay.getCodeX());
									settleProcess.setProvCode(merchantSettleAcct.getProvinceCode());
									settleProcess.setAcctFlag(merchantSettleAcct.getTypeFlag().toString());
									settleProcess.setOtherBankFlag(merchantSettleAcct.getBankCode().equals(settleWay.getCodeX()) ? "0" : "1");
									settleProcess.setCrBankName(merchantSettleAcct.getBankFullName());
									settleProcess.setCrBankNo(merchantSettleAcct.getRelatedBankNo());

									settleDay = DateUtil.addDays(date, merchantSettleAcct.getSettleX());
									settleProcess.setSettleX(merchantSettleAcct.getSettleX());
									settleProcess.setSettleDay(settleDay);

									temp = settleWay.getSeqNo();
								}
							}
						}
					}

					// 设置状态
					if (settleDay == null) {// 如果settDay为null设为8
						settleProcess.setSettleFlag(8);
					} else {
						// 判断状态，给settleFlag赋值
						this.updateSettleFlagWhenCRB(settleDay, merchantId, settleProcess);
					}

				}
			}
		}

	}

	public void updateSettleFlagWhenCRB(Date settleDay, Integer merchantId, SettleProcess settleProcess) {
		// 当生成清算单、更新清算单、重新选择绑定结算单的资金账户时所作操作
		if (DateUtils.getCompareDate(DateUtil.toStdDateStr(settleDay))) {// 如果结算日期>今天，则结算单的状态为：7，未到结算日
			settleProcess.setSettleFlag(7);
		} else if (DateUtil.toStdDateStr(settleDay).equals(DateUtil.toStdDateStr(new Date())) && settleProcess.getSettleX() == 1) {// 如果结算日期=今天，且settleX为1，则用当前时间比较昨天最后完成订单的时间+1天
			// 查询此商户昨天所有完成订单里，finishedTime最大的。
			Date yesterday = DateUtil.addDays(-1);
			Date maxFinishedTime = saleOrderDao.selectMaxFinishedTimeByDate(merchantId, yesterday);
			if (null != maxFinishedTime) {
				Date settleTime = DateUtil.addDays(maxFinishedTime, 1);
				if (settleTime.compareTo(new Date()) < 0) {// 结算时间小于当前时间
					settleProcess.setSettleFlag(6);// 可提交结算
				} else {
					settleProcess.setSettleFlag(7);
				}
			} else {// 否则昨天没有完成的。

			}
		} else {
			settleProcess.setSettleFlag(6);// 可提交结算
		}
	}

	@Override
	public PaginatedList<SettleProcess> getSettleProcessByWaitingFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<SettleProcess> paginatedList = settleProcessDao.selectByWaitingFilter(paginatedFilter);
		//
		for (SettleProcess settleProcess : paginatedList.getRows()) {
			settleProcess.setSettleAmount(settleProcess.getSettleAmount().substring(0, settleProcess.getSettleAmount().length() - 2));
		}
		//
		return paginatedList;
	}

	@Override
	public Boolean submitSettleInfo() {
		return settleProcessDao.submitSettleInfo() > 0;
	}

	@Override
	public Boolean updateSettleInfo() {
		// 查询所有状态为7和8的结算单
		List<SettleProcess> list = settleProcessDao.selectBysettleFlag();
		//
		Integer count = 0;
		//
		if (null != list && list.size() != 0) {
			//
			for (SettleProcess settleProcess : list) {
				//
				Date noDate = saleOrderDao.selectFinishedTimeBySettleProcessId(settleProcess.getId());
				this.setAcctNoAndSettleFlag(settleProcess.getMerchantId(), noDate, settleProcess);
				//
				boolean flag = settleProcessDao.updateSettleInfo(settleProcess) > 0;
				if (flag) {
					count++;
				}
			}
		}

		return count == list.size();
	}

	@Override
	public JSON createReqDataForAbcAsSingleAndManual(MapContext requestData, UserContext userContext) {
		//
		String id = requestData.getTypedValue("id", String.class);
		PayRefundRec payRefundRec = payRefundRecDao.selectById(Long.valueOf(id));
		//
		// 1、生成退款请求对象
		RefundRequest tRequest = new RefundRequest();
		if (null != payRefundRec) {
			Date date = new Date();
			tRequest.dicRequest.put("OrderDate", DateUtil.DATE_DIR_FORMAT.format(date)); // 订单日期（必要信息）
			tRequest.dicRequest.put("OrderTime", DateUtil.STD_TIME_FORMAT.format(date)); // 订单时间（必要信息）
			// tRequest.dicRequest.put("MerRefundAccountNo", request.getParameter("txtMerRefundAccountNo")); //商户退款账号
			// tRequest.dicRequest.put("MerRefundAccountName", request.getParameter("txtMerRefundAccountName")); //商户退款名
			String no = payRefundRec.getNo();
			tRequest.dicRequest.put("OrderNo", payRefundRec.getNoForAbc()); // 原交易编号（订单编号，必要信息）
			tRequest.dicRequest.put("NewOrderNo", NoUtil.newOrderNo()); // 交易编号（生成新的订单编号，必要信息）
			tRequest.dicRequest.put("CurrencyCode", "156"); // 交易币种（人民币，必要信息）
			tRequest.dicRequest.put("TrxAmount", payRefundRec.getTotalFee()); // 退货金额 （必要信息）
			// tRequest.dicRequest.put("MerchantRemarks", request.getParameter("txtMerchantRemarks")); //附言
		}

		// 2、传送退款请求并取得退货结果
		JSON json = tRequest.postRequest();

		return json;
	}

	@Override
	public Boolean refundFinishedAsAbcpayAndSingle(JSON json, MapContext requestData, UserContext userContext) {

		// String ReturnCode = json.GetKeyValue("ReturnCode");
		// String ErrorMessage = json.GetKeyValue("ErrorMessage");
		// String orderNo = json.GetKeyValue("OrderNo");
		String newOrderNo = json.GetKeyValue("NewOrderNo");
		// String trxAmount = json.GetKeyValue("TrxAmount");
		String batchNo = json.GetKeyValue("BatchNo");
		String voucherNo = json.GetKeyValue("VoucherNo");
		// String hostDate = json.GetKeyValue("HostDate");
		// String hostTime = json.GetKeyValue("HostTime");
		String iRspRef = json.GetKeyValue("iRspRef");

		String id = requestData.getTypedValue("id", String.class);
		String refund_date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

		PayRefundRec payRefundRec = payRefundRecDao.selectById(Long.valueOf(id));
		if (null != payRefundRec) {
			// 退款、及各种退回操作(还原E卡、优惠券等信息)
			saleOrderService.restoreSaleOrderInfo(payRefundRec.getOrderId());
			// 操作、记录
			SaleOrderRecord saleOrderRecord = new SaleOrderRecord();
			saleOrderRecord.setOrderId(payRefundRec.getOrderId());
			saleOrderRecord.setAction(OrderAction.refund.toString());
			saleOrderRecord.setActorId(userContext.getUserId());
			saleOrderRecord.setActorName(userContext.getUserName());
			saleOrderRecordDao.insert(saleOrderRecord);
			// 更改订单状态为已退款
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("no", payRefundRec.getNo());
			map.put("payState", PayStateType.refunded.toString());
			saleOrderDao.updatePayStateByNo(map);
			// 更改状态
			payRefundRec.setRefundStatus(0);
			payRefundRec.setRefundTime(refund_date);
			payRefundRec.setBatchNo(batchNo);
			payRefundRec.setRefundOrderNo(newOrderNo);
			payRefundRec.setVoucherNo(voucherNo);
			payRefundRec.setiRspRef(iRspRef);

			// 4、判断是否为服务套餐票，是的话，销毁
			boolean flag = this.judgeSaleOrderSvcPackId(payRefundRec.getNo());
			if (flag) {
				saleOrderService.deleteUserSvcPackTicketByUserIdAndOrderNo(payRefundRec.getUserId(), payRefundRec.getNo());
			}
			
			// 5、 退款成功后关闭订单
			saleOrderService.updateForClosed(payRefundRec.getOrderId());

		}
		return payRefundRecDao.updateRefundStatus(payRefundRec) > 0;
	}

	@Override
	public PaginatedList<SettleProcess> getSettleProcessByWaitingLiquidation(PaginatedFilter paginatedFilter) {
		MapContext filterItems = paginatedFilter.getFilterItems();
		String merchantName = (String) filterItems.get("merchantName");

		List<SettleProcess> listSP = new ArrayList<SettleProcess>();
		
		if (null == merchantName) {
			//
			List<Integer> merchantList = saleOrderDao.selectMerchantIdsAsFinishedDate(new Date());
			for (Integer merchantIdTody : merchantList) {

				// 查询所有今天服务完成的订单，根据商户ID，放到settleprocess里。
				List<SaleOrder> saleOrderList = saleOrderDao.selectByFinishedTimeAsToday(merchantIdTody);
				//
				if (null != saleOrderList && saleOrderList.size() != 0) {
					SettleProcess settleProcess = new SettleProcess();
					settleProcess.setMerchantId(merchantIdTody);
					settleProcess.setMerchantName(saleOrderList.get(0).getMerchantName());

					// 计算结算金额
					BigDecimal settleAmount = new BigDecimal("0.00");
					for (SaleOrder saleOrder : saleOrderList) {
						settleAmount = settleAmount.add(saleOrder.getSettleAmount());
					}
					settleProcess.setSettleAmount(String.valueOf(settleAmount));
					settleProcess.setSaleOrder(saleOrderList);
					settleProcess.setSettleFlag(9);
					//
					listSP.add(settleProcess);
				}

			}
		} else {
			List<SaleOrder> saleOrderList = saleOrderDao.selectByMerchantName(merchantName, new Date());
			//
			if (null != saleOrderList && saleOrderList.size() != 0) {
				SettleProcess settleProcess = new SettleProcess();
				settleProcess.setMerchantId(saleOrderList.get(0).getMerchantId());
				settleProcess.setMerchantName(merchantName);

				// 计算结算金额
				BigDecimal settleAmount = new BigDecimal("0.00");
				for (SaleOrder saleOrder : saleOrderList) {
					settleAmount = settleAmount.add(saleOrder.getSettleAmount());
				}
				settleProcess.setSettleAmount(String.valueOf(settleAmount));
				settleProcess.setSaleOrder(saleOrderList);
				settleProcess.setSettleFlag(9);
				//
				//
				listSP.add(settleProcess);
			}
		}

		PaginatedList<SettleProcess> paginatedList = new PaginatedList<>();
		paginatedList.setPagination(paginatedFilter.getPagination());
		paginatedList.setRows(listSP);

		return paginatedList;
	}

	@Override
	public Boolean updateSettleProcessAcctInfo(MapContext requestData) {

		//1、通过条件商户ID和settleWayCode查询出商户的结算资金账户
		Integer merchantId = requestData.getTypedValue("merchantId", Integer.class);
		String settleWayCode = requestData.getTypedValue("settleWayCode", String.class);
		MerchantSettleAcct merchantSettleAcct = merchantSettleAcctDao.selectByMerchantIdAndSettleWayCode(merchantId, settleWayCode);
		//2、根据结算单ID，更新此结算单结算方式、账户名、结算账户号
		Long settleProcessId = requestData.getTypedValue("settleProcessId", Long.class);
		SettleProcess settleProcess = settleProcessDao.selectById(settleProcessId);
//		Date noDate = saleOrderDao.selectFinishedTimeBySettleProcessId(settleProcess.getId());
//		settleProcess.setSettleWay(merchantSettleAcct.getSettleWayCode());
//		settleProcess.setAcctName(merchantSettleAcct.getAcctName());
//		settleProcess.setAcctNo(merchantSettleAcct.getAcctNo());
//		// settleProcess.setSettleFlag(7);
//		this.setAcctNoAndSettleFlag(merchantId, noDate, settleProcess);
		
		// 查询当前启用的结算方式,一般情况下，当前只启用一种。
		String codeX = null;
		List<SettleWay> settleWayList = settleWayDao.selectAll(false);
		if (null != settleWayList && settleWayList.size() != 0) {
			codeX = settleWayList.get(0).getCodeX(); // TODO 如果有多个银企直联，那这个codeX可随机取
		}
		
		settleProcess.setAcctName(merchantSettleAcct.getAcctName());
		settleProcess.setAcctNo(merchantSettleAcct.getAcctNo());
		settleProcess.setSettleWay(merchantSettleAcct.getSettleWayCode());
		settleProcess.setSettleWayCodeX(codeX);
		settleProcess.setProvCode(merchantSettleAcct.getProvinceCode());
		settleProcess.setAcctFlag(merchantSettleAcct.getTypeFlag().toString());
		settleProcess.setOtherBankFlag(merchantSettleAcct.getBankCode().equals(codeX) ? "0" : "1");
		settleProcess.setCrBankName(merchantSettleAcct.getBankFullName());
		settleProcess.setCrBankNo(merchantSettleAcct.getRelatedBankNo());

		Date noDate = saleOrderDao.selectFinishedTimeBySettleProcessId(settleProcess.getId());
		Date settleDay = DateUtil.addDays(noDate, merchantSettleAcct.getSettleX());
		settleProcess.setSettleX(merchantSettleAcct.getSettleX());
		settleProcess.setSettleDay(settleDay);
		// 判断状态，给settleFlag赋值
		this.updateSettleFlagWhenCRB(settleDay, merchantId , settleProcess);

		return settleProcessDao.updateSettleInfo(settleProcess) > 0;
	}

	@Override
	public Boolean applyRefundFinishedByWechatpay(WxPayResultDto wpr, PayRefundRec payRefundRec, UserContext userContext) {
		int num = 0;
		// 如果状态不为 已退款或者退款失败
		if (payRefundRec.getRefundStatus() != 0) {
			// 更新支付退款记录中，退款请求数据
			payRefundRec.setRefundOrderNo(wpr.getOut_refund_no());
			payRefundRec.setRefundNo(wpr.getRefund_id());
			payRefundRec.setRefundFee(WechatpayCore.getMoneyF2Y(wpr.getRefund_fee()));
			payRefundRec.setRefundChannel(wpr.getRefund_channel());
			payRefundRec.setRefundStatus(2);// 退款中
			//
			Boolean flag = payRefundRecDao.updateRefundInfoForWechatPay(payRefundRec) > 0;

			// 插入销售订单 操作记录
			if (flag) {
				String action = OrderAction.refund.toString();
				SaleOrderRecord saleOrderRecord = new SaleOrderRecord();
				saleOrderRecord.setOrderId(payRefundRec.getOrderId());
				saleOrderRecord.setAction(action);
				saleOrderRecord.setActRole("财务");
				saleOrderRecord.setActorId(userContext.getUserId());
				saleOrderRecord.setActorName(userContext.getUserName());
				num = saleOrderRecordDao.insert(saleOrderRecord);
			}
		}
		return num > 0;
	}

	@Override
	public Boolean queryRefundFinishedByWechatpay(WxPayResultDto wpr, PayRefundRec payRefundRec, UserContext userContext) {
		// TODO 760
		// 判断是否处理过，如果状态不为已退款，则执行更新
		if (payRefundRec.getRefundStatus() != 0) {

			Map<String, Object> map = new HashMap<String, Object>();
			map.put("no", payRefundRec.getNo());
			if (wpr.getRefund_status_$n().equals("SUCCESS")) {// 退款成功

				// 1、根据订单号，把支付退款记录表中，退款状态改为 成功/失败。
				payRefundRec.setRefundStatus(0);
				payRefundRec.setRefundReceiveAccount(wpr.getRefund_recv_accout_$n());
				payRefundRec.setTempDay(null);
				payRefundRecDao.updateRefundStatus(payRefundRec);

				// 2、根据交易号，查到订单号，把订单支付状态改为 已退款。
				map.put("payState", PayStateType.refunded.toString());
				saleOrderDao.updatePayStateByNo(map);

				// 3、查询此订单是否使用了内部支付，是的话执行操作（还原E卡、优惠券等）
				saleOrderService.restoreSaleOrderInfo(payRefundRec.getOrderId());

				// 4、判断是否为服务套餐票，是的话，销毁
				boolean flag = this.judgeSaleOrderSvcPackId(payRefundRec.getNo());
				if (flag) {
					saleOrderService.deleteUserSvcPackTicketByUserIdAndOrderNo(payRefundRec.getUserId(), payRefundRec.getNo());
				}
				// 5、 退款成功后关闭订单
				saleOrderService.updateForClosed(payRefundRec.getOrderId());

			} else if (wpr.getRefund_status_$n().equals("PROCESSING")) {// 退款处理中

			} else if (wpr.getRefund_status_$n().equals("NOTSURE")) {// 未确定，需要商户原退款单号重新发起
				payRefundRec.setRefundStatus(3);
				payRefundRec.setTempDay(null);
				payRefundRecDao.updateRefundStatus(payRefundRec);
			} else if (wpr.getRefund_status_$n().equals("CHANGE")) {// 转入代发，商户线下或者线上转账
				payRefundRec.setRefundStatus(7);
				payRefundRec.setTempDay(null);
				payRefundRecDao.updateRefundStatus(payRefundRec);
			} else if (wpr.getRefund_status_$n().equals("FAIL")) {// 退款失败
				payRefundRec.setRefundStatus(1);
				payRefundRecDao.updateRefundStatus(payRefundRec);
				//
				map.put("payState", PayStateType.refundFail.toString());
				saleOrderDao.updatePayStateByNo(map);
			} else {// 其他

			}
		}
		return null;
	}

	@Override
	public DpRequest createReqTransferDataAsAbc(Long id, UserContext userContext) {
		DpRequest dpRequest = DpRequest.newOne();
		//
		SettleProcess settleProcess = settleProcessDao.selectById(id);
		//
		dpRequest.channel = settleProcess.getSettleWayCodeX();
		String newSwiftNo = NoUtil.newSerialNo();// 流水号
		String amt = settleProcess.getSettleAmount().substring(0, settleProcess.getSettleAmount().length() - 2);
		//
		// 判断是本行卡还是它行的
		String transcode = "";
		if (settleProcess.getOtherBankFlag().equals("0")) {
			transcode = "CFRT21";// 本行
		} else {
			transcode = "CFRT02";// 跨行
		}
		//
		ReqData reqData = ReqData.newOne();
		reqData.transCode = transcode;
		reqData.reqSeqNo = newSwiftNo;
		reqData.amount = new BigDecimal(amt);
		//
		CmpData cmpData = reqData.cmpData = CmpData.newOne();
		cmpData.crdProv = settleProcess.getProvCode();
		cmpData.crdAcctNo = settleProcess.getAcctNo();
		//
		CorpData corpData = reqData.corpData = CorpData.newOne();
		corpData.otherBankFlag = settleProcess.getOtherBankFlag();
		corpData.crdAcctName = settleProcess.getAcctName();
		corpData.crdBankName = settleProcess.getCrBankName();
		corpData.crdBankNo = settleProcess.getCrBankNo();

		// 2、更新一些数据
		// 更新settleProcess的请求流水号。
		settleProcess.setMemo(AbcpayConfig.whyUse);
		settleProcess.setReqNo(newSwiftNo);
		settleProcess.setSettleFlag(1);
		settleProcessDao.updateForReq(settleProcess);

		// 判断是否有记录，没有的话，插入记录.有的话，更新reqNo.
		SaleSettleRec saleRec = saleSettleRecDao.selecBySettleProcessId(id);
		if (null == saleRec) {
			//
			SaleSettleRec saleSettleRec = new SaleSettleRec();
			saleSettleRec.setSubject(PaySubjectType.saleOrder.name());
			saleSettleRec.setSettleProcessId(id);
			saleSettleRec.setPeerType(0);
			saleSettleRec.setPeerId(settleProcess.getMerchantId());
			saleSettleRec.setPeerName(settleProcess.getMerchantName());
			saleSettleRec.setBankCode(PayBankType.abc.name());
			saleSettleRec.setAcctNo(settleProcess.getAcctNo());
			saleSettleRec.setAcctName(settleProcess.getAcctName());
			saleSettleRec.setDirectFlag(1);
			saleSettleRec.setAmount(new BigDecimal(settleProcess.getSettleAmount()));
			//
			Integer operatorId = userContext.getUserId();
			String operatorName = userContext.getUserName();
			//
			saleSettleRec.setOperatorId(operatorId);
			saleSettleRec.setReqNo(newSwiftNo);
			saleSettleRec.setOperatorName(operatorName);
			saleSettleRec.setConfirmed(false);
			saleSettleRec.setState(1);
			saleSettleRec.setSettleDay(settleProcess.getSettleDay());
			saleSettleRecDao.insert(saleSettleRec);
			// 根据结算单ID，查找settleorder里所有相关的orderNo，更新这些结算记录ID
			List<SettleOrder> settleOrderList = settleOrderDao.selectBySettleProcessId(id);
			for (SettleOrder settleOrder : settleOrderList) {
				saleOrderDao.updateForRecId(settleOrder.getNo(), saleSettleRec.getId());
			}

		} else {
			saleRec.setReqNo(newSwiftNo);
			saleSettleRecDao.updateForReqData(saleRec);
		}
		dpRequest.reqData = reqData;
		return dpRequest;
	}

	@Override
	public Boolean sendSocket(String sendData, Long settleProcessId) {
		Socket socket = null;
		try {
			// 创建一个流套接字并将其连接到指定主机上的指定端口号
			socket = new Socket(AbcpayConfig.IP_ADDR, AbcpayConfig.PORT);

			// 读取服务器端数据
			DataInputStream input = new DataInputStream(socket.getInputStream());
			// 向服务器端发送数据
			DataOutputStream out = new DataOutputStream(socket.getOutputStream());
			//
			String sendHead = StringUtils.rightPad("1" + (sendData.getBytes("GBK").length), 7, " ");
			sendData = sendHead + sendData;
			System.out.println("客户端组装的数据是:" + sendData);

			out.write(sendData.getBytes("GBK"));
			System.out.println("========发送过去了=======");

			// 2
			byte[] bys = new byte[10240];
			int len = 0;
			String string = "";
			while ((len = input.read(bys)) != -1) {
				string = new String(bys, 0, len, "GBK");
			}

			System.out.println("服务器端返回过来的是: " + string);

			// 判断返回来的是否为空字符串
			SettleProcess settleProcess = settleProcessDao.selectById(settleProcessId);
			if (!string.trim().equals("")) {
				String substring = string.substring(7);
				Map m = XmlUtil.doXMLParse(substring);

				String cCTransCode = WechatpayCore.mapToString(m.get("CCTransCode"));// 交易代码
				// String reqSeqNo = WechatpayCore.mapToString(m.get("ReqSeqNo"));// 请求方流水号
				String respSource = WechatpayCore.mapToString(m.get("RespSource"));// 返回来源:0
				String respDate = WechatpayCore.mapToString(m.get("RespDate"));//
				String respTime = WechatpayCore.mapToString(m.get("RespTime"));//
				String respCode = WechatpayCore.mapToString(m.get("RespCode"));// 返回码：0000
				String respInfo = WechatpayCore.mapToString(m.get("RespInfo"));// 返回信息：交易成功
				String rxtInfo = WechatpayCore.mapToString(m.get("RxtInfo"));// 返回扩展信息：无

				String respSeqNo = WechatpayCore.mapToString(m.get("RespSeqNo"));// 应答流水号：空
				String fileFlag = WechatpayCore.mapToString(m.get("FileFlag"));// 数据文件标识：0

				if (!respSource.equals("0")) { // 统一失败
					// 根据请求流水号，更新数据（返回来源、返回码、返回信息、返回扩展信息、日期、时间）,状态为失败。
					settleProcess.setRespSource(respSource);
					settleProcess.setRespCode(respCode);
					settleProcess.setRespInfo(respInfo);
					settleProcess.setSettleFlag(2);
					settleProcessDao.updateForResp(settleProcess);
				} else {// 统一成功
					if (fileFlag.equals("0")) {
						// 根据请求流水号，更新数据（返回来源、返回码、返回信息、返回扩展信息、日期、时间、应答流水号）,状态为成功。
						settleProcess.setRespSource(respSource);
						settleProcess.setRespCode(respCode);
						settleProcess.setRepNo(respSeqNo);
						settleProcess.setRespInfo(respInfo);
						settleProcess.setSettleFlag(0);
						// settleProcess.setReason(reason);
						// settleProcess.setDoneTime(doneTime);
						settleProcessDao.updateForResp(settleProcess);

						// 更新结算记录的状态
						SaleSettleRec saleSettleRec = saleSettleRecDao.selecBySettleProcessId(settleProcessId);
						saleSettleRec.setState(3);
						saleSettleRecDao.updateForState(saleSettleRec);

					} else { // 则组装cme和cmp字符串，再doXMLParse。获取其中的文件名，然后下载，读取，插数据
						String cme = WechatpayCore.mapToString(m.get("Cme"));//
						String cmp = WechatpayCore.mapToString(m.get("Cmp"));//
						//
						Map m2 = XmlUtil.doXMLParse(cme + cmp);
						String recordNum = WechatpayCore.mapToString(m2.get("RecordNum"));// 记录数：0
						String fieldNum = WechatpayCore.mapToString(m2.get("FieldNum"));// 字段数：0
						String respPrvData = WechatpayCore.mapToString(m2.get("RespPrvData"));// 私有数据区
						String batchFileName = WechatpayCore.mapToString(m2.get("BatchFileName"));// 批量文件名
						// 下载，读取，插数据 插入数据时，参考上面(成功、失败)。
						String path = "d:\\Program Files (x86)\\中国农业银行\\中国农业银行银企通平台\\detail\\" + batchFileName;// TODO 路径
						String encoding = "ISO-8859-1";
						File file = new File(path);
						InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
						BufferedReader rd = new BufferedReader(read);
						String s = "";
						s = rd.readLine();
						while (null != s) {// TODO 没样例，还没写。确定上面编码格式。

						}

					}
				}
			} else {
				settleProcess.setRespSource("self");
				settleProcess.setRespCode("000000");
				settleProcess.setRespInfo("返回报文为空了");
				settleProcess.setSettleFlag(2);
				settleProcessDao.updateForResp(settleProcess);
			}

			out.close();
			input.close();

		} catch (Exception e) {
			System.out.println("客户端异常:" + e.getMessage());
			e.printStackTrace();
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
					socket = null;
					System.out.println("客户端 finally 异常:" + e.getMessage());
				}
			}
		}
		return null;
	}

	@Override
	public Boolean settleDoAsManual(SettleProcess settleProcess, UserContext userContext) {
		// 更新结算流程表里 转账结果数据
		settleProcess.setSettleFlag(0);
		settleProcess.setTempDay(null);
		settleProcess.setReason("正常");
		settleProcess.setMemo("线下打款");
		settleProcess.setReqNo("--无--");
		settleProcess.setRepNo("--无--");
		settleProcess.setBatchNo("--无--");
		settleProcess.setSettleType("1");
		settleProcess.setDoneTime(DateUtil.toStdDateTimeStr(new Date()));
		int count = settleProcessDao.updateForResp(settleProcess);
		boolean flag = false;
		if (count > 0) {
			//
			Integer operatorId = userContext.getUserId();
			String operatorName = userContext.getUserName();
			// 更新结算记录的状态
			SaleSettleRec ssr = saleSettleRecDao.selecBySettleProcessId(settleProcess.getId());
			if (null == ssr) {
				//
				SettleProcess sp = settleProcessDao.selectById(settleProcess.getId());
				//
				SaleSettleRec saleSettleRec = new SaleSettleRec();
				saleSettleRec.setSubject(PaySubjectType.saleOrder.name());
				saleSettleRec.setSettleProcessId(sp.getId());
				saleSettleRec.setPeerType(0);
				saleSettleRec.setPeerId(sp.getMerchantId());
				saleSettleRec.setPeerName(sp.getMerchantName());
				saleSettleRec.setBankCode(PayBankType.manual.name());
				saleSettleRec.setAcctNo(sp.getAcctNo());
				saleSettleRec.setAcctName(sp.getAcctName());
				saleSettleRec.setDirectFlag(1);
				saleSettleRec.setAmount(new BigDecimal(sp.getSettleAmount()));
				//
				saleSettleRec.setOperatorId(operatorId);
				saleSettleRec.setReqNo("--无--");
				saleSettleRec.setOperatorName(operatorName);
				saleSettleRec.setBillExtra(sp.getBillExtra());
				saleSettleRec.setConfirmed(false);
				saleSettleRec.setState(3);// 已结算
				saleSettleRec.setSettleDay(sp.getSettleDay());
				saleSettleRecDao.insert(saleSettleRec);
				// 根据结算单ID，查找settleorder里所有相关的orderNo，更新这些结算记录ID
				List<SettleOrder> settleOrderList = settleOrderDao.selectBySettleProcessId(settleProcess.getId());
				for (SettleOrder settleOrder : settleOrderList) {
					saleOrderDao.updateForRecId(settleOrder.getNo(), saleSettleRec.getId());
				}

			} else {
				ssr.setReqNo("--无--");
				ssr.setBillExtra(settleProcess.getBillExtra());
				ssr.setBankCode(PayBankType.manual.name());
				ssr.setOperatorId(operatorId);
				ssr.setOperatorName(operatorName);
				ssr.setState(3);// 已结算
				saleSettleRecDao.updateForReqData(ssr);
			}
			//
			flag = true;
		}

		return flag;
	}

	@Override
	public Result<RspData> sendDataToProxy(DpRequest dpRequest) {

		AppUrlPool.getInstance().addAppUrl("http://bawangbieji419.xicp.net:14936/bedirect");
		//
		HttpClientX<String> httpClient = new HttpClientX<String>(AppUrlPool.getInstance().getAppUrl(), new StringResponseHandler(true));
		//
		httpClient.setContentType(ContentTypes.APPLICATION_JSON);
		httpClient.setAccept(ContentTypes.withMimeTypeAll(ContentTypes.APPLICATION_JSON_VALUE));
		//
		Result<RspData> result = Result.newOne();
		//
		try {
			// 指定要传输data的字符串，而非data
			boolean dataAsStr = true;
			//
			Request requst = Request.newOne();
			// 设置非空即可加密
			requst.cryptKey = "";
			//
			requst.dataItem("reqInfo", JsonUtil.toJson(dpRequest));
			//
			List<HttpNameValuePair> params = requst.toHttpNameValuePairs(dataAsStr);
			// 结果字符串
			String reqResultStr = httpClient.doPostRequest("/rest/settle/data/do3", params);
			//
			System.out.println(reqResultStr);

			// 结果对象
			ResultX reqResult = JsonUtil.fromJson(reqResultStr, ResultX.class);
			// 自动解密
			reqResult.dataFromStr();
			//
			result.type = reqResult.type;
			result.message = reqResult.message;
			//
			Map<String, Object> resultData = reqResult.data;
			if (resultData != null) {
				MapContext dataMap = MapContext.from(resultData);
				//
				RspData rspData = JsonUtil.fromJson(dataMap.getTypedValue("rspData", String.class), RspData.class);
				if (rspData != null) {
					result.data = rspData;
				}
			}
		} catch (HttpException e) {
			result.type = Type.error;
			result.message = "向直联代理服务器请求失败";
		}

		return result;
	}

	@Override
	public boolean judgeSaleOrderSvcPackId(String orderNo) {
		SaleOrder saleOrder = saleOrderDao.selectByNo(orderNo);
		if (saleOrder.getSvcPackId() != null) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public PaginatedList<DistSettleRec> getDistSettleRecListAsShop(PaginatedFilter paginatedFilter) {
		PaginatedList<DistSettleRec> paginatedList = distSettleRecDao.selectByFilterP(paginatedFilter);

		// List<DistSettleRec> list = paginatedList.getRows();
		// for (DistSettleRec distSettleRec : list) {
		// List<SaleOrder> saleOrderList = saleOrderDao.selectBySettleRecId2Dist(distSettleRec.getId());
		// distSettleRec.setSaleOrder(saleOrderList);
		// }
		return paginatedList;
	}

	@Override
	public PaginatedList<SaleOrder> getOrderDeatilListByShop(PaginatedFilter paginatedFilter) {
		return saleOrderDao.selectbyFilterByShop(paginatedFilter);
	}

	@Override
	public Result<String> updateRspDataAsAbcDirect(Result<RspData> resultInfo) {
		Result<String> result = Result.newOne();
		//
		RspData rspData = resultInfo.data;

		SettleProcess settleProcess = settleProcessDao.selectByReqNo(rspData.reqSeqNo);
		// 判断返回来的是否为空字符串
		try {
			if (resultInfo.type == Type.info) {

				String cCTransCode = rspData.transCode;// 交易代码
				String reqSeqNo = rspData.reqSeqNo;// 请求方流水号
				String respSource = rspData.rspSrc;// 返回来源:0
				String respDate = DateUtil.DATE_INT_FORMAT.format(rspData.rspDate);//
				String respTime = DateUtil.TIME_INT_FORMAT.format(rspData.rspTime);//
				String respCode = rspData.rspCode;// 返回码：0000
				String respInfo = rspData.rspInfo;// 返回信息：交易成功
				String rxtInfo = rspData.rspInfoX;// 返回扩展信息：""

				String respSeqNo = rspData.rspSeqNo;// 应答流水号：空
				String fileFlag = rspData.fileFlag;// 数据文件标识：0

				String waitFlag = rspData.corpData.waitFlag;

				if (!respSource.equals("0")) { // 统一失败
					// 根据请求流水号，更新数据（返回来源、返回码、返回信息、返回扩展信息、日期、时间）,状态为失败。
					settleProcess.setRespSource(respSource);
					settleProcess.setRespCode(respCode);
					settleProcess.setRespInfo(respInfo);
					settleProcess.setSettleFlag(2);
					settleProcessDao.updateForResp(settleProcess);
				} else {// 统一成功
					if (fileFlag.equals("0")) {
						// 根据请求流水号，更新数据（返回来源、返回码、返回信息、返回扩展信息、日期、时间、应答流水号）,状态为成功。
						settleProcess.setRespSource(respSource);
						settleProcess.setRespCode(respCode);
						settleProcess.setRepNo(respSeqNo);
						settleProcess.setRespInfo(respInfo);
						settleProcess.setSettleFlag(0);
						// settleProcess.setReason(reason);
						// settleProcess.setDoneTime(doneTime);
						settleProcessDao.updateForResp(settleProcess);

						// 更新结算记录的状态
						SaleSettleRec saleSettleRec = saleSettleRecDao.selecBySettleProcessId(settleProcess.getId());
						saleSettleRec.setState(3);
						saleSettleRecDao.updateForState(saleSettleRec);

					} else { // 则组装cme和cmp字符串，再doXMLParse。获取其中的文件名，然后下载，读取，插数据
						String recordNum = rspData.cmeData.recordNum;// 记录数：0
						Integer fieldNum = rspData.cmeData.fieldNum;// 字段数：0
						String respPrvData = rspData.cmpData.rspPrivateData;// 私有数据区
						String batchFileName = rspData.cmpData.batFileName;// 批量文件名
						// 下载，读取，插数据 插入数据时，参考上面(成功、失败)。
						String path = "d:\\Program Files (x86)\\中国农业银行\\中国农业银行银企通平台\\detail\\" + batchFileName;// TODO 路径
						String encoding = "GBK";
						File file = new File(path);
						InputStreamReader read = new InputStreamReader(new FileInputStream(file), encoding);// 考虑到编码格式
						BufferedReader rd = new BufferedReader(read);
						String s = "";
						s = rd.readLine();
						while (null != s) {// TODO 没样例，还没写。确定上面编码格式。

						}

					}
				}
			} else {
				settleProcess.setRespSource("self");
				settleProcess.setRespCode("000000");
				settleProcess.setRespInfo("返回报文为空了");
				settleProcess.setSettleFlag(2);
				settleProcessDao.updateForResp(settleProcess);
				result.message = "返回报文为空了";
				result.type = Type.warn;
			}
		} catch (Exception e) {
			result.message = "接收到返回信息，但更新失败";
			result.type = Type.error;
			e.printStackTrace();
		}
		return result;
	}

}
