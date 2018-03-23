package priv.starfish.mall.dao.car;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.car.entity.UserCar;

@IBatisSqlTarget
public interface UserCarDao extends BaseDao<UserCar, Integer> {
	UserCar selectById(Integer id);

	UserCar selectByUserIdAndModelId(Integer userId, Integer modelId);

	int insert(UserCar userCar);

	int update(UserCar userCar);

	int deleteById(Integer id);
	
	/**
	 * 
	 * 
	 * @author 郝江奎
	 * @date 2016年1月23日 下午2:58:03
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<UserCar> selectUserCars(PaginatedFilter paginatedFilter);

	/**
	 * 通过userId修改userCar
	 * 
	 * @author 郝江奎
	 * @date 2016年1月26日 上午10:16:32
	 * 
	 * @param userId
	 * @return
	 */
	Integer updateUserCarByUserId(Integer userId);

	/**
	 * 获取用户默认的车辆
	 * 
	 * @author 郝江奎
	 * @date 2016年1月26日 下午2:55:39
	 * 
	 * @param defaulted
	 * @param userId
	 * @return
	 */
	UserCar selectDefaultByUserId(boolean defaulted, Integer userId);

	/**
	 * 获取用户的车辆
	 * 
	 * @author 郝江奎
	 * @date 2016年1月26日 下午2:55:39
	 * 
	 * @param userId
	 * @return
	 */
	List<UserCar> selectByUserId(Integer userId);
}