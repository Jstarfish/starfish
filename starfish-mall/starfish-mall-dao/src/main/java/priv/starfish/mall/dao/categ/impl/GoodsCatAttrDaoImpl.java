package priv.starfish.mall.dao.categ.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.categ.GoodsCatAttrDao;
import priv.starfish.mall.categ.entity.GoodsCatAttr;

@Component("goodsCatAttrDao")
public class GoodsCatAttrDaoImpl extends BaseDaoImpl<GoodsCatAttr, Integer> implements GoodsCatAttrDao {
	@Override
	public GoodsCatAttr selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public GoodsCatAttr selectByCatIdAndRefId(Integer catId, Integer refId) {
		String sqlId = this.getNamedSqlId("selectByCatIdAndRefId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("catId", catId);
		params.put("refId", refId);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(GoodsCatAttr goodsCatAttr) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, goodsCatAttr);
	}

	@Override
	public int update(GoodsCatAttr goodsCatAttr) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, goodsCatAttr);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public int selectCountByCatId(Integer catId) {
		String sqlId = this.getNamedSqlId("selectCountByCatId");
		return this.getSqlSession().selectOne(sqlId, catId);
	}

	@Override
	public List<GoodsCatAttr> selectByCatId(Integer catId) {
		String sqlId = this.getNamedSqlId("selectByCatId");
		return this.getSqlSession().selectList(sqlId, catId);
	}
	
	@Override
	public List<Integer> selectIdsByRefId(Integer refId) {
		String sqlId = this.getNamedSqlId("selectIdByRefId");
		return this.getSqlSession().selectList(sqlId, refId);
	}

	@Override
	public int deleteByIds(List<Integer> ids) {
		if (ids != null && ids.size() > 0) {
			String sqlId = this.getNamedSqlId("deleteByIds");
			return this.getSqlSession().delete(sqlId, ids);
		} else {
			return 0;
		}
	}

	@Override
	public List<Integer> selectIdByCatId(Integer catId) {
		String sqlId = this.getNamedSqlId("selectIdByCatId");
		return this.getSqlSession().selectList(sqlId, catId);
	}

	@Override
	public List<Integer> selectIdByCatIds(List<Integer> catIds) {
		String sqlId = this.getNamedSqlId("selectIdByCatIds");
		return this.getSqlSession().selectList(sqlId, catIds);
	}

	@Override
	public Integer selectIdByCatIdAndBrandFlag(Integer catId, Boolean brandFlag) {
		String sqlId = this.getNamedSqlId("selectIdByCatIdAndBrandFlag");
		Map<String, Object> params = this.newParamMap();
		params.put("catId", catId);
		params.put("brandFlag", brandFlag);
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public List<GoodsCatAttr> selectByGroupId(Integer groupId) {
		
		String sqlId = this.getNamedSqlId("selectByGroupId");
		return this.getSqlSession().selectList(sqlId, groupId);
	}

	@Override
	public int deleteByRefId(Integer refId) {
		String sqlId = this.getNamedSqlId("deleteByRefId");
		return this.getSqlSession().delete(sqlId, refId);
	}

	@Override
	public List<Integer> selectIdsByRefIds(List<Integer> refIds) {
		String sqlId = this.getNamedSqlId("selectIdsByRefIds");
		return this.getSqlSession().selectList(sqlId, refIds);
	}

	@Override
	public int deleteByRefIds(List<Integer> refIds) {
		if (refIds != null && refIds.size() > 0) {
			String sqlId = this.getNamedSqlId("deleteByRefIds");
			return this.getSqlSession().delete(sqlId, refIds);
		} else {
			return 0;
		}
	}

	@Override
	public List<Integer> selectIdsByBrandFlag(boolean brandFlag) {
		String sqlId = this.getNamedSqlId("selectIdsByBrandFlag");
		//
		return this.getSqlSession().selectList(sqlId, brandFlag);
	}
}