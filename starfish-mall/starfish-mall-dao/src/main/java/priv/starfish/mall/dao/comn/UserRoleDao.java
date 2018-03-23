package priv.starfish.mall.dao.comn;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.model.ScopeEntity;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.comn.entity.Role;
import priv.starfish.mall.comn.entity.UserRole;
import priv.starfish.mall.dao.base.BaseDao;

import java.util.List;

@IBatisSqlTarget
public interface UserRoleDao extends BaseDao<UserRole, Integer> {
	UserRole selectById(Integer id);

	UserRole selectByUserIdAndRoleIdAndEntityId(Integer userId, Integer roleId, Integer entityId);

	int insert(UserRole userRole);

	int update(UserRole userRole);

	int deleteById(Integer id);

	/**
	 * 通过用户Id获取所拥有角色
	 * 
	 * @author 廖晓远
	 * @date 2015-5-14 上午9:32:03
	 * 
	 * @param userId
	 *            用户ID
	 * @return List<Role> 角色列表
	 */
	List<Role> selectRolesByUserId(Integer userId);

	/**
	 * 获取给定用户的所有角色id列表
	 * 
	 * @author koqiui
	 * @date 2015年8月10日 下午4:34:46
	 * 
	 * @param userId
	 * @return
	 */
	List<Integer> selectRoleIdsByUserId(Integer userId);

	/**
	 * 批量删除用户下的指定角色
	 * 
	 * @author zjl
	 * @date 2015年6月6日 上午11:59:30
	 * 
	 * @param userId
	 *            用户ID
	 * @param roleListIds
	 *            角色IDs
	 * @return
	 */
	int deleteByUserIdAndRoleIds(Integer userId, List<Integer> roleListIds);

	/**
	 * 删除某认证范围下的某个实体下的某个用户的相关角色列表
	 * 
	 * @author guoyn
	 * @date 2015年8月20日 上午10:57:50
	 * 
	 * @param userId
	 * @param authScope
	 * @param entityId
	 * @return int
	 */
	int deleteByUserIdAndScopeAndEntityId(Integer userId, AuthScope authScope, Integer entityId);

	/**
	 * 根据用户ID 查询UserRole
	 * 
	 * @author 廖晓远
	 * @date 2015-6-9 下午8:24:40
	 * 
	 * @param userId
	 * @return
	 */
	List<UserRole> selectByUserId(Integer userId);

	/**
	 * 获取用户所有可用的scopeEnitiy(scope + entityId)
	 * 
	 * @author koqiui
	 * @date 2016年1月20日 下午4:57:54
	 * 
	 * @param userId
	 * @return
	 */
	List<ScopeEntity> selectScopeEntitiesByUserId(Integer userId);

	/**
	 * 根据用户Id和scope和entityId查询角色列表
	 * 
	 * @author guoyn
	 * @date 2015年8月9日 下午4:46:39
	 * 
	 * @param userId
	 * @param entityId
	 * @return List<Role>
	 */
	List<Role> selectRolesByUserIdAndScopeAndEntityId(Integer userId, AuthScope authScope, Integer entityId);

	/**
	 * 返回给定的用户是否拥有给定的scope和entityId的角色（可简单判断是否具有scope 和 entityId的权限）
	 * 
	 * @author koqiui
	 * @date 2015年12月16日 下午11:36:54
	 * 
	 * @param userId
	 * @param authScope
	 * @param entityId
	 * @return
	 */
	boolean existsRolesByUserIdAndScopeAndEntityId(Integer userId, AuthScope authScope, Integer entityId);

	/**
	 * 根据scope和entityId分页查询用户id列表
	 * 
	 * @author guoyn
	 * @date 2015年8月9日 下午3:39:38
	 * 
	 * @param authScope
	 * @param entityId
	 * @param paginatedFilter
	 *            like nickName AND = phoneNo
	 * @return PaginatedList<Integer>
	 */
	PaginatedList<Integer> selectUserIdsByScopeAndEntityIdAndFilter(AuthScope authScope, Integer entityId, PaginatedFilter paginatedFilter);

	/**
	 * 返回拥有给定的scope和entityId的角色的所有用户（工作人员）id
	 * 
	 * @author koqiui
	 * @date 2015年12月17日 上午12:10:33
	 * 
	 * @param authScope
	 * @param entityId
	 * @return
	 */
	List<Integer> selectAllUserIdsByScopeAndEntityId(AuthScope authScope, Integer entityId);

	/**
	 * 删除某个用户的所有角色
	 * 
	 * @author guoyn
	 * @date 2015年8月20日 上午11:10:34
	 * 
	 * @param userId
	 * @return int
	 */
	int deleteByUserId(Integer userId);

	/**
	 * 根据角色id查找userRole列表
	 * 
	 * @author 毛智东
	 * @date 2015年8月22日 下午5:27:42
	 * 
	 * @param roleId
	 * @return
	 */
	List<UserRole> selectByRoleId(Integer roleId);

	/**
	 * 删除该角色的所有用户关联
	 * 
	 * @author 毛智东
	 * @date 2015年8月22日 下午5:44:48
	 * 
	 * @param roleId
	 * @return
	 */
	int deleteByRoleId(Integer roleId);

	/**
	 * 批量删除该角色的所有用户关联
	 * 
	 * @author 毛智东
	 * @date 2015年8月22日 下午5:44:53
	 * 
	 * @param roleIds
	 * @return
	 */
	int deleteByRoleIds(List<Integer> roleIds);

	/**
	 * 批量根据角色id查找userRole列表
	 * 
	 * @author 毛智东
	 * @date 2015年8月22日 下午5:27:42
	 * 
	 * @param roleIds
	 * @return
	 */
	List<UserRole> selectByRoleIds(List<Integer> roleIds);
}
