package priv.starfish.mall.service;

import java.util.List;
import java.util.Map;

import priv.starfish.common.model.RelChangeInfo;
import priv.starfish.common.model.ScopeEntity;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.comn.dto.RoleDto;
import priv.starfish.mall.comn.entity.Module;
import priv.starfish.mall.comn.entity.Permission;
import priv.starfish.mall.comn.entity.Role;
import priv.starfish.mall.comn.entity.RolePermission;
import priv.starfish.mall.comn.entity.UserRole;

/**
 * 
 * @author koqiui
 * 
 *         与权限相关的服务（角色、权限等） 比如根据用户id获取所有相关的角色、权限
 */
public interface AuthxService extends BaseService {

	/**
	 * 根据模块id获取权限集合
	 * 
	 * @author guoyn
	 * @date 2015年5月13日 上午10:16:23
	 * 
	 * @param moduleId
	 * @return List<Permission>
	 */
	List<Permission> getPermissionsByModuleId(Integer moduleId);

	/**
	 * 根据禁用启用一个权限
	 * 
	 * @author guoyn
	 * @date 2015年5月13日 上午10:04:53
	 * 
	 * @param id
	 * @param status
	 * @return boolean
	 */
	boolean updatePermissionStatusById(Integer id, Boolean status);

	/**
	 * 根据status状态禁用启用多个权限
	 * 
	 * @author guoyn
	 * @date 2015年5月12日 下午6:19:28
	 * 
	 * @param ids
	 *            权限Id集合
	 * 
	 * @param status
	 *            禁用true 启用false
	 * 
	 * @return boolean
	 */
	boolean updatePermissionsStatusByIds(List<Integer> ids, Boolean status);

	/**
	 * 根据scope获取系统模块集合
	 * 
	 * @author guoyn
	 * @date 2015年5月13日 上午10:18:58
	 * 
	 * @param scope
	 * @return List<Integer>
	 */
	List<Integer> getModuleIdsByScope(AuthScope scope);

	/**
	 * 根据scope获取系统模块及相应权限集合
	 * 
	 * @author guoyn
	 * @date 2015年5月12日 下午6:12:02
	 * 
	 * @param scope
	 * @return List<Module>
	 */
	List<Module> getModulesByScope(AuthScope scope);

	/**
	 * 获取系统资源列表
	 * 
	 * @author 郭营
	 * @date 2015年5月13日 下午7:45:30
	 * @param tscope
	 * @return 系统资源列表
	 */

	List<Permission> getPermissionsByScope(AuthScope scope);

	/**
	 * 获取指定用户所有权限id列表
	 * 
	 * @author koqiui
	 * @date 2015年8月10日 下午4:27:42
	 * 
	 * @param userId
	 * @return
	 */
	List<Integer> getPermIdsByUserId(Integer userId);

	/**
	 * 获取给定用户的所有角色id列表
	 * 
	 * @author koqiui
	 * @date 2015年8月10日 下午4:34:46
	 * 
	 * @param userId
	 * @return
	 */
	List<Integer> getRoleIdsByUserId(Integer userId);

	/**
	 * 获取用户拥有的范围实体的权限id列表
	 * 
	 * @author 廖晓远
	 * @date 2015年8月4日 下午6:08:36
	 * 
	 * @param userId
	 *            用户I的
	 * @param scope
	 *            范围
	 * @param entityId
	 *            实体ID
	 * @return
	 */
	List<Integer> getPermIdsByUserIdAndScopeAndEntityId(Integer userId, AuthScope scope, Integer entityId);

	/**
	 * 获取用户拥有的系统或用户扩展范围实体的权限id列表
	 * 
	 * @author koqiui
	 * @date 2016年1月20日 上午10:37:09
	 * 
	 * @param userId
	 * @param scope
	 * @return
	 */
	List<Integer> getPermIdsByUserIdAndScope(Integer userId, AuthScope scope);

	/**
	 * 获取用户所有可用的scopeEnitiy(scope + entityId)
	 * 
	 * @author koqiui
	 * @date 2015年8月10日 下午8:34:09
	 * 
	 * @param userId
	 * @return
	 */
	List<ScopeEntity> getScopeEntitiesByUserId(Integer userId);

