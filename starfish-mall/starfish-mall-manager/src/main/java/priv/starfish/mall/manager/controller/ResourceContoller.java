package priv.starfish.mall.manager.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import priv.starfish.common.annotation.Remark;
import priv.starfish.common.base.TargetJudger;
import priv.starfish.common.model.*;
import priv.starfish.common.model.Result.Type;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.*;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.comn.entity.Resource;
import priv.starfish.mall.comn.entity.SiteFunction;
import priv.starfish.mall.comn.entity.SiteModule;
import priv.starfish.mall.web.base.BaseController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Remark("资源、模块：系统资源、模块站点资源、功能、模块相关处理的Controller")
@Controller
@RequestMapping("/res")
public class ResourceContoller extends BaseController {

	// --------------------------------------- Resource Registered ----------------------------------------------
	/**
	 * 跳转到系统资源注册页面
	 * 
	 * @author 郭营
	 * @date 2015年5月13日 下午7:33:04
	 * @return 系统资源注册页面字符串
	 */
	@Remark("系统资源注册页面")
	@RequestMapping(value = "/sysRes/regist/list/jsp", method = RequestMethod.GET)
	public String toSysResRegistListPage() {
		return "res/sysResRegistList";
	}

	/**
	 * 删除系统资源
	 * 
	 * @author 郭营
	 * @date 2015年5月13日 下午7:33:38
	 * 
	 * @param requestData
	 *            删除系统资源ID集合
	 * 
	 * @return Result<String>
	 */
	@SuppressWarnings("unchecked")
	@Remark("删除系统资源")
	@RequestMapping(value = "/sysRes/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<String> deleteResourceByIds(@RequestBody MapContext requestData) {
		Result<String> result = new Result<String>();
		List<Integer> resourceIds = requestData.getTypedValue("resourceIds", TypeUtil.Types.IntegerList.getClass());
		boolean stus = rescService.deleteResourcesByIds(resourceIds);

		if (stus) {
			result.type = Type.info;
			result.message = "操作成功";
		} else {
			result.type = Type.warn;
			result.message = "操作失败";
		}
		return result;
	}

	/**
	 * 更新保存系统资源
	 * 
	 * @author 郭营
	 * @date 2015年5月13日 下午7:34:52
	 * 
	 *            更新保存系统资源的集合
	 * 
	 * @return
	 */
	@Remark("更新保存系统资源")
	@RequestMapping(value = "/sysRes/save/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<String> saveOrUpdate(@RequestBody Resource resource) {
		Result<String> result = new Result<String>();
		boolean status = false;
		if (resource.getId() == null) {
			status = rescService.saveSysResource(resource);
		} else {
			status = rescService.updateSysResource(resource);
		}
		if (status) {
			result.type = Type.info;
			result.message = "操作成功";
		} else {
			result.type = Type.warn;
			result.message = "操作失败";
		}
		return result;
	}

