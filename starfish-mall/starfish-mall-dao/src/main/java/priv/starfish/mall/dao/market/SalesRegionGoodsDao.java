package priv.starfish.mall.dao.market;


import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.market.entity.SalesRegionGoods;

@IBatisSqlTarget
public interface SalesRegionGoodsDao extends BaseDao<SalesRegionGoods, Integer> {
	SalesRegionGoods selectById(Integer id);

	SalesRegionGoods selectByRegionIdAndProductId(Integer regionId, Long productId);

	int insert(SalesRegionGoods salesRegionGoods);

	int update(SalesRegionGoods salesRegionGoods);

	int deleteById(Integer id);

	/**
	 * 根据销售专区Id获取销售专区商品
	 * 
	 * @author 廖晓远
	 * @date 2015年7月18日 上午10:40:57
	 * 
	 * @param regionId
	 * @return
	 */
	List<SalesRegionGoods> selectByRegionId(Integer regionId);

	/**
	 * 根据销售专区Id删除销售专区商品
	 * 
	 * @author 廖晓远
	 * @date 2015年7月18日 上午10:41:38
	 * 
	 * @param regionId
	 * @return
	 */
	int deleteByRegionId(Integer regionId);

	/**
	 * 根据分区Id和ids删除多余的销售专区商品
	 * 
	 * @author 廖晓远
	 * @date 2015年7月18日 下午6:46:39
	 * 
	 * @param regionId
	 * @param uncontainIds
	 * @return
	 */
	int deleteByRegionIdAndUncontainIds(Integer regionId, List<Integer> uncontainIds);

	/**
	 * 分页查询专区商品
	 * 
	 * @author 郝江奎
	 * @date 2016年2月1日 下午4:41:16
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<SalesRegionGoods> selectSalesRegionGoods(PaginatedFilter paginatedFilter);
}