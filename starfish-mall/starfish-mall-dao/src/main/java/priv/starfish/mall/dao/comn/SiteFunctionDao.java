package priv.starfish.mall.dao.comn;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.comn.entity.SiteFunction;

@IBatisSqlTarget
public interface SiteFunctionDao extends BaseDao<SiteFunction, Integer> {
	SiteFunction selectById(Integer id);

	SiteFunction selectByModuleIdAndName(Integer moduleId, String name);

	int insert(SiteFunction siteFunction);

	int update(SiteFunction siteFunction);

	int deleteById(Integer id);

	/**
	 * 根据系统模块ID 查询站点功能
	 * 
	 * @author 廖晓远
	 * @date 2015-5-14 下午1:05:55
	 * 
	 * @param moduleId
	 *            站点模块ID
	 * @return List<SiteFunction> 功能列表
	 */
	List<SiteFunction> selectByModuleId(Integer moduleId);

	/**
	 * 通过Ids获取功能
	 * 
	 * @author 廖晓远
	 * @date 2015-5-14 下午1:06:39
	 * 
	 * @param funcIds
	 *            功能Id列表
	 * @return List<SiteFunction> 功能列表
	 */
	List<SiteFunction> selectByFuncIds(List<Integer> funcIds);

	/**
	 * 通过功能ID获取站点模块ID列表
	 * 
	 * @author 廖晓远
	 * @date 2015-5-14 下午1:07:32
	 * 
	 * @param funcIds
	 *            功能Id列表
	 * @return List<Integer> 站点模块Id列表
	 */
	List<Integer> selectModuleIdsByFuncIds(List<Integer> funcIds);

	/**
	 * 通过Scope获取系统功能
	 * 
	 * @author 廖晓远
	 * @date 2015-5-13 下午7:49:56
	 * 
	 * @param scope
	 * @return List<Resource>
	 */
	List<SiteFunction> selectByFuncIdsAndModule(List<Integer> funcIds, Integer siteModuleId);
}