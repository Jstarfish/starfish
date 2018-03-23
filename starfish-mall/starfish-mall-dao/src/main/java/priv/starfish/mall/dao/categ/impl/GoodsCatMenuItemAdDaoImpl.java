package priv.starfish.mall.dao.categ.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.categ.GoodsCatMenuItemAdDao;
import priv.starfish.mall.categ.entity.GoodsCatMenuItemAd;

@Component("goodsCatMenuItemAdDao")
public class GoodsCatMenuItemAdDaoImpl extends BaseDaoImpl<GoodsCatMenuItemAd, Integer> implements GoodsCatMenuItemAdDao {

	@Override
	public GoodsCatMenuItemAd selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public List<GoodsCatMenuItemAd> selectByMenuItemId(Integer menuItemId) {
		String sqlId = this.getNamedSqlId("selectByMenuItemId");
		return this.getSqlSession().selectList(sqlId, menuItemId);
	}
	
	@Override
	public GoodsCatMenuItemAd selectByMenuItemIdAndImageUuid(Integer menuItemId, String imageUuid) {
		String sqlId = this.getNamedSqlId("selectByMenuItemIdAndImageUuid");
		Map<String, Object> params = this.newParamMap();
		params.put("menuItemId", menuItemId);
		params.put("imageUuid", imageUuid);
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(GoodsCatMenuItemAd goodsCatMenuItemAd) {
		String sqlId = this.getNamedSqlId("insert");
		return this.getSqlSession().insert(sqlId, goodsCatMenuItemAd);
	}

	@Override
	public int update(GoodsCatMenuItemAd goodsCatMenuItemAd) {
		String sqlId = this.getNamedSqlId("update");
		return this.getSqlSession().update(sqlId, goodsCatMenuItemAd);
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
