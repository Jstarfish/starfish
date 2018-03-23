package priv.starfish.mall.service.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.model.Result;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.PasswordUtil;
import priv.starfish.common.util.StrUtil;
import priv.starfish.common.util.TypeUtil;
import priv.starfish.mall.dao.comn.BizParamDao;
import priv.starfish.mall.dao.comn.RoleDao;
import priv.starfish.mall.dao.comn.UserDao;
import priv.starfish.mall.dao.comn.UserLinkWayDao;
import priv.starfish.mall.dao.comn.UserRoleDao;
import priv.starfish.mall.dao.comn.UserVerfStatusDao;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.comn.dict.VerfAspect;
import priv.starfish.mall.comn.dto.RegionParts;
import priv.starfish.mall.comn.entity.BizParam;
import priv.starfish.mall.comn.entity.Role;
import priv.starfish.mall.comn.entity.User;
import priv.starfish.mall.comn.entity.UserLinkWay;
import priv.starfish.mall.comn.entity.UserVerfStatus;
import priv.starfish.mall.dao.interact.OnlineServeNoDao;
import priv.starfish.mall.dao.interact.OnlineServeRecordDao;
import priv.starfish.mall.interact.entity.OnlineServeNo;
import priv.starfish.mall.interact.entity.OnlineServeRecord;
import priv.starfish.mall.dao.mall.OperatorDao;
import priv.starfish.mall.service.SettingService;
import priv.starfish.mall.service.UserService;

@Service("userService")
public class UserServiceImpl extends BaseServiceImpl implements UserService {

	@Resource
	UserDao userDao;
	@Resource
	BizParamDao bizParamsDao;
	@Resource
	UserRoleDao userRoleDao;
	@Resource
	UserLinkWayDao userLinkWayDao;
	/** 在线客服号 */
	@Resource
	OnlineServeNoDao onlineSrvNoDao;
	/** 在线客服记录 */
	@Resource
	OnlineServeRecordDao onlineSrvRecDao;
	@Resource
	RoleDao roleDao;
	@Resource
	UserVerfStatusDao userVerfStatusDao;
	@Resource
	OperatorDao operatorDao;
	@Resource
	SettingService settingService;

	@Override
	public User getUserById(Integer userId) {
		return userDao.selectById(userId);
	}

	@Override
	public User getUserByPhoneNo(String phoneNo) {
		return userDao.selectByPhoneNo(phoneNo);
	}

	@Override
	public boolean existsUserByPhoneNo(String phoneNo) {
		return userDao.existsByPhoneNo(phoneNo);
	}

	@Override
	public boolean existsOtherUserByPhoneNo(String phoneNo, Integer notId) {
		return userDao.existsOtherByPhoneNo(phoneNo, notId);
	}

	@Override
	public User getUserByEmail(String email) {
		return userDao.selectByEmail(email);
	}

	/**
	 * 登录
	 */
	@Override
	public Result<?> doLogin(String phoneNo, String password) {
		Result<?> result = Result.newOne();
		// 登录错误次数Code
		String bizCode = "login.fail.lock.count";
		BizParam bizParams = bizParamsDao.selectById(bizCode);
		// 默认错误次数
		Integer maxFaileTimes = 5;
		if (bizParams != null)
			maxFaileTimes = Integer.valueOf(bizParams.getValue());
		User user = userDao.selectByPhoneNo(phoneNo);
		if (user != null) {
			if (StrUtil.hasText(user.getPassword())) {
				// 判断锁定
				if (user.getLocked()) {
					result.type = Type.warn;
					result.message = "用户已锁定，请解锁后重试";
				} else {
					String encrypted = user.getPassword();
					String saltStr = user.getSalt();
					String loginEncrypted = PasswordUtil.encrypt(password, saltStr);
					if (encrypted.equals(loginEncrypted)) {
						user.setLockTime(null);
						user.setFailTime(null);
						user.setFailCount(0);
						userDao.update(user);
						result.message = "验证通过";
					} else {
						Date date = new Date();
						Date failTime = user.getFailTime();
						if (failTime != null) {
							Calendar calendar = Calendar.getInstance();
							calendar.setTime(failTime);
							int day1 = calendar.get(Calendar.DAY_OF_YEAR);
							calendar.setTime(date);
							int day2 = calendar.get(Calendar.DAY_OF_YEAR);
							int days = day2 - day1;
							if (days > 0) {
								user.setFailTime(null);
								user.setFailCount(0);
							}
						}
						Integer faileTimes = 1;
						if (user.getFailCount() != null)
							faileTimes = user.getFailCount() + 1;
						// 判断错误次数
						if (faileTimes >= maxFaileTimes) {
							user.setLockTime(date);
							user.setLocked(true);
							user.setFailTime(null);
							user.setFailCount(0);

							result.type = Type.warn;
							result.message = "失败次数达到" + maxFaileTimes + "次，已被锁定";
						} else {
							user.setFailTime(date);
							user.setFailCount(faileTimes);

							result.type = Type.warn;
							result.message = "密码错误";
						}
						userDao.update(user);
					}
				}
			} else {
				result.type = Type.warn;
				result.message = "还没有设置密码，请设置完密码后登录";
			}
		} else {
			result.type = Type.warn;
			result.message = "手机号码错误";
		}
		return result;
	}

