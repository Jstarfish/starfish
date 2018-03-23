package priv.starfish.mall.service;

import java.util.List;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.model.Result;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.comn.dict.VerfAspect;
import priv.starfish.mall.comn.entity.User;
import priv.starfish.mall.comn.entity.UserLinkWay;
import priv.starfish.mall.comn.entity.UserVerfStatus;
import priv.starfish.mall.interact.entity.OnlineServeNo;
import priv.starfish.mall.interact.entity.OnlineServeRecord;

/**
 * 判断密码有效性、登录、登出、个人及相关信息
 * 
 * @author koqiui
 * 
 */
public interface UserService extends BaseService {

	// ----------------------------------用户------------------------------------
	/**
	 * 通过用户Id获取用户
	 * 
	 * @author 廖晓远
	 * @date 2015-5-21 下午7:34:26
	 * 
	 * @param userId
	 * @return
	 */
	User getUserById(Integer userId);

	/**
	 * 通过手机号码获取用户
	 * 
	 * @author 廖晓远
	 * @date 2015-5-13 下午3:34:20
	 * @param phoneNo
	 *            手机号码
	 * @return User 用户信息
	 */
	User getUserByPhoneNo(String phoneNo);

	/**
	 * 是否存在手机号为给定手机号的用户
	 * 
	 * @author koqiui
	 * @date 2015年8月18日 下午11:40:33
	 * 
	 * @param phoneNo
	 * @return
	 */
	boolean existsUserByPhoneNo(String phoneNo);

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
	boolean existsOtherUserByPhoneNo(String phoneNo, Integer notId);

	/**
	 * 根据邮箱查找用户
	 * 
	 * @author 毛智东
	 * @date 2015年7月6日 下午1:16:16
	 * 
	 * @param email
	 * @return
	 */
	User getUserByEmail(String email);

	/**
	 * 登录验证
	 * 
	 * @author 廖晓远
	 * @date 2015-5-13 下午3:33:37
	 * @param phoneNo
	 *            手机号码
	 * @param password
	 *            密码
	 * @return Result<?> 结果Map
	 */
	Result<?> doLogin(String phoneNo, String password);

	/**
	 * 更新用户信息
	 * 
	 * @author 王少辉
	 * @date 2015年5月30日 下午4:22:32
	 * 
	 * @param user
	 *            用户信息
	 * @return 返回更新结果
	 */
	boolean updateUser(User user);

	/**
	 * 检验用户密码是否已初始化
	 * 
	 * @author 郝江奎
	 * @date 2015年12月1日 上午12:03:10
	 * @return 返回检验结果
	 */
	boolean checkUserPasswordSet(Integer userId);

	/**
	 * 检验用户支付密码是否已初始化
	 * 
	 * @author 郝江奎
	 * @date 2015年12月1日 上午12:03:10
	 * @return 返回检验结果
	 */
	boolean checkUserPayPasswordSet(Integer userId);

	/**
	 * 验证用户密码
	 * 
	 * @author 王少辉
	 * @date 2015年5月30日 下午4:53:10
	 * @return 返回检验结果
	 */
	boolean verifyUserPassword(Integer userId, String password);

	/**
	 * 验证用户支付密码
	 * 
	 * @author 郝江奎
	 * @date 2015年11月19日 下午1:53:10
	 * @return 返回检验结果
	 */
	boolean verifyUserPayPassword(Integer userId, String payPassword);

	/**
	 * 分页获取用户列表
	 * 
	 * @author guoyn
	 * @date 2015年8月20日 下午12:01:33
	 * 
	 * @param paginatedFilter
	 *            like nickName， = phoneNo
	 * @return PaginatedList<User>
	 */
	PaginatedList<User> getUsersByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 分页获取用户列表
	 * 
	 * @author guoyn
	 * @date 2015年9月16日 上午11:54:42
	 * 
	 * @param paginatedFilter
	 * @param isFuzzyQry
	 *            是否全部模糊查询: true-->like nickName， like phoneNo, false:like nickName， = phoneNo
	 * @return PaginatedList<User>
	 */
	PaginatedList<User> getUsersByFilter(PaginatedFilter paginatedFilter, Boolean isFuzzyQry);

	/**
	 * 获取用户列表(不分页)
	 * 
	 * @author wangdi
	 * @date 2015年12月20日 上午11:54:42
	 * 
	 * @param filter
	 * @return List<User>
	 */
	List<User> getUsersByFilterNormal(MapContext filter);

	// ----------------------------------客服编号------------------------------------

	/**
	 * 根据客服id获取客服
	 * 
	 * @author 王少辉
	 * @date 2015年6月3日 下午12:19:37
	 * 
	 * @param id
	 *            客服编号
	 * @return 返回客服
	 */
	OnlineServeNo getOnlineServeNoById(Integer id);

	/**
	 * 添加客服
	 * 
	 * @author 王少辉
	 * @date 2015年6月3日 下午12:27:56
	 * 
	 * @param onlineServeNo
	 *            客服信息
	 * @return 返回添加结果
	 */
	boolean saveOnlineServeNo(OnlineServeNo onlineServeNo);

	/**
	 * 修改客服
	 * 
	 * @author 王少辉
	 * @date 2015年6月3日 下午12:27:56
	 * 
	 * @param onlineServeNo
	 *            客服信息
	 * @return 返回修改结果
	 */
	boolean updateOnlineServeNo(OnlineServeNo onlineServeNo);

	/**
	 * 根据客服序号id删除客服
	 * 
	 * @author 郝江奎
	 * @date 2015年9月8日 下午5:38:37
	 * 
	 * @param id
	 *            客服序号
	 * @return 返回删除结果
	 */
	boolean deleteOnlineServeNoById(Integer id);

