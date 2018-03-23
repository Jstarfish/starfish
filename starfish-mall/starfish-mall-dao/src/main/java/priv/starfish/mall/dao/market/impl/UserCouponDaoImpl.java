package priv.starfish.mall.dao.market.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.dao.market.UserCouponDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.market.entity.UserCoupon;

@Component("userCouponDao")
public class UserCouponDaoImpl extends BaseDaoImpl<UserCoupon, Integer> implements UserCouponDao {
	@Override
	public UserCoupon selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public UserCoupon selectByIdAndUserId(Integer id, Integer userId) {
		String sqlId = this.getNamedSqlId("selectByIdAndUserId");
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("id", id);
		map.put("userId", userId);
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public UserCoupon selectByOrderId(Long orderId) {
		String sqlId = this.getNamedSqlId("selectByOrderId");
		//
		return this.getSqlSession().selectOne(sqlId, orderId);
	}

	@Override
	public int insert(UserCoupon userCoupon) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, userCoupon);
	}

	@Override
	public int update(UserCoupon userCoupon) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, userCoupon);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<UserCoupon> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<UserCoupon> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public int updateForDelete(UserCoupon userCoupon) {
		String sqlId = this.getNamedSqlId("updateForDelete");
		//
		return this.getSqlSession().update(sqlId, userCoupon);
	}

	@Override
	public Integer selectCountByUserId(Integer userId) {
		String sqlId = this.getNamedSqlId("selectCountByUserId");
		//
		return this.getSqlSession().selectOne(sqlId, userId);
	}

	@Override
	public List<UserCoupon> selectByProductId(MapContext filter) {
		String sqlId = this.getNamedSqlId("selectByProductId");
		//
		return this.getSqlSession().selectList(sqlId, filter);
	}

	@Override
	public List<UserCoupon> selectByFilter(MapContext filter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		//
		return this.getSqlSession().selectList(sqlId, filter);
	}

}
