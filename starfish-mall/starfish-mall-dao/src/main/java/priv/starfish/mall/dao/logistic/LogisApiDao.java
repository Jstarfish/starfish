package priv.starfish.mall.dao.logistic;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.logistic.entity.LogisApi;

@IBatisSqlTarget
public interface LogisApiDao extends BaseDao<LogisApi, Integer> {

	/**
	 * 分页获取物流接口信息
	 * 
	 * @author guoyn
	 * @date 2015年5月19日 下午11:01:10
	 * 
	 * @param paginatedFilter
	 * @return PaginatedList<LogisticApi>
	 */
	PaginatedList<LogisApi> selectLogisApisByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 根据id获取LogisApi
	 * 
	 * @author guoyn
	 * @date 2015年5月21日 下午2:47:18
	 * 
	 * @param logisApiId
	 * @return LogisApi
	 */
	LogisApi selectById(Integer logisApiId);

}
