package priv.starfish.mall.dao.goods.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.goods.ProductSpecValDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.goods.entity.ProductSpecVal;

@Component("productSpecValDao")
public class ProductSpecValDaoImpl extends BaseDaoImpl<ProductSpecVal, Long> implements ProductSpecValDao {
	@Override
	public ProductSpecVal selectById(Long id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public ProductSpecVal selectByProductIdAndSpecId(Integer productId, Integer specId) {
		String sqlId = this.getNamedSqlId("selectByProductIdAndSpecId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("productId", productId);
		params.put("specId", specId);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(ProductSpecVal productSpecVal) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, productSpecVal);
	}

	@Override
	public int update(ProductSpecVal productSpecVal) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, productSpecVal);
	}

	@Override
	public int deleteById(Long id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<ProductSpecVal> selectByProductIdAndGoodsId(Long productId, Integer goodsId) {
		String sqlId = this.getNamedSqlId("selectByProductIdAndGoodsId");
		Map<String, Object> params = this.newParamMap();
		params.put("productId", productId);
		params.put("goodsId", goodsId);
		//
		return this.getSqlSession().selectList(sqlId, params );
	}

	@Override
	public int deleteByProductId(Long productId) {
		String sqlId = this.getNamedSqlId("deleteByProductId");
		//
		return this.getSqlSession().delete(sqlId, productId);
	}

	@Override
	public List<Integer> selectSpecItemIdsBySpecIdAndGoodsId(Integer specId, Integer goodsId) {
		String sqlId = this.getNamedSqlId("selectSpecItemIdsBySpecIdAndGoodsId");
		Map<String, Object> params = this.newParamMap();
		params.put("specId", specId);
		params.put("goodsId", goodsId);
		//
		return this.getSqlSession().selectList(sqlId, params );
	}

	@Override
	public int deleteBySpecIds(List<Integer> goodsCatSpecId) {
		if (goodsCatSpecId != null && goodsCatSpecId.size() > 0) {
			String sqlId = this.getNamedSqlId("deleteBySpecIds");
			return this.getSqlSession().delete(sqlId, goodsCatSpecId);
		} else {
			return 0;
		}
	}

	@Override
	public int deleteBySpecId(Integer goodsCatSpecId) {
		String sqlId = this.getNamedSqlId("deleteBySpecId");
		return this.getSqlSession().delete(sqlId, goodsCatSpecId);
	}

	@Override
	public List<ProductSpecVal> selectByProductId(Long productId) {
		String sqlId = this.getNamedSqlId("selectByProductId");
		//
		return this.getSqlSession().selectList(sqlId, productId);
	}

	@Override
	public List<String> selectRefCodeByGoodsId(Integer goodsId) {
		String sqlId = this.getNamedSqlId("selectRefCodeByGoodsId");
		//
		return this.getSqlSession().selectList(sqlId, goodsId);
	}

	@Override
	public List<Integer> selectSpecItemIdsByRefCodeAndGoodsId(String refCode, Integer goodsId) {
		String sqlId = this.getNamedSqlId("selectSpecItemIdsByRefCodeAndGoodsId");
		Map<String, Object> params = this.newParamMap();
		params.put("refCode", refCode);
		params.put("goodsId", goodsId);
		//
		return this.getSqlSession().selectList(sqlId, params);
	}

	@Override
	public List<String> selectRefCodeByIncludeProductIds(List<Long> productIds) {
		String sqlId = this.getNamedSqlId("selectRefCodeByIncludeProductIds");
		//
		return this.getSqlSession().selectList(sqlId, productIds);
	}

	@Override
	public List<Integer> selectSpecItemIdsByRefCodeAndIncludeProductIds(String refCode, List<Long> productIds) {
		String sqlId = this.getNamedSqlId("selectSpecItemIdsByRefCodeAndIncludeProductIds");
		Map<String, Object> params = this.newParamMap();
		params.put("refCode", refCode);
		params.put("list", productIds);
		//
		return this.getSqlSession().selectList(sqlId, params);
	}
}