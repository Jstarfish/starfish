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
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.market.UserSvcCouponDao;
import priv.starfish.mall.market.entity.UserSvcCoupon;

@Component("userSvcCouponDao")
public class UserSvcCouponDaoImpl extends BaseDaoImpl<UserSvcCoupon, Integer> implements UserSvcCouponDao {
	@Override
	public UserSvcCoupon selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public UserSvcCoupon selectByIdAndUserId(Integer id, Integer userId) {
		String sqlId = this.getNamedSqlId("selectByIdAndUserId");
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("id", id);
		map.put("userId", userId);
		return this.getSqlSession().selectOne(sqlId, map);
	}

	@Override
	public UserSvcCoupon selectByNo(String no) {
		String sqlId = this.getNamedSqlId("selectByNo");
		//
		return this.getSqlSession().selectOne(sqlId, no);
	}

	@Override
	public int insert(UserSvcCoupon userSvcCoupon) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, userSvcCoupon);
	}

	@Override
	public int update(UserSvcCoupon userSvcCoupon) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, userSvcCoupon);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<UserSvcCoupon> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<UserSvcCoupon> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public int updateForDeleted(UserSvcCoupon userCoupon) {
		String sqlId = this.getNamedSqlId("updateForDeleted");
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
	public List<UserSvcCoupon> selectBySvcIdAndUserId(MapContext filter) {
		String sqlId = this.getNamedSqlId("selectBySvcIdAndUserId");
		//
		return this.getSqlSession().selectList(sqlId, filter);
	}

	@Override
	public List<UserSvcCoupon> selectByFilter(MapContext filter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		//
		return this.getSqlSession().selectList(sqlId, filter);
	}
}
