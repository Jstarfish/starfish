package priv.starfish.mall.dao.order;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.order.entity.SaleOrderInnerAmount;

/**
 * 销售订单内部金额
 * @author 邓华锋
 * @date 2015年11月17日 14:46:13
 *
 */
@IBatisSqlTarget
public interface SaleOrderInnerAmountDao extends BaseDao<SaleOrderInnerAmount, Long> {
	
	int insert(SaleOrderInnerAmount saleOrderInnerAmount);
	
	int deleteById(Long id);
	
	int update(SaleOrderInnerAmount saleOrderInnerAmount);
	
	SaleOrderInnerAmount selectById(Long id);
	
	/**
	 * 销售订单内部金额分页
	 * @author 邓华锋
	 * @date  2015年11月17日 02:46:13
	 * 
	 * @param paginatedFilter 
	 * 						like  extValStr
	 * @return
	 */
	PaginatedList<SaleOrderInnerAmount> selectByFilter(PaginatedFilter paginatedFilter);
	
	List<SaleOrderInnerAmount> selectByOrderId(Long orderId);
	
}