package priv.starfish.mall.dao.goods.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.goods.GoodsAttrValDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.goods.entity.GoodsAttrVal;

@Component("goodsAttrValDao")
public class GoodsAttrValDaoImpl extends BaseDaoImpl<GoodsAttrVal, Long> implements GoodsAttrValDao {
	@Override
	public GoodsAttrVal selectById(Long id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public List<GoodsAttrVal> selectByGoodsIdAndAttrId(Integer goodsId, Integer attrId) {
		String sqlId = this.getNamedSqlId("selectByGoodsIdAndAttrId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("goodsId", goodsId);
		params.put("attrId", attrId);
		//
		return this.getSqlSession().selectList(sqlId, params);
	}

	@Override
	public int insert(GoodsAttrVal goodsAttrVal) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, goodsAttrVal);
	}

	@Override
	public int update(GoodsAttrVal goodsAttrVal) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, goodsAttrVal);
	}

	@Override
	public int deleteById(Long id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<GoodsAttrVal> selectByGoodsId(Integer goodsId) {
		String sqlId = this.getNamedSqlId("selectByGoodsId");
		return this.getSqlSession().selectList(sqlId, goodsId);
	}

	@Override
	public int deleteByAttrId(Integer attrId) {
		String sqlId = this.getNamedSqlId("deleteByAttrId");
		return this.getSqlSession().delete(sqlId, attrId);
	}
	
	@Override
	public int deleteByAttrIds(List<Integer> attrIds) {
		if (attrIds != null && attrIds.size() > 0) {
			String sqlId = this.getNamedSqlId("deleteByAttrIds");
			return this.getSqlSession().delete(sqlId, attrIds);
		} else {
			return 0;
		}
	}

	@Override
	public List<Integer> selectGoodsIdByAttrIdAndAttrVal(Integer goodsCatAttrId, String attrVal) {
		String sqlId = this.getNamedSqlId("selectGoodsIdByAttrIdAndAttrVal");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("attrId", goodsCatAttrId);
		params.put("attrVal", attrVal);
		//
		return this.getSqlSession().selectList(sqlId, params);
	}

	@Override
	public int deleteByGoodsId(Integer goodsId) {
		String sqlId = this.getNamedSqlId("deleteByGoodsId");
		//
		return this.getSqlSession().delete(sqlId, goodsId);
	}

	@Override
	public List<GoodsAttrVal> selectByGoodsIdAndKeyFlag(Integer goodsId, boolean keyFlag) {
		String sqlId = this.getNamedSqlId("selectByGoodsIdAndKeyFlag");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("goodsId", goodsId);
		params.put("keyFlag", keyFlag);
		//
		return this.getSqlSession().selectList(sqlId, params);
	}
}
