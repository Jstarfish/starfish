package priv.starfish.mall.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import priv.starfish.common.jms.SimpleMessage;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.model.RelChangeInfo;
import priv.starfish.common.util.TypeUtil;
import priv.starfish.mall.dao.comn.PermissionDao;
import priv.starfish.mall.dao.comn.ResourceDao;
import priv.starfish.mall.dao.comn.RolePermissionDao;
import priv.starfish.mall.dao.comn.SiteFunctionDao;
import priv.starfish.mall.dao.comn.SiteModuleDao;
import priv.starfish.mall.dao.comn.SiteResourceDao;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.comn.entity.Resource;
import priv.starfish.mall.comn.entity.SiteFunction;
import priv.starfish.mall.comn.entity.SiteModule;
import priv.starfish.mall.comn.entity.SiteResource;
import priv.starfish.mall.service.BaseConst;
import priv.starfish.mall.service.ResourceService;

@Service("resourceService")
public class ResourceServiceImpl extends BaseServiceImpl implements ResourceService {
	@javax.annotation.Resource
	ResourceDao resourceDao;

	@javax.annotation.Resource
	SiteResourceDao siteResourceDao;

	@javax.annotation.Resource
	PermissionDao permissionDao;

	@javax.annotation.Resource
	RolePermissionDao rolePermissionDao;

	@javax.annotation.Resource
	SiteModuleDao siteModuleDao;

	@javax.annotation.Resource
	SiteFunctionDao siteFunctionDao;

	private void sendResourceChangeTopicMessage() {
		SimpleMessage messageToSend = SimpleMessage.newOne();
		messageToSend.subject = BaseConst.SubjectNames.RESOURCE;
		simpleMessageSender.sendTopicMessage(BaseConst.TopicNames.RESLIST, messageToSend);
	}

	/**
	 * 查询系统资源列表
	 * 
	 * @author 郭营
	 * @date 2015年5月13日 下午7:39:38
	 * 
	 * @param paginatedFilter
	 * @return 系统资源列表
	 */
	@Override
	public PaginatedList<Resource> getResourcesByFilter(PaginatedFilter paginatedFilter) {
		return resourceDao.selectByFilter(paginatedFilter);
	}

	@Override
	public List<Resource> getSysUrlResources(boolean protectedOnly) {
		return resourceDao.selectUrls(protectedOnly);
	}

	/**
	 * 批量删除系统资源
	 * 
	 * @author 郭营
	 * @date 2015年5月13日 下午7:40:02
	 * 
	 * @param list
	 *            系统资源ID集合
	 * @throws Exception
	 * 
	 */
	@Override
	public boolean deleteResourcesByIds(List<Integer> resourceIds) {
		siteResourceDao.deleteByResIds(resourceIds);
		int count = resourceDao.deleteByIds(resourceIds);
		//
		boolean success = count > 0;
		if (success) {
			this.sendResourceChangeTopicMessage();
		}
		return success;
	}

	@Override
	public boolean saveSysResource(Resource resource) {
		int count = resourceDao.insert(resource);
		//
		boolean success = count > 0;
		if (success) {
			this.sendResourceChangeTopicMessage();
		}
		return success;
	}

	@Override
	public boolean updateSysResource(Resource resource) {
		int count = resourceDao.update(resource);
		//
		boolean success = count > 0;
		if (success) {
			this.sendResourceChangeTopicMessage();
		}
		return success;
	}

	@Override
	public List<Resource> getSysResourcesByRoleIdAndFuncId(Integer roleId, Integer funcId) {
		List<Resource> resReturn = new ArrayList<Resource>();
		// 获取所有权限
		List<Integer> permIds = rolePermissionDao.selectPermIdsByRoleId(roleId);
		if (permIds.size() == 0) {
			return resReturn;
		}
		// 获取所有资源
		List<Integer> resIds = resourceDao.selectIdsByPermIds(permIds);
		if (resIds.size() == 0) {
			return resReturn;
		}
		// 获取所有资源功能关系
		List<SiteResource> siteResources = siteResourceDao.selectByFuncIdAndResIds(funcId, resIds);
		if (null == siteResources) {
			return resReturn;
		}
		//
		List<Integer> ids = new ArrayList<Integer>();
		//
		for (SiteResource siteResource : siteResources) {
			ids.add(siteResource.getResId());
		}
		if (ids.size() == 0) {
			return resReturn;
		}
		resReturn = resourceDao.selectByIds(ids);
		return resReturn;
	}

