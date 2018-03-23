package priv.starfish.mall.service;

import java.util.List;
import java.util.Map;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.order.dto.ECardDto;
import priv.starfish.mall.order.entity.ECardOrder;

public interface ECardOrderService extends BaseService {

	/**
	 * 分页查询用户的e卡订单
	 * 
	 * @author wangdi
	 * @date 2015年11月10日 下午4:36:23
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	public PaginatedList<ECardOrder> getECardOrdersByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 获取E卡订单详情
	 * 
	 * @author wangdi
	 * @date 2015年11月12日 下午4:39:42
	 * 
	 * @param cardId
	 * @return
	 */
	public ECardOrder getECardOrderByOrderIdAndUserId(Integer orderId, Integer userId);

	/**
	 * 取消E卡订单
	 * 
	 * @author wangdi
	 * @date 2015年11月13日 下午5:52:19
	 * 
	 * @param orderId
	 * @param userId
	 * @return
	 */
	public Boolean updateECardOrderForCancel(Integer orderId, Integer userId);

	/**
	 * 提交订单
	 * 
	 * @author lichaojie
	 * @date 2015年11月15日
	 * 
	 * @param userContext
	 * @param eCardDto
	 * @return
	 */
	Integer createECardOrder(UserContext userContext, ECardDto eCardDto);

	public ECardOrder getECardOrderByOrderId(Integer orderId, Integer userId);

	/**
	 * 通过e卡订单号no查询e卡订单
	 * 
	 * @author "WJJ"
	 * @date 2015年11月18日 上午10:28:50
	 * 
	 * @param out_trade_no
	 * @return
	 */
	public ECardOrder getECardOrderByNo(String no);

	/**
	 * 根据订单号no，更新订单的支付状态与支付时间
	 * 
	 * @author "WJJ"
	 * @date 2015年11月18日 上午11:50:07
	 * 
	 * @param map
	 * @return
	 */
	public Boolean updateECardOrderByNo(Map<String, Object> map);

	/**
	 * 首单赠送优惠券 (支付成功调用)
	 * 
	 * @author "lichaojie"
	 * @date 2015年11月20日 下午5:53:23
	 * 
	 * @param userId
	 * @return
	 */
	public boolean existFirstOrder(Integer userId, String code);

	/**
	 * 获取e卡订单列表
	 * 
	 * @author wangdi
	 * @date 2015年12月8日 下午4:59:55
	 * 
	 * @param filter
	 * @return
	 */
	public List<ECardOrder> getECardOrders(MapContext filter);

	/**
	 * 获取e卡订单数量
	 * 
	 * @author wangdi
	 * @date 2015年12月29日 下午5:24:17
	 * 
	 * @param filter
	 * @return
	 */
	public Integer getECardOrderCount(MapContext filter);

	/**
	 * 查询是否有此code的E卡订单
	 * 
	 * @author "WJJ"
	 * @date 2016年1月6日 上午11:19:11
	 * 
	 * @param code
	 * @return
	 */
	public boolean getECardOrderCountByCode(String code);

	/**
	 * 商城后台——分页查询E卡订单列表
	 * 
	 * @author "WJJ"
	 * @date 2016年1月12日 下午2:44:38
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	public PaginatedList<ECardOrder> getByFilter(PaginatedFilter paginatedFilter);
}
