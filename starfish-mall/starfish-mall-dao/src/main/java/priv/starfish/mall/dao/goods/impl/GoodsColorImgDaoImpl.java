package priv.starfish.mall.dao.goods.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.goods.GoodsColorImgDao;
import priv.starfish.mall.goods.entity.GoodsColorImg;

@Component("goodsColorImgDao")
public class GoodsColorImgDaoImpl extends BaseDaoImpl<GoodsColorImg, Long> implements GoodsColorImgDao {
	@Override
	public GoodsColorImg selectById(Long id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public GoodsColorImg selectByGoodsIdAndSpecIdAndSpecItemId(Integer goodsId, Integer specId, Integer specItemId) {
		String sqlId = this.getNamedSqlId("selectByGoodsIdAndSpecIdAndSpecItemId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("goodsId", goodsId);
		params.put("specId", specId);
		params.put("specItemId", specItemId);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(GoodsColorImg goodsColorImg) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, goodsColorImg);
	}

	@Override
	public int update(GoodsColorImg goodsColorImg) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, goodsColorImg);
	}

	@Override
	public int deleteById(Long id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<GoodsColorImg> selectByGoodsId(Integer goodsId) {
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

	@Override
	public int deleteByImgId(Long imgId) {
		String sqlId = this.getNamedSqlId("deleteByImgId");
		//
		return this.getSqlSession().delete(sqlId, imgId);
	}
}
