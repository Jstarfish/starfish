package priv.starfish.mall.dao.goods.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.goods.GoodsExDao;
import priv.starfish.mall.goods.entity.GoodsEx;

@Component("goodsExDao")
public class GoodsExDaoImpl extends BaseDaoImpl<GoodsEx, Integer> implements GoodsExDao {
	@Override
	public GoodsEx selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public GoodsEx selectByGoodsIdAndSpecCode(Integer goodsId, String specCode) {
		String sqlId = this.getNamedSqlId("selectByGoodsIdAndSpecCode");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("goodsId", goodsId);
		params.put("specCode", specCode);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(GoodsEx goodsEx) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, goodsEx);
	}

	@Override
	public int update(GoodsEx goodsEx) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, goodsEx);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<GoodsEx> selectByGoodsId(Integer goodsId) {
		String sqlId = this.getNamedSqlId("selectByGoodsId");
		//
		return this.getSqlSession().selectList(sqlId, goodsId);
	}

	@Override
	public int deleteByGoodsId(Integer goodsId) {
		String sqlId = this.getNamedSqlId("deleteByGoodsId");
		//
		return this.getSqlSession().delete(sqlId, goodsId);
	}
}