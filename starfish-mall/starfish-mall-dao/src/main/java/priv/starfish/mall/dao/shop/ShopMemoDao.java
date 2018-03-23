package priv.starfish.mall.dao.shop;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.shop.entity.ShopMemo;

/**
 * 店铺备忘录
 * @author 邓华锋
 * @date 2016年02月19日 18:09:44
 *
 */@IBatisSqlTarget
public interface ShopMemoDao extends BaseDao<ShopMemo, Integer> {
	
	int insert(ShopMemo shopMemo);
	
	int deleteById(Integer id);
	
	int update(ShopMemo shopMemo);
	
	ShopMemo selectById(Integer id);
	
	ShopMemo selectByIdAndShopId(Integer id, Integer shopId);
	
	/**
	 * 店铺备忘录分页
	 * @author 邓华锋
	 * @date  2016年02月19日 06:09:44
	 * 
	 * @param paginatedFilter 
	 * 						like  shopName,like  title,like  content
	 * @return
	 */
	PaginatedList<ShopMemo> selectByFilter(PaginatedFilter paginatedFilter);
	
}