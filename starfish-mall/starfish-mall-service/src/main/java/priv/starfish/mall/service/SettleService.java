package priv.starfish.mall.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.abc.trustpay.client.JSON;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.model.Result;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.order.entity.ECardOrderRecord;
import priv.starfish.mall.order.entity.SaleOrder;
import priv.starfish.mall.settle.dto.WxPayResultDto;
import priv.starfish.mall.settle.entity.DistSettleRec;
import priv.starfish.mall.settle.entity.ECardPayRec;
import priv.starfish.mall.settle.entity.PayRefundRec;
import priv.starfish.mall.settle.entity.SaleSettleRec;
import priv.starfish.mall.settle.entity.SettleProcess;
import priv.starfish.mall.settle.entity.SettleRec;
import priv.starfish.mall.settle.entity.SettleWay;
import priv.starfish.mall.xpay.channel.ebdirect.bean.DpRequest;
import priv.starfish.mall.xpay.channel.ebdirect.bean.RspData;

public interface SettleService extends BaseService {

	// TODO------------------------支付宝------------------------------

	/**
	 * 支付宝--生成请求批量退款数据
	 * 
	 * @author "WJJ"
	 * @date 2015年12月16日 上午10:48:54
	 * 
	 * @param ids
	 * @return
	 */
	Map<String, String> createReqRefundData(String ids);

	/**
	 * 支付宝--批量退款后，一系列操作
	 * 
	 * @author "WJJ"
	 * @date 2015年12月16日 上午10:51:59
	 * 
	 * @param result_details
	 */
	void refundFinishedOperation(String result_details, String batch_no);

	/**
	 * 支付宝--后台人员点击：生成结算单信息
	 * 
	 * @author "WJJ"
	 * @date 2015年12月16日 下午1:42:40
	 * 
	 * @return
	 * @throws Exception
	 */
	Result<String> createSettleInfoByAlipay() throws Exception;

	/**
	 * 支付宝--生成请求批量转账数据
	 * 
	 * @author "WJJ"
	 * @date 2015年12月16日 上午10:02:26
	 * 
	 * @return
	 */
	Map<String, String> createReqTransferData(UserContext userContext, String ids);

	/**
	 * 支付宝--批量转账后，一系列操作
	 * 
	 * @author "WJJ"
	 * @date 2015年12月16日 上午10:41:45
	 * 
	 * @param success_details
	 * @param fail_details
	 */
	void transferFinishedOperation(String success_details, String fail_details);

	// TODO------------------------E卡退款操作------------------------------

	Boolean refundDoForECard(MapContext requestData, UserContext userContext);

	// TODO------------------------支付记录------------------------------
	/**
	 * 插入支付记录
	 * 
	 * @author "WJJ"
	 * @date 2015年11月7日 下午4:55:19
	 * 
	 * @param payRefundRec
	 * @return
	 */
	Boolean savePayRefundRec(PayRefundRec payRefundRec);

	/**
	 * 给支付记录表，更新退款批次号、退款申请时间
	 * 
	 * @author "WJJ"
	 * @date 2015年11月7日 下午6:21:49
	 * 
	 * @param payRefundRec
	 * @return
	 */
	Boolean updatePayRefundRecForRefund(PayRefundRec payRefundRec);

	/**
	 * 根据订单号，查询支付记录
	 * 
	 * @author "WJJ"
	 * @date 2015年11月7日 下午6:01:19
	 * 
	 * @return
	 */
	PayRefundRec getSalePayRecByOrderId(String orderId);

	/**
	 * 根据(支付宝)交易号，查询支付记录
	 * 
	 * @author "WJJ"
	 * @date 2015年11月9日 上午10:38:13
	 * 
	 * @param tradeNo
	 * @return
	 */
	PayRefundRec getSalePayRecByTradeNo(String tradeNo);

	/**
	 * 根据订单编号no，查询支付记录
	 * 
	 * @author "WJJ"
	 * @date 2015年11月19日 下午3:15:26
	 * 
	 * @param out_trade_no
	 * @return
	 */
	PayRefundRec getSalePayRecByOrderNo(String out_trade_no);

	/**
	 * 根据id，查询支付记录
	 * 
	 * @author "WJJ"
	 * @date 2015年11月11日 下午10:47:32
	 * 
	 * @return
	 */
	PayRefundRec getSalePayRecById(Long id);

	/**
	 * 分页查询支付记录（对最终客户）
	 * 
	 * @author "WJJ"
	 * @date 2015年11月7日 下午5:35:25
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<PayRefundRec> getSalePayRecsByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 申请退款后，更改支付退款记录表中，退款状态为：申请退款
	 * 
	 * @author "WJJ"
	 * @date 2015年11月10日 下午6:37:41
	 * 
	 * @param payRefundRec
	 * @return
	 */
	Boolean updateRefundStatus(PayRefundRec payRefundRec);

