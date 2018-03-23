package priv.starfish.mall.dao.comn;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.comn.entity.Resource;
import priv.starfish.mall.comn.entity.SiteResource;

@IBatisSqlTarget
public interface SiteResourceDao extends BaseDao<SiteResource, Integer> {
	SiteResource selectById(Integer id);

	SiteResource selectByFuncIdAndResId(Integer funcId, Integer resId);

	int insert(SiteResource siteResource);

	int update(SiteResource siteResource);

	int deleteById(Integer id);

	/**
	 * 通过系统资源ID列表和功能ID获取
	 * 
	 * @author 张军磊
	 * @date 2015-5-14 上午9:39:17
	 * 
	 * @param resId
	 *            系统资源ID
	 * @return List<SiteResource> 站点资源列表
	 */
	List<SiteResource> selectByFuncIdAndResIds(Integer funcId, List<Integer> resIds);

	/**
	 * 通过功能ID和资源列表批量删除
	 * 
	 * @author 张军磊
	 * @date 2015-5-14 上午9:36:11
	 * 
	 * @param resIds
	 *            系统资源ID列表
	 * @param funcId
	 *            功能ID
	 * @return 影响条数
	 */
	int deleteByFuncIdAndResIds(Integer funcId, List<Integer> resIds);

	/**
	 * 通过功能Id获取
	 * 
	 * @author 廖晓远
	 * @date 2015-6-11 下午8:19:58
	 * 
	 * @param funcId
	 * @return
	 */
	List<SiteResource> selectByFuncId(Integer funcId);

	/**
	 * 通过功能ID删除
	 * 
	 * @author 张军磊
	 * @date 2015-5-14 上午9:38:33
	 * 
	 * @param funcId
	 *            功能ID
	 * @return 影响条数
	 */
	int deleteByFuncId(Integer funcId);

	/**
	 * 通过资源ID删除
	 * 
	 * @author 廖晓远
	 * @date 2015年8月13日 上午11:15:32
	 * 
	 * @param resId
	 * @return
	 */
	int deleteByResId(Integer resId);

	/**
	 * 通过资源ID列表删除
	 * 
	 * @author 廖晓远
	 * @date 2015年8月13日 上午11:18:42
	 * 
	 * @param resIds
	 * @return
	 */
	int deleteByResIds(List<Integer> resIds);

	/**
	 * 通过系统资源ID获取
	 * 
	 * @author 张军磊
	 * @date 2015-5-14 上午9:39:17
	 * 
	 * @param resId
	 *            系统资源ID
	 * @return List<SiteResource> 站点资源列表
	 */
	List<SiteResource> selectByResId(Integer resId);

	/**
	 * 通过系统资源ID列表获取
	 * 
	 * @author 张军磊
	 * @date 2015-5-14 上午9:39:17
	 * 
	 * @param resIds
	 *            系统资源ID列表
	 * @return List<SiteResource> 站点资源列表
	 */
	List<SiteResource> selectByResIds(List<Integer> resIds);

	/**
	 * 通过系统资源ID列表获取
	 * 
	 * @author 张军磊
	 * @date 2015-5-14 上午9:39:17
	 * 
	 * @param resIds
	 *            系统资源ID列表
	 * @return List<SiteResource> 站点资源列表
	 */
	List<Integer> selectFuncIdsByResIds(List<Integer> resIds);

	/**
	 * 根据功能Id查询系统资源功能
	 * 
	 * @author 张军磊
	 * @date 2015-5-14 上午9:39:17
	 * 
	 * @param funcId
	 *            功能Id
	 * @return List<Resource> 系统资源列表
	 */
	List<Resource> selectResourcesByFuncId(Integer funcId);

}
