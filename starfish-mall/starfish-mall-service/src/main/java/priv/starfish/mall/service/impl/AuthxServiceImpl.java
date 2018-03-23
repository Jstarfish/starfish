package priv.starfish.mall.service.impl;

import org.springframework.stereotype.Service;
import priv.starfish.common.exception.InvalidActionException;
import priv.starfish.common.jms.SimpleMessage;
import priv.starfish.common.model.RelChangeInfo;
import priv.starfish.common.model.ScopeEntity;
import priv.starfish.common.util.TypeUtil;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.comn.dto.RoleDto;
import priv.starfish.mall.comn.entity.*;
import priv.starfish.mall.dao.comn.*;
import priv.starfish.mall.service.AuthxService;
import priv.starfish.mall.service.BaseConst;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service("authxService")
public class AuthxServiceImpl extends BaseServiceImpl implements AuthxService {

	@javax.annotation.Resource
    PermissionDao permissionDao;

	@javax.annotation.Resource
	RolePermissionDao rolePermissionDao;

	@javax.annotation.Resource
    ModuleDao moduleDao;

	@javax.annotation.Resource
    RoleDao roleDao;

	@javax.annotation.Resource
	ResourceDao resourceDao;

	@javax.annotation.Resource
    SiteResourceDao siteResourceDao;

	@javax.annotation.Resource
    SiteFunctionDao siteFunctionDao;

	@javax.annotation.Resource
	SiteModuleDao siteModuleDao;

	@javax.annotation.Resource
	UserRoleDao userRoleDao;

	@javax.annotation.Resource
	UserDao userDao;

	private void sendPermissionChangeQueueMessage() {
		SimpleMessage messageToSend = SimpleMessage.newOne();
		messageToSend.subject = BaseConst.SubjectNames.PERMISSION;
		simpleMessageSender.sendQueueMessage(BaseConst.QueueNames.CACHE, messageToSend);
	}

	/**
	 * 根据模块id获取权限集合
	 */
	@Override
	public List<Permission> getPermissionsByModuleId(Integer moduleId) {
		return permissionDao.selectByModuleId(moduleId);
	}

	/**
	 * 禁用启用一个权限
	 */
	@Override
	public boolean updatePermissionStatusById(Integer id, Boolean status) {
		int count = permissionDao.updateStatusById(id, status);
		if (status) {
			rolePermissionDao.deleteByPermId(id);
		}
		boolean success = count > 0;
		if (success) {
			this.sendPermissionChangeQueueMessage();
		}
		return success;
	}

	@Override
	public List<Integer> getModuleIdsByScope(AuthScope scope) {
		return permissionDao.selectModuleIdsByScope(scope);
	}

	@Override
	public List<Module> getModulesByScope(AuthScope scope) {
		return moduleDao.selectByScope(scope);
	}

	@Override
	public boolean updatePermissionsStatusByIds(List<Integer> ids, Boolean disabled) {
		int count = permissionDao.updateStatusByIds(ids, disabled);
		if (disabled) {
			rolePermissionDao.deleteByPermIds(ids);
		}
		boolean success = count > 0;
		if (success) {
			this.sendPermissionChangeQueueMessage();
		}
		return success;
	}

	/**
	 * 获取系统资源列表
	 * 
	 * @author 郭营
	 * @date 2015年5月13日 下午7:45:30
	 * @return 系统资源列表
	 */
	@Override
	public List<Permission> getPermissionsByScope(AuthScope scope) {
		return permissionDao.selectByScope(scope);
	}

	@Override
	public List<Integer> getRoleIdsByUserId(Integer userId) {
		return userRoleDao.selectRoleIdsByUserId(userId);
	}

	@Override
	public List<Integer> getPermIdsByUserId(Integer userId) {
		List<Integer> roleIds = userRoleDao.selectRoleIdsByUserId(userId);
		return rolePermissionDao.selectPermIdsByRoleIds(roleIds);
	}

