package priv.starfish.mall.manager.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.model.*;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.TypeUtil;
import priv.starfish.mall.agency.entity.Agency;
import priv.starfish.mall.agency.entity.AgencyAuditRec;
import priv.starfish.mall.service.AgencyService;
import priv.starfish.mall.service.UserService;
import priv.starfish.mall.web.base.BaseController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 代理处 控制层
 * 
 * @author WJJ
 * @date 2015年9月10日 下午5:29:11
 *
 */
@Controller
@RequestMapping("/agency")
public class AgencyController extends BaseController {

	@Resource
	private AgencyService agencyService;
	
	@Resource
	private UserService userService;

	// --------------------------------------- agency ----------------------------------------------
	
	/**
	 * 代理处列表页面
	 * @author WJJ
	 * @date 2015年9月17日 上午10:33:30
	 * 
	 * @return
	 */
	@Remark("代理处列表页面")
	@RequestMapping(value = "/list/jsp", method = RequestMethod.GET)
	public String toAgencyJsp() {
		return "agency/agencyList";
	}

	/**
	 * 分页查询代理处
	 * @author WJJ
	 * @date 2015年9月17日 上午10:33:44
	 * 
	 * @param request
	 * @return
	 */
	@Remark("分页查询代理处")
	@RequestMapping(value = "/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<Agency> listAgencies(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<Agency> paginatedList = agencyService.getAgenciesByFilter(paginatedFilter);
		JqGridPage<Agency> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 增加代理处
	 * @author WJJ
	 * @date 2015年9月17日 上午10:33:55
	 * 
	 * @param request
	 * @param agency
	 * @return
	 */
	@Remark("增加代理处")
	@RequestMapping(value = "/add/do", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<Agency> addAgency(HttpServletRequest request, @RequestBody Agency agency) {
		//
		Result<Agency> result = Result.newOne();
		boolean ok = agencyService.saveAgency(agency);
		result.message = "保存成功!";
		result.data = agency;
		//
		if (!ok) {
			result.type = Type.warn;
			result.message = "保存失败!";
		}
		//
		return result;
	}
	
	/**
	 * 修改代理处
	 * @author WJJ
	 * @date 2015年9月17日 上午10:34:08
	 * 
	 * @param request
	 * @param agency
	 * @return
	 */
	@Remark("修改代理处")
	@RequestMapping(value = "/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Agency> updateShop(HttpServletRequest request, @RequestBody Agency agency) {
		Result<Agency> result = Result.newOne();
		boolean flag = agencyService.updateAgency(agency);
		if (flag) {
			result.message = "修改成功";
		} else {
			result.message = "修改失败";
			result.type = Result.Type.error;
		}
		return result;
	}
	
	/**
	 * 代理处审核
	 * @author WJJ
	 * @date 2015年9月17日 上午10:34:17
	 * 
	 * @param request
	 * @param agencyAuditRec
	 * @return
	 */
	@Remark("代理处审核")
	@RequestMapping(value = "/audit/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<AgencyAuditRec> auditAgency(HttpServletRequest request, @RequestBody AgencyAuditRec agencyAuditRec) {
		UserContext userContext = getUserContext(request);
		Result<AgencyAuditRec> result = Result.newOne();
		boolean flag = false;
		if (userContext.isSysUser()) {
			flag = agencyService.updateAgencyAuditStatusById(agencyAuditRec, userContext);
		}
		if (flag) {
			result.message = "操作成功";
		} else {
			result.message = "操作失败";
			result.type = Result.Type.error;
		}
		return result;
	}
	
	/**
	 * 批量审核代理处
	 * @author WJJ
	 * @date 2015年9月17日 上午10:34:25
	 * 
	 * @param request
	 * @param requestData
	 * @return
	 */
	@Remark("批量审核代理处")
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/batch/audit/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> auditagencies(HttpServletRequest request, @RequestBody MapContext requestData) {
		Result<String> result = Result.newOne();
		UserContext userContext = getUserContext(request);
		
		List<String> list = requestData.getTypedValue("ids", TypeUtil.Types.StringList.getClass());
		Integer auditStatus = requestData.getTypedValue("auditStatus", Integer.class);
		String desc = requestData.getTypedValue("desc", String.class);
		
		AgencyAuditRec agencyAuditRec = new AgencyAuditRec();
		agencyAuditRec.setAuditStatus(auditStatus);
		agencyAuditRec.setDesc(desc);
		boolean flag = agencyService.updateAgencyAuditStatusByIds(list, agencyAuditRec, userContext);
		if (flag) {
			result.message = "操作成功";
		} else {
			result.message = "操作失败";
			result.type = Result.Type.error;
		}
		return result;
	}
	
	/**
	 * 代理处删除
	 * @author WJJ
	 * @date 2015年9月17日 上午10:34:34
	 * 
	 * @param request
	 * @param agency
	 * @return
	 */
	@Remark("代理处删除")
	@RequestMapping(value = "/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Agency> delAgencyById(HttpServletRequest request, @RequestBody Agency agency) {
		Result<Agency> result = Result.newOne();
		boolean flag = agencyService.deleteAgencyById(agency.getId());
		if (flag) {
			result.message = "删除成功";
		} else {
			result.message = "删除失败";
			result.type = Result.Type.error;
		}
		return result;
	}
	
	/**
	 * 批量删除代理处 根据ids
	 * @author WJJ
	 * @date 2015年9月17日 上午10:34:41
	 * 
	 * @param request
	 * @param requestData
	 * @return
	 */
	@Remark("批量删除代理处 根据ids")
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/delete/by/ids", method = RequestMethod.POST)
	@ResponseBody
	public Result<String> delShopsByIds(HttpServletRequest request, @RequestBody MapContext requestData) {
		Result<String> result = Result.newOne();
		
		List<String> list = requestData.getTypedValue("ids", TypeUtil.Types.StringList.getClass());
		boolean flag = agencyService.deleteAgenciesByIds(list);
		if (flag) {
			result.message = "删除成功";
		} else {
			result.message = "删除失败";
			result.type = Result.Type.error;
		}
		return result;
	}
	
	/**
	 * 代理处禁用启用
	 * @author WJJ
	 * @date 2015年9月17日 上午10:34:48
	 * 
	 * @param request
	 * @param agency
	 * @return
	 */
	@Remark("代理处禁用启用")
	@RequestMapping(value = "/disabled/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Agency> changeIsClose(HttpServletRequest request, @RequestBody Agency agency) {
		Result<Agency> result = Result.newOne();
		boolean flag = agencyService.updateAgencyStatus(agency.getId(), agency.getDisabled());
		if (flag) {
			result.message = "操作成功";
		} else {
			result.message = "操作失败";
			result.type = Result.Type.error;
		}
		return result;
	}
	
	/*

	@Remark("代理处名称是否存在")
	@RequestMapping(value = "/exist/by/name/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> getShopExistByName(@RequestBody String name) {
		Result<Boolean> result = Result.newOne();
		if (name != null) {
			result.data = (shopService.getShopByName(name) != null);
		}
		return result;
	}

	// -------------------------------------------代理处参数设置--------------------------------------------------------

	@Remark("代理处设置：配送方式设置页面")
	@RequestMapping(value = "/delivery/way/list/jsp", method = RequestMethod.GET)
	public String toDeliveryWayJsp() {
		return "shop/deliveryWayList";
	}

	@Remark("代理处设置：获取代理处参数")
	@RequestMapping(value = "/param/val/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<ShopParam> getShopParamVals(HttpServletRequest request, @RequestBody ShopParam shopParam) {
		UserContext userContext = getUserContext(request);
		//
		Result<ShopParam> result = Result.newOne();
		//
		String code = shopParam.getCode();
		Integer shopId = userContext.getScopeEntityId(AuthScope.shop.name());
		ShopParam param = shopService.getShopParamByCodeAndShopId(code, shopId);
		result.data = param;
		//
		return result;
	}

	@Remark("保存代理处参数")
	@RequestMapping(value = "/param/val/save/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> saveShopParamDto(HttpServletRequest request, @RequestBody ShopParamDto shopParamDto) {
		UserContext userContext = getUserContext(request);
		//
		Result<?> result = Result.newOne();
		//
		shopParamDto.shopId = userContext.getScopeEntityId(AuthScope.shop.name());
		result.message = shopService.saveShopParamDto(shopParamDto) ? "保存成功！" : "保存失败！";
		//
		return result;
	}

	@Remark("删除参数值")
	@RequestMapping(value = "/param/val/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteShopParam(HttpServletRequest request, @RequestBody ShopParamDto shopParamDto) {
		UserContext userContext = getUserContext(request);
		//
		Result<?> result = Result.newOne();
		//
		shopParamDto.shopId = userContext.getScopeEntityId(AuthScope.shop.name());
		result.message = shopService.saveShopParamDto(shopParamDto) ? "删除成功！" : "删除失败！";
		//
		return result;
	}

	// -------------------------------------------代理处基本信息--------------------------------------------------------

	@Remark("代理处基本信息界面(代理处信息，代理处公告，发票设置，短信设置)")
	@RequestMapping(value = "/basic/info/jsp", method = RequestMethod.GET)
	public String toShopBasicInfoPage() {
		return "shop/shopMsg";
	}

	@Remark("根据ID获取代理处信息")
	@RequestMapping(value = "/shop/get/by/id", method = RequestMethod.POST)
	@ResponseBody
	public Result<Shop> getShopById(HttpServletRequest request) {
		Result<Shop> result = Result.newOne();
		UserContext userContext = getUserContext(request);
		String scope = userContext.getScope();
		Integer entityId = userContext.getScopeEntityId(scope);
		result.data = shopService.getShopById(entityId);
		return result;
	}

	// ----------------------------------代理处人员列表 角色分配----------------------------------
	@Remark("代理处人员列表界面")
	@RequestMapping(value = "/staff/list/jsp", method = RequestMethod.GET)
	public String toUserRoleListPage() {
		return "shop/shopStaffList";
	}

	@Remark("代理处下的人员")
	@RequestMapping(value = "/staff/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<User> listUserRole(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		UserContext userContext = getUserContext(request);
		String scope = userContext.getScope();
		AuthScope authScope = AuthScope.valueOf(scope);
		Integer entityId = userContext.getScopeEntityId(scope);
		//
		PaginatedList<User> paginatedList = userService.getUsersByScopeAndEntityIdAndFilter(authScope, entityId, paginatedFilter);
		// PaginatedList<User> paginatedList = userService.getUsersByScopeAndEntityIdAndFilter(AuthScope.shop, 1, paginatedFilter);
		//
		JqGridPage<User> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	@Remark("根据条件查询用户")
	@RequestMapping(value = "/user/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<User> listUsersByParms(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		UserContext userContext = getUserContext(request);
		String scope = userContext.getScope();
		AuthScope authScope = AuthScope.valueOf(scope);
		Integer entityId = userContext.getScopeEntityId(scope);
		//
		MapContext filters = paginatedFilter.getFilterItems();
		String phoneNo = filters.getTypedValue("phoneNo", String.class);
		//
		PaginatedList<User> paginatedList = PaginatedList.newOne();
		//
		if (!StrUtil.hasText(phoneNo)) {
			paginatedList.setRows(new ArrayList<User>(0));
		} else {
			User user = userService.getUserByPhoneNo(phoneNo);
			List<User> userList = new ArrayList<User>();
			if (user != null) {
				List<Role> roles = userService.getRolesByUserIdAndScopeAndEntityId(user.getId(), authScope, entityId);
				// List<Role> roles = userService.getRolesByUserIdAndScopeAndEntityId(user.getId(), AuthScope.shop, 1);
				user.setRoles(roles);
				userList.add(user);
			}
			//
			paginatedList.setRows(userList);
		}
		//
		paginatedList.setPagination(paginatedFilter.getPagination());
		JqGridPage<User> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	@Remark("获取代理处下所有的角色及相应权限列表")
	@RequestMapping(value = "/roles/and/perms/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<Role>> getAllRoles(HttpServletRequest request, @RequestBody MapContext requestData) {
		//
		Result<List<Role>> result = Result.newOne();
		//
		UserContext userContext = getUserContext(request);
		String scope = userContext.getScope();
		AuthScope authScope = AuthScope.valueOf(scope);
		Integer entityId = userContext.getScopeEntityId(scope);
		//
		List<Role> roles = authxService.getRolesByScopeAndEntityId(authScope, entityId, true);
		// List<Role> roles = authxService.getRolesByScopeAndEntityId(AuthScope.shop, 1, true);
		for (Role role : roles) {
			List<Permission> perms = authxService.getPermissonsByRoleId(role.getId());
			role.setPerms(perms);
		}
		//
		result.data = roles;
		//
		return result;
	}

	@Remark("更新代理处人员的角色")
	@RequestMapping(value = "/user/roles/update/do", method = RequestMethod.POST)
	@ResponseBody
	@SuppressWarnings("unchecked")
	public Result<Object> updateUserRoles(HttpServletRequest request, @RequestBody MapContext requestData) {
		//
		Result<Object> result = Result.newOne();
		result.message = "保存成功";
		//
		UserContext userContext = getUserContext(request);
		String scope = userContext.getScope();
		AuthScope authScope = AuthScope.valueOf(scope);
		Integer entityId = userContext.getScopeEntityId(scope);
		//
		RelChangeInfo relChangeInfo = new RelChangeInfo();
		//
		Integer userId = requestData.getTypedValue("userId", Integer.class);
		List<Integer> deleteRoleIds = requestData.getTypedValue("deleteRoleIds", TypeUtil.Types.IntegerList.getClass());
		List<Integer> addRoleIds = requestData.getTypedValue("addRoleIds", TypeUtil.Types.IntegerList.getClass());
		//
		relChangeInfo.setMainId(userId);
		relChangeInfo.setSubIdsDeleted(deleteRoleIds);
		relChangeInfo.setSubIdsAdded(addRoleIds);
		//
		boolean ok = userService.updateUserRoles(relChangeInfo, entityId, authScope);
		// boolean ok = userService.updateUserRoles(relChangeInfo, 1, AuthScope.shop);
		//
		result.message = ok ? "保存成功!" : "保存失败!";
		return result;
	}

	@Remark("解除用户所有角色")
	@RequestMapping(value = "/user/roles/unbind/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Object> unbindUserRoles(HttpServletRequest request, @RequestBody MapContext requestData) {
		Result<Object> result = Result.newOne();
		//
		UserContext userContext = getUserContext(request);
		String scope = userContext.getScope();
		AuthScope authScope = AuthScope.valueOf(scope);
		Integer entityId = userContext.getScopeEntityId(scope);
		//
		Integer userId = requestData.getTypedValue("userId", Integer.class);
		boolean ok = userService.unbindUserRolesByUserIdAndScopeAndEntityId(userId, authScope, entityId);
		// boolean ok = userService.unbindUserRolesByUserIdAndScopeAndEntityId(userId, AuthScope.shop, 1);
		result.message = ok ? "操作成功" : "操作失败";
		return result;
	}

	// --------------------------------------------------代理处公告--------------------------------------------------

	@Remark("得到代理处公告")
	@RequestMapping(value = "/notice/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<ShopNotice> getShopNotice(HttpServletRequest request) {
		UserContext userContext = getUserContext(request);
		Integer shopId = userContext.getScopeEntity().getId();
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		MapContext filterItems = paginatedFilter.getFilterItems();
		filterItems.put("shopId", shopId);
		//
		PaginatedList<ShopNotice> paginatedList = shopService.selectShopNotices(paginatedFilter);
		JqGridPage<ShopNotice> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	@Remark("保存代理处公告")
	@RequestMapping(value = "/notice/save/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<ShopNotice> saveShopNotice(HttpServletRequest request, @RequestBody ShopNotice shopNotice) {
		UserContext userContext = getUserContext(request);
		//
		Result<ShopNotice> result = Result.newOne();
		if (shopNotice.getAutoFlag()) {
			// 自动发布
			shopNotice.setStartDate(shopNotice.getPubTime());
			shopNotice.setEndDate(shopNotice.getEndTime());
		} else {
			// 手动发布
			shopNotice.setEndDate(shopNotice.getPubTime());
		}
		//
		Integer shopId = userContext.getScopeEntity().getId();
		shopNotice.setShopId(shopId);
		result.message = shopService.saveShopNotice(shopNotice) ? "保存成功！" : "保存失败！";
		result.data = shopNotice;
		//
		return result;
	}

	@Remark("修改代理处公告")
	@RequestMapping(value = "/notice/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<ShopNotice> updateShopNotice(HttpServletRequest request, @RequestBody ShopNotice shopNotice) {
		UserContext userContext = getUserContext(request);
		//
		Result<ShopNotice> result = Result.newOne();
		if (shopNotice.getAutoFlag()) {
			// 自动发布
			shopNotice.setStartDate(shopNotice.getPubTime());
			shopNotice.setEndDate(shopNotice.getEndTime());
		} else {
			// 手动发布
			shopNotice.setPubTime(null);
			shopNotice.setEndDate(shopNotice.getPubTime());
		}

		Integer shopId = userContext.getScopeEntity().getId();
		shopNotice.setShopId(shopId);
		result.message = shopService.updateShopNotice(shopNotice) ? "修改成功！" : "修改失败！";
		result.data = shopNotice;
		//
		return result;
	}

	@Remark("发布代理处公告")
	@RequestMapping(value = "/notice/publish/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> publishShopNotice(@RequestBody MapContext requestData) {
		Result<?> result = Result.newOne();
		ShopNotice shopNotice = new ShopNotice();
		Integer id = requestData.getTypedValue("id", Integer.class);
		Date pubTime = new Date();
		shopNotice.setId(id);
		shopNotice.setStatus(1);
		shopNotice.setPubTime(pubTime);
		shopNotice.setStartDate(pubTime);
		result.message = shopService.updateShopNotice(shopNotice) ? "发布成功！" : "发布失败！";
		return result;
	}

	@Remark("停止代理处公告")
	@RequestMapping(value = "/notice/stop/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> stopShopNotice(@RequestBody MapContext requestData) {
		Result<?> result = Result.newOne();
		ShopNotice shopNotice = new ShopNotice();
		Integer id = requestData.getTypedValue("id", Integer.class);
		Date endTime = new Date();
		shopNotice.setId(id);
		shopNotice.setEndTime(endTime);
		shopNotice.setStatus(2);
		shopNotice.setEndDate(endTime);
		result.message = shopService.updateShopNotice(shopNotice) ? "停止成功！" : "停止失败！";
		return result;
	}

	@Remark("删除代理处公告")
	@RequestMapping(value = "/notice/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteShopNotice(@RequestParam("id") Integer id) {
		Result<?> result = Result.newOne();
		result.message = shopService.deleteShopNoticeById(id) ? "删除成功！" : "删除失败！";
		return result;
	}

	@Remark("批量删除代理处公告")
	@RequestMapping(value = "/notice/delete/batch/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteShopNoticeByIds(@RequestBody List<Integer> ids) {
		Result<?> result = Result.newOne();
		result.message = shopService.deleteShopNoticeByIds(ids) ? "删除成功！" : "删除失败！";
		return result;
	}

	// -------------------------------- 个人信息 ------------------------------------
	@Remark("代理处个人信息页面")
	@RequestMapping(value = "/personal/info/jsp", method = RequestMethod.GET)
	public String toUserSettingPage() {
		return "shop/shopPersonalInfo";
	}*/
}
