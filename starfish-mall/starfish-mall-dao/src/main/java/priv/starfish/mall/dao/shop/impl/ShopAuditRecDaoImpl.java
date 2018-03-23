package priv.starfish.mall.dao.shop.impl;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.shop.ShopAuditRecDao;
import priv.starfish.mall.shop.entity.ShopAuditRec;

@Component("shopAuditRecDao")
public class ShopAuditRecDaoImpl extends BaseDaoImpl<ShopAuditRec, Integer> implements ShopAuditRecDao {
	@Override
	public ShopAuditRec selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(ShopAuditRec shopAuditRec) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, shopAuditRec);
	}

	@Override
	public int update(ShopAuditRec shopAuditRec) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, shopAuditRec);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}
}