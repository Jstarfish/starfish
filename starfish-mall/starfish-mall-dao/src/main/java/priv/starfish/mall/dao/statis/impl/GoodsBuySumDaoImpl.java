package priv.starfish.mall.dao.statis.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.statis.GoodsBuySumDao;
import priv.starfish.mall.statis.entity.GoodsBuySum;

@Component("goodsBuySumDao")
public class GoodsBuySumDaoImpl extends BaseDaoImpl<GoodsBuySum, Long> implements GoodsBuySumDao {
	@Override
	public GoodsBuySum selectById(Long id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public GoodsBuySum selectByUserIdAndProductId(Integer userId, Long productId) {
		String sqlId = this.getNamedSqlId("selectByUserIdAndProductId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("userId", userId);
		params.put("productId", productId);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(GoodsBuySum goodsBuySum) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, goodsBuySum);
	}

	@Override
	public int update(GoodsBuySum goodsBuySum) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, goodsBuySum);
	}

	@Override
	public int deleteById(Long id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<Integer> selectUserIdsByShopId(Integer shopId) {
		String sqlId = this.getNamedSqlId("selectUserIdsByShopId");
		//
		return this.getSqlSession().selectList(sqlId, shopId);
	}

	@Override
	public long selectBuyCountByProductId(Long productId) {
		String sqlId = this.getNamedSqlId("selectBuyCountByProductId");
		//
		return this.getSqlSession().selectOne(sqlId, productId);
	}

	@Override
	public int deleteByProductId(Long productId) {
		String sqlId = this.getNamedSqlId("deleteByProductId");
		//
		return this.getSqlSession().delete(sqlId, productId);
	}

}