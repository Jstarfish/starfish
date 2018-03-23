package priv.starfish.mall.service;

import java.util.List;
import java.util.Map;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.cart.entity.SaleCartSvc;
import priv.starfish.mall.order.dto.OrderStateTypeCountDto;
import priv.starfish.mall.order.dto.SaleOrderInfo;
import priv.starfish.mall.order.entity.SaleOrder;
import priv.starfish.mall.order.entity.SaleOrderGoods;
import priv.starfish.mall.order.entity.SaleOrderRecord;
import priv.starfish.mall.order.entity.SaleOrderSvc;
import priv.starfish.mall.order.entity.SaleOrderWork;
import priv.starfish.mall.order.entity.UserSvcPackTicket;
import priv.starfish.mall.order.po.SaleOrderActionPo;
import priv.starfish.mall.order.po.SaleOrderPo;
import priv.starfish.mall.settle.entity.PayRefundRec;

public interface SaleOrderService extends BaseService {

	// -------------------------------- 订单操作记录 ---------------------------------------
	/**
	 * 添加订单操作记录
	 * 
	 * @author wangdi
	 * @date 2015年10月24日 下午6:21:32
	 * 
	 * @param saleOrderRecord
	 * @return
	 */
	public boolean saveSaleOrderRecord(SaleOrderRecord saleOrderRecord);

	/**
	 * 添加订单操作记录
	 * 
	 * @author "WJJ"
	 * @date 2015年11月17日 下午5:10:55
	 * 
	 * @param payRefundRec里的orderId
	 * @return
	 */
	public boolean saveSaleOrderRecord(String action, UserContext userContext, PayRefundRec payRefundRec);

	/**
	 * 分页查询销售订单列表
	 * 
	 * @author wangdi
	 * @date 2015年10月14日 上午11:40:55
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	public PaginatedList<SaleOrder> getSaleOrdersByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 查询销售订单列表(不分页)
	 * 
	 * @author wangdi
	 * @date 2015年12月20日 上午11:40:55
	 * 
	 * @param filter
	 * @return
	 */
	List<SaleOrder> getSaleOrdersByFilterNormal(MapContext filter);

	/**
	 * 分页查询销售订单列表
	 * 
	 * @author MCIUJavaDept
	 * @date 2015年10月15日 下午2:19:13
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	public PaginatedList<SaleOrder> getSaleOrdersByUserId(PaginatedFilter paginatedFilter);

	// -------------------------------- 订单工作信息 ---------------------------------------
	/**
	 * 返回订单工作信息
	 * 
	 * @author koqiui
	 * @date 2015年12月17日 上午2:24:52
	 * 
	 * @param orderId
	 * @return
	 */
	public List<SaleOrderWork> getSaleOrderWorksById(Long orderId);

	/**
	 * 保存订单工作信息（整体覆盖/替换）
	 * 
	 * @author koqiui
	 * @date 2015年12月17日 上午2:25:44
	 * 
	 * @param saleOrderWorks
	 *            为 null 或 size() == 0 时，相当于删除订单的工作信息
	 * @return
	 */
	public boolean saveSaleOrderWorks(Long orderId, List<SaleOrderWork> saleOrderWorks);

	// -------------------------------- 订单主体 ---------------------------------------
	/**
	 * 
	 * 根据订单ID获取订单信息
	 * 
	 * @author 邓华锋
	 * @date 2015年10月19日 上午10:46:45
	 * 
	 * @param id
	 *            订单ID
	 * @return
	 */
	public SaleOrder getSaleOrderById(Long id);

	/**
	 * 获取订单详情
	 * 
	 * @author wangdi
	 * @date 2016年1月27日 下午4:50:25
	 * 
	 * @param id
	 * @return
	 */
	public SaleOrder getSaleOrderDetailById(Long id);

	/**
	 * 根据订单id查询订单服务列表
	 * 
	 * @author 李超杰
	 * @date 2015年10月20日 下午1:32:05
	 * 
	 * @param orderId
	 * @return
	 */
	public List<SaleOrderSvc> getSaleOrderSvcsByOrderId(Long orderId);

	public OrderStateTypeCountDto getSaleOrderCount(MapContext requestData);

	public OrderStateTypeCountDto getSaleOrderCountByUserIdAndStateType(MapContext requestData);

	public SaleOrder getSaleOrderDetailByOrderId(Long orderId);

	/**
	 * 代理下单生成订单
	 * 
	 * @author wangdi
	 * @date 2015年10月23日 下午4:38:09
	 * 
	 * @param saleOrderContext
	 * @return
	 */
	public boolean saveSaleOrderAsProxy(SaleOrderPo saleOrderContext);

	/**
	 * 根据订单编号查询订单
	 * 
	 * @author wangdi
	 * @date 2015年10月24日 下午1:44:37
	 * 
	 * @param orderNo
	 * @return
	 */
	public SaleOrder getSaleOrderByNo(String orderNo);

	/**
	 * 提交销售订单
	 * 
	 * @author 邓华锋
	 * @date 2015年11月2日 下午3:00:07
	 * 
	 * @param saleOrder
	 * @return
	 */
	public boolean saveSaleOrder(Integer userId, SaleOrderInfo saleOrderInfo);

	/**
	 * 更新销售订单
	 * 
	 * @author wangdi
	 * @date 2015年10月24日 下午3:49:07
	 * 
	 * @param saleOrder
	 * @return
	 */
	public boolean updateSaleOrder(SaleOrder saleOrder);

	/**
	 * 验证服务完成码
	 * 
	 * @author wangdi
	 * @date 2015年10月27日 下午2:50:29
	 * 
	 * @param doneCode
	 * @param orderId
	 * @return
	 */
	public boolean checkDoneCode(String doneCode, Long orderId);

