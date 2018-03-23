package priv.starfish.mall.dao.comn;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.comn.entity.UserLinkWay;

@IBatisSqlTarget
public interface UserLinkWayDao extends BaseDao<UserLinkWay, Integer> {
	UserLinkWay selectById(Integer id);

	UserLinkWay selectByUserIdAndAlias(Integer userId, String alias);

	int insert(UserLinkWay userLinkWay);

	int update(UserLinkWay userLinkWay);

	int deleteById(Integer id);

	List<UserLinkWay> selectByUserId(Integer userId);
	
	UserLinkWay selectDefaultByUserId(Integer userId, boolean defaulted);

	int updateLinkWayByUserId(Integer userId);
}