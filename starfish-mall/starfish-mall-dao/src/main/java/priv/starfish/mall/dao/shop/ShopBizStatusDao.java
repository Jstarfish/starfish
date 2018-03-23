package priv.starfish.mall.dao.shop;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.shop.entity.ShopBizStatus;

@IBatisSqlTarget
public interface ShopBizStatusDao extends BaseDao<ShopBizStatus, Long> {
	ShopBizStatus selectById(Long id);

	ShopBizStatus selectByShopIdAndDateStr(Integer shopId, String dateStr);

	int insert(ShopBizStatus shopBizStatus);

	int update(ShopBizStatus shopBizStatus);

	int deleteById(Long id);
}