package priv.starfish.mall.dao.comn;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.comn.dict.VerfAspect;
import priv.starfish.mall.comn.entity.UserVerfStatus;

@IBatisSqlTarget
public interface UserVerfStatusDao extends BaseDao<UserVerfStatus, Integer> {
	UserVerfStatus selectById(Integer id);
	
	List<UserVerfStatus> selectByUserId(Integer userId);

	UserVerfStatus selectByUserIdAndAspect(Integer userId, VerfAspect aspect);

	int insert(UserVerfStatus userVerfStatus);

	int update(UserVerfStatus userVerfStatus);

	int deleteById(Integer id);

	/**
	 * 根据用户id和aspect删除用户验证状态
	 * 
	 * @author 毛智东
	 * @date 2015年7月7日 上午9:38:38
	 * 
	 * @param userId
	 * @param aspect
	 * @return
	 */
	int deleteByUserIdAndAspect(Integer userId, VerfAspect aspect);
}