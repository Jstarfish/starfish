package priv.starfish.mall.dao.shop.impl;

import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.shop.ShopGoodsCatDao;
import priv.starfish.mall.shop.entity.ShopGoodsCat;

@Component("shopGoodsCatDao")
public class ShopGoodsCatDaoImpl extends BaseDaoImpl<ShopGoodsCat, Integer> implements ShopGoodsCatDao {
	@Override
	public ShopGoodsCat selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public ShopGoodsCat selectByShopIdAndCatId(Integer shopId, Integer catId) {
		String sqlId = this.getNamedSqlId("selectByShopIdAndCatId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("shopId", shopId);
		params.put("catId", catId);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(ShopGoodsCat shopGoodsCat) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, shopGoodsCat);
	}

	@Override
	public int update(ShopGoodsCat shopGoodsCat) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, shopGoodsCat);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public int countShopByCatId(Integer catId) {
		String sqlId = this.getNamedSqlId("countShopByCatId");
		return this.getSqlSession().selectOne(sqlId, catId);
	}
}