package priv.starfish.mall.dao.shop.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.shop.ShopParamDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.shop.entity.ShopParam;

@Component("shopParamDao")
public class ShopParamDaoImpl extends BaseDaoImpl<ShopParam, Integer> implements ShopParamDao {
	@Override
	public ShopParam selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public ShopParam selectByShopIdAndCode(Integer shopId, String code) {
		String sqlId = this.getNamedSqlId("selectByShopIdAndCode");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("shopId", shopId);
		params.put("code", code);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(ShopParam shopParam) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, shopParam);
	}

	@Override
	public int update(ShopParam shopParam) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, shopParam);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<ShopParam> selectByCode(String code) {
		String sqlId = this.getNamedSqlId("selectByCode");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("code", code);
		//
		return this.getSqlSession().selectList(sqlId, params);
	}
}