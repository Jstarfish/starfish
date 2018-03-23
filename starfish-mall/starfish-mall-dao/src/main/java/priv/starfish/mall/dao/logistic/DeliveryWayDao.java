package priv.starfish.mall.dao.logistic;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.logistic.entity.DeliveryWay;

@IBatisSqlTarget
public interface DeliveryWayDao extends BaseDao<DeliveryWay, Integer> {
	DeliveryWay selectById(Integer id);

	DeliveryWay selectByNameAndComId(String name, Integer comId);

	int insert(DeliveryWay deliveryWay);

	int update(DeliveryWay deliveryWay);

	int deleteById(Integer id);

	/**
	 * 分页查询
	 * 
	 * @author 毛智东
	 * @date 2015年5月29日 上午11:59:57
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<DeliveryWay> selectList(PaginatedFilter paginatedFilter);
}