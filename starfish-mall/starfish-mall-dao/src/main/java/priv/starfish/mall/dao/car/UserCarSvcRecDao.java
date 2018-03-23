package priv.starfish.mall.dao.car;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.car.entity.UserCarSvcRec;

@IBatisSqlTarget
public interface UserCarSvcRecDao extends BaseDao<UserCarSvcRec, Integer> {

	UserCarSvcRec selectById(Integer id);
	
	UserCarSvcRec selectUserCarSvcRecByUserIdAndCarId(Integer userId, Integer carId);

	int insert(UserCarSvcRec userCarSvcRec);

	int update(UserCarSvcRec userCarSvcRec);

	int deleteById(Integer id);

	/**
	 * 
	 * 
	 * @author 郝江奎
	 * @date 2016年1月25日 上午10:02:39
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<UserCarSvcRec> selectUserCarSvcRecs(PaginatedFilter paginatedFilter);
}