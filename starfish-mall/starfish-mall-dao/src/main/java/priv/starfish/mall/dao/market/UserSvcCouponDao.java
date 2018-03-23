package priv.starfish.mall.dao.market;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.market.entity.UserSvcCoupon;

@IBatisSqlTarget
public interface UserSvcCouponDao extends BaseDao<UserSvcCoupon, Integer> {
	UserSvcCoupon selectById(Integer id);
	
	UserSvcCoupon selectByIdAndUserId(Integer id, Integer userId);
	
	UserSvcCoupon selectByNo(String no);

	int insert(UserSvcCoupon userSvcCoupon);

	int update(UserSvcCoupon userSvcCoupon);

	int deleteById(Integer id);
	
	PaginatedList<UserSvcCoupon> selectByFilter(PaginatedFilter paginatedFilter);

	List<UserSvcCoupon> selectByFilter(MapContext filter);

	int updateForDeleted(UserSvcCoupon userSvcCoupon);

	Integer selectCountByUserId(Integer userId);

	List<UserSvcCoupon> selectBySvcIdAndUserId(MapContext filter);
}