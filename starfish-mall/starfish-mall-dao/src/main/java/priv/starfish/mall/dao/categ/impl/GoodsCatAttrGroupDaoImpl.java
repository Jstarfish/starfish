package priv.starfish.mall.dao.categ.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.categ.GoodsCatAttrGroupDao;
import priv.starfish.mall.categ.entity.GoodsCatAttrGroup;

@Component("goodsCatAttrGroupDao")
public class GoodsCatAttrGroupDaoImpl extends BaseDaoImpl<GoodsCatAttrGroup, Integer> implements GoodsCatAttrGroupDao {

	@Override
	public GoodsCatAttrGroup selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public GoodsCatAttrGroup selectByCatIdAndName(Integer catId, String name) {
		String sqlId = this.getNamedSqlId("selectByCatIdAndName");
		Map<String, Object> params = this.newParamMap();
		params.put("catId", catId);
		params.put("name", name);
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(GoodsCatAttrGroup goodsCatAttrGroup) {
		String sqlId = this.getNamedSqlId("insert");
		return this.getSqlSession().insert(sqlId, goodsCatAttrGroup);
	}

	@Override
	public int update(GoodsCatAttrGroup goodsCatAttrGroup) {
		String sqlId = this.getNamedSqlId("update");
		return this.getSqlSession().update(sqlId, goodsCatAttrGroup);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public int selectCountByCatId(Integer catId) {
		String sqlId = this.getNamedSqlId("selectCountByCatId");
		return this.getSqlSession().selectOne(sqlId, catId);
	}

	@Override
	public int deleteByCatId(Integer catId) {
		String sqlId = this.getNamedSqlId("deleteByCatId");
		return this.getSqlSession().delete(sqlId, catId);
	}

	@Override
	public int deleteByCatIds(List<Integer> catIds) {
		String sqlId = this.getNamedSqlId("deleteByCatIds");
		return this.getSqlSession().delete(sqlId, catIds);
	}

	@Override
	public List<GoodsCatAttrGroup> selectByCatId(Integer catId) {
		String sqlId = this.getNamedSqlId("selectByCatId");
		return this.getSqlSession().selectList(sqlId, catId);
	}

	@Override
	public int deleteByUncontainIdsAndCatId(List<Integer> ids, Integer catId) {
		String sqlId = this.getNamedSqlId("deleteByUncontainIdsAndCatId");
		Map<String, Object> params = this.newParamMap();
		params.put("ids", ids);
		params.put("catId", catId);
		return this.getSqlSession().delete(sqlId, params);
	}
}
