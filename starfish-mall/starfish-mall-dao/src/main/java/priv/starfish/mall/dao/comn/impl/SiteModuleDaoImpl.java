package priv.starfish.mall.dao.comn.impl;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;
import priv.starfish.common.jdbc.SqlBuilder;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.common.util.TypeUtil;
import priv.starfish.mall.dao.comn.SiteModuleDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.comn.entity.SiteModule;

import java.util.List;
import java.util.Map;

@Component("siteModuleDao")
public class SiteModuleDaoImpl extends BaseDaoImpl<SiteModule, Integer> implements SiteModuleDao {
	@Override
	public SiteModule selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(SiteModule siteModule) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, siteModule);
	}

	@Override
	public int update(SiteModule siteModule) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, siteModule);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PaginatedList<SiteModule> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		//
		MapContext filterItems = paginatedFilter.getFilterItems();

		String name = filterItems.getTypedValue("name", String.class);
		filterItems.remove("name");
		if (StrUtil.hasText(name)) {
			name = SqlBuilder.likeStrVal(name);
			filterItems.put("name", name);
		}
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<SiteModule> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		//
		return this.toPaginatedList(PageList);
	}

	@Override
	public List<SiteModule> selectByScope(AuthScope scope) {
		String sqlId = this.getNamedSqlId("selectByScope");
		//
		return this.getSqlSession().selectList(sqlId, scope);
	}

	@Override
	public List<SiteModule> selectByIds(List<Integer> ids) {
		if (TypeUtil.isNullOrEmpty(ids)) {
			return TypeUtil.newEmptyList(SiteModule.class);
		}
		String sqlId = this.getNamedSqlId("selectByIds");
		//
		return this.getSqlSession().selectList(sqlId, ids);
	}

	@Override
	public SiteModule selectByNameAndScope(String name, AuthScope scope) {
		String sqlId = this.getNamedSqlId("selectByNameAndScope");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("name", name);
		params.put("scope", scope.toString());
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

}