	/**
	 * 删除订单（假删）
	 * 
	 * @author lichaojie
	 * @date 2015年11月21日 下午4:48:42
	 * 
	 * @param saleOrder
	 * @param user
	 * @return
	 */
	boolean updateSaleOrderForDelete(SaleOrder saleOrder, UserContext user);

	/**
	 * 取消订单
	 * 
	 * @author lichaojie
	 * @date 2015年11月21日 下午4:48:42
	 * 
	 * @param saleOrder
	 * @param user
	 * @return
	 */
	boolean updateSaleOrderForCancel(SaleOrder saleOrder, UserContext user);

	/**
	 * 完成订单
	 * 
	 * @author lichaojie
	 * @date 2015年11月21日 下午4:48:42
	 * 
	 * @param saleOrder
	 * @param user
	 * @return
	 */
	boolean updateSaleOrderForFinish(SaleOrder saleOrder, UserContext user);

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
	public boolean updateSaleOrderPayStateByNo(Map<String, Object> map);

	/**
	 * 代理订单完成操作
	 * 
	 * @author wangdi
	 * @date 2015年11月19日 下午6:29:59
	 * 
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public boolean updateSaleOrderForFinishAsProxy(SaleOrderActionPo saleOrderActionPo);

	/**
	 * 补充订单信息
	 * 
	 * @author wangdi
	 * @date 2015年12月20日 下午6:29:59
	 * 
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public boolean updateSaleOrderForAddInfo(SaleOrderActionPo saleOrderActionPo);

	/**
	 * 取消订单
	 * 
	 * @author wangdi
	 * @date 2015年12月26日 上午10:20:56
	 * 
	 * @param saleOrderActionPo
	 * @return
	 */
	public boolean updateSaleOrderForCancelAsProxy(SaleOrderActionPo saleOrderActionPo);

	/**
	 * 还原订单使用优惠券和e卡信息
	 * 
	 * @author "WJJ"
	 * @date 2015年11月24日 下午1:19:44
	 * 
	 * @param id
	 */
	public void restoreSaleOrderInfo(Long id);

	/**
	 * 根据订单No和用户ID获取商品详情
	 * 
	 * @author 邓华锋
	 * @date 2015年12月7日 上午10:00:24
	 * 
	 * @param orderNo
	 * @param userId
	 * @return
	 */
	public List<SaleOrderGoods> getSaleOrderGoodssByOrderNoAndUserId(String orderNo, Integer userId);

	public List<SaleOrderGoods> getSaleOrderGoodsByOrderId(Long orderId);

	/**
	 * 分页查询套餐服务票
	 * 
	 * @author wangdi
	 * @date 2016年1月25日 上午11:23:24
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	public PaginatedList<UserSvcPackTicket> getUserSvcPackTicketsByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 根据用户ID和订单ID，销毁服务套餐票
	 * 
	 * @author 邓华锋
	 * @date 2016年2月1日 上午10:33:56
	 * 
	 * @param userSvcPackTicket
	 * @return
	 */
	public boolean deleteUserSvcPackTicketByUserIdAndOrderNo(Integer userId, String orderNo);

	/**
	 * 获取用户已经使用的服务套餐票数目
	 * 
	 * @author 邓华锋
	 * @date 2016年2月1日 上午11:04:08
	 * 
	 * @param userId
	 * @param orderNo
	 * @return
	 */
	public Integer getUsedSvcPackTicketCount(Integer userId, String orderNo);

	/**
	 * 更新套餐服务票
	 * 
	 * @author wangdi
	 * @date 2016年1月26日 下午3:35:10
	 * 
	 * @param userSvcPackTicket
	 * @return
	 */
	public boolean updateUserSvcPackTicket(UserSvcPackTicket userSvcPackTicket);

	/**
	 * 查询单个套餐服务票
	 * 
	 * @author wangdi
	 * @date 2016年1月26日 下午4:56:57
	 * 
	 * @param filter
	 * @return
	 */
	public UserSvcPackTicket getUserSvcPackTicketByFilter(MapContext filter);

	/**
	 * 生成服务套餐票
	 * 
	 * @author 邓华锋
	 * @date 2016年1月28日 下午2:26:50
	 * 
	 * @param saleOrder
	 */
	public void saveUserSvcPackTicket(SaleOrder saleOrder);

	/**
	 * 示例发送订单消息
	 * 
	 * @author koqiui
	 * @date 2016年2月2日 上午2:30:11
	 *
	 */
	public void sendDemoActionMessage();

	/**
	 * 获取用户未使用的套餐票数
	 * 
	 * @author 邓华锋
	 * @date 2016年2月17日 上午11:18:23
	 * 
	 * @param userId
	 * @return
	 */
	public int getUnUsedSvcPackTicketCount(Integer userId);

	/**
	 * 根据
	 * 
	 * @author Administrator
	 * @date 2016年2月23日 下午1:44:55
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	public PaginatedList<SaleOrder> getSaleOrdersByDistributorId(PaginatedFilter paginatedFilter);

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
	 * 关闭订单操作
	 * 
	 * @author 李江
	 * @date 2016年2月25日 下午2:04:49
	 * 
	 * @param orderId
	 * @return
	 */
	Boolean updateForClosed(Long orderId);

	/**
	 * 
	 * 查询为某个用户服务过的门店Id
	 */
	public List<Integer> getShopIdsByHaveServedUser(Integer userId);

	public void calcRankRate(List<SaleCartSvc> svcList, Integer userId, Integer shopId);
	
	Boolean updateSaleOrderForShopChange(SaleOrder saleOrder);
}
