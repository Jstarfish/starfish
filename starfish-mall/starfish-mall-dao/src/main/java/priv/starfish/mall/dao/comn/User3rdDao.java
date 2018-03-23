package priv.starfish.mall.dao.comn;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.comn.entity.User3rd;

@IBatisSqlTarget
public interface User3rdDao extends BaseDao<User3rd, Integer> {
	User3rd selectById(Integer id);

	User3rd selectByAppIdAndOpenId(Integer appId, String openId);

	Integer selectSysUserIdById(Integer id);

	Integer selectSysUserIdByAppIdAndOpenId(Integer appId, String openId);

	int insert(User3rd user3rd);

	int update(User3rd user3rd);

	int deleteById(Integer id);
}