	/**
	 * 提交退款操作后，更改状态为:refunding
	 * 
	 * @author "WJJ"
	 * @date 2015年11月13日 下午1:48:00
	 * 
	 * @param requestData
	 * @return
	 */
	Boolean updateRefundStatusToRefunding(UserContext userContext, MapContext requestData);

	/**
	 * 提交取消退款操作后，更改状态为：4，拒绝退款
	 * 
	 * @author "WJJ"
	 * @date 2015年11月21日 下午5:35:04
	 * 
	 * @param userContext
	 * @param requestData
	 * @return
	 */
	Boolean updateRefundStatusToRefuseRefund(UserContext userContext, MapContext requestData);

	/**
	 * 分页查询，支付记录中，退款状态为当前过滤条件里的状态
	 * 
	 * @author "WJJ"
	 * @date 2015年11月12日 下午2:05:50
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<PayRefundRec> getToRefundAuditRecs(PaginatedFilter paginatedFilter);

	/**
	 * 分页查询，支付记录中，退款待审核的记录
	 * 
	 * @author "WJJ"
	 * @date 2015年11月18日 下午5:22:51
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<PayRefundRec> getRefundAuditRecs(PaginatedFilter paginatedFilter);

	/**
	 * 分页查询，退款状态为，可退款的
	 * 
	 * @author "WJJ"
	 * @date 2015年11月12日 下午5:02:57
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<PayRefundRec> getCanRefundRecsByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 根据订单号no查询e卡支付记录
	 * 
	 * @author "WJJ"
	 * @date 2015年11月18日 上午11:06:39
	 * 
	 * @param out_trade_no
	 * @return
	 */
	ECardPayRec getECardPayRecByNo(String out_trade_no);

	/**
	 * 分页查询E卡支付记录
	 * 
	 * @author "WJJ"
	 * @date 2015年11月21日 下午5:04:55
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<ECardPayRec> getECardPayRecsByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 只用E卡支付完成后
	 * 
	 * @author "WJJ"
	 * @date 2015年11月24日 下午3:25:37
	 * 
	 */
	public void payFinished(SaleOrder saleOrder);

	// TODO------------------------结算流程------------------------------
	/**
	 * 查询是否已经有此账期的结算单
	 * 
	 * @author "WJJ"
	 * @date 2015年11月14日 上午1:34:39
	 * 
	 * @param settleDay
	 * @return
	 */
	Boolean getSettleProcessBySettleDay(Date settleDay);

	/**
	 * 生成本账期结算单
	 * 
	 * @author "WJJ"
	 * @date 2015年11月13日 下午11:56:59
	 * 
	 * @return
	 * @throws ParseException
	 */
	void saveSettleInfo(Date settleDay, Date beforeSettleDay);

	List<SettleWay> getSettleWays(boolean includeDisabled);

	/**
	 * 根据id获取结算流程表中数据
	 * 
	 * @author "WJJ"
	 * @date 2015年11月9日 下午3:00:21
	 * 
	 * @param id
	 * @return
	 */
	SettleProcess getSettleProcessById(Long id);

	/**
	 * 根据请求流水号获取结算流程表中数据
	 * 
	 * @author "WJJ"
	 * @date 2015年11月9日 下午3:00:21
	 * 
	 * @return
	 */
	SettleProcess getSettleProcessByReqNo(String reqSerialNumber);

	/**
	 * 商城后台--分页查询，结算流程表 （即待结算的信息）
	 * 
	 * @author "WJJ"
	 * @date 2015年11月12日 上午12:07:19
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<SettleProcess> getSettleProcessByFilterAsMall(PaginatedFilter paginatedFilter);

	/**
	 * 商户后台--分页查询，结算流程表 （即待结算的信息）
	 * 
	 * @author "WJJ"
	 * @date 2015年11月12日 上午12:07:19
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<SettleProcess> getSettleProcessByFilterAsMerch(PaginatedFilter paginatedFilter);

	/**
	 * 请求转账时，更新 结算流程表 ：请求批次号、请求流水号
	 * 
	 * @author "WJJ"
	 * @date 2015年11月9日 下午3:13:07
	 * 
	 * @param settleProcess
	 * @return
	 */
	Boolean updateSettleProcessForReq(SettleProcess settleProcess);

	/**
	 * 转账完成后，更新 结算流程表 ：（支付宝）流水号、完成时间、转账标识。
	 * 
	 * @author "WJJ"
	 * @date 2015年11月9日 下午3:13:07
	 * 
	 * @param settleProcess
	 * @return
	 */
	Boolean updateSettleProcessForResp(SettleProcess settleProcess);

