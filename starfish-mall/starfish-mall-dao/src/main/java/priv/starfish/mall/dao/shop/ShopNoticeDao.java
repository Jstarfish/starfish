package priv.starfish.mall.dao.shop;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.shop.entity.ShopNotice;

@IBatisSqlTarget
public interface ShopNoticeDao extends BaseDao<ShopNotice, Integer> {
	ShopNotice selectById(Integer id);

	int insert(ShopNotice shopNotice);

	int update(ShopNotice shopNotice);

	int deleteById(Integer id);

	/**
	 * 分页查询店铺公告
	 * 
	 * @author 毛智东
	 * @date 2015年8月14日 下午6:00:15
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<ShopNotice> selectList(PaginatedFilter paginatedFilter);

	/**
	 * 批量删除
	 * 
	 * @author 毛智东
	 * @date 2015年8月14日 下午6:17:00
	 * 
	 * @param ids
	 * @return
	 */
	int deleteByIds(List<Integer> ids);
}