package priv.starfish.mall.service;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.model.RelChangeInfo;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.comn.entity.Resource;
import priv.starfish.mall.comn.entity.SiteFunction;
import priv.starfish.mall.comn.entity.SiteModule;
import priv.starfish.mall.comn.entity.SiteResource;

import java.util.List;
import java.util.Map;

public interface ResourceService extends BaseService {
	/**
	 * 
	 * @author 郭营
	 * @date 2015年5月13日 下午7:39:38
	 * 
	 * @param paginatedFilter
	 * @return 查询系统资源列表
	 */
	public PaginatedList<Resource> getResourcesByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 获取所有url资源
	 * 
	 * @author koqiui
	 * @date 2015年8月10日 下午7:23:56
	 * 
	 * @param protectedOnly
	 *            是否仅仅是被保护的（挂接有权限，从而被保护的）
	 * @return
	 */
	public List<Resource> getSysUrlResources(boolean protectedOnly);

	/**
	 * 批量删除系统资源
	 * 
	 * @author 郭营
	 * @date 2015年5月13日 下午7:40:02
	 * 
	 *            系统资源ID集合
	 * @throws Exception
	 */
	boolean deleteResourcesByIds(List<Integer> resourceIds);

	/**
	 * TODO
	 * 
	 * @author 郭营
	 * @date 2015年5月14日 下午4:04:07
	 * 
	 * @param resource
	 * @return
	 */
	public boolean saveSysResource(Resource resource);

	/**
	 * TODO
	 * 
	 * @author 郭营
	 * @date 2015年5月14日 下午4:04:07
	 * 
	 * @param resource
	 * @return
	 */
	public boolean updateSysResource(Resource resource);

	/**
	 * 通过角色ID和功能ID查找系统资源
	 * 
	 * @author 廖晓远
	 * @date 2015-5-14 下午12:38:37
	 * 
	 * @param roleId
	 *            角色ID
	 * @param funcId
	 *            功能ID
	 * @return List<Resource> 系统资源
	 */
	List<Resource> getSysResourcesByRoleIdAndFuncId(Integer roleId, Integer funcId);

	/**
	 * 通过角色ID和功能ID查找系统资源，封装Map用于手风琴菜单
	 * 
	 * @author 廖晓远
	 * @date 2015-5-14 下午12:39:13
	 * 
	 * @param roleId
	 *            角色ID
	 * @param funcId
	 *            功能ID
	 * @return List<Resource> 系统资源Map
	 */
	List<Map<String, Object>> getSysResourcesMapByRoleIdAndFunctionId(Integer roleId, Integer funcId);

	/**
	 * 通过功能ID查找系统资源，封装Map用于手风琴菜单 测试使用
	 * 
	 * @author 廖晓远
	 * @date 2015-6-11 下午8:23:24
	 * 
	 * @param funcId
	 * @return
	 */
	List<Map<String, Object>> getSysResourcesMapByFunctionId(Integer funcId);

	/**
	 * 通过角色ID列表和功能ID查找系统资源，封装Map用于手风琴菜单
	 * 
	 * @author 廖晓远
	 * @date 2015-6-10 下午4:49:56
	 * 
	 * @param roleIds
	 * @param funcId
	 * @return
	 */
	List<Map<String, Object>> getSysResourcesMapByRoleIdsAndFunctionId(List<Integer> roleIds, Integer funcId);

	/**
	 * 通过地址获取系统资源
	 * 
	 * @author 廖晓远
	 * @date 2015年7月30日 上午11:37:42
	 * 
	 * @param pattern
	 * @return
	 */
	List<Resource> getSysResourceByPattern(String pattern);

	// ----------------------------------- SiteFunction -------------------------------------------------

	SiteFunction getSiteFuncById(Integer id);

	SiteFunction getSiteFuncByModuleIdAndName(Integer moduleId, String name);

	boolean saveSiteFunc(SiteFunction siteFunction);

	boolean updateSiteFunc(SiteFunction siteFunction);

	boolean deleteSiteFuncById(Integer id);

	/**
	 * 根据系统模块ID 查询站点功能
	 * 
	 * @author 廖晓远
	 * @date 2015-5-13 下午7:47:31
	 * 
	 * @param moduleId
	 *            站点模块ID
	 * @return List<SiteFunction> 功能列表
	 */
	List<SiteFunction> getSiteFuncsBySiteModuleId(Integer moduleId);

	/**
	 * 通过功能ID获取系统资源
	 * 
	 * @author 廖晓远
	 * @date 2015-5-13 下午7:49:05
	 * 
	 * @param funcId
	 *            功能ID
	 * @return List<Resource> 系统资源列表
	 */
	List<Resource> getResourcesByFuncId(Integer funcId);

