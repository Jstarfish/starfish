package priv.starfish.mall.dao.categ.impl;

import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.jdbc.SqlBuilder;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.dao.categ.ColorDefDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.categ.entity.ColorDef;

@Component("colorDefDao")
public class ColorDefDaoImpl extends BaseDaoImpl<ColorDef, Integer> implements ColorDefDao {
	@Override
	public ColorDef selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public ColorDef selectByName(String name) {
		String sqlId = this.getNamedSqlId("selectByName");
		//
		return this.getSqlSession().selectOne(sqlId, name);
	}

	@Override
	public int insert(ColorDef colorDef) {
		String sqlId = this.getNamedSqlId("insert");
		//
		int maxSeqNo = this.getEntityMaxSeqNo(ColorDef.class);
		colorDef.setSeqNo(maxSeqNo + 1);
		//
		return this.getSqlSession().insert(sqlId, colorDef);
	}

	@Override
	public int update(ColorDef colorDef) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, colorDef);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<ColorDef> selectColorDefs(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectColorDefs");
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
		PageList<ColorDef> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public List<ColorDef> selectByFilter(List<String> names, String name) {
		String sqlId = this.getNamedSqlId("selectByFilters");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("names", names);
		if (StrUtil.hasText(name)) {
			name = SqlBuilder.likeStrVal(name.toString());
			params.put("name", name);
		} else {
			params.put("name", "");
		}
		return this.getSqlSession().selectList(sqlId, params);
	}

}