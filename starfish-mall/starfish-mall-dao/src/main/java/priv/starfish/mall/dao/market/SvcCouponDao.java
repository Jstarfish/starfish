package priv.starfish.mall.dao.market;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.market.entity.SvcCoupon;

@IBatisSqlTarget
public interface SvcCouponDao extends BaseDao<SvcCoupon, Integer> {
	SvcCoupon selectById(Integer id);

	SvcCoupon selectByName(String name);

	int insert(SvcCoupon coupon);

	int update(SvcCoupon coupon);

	int deleteById(Integer id);

	PaginatedList<SvcCoupon> selectByFilter(PaginatedFilter paginatedFilter);

	int updateForDeleted(Integer id, Boolean deleted);

	int updateForDisabled(Integer id, Boolean disabled);

	List<SvcCoupon> selectByFilter(MapContext filter);

	int selectCountByName(String name);
}
