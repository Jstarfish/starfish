package priv.starfish.mall.dao.shop.impl;

import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.shop.ShopBizStatusDao;
import priv.starfish.mall.shop.entity.ShopBizStatus;

@Component("shopBizStatusDao")
public class ShopBizStatusDaoImpl extends BaseDaoImpl<ShopBizStatus, Long> implements ShopBizStatusDao {
	@Override
	public ShopBizStatus selectById(Long id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public ShopBizStatus selectByShopIdAndDateStr(Integer shopId, String dateStr) {
		String sqlId = this.getNamedSqlId("selectByShopIdAndDateStr");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("shopId", shopId);
		params.put("dateStr", dateStr);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(ShopBizStatus shopBizStatus) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, shopBizStatus);
	}

	@Override
	public int update(ShopBizStatus shopBizStatus) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, shopBizStatus);
	}

	@Override
	public int deleteById(Long id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}
}