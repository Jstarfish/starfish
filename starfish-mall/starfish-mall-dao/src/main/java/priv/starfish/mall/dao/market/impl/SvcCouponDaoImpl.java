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
import priv.starfish.mall.dao.market.SvcCouponDao;
import priv.starfish.mall.market.entity.SvcCoupon;

@Component("svcCouponDao")
public class SvcCouponDaoImpl extends BaseDaoImpl<SvcCoupon, Integer> implements SvcCouponDao {
	@Override
	public SvcCoupon selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public SvcCoupon selectByName(String name) {
		String sqlId = this.getNamedSqlId("selectByName");
		//
		return this.getSqlSession().selectOne(sqlId, name);
	}

	@Override
	public int insert(SvcCoupon coupon) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, coupon);
	}

	@Override
	public int update(SvcCoupon coupon) {
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
	public PaginatedList<SvcCoupon> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<SvcCoupon> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public int updateForDeleted(Integer id, Boolean deleted) {
		String sqlId = this.getNamedSqlId("updateForDeleted");
		Map<String, Object> params = this.newParamMap();
		params.put("id", id);
		params.put("deleted", deleted);
		return this.getSqlSession().update(sqlId, params);
	}

	@Override
	public int updateForDisabled(Integer id, Boolean disabled) {
		String sqlId = this.getNamedSqlId("updateForDisabled");
		Map<String, Object> params = this.newParamMap();
		params.put("id", id);
		params.put("disabled", disabled);
		return this.getSqlSession().update(sqlId, params);
	}

	@Override
	public List<SvcCoupon> selectByFilter(MapContext filter) {
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