	@Override
	public List<Map<String, Object>> getSysResourcesMapByRoleIdAndFunctionId(Integer roleId, Integer funcId) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 获取所有权限Ids
		List<Integer> permIds = rolePermissionDao.selectPermIdsByRoleId(roleId);
		if (permIds.size() == 0) {
			return list;
		}

		// 获取所有资源
		List<Integer> resIds = resourceDao.selectIdsByPermIds(permIds);
		if (resIds.size() == 0) {
			return list;
		}

		// 获取所有资源功能关系
		List<SiteResource> siteResources = siteResourceDao.selectByFuncIdAndResIds(funcId, resIds);
		if (null == siteResources) {
			return list;
		}
		//
		for (SiteResource siteResource : siteResources) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", siteResource.getResId());
			map.put("name", siteResource.getDispName());
			Resource resource = resourceDao.selectById(siteResource.getResId());
			if (null != resource)
				map.put("url", resource.getPattern());
			list.add(map);
		}
		return list;
	}

	@Override
	public List<Map<String, Object>> getSysResourcesMapByRoleIdsAndFunctionId(List<Integer> roleIds, Integer funcId) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// 获取所有权限Ids
		List<Integer> permIds = rolePermissionDao.selectPermIdsByRoleIds(roleIds);
		if (permIds.size() == 0) {
			return list;
		}

		// 获取所有资源
		List<Integer> resIds = resourceDao.selectIdsByPermIds(permIds);
		if (resIds.size() == 0) {
			return list;
		}

		// 获取所有资源功能关系
		List<SiteResource> siteResources = siteResourceDao.selectByFuncIdAndResIds(funcId, resIds);
		if (null == siteResources) {
			return list;
		}
		//
		for (SiteResource siteResource : siteResources) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", siteResource.getResId());
			map.put("name", siteResource.getDispName());
			Resource resource = resourceDao.selectById(siteResource.getResId());
			if (null != resource)
				map.put("url", resource.getPattern());
			list.add(map);
		}
		return list;
	}

	@Override
	public List<Resource> getSysResourceByPattern(String pattern) {
		return resourceDao.selectByPattern(pattern);
	}

	// ----------------------------------- SiteFunction -------------------------------------------------
	@Override
	public List<Map<String, Object>> getSysResourcesMapByFunctionId(Integer funcId) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(0);
		// 获取所有资源功能关系
		List<SiteResource> siteResources = siteResourceDao.selectByFuncId(funcId);
		if (null == siteResources) {
			return list;
		}
		//
		for (SiteResource siteResource : siteResources) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("id", siteResource.getResId());
			map.put("name", siteResource.getDispName());
			Resource resource = resourceDao.selectById(siteResource.getResId());
			if (null != resource)
				map.put("url", resource.getPattern());
			list.add(map);
		}
		return list;
	}

	// ----------------------------------- SiteFunction -------------------------------------------------
	@Override
	public SiteFunction getSiteFuncById(Integer siteFuncId) {
		return siteFunctionDao.selectById(siteFuncId);
	}

	@Override
	public SiteFunction getSiteFuncByModuleIdAndName(Integer moduleId, String name) {
		return siteFunctionDao.selectByModuleIdAndName(moduleId, name);
	}

	@Override
	public boolean saveSiteFunc(SiteFunction siteFunction) {
		Integer maxSeqNo = siteFunctionDao.getEntityMaxSeqNo(SiteFunction.class, "moduleId", siteFunction.getModuleId());
		siteFunction.setSeqNo(maxSeqNo + 1);
		int count = siteFunctionDao.insert(siteFunction);
		return count > 0;
	}

	@Override
	public boolean updateSiteFunc(SiteFunction siteFunction) {
		int count = siteFunctionDao.update(siteFunction);
		return count > 0;
	}

	@Override
	public boolean deleteSiteFuncById(Integer siteFuncId) {
		int count = 0;
		List<Resource> listResource = siteResourceDao.selectResourcesByFuncId(siteFuncId);
		if (listResource.size() > 0) {
			List<Integer> resIds = new ArrayList<Integer>();
			for (int j = 0; j < listResource.size(); j++) {
				resIds.add(listResource.get(j).getId());
			}
			siteResourceDao.deleteByFuncIdAndResIds(siteFuncId, resIds);
			count = siteFunctionDao.deleteById(siteFuncId);
		} else {
			count = siteFunctionDao.deleteById(siteFuncId);
		}
		return count > 0;
	}

	@Override
	public List<SiteFunction> getSiteFuncsBySiteModuleId(Integer moduleId) {
		return siteFunctionDao.selectByModuleId(moduleId);
	}

	@Override
	public List<Resource> getResourcesByFuncId(Integer funcId) {
		return siteResourceDao.selectResourcesByFuncId(funcId);
	}

	@Override
	public List<Resource> getResourcesByScope(AuthScope scope) {
		return resourceDao.selectByScope(scope);
	}

	@Override
	public void unbindSiteResource(Integer funcId, List<Integer> list) {
		siteResourceDao.deleteByFuncIdAndResIds(funcId, list);
	}

	@Override
	public void deleteSiteResourceByFuncId(Integer funcId) {
		siteResourceDao.deleteByFuncId(funcId);
	}

	@Override
	public void saveSiteResource(SiteResource siteResource) {
		Integer maxSeqNo = siteResourceDao.getEntityMaxSeqNo(SiteResource.class, "funcId", siteResource.getFuncId());
		siteResource.setSeqNo(maxSeqNo + 1);
		siteResourceDao.insert(siteResource);
	}

	@Override
	public List<SiteFunction> getSiteFuncsByRoleIdAndModuleId(Integer roleId, Integer moduleId) {
		List<SiteFunction> funcs = new ArrayList<SiteFunction>();
		// 获取所有权限
		List<Integer> permIds = rolePermissionDao.selectPermIdsByRoleId(roleId);
		if (permIds.size() == 0) {
			return funcs;
		}
		// 获取所有资源
		List<Integer> resIds = resourceDao.selectIdsByPermIds(permIds);
		if (resIds.size() == 0) {
			return funcs;
		}
		// 获取所有资源功能关系
		List<Integer> funcIds = siteResourceDao.selectFuncIdsByResIds(resIds);
		if (funcIds.size() == 0) {
			return funcs;
		}
		// 获取所有功能
		funcs = siteFunctionDao.selectByFuncIdsAndModule(funcIds, moduleId);
		return funcs;
	}

	@Override
	public List<SiteFunction> getSiteFuncsByRoleIdsAndModuleId(List<Integer> roleIds, Integer moduleId) {
		List<SiteFunction> funcs = new ArrayList<SiteFunction>();
		// 获取所有权限
		List<Integer> permIds = rolePermissionDao.selectPermIdsByRoleIds(roleIds);
		if (permIds.size() == 0) {
			return funcs;
		}
		// 获取所有资源
		List<Integer> resIds = resourceDao.selectIdsByPermIds(permIds);
		if (resIds.size() == 0) {
			return funcs;
		}
		// 获取所有资源功能关系
		List<Integer> funcIds = siteResourceDao.selectFuncIdsByResIds(resIds);
		if (funcIds.size() == 0) {
			return funcs;
		}
		// 获取所有功能
		funcs = siteFunctionDao.selectByFuncIdsAndModule(funcIds, moduleId);
		return funcs;
	}

	@Override
	public boolean updateSiteResources(RelChangeInfo relChangeInfo) {
		boolean flag = true;
		Integer funcId = relChangeInfo.getMainId();
		List<Integer> addList = relChangeInfo.getSubIdsAdded();
		List<Integer> delList = relChangeInfo.getSubIdsDeleted();
		if (delList.size() > 0) {
			flag = siteResourceDao.deleteByFuncIdAndResIds(funcId, delList) > 0 && flag;
		}
		//
		Integer maxSeqNo = siteResourceDao.getEntityMaxSeqNo(SiteResource.class, "funcId", funcId);
		for (Integer resId : addList) {
			SiteResource siteResource = new SiteResource();
			Resource resource = resourceDao.selectById(resId);
			String name = resource.getName();
			if (name.endsWith("页面")) {
				name = name.substring(0, name.length() - 2);
			}
			siteResource.setDispName(name);
			siteResource.setFuncId(funcId);
			siteResource.setResId(resId);
			siteResource.setSeqNo(++maxSeqNo);
			flag = siteResourceDao.insert(siteResource) > 0 && flag;
		}
		return flag;
	}

	// ----------------------------------- SiteModule -------------------------------------------------

	@Override
	public SiteModule getSiteModuById(Integer id) {
		return siteModuleDao.selectById(id);
	}

	@Override
	public Boolean createSiteModule(SiteModule siteModule) {
		Integer maxSeqNo = siteModuleDao.getEntityMaxSeqNo(SiteModule.class, "scope", siteModule.getScope());
		siteModule.setSeqNo(maxSeqNo + 1);
		Integer count = siteModuleDao.insert(siteModule);
		return count > 0;
	}

	@Override
	public Boolean updateSiteModule(SiteModule siteModule) {
		Integer count = siteModuleDao.update(siteModule);
		return count > 0;
	}

	@Override
	public Boolean deleteSiteModuById(Integer id) {
		List<SiteFunction> siteFunctionList = siteFunctionDao.selectByModuleId(id);
		for (SiteFunction siteFunction : siteFunctionList) {
			Integer tmpFuncId = siteFunction.getId();
			List<Resource> listResource = siteResourceDao.selectResourcesByFuncId(tmpFuncId);
			List<Integer> resIds = new ArrayList<Integer>();
			for (Resource resource : listResource) {
				resIds.add(resource.getId());
			}
			siteResourceDao.deleteByFuncIdAndResIds(tmpFuncId, resIds);
			siteFunctionDao.deleteById(tmpFuncId);
		}
		return siteModuleDao.deleteById(id) > 0;

	}

	@Override
	public PaginatedList<SiteModule> getSiteModulesByFilter(PaginatedFilter paginatedFilter) {
		return siteModuleDao.selectByFilter(paginatedFilter);
	}

	@Override
	public List<SiteModule> getSiteModulesByScope(AuthScope scope, boolean cascaded) {
		//
		List<SiteModule> siteModules = siteModuleDao.selectByScope(scope);
		//
		if (cascaded) {
			for (SiteModule siteModule : siteModules) {
				Integer moduleId = siteModule.getId();
				List<SiteFunction> siteFuctions = siteFunctionDao.selectByModuleId(moduleId);
				//
				for (SiteFunction siteFuction : siteFuctions) {
					Integer funcId = siteFuction.getId();
					List<Resource> resources = siteResourceDao.selectResourcesByFuncId(funcId);
					siteFuction.setResources(resources);
				}
				//
				siteModule.setFunctions(siteFuctions);
			}
		}

		//
		return siteModules;
	}

	@Override
	public boolean deleteSiteModusByIds(List<Integer> ids) {
		boolean flag = true;
		for (Integer id : ids) {
			flag = this.deleteSiteModuById(id) && flag;
		}
		return flag;
	}

	@Override
	public List<SiteModule> getSiteModulesByRoleId(Integer roleId) {
		List<SiteModule> modules = TypeUtil.newEmptyList(SiteModule.class);
		// 获取所有权限
		List<Integer> permIds = rolePermissionDao.selectPermIdsByRoleId(roleId);
		if (permIds.size() == 0) {
			return modules;
		}
		// 获取所有资源
		List<Integer> resIds = resourceDao.selectIdsByPermIds(permIds);
		if (resIds.size() == 0) {
			return modules;
		}
		// 获取所有资源功能关系
		List<Integer> funcIds = siteResourceDao.selectFuncIdsByResIds(resIds);
		if (funcIds.size() == 0) {
			return modules;
		}
		// 获取所有功能
		List<Integer> siteModuleIds = siteFunctionDao.selectModuleIdsByFuncIds(funcIds);
		if (siteModuleIds.size() == 0) {
			return modules;
		}
		modules = siteModuleDao.selectByIds(siteModuleIds);
		return modules;
	}

	@Override
	public List<SiteModule> getSiteModulesByRoleIds(List<Integer> roleIds) {
		List<SiteModule> modules = TypeUtil.newEmptyList(SiteModule.class);
		// 获取所有权限
		List<Integer> permIds = rolePermissionDao.selectPermIdsByRoleIds(roleIds);
		if (permIds.size() == 0) {
			return modules;
		}
		// 获取所有资源
		List<Integer> resIds = resourceDao.selectIdsByPermIds(permIds);
		if (resIds.size() == 0) {
			return modules;
		}
		// 获取所有资源功能关系
		List<Integer> funcIds = siteResourceDao.selectFuncIdsByResIds(resIds);
		if (funcIds.size() == 0) {
			return modules;
		}
		// 获取所有功能
		List<Integer> siteModuleIds = siteFunctionDao.selectModuleIdsByFuncIds(funcIds);
		if (siteModuleIds.size() == 0) {
			return modules;
		}
		modules = siteModuleDao.selectByIds(siteModuleIds);
		return modules;
	}

	@Override
	public SiteModule getSiteModuByNameAndScope(String name, AuthScope scope) {
		return siteModuleDao.selectByNameAndScope(name, scope);
	}

}