	@Override
	public List<Integer> getPermIdsByUserIdAndScopeAndEntityId(Integer userId, AuthScope scope, Integer entityId) {
		if (AuthScope.sys.equals(scope)) {
			// 纠正系统管理实体对象id
			entityId = UserRole.SysEntityId;
		} else if (scope.isUserExtended()) {
			entityId = userId;
		}
		List<Role> roles = userRoleDao.selectRolesByUserIdAndScopeAndEntityId(userId, scope, entityId);
		List<Integer> roleIds = new ArrayList<Integer>();
		for (Role role : roles) {
			roleIds.add(role.getId());
		}
		return rolePermissionDao.selectPermIdsByRoleIds(roleIds);
	}

	@Override
	public List<Integer> getPermIdsByUserIdAndScope(Integer userId, AuthScope scope) {
		return this.getPermIdsByUserIdAndScopeAndEntityId(userId, scope, null);
	}

	@Override
	public List<ScopeEntity> getScopeEntitiesByUserId(Integer userId) {
		return userRoleDao.selectScopeEntitiesByUserId(userId);
	}

	// -------------------------------角色管理--------------------------------------

	@Override
	public Role getRoleById(Integer roleId) {
		return roleDao.selectById(roleId);
	}

	@Override
	public Role getRoleByScopeAndEntityIdAndName(AuthScope scope, Integer entityId, String name) {
		return roleDao.selectByScopeAndEntityIdAndName(scope, entityId, name);
	}

	@Override
	public List<RolePermission> getRolePermissionsByRoleId(Integer roleId) {
		return rolePermissionDao.selectByRoleId(roleId);
	}

	@Override
	public boolean saveRolePermMap(Map<Integer, Integer> addMap) {
		int number = 0;
		for (Map.Entry<Integer, Integer> entry : addMap.entrySet()) {
			RolePermission rolePermission = new RolePermission();
			rolePermission.setRoleId(entry.getValue());
			rolePermission.setPermId(entry.getKey());
			number += rolePermissionDao.insert(rolePermission);
		}
		boolean success = number == addMap.size();
		if (success) {
			this.sendPermissionChangeQueueMessage();
		}
		return success;
	}

	@Override
	public boolean deleteRolePermMap(Map<Integer, Integer> deleteMap) {
		int count = 0;
		for (Map.Entry<Integer, Integer> entry : deleteMap.entrySet()) {
			count += rolePermissionDao.deleteByRoleIdAndPermId(entry.getValue(), entry.getKey());
		}
		boolean success = count == deleteMap.size();
		if (success) {
			this.sendPermissionChangeQueueMessage();
		}
		return success;
	}

	@Override
	public boolean updateRolePermsBatch(List<RelChangeInfo> relChangeInfos) {
		boolean success = true;
		for (RelChangeInfo relChangeInfo : relChangeInfos) {
			Integer roleId = relChangeInfo.getMainId();
			List<Integer> subIdsAdded = relChangeInfo.getSubIdsAdded();
			List<Integer> subIdsDeleted = relChangeInfo.getSubIdsDeleted();
			for (Integer permId : subIdsAdded) {
				RolePermission rolePerm = new RolePermission();
				rolePerm.setRoleId(roleId);
				rolePerm.setPermId(permId);
				success = rolePermissionDao.insert(rolePerm) == 1 && success;
			}
			for (Integer permId : subIdsDeleted) {
				success = rolePermissionDao.deleteByRoleIdAndPermId(roleId, permId) == 1 && success;
			}
		}
		if (success) {
			this.sendPermissionChangeQueueMessage();
		}
		return success;
	}

	// -----------------------------------角色列表---------------------------------------------------

	@Override
	public List<Role> getRolesByScopeAndEntityId(AuthScope scope, Integer entityId, Boolean includeSysSet) {
		return roleDao.selectByScopeAndEntityId(scope, entityId, includeSysSet);
	}

