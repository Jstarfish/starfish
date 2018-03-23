package priv.starfish.mall.dao.categ.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.categ.GoodsCatSpecDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.categ.entity.GoodsCatSpec;

@Component("goodsCatSpecDao")
public class GoodsCatSpecDaoImpl extends BaseDaoImpl<GoodsCatSpec, Integer> implements GoodsCatSpecDao {

	@Override
	public GoodsCatSpec selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public GoodsCatSpec selectByCatIdAndRefId(Integer catId, Integer refId) {
		String sqlId = this.getNamedSqlId("selectByCatIdAndRefId");
		Map<String, Object> params = this.newParamMap();
		params.put("catId", catId);
		params.put("refId", refId);
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(GoodsCatSpec goodsCatSpec) {
		String sqlId = this.getNamedSqlId("insert");
		return this.getSqlSession().insert(sqlId, goodsCatSpec);
	}

	@Override
	public int update(GoodsCatSpec goodsCatSpec) {
		String sqlId = this.getNamedSqlId("update");
		return this.getSqlSession().update(sqlId, goodsCatSpec);
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
	public List<GoodsCatSpec> selectByCatId(Integer catId) {
		String sqlId = this.getNamedSqlId("selectByCatId");
		return this.getSqlSession().selectList(sqlId, catId);
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
	public List<Integer> selectIdsByRefId(Integer specId) {
		String sqlId = this.getNamedSqlId("selectIdByRefId");
		return this.getSqlSession().selectList(sqlId, specId);
	}

	@Override
	public int deleteByRefId(Integer specId) {
		String sqlId = this.getNamedSqlId("deleteByRefId");
		return this.getSqlSession().delete(sqlId, specId);
	}

	@Override
	public List<Integer> selectIdByRefIds(List<Integer> specIds) {
		String sqlId = this.getNamedSqlId("selectIdByRefIds");
		return this.getSqlSession().selectList(sqlId, specIds);
	}

	@Override
	public int deleteByRefIds(List<Integer> specIds) {
		if (specIds != null && specIds.size() > 0) {
			String sqlId = this.getNamedSqlId("deleteByRefIds");
			return this.getSqlSession().delete(sqlId, specIds);
		} else {
			return 0;
		}
	}

	@Override
	public List<Integer> selectIdsByColorFlag(boolean colorFlag) {
		String sqlId = this.getNamedSqlId("selectIdsByColorFlag");
		//
		return this.getSqlSession().selectList(sqlId, colorFlag);
	}

}
