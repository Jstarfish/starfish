package priv.starfish.mall.dao.market;


import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.market.entity.SalesRegion;

@IBatisSqlTarget
public interface SalesRegionDao extends BaseDao<SalesRegion, Integer> {
	SalesRegion selectById(Integer id);

	SalesRegion selectByNameAndFloorNo(String name, Integer floorNo);

	int insert(SalesRegion salesRegion);

	int update(SalesRegion salesRegion);

	int deleteById(Integer id);

	/**
	 * 通过楼层号获取销售专区
	 * 
	 * @author 廖晓远
	 * @date 2015年7月18日 上午10:36:00
	 * 
	 * @return
	 */
	List<SalesRegion> selectByFloorNo(Integer floorNo);

	/**
	 * 通过楼层号删除销售专区
	 * 
	 * @author 廖晓远
	 * @date 2015年7月18日 上午10:36:24
	 * 
	 * @param floorNo
	 * @return
	 */
	int deleteByFloorNo(Integer floorNo);

	/**
	 * 根据楼层号和ids删除多余的销售专区商品
	 * 
	 * @author 廖晓远
	 * @date 2015年7月18日 下午6:46:39
	 * 
	 * @param floorNo
	 * @param uncontainIds
	 * @return
	 */
	int deleteByFloorNoAndUncontainIds(Integer floorNo, List<Integer> uncontainIds);

	/**
	 * 分页查询SalesRegion
	 * 
	 * @author 郝江奎
	 * @date 2016年1月30日 下午6:04:59
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<SalesRegion> selectSalesRegions(PaginatedFilter paginatedFilter);

	/**
	 * 查询不同类型的SalesRegion
	 * 
	 * @author 郝江奎
	 * @date 2016年1月30日 下午6:05:36
	 * 
	 * @param type
	 * @return
	 */
	List<SalesRegion> selectByType(Integer type);
}