	/**
	 * 通过Scope获取系统资源
	 * 
	 * @author 廖晓远
	 * @date 2015-5-13 下午7:49:56
	 * 
	 * @param scope
	 *            范围
	 * @return List<Resource> 系统资源列表
	 */
	List<Resource> getResourcesByScope(AuthScope scope);

	/**
	 * @author zjl
	 * @date 2015年5月15日 下午4:40:33
	 * 
	 * @param funcId
	 *            功能ID
	 * @param list
	 *            资源ID List
	 */
	void unbindSiteResource(Integer funcId, List<Integer> list);

	/**
	 * 根据功能ID 删除资源功能关联
	 * 
	 * @author zjl
	 * @date 2015年5月15日 下午4:41:52
	 * 
	 * @param funcId
	 */
	void deleteSiteResourceByFuncId(Integer funcId);

	void saveSiteResource(SiteResource siteResource);

	/**
	 * 通过角色ID和模块ID查找站点功能
	 * 
	 * @author 廖晓远
	 * @date 2015-5-14 下午12:37:58
	 * 
	 * @param roleId
	 *            角色ID
	 * @param moduleId
	 *            模块ID
	 * @return List<SiteFunction> 功能列表
	 */
	List<SiteFunction> getSiteFuncsByRoleIdAndModuleId(Integer roleId, Integer moduleId);

	/**
	 * 通过角色ID列表和模块ID查找站点功能
	 * 
	 * @author 廖晓远
	 * @date 2015-6-10 下午4:47:43
	 * 
	 * @param roleIds
	 * @param moduleId
	 * @return
	 */
	List<SiteFunction> getSiteFuncsByRoleIdsAndModuleId(List<Integer> roleIds, Integer moduleId);

	/**
	 * 功能解绑和绑定站点资源
	 * 
	 * @author zjl
	 * @date 2015年5月19日 下午8:35:37
	 * 
	 * @param relChangeInfo
	 */
	boolean updateSiteResources(RelChangeInfo relChangeInfo);

	// ----------------------------------- SiteModule -------------------------------------------------

	/**
	 * 根据ID查询站点模块
	 * 
	 * @author zjl
	 * @date 2015年5月13日 下午3:00:10
	 * 
	 * @param id
	 *            模块ID
	 * @return SiteModule
	 */
	public SiteModule getSiteModuById(Integer id);

	/**
	 * 增加站点模块
	 * 
	 * @author zjl
	 * @date 2015年5月14日 上午10:14:01
	 * 
	 * @param siteModule
	 * @return 是否成功
	 */
	public Boolean createSiteModule(SiteModule siteModule);

	/**
	 * 更新站点模块
	 * 
	 * @author zjl
	 * @date 2015年5月14日 上午10:14:37
	 * 
	 * @param siteModule
	 * @return 是否成功
	 */
	public Boolean updateSiteModule(SiteModule siteModule);

	/**
	 * 根据ID删除站点模块
	 * 
	 * @author zjl
	 * @date 2015年5月13日 下午3:09:16
	 * 
	 * @param id
	 * @return 是否成功
	 */
	public Boolean deleteSiteModuById(Integer id);

	/**
	 * 根据Ids 删除站点模块
	 * 
	 * @author zjl
	 * @date 2015年5月13日 下午3:09:53
	 * 
	 * @param ids
	 *            模块ids
	 * @return 是否成功
	 */
	public boolean deleteSiteModusByIds(List<Integer> ids);

	/**
	 * 分页查询站点模块
	 * 
	 * @author zjl
	 * @date 2015年5月13日 下午3:10:21
	 * 
	 * @return PaginatedList<SiteModule>
	 */
	public PaginatedList<SiteModule> getSiteModulesByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 查询认证范围查询模块
	 * 
	 * @author zjl
	 * @date 2015年5月14日 上午10:16:28
	 * 
	 * @param scope 认证范围
	 * @param cascaded 是否级联获取
	 * @return List<SiteModule>
	 */
	List<SiteModule> getSiteModulesByScope(AuthScope scope, boolean cascaded);

	/**
	 * 通过角色ID查找站点模块
	 * 
	 * @author 廖晓远
	 * @date 2015-5-14 下午12:37:26
	 * 
	 * @param roleId
	 *            角色ID
	 * @return List<SiteModule> 站点模块列表
	 */
	List<SiteModule> getSiteModulesByRoleId(Integer roleId);

	/**
	 * 通过角色ID列表查找站点模块
	 * 
	 * @author 廖晓远
	 * @date 2015-6-10 下午2:00:29
	 * 
	 * @param roleIds
	 * @return
	 */
	List<SiteModule> getSiteModulesByRoleIds(List<Integer> roleIds);

	/**
	 * 根据站点模块和认证范围查询
	 * @author guoyn
	 * @date 2015年9月25日 上午11:17:14
	 * 
	 * @param name
	 * @param scope
	 * @return SiteModule
	 */
	SiteModule getSiteModuByNameAndScope(String name, AuthScope scope);

}