	@Override
	public boolean deleteRolesByIds(List<Integer> roleIds) {
		boolean success = true;
		int size = rolePermissionDao.selectByRoleIds(roleIds).size();
		success = rolePermissionDao.deleteByRoleIds(roleIds) == size && success;
		success = roleDao.deleteByIds(roleIds) > 0 && success;
		if (success) {
			this.sendPermissionChangeQueueMessage();
		}
		return success;
	}

	@Override
	public boolean saveRole(RoleDto roleDto) {
		boolean success = true;
		Role role = new Role();
		List<Integer> subIdsAdded = roleDto.getSubIdsAdded();
		TypeUtil.copyProperties(roleDto, role);
		// 禁止插入admin特殊名称的角色
		if (Role.ADMIN_NAME.equals(role.getName().trim())) {
			throw new InvalidActionException("不能创建名称为\"" + Role.ADMIN_NAME + "\"的角色");
		}
		success = roleDao.insert(role) > 0 && success;
		for (Integer permId : subIdsAdded) {
			RolePermission rolePerm = new RolePermission();
			rolePerm.setRoleId(role.getId());
			rolePerm.setPermId(permId);
			success = rolePermissionDao.insert(rolePerm) > 0 && success;
		}
		TypeUtil.copyProperties(role, roleDto);
		if (success) {
			this.sendPermissionChangeQueueMessage();
		}
		return success;
	}

	@Override
	public boolean updateRole(RoleDto roleDto) {
		boolean success = true;
		Integer roleId = roleDto.getId();
		//
		Role dbRole = roleDao.selectById(roleId);
		if (!dbRole.getScope().equals(roleDto.getScope()) || dbRole.getEntityId().equals(roleDto.getEntityId())) {
			throw new InvalidActionException("不能更新其他范围、其他实体所属的角色");
		}
		//
		List<Integer> subIdsAdded = roleDto.getSubIdsAdded();
		List<Integer> subIdsDeleted = roleDto.getSubIdsDeleted();
		Role role = new Role();
		TypeUtil.copyProperties(roleDto, role);

		success = roleDao.update(role) > 0 && success;
		for (Integer permId : subIdsAdded) {
			RolePermission rolePerm = new RolePermission();
			rolePerm.setRoleId(roleId);
			rolePerm.setPermId(permId);
			success = rolePermissionDao.insert(rolePerm) > 0 && success;
		}
		for (Integer permId : subIdsDeleted) {
			success = rolePermissionDao.deleteByRoleIdAndPermId(roleId, permId) == subIdsDeleted.size() && success;
		}
		if (success) {
			this.sendPermissionChangeQueueMessage();
		}
		return success;
	}

	@Override
	public boolean deleteRole(Integer roleId) {
		boolean success = true;
		Role dbRole = roleDao.selectById(roleId);
		//
		if (dbRole.getBuiltIn() == true) {
			throw new InvalidActionException("不能删除内建的角色");
		}
		//
		int size = rolePermissionDao.selectIdsByRoleId(roleId).size();
		success = rolePermissionDao.deleteByRoleId(roleId) == size && success;
		success = roleDao.deleteById(roleId) > 0 && success;
		if (success) {
			this.sendPermissionChangeQueueMessage();
		}
		return success;
	}

	@Override
	public Role getBuiltInAdminRoleByScope(AuthScope scope) {
		return roleDao.selectBuiltInAdminByScope(scope);
	}

	@Override
	public List<Integer> getPermIdsByRoleIds(List<Integer> roleIds) {
		return rolePermissionDao.selectPermIdsByRoleIds(roleIds);
	}

	@Override
	public List<Permission> getPermissonsByRoleId(Integer roleId) {
		//
		List<Permission> perms = new ArrayList<Permission>(0);
		//
		List<Integer> permIds = rolePermissionDao.selectPermIdsByRoleIdOrderByModuleId(roleId, false);
		for (Integer permId : permIds) {
			Permission permission = permissionDao.selectById(permId);
			//
			Integer moduleId = permission.getModuleId();
			//
			Module module = moduleDao.selectById(moduleId);
			//
			permission.setModule(module);
			//
			perms.add(permission);
		}
		return perms;
	}

