package priv.starfish.mall.dao.settle;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.settle.entity.PayRefundRec;

@IBatisSqlTarget
public interface PayRefundRecDao extends BaseDao<PayRefundRec, Long> {
	PayRefundRec selectById(Long id);

	int insert(PayRefundRec payRefundRec);

	int update(PayRefundRec payRefundRec);

	int deleteById(Long id);

	/**
	 * 分页查询支付记录（对最终客户）
	 * 
	 * @author "WJJ"
	 * @date 2015年11月7日 下午5:37:52
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<PayRefundRec> selectByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 根据订单号，查询支付记录
	 * 
	 * @author "WJJ"
	 * @date 2015年11月7日 下午6:02:16
	 * 
	 * @param no
	 * @return
	 */
	PayRefundRec selectByOrderId(String orderId);

	/**
	 * 根据(支付宝)交易号，查询支付记录
	 * 
	 * @author "WJJ"
	 * @date 2015年11月9日 上午10:39:43
	 * 
	 * @param tradeNo
	 * @return
	 */
	PayRefundRec selectByTradeNo(String tradeNo);

	/**
	 * 给支付记录表，更新退款批次号、退款申请时间
	 * 
	 * @author "WJJ"
	 * @date 2015年11月7日 下午6:21:49
	 * 
	 * @param payRefundRec
	 * @return
	 */
	Integer updatePayRefundRecForRefund(PayRefundRec payRefundRec);

	/**
	 * 申请退款后，更改支付退款记录表中，退款状态为：申请退款
	 * 
	 * @author "WJJ"
	 * @date 2015年11月10日 下午6:39:44
	 * 
	 * @param payRefundRec
	 * @return
	 */
	Integer updateRefundStatus(PayRefundRec payRefundRec);

	/**
	 * 分页查询，支付记录中，退款状态为过滤条件里的记录
	 * 
	 * @author "WJJ"
	 * @date 2015年11月12日 下午2:07:51
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<PayRefundRec> selectByToRefundFilter(PaginatedFilter paginatedFilter);

	/**
	 * 分页查询，退款状态为，可退款的
	 * 
	 * @author "WJJ"
	 * @date 2015年11月12日 下午5:03:57
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<PayRefundRec> selectByCanRefundFilter(PaginatedFilter paginatedFilter);

	/**
	 * 分页查询，支付记录中，退款待审核的记录
	 * 
	 * @author "WJJ"
	 * @date 2015年11月18日 下午5:25:35
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<PayRefundRec> selectByRefundAuditRecsFilter(PaginatedFilter paginatedFilter);

	/**
	 * 根据订单号no，更改支付记录表中退款状态（退款返回结果）
	 * 
	 * @author "WJJ"
	 * @date 2015年11月18日 下午6:41:46
	 * 
	 * @param payRefundRec
	 * @return
	 */
	int updateRefundStatu(PayRefundRec payRefundRec);

	/**
	 * 根据订单编号no，查询支付记录表
	 * 
	 * @author "WJJ"
	 * @date 2015年11月19日 下午3:18:18
	 * 
	 * @param no
	 * @return
	 */
	PayRefundRec selectByOrderNo(String no);

	/**
	 * (微信支付)退款，申请成功后，更新数据
	 * 
	 * @author "WJJ"
	 * @date 2016年1月18日 下午3:31:09
	 * 
	 * @param payRefundRec
	 * @return
	 */
	int updateRefundInfoForWechatPay(PayRefundRec payRefundRec);

}