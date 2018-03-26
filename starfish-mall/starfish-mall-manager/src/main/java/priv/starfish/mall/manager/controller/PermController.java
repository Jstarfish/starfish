package priv.starfish.mall.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.model.JqGridPage;
import priv.starfish.common.model.RelChangeInfo;
import priv.starfish.common.model.Result;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.EnumUtil;
import priv.starfish.common.util.JsonUtil;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.TypeUtil;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.comn.dto.RoleDto;
import priv.starfish.mall.comn.entity.*;
import priv.starfish.mall.web.base.BaseController;
import priv.starfish.mall.service.BaseConst;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Remark("权限、角色、系统模块相关处理")
@Controller
@RequestMapping(value = "/perm")
public class PermController extends BaseController {

	/**
	 * 跳转到权限管理页面
	 * 
	 * @author guoyn
	 * @date 2015年5月13日 下午7:04:28
	 * 
	 * @return String
	 */
	@Remark("启用或禁用权限页面")
	@RequestMapping(value = "/mgmt/jsp", method = RequestMethod.GET)
	public String toPermissionePage() {
		return "perm/permMgmt";
	}

	/**
	 * 获取所有系统模块及模块权限集合
	 * 
	 * @author guoyn
	 * @date 2015年5月12日 下午6:14:58
	 * 
	 * @param request
	 * @param response
	 * @return Result<Map<String, List<Module>>>
	 */
	@Remark("获取所有系统模块及模块下的权限列表")
	@RequestMapping(value = "/modu/and/perms/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<Map<String, List<Module>>> getAllModules(HttpServletRequest request, HttpServletResponse response) {
		Result<Map<String, List<Module>>> result = Result.newOne();
		Map<String, List<Module>> dataMap = new HashMap<String, List<Module>>();
		//
		AuthScope[] scopes = BaseConst.AUTH_SCOPE_LIST;
		for (int i = 0; i < scopes.length; i++) {
			AuthScope scope = scopes[i];
			List<Module> allModules = authxService.getModulesByScope(scope);
			dataMap.put(scope.name(), allModules);
		}
		//
		result.data = dataMap;

		return result;
	}

	/**
	 * 保存用户发生更改的权限
	 * 
	 * @author guoyn
	 * @date 2015年5月12日 下午6:15:58
	 * 
	 * @param requestData
	 * @param response
	 * @return Result<?>
	 */
	@Remark("更新权限的disabled状态")
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/status/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> update(@RequestBody MapContext requestData, HttpServletResponse response) {
		Result<?> result = Result.newOne();

		// 获取需要禁用的权限Id集合
		List<Integer> disabledIds = requestData.getTypedValue("disabledIds", TypeUtil.Types.IntegerList.getClass());

		// 获取需要启用的权限Id集合
		List<Integer> enabledIds = requestData.getTypedValue("enabledIds", TypeUtil.Types.IntegerList.getClass());

		Boolean ok = true;
		if (disabledIds.size() > 0) {
			ok = authxService.updatePermissionsStatusByIds(disabledIds, true) && ok;
		}
		if (enabledIds.size() > 0) {
			ok = authxService.updatePermissionsStatusByIds(enabledIds, false) && ok;
		}

		result.message = ok ? "修改成功!" : "修改失败!";
		return result;
	}

	// -------------------------------角色管理--------------------------------------
	/**
	 * 跳转角色管理页面
	 * 
	 * @author 毛智东
	 * @date 2015年5月13日 下午6:33:19
	 * @return
	 */
	@Remark("角色管理页面")
	@RequestMapping(value = "/role/mgmt/jsp", method = RequestMethod.GET)
	public String getRolePage() {
		return "perm/roleMgmt";
	}

