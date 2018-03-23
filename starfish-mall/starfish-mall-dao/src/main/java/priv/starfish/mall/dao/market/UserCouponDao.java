package priv.starfish.mall.dao.market;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.market.entity.UserCoupon;

@IBatisSqlTarget
public interface UserCouponDao extends BaseDao<UserCoupon, Integer> {
	UserCoupon selectById(Integer id);

	UserCoupon selectByOrderId(Long orderId);
	
	UserCoupon selectByIdAndUserId(Integer id, Integer userId);

	int insert(UserCoupon userCoupon);

	int update(UserCoupon userCoupon);

	int deleteById(Integer id);

	PaginatedList<UserCoupon> selectByFilter(PaginatedFilter paginatedFilter);

	List<UserCoupon> selectByFilter(MapContext filter);

	int updateForDelete(UserCoupon userCoupon);

	Integer selectCountByUserId(Integer userId);

	List<UserCoupon> selectByProductId(MapContext filter);
}
