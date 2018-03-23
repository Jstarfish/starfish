package priv.starfish.mall.dao.order;

import java.util.Date;
import java.util.List;
import java.util.Map;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.order.entity.SaleOrder;

@IBatisSqlTarget
public interface SaleOrderDao extends BaseDao<SaleOrder, Long> {
	
	int updateForClosed(SaleOrder saleOrder);

	SaleOrder selectById(Long id);

	/**
	 * 根据订单编号查询订单
	 * 
	 * @author wangdi
	 * @date 2015年10月24日 下午1:47:16
	 * 
	 * @param no
	 * @return
	 */
	SaleOrder selectByNo(String no);

	int insert(SaleOrder saleOrder);

	int update(SaleOrder saleOrder);

	int deleteById(Long id);

	/**
	 * 分页查询销售订单
	 * 
	 * @author wangdi
	 * @date 2015年10月14日 上午9:45:49
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<SaleOrder> selectByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 查询销售订单(不分页)
	 * 
	 * @author wangdi
	 * @date 2015年12月20日 上午9:45:49
	 * 
	 * @param filter
	 * @return
	 */
	List<SaleOrder> selectByFilterNormal(MapContext filter);

	/**
	 * 根据用户id查询订单列表
	 * 
	 * @author 李超杰
	 * @date 2015年10月14日 下午3:07:23
	 * 
	 * @param saleOrder
	 * @return
	 */
	PaginatedList<SaleOrder> selectByUserId(PaginatedFilter paginatedFilter);

	Integer selectCount(MapContext requestData);

	/**
	 * 删除订单，假删
	 * 
	 * @return
	 */
	Integer updateForDelete(SaleOrder saleOrder);

	/**
	 * 取消订单
	 * 
	 * @param id
	 * @param cancelled
	 * @return
	 */
	Integer updateForCancel(SaleOrder saleOrder);

	/**
	 * 享受完成订单
	 * 
	 * @param id
	 * @param cancelled
	 * @return
	 */
	Integer updateForFinish(SaleOrder saleOrder);

	/**
	 * 根据订单号，更新订单支付状态
	 * 
	 * @author "WJJ"
	 * @date 2015年11月7日 下午4:06:35
	 * 
	 * @param no
	 * @param payState
	 * @return
	 */
	Integer updatePayStateByNo(Map<String, Object> map);

	/**
	 * 查询当前账期内的所有订单
	 * 
	 * @author "WJJ"
	 * @date 2015年11月14日 上午1:14:38
	 * 
	 * @param d
	 * @return
	 */
	List<SaleOrder> selectByCompareSettleDayUse(Date settleDay, Date beforeSettleDay, Integer merchantId);

	/**
	 * 查询当前账期内的所有订单
	 * 
	 * @author "WJJ"
	 * @date 2015年11月14日 上午1:14:38
	 * 
	 * @param d
	 * @return
	 */
	List<SaleOrder> selectByCompareSettleDay(Date settleDay, Date beforeSettleDay);

	/**
	 * 查询当前账期内所有的商户IDS
	 * 
	 * @author "WJJ"
	 * @date 2015年11月14日 上午2:35:35
	 * 
	 * @param settleDay
	 * @param beforeSettleDay
	 * @return
	 */
	List<Integer> selectMerchantIds(Date settleDay, Date beforeSettleDay);

	/**
	 * 分页查询，某个账期内，某笔结算信息的订单详情
	 * 
	 * @author "WJJ"
	 * @date 2015年11月14日 下午5:46:04
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<SaleOrder> selectByFilterAsSettleDay(PaginatedFilter paginatedFilter);

	/**
	 * 根据settleProcess的ID，获取跟它关联的所有订单的 nos
	 * 
	 * @author "WJJ"
	 * @date 2015年11月17日 下午6:57:33
	 * 
	 * @param id
	 * @return
	 */
	List<String> selectOrderIdsByProcessId(Long processId);

	/**
	 * 代理完成订单
	 * 
	 * @author wangdi
	 * @date 2015年11月20日 下午5:34:35
	 * 
	 * @param saleOrder
	 * @return
	 */
	Integer updateForFinishAsProxy(SaleOrder saleOrder);

	/**
	 * 补充订单信息
	 * 
	 * @author wangdi
	 * @date 2015年12月20日 下午5:34:35
	 * 
	 * @param saleOrder
	 * @return
	 */
	Integer updateForAddInfo(SaleOrder saleOrder);

