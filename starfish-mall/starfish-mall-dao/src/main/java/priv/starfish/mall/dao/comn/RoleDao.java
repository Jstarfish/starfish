package priv.starfish.mall.dao.comn;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.comn.entity.Role;

@IBatisSqlTarget
public interface RoleDao extends BaseDao<Role, Integer> {
	Role selectById(Integer id);

	Role selectByScopeAndEntityIdAndName(AuthScope scope, Integer entityId, String name);

	int insert(Role role);

	int update(Role role);

	int deleteById(Integer id);

	/**
	 * 批量删除
	 * 
	 * @author 毛智东
	 * @date 2015年6月2日 下午3:56:36
	 * 
	 * @param ids
	 * @return
	 */
	int deleteByIds(List<Integer> ids);

	/**
	 * 根据给定的scope返回内建的admin角色
	 * 
	 * @author koqiui
	 * @date 2015年11月11日 下午8:06:17
	 * 
	 * @param scope
	 * @return
	 */
	Role selectBuiltInAdminByScope(AuthScope scope);

	/**
	 * 查询某认证范围下某实体的角色列表
	 * 
	 * @author guoyn
	 * @date 2015年8月20日 上午10:25:55
	 * 
	 * @param scope
	 *            认证范围
	 * @param entityId
	 *            实体id
	 * @param includeSysSet
	 *            是否包含系统设置的角色
	 * @return List<Role>
	 */
	List<Role> selectByScopeAndEntityId(AuthScope scope, Integer entityId, Boolean includeSysSet);

	/**
	 * 查询某认证范围下某实体的角色列表
	 * 
	 * @author guoyn
	 * @date 2015年8月20日 上午10:27:09
	 * 
	 * @param scope
	 *            认证范围
	 * @param entityId
	 *            实体id
	 * @param grantable
	 *            true：可分配的角色，false：不可分配的角色
	 * @return List<Role>
	 */
	List<Role> selectByScopeAndEntityIdAndGrantable(AuthScope scope, Integer entityId, Boolean grantable);
}
