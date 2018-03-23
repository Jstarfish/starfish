package priv.starfish.mall.dao.comn;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.comn.entity.SiteModule;

@IBatisSqlTarget
public interface SiteModuleDao extends BaseDao<SiteModule, Integer> {
	/**
	 * 根据ID查询站点模块
	 */
	SiteModule selectById(Integer id);

	/**
	 * 增加站点模块
	 */
	int insert(SiteModule siteModule);

	/**
	 * 更新站点模块
	 */
	int update(SiteModule siteModule);

	/**
	 * 根据Id删除站点模块
	 */
	int deleteById(Integer id);

	/**
	 * 分页查询站点模块
	 * 
	 * @author zjl
	 * @date 2015年5月13日 下午3:16:00
	 * 
	 * @param paginatedFilter
	 *            分页信息及过滤条件
	 * @return PaginatedList<SiteModule>
	 */
	 PaginatedList<SiteModule> selectByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 根据认证范围查询站点模块
	 * 
	 * @author zjl
	 * @date 2015年5月14日 上午10:08:48
	 * 
	 * @param scope
	 *            认证范围
	 * @return List<SiteModule>
	 */
	List<SiteModule> selectByScope(AuthScope scope);

	/**
	 * 通过Id集合查询模块
	 * 
	 * @author zjl
	 * @date 2015年5月13日 下午3:16:52
	 * 
	 * @param siteModuleIds
	 * @return List<SiteModule>
	 */
	List<SiteModule> selectByIds(List<Integer> siteModuleIds);

	/**
	 * 根据站点模块名称和认证范围查询
	 * @author guoyn
	 * @date 2015年9月25日 上午11:18:52
	 * 
	 * @param name
	 * @param scope
	 * @return SiteModule
	 */
	SiteModule selectByNameAndScope(String name, AuthScope scope);
}