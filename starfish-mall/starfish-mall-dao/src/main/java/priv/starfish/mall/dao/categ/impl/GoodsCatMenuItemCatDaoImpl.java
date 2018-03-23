package priv.starfish.mall.dao.categ.impl;

import java.util.List;
import java.util.Map;


import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.categ.GoodsCatMenuItemCatDao;
import priv.starfish.mall.categ.entity.GoodsCatMenuItemCat;

@Component("goodsCatMenuItemCatDao")
public class GoodsCatMenuItemCatDaoImpl extends BaseDaoImpl<GoodsCatMenuItemCat, Integer> implements GoodsCatMenuItemCatDao {

	@Override
	public GoodsCatMenuItemCat selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public List<GoodsCatMenuItemCat> selectByMenuItemId(Integer menuItemId) {
		String sqlId = this.getNamedSqlId("selectByMenuItemId");
		return this.getSqlSession().selectList(sqlId, menuItemId);
	}

	@Override
	public GoodsCatMenuItemCat selectByMenuItemIdAndName(Integer menuItemId, String name) {
		String sqlId = this.getNamedSqlId("selectByMenuItemIdAndName");
		Map<String, Object> params = this.newParamMap();
		params.put("menuItemId", menuItemId);
		params.put("name", name);
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(GoodsCatMenuItemCat goodsCatMenuItemCat) {
		String sqlId = this.getNamedSqlId("insert");
		return this.getSqlSession().insert(sqlId, goodsCatMenuItemCat);
	}

	@Override
	public int update(GoodsCatMenuItemCat goodsCatMenuItemCat) {
		String sqlId = this.getNamedSqlId("update");
		return this.getSqlSession().update(sqlId, goodsCatMenuItemCat);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public int deleteByMenuItemId(Integer menuItemId) {
		String sqlId = this.getNamedSqlId("deleteByMenuItemId");
		return this.getSqlSession().delete(sqlId, menuItemId);
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public PaginatedList<GoodsCatMenuItemCat> selectAllByMenuItemId(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectAllByMenuItemId");
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<GoodsCatMenuItemCat> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

}
