package priv.starfish.mall.dao.comn;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.comn.entity.User;

@IBatisSqlTarget
public interface UserDao extends BaseDao<User, Integer> {
	User selectById(Integer id);

	User selectByPhoneNo(String phoneNo);

	int insert(User user);

	int update(User user);

	int deleteById(Integer id);

	int deleteByPhoneNo(String phoneNo);

	/**
	 * 是否存在手机号为给定手机号的用户
	 * 
	 * @author koqiui
	 * @date 2015年8月18日 下午11:40:33
	 * 
	 * @param phoneNo
	 * @return
	 */
	boolean existsByPhoneNo(String phoneNo);

	/**
	 * 是否存在手机号为给定手机号而用户id不是给定的用户id的用户
	 * 
	 * @author koqiui
	 * @date 2015年8月18日 下午11:45:54
	 * 
	 * @param phoneNo
	 * @param notId
	 * @return
	 */
	boolean existsOtherByPhoneNo(String phoneNo, Integer notId);

	/**
	 * 根据邮箱查找用户
	 * 
	 * @author 毛智东
	 * @date 2015年7月6日 上午11:59:08
	 * 
	 * @param email
	 * @return
	 */
	User selectByEmail(String email);

	/**
	 * 分页查询用户列表
	 * @author guoyn
	 * @date 2015年8月20日 下午12:03:39
	 * 
	 * @param paginatedFilter like nickName，  =phoneNo
	 * @return PaginatedList<User>
	 */
	PaginatedList<User> selectByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 分页获取用户列表
	 * @author guoyn
	 * @date 2015年9月16日 上午11:54:42
	 * 
	 * @param paginatedFilter
	 * @return PaginatedList<User>
	 */
	PaginatedList<User> selectByFuzzyFilter(PaginatedFilter paginatedFilter);
	
	/**
	 * 获取用户列表(不分页)
	 * @author wangdi
	 * @date 2015年12月20日 上午11:54:42
	 * 
	 * @param filter
	 * @return List<User>
	 */
	List<User> selectByFilterNormal(MapContext filter);
}