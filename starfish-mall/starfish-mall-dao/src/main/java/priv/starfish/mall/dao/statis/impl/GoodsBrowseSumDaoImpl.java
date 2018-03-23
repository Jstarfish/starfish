package priv.starfish.mall.dao.statis.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.common.util.TypeUtil;
import priv.starfish.mall.dao.statis.GoodsBrowseSumDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.statis.entity.GoodsBrowseSum;

@Component("goodsBrowseSumDao")
public class GoodsBrowseSumDaoImpl extends BaseDaoImpl<GoodsBrowseSum, Long> implements GoodsBrowseSumDao {
	@Override
	public GoodsBrowseSum selectById(Long id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public GoodsBrowseSum selectByUserIdAndProductId(Integer userId, Long productId) {
		String sqlId = this.getNamedSqlId("selectByUserIdAndProductId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("userId", userId);
		params.put("productId", productId);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(GoodsBrowseSum goodsBrowseSum) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, goodsBrowseSum);
	}

	@Override
	public int update(GoodsBrowseSum goodsBrowseSum) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, goodsBrowseSum);
	}

	@Override
	public int deleteById(Long id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public boolean addBrowseCountById(Long id, int count) {
		String sqlId = this.getNamedSqlId("addBrowseCountById");
		//
		//
		Map<String, Object> params = this.newParamMap();
		params.put("id", id);
		params.put("count", count);
		//
		return this.getSqlSession().update(sqlId, params) > 0;
	}

	@Override
	public long selectBrowseCountByProductId(Long productId) {
		String sqlId = this.getNamedSqlId("selectBrowseCountByProductId");
		//
		return this.getSqlSession().selectOne(sqlId, productId);
	}

	@Override
	public Map<Long, Long> selectBrowseCountByProductIds(List<Long> productIds) {
		Map<Long, Long> retMap = TypeUtil.newMap(Long.class, Long.class);
		//
		if (!TypeUtil.isNullOrEmpty(productIds)) {
			String sqlId = this.getNamedSqlId("selectBrowseCountByProductIds");
			//
			List<GoodsBrowseSum> result = this.getSqlSession().selectList(sqlId, productIds);
			//
			for (int i = 0; i < result.size(); i++) {
				GoodsBrowseSum tmpSum = result.get(i);
				retMap.put(tmpSum.getProductId(), tmpSum.getCount());
			}
		}
		//
		return retMap;
	}
}