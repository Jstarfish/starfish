package priv.starfish.mall.dao.order;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.order.entity.UserSvcPackTicket;

@IBatisSqlTarget
public interface UserSvcPackTicketDao extends BaseDao<UserSvcPackTicket, Integer> {
	UserSvcPackTicket selectById(Integer id);
	
	UserSvcPackTicket selectByUserIdAndOrderIdAndOrderSvcId(Integer userId, Long orderId, Long orderSvcId);

	int insert(UserSvcPackTicket userSvcPackTicket);

	int update(UserSvcPackTicket userSvcPackTicket);

	int deleteById(Integer id);
	
	int deleteByUserIdAndOrderNo(Integer userId, String orderNo);
	
	int selectUsedSvcPackTicketCount(Integer userId, String orderNo);
	
	/**
	 * 获取用户未使用的套餐票数
	 * 
	 * @author 邓华锋
	 * @date 2016年2月17日 上午11:16:01
	 * 
	 * @param userId
	 * @return
	 */
	int selectUnUsedSvcPackTicketCount(Integer userId);
	
	/**
	 * 分页查询列表
	 * 
	 * @author wangdi
	 * @date 2016年1月26日 下午4:37:28
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<UserSvcPackTicket> selectByFilter(PaginatedFilter paginatedFilter);
	
	/**
	 * 查询单个
	 * 
	 * @author wangdi
	 * @date 2016年1月26日 下午4:37:52
	 * 
	 * @param filter
	 * @return
	 */
	UserSvcPackTicket selectOneByFilter(MapContext filter);
}