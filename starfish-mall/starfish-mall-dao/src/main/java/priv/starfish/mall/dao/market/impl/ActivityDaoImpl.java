package priv.starfish.mall.dao.market.impl;

import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.jdbc.SqlBuilder;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.dao.market.ActivityDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.market.entity.Activity;

@Component("activityDao")
public class ActivityDaoImpl extends BaseDaoImpl<Activity, Integer> implements ActivityDao {
	@Override
	public Activity selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public Activity selectByYearAndNameAndTargetFlag(Integer year, String name, Integer targetFlag) {
		String sqlId = this.getNamedSqlId("selectByYearAndNameAndTargetFlag");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("year", year);
		params.put("name", name);
		params.put("targetFlag", targetFlag);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(Activity activity) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, activity);
	}

	@Override
	public int update(Activity activity) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, activity);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PaginatedList<Activity> selectActivitys(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectActivitys");
		//
		MapContext filterItems = paginatedFilter.getFilterItems();
		//
		String name = filterItems.getTypedValue("name", String.class);
		filterItems.remove("name");
		if (StrUtil.hasText(name)) {
			name = SqlBuilder.likeStrVal(name);
			filterItems.put("name", name);
		}
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<Activity> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public int updateActivityState(Integer id, Integer status) {
		String sqlId = this.getNamedSqlId("updateActivityState");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("id", id);
		params.put("status", status);
		//
		return this.getSqlSession().update(sqlId, params);
	}
}