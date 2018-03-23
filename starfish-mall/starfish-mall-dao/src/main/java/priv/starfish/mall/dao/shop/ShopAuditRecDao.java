package priv.starfish.mall.dao.shop;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.shop.entity.ShopAuditRec;

@IBatisSqlTarget
public interface ShopAuditRecDao extends BaseDao<ShopAuditRec, Integer> {
	ShopAuditRec selectById(Integer id);

	int insert(ShopAuditRec shopAuditRec);

	int update(ShopAuditRec shopAuditRec);

	int deleteById(Integer id);
}