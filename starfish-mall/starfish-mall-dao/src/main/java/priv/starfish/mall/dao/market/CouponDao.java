package priv.starfish.mall.dao.market;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.market.entity.Coupon;

@IBatisSqlTarget
public interface CouponDao extends BaseDao<Coupon, Integer> {
	Coupon selectById(Integer id);

	Coupon selectByName(String name);

	int insert(Coupon coupon);

	int update(Coupon coupon);

	int deleteById(Integer id);
	
	PaginatedList<Coupon> selectByFilter(PaginatedFilter paginatedFilter);
	
	int updateDeletedById(Integer id, Boolean deleted);
	
	int updateByIdAndState(Integer id, Boolean disabled);
	
	List<Coupon> selectByFilter(MapContext filter);
	
	int selectCountByName(String name);
}
