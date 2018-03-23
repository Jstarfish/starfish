package priv.starfish.mall.dao.comn;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.comn.entity.Permission;

@IBatisSqlTarget
public interface PermissionDao extends BaseDao<Permission, Integer> {
	Permission selectById(Integer id);

	/**
	 * 根据AuthScope和code查询权限
	 * 
	 * @author guoyn
	 * @date 2015年5月14日 上午9:23:12
	 * 
	 * @param scope
	 * @param code
	 * @return 单个权限
	 */
	Permission selectByScopeAndCode(AuthScope scope, String code);

	int insert(Permission permission);

	int update(Permission permission);

	int deleteById(Integer id);

	/**
	 * 根据模块id获取权限集合
	 * 
	 * @author guoyn
	 * @date 2015年5月13日 下午6:36:10
	 * 
	 * @param moduleId
	 * @return List<Permission>
	 */
	List<Permission> selectByModuleId(Integer moduleId);

	/**
	 * 根据认证范围获取权限集合
	 * 
	 * @author guoyn
	 * @date 2015年5月13日 下午6:37:37
	 * 
	 * @param scope
	 * @return List<Permission>
	 */
	List<Permission> selectByScope(AuthScope scope);

	/**
	 * 禁用/启用权限一个权限
	 * 
	 * @author guoyn
	 * @date 2015年5月13日 下午6:38:09
	 * 
	 * @param id
	 * @param disabled
	 *            禁用true 启用false
	 * @return Integer
	 */
	Integer updateStatusById(Integer id, Boolean disabled);

	/**
	 * 获取认证范围下的模块id
	 * 
	 * @author guoyn
	 * @date 2015年5月13日 下午6:39:03
	 * 
	 * @param scope
	 * @return List<Integer>
	 */
	List<Integer> selectModuleIdsByScope(AuthScope scope);

	/**
	 * 批量更新权限状态为status
	 * 
	 * @author guoyn
	 * @date 2015年5月12日 下午6:20:57
	 * 
	 * @param ids
	 *            权限Id集合
	 * @param disabled
	 * @return Integer
	 */
	Integer updateStatusByIds(List<Integer> ids, Boolean disabled);

}