	@Override
	public List<Role> getRolesByUserId(Integer userId) {
		return userRoleDao.selectRolesByUserId(userId);
	}

	@Override
	public List<Role> getRolesByUserIdAndScopeAndEntityId(Integer userId, AuthScope scope, Integer entityId) {
		if (AuthScope.sys.equals(scope)) {
			// 纠正系统管理实体对象id
			entityId = UserRole.SysEntityId;
		} else if (scope.isUserExtended()) {
			entityId = userId;
		}
		return userRoleDao.selectRolesByUserIdAndScopeAndEntityId(userId, scope, entityId);
	}

	@Override
	public List<Role> getRolesByUserIdAndScopeAndEntityId(Integer userId, AuthScope scope) {
		return this.getRolesByUserIdAndScopeAndEntityId(userId, scope, null);
	}

	@Override
	public List<UserRole> getUserRolesByRoleId(Integer roleId) {
		return userRoleDao.selectByRoleId(roleId);
	}

	@Override
	public boolean deleteUserRoleByRoleId(Integer roleId) {
		return userRoleDao.deleteByRoleId(roleId) > 0;
	}

	@Override
	public boolean deleteUserRoleByRoleIds(List<Integer> roleIds) {
		return userRoleDao.deleteByRoleIds(roleIds) > 0;
	}

	@Override
	public List<UserRole> getUserRolesByRoleIds(List<Integer> roleIds) {
		return userRoleDao.selectByRoleIds(roleIds);
	}

	@Override
	public List<UserRole> getUserRolesByUserId(Integer userId) {
		return userRoleDao.selectByUserId(userId);
	}

	@Override
	public boolean updateUserRoles(RelChangeInfo relChangeInfo, AuthScope scope, Integer entityId) {
		boolean flag = true;
		Integer userId = relChangeInfo.getMainId();
		//
		if (AuthScope.sys.equals(scope)) {
			// 纠正系统管理实体对象id
			entityId = UserRole.SysEntityId;
		} else if (scope.isUserExtended()) {
			entityId = userId;
		}
		//
		List<Integer> addList = relChangeInfo.getSubIdsAdded();
		List<Integer> delList = relChangeInfo.getSubIdsDeleted();
		if (delList.size() > 0) {
			flag = userRoleDao.deleteByUserIdAndRoleIds(userId, delList) > 0 && flag;
		}
		for (Integer roleId : addList) {
			UserRole userRole = new UserRole();
			userRole.setUserId(userId);
			userRole.setScope(scope);
			userRole.setRoleId(roleId);
			userRole.setEntityId(entityId);

			flag = userRoleDao.insert(userRole) > 0 && flag;
		}
		return flag;
	}

	@Override
	public boolean updateUserRoles(RelChangeInfo relChangeInfo, AuthScope scope) {
		return this.updateUserRoles(relChangeInfo, scope, null);
	}

	@Override
	public boolean unbindUserRolesByUserIdAndScopeAndEntityId(Integer userId, AuthScope authScope, Integer entityId) {
		if (AuthScope.sys.equals(authScope)) {
			// 纠正系统管理实体对象id
			entityId = UserRole.SysEntityId;
		} else if (authScope.isUserExtended()) {
			entityId = userId;
		}
		int count = userRoleDao.deleteByUserIdAndScopeAndEntityId(userId, authScope, entityId);
		return count > 0;
	}

	@Override
	public boolean unbindUserRolesByUserIdAndScopeAndEntityId(Integer userId, AuthScope authScope) {
		return this.unbindUserRolesByUserIdAndScopeAndEntityId(userId, authScope, null);
	}

}