	// -------------------------------角色管理--------------------------------------

	/**
	 * 根据角色id查找角色
	 * 
	 * @author 毛智东
	 * @date 2015年5月14日 下午12:20:25
	 * 
	 * @param roleId
	 *            角色id
	 * @return 单个角色
	 */
	Role getRoleById(Integer roleId);

	/**
	 * 根据范围，名字，创建id查找角色
	 * 
	 * @author 毛智东
	 * @date 2015年5月13日 下午7:09:17
	 * 
	 * @param scope
	 *            范围
	 * @param name
	 *            角色名字
	 * @param entityId
	 *            创建者id
	 * @return 单个角色
	 */
	Role getRoleByScopeAndEntityIdAndName(AuthScope scope, Integer entityId, String name);

	/**
	 * 根据给定的scope返回内建的admin角色
	 * 
	 * @author koqiui
	 * @date 2015年11月11日 下午8:06:17
	 * 
	 * @param scope
	 * @return
	 */
	Role getBuiltInAdminRoleByScope(AuthScope scope);

	/**
	 * 得到角色的权限列表
	 * 
	 * @author 毛智东
	 * @date 2015年5月13日 上午9:29:59
	 * 
	 * @param roleId
	 *            角色id
	 * @return 角色权限列表
	 */
	List<RolePermission> getRolePermissionsByRoleId(Integer roleId);

	/**
	 * 添加角色权限(map)
	 * 
	 * @author 毛智东
	 * @date 2015年5月14日 上午9:40:27
	 * 
	 * @param addMap
	 *            <权限id，角色id>
	 * @return true or false
	 */
	boolean saveRolePermMap(Map<Integer, Integer> addMap);

	/**
	 * 删除角色权限(map)
	 * 
	 * @author 毛智东
	 * @date 2015年5月13日 下午2:52:41
	 * 
	 * @param deleteList
	 *            <权限id，角色id>
	 * @return true or false
	 */
	boolean deleteRolePermMap(Map<Integer, Integer> deleteMap);

	/**
	 * 批量更新角色权限
	 * 
	 * @author 毛智东
	 * @date 2015年5月15日 下午12:33:19
	 * 
	 * @param relChangeInfos
	 * @return true or false
	 */
	boolean updateRolePermsBatch(List<RelChangeInfo> relChangeInfos);

	// -----------------------------------角色列表---------------------------------------------------

	/**
	 * 分页查询角色列表
	 * 
	 * @author 毛智东
	 * @date 2015年6月2日 下午5:37:52
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	List<Role> getRolesByScopeAndEntityId(AuthScope scope, Integer entityId, Boolean includeSysSet);

	/**
	 * 批量删除角色
	 * 
	 * @author 毛智东
	 * @date 2015年6月2日 下午5:38:11
	 * 
	 * @param ids
	 * @return
	 */
	boolean deleteRolesByIds(List<Integer> ids);

	/**
	 * 保存角色
	 * 
	 * @author 毛智东
	 * @date 2015年6月3日 下午5:26:53
	 * 
	 * @param roleDto
	 * @return
	 */
	boolean saveRole(RoleDto roleDto);

	/**
	 * 修改角色
	 * 
	 * @author 毛智东
	 * @date 2015年6月3日 下午5:27:35
	 * 
	 * @param roleDto
	 * @return
	 */
	boolean updateRole(RoleDto roleDto);

	/**
	 * 删除角色
	 * 
	 * @author 毛智东
	 * @date 2015年6月3日 下午5:27:59
	 * 
	 * @param roleId
	 * @return
	 */
	boolean deleteRole(Integer roleId);

	/**
	 * 根据角色Ids查询权限ids
	 * 
	 * @author zjl
	 * @date 2015年6月5日 下午12:10:39
	 * 
	 * @param roleIds
	 *            角色Id
	 * @return List<Integer>
	 */
	List<Integer> getPermIdsByRoleIds(List<Integer> roleIds);

	/**
	 * 根据角色Id查询相应的权限列表及相应的模块信息
	 * 
	 * @author guoyn
	 * @date 2015年8月12日 上午10:28:08
	 * 
	 * @param roleId
	 * @return List<Permission>
	 */
	List<Permission> getPermissonsByRoleId(Integer roleId);

