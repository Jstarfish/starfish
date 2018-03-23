package priv.starfish.mall.dao.market.impl;

import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.market.CouponDao;
import priv.starfish.mall.market.entity.Coupon;

@Component("couponDao")
public class CouponDaoImpl extends BaseDaoImpl<Coupon, Integer> implements CouponDao {
	@Override
	public Coupon selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public Coupon selectByName(String name) {
		String sqlId = this.getNamedSqlId("selectByName");
		//
		return this.getSqlSession().selectOne(sqlId, name);
	}

	@Override
	public int insert(Coupon coupon) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, coupon);
	}

	@Override
	public int update(Coupon coupon) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, coupon);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<Coupon> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<Coupon> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public int updateDeletedById(Integer id, Boolean deleted) {
		String sqlId = this.getNamedSqlId("updateDeletedById");
		Map<String, Object> params = this.newParamMap();
		params.put("id", id);
		params.put("deleted", deleted);
		return this.getSqlSession().update(sqlId, params);
	}

	@Override
	public int updateByIdAndState(Integer id, Boolean disabled) {
		String sqlId = this.getNamedSqlId("updateByIdAndState");
		Map<String, Object> params = this.newParamMap();
		params.put("id", id);
		params.put("disabled", disabled);
		return this.getSqlSession().update(sqlId, params);
	}

	@Override
	public List<Coupon> selectByFilter(MapContext filter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		//
		return this.getSqlSession().selectList(sqlId, filter);
	}

	@Override
	public int selectCountByName(String name) {
		String sqlId = this.getNamedSqlId("selectCountByName");
		//
		return this.getSqlSession().selectOne(sqlId, name);
	}

}