	/**
	 * 
	 * @author 郭营
	 * @date 2015年5月13日 下午7:35:29
	 * @param request
	 *            请求数据集合
	 * @param response
	 * @return
	 * @throws IOException
	 *             查询系统资源列表
	 */
	@Remark("系统资源列表")
	@RequestMapping(value = "/sysRes/list/get", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public JqGridPage<Resource> getResouces(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		PaginatedList<Resource> paginatedList = rescService.getResourcesByFilter(paginatedFilter);
		JqGridPage<Resource> jqgridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqgridPage;
	}

	/**
	 * 根据功能ID查询站点资源
	 * 
	 * @author zjl
	 * @date 2015年5月14日 上午10:26:14
	 * 
	 * @param request
	 * @param siteFunction
	 * @return Result<List<Resource>>
	 */
	@Remark("根据功能ID查询站点资源")
	@RequestMapping(value = "/siteRes/list/get/by/func", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<Resource>> getResource(HttpServletRequest request, @RequestBody SiteFunction siteFunction) {
		Result<List<Resource>> result = Result.newOne();
		result.data = rescService.getResourcesByFuncId(siteFunction.getId());
		return result;
	}

	/**
	 * 根据Scoope和functionId查询站点所有资源和该功能下被绑定的资源
	 * 
	 * @author zjl
	 * @date 2015年5月14日 上午10:28:49
	 * 
	 * @param request
	 * @param requestData
	 * @return Result<List<Resource>>
	 */
	@Remark("根据Scoope和functionId查询站点所有资源和该功能下被绑定的资源")
	@RequestMapping(value = "/siteRes/list/get/all", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<Map<String, List<Resource>>> getAllResource(HttpServletRequest request, @RequestBody MapContext requestData) {
		Result<Map<String, List<Resource>>> result = Result.newOne();
		Map<String, List<Resource>> dataMap = new HashMap<String, List<Resource>>();
		String scope = requestData.getTypedValue("scope", String.class);
		//
		List<Resource> resourceAll = rescService.getResourcesByScope(EnumUtil.valueOf(AuthScope.class, scope));
		dataMap.put("resAll", resourceAll);
		Integer functionId = requestData.getTypedValue("funcId", Integer.class);
		List<Resource> resourceChecked = rescService.getResourcesByFuncId(functionId);
		dataMap.put("resChecked", resourceChecked);
		result.data = dataMap;
		return result;
	}

	/**
	 * 批量解除站点资源
	 * 
	 * @author zjl
	 * @date 2015年5月14日 上午10:30:55
	 * 
	 * @param request
	 * @param requestData
	 * @return
	 */
	@Remark("批量解除站点资源")
	@RequestMapping(value = "/siteFunc/sitRes/unbind/do", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<Map<String, Object>> unbindSiteResource(HttpServletRequest request, @RequestBody MapContext requestData) {
		// 组装返回数据
		Result<Map<String, Object>> result = Result.newOne();
		String msg = "操作失败！";
		Integer functionId = requestData.getTypedValue("functionId", Integer.class);
		@SuppressWarnings("unchecked")
		List<Object> list = (List<Object>) requestData.get("list");
		List<Integer> resources = new ArrayList<Integer>();
		for (int i = 0; i < list.size(); i++) {
			resources.add(Integer.valueOf(list.get(i).toString()));
		}
		try {
			rescService.unbindSiteResource(functionId, resources);
			msg = "操作成功";
		} catch (Exception e) {
			e.printStackTrace();
			msg = "操作失败";
		}
		result.message = msg;
		return result;
	}

	/**
	 * 更新functionId下的站点资源
	 * 
	 * @author zjl
	 * @date 2015年5月14日 上午10:31:31
	 * 
	 * @param request
	 * @param requestData
	 * @return
	 */
	@Remark("批量绑定站点资源")
	@RequestMapping(value = "/siteRes/batch/add/do", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<?> addSiteResource(HttpServletRequest request, @RequestBody MapContext requestData) {
		Result<?> result = Result.newOne();
		String message = "更新成功";
		String json = requestData.getTypedValue("json", String.class);
		List<RelChangeInfo> relChangeInfos = JsonUtil.fromJson(json, TypeUtil.TypeRefs.RelChangeInfoListType);
		for (RelChangeInfo relChangeInfo : relChangeInfos) {
			message = rescService.updateSiteResources(relChangeInfo) ? "操作成功" : "操作失败";
		}
		result.message = message;
		return result;
	}

	@Remark("系统资源是否存在")
	@RequestMapping(value = "sysRes/exist/by/pattern/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> getUserExistByPhoneNo(@RequestBody String pattern) {
		Result<Boolean> result = Result.newOne();
		if (pattern != null) {
			result.data = !TypeUtil.isNullOrEmpty(rescService.getSysResourceByPattern(pattern));
		}
		return result;
	}

	// ----------------------------------- SiteModule -------------------------------------------------

	/**
	 * 通过角色获取站点模块
	 * 
	 * @author 廖晓远
	 * @date 2015-5-13 下午7:09:27
	 * 
	 * @param request
	 * @return
	 */
	@Remark("通过上下文获取站点模块")
	@RequestMapping(value = "/siteModu/list/get/by/context", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<SiteModule>> getSiteModulesByRole(HttpServletRequest request) {
		//
		Result<List<SiteModule>> result = Result.newOne();
		result.data = getUserScopeEntitySiteResTree(request);
		//
		return result;
	}

	/**
	 * 转到站点模块界面
	 * 
	 * @author zjl
	 * @date 2015年5月13日 下午2:50:46
	 * 
	 * @return resModule/siteModule 转向的界面
	 * @throws IOException
	 */
	@Remark("模块管理")
	@RequestMapping(value = "/siteModu/list/jsp", method = RequestMethod.GET)
	public String toSiteModuleListPage() {
		return "res/siteModule";
	}

	/**
	 * 分页查询模块
	 * 
	 * @author zhuangjunlei
	 * @date 2015年5月13日 上午11:36:13
	 * 
	 * @param request
	 * @return JqGridPage<SiteModule>
	 * @throws IOException
	 */
	@Remark("分页查询模块")
	@RequestMapping(value = "/siteModu/list/get", method = RequestMethod.POST)
	@ResponseBody
	public JqGridPage<SiteModule> list(HttpServletRequest request) {
		// 封装前台参数为JqGridRequest格式
		JqGridRequest jqGridRequest = JqGridRequest.fromRequest(request);
		// 封装为PaginatedFilter格式
		PaginatedFilter paginatedFilter = jqGridRequest.toPaginatedFilter();
		//
		PaginatedList<SiteModule> paginatedList = rescService.getSiteModulesByFilter(paginatedFilter);
		JqGridPage<SiteModule> jqGridPage = JqGridPage.fromPaginatedList(paginatedList);
		return jqGridPage;
	}

	/**
	 * 增加站点模块
	 * 
	 * @author zjl
	 * @date 2015年5月13日 下午2:51:48
	 * 
	 * @param request
	 * @param siteModule
	 * @return Result<List<SiteModule>>
	 */
	@Remark("增加站点模块")
	@RequestMapping(value = "/siteModu/save/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<SiteModule> createSiteModule(HttpServletRequest request, @RequestBody SiteModule siteModule) {
		Result<SiteModule> result = Result.newOne();
		boolean ok = true;
		try {
			ok = rescService.createSiteModule(siteModule);
			result.data = siteModule;

		} catch (Exception e) {
			ok = false;
			result.type = Result.Type.error;
			e.printStackTrace();
		}
		result.message = ok ? "操作成功" : "操作失败";
		return result;
	}

	/**
	 * 根据ID获取站点模块
	 * 
	 * @author zjl
	 * @date 2015年5月13日 下午2:52:24
	 * 
	 * @param request
	 * @param siteModule
	 * @return Result<SiteModule>
	 */
	@Remark("根据ID获取站点模块")
	@RequestMapping(value = "/siteModu/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<SiteModule> getSiteModule(HttpServletRequest request, @RequestBody SiteModule siteModule) {
		Result<SiteModule> result = Result.newOne();
		result.data = rescService.getSiteModuById(siteModule.getId());
		return result;
	}

	/**
	 * 更新站点模块
	 * 
	 * @author zjl
	 * @date 2015年5月13日 下午2:53:06
	 * 
	 * @param request
	 * @param siteModule
	 * @return Result<List<SiteModule>>
	 */
	@Remark("更新站点模块")
	@RequestMapping(value = "/siteModu/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<SiteModule> updateSiteModule(HttpServletRequest request, @RequestBody SiteModule siteModule) {
		Result<SiteModule> result = Result.newOne();
		boolean ok = true;
		try {
			ok = rescService.updateSiteModule(siteModule);
			result.data = siteModule;

		} catch (Exception e) {
			ok = false;
			result.type = Result.Type.error;
		}
		result.message = ok ? "操作成功" : "操作失败";
		return result;
	}

	/**
	 * 根据ID删除站点模块
	 * 
	 * @author zjl
	 * @date 2015年5月13日 下午2:54:49
	 * 
	 * @param request
	 * @param siteModule
	 * @return Result<List<SiteModule>>
	 */
	@Remark("根据ID删除站点模块")
	@RequestMapping(value = "/siteModu/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteSiteModule(HttpServletRequest request, @RequestBody SiteModule siteModule) {
		Result<?> result = Result.newOne();
		boolean ok = true;
		try {
			ok = rescService.deleteSiteModuById(siteModule.getId());

		} catch (Exception e) {
			ok = false;
			result.type = Result.Type.error;
		}
		result.message = ok ? "操作成功" : "操作失败";
		return result;
	}

	/**
	 * 根据IDs 删除站点模块
	 * 
	 * @author zjl
	 * @date 2015年5月13日 下午2:55:28
	 * 
	 * @param request
	 * @return Result<String>
	 */
	@Remark("根据IDs 删除站点模块")
	@RequestMapping(value = "/siteModu/delete/by/ids", method = RequestMethod.POST)
	@ResponseBody
	public Result<?> deleteSiteModules(HttpServletRequest request, @RequestBody List<Integer> ids) {
		Result<?> result = Result.newOne();
		boolean ok = true;
		try {
			ok = rescService.deleteSiteModusByIds(ids);

		} catch (Exception e) {
			ok = false;
			result.type = Result.Type.error;
		}
		result.message = ok ? "操作成功" : "操作失败";
		return result;

	}

	/**
	 * 模块名称是否存在
	 * @author guoyn
	 * @date 2015年9月25日 上午11:03:24
	 * 
	 * @param request
	 * @param siteModule
	 * @return Result<Boolean>
	 */
	@Remark("模块名称是否存在")
	@RequestMapping(value = "/siteModu/exsit/by/name", method = RequestMethod.POST)
	@ResponseBody
	public Result<Boolean> getSiteModuByName(HttpServletRequest request, @RequestBody SiteModule siteModule) {
		Result<Boolean> result = Result.newOne();
		String name = siteModule.getName();
		AuthScope scope = siteModule.getScope();
		boolean isExsit = false;
		SiteModule module = rescService.getSiteModuByNameAndScope(name, scope);
		if (module != null) isExsit = true;
		//
		result.data = isExsit;
		return result;
	}

	// ---------------------------------------- SiteFunction -----------------------------------------------

	/**
	 * 通过角色和站点模块获取站点功能和系统资源
	 * 
	 * @author 廖晓远
	 * @date 2015-5-13 下午7:13:54
	 * 
	 * @param request
	 * @param requestData
	 * @return
	 */
	@Remark("通过站点模块和上下文获取站点功能和系统资源")
	@RequestMapping(value = "/siteFunc/list/get/by/siteModuId", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<List<Map<String, Object>>> getSiteFunctionsByRoleAndSiteModuId(HttpServletRequest request, @RequestBody MapContext requestData) {
		UserContext userContext = getUserContext(request);
		final int siteModuleId = requestData.getTypedValue("siteModuleId", Integer.class);
		List<SiteModule> siteResTree = getUserScopeEntitySiteResTree(request);

		Result<List<Map<String, Object>>> result = Result.newOne();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (userContext.isSysUser()) {
					
			SiteModule siteModule = CollectionUtil.search(siteResTree, new TargetJudger<SiteModule>(){

				@Override
				public boolean isTarget(SiteModule toBeChecked) {
					return toBeChecked.getId().intValue() == siteModuleId;
				}
				
			});
			List<SiteFunction> funcList = siteModule == null ? TypeUtil.newEmptyList(SiteFunction.class) : siteModule.getFunctions();
			for (SiteFunction sf : funcList) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", sf.getId());
				map.put("name", sf.getName());
				List<Map<String, Object>> resources = new ArrayList<Map<String, Object>>();
				List<Resource> tmpResList = sf.getResources();
				for (Resource tmpRes : tmpResList) {
					Map<String, Object> resource = new HashMap<String, Object>();
					resource.put("id", tmpRes.getId());
					resource.put("name", tmpRes.getName());
					resource.put("url", tmpRes.getPattern());
					resources.add(resource);
				}
				map.put("resources", resources);
				list.add(map);
			}
			result.data = list;
		}

		return result;
	}

	/**
	 * 转向功能模块界面
	 * 
	 * @author zjl
	 * @date 2015年5月14日 上午10:22:27
	 * 
	 * @return
	 * @throws IOException
	 */
	@Remark("模块功能管理")
	@RequestMapping(value = "/siteFunc/mgmt/jsp", method = RequestMethod.GET)
	public String toSiteSourse() {
		return "res/siteMduFuncTree";
	}

	/**
	 * 根据Scope查询站点模块
	 * 
	 * @author zjl
	 * @date 2015年5月14日 上午10:23:32
	 * 
	 * @param request
	 * @param siteModule
	 * @return Result<List<SiteModule>>
	 */
	@Remark("根据Scope查询站点模块")
	@RequestMapping(value = "/siteModu/list/get/by/scope", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<SiteModule>> getSiteModuleByScope(HttpServletRequest request, @RequestBody SiteModule siteModule) {
		Result<List<SiteModule>> result = Result.newOne();
		//
		result.data = rescService.getSiteModulesByScope(siteModule.getScope(), false);
		return result;
	}

	/**
	 * 根据ModuleId 查询站点功能
	 * 
	 * @author zjl
	 * @date 2015年5月14日 上午10:24:31
	 * 
	 * @param request
	 * @param siteFunction
	 * @return
	 */
	@Remark("根据ModuleId 查询站点功能")
	@RequestMapping(value = "/siteFunc/list/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<SiteFunction>> getSiteFunctionByModuleId(HttpServletRequest request, @RequestBody SiteFunction siteFunction) {
		Result<List<SiteFunction>> result = Result.newOne();
		result.data = rescService.getSiteFuncsBySiteModuleId(siteFunction.getModuleId());
		return result;
	}

	/**
	 * 添加站点功能
	 * 
	 * @author zjl
	 * @date 2015年5月14日 上午10:25:13
	 * 
	 * @param request
	 * @param siteFunction
	 * @return Result<List<SiteFunction>>
	 */
	@Remark("添加站点功能")
	@RequestMapping(value = "/siteFunc/add/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<SiteFunction> addSiteFunction(HttpServletRequest request, @RequestBody SiteFunction siteFunction) {
		Result<SiteFunction> result = Result.newOne();
		boolean ok=true;
		try {
			ok = rescService.saveSiteFunc(siteFunction);
			result.data = siteFunction;

		} catch (Exception e) {
			ok = false;
			result.type = Result.Type.error;
		}
		result.message = ok ? "操作成功" : "操作失败";
		return result;
	}

	/**
	 * 更新站点功能
	 * 
	 * @author zjl
	 * @date 2015年5月14日 上午10:26:14
	 * 
	 * @param request
	 * @param siteFunction
	 * @return Result<List<SiteFunction>>
	 */
	@Remark("更新站点功能")
	@RequestMapping(value = "/siteFunc/update/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<SiteFunction> updateSiteFunction(HttpServletRequest request, @RequestBody SiteFunction siteFunction) {
		Result<SiteFunction> result = Result.newOne();
		boolean ok=true;
		try {
			ok = rescService.updateSiteFunc(siteFunction);
			result.data = siteFunction;

		} catch (Exception e) {
			ok = false;
			result.type = Result.Type.error;
		}
		result.message = ok ? "操作成功" : "操作失败";
		return result;
	}

	/**
	 * 根据ID查询站点功能
	 * 
	 * @author zjl
	 * @date 2015年5月14日 上午10:26:14
	 * 
	 * @param request
	 * @param siteFunction
	 * @return Result<List<SiteFunction>>
	 */
	@Remark("根据ID查询站点功能")
	@RequestMapping(value = "/siteFunc/get", method = RequestMethod.POST)
	@ResponseBody
	public Result<SiteFunction> getSiteFunction(HttpServletRequest request, @RequestBody SiteFunction siteFunction) {
		Result<SiteFunction> result = Result.newOne();
		result.data = rescService.getSiteFuncById(siteFunction.getId());
		return result;
	}

	/**
	 * 删除站点功能
	 * 
	 * @author zjl
	 * @date 2015年5月14日 上午10:31:31
	 * 
	 * @param request
	 * @param siteFunction
	 * @return Result<List<SiteFunction>>
	 */
	@Remark("删除站点功能")
	@RequestMapping(value = "/siteFunc/delete/do", method = RequestMethod.POST)
	@ResponseBody
	public Result<List<SiteFunction>> deleteSiteFunction(HttpServletRequest request, @RequestBody SiteFunction siteFunction) {
		Result<List<SiteFunction>> result = Result.newOne();
		try {
			rescService.deleteSiteFuncById(siteFunction.getId());
			result.data = rescService.getSiteFuncsBySiteModuleId(siteFunction.getModuleId());
			result.message = "删除成功";
		} catch (Exception e) {
			result.message = "删除失败";
			result.type = Result.Type.warn;
		}
		return result;
	}

	@Remark("根据Scope查询资源树")
	@RequestMapping(value = "/siteModu/get/tree/by/scope", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Result<List<SiteModule>> getSiteModuleTree(HttpServletRequest request, @RequestBody SiteModule siteModule) {
		Result<List<SiteModule>> result = Result.newOne();
		//
		result.data = rescService.getSiteModulesByScope(siteModule.getScope(), true);
		;
		//
		return result;
	}
}
