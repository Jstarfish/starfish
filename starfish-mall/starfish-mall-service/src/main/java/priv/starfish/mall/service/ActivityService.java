package priv.starfish.mall.service;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.market.entity.Activity;

public interface ActivityService extends BaseService {

	Activity getActivityById(Integer id);

	Activity getActivityByYearAndNameAndTargetFlag(Integer year, String name, Integer targetFlag);

	boolean insert(Activity activity);

	boolean update(Activity activity);

	boolean deleteActivityById(Integer id);

	/**
	 * 分页查询活动
	 * 
	 * @author 郝江奎
	 * @date 2016年1月28日 下午2:45:38
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<Activity> getActivitysByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 添加活动
	 * 
	 * @author 郝江奎
	 * @date 2016年1月28日 下午2:48:35
	 * 
	 * @param activity
	 * @return
	 */
	boolean saveActivity(Activity activity);

}