	/**
	 * 根据客服序号ids删除客服
	 * 
	 * @author 郝江奎
	 * @date 2015年9月8日 下午5:40:37
	 * 
	 * @param ids
	 *            客服序号
	 * @return 返回删除结果
	 */
	boolean deleteOnlineServeNosByIds(List<Integer> ids);

	// ----------------------------------客服记录------------------------------------
	/**
	 * 根据客服记录id获取客服记录
	 * 
	 * @author 王少辉
	 * @date 2015年6月3日 下午12:37:46
	 * 
	 * @param id
	 *            客服记录id
	 * @return 返回客服记录
	 */
	OnlineServeRecord getOnlineServeRecordByRecId(Long id);

	/**
	 * 分页查询客服记录
	 * 
	 * @author 王少辉
	 * @date 2015年6月3日 下午1:00:33
	 * 
	 * @param paginatedFilter
	 *            过滤条件
	 * @return 返回分页查询客服记录结果
	 */
	PaginatedList<OnlineServeRecord> getOnlineServeRecords(PaginatedFilter paginatedFilter);

	/**
	 * 根据会员编号获取与客服对话的所有内容
	 * 
	 * @author 王少辉
	 * @date 2015年6月4日 下午1:25:50
	 * 
	 * @param memberId
	 *            会员编号
	 * @return 返回所有对话
	 */
	List<OnlineServeRecord> getServeRecordByMemeberId(Integer memberId);

	/**
	 * 添加客服记录
	 * 
	 * @author 王少辉
	 * @date 2015年6月3日 下午12:37:52
	 * 
	 * @param onlineServeRecord
	 *            客服记录
	 * @return 返回添加结果
	 */
	boolean saveOnlineServeRecord(OnlineServeRecord onlineServeRecord);

	/**
	 * 修改客服记录
	 * 
	 * @author 王少辉
	 * @date 2015年6月3日 下午12:37:56
	 * 
	 * @param onlineServeRecord
	 *            客服记录
	 * @return 返回修改结果
	 */
	boolean updateOnlineServeRecord(OnlineServeRecord onlineServeRecord);

	/**
	 * 根据客服编号删除客服记录
	 * 
	 * @author 王少辉
	 * @date 2015年6月3日 下午12:38:00
	 * 
	 * @param id
	 *            客户记录id
	 * @return 返回删除结果
	 */
	boolean deleteOnlineServeRecordByRecId(Long id);

	// -------------------------------------------------用户验证状态表维护-----------------------------------------------------------

	/**
	 * 保存用户验证状态
	 * 
	 * @author 毛智东
	 * @date 2015年7月7日 上午9:30:53
	 * 
	 * @param userVerfStatus
	 * @return
	 */
	boolean saveUserVerfStatus(UserVerfStatus userVerfStatus);

	/**
	 * 修改用户验证状态
	 * 
	 * @author 毛智东
	 * @date 2015年7月7日 上午9:30:57
	 * 
	 * @param userVerfStatus
	 * @return
	 */
	boolean updateUserVerfStatus(UserVerfStatus userVerfStatus);

	/**
	 * 根据id删除用户验证状态
	 * 
	 * @author 毛智东
	 * @date 2015年7月7日 上午9:31:02
	 * 
	 * @param id
	 * @return
	 */
	boolean deleteUserVerfStatusById(Integer id);

	/**
	 * 根据用户id和方面删除用户验证状态
	 * 
	 * @author 毛智东
	 * @date 2015年7月7日 上午9:31:07
	 * 
	 * @param userId
	 * @param aspect
	 * @return
	 */
	boolean deleteUserVerfStatusByUserIdAndAspect(Integer userId, VerfAspect aspect);

	/**
	 * 根据用户id和方面查找用户验证状态
	 * 
	 * @author 毛智东
	 * @date 2015年7月7日 上午9:31:16
	 * 
	 * @param userId
	 * @param aspect
	 * @return
	 */
	UserVerfStatus getUserVerfStatusByUserIdAndAspect(Integer userId, VerfAspect aspect);

	/**
	 * 根据scope和entityId分页查询用户列表
	 * 
	 * @author guoyn
	 * @date 2015年8月9日 下午3:27:20
	 * 
	 * @param authScope
	 * @param entityId
	 * @param paginatedFilter
	 *            like nickName OR =phoneNo
	 * @return PaginatedList<User>
	 */
	PaginatedList<User> getUsersByScopeAndEntityIdAndFilter(AuthScope authScope, Integer entityId, PaginatedFilter paginatedFilter);

	/**
	 * 根据scope和entityId返回所有用户列表
	 * 
	 * @author koqiui
	 * @date 2015年12月17日 上午12:32:33
	 * 
	 * @param authScope
	 * @param entityId
	 * @param includeRoles
	 *            是否包含角色信息
	 * @return
	 */
	List<User> getAllUsersByScopeAndEntityId(AuthScope authScope, Integer entityId, boolean includeRoles);

	List<UserLinkWay> getUserLinkWayByUserId(Integer userId);

	boolean addUserLinkWay(UserLinkWay userLinkWay);

	boolean updateUserLinkWay(UserLinkWay userLinkWay);

	boolean deleteUserLinkWayById(Integer linkWayId);

	UserLinkWay getUserLinkWayByUserIdAndAlias(Integer userId, String alias);

	/**
	 * 获取用户默认的联系方式
	 * 
	 * @author 郝江奎
	 * @date 2015年12月17日 下午4:07:14
	 * 
	 * @param userId
	 * @return
	 */
	UserLinkWay getUserDefaultLinkWay(Integer userId, boolean defaulted);

	boolean setUserDefaultLinkWay(Integer userId, Integer linkWayId);
}