	/**
	 * 后台人员提交结算申请后，更新settleFlag为 settling
	 * 
	 * @author "WJJ"
	 * @date 2015年11月13日 下午3:39:53
	 * 
	 * @param requestData
	 * @return
	 */
	Boolean updateSettleProcessDoing(UserContext userContext, MapContext requestData);

	// TODO------------------------结算记录------------------------------
	/**
	 * 分页查询结算记录（对商户）
	 * 
	 * @author "WJJ"
	 * @date 2015年10月19日 下午7:43:49
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<SettleRec> getSettleRecsByFilter(PaginatedFilter paginatedFilter);

	// TODO------------------------销售结算记录------------------------------

	/**
	 * 分页查询销售结算记录（对最终客户）
	 * 
	 * @author "WJJ"
	 * @date 2015年10月19日 下午7:43:49
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<SaleSettleRec> getSaleSettleRecsByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 插入 销售结算记录
	 * 
	 * @author "WJJ"
	 * @date 2015年11月11日 下午11:49:33
	 * 
	 * @param saleSettleRec
	 * @return
	 */
	Boolean saveSaleSettleRec(SaleSettleRec saleSettleRec);

	/**
	 * 根据请求转账流水号，查询转账记录
	 * 
	 * @author "WJJ"
	 * @date 2015年11月24日 上午11:41:35
	 * 
	 * @param reqNo
	 * @return
	 */
	SaleSettleRec getSaleSettleRecByReqNo(String reqNo);

	/**
	 * 更新状态state
	 * 
	 * @author "WJJ"
	 * @date 2015年11月24日 上午11:49:16
	 * 
	 * @param saleSettleRec
	 * @return
	 */
	Boolean updateSaleSettleRecState(SaleSettleRec saleSettleRec);

	/**
	 * 分页查询销售结算流程记录
	 * 
	 * @author "WJJ"
	 * @date 2015年11月14日 下午12:08:42
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<SettleProcess> getSettleProcessByFilterAsSuccess(PaginatedFilter paginatedFilter);

	/**
	 * 分页查询，某个账期内，某笔结算信息的订单详情
	 * 
	 * @author "WJJ"
	 * @date 2015年11月14日 下午5:43:43
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<SaleOrder> getOrderDeatilListBySettleDay(PaginatedFilter paginatedFilter);

	/**
	 * 插入e卡支付记录
	 * 
	 * @author "WJJ"
	 * @date 2015年11月18日 下午12:05:11
	 * 
	 * @param eCardPayRec
	 * @return
	 */
	Boolean saveECardPayRec(ECardPayRec eCardPayRec);

	/**
	 * 插入e卡订单记录 动作
	 * 
	 * @author "WJJ"
	 * @date 2015年11月18日 下午12:12:36
	 * 
	 * @param eCardOrderRecord
	 * @return
	 */
	Boolean saveECardOrderRecord(ECardOrderRecord eCardOrderRecord);

	/**
	 * 根据订单号，更改支付记录退款状态
	 * 
	 * @author "WJJ"
	 * @date 2015年11月18日 下午6:40:02
	 * 
	 * @param payRefundRec
	 * @return
	 */
	Boolean updatePayRefundRecByNo(PayRefundRec payRefundRec);

	/**
	 * 商户后台，对应自己商户的结算记录
	 * 
	 * @author "WJJ"
	 * @date 2015年11月26日 下午3:24:40
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<SaleSettleRec> getSettleListAsMerch(PaginatedFilter paginatedFilter);

	/**
	 * 商户后台，对自己商户结算信息的操作
	 * 
	 * @author "WJJ"
	 * @date 2015年11月26日 下午4:27:53
	 * 
	 * @param settleProcess
	 * @return
	 */
	Boolean updateSettleProcessSettleFlag(SettleProcess settleProcess);

	/**
	 * 商户后台，人员查看资金统计
	 * 
	 * @author "WJJ"
	 * @date 2015年11月26日 下午5:56:58
	 * 
	 * @return
	 */
	List<SaleSettleRec> countCapitalByMerch(Map<String, Object> params);

	/**
	 * 后台人员，点击按钮：生成本期结算单
	 * 
	 * @author "WJJ"
	 * @date 2015年12月26日 下午3:54:53
	 * 
	 * @return
	 */
	void createSettleInfo();

	/**
	 * 商城后台，待结算页面：分页查询状态为6、7的。
	 * 
	 * @author "WJJ"
	 * @date 2015年12月28日 上午1:38:18
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<SettleProcess> getSettleProcessByWaitingFilter(PaginatedFilter paginatedFilter);

	/**
	 * 支付宝——后台人员，点击按钮：提交结算单数据
	 * 
	 * @author "WJJ"
	 * @date 2015年12月28日 上午2:12:48
	 * 
	 * @return
	 */
	Boolean submitSettleInfo();

