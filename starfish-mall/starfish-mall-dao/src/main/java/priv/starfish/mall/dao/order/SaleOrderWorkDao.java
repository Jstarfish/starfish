package priv.starfish.mall.dao.order;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.order.entity.SaleOrderWork;

@IBatisSqlTarget
public interface SaleOrderWorkDao extends BaseDao<SaleOrderWork, Long> {
	SaleOrderWork selectById(Long id);

	SaleOrderWork selectByOrderIdAndWorkerId(Long orderId, Integer workerId);

	int insert(SaleOrderWork saleOrderWork);

	int update(SaleOrderWork saleOrderWork);

	int deleteById(Long id);

	/**
	 * 返回给定销售订单的工作信息
	 * 
	 * @author koqiui
	 * @date 2015年12月17日 上午2:32:08
	 * 
	 * @param orderId
	 * @return
	 */
	List<SaleOrderWork> selectByOrderId(Long orderId);

	/**
	 * 删除给定销售订单的工作信息
	 * 
	 * @author koqiui
	 * @date 2015年12月17日 上午2:36:43
	 * 
	 * @param orderId
	 * @return
	 */
	int deleteByOrderId(Long orderId);
}