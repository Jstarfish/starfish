package priv.starfish.mall.dao.order;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.order.entity.ECardOrderItem;

@IBatisSqlTarget
public interface ECardOrderItemDao extends BaseDao<ECardOrderItem, Integer> {
	ECardOrderItem selectById(Integer id);

	int insert(ECardOrderItem eCardOrderItem);

	int update(ECardOrderItem eCardOrderItem);

	int deleteById(Integer id);
}