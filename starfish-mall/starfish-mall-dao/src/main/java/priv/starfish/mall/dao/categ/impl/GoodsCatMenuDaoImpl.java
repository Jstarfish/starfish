package priv.starfish.mall.dao.categ.impl;

import java.util.List;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.jdbc.SqlBuilder;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.dao.categ.GoodsCatMenuDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.categ.entity.GoodsCatMenu;

@Component("goodsCatMenuDao")
public class GoodsCatMenuDaoImpl extends BaseDaoImpl<GoodsCatMenu, Integer> implements GoodsCatMenuDao {

	@Override
	public GoodsCatMenu selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public GoodsCatMenu selectByName(String name) {
		String sqlId = this.getNamedSqlId("selectByName");
		return this.getSqlSession().selectOne(sqlId, name);
	}

	@Override
	public List<GoodsCatMenu> selectFirstLevel() {
		String sqlId = this.getNamedSqlId("selectFirstLevel");
		return this.getSqlSession().selectList(sqlId);
	}

	@Override
	public List<GoodsCatMenu> selectSecondLevelByPId(Integer parentId) {
		String sqlId = this.getNamedSqlId("selectSecondLevelByPId");
		return this.getSqlSession().selectList(sqlId, parentId);
	}
	
	@Override
	public List<GoodsCatMenu> selectByDefaulted(Boolean defaulted) {
		String sqlId = this.getNamedSqlId("selectByDefaulted");
		return this.getSqlSession().selectList(sqlId, defaulted);
	}

	@Override
	public int insert(GoodsCatMenu goodsCatMenu) {
		String sqlId = this.getNamedSqlId("insert");
		return this.getSqlSession().insert(sqlId, goodsCatMenu);
	}

	@Override
	public int update(GoodsCatMenu goodsCatMenu) {
		String sqlId = this.getNamedSqlId("update");
		return this.getSqlSession().update(sqlId, goodsCatMenu);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		return this.getSqlSession().delete(sqlId, id);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PaginatedList<GoodsCatMenu> selectGoodsCatMenus(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectGoodsCatMenus");
		// 过滤like参数
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
		PageList<GoodsCatMenu> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

}
