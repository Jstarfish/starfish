package priv.starfish.mall.dao.goods.impl;

import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.goods.ProductExDao;
import priv.starfish.mall.goods.entity.ProductEx;

@Component("productExDao")
public class ProductExDaoImpl extends BaseDaoImpl<ProductEx, Long> implements ProductExDao {
	@Override
	public ProductEx selectById(Long id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(ProductEx productEx) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, productEx);
	}

	@Override
	public int update(ProductEx productEx) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, productEx);
	}

	@Override
	public int deleteById(Long id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public Long selectIdBySpecStrAndGoodsId(String specStr, Integer goodsId) {
		String sqlId = this.getNamedSqlId("selectIdBySpecStrAndGoodsId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("goodsId", goodsId);
		params.put("specStr", specStr);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}
}