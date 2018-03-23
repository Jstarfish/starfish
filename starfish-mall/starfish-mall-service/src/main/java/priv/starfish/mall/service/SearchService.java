package priv.starfish.mall.service;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.shop.dto.ShopDto;
import priv.starfish.mall.shop.entity.DistShop;

/**
 * elasticsearch相关
 */
public interface SearchService {
	void echoSearcherInfo();

	/**
	 * 
	 * 创建全局或者增量索引（店铺）
	 */
	void indexShopDocs(boolean scanAll);

	/**
	 * 增量更新某一条记索引录（店铺）
	 */
	void indexShopDocById(Integer shopId);

	/**
	 * 
	 * 创建全局或者增量索引（卫星店铺）
	 */
	void indexDistShopDocs(boolean scanAll);

	/**
	 * 
	 * 根据条件搜索店铺
	 */
	PaginatedList<ShopDto> searchShopsByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 
	 * 根据条件搜索卫星店铺
	 */
	PaginatedList<DistShop> searchDistShopsByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 
	 * 增量索引某一条记录(卫星店铺)
	 */
	void indexDistShopDocById(Integer distShopId);

}
