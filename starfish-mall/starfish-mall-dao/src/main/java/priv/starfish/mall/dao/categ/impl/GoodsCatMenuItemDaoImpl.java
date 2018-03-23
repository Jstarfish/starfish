package priv.starfish.mall.dao.categ.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.categ.GoodsCatMenuItemDao;
import priv.starfish.mall.categ.entity.GoodsCatMenuItem;

@Component("goodsCatMenuItemDao")
public class GoodsCatMenuItemDaoImpl extends BaseDaoImpl<GoodsCatMenuItem, Integer> implements GoodsCatMenuItemDao {

	@Override
	public GoodsCatMenuItem selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public GoodsCatMenuItem selectByMenuIdAndLevelAndName(Integer menuId, Integer level, String name) {
		String sqlId = this.getNamedSqlId("selectByMenuIdAndLevelAndName");
		Map<String, Object> params = this.newParamMap();
		params.put("menuId", menuId);
		params.put("level", level);
		params.put("name", name);
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public List<GoodsCatMenuItem> selectByMenuIdAndLevel(Integer menuId, Integer level) {
		String sqlId = this.getNamedSqlId("selectByMenuIdAndLevel");
		Map<String, Object> params = this.newParamMap();
		params.put("menuId", menuId);
		params.put("level", level);
		return this.getSqlSession().selectList(sqlId, params);
	}

	@Override
	public List<GoodsCatMenuItem> selectByPId(Integer pId) {
		String sqlId = this.getNamedSqlId("selectByPId");
		Map<String, Object> params = this.newParamMap();
		params.put("parentId", pId);
		return this.getSqlSession().selectList(sqlId, params);
	}

	@Override
	public int insert(GoodsCatMenuItem goodsCatMenuItem) {
		String sqlId = this.getNamedSqlId("insert");
		return this.getSqlSession().insert(sqlId, goodsCatMenuItem);
	}

	@Override
	public int update(GoodsCatMenuItem goodsCatMenuItem) {
		String sqlId = this.getNamedSqlId("update");
		return this.getSqlSession().update(sqlId, goodsCatMenuItem);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		return this.getSqlSession().delete(sqlId, id);
	}

}