	/**
	 * 获取角色及模块列表
	 * 
	 * @author 毛智东
	 * @date 2015年5月13日 上午11:01:20
	 * @param request
	 * @param response
	 * @return {sys = {role = sysRole, modules = sysModules}, mall = {role = mallRole, modules = mallModules}}, shop = {role = shopRole, modules = shopModules}}
	 */
	@Remark("获取角色及模块列表")
	@RequestMapping(value = "/role/mgmt/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<Map<String, Object>> getRolesAndModules(HttpServletRequest request, HttpServletResponse response) {
		Result<Map<String, Object>> result = Result.newOne();
		Map<String, Object> dataMap = new HashMap<String, Object>();
		//
		AuthScope[] scopes = BaseConst.AUTH_SCOPE_LIST;
		for (int i = 0; i < scopes.length; i++) {
			AuthScope scope = scopes[i];
			Role role = authxService.getBuiltInAdminRoleByScope(scope);
			if (role != null) {
				List<Module> allModules = authxService.getModulesByScope(scope);
				Map<String, Object> scopeMap = new HashMap<String, Object>();
				scopeMap.put("role", role);
				scopeMap.put("modules", allModules);

				dataMap.put(scope.name(), scopeMap);
			}
		}
		result.data = dataMap;
		//
		return result;
	}

	/**
	 * 修改角色权限
	 * 
	 * @author 毛智东
	 * @date 2015年5月13日 下午6:41:33
	 * @param request
	 * @param requestData
	 *            [{id=2, addedSubIds=[7], deletedSubIds=[8,9]}, {id=3, addedSubIds=[2], deletedSubIds=[23,11]}]
	 * @return
	 */
	@Remark("修改角色权限")
	@RequestMapping(value = "/role/mgmt/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> updateRolePers(HttpServletRequest request, @RequestBody MapContext requestData) {
		Result<?> result = Result.newOne();
		String json = requestData.getTypedValue("json", String.class);
		List<RelChangeInfo> relChangeInfos = JsonUtil.fromJson(json, TypeUtil.TypeRefs.RelChangeInfoListType);
		result.message = authxService.updateRolePermsBatch(relChangeInfos) ? "修改成功" : "修改失败";
		return result;
	}

	/**
	 * 获得角色权限列表
	 * 
	 * @author 毛智东
	 * @date 2015年5月14日 下午4:34:44
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@Remark("获得角色权限列表")
	@RequestMapping(value = "/role/perms/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<RolePermission>> getRolePerms(HttpServletRequest request, HttpServletResponse response) {
		Result<List<RolePermission>> result = Result.newOne();
		List<RolePermission> checkedList = new ArrayList<RolePermission>();
		//
		AuthScope[] scopes = BaseConst.AUTH_SCOPE_LIST;
		for (int i = 0; i < scopes.length; i++) {
			AuthScope scope = scopes[i];
			Role role = authxService.getBuiltInAdminRoleByScope(scope);
			if (role != null) {
				List<RolePermission> checked = authxService.getRolePermissionsByRoleId(role.getId());
				checkedList.addAll(checked);
			}
		}
		result.data = checkedList;
		//
		return result;
	}

	// ------------------------------------------ Resource -----------------------------------
	/**
	 * 通过scope获取权限
	 * 
	 * @author 郭营
	 * @date 2015年5月13日 下午7:45:30
	 * @return 权限列表
	 * 
	 */
	@Remark("获取权限列表")
	@RequestMapping(value = "/sysRes/perms/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<Map<String, List<Permission>>> getPermissions(@RequestBody MapContext requestData) {
		Result<Map<String, List<Permission>>> result = Result.newOne();
		String scope = requestData.getTypedValue("scope", String.class);
		Map<String, List<Permission>> permList = new HashMap<String, List<Permission>>();
		List<Permission> permissions = authxService.getPermissionsByScope(EnumUtil.valueOf(AuthScope.class, scope));
		permList.put(scope, permissions);
		result.data = permList;
		return result;
	}

	// ------------------------------------------角色列表-------------------------------------------
	/**
	 * 角色列表页面
	 * 
	 * @author 毛智东
	 * @date 2015年6月2日 上午10:50:37
	 * 
	 * @return
	 */
	@Remark("商城角色列表页面")
	@RequestMapping(value = "/mall/role/list/jsp", method = RequestMethod.GET)
	public String toMallRoleList() {
		return "perm/mallRoleList";
	}

	/**
	 * 角色列表页面
	 * 
	 * @author 毛智东
	 * @date 2015年6月2日 上午10:50:37
	 * 
	 * @return
	 */
	@Remark("店铺角色列表页面")
	@RequestMapping(value = "/shop/role/list/jsp", method = RequestMethod.GET)
	public String toShopRoleList() {
		return "perm/shopRoleList";
	}

