package priv.starfish.mall.dao.market;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.market.entity.SalesRegionSvc;

@IBatisSqlTarget
public interface SalesRegionSvcDao extends BaseDao<SalesRegionSvc, Integer> {
	SalesRegionSvc selectById(Integer id);

	SalesRegionSvc selectByRegionIdAndSvcId(Integer regionId, Integer svcId);

	int insert(SalesRegionSvc salesRegionSvc);

	int update(SalesRegionSvc salesRegionSvc);

	int deleteById(Integer id);

	/**
	 * 专区服务
	 * 
	 * @author 郝江奎
	 * @date 2016年2月2日 下午2:36:48
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<SalesRegionSvc> selectSalesRegionSvc(PaginatedFilter paginatedFilter);
	/**
	 * 根据销售专区Id获取销售专区服务
	 * 
	 * @author 廖晓远
	 * @date 2015年7月18日 上午10:40:57
	 * 
	 * @param regionId
	 * @return
	 */
	List<SalesRegionSvc> selectByRegionId(Integer regionId);

	/**
	 * 删除服务专区
	 * 
	 * @author 郝江奎
	 * @date 2016年2月17日 下午2:50:24
	 * 
	 * @param id
	 * @return
	 */
	int deleteByRegionId(Integer regionId);
	
}