	@Override
	public boolean updateUser(User user) {
		return userDao.update(user) > 0;
	}

	@Override
	public boolean checkUserPasswordSet(Integer userId) {
		User user = userDao.selectById(userId);
		if (user == null) {
			return false;
		}
		if (StrUtil.isNullOrBlank(user.getPassword())) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean checkUserPayPasswordSet(Integer userId) {
		User user = userDao.selectById(userId);
		if (user == null) {
			return false;
		}
		if (StrUtil.isNullOrBlank(user.getPayPassword())) {
			return false;
		} else {
			return true;
		}

	}

	@Override
	public boolean verifyUserPassword(Integer userId, String password) {
		User user = userDao.selectById(userId);
		if (user == null) {
			return false;
		}

		String encrypted = user.getPassword();
		String saltStr = user.getSalt();
		String checkEncrypted = PasswordUtil.encrypt(password, saltStr);
		if (encrypted.equals(checkEncrypted)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean verifyUserPayPassword(Integer userId, String payPassword) {
		User user = userDao.selectById(userId);
		if (user == null) {
			return false;
		}

		String encrypted = user.getPayPassword();
		String saltStr = user.getSalt();
		String checkEncrypted = PasswordUtil.encrypt(payPassword, saltStr);
		if (encrypted.equals(checkEncrypted)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public PaginatedList<User> getUsersByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<User> userList = userDao.selectByFilter(paginatedFilter);
		return userList;
	}

	@Override
	public PaginatedList<User> getUsersByFilter(PaginatedFilter paginatedFilter, Boolean isFuzzyQry) {
		PaginatedList<User> userList = null;
		if (!isFuzzyQry) {
			userList = getUsersByFilter(paginatedFilter);
		} else {
			userList = userDao.selectByFuzzyFilter(paginatedFilter);
		}
		return userList;
	}

	@Override
	public List<User> getUsersByFilterNormal(MapContext filter) {
		return userDao.selectByFilterNormal(filter);
	}

	// ----------------------------------客服编号------------------------------------

	@Override
	public OnlineServeNo getOnlineServeNoById(Integer id) {
		return onlineSrvNoDao.selectById(id);
	}

	@Override
	public boolean saveOnlineServeNo(OnlineServeNo onlineServeNo) {
		return onlineSrvNoDao.insert(onlineServeNo) > 0;
	}

	@Override
	public boolean updateOnlineServeNo(OnlineServeNo onlineServeNo) {
		return onlineSrvNoDao.update(onlineServeNo) > 0;
	}

	@Override
	public boolean deleteOnlineServeNoById(Integer id) {
		return onlineSrvNoDao.deleteById(id) > 0;
	}

	@Override
	public boolean deleteOnlineServeNosByIds(List<Integer> ids) {
		return onlineSrvNoDao.deleteByIds(ids) > 0;
	}

	// ----------------------------------客服记录------------------------------------
	@Override
	public OnlineServeRecord getOnlineServeRecordByRecId(Long id) {
		return onlineSrvRecDao.selectById(id);
	}

	@Override
	public PaginatedList<OnlineServeRecord> getOnlineServeRecords(PaginatedFilter paginatedFilter) {
		return onlineSrvRecDao.selectByFilter(paginatedFilter);
	}

	@Override
	public List<OnlineServeRecord> getServeRecordByMemeberId(Integer memberId) {
		return onlineSrvRecDao.selectByMemberId(memberId);
	}

	@Override
	public boolean saveOnlineServeRecord(OnlineServeRecord onlineServeRecord) {
		return onlineSrvRecDao.insert(onlineServeRecord) > 0;
	}

	@Override
	public boolean updateOnlineServeRecord(OnlineServeRecord onlineServeRecord) {
		return onlineSrvRecDao.update(onlineServeRecord) > 0;
	}

	@Override
	public boolean deleteOnlineServeRecordByRecId(Long id) {
		return onlineSrvRecDao.deleteById(id) > 0;
	}

	// -------------------------------------------------用户验证状态表维护-----------------------------------------------------------

	@Override
	public boolean saveUserVerfStatus(UserVerfStatus userVerfStatus) {
		return userVerfStatusDao.insert(userVerfStatus) > 0;
	}

	@Override
	public boolean updateUserVerfStatus(UserVerfStatus userVerfStatus) {
		return userVerfStatusDao.update(userVerfStatus) > 0;
	}

	@Override
	public boolean deleteUserVerfStatusById(Integer id) {
		return userVerfStatusDao.deleteById(id) > 0;
	}

	@Override
	public boolean deleteUserVerfStatusByUserIdAndAspect(Integer userId, VerfAspect aspect) {
		return userVerfStatusDao.deleteByUserIdAndAspect(userId, aspect) > 0;
	}

	@Override
	public UserVerfStatus getUserVerfStatusByUserIdAndAspect(Integer userId, VerfAspect aspect) {
		return userVerfStatusDao.selectByUserIdAndAspect(userId, aspect);
	}

	@Override
	public PaginatedList<User> getUsersByScopeAndEntityIdAndFilter(AuthScope authScope, Integer entityId, PaginatedFilter paginatedFilter) {
		//
		PaginatedList<User> pageUsers = PaginatedList.newOne();
		List<User> users = pageUsers.getRows();
		//
		PaginatedList<Integer> pageUserIds = userRoleDao.selectUserIdsByScopeAndEntityIdAndFilter(authScope, entityId, paginatedFilter);
		List<Integer> userIds = pageUserIds.getRows();
		for (Integer userId : userIds) {
			User user = userDao.selectById(userId);
			List<Role> roles = userRoleDao.selectRolesByUserIdAndScopeAndEntityId(userId, authScope, entityId);
			user.setRoles(roles);
			users.add(user);
		}
		//
		pageUsers.setPagination(pageUserIds.getPagination());
		pageUsers.setRows(users);
		//
		return pageUsers;
	}

	@Override
	public List<User> getAllUsersByScopeAndEntityId(AuthScope authScope, Integer entityId, boolean includeRoles) {
		List<Integer> userIds = userRoleDao.selectAllUserIdsByScopeAndEntityId(authScope, entityId);
		//
		List<User> users = TypeUtil.newList(User.class);
		for (Integer userId : userIds) {
			User user = userDao.selectById(userId);
			if (includeRoles) {
				List<Role> roles = userRoleDao.selectRolesByUserIdAndScopeAndEntityId(userId, authScope, entityId);
				user.setRoles(roles);
			}
			users.add(user);
		}
		//
		return users;
	}

	@Override
	public List<UserLinkWay> getUserLinkWayByUserId(Integer userId) {
		return userLinkWayDao.selectByUserId(userId);
	}

	@Override
	public boolean updateUserLinkWay(UserLinkWay userLinkWay) {
		Integer regionId = userLinkWay.getRegionId();
		if (regionId != null) {
			// 获取地区全名
			RegionParts regionParts = settingService.getRegionPartsById(userLinkWay.getRegionId());
			userLinkWay.setRegionName(regionParts.fullName);
			userLinkWay.setProvinceId(regionParts.provinceId);
			userLinkWay.setCityId(regionParts.cityId);
			userLinkWay.setCountyId(regionParts.countyId);
			userLinkWay.setTownId(regionParts.townId);
		}
		return userLinkWayDao.update(userLinkWay) > 0;
	}

	@Override
	public boolean deleteUserLinkWayById(Integer id) {
		return userLinkWayDao.deleteById(id) > 0;
	}

	@Override
	public boolean addUserLinkWay(UserLinkWay userLinkWay) {
		Integer regionId = userLinkWay.getRegionId();
		if (regionId != null) {
			// 获取地区全名
			RegionParts regionParts = settingService.getRegionPartsById(userLinkWay.getRegionId());
			userLinkWay.setRegionName(regionParts.fullName);
			userLinkWay.setProvinceId(regionParts.provinceId);
			userLinkWay.setCityId(regionParts.cityId);
			userLinkWay.setCountyId(regionParts.countyId);
			userLinkWay.setTownId(regionParts.townId);
		}
		userLinkWay.setDefaulted(false);
		return userLinkWayDao.insert(userLinkWay) > 0;
	}

	@Override
	public UserLinkWay getUserLinkWayByUserIdAndAlias(Integer userId, String alias) {
		return userLinkWayDao.selectByUserIdAndAlias(userId, alias);
	}

	@Override
	public UserLinkWay getUserDefaultLinkWay(Integer userId, boolean defaulted) {
		return userLinkWayDao.selectDefaultByUserId(userId, defaulted);
	}

	@Override
	public boolean setUserDefaultLinkWay(Integer userId, Integer linkWayId) {
		Integer updateLinkWayId = userLinkWayDao.updateLinkWayByUserId(userId);
		boolean ok = false;
		if (updateLinkWayId > 0) {
			UserLinkWay userLinkWay = new UserLinkWay();
			userLinkWay.setId(linkWayId);
			userLinkWay.setDefaulted(true);
			ok = updateUserLinkWay(userLinkWay);
		}
		return ok;
	}
}
