package priv.starfish.mall.dao.comn.impl;

import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Service;

import priv.starfish.common.jdbc.SqlBuilder;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.common.util.TypeUtil;
import priv.starfish.mall.dao.comn.ResourceDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.comn.dict.ResType;
import priv.starfish.mall.comn.entity.Resource;

@Service("resourceDao")
public class ResourceDaoImpl extends BaseDaoImpl<Resource, Integer> implements ResourceDao {
	@Override
	public Resource selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public Resource selectByTypeAndPattern(String type, String pattern) {
		String sqlId = this.getNamedSqlId("selectByTypeAndPattern");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("type", type);
		params.put("pattern", pattern);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(Resource resource) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, resource);
	}

	@Override
	public int update(Resource resource) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, resource);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public int deleteByIds(List<Integer> ids) {
		if (TypeUtil.isNullOrEmpty(ids)) {
			return 0;
		}
		//
		String sqlId = this.getNamedSqlId("deleteByIds");
		//
		return this.getSqlSession().delete(sqlId, ids);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PaginatedList<Resource> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		//
		MapContext filterItems = paginatedFilter.getFilterItems();
		String name = filterItems.getTypedValue("name", String.class);
		filterItems.remove("name");
		if (StrUtil.hasText(name)) {
			name = SqlBuilder.likeStrVal(name);
			filterItems.put("name", name);
		}
		String url = filterItems.getTypedValue("url", String.class);
		filterItems.remove("url");
		
		if (StrUtil.hasText(url)) {
			url = SqlBuilder.likeStrVal(url);
			filterItems.put("pattern", url);
		}
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<Resource> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		//
		return this.toPaginatedList(PageList);
	}

	@Override
	public List<Resource> selectByIds(List<Integer> ids) {
		if (TypeUtil.isNullOrEmpty(ids)) {
			return TypeUtil.newEmptyList(Resource.class);
		}
		String sqlId = this.getNamedSqlId("selectByIds");
		//
		return this.getSqlSession().selectList(sqlId, ids);
	}

	@Override
	public List<Resource> selectByPattern(String pattern) {
		String sqlId = this.getNamedSqlId("selectByPattern");
		//
		return this.getSqlSession().selectList(sqlId, pattern);
	}

	@Override
	public List<Resource> selectByPermId(int permId) {
		String sqlId = this.getNamedSqlId("selectByPermId");
		//
		return this.getSqlSession().selectList(sqlId, permId);
	}

	@Override
	public List<Resource> selectByPermIds(List<Integer> permIds) {
		if (TypeUtil.isNullOrEmpty(permIds)) {
			return TypeUtil.newEmptyList(Resource.class);
		}
		String sqlId = this.getNamedSqlId("selectByPermIds");
		//
		return this.getSqlSession().selectList(sqlId, permIds);
	}

	@Override
	public List<Integer> selectIdsByPermIds(List<Integer> permIds) {
		if (TypeUtil.isNullOrEmpty(permIds)) {
			return TypeUtil.newEmptyList(Integer.class);
		}
		String sqlId = this.getNamedSqlId("selectIdsByPermIds");
		//
		return this.getSqlSession().selectList(sqlId, permIds);
	}

	@Override
	public List<Resource> selectUrls(boolean protectedOnly) {
		String sqlId = this.getNamedSqlId("selectUrls");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("type", ResType.url);
		params.put("protectedOnly", protectedOnly);
		//
		return this.getSqlSession().selectList(sqlId, params);
	}

	@Override
	public List<Resource> selectByScope(AuthScope scope) {
		String sqlId = this.getNamedSqlId("selectByScope");
		//
		return this.getSqlSession().selectList(sqlId, scope);
	}

}