	/**
	 * 通过用户ID获取角色列表
	 * 
	 * @author 廖晓远
	 * @date 2015-5-13 下午3:34:29
	 * @param userId
	 *            用户ID
	 * @return List<Role> 角色列表
	 */
	List<Role> getRolesByUserId(Integer userId);

	/**
	 * 获取用户拥有的范围实体的角色列表
	 * 
	 * @author 廖晓远
	 * @date 2015-6-10 下午12:23:14
	 * 
	 * @param userId
	 * @param scope
	 * @param entityId
	 * @return
	 */
	List<Role> getRolesByUserIdAndScopeAndEntityId(Integer userId, AuthScope scope, Integer entityId);

	/**
	 * 获取用户拥有的系统或用户扩展范围实体的角色列表
	 * 
	 * @author koqiui
	 * @date 2016年1月20日 下午12:30:36
	 * 
	 * @param userId
	 * @param scope
	 * @return
	 */
	List<Role> getRolesByUserIdAndScopeAndEntityId(Integer userId, AuthScope scope);

	// -----------------------------------UserRole---------------------------------------------------

	/**
	 * 根据roleId查找UserRole列表
	 * 
	 * @author 毛智东
	 * @date 2015年8月22日 下午5:33:28
	 * 
	 * @param roleId
	 * @return
	 */
	List<UserRole> getUserRolesByRoleId(Integer roleId);

	/**
	 * 根据roleIds查找UserRole列表
	 * 
	 * @author 毛智东
	 * @date 2015年8月22日 下午5:33:28
	 * 
	 * @param roleId
	 * @return
	 */
	List<UserRole> getUserRolesByRoleIds(List<Integer> roleIds);

	/**
	 * 删除该角色的所有用户关联
	 * 
	 * @author 毛智东
	 * @date 2015年8月22日 下午5:49:17
	 * 
	 * @param roleId
	 * @return
	 */
	boolean deleteUserRoleByRoleId(Integer roleId);

	/**
	 * 批量删除该角色的所有用户关联
	 * 
	 * @author 毛智东
	 * @date 2015年8月22日 下午5:50:03
	 * 
	 * @param roleIds
	 * @return
	 */
	boolean deleteUserRoleByRoleIds(List<Integer> roleIds);

	/**
	 * 通过用户ID获取用户角色关联
	 * 
	 * @author 廖晓远
	 * @date 2015-6-9 下午8:22:16
	 * 
	 * @param userId
	 * @return
	 */
	List<UserRole> getUserRolesByUserId(Integer userId);

	/**
	 * 更新用户认证范围下的某实体id下的角色
	 * 
	 * @author guoyn
	 * @date 2015年8月20日 上午10:45:51
	 * 
	 * @param relChangeInfo
	 *            用户id，用户相关的需要添加的角色id列表，用户相关的需要删除的角色id列表
	 * @param scope
	 *            认证范围
	 * @param entityId
	 *            实体id
	 * @return boolean
	 */
	boolean updateUserRoles(RelChangeInfo relChangeInfo, AuthScope scope, Integer entityId);

	/**
	 * 更新系统或用户扩展范围下的实体的角色
	 * 
	 * @author koqiui
	 * @date 2016年1月20日 下午12:40:06
	 * 
	 * @param relChangeInfo
	 * @param scope
	 * @return
	 */
	boolean updateUserRoles(RelChangeInfo relChangeInfo, AuthScope scope);

	/**
	 * 解除用户的给定认证范围下的某实体id的角色
	 * 
	 * @author guoyn
	 * @date 2015年8月20日 上午10:56:00
	 * 
	 * @param userId
	 * @param authScope
	 * @param entityId
	 * @return boolean
	 */
	boolean unbindUserRolesByUserIdAndScopeAndEntityId(Integer userId, AuthScope authScope, Integer entityId);

	/**
	 * 解除用户的用户扩展范围下的实体的角色
	 * 
	 * @author koqiui
	 * @date 2016年1月20日 下午12:47:23
	 * 
	 * @param userId
	 * @param authScope
	 * @return
	 */
	boolean unbindUserRolesByUserIdAndScopeAndEntityId(Integer userId, AuthScope authScope);

}
