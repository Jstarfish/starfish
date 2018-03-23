package priv.starfish.mall.dao.categ.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.categ.GoodsCatMenuItemLinkDao;
import priv.starfish.mall.categ.entity.GoodsCatMenuItemLink;

@Component("goodsCatMenuItemLinkDao")
public class GoodsCatMenuItemLinkDaoImpl extends BaseDaoImpl<GoodsCatMenuItemLink, Integer> implements GoodsCatMenuItemLinkDao {

	@Override
	public GoodsCatMenuItemLink selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public List<GoodsCatMenuItemLink> selectByMenuItemId(Integer menuItemId) {
		String sqlId = this.getNamedSqlId("selectByMenuItemId");
		return this.getSqlSession().selectList(sqlId, menuItemId);
	}
	
	@Override
	public GoodsCatMenuItemLink selectByMenuItemIdAndName(Integer menuItemId, String name) {
		String sqlId = this.getNamedSqlId("selectByMenuItemIdAndName");
		Map<String, Object> params = this.newParamMap();
		params.put("menuItemId", menuItemId);
		params.put("name", name);
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(GoodsCatMenuItemLink goodsCatMenuItemLink) {
		String sqlId = this.getNamedSqlId("insert");
		return this.getSqlSession().insert(sqlId, goodsCatMenuItemLink);
	}

	@Override
	public int update(GoodsCatMenuItemLink goodsCatMenuItemLink) {
		String sqlId = this.getNamedSqlId("update");
		return this.getSqlSession().update(sqlId, goodsCatMenuItemLink);
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

}
