package priv.starfish.mall.dao.market;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.market.entity.SalesFloor;

@IBatisSqlTarget
public interface SalesFloorDao extends BaseDao<SalesFloor, Integer> {
	SalesFloor selectById(Integer no);

	SalesFloor selectByName(String name);

	int insert(SalesFloor salesFloor);

	int update(SalesFloor salesFloor);

	int deleteById(Integer no);

	/**
	 * 分页查询销售楼层
	 * 
	 * @author 郝江奎
	 * @date 2015年7月14日 下午6:32:24
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<SalesFloor> selectSalesFloors(PaginatedFilter paginatedFilter);

	/**
	 * 查询楼层
	 * 
	 * @author 郝江奎
	 * @date 2016年1月30日 下午5:14:52
	 * 
	 * @param type
	 * @return
	 */
	List<SalesFloor> selectByType(Integer type);
}