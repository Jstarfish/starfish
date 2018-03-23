package priv.starfish.mall.dao.shop;

import java.util.Date;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.shop.entity.DistShop;

@IBatisSqlTarget
public interface DistShopDao extends BaseDao<DistShop, Integer> {
	DistShop selectById(Integer id);

	int insert(DistShop distShop);

	int update(DistShop distShop);

	int deleteById(Integer id);

	/**
	 * 分页查询店铺
	 * 
	 * @author 郝江奎
	 * @date 2016年1月19日 上午10:30:43
	 * 
	 * @param paginatedFilter
	 * @return PaginatedList<DistShop>
	 */
	PaginatedList<DistShop> selectDistShops(PaginatedFilter paginatedFilter);

	/**
	 * 获取分页的最新变化/需要重新索引的(indexTime == null || changeTime != null && changeTime > indexTime) <br/>
	 */
	PaginatedList<DistShop> selectByLatestChanges(PaginatedFilter pager);

	/**
	 * 
	 * 更新为已索引
	 */
	int updateAsIndexed(Integer id, Date newIndexTime);

	/**
	 * 
	 *更新更改时间
	 */
	int updateChangeTime(Integer id);
}