	/**
	 * 后台人员，点击按钮：更新清算单 1、比较settleDay与today，更改7为6 2、把结算账户为空的，查找商户结算账户，有的话，赋值
	 * 
	 * @author "WJJ"
	 * @date 2015年12月28日 上午2:32:33
	 * 
	 * @return
	 */
	Boolean updateSettleInfo();

	/**
	 * 农行——后台人员，退款页面，点击按钮：（农行）单笔退款时，组装单笔退款的数据
	 * 
	 * @author "WJJ"
	 * @date 2016年1月8日 上午10:45:17
	 * 
	 * @param requestData
	 * @param userContext
	 * @return
	 */
	JSON createReqDataForAbcAsSingleAndManual(MapContext requestData, UserContext userContext);

	/**
	 * 农行——单笔退款成功后，一系列操作
	 * 
	 * @author "WJJ"
	 * @date 2016年1月8日 上午11:38:32
	 * 
	 * @param json
	 * @param userContext
	 * @return
	 */
	Boolean refundFinishedAsAbcpayAndSingle(JSON json, MapContext requestData, UserContext userContext);

	/**
	 * 条件查询未清算的交易
	 * 
	 * @author "WJJ"
	 * @date 2016年1月10日 下午11:16:15
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<SettleProcess> getSettleProcessByWaitingLiquidation(PaginatedFilter paginatedFilter);

	/**
	 * 1、通过条件商户ID和settleWayCode查询出商户的结算资金账户。 2、根据结算单ID，更新此结算单结算方式、账户名、结算账户号
	 * 
	 * @author "WJJ"
	 * @date 2016年1月11日 下午12:05:57
	 * 
	 * @param requestData
	 * @return
	 */
	Boolean updateSettleProcessAcctInfo(MapContext requestData);

	/**
	 * 微信支付——申请退款成功后，一系列操作
	 * 
	 * @author "WJJ"
	 * @date 2016年1月18日 下午3:10:54
	 * 
	 * @param wpr
	 * @return
	 */
	Boolean applyRefundFinishedByWechatpay(WxPayResultDto wpr, PayRefundRec payRefundRec, UserContext userContext);

	/**
	 * 微信支付——退款查询成功后，一系列操作
	 * 
	 * @author "WJJ"
	 * @date 2016年1月18日 下午4:36:19
	 * 
	 * @param wpr
	 * @param payRefundRec
	 * @param userContext
	 * @return
	 */
	Boolean queryRefundFinishedByWechatpay(WxPayResultDto wpr, PayRefundRec payRefundRec, UserContext userContext);

	/**
	 * 组装农行银企直联，请求报文数据
	 * 
	 * @author "WJJ"
	 * @date 2016年1月26日 下午5:05:20
	 * 
	 * @return
	 */
	DpRequest createReqTransferDataAsAbc(Long settleProcessId, UserContext userContext);

	/**
	 * 向银企通平台发送转账请求
	 * 
	 * @author "WJJ"
	 * @date 2016年1月26日 下午6:04:06
	 * 
	 * @param reqData
	 * @return
	 */
	Boolean sendSocket(String reqData, Long settleProcessId);

	/**
	 * 后台——操作人员对结算单，线下打款，线上点击手动结算
	 * 
	 * @author "WJJ"
	 * @date 2016年1月30日 下午7:05:43
	 * 
	 * @param settleProcess
	 * @param userContext
	 * @return
	 */
	Boolean settleDoAsManual(SettleProcess settleProcess, UserContext userContext);

	/**
	 * 向代理服务器发送请求交易报文
	 * 
	 * @author "WJJ"
	 * @date 2016年2月3日 下午4:21:34
	 * 
	 * @return
	 */
	Result<RspData> sendDataToProxy(DpRequest dpRequest);

	/**
	 * 判断订单是否为服务套餐票
	 * 
	 * @author "WJJ"
	 * @date 2016年2月16日 下午11:21:07
	 * 
	 * @return
	 */
	boolean judgeSaleOrderSvcPackId(String orderNo);

	/**
	 * 商户后台，分页查询对合作店的结算记录
	 * 
	 * @author "WJJ"
	 * @date 2016年2月25日 上午11:26:43
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<DistSettleRec> getDistSettleRecListAsShop(PaginatedFilter paginatedFilter);

	/**
	 * 商户后台，对合作店的结算记录，查询关联的订单
	 * 
	 * @author "WJJ"
	 * @date 2016年2月26日 下午5:07:37
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<SaleOrder> getOrderDeatilListByShop(PaginatedFilter paginatedFilter);

	/**
	 * 根据银企直联返回报文，更新结算单相关信息
	 * 
	 * @author "WJJ"
	 * @date 2016年2月27日 下午6:25:42
	 * 
	 * @return
	 */
	Result<String> updateRspDataAsAbcDirect(Result<RspData> resultInfo);

}
