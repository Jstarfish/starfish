package priv.starfish.mall.dao.order;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.order.entity.SaleOrderSvc;

@IBatisSqlTarget
public interface SaleOrderSvcDao extends BaseDao<SaleOrderSvc, Long> {

	SaleOrderSvc selectById(Long id);

	int insert(SaleOrderSvc saleOrderSvc);

	int update(SaleOrderSvc saleOrderSvc);

	int deleteById(Long id);
	
	List<SaleOrderSvc> selectByOrderId(Long orderId);
}