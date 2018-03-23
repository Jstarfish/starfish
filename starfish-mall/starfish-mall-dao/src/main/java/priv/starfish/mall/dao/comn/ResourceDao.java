package priv.starfish.mall.dao.comn;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.comn.entity.Resource;

@IBatisSqlTarget
public interface ResourceDao extends BaseDao<Resource, Integer> {
	Resource selectById(Integer id);

	Resource selectByTypeAndPattern(String type, String pattern);

	int update(Resource resource);

	int deleteById(Integer id);

	/**
	 * 批量删除系统资源
	 * 
	 * @author 郭营
	 * @date 2015年5月14日 下午2:53:41
	 * 
	 * @param ids
	 * @return int
	 */
	int deleteByIds(List<Integer> ids);

	/**
	 * 查询系统资源列表
	 * 
	 * @author 郭营
	 * @date 2015年5月13日 下午7:39:38
	 * @param paginatedFilter
	 *            = scope , like name, like pattern
	 * @return 系统资源列表
	 */
	public PaginatedList<Resource> selectByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 通过系统资源Id列表获取资源列表
	 * 
	 * @author 廖晓远
	 * @date 2015-5-14 下午12:46:04
	 * 
	 * @param resIds
	 *            Ids
	 * @return List<Resource> 系统资源列表
	 */
	List<Resource> selectByIds(List<Integer> ids);

	/**
	 * 通过地址获取系统资源
	 * 
	 * @author 廖晓远
	 * @date 2015年7月30日 上午11:40:54
	 * 
	 * @param pattern
	 * @return
	 */
	List<Resource> selectByPattern(String pattern);

	/**
	 * 通过权限Id获取资源
	 * 
	 * @author 郭营
	 * @date 2015年5月14日 下午2:56:37
	 * 
	 * @param permId
	 * @return Resource
	 */
	List<Resource> selectByPermId(int permId);

	/**
	 * 通过权限Id获取资源列表
	 * 
	 * @author 廖晓远
	 * @date 2015-5-14 下午12:44:11
	 * 
	 * @param permIds
	 *            权限ID列表
	 * @return List<Resource> 系统资源列表
	 */
	List<Resource> selectByPermIds(List<Integer> permIds);

	/**
	 * 通过权限Id获取资源ID列表
	 * 
	 * @author 廖晓远
	 * @date 2015-5-14 下午12:44:11
	 * 
	 * @param permIds
	 *            权限ID列表
	 * @return List<Resource> 系统资源ID列表
	 */
	List<Integer> selectIdsByPermIds(List<Integer> permIds);

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
	List<Resource> selectUrls(boolean protectedOnly);

	/**
	 * 根据认证范围查询资源列表
	 * @author guoyn
	 * @date 2015年9月25日 下午2:53:31
	 * 
	 * @param scope
	 * @return List<Resource>
	 */
	List<Resource> selectByScope(AuthScope scope);
}