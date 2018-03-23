package priv.starfish.mall.dao.categ.impl;


import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.categ.GoodsCatPriceRangeDao;
import priv.starfish.mall.categ.entity.GoodsCatPriceRange;

@Component("goodsCatPriceRangeDao")
public class GoodsCatPriceRangeDaoImpl extends BaseDaoImpl<GoodsCatPriceRange, Integer> implements GoodsCatPriceRangeDao {
	@Override
	public GoodsCatPriceRange selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(GoodsCatPriceRange goodsCatPriceRange) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, goodsCatPriceRange);
	}

	@Override
	public int update(GoodsCatPriceRange goodsCatPriceRange) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, goodsCatPriceRange);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<GoodsCatPriceRange> selectByCatId(Integer catId) {
		String sqlId = this.getNamedSqlId("selectByCatId");
		return this.getSqlSession().selectList(sqlId, catId);
	}

	@Override
	public int deleteByCatIds(List<Integer> catIds) {
		String sqlId = this.getNamedSqlId("deleteByCatIds");
		return this.getSqlSession().delete(sqlId, catIds);
	}

	@Override
	public int deleteByCatId(Integer catId) {
		String sqlId = this.getNamedSqlId("deleteByCatId");
		return this.getSqlSession().delete(sqlId, catId);
	}

	@Override
	public int deleteCatPriceRangeByCatIdAndNotInIds(Integer catId, List<Integer> ids) {
		String sqlId = this.getNamedSqlId("deleteCatPriceRangeByCatIdAndNotInIds");
		Map<String, Object> params = this.newParamMap();
		params.put("ids", ids);
		params.put("catId", catId);
		return this.getSqlSession().delete(sqlId, params);
	}
}