	/**
	 * 分页查询角色列表
	 * 
	 * @author 毛智东
	 * @date 2015年6月2日 下午5:44:14
	 * 
	 * @param request
	 * @return
	 */
	@Remark(value = "查询角色列表", code = "perm.role.list")
	@RequestMapping(value = { "/roles/list/get/-mall", "/roles/list/get/-shop" }, method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<Role> getRolesList(HttpServletRequest request) {
		UserContext userContext = getUserContext(request);

		// TODO 确认url的权限后缀和作用
		AuthScope scope = EnumUtil.valueOf(AuthScope.class, userContext.getScope());
		Integer entityId = userContext.getScopeEntityId(userContext.getScope());
		Boolean includeSysSet = true;
		List<Role> paginatedList = authxService.getRolesByScopeAndEntityId(scope, entityId, includeSysSet);

		JqGridPage<Role> jqGridPage = new JqGridPage<Role>();
		jqGridPage.setTotalCount(paginatedList.size());
		jqGridPage.setTotalPages(1);
		jqGridPage.setPageNumber(1);
		jqGridPage.setRows(paginatedList);
		return jqGridPage;
	}

	/**
	 * 根据范围获得模块权限列表
	 * 
	 * @author 毛智东
	 * @date 2015年6月3日 上午10:21:16
	 * 
	 * @param scope
	 * @return
	 */
	@Remark("根据范围获得模块权限列表")
	@RequestMapping(value = "/modules/and/perms/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<Module>> getModulesByScope(@RequestBody String scope) {
		Result<List<Module>> result = Result.newOne();
		// 查询系统模块权限
		List<Module> moudules = authxService.getModulesByScope(AuthScope.valueOf(scope));
		result.data = moudules;
		return result;
	}

	/**
	 * 根据角色id获得权限列表
	 * 
	 * @author 毛智东
	 * @date 2015年6月3日 上午11:32:26
	 * 
	 * @param id
	 * @return
	 */
	@Remark("根据角色id获得权限列表")
	@RequestMapping(value = "/perms/by/roleId/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<RolePermission>> getRolePermsByRoleId(@RequestParam("id") Integer id) {
		Result<List<RolePermission>> result = Result.newOne();
		List<RolePermission> rolePerms = authxService.getRolePermissionsByRoleId(id);
		result.data = rolePerms;
		return result;
	}

	/**
	 * 添加角色
	 * 
	 * @author 毛智东
	 * @date 2015年6月3日 下午5:53:33
	 * 
	 * @param roleDto
	 * @return
	 */
	@Remark("添加角色")
	@RequestMapping(value = "/role/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Role> addRole(HttpServletRequest request, @RequestBody RoleDto roleDto) {
		UserContext userContext = getUserContext(request);
		//
		Result<Role> result = Result.newOne();
		if (userContext.isSysUser()) {
			Role role = new Role();
			roleDto.setEntityId(userContext.getScopeEntity().getId());
			result.message = authxService.saveRole(roleDto) ? "保存成功！" : "保存失败！";
			TypeUtil.copyProperties(roleDto, role);
			result.data = role;
		} else {
			result.type = Result.Type.error;
			result.message = "登录超时";
		}
		return result;
	}

	/**
	 * 根据唯一条件查找角色
	 * 
	 * @author 毛智东
	 * @date 2015年6月3日 下午6:20:56
	 * 
	 * @param role
	 * @return
	 */
	@Remark("根据唯一条件查找角色")
	@RequestMapping(value = "/role/uniq/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> getRoleByUniq(HttpServletRequest request, @RequestBody Role role) {
		UserContext userContext = getUserContext(request);
		//
		Result<Boolean> result = Result.newOne();
		if (userContext.isSysUser()) {
			role.setEntityId(userContext.getScopeEntity().getId());
			result.data = authxService.getRoleByScopeAndEntityIdAndName(role.getScope(), role.getEntityId(), role.getName()) == null;
		} else {
			result.type = Result.Type.error;
			result.message = "登录超时";
		}
		return result;
	}

	/**
	 * 删除角色
	 * 
	 * @author 毛智东
	 * @date 2015年6月3日 下午8:02:20
	 * 
	 * @param request
	 * @param id
	 * @return
	 */
	@Remark("删除角色")
	@RequestMapping(value = "/role/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteRole(HttpServletRequest request, @RequestParam("id") Integer id) {
		UserContext userContext = getUserContext(request);
		//
		Result<?> result = Result.newOne();
		List<UserRole> userRoles = authxService.getUserRolesByRoleId(id);
		if (userRoles == null || userRoles.size() == 0) {
			if (userContext.isSysUser()) {
				result.message = authxService.deleteRole(id) ? "删除成功！" : "删除失败！";
			} else {
				result.type = Result.Type.error;
			}
		} else {
			result.type = Type.warn;
		}
		return result;
	}

	/**
	 * 删除角色和用户角色
	 * 
	 * @author 毛智东
	 * @date 2015年8月22日 下午5:43:06
	 * 
	 * @param request
	 * @param id
	 * @return
	 */
	@Remark("删除角色和用户角色")
	@RequestMapping(value = "/role/and/userRole/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> deleteRoleAndUserRole(HttpServletRequest request, @RequestParam("id") Integer id) {
		UserContext userContext = getUserContext(request);
		//
		Result<Boolean> result = Result.newOne();
		boolean flag = true;
		if (userContext.isSysUser()) {
			flag = authxService.deleteUserRoleByRoleId(id) && flag;
			flag = authxService.deleteRole(id) && flag;
			result.data = flag;
		}
		return result;
	}

	/**
	 * 批量删除角色
	 * 
	 * @author 毛智东
	 * @date 2015年6月3日 下午8:02:20
	 * 
	 * @param request
	 * @param ids
	 * @return
	 */
	@Remark("批量删除角色")
	@RequestMapping(value = "/role/delete/batch/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteRoleBatch(HttpServletRequest request, @RequestBody List<Integer> ids) {
		UserContext userContext = getUserContext(request);
		//
		Result<?> result = Result.newOne();
		List<UserRole> userRoles = authxService.getUserRolesByRoleIds(ids);
		if (userRoles == null || userRoles.size() == 0) {
			if (userContext.isSysUser()) {
				result.message = authxService.deleteRolesByIds(ids) ? "删除成功！" : "删除失败！";
			} else {
				result.type = Result.Type.error;
			}
		} else {
			result.type = Type.warn;
		}
		return result;
	}

	/**
	 * 批量删除角色和userRole
	 * 
	 * @author 毛智东
	 * @date 2015年8月22日 下午6:17:09
	 * 
	 * @param request
	 * @param ids
	 * @return
	 */
	@Remark("批量删除角色和userRole")
	@RequestMapping(value = "/role/and/userRole/delete/batch/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> deleteRoleAndUserRoleByRoleIds(HttpServletRequest request, @RequestBody List<Integer> ids) {
		UserContext userContext = getUserContext(request);
		//
		Result<Boolean> result = Result.newOne();
		boolean flag = true;
		if (userContext.isSysUser()) {
			flag = authxService.deleteUserRoleByRoleIds(ids) && flag;
			flag = authxService.deleteRolesByIds(ids) && flag;
			result.data = flag;
		}
		return result;
	}

	/**
	 * 修改角色
	 * 
	 * @author 毛智东
	 * @date 2015年6月4日 下午5:32:16
	 * 
	 * @param request
	 * @param roleDto
	 * @return
	 */
	@Remark("修改角色")
	@RequestMapping(value = "/role/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Role> updateRole(HttpServletRequest request, @RequestBody RoleDto roleDto) {
		UserContext userContext = getUserContext(request);
		//
		Result<Role> result = Result.newOne();
		if (userContext.isSysUser()) {
			Role role = new Role();
			roleDto.setEntityId(userContext.getScopeEntity().getId());
			result.message = authxService.updateRole(roleDto) ? "保存成功！" : "保存失败！";
			TypeUtil.copyProperties(roleDto, role);
			result.data = role;
		} else {
			result.type = Result.Type.error;
			result.message = "登录超时";
		}
		return result;
	}
}
