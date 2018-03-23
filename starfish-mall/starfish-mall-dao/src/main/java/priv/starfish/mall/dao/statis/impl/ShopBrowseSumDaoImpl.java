package priv.starfish.mall.dao.statis.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.common.util.TypeUtil;
import priv.starfish.mall.dao.statis.ShopBrowseSumDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.statis.entity.ShopBrowseSum;

@Component("shopBrowseSumDao")
public class ShopBrowseSumDaoImpl extends BaseDaoImpl<ShopBrowseSum, Long> implements ShopBrowseSumDao {
	@Override
	public ShopBrowseSum selectById(Long id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public ShopBrowseSum selectByUserIdAndShopId(Integer userId, Integer shopId) {
		String sqlId = this.getNamedSqlId("selectByUserIdAndShopId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("userId", userId);
		params.put("shopId", shopId);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(ShopBrowseSum shopBrowseSum) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, shopBrowseSum);
	}

	@Override
	public int update(ShopBrowseSum shopBrowseSum) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, shopBrowseSum);
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
	public long selectBrowseCountByShopId(Integer shopId) {
		String sqlId = this.getNamedSqlId("selectBrowseCountByShopId");
		//
		return this.getSqlSession().selectOne(sqlId, shopId);
	}

	@Override
	public Map<Integer, Long> selectBrowseCountByShopIds(List<Integer> shopIds) {
		Map<Integer, Long> retMap = TypeUtil.newMap(Integer.class, Long.class);
		//
		if (!TypeUtil.isNullOrEmpty(shopIds)) {
			String sqlId = this.getNamedSqlId("selectBrowseCountByShopIds");
			//
			List<ShopBrowseSum> result = this.getSqlSession().selectList(sqlId, shopIds);
			//
			for (int i = 0; i < result.size(); i++) {
				ShopBrowseSum tmpSum = result.get(i);
				retMap.put(tmpSum.getShopId(), tmpSum.getCount());
			}
		}
		//
		return retMap;
	}
}