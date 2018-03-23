package priv.starfish.mall.dao.market;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.market.entity.Activity;

@IBatisSqlTarget
public interface ActivityDao extends BaseDao<Activity, Integer> {
	Activity selectById(Integer id);

	Activity selectByYearAndNameAndTargetFlag(Integer year, String name, Integer targetFlag);

	int insert(Activity activity);

	int update(Activity activity);

	int deleteById(Integer id);

	/**
	 * 分页查询活动
	 * 
	 * @author 郝江奎
	 * @date 2016年1月28日 下午2:53:43
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<Activity> selectActivitys(PaginatedFilter paginatedFilter);

	/**
	 * 活动发布停用
	 * 
	 * @author 郝江奎
	 * @date 2016年1月29日 上午10:57:48
	 * 
	 * @param id
	 * @param status
	 * @return
	 */
	int updateActivityState(Integer id, Integer status);
}