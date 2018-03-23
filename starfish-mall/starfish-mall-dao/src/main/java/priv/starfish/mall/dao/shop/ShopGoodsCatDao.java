package priv.starfish.mall.dao.shop;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.shop.entity.ShopGoodsCat;

@IBatisSqlTarget
public interface ShopGoodsCatDao extends BaseDao<ShopGoodsCat, Integer> {
	ShopGoodsCat selectById(Integer id);

	ShopGoodsCat selectByShopIdAndCatId(Integer shopId, Integer catId);

	int insert(ShopGoodsCat shopGoodsCat);

	int update(ShopGoodsCat shopGoodsCat);

	int deleteById(Integer id);

	/**
	 * 查询分类下店铺数量
	 * 
	 * @author 廖晓远
	 * @date 2015-5-29 上午11:20:22
	 * 
	 * @param catId
	 * @return
	 */
	int countShopByCatId(Integer catId);
}