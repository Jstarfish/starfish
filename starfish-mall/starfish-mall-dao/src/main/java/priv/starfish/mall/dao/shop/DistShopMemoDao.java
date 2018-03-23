package priv.starfish.mall.dao.shop;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.shop.entity.DistShopMemo;

/**
 * 卫星店备忘录
 * @author 邓华锋
 * @date 2016年02月19日 19:11:12
 *
 */@IBatisSqlTarget
public interface DistShopMemoDao extends BaseDao<DistShopMemo, Integer> {
	
	int insert(DistShopMemo distShopMemo);
	
	int deleteById(Integer id);
	
	int update(DistShopMemo distShopMemo);
	
	DistShopMemo selectById(Integer id);
	
	DistShopMemo selectByIdAndDistShopId(Integer id, Integer distShopId);
	
	/**
	 * 卫星店备忘录分页
	 * @author 邓华锋
	 * @date  2016年02月19日 07:11:12
	 * 
	 * @param paginatedFilter 
	 * 						like  distShopName,like  title,like  content
	 * @return
	 */
	PaginatedList<DistShopMemo> selectByFilter(PaginatedFilter paginatedFilter);
	
}