	/**
	 * 根据商户ID查询所有已完成、未生成结算单信息的订单。
	 * 
	 * @author "WJJ"
	 * @param finishedTime
	 * @date 2015年12月11日 下午7:05:05
	 * 
	 * @param id
	 * @return
	 */
	List<SaleOrder> selectForCreateSettleInfo(Integer merchantId, Date finishTime);

	/**
	 * 订单表中，查询此商户，最早完成的订单的日期
	 * 
	 * @author "WJJ"
	 * @date 2015年12月28日 上午12:28:08
	 * 
	 * @param merchantId
	 * @return
	 */
	Date selectMinFinishedDay(Integer merchantId);

	/**
	 * 通过id和指定日期，查询当天最后完成的订单的完成时间
	 * 
	 * @author "WJJ"
	 * @date 2016年1月10日 下午6:57:07
	 * 
	 * @param merchantId
	 * @param yesterday
	 * @return
	 */
	Date selectMaxFinishedTimeByDate(Integer merchantId, Date yesterday);

	/**
	 * 通过结算单ID，查找出相关订单的最大完成时间
	 * 
	 * @author "WJJ"
	 * @date 2016年1月10日 下午9:25:36
	 * 
	 * @param settleProcessId
	 * @return
	 */
	Date selectFinishedTimeBySettleProcessId(Long settleProcessId);

	/**
	 * 查询所有今天完成的订单
	 * 
	 * @author "WJJ"
	 * @date 2016年1月10日 下午11:35:35
	 * 
	 * @return
	 */
	List<SaleOrder> selectByFinishedTimeAsToday(Integer merchantId);

	/**
	 * 查询制定日期，有服务完成的所有商户ID
	 * 
	 * @author "WJJ"
	 * @date 2016年1月11日 上午12:26:05
	 * 
	 * @param date
	 * @return
	 */
	List<Integer> selectMerchantIdsAsFinishedDate(Date date);

	/**
	 * 通过商户ID,服务完成时间当天，查询订单集合
	 * 
	 * @author "WJJ"
	 * @date 2016年1月11日 上午1:22:49
	 * 
	 * @param merchantId
	 * @return
	 */
	List<SaleOrder> selectByMerchantName(String merchantName, Date finishedTime);

	/**
	 * 更新订单的结算记录关联ID
	 * 
	 * @author "WJJ"
	 * @date 2016年1月12日 下午1:56:22
	 * 
	 * @param no
	 * @param saleSettleRecId
	 * @return
	 */
	int updateForRecId(String no, Integer saleSettleRecId);

	/**
	 * 支付成功后，更新订单状态
	 * 
	 * @author "WJJ"
	 * @date 2016年2月17日 下午3:39:18
	 * 
	 * @param saleOrder
	 * @return
	 */
	Integer updateForPayFinished(SaleOrder saleOrder);

	PaginatedList<SaleOrder> selectByDistributorId(PaginatedFilter paginatedFilter);

	/**
	 * 根据合作店结算记录ID，查询订单
	 * 
	 * @author "WJJ"
	 * @date 2016年2月25日 下午2:04:49
	 * 
	 * @param id
	 * @return
	 */
	List<SaleOrder> selectBySettleRecId2Dist(Integer id);

	/**
	 * 商户后台，对合作店的结算记录，查询关联的订单
	 * 
	 * @author "WJJ"
	 * @date 2016年2月26日 下午5:10:45
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<SaleOrder> selectbyFilterByShop(PaginatedFilter paginatedFilter);

	/**
	 * 根据条件查询合作店订单
	 * 
	 * @author 李江
	 * @date 2016年2月25日 下午2:04:49
	 * 
	 * @param id
	 * @return
	 */
	PaginatedList<SaleOrder> selectDistShopOrderByFilter(PaginatedFilter paginatedFilter);
	
	/**
	 * 根据条件查询合作店结算订单
	 * 
	 * @author 李江
	 * @date 2016年2月25日 下午2:04:49
	 * 
	 * @param id
	 * @return
	 */
	PaginatedList<SaleOrder> selectDistShopOrderSettleByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 
	 * 查询为某个用户服务过的门店Id
	 */
	List<Integer> selectShopIdsByUserId(Integer userId);
}
