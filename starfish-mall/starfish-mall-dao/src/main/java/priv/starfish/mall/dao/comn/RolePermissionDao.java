package priv.starfish.mall.dao.comn;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.comn.entity.RolePermission;

@IBatisSqlTarget
public interface RolePermissionDao extends BaseDao<RolePermission, Integer> {
	RolePermission selectById(Integer id);

	RolePermission selectByRoleIdAndPermId(Integer roleId, Integer permId);

	int insert(RolePermission rolePermission);

	int update(RolePermission rolePermission);

	int deleteById(Integer id);

	int deleteByPermId(Integer permId);

	
	/** 批量删除 */
	int deleteByPermIds(List<Integer> permIds);

	/**
	 * 根据角色ID查找列表
	 * 
	 * @author 廖晓远
	 * @date 2015-5-14 下午12:13:32
	 * 
	 * @param roleId
	 *            角色ID
	 * @return List<RolePermission> 权限关系列表
	 */
	List<RolePermission> selectByRoleId(Integer roleId);

	/**
	 * 根据角色ID查找ID列表
	 * 
	 * @author 廖晓远
	 * @date 2015-5-14 下午12:15:08
	 * 
	 * @param roleId
	 *            角色ID
	 * @return List<Integer> Id列表
	 */
	List<Integer> selectIdsByRoleId(Integer roleId);

	/**
	 * 根据角色id和权限id删除角色权限
	 * 
	 * @author 毛智东
	 * @date 2015年5月14日 上午9:35:33
	 * 
	 * @param roleId
	 *            角色id
	 * @param permId
	 *            权限id
	 * @return 数据库执行行数
	 */
	int deleteByRoleIdAndPermId(Integer roleId, Integer permId);

	/**
	 * 根据角色id删除角色权限
	 * 
	 * @author 毛智东
	 * @date 2015年6月3日 下午5:30:01
	 * 
	 * @param roleId
	 * @return
	 */
	int deleteByRoleId(Integer roleId);

	/**
	 * 根据角色id列表批量删除
	 * 
	 * @author 毛智东
	 * @date 2015年6月3日 下午8:11:46
	 * 
	 * @param roleIds
	 * @return
	 */
	int deleteByRoleIds(List<Integer> roleIds);

	/**
	 * 根据id列表获得角色权限列表
	 * 
	 * @author 毛智东
	 * @date 2015年6月3日 下午8:13:13
	 * 
	 * @param roleIds
	 * @return
	 */
	List<RolePermission> selectByRoleIds(List<Integer> roleIds);

	/**
	 * 根据角色ID查询权限Ids
	 * 
	 * @author 廖晓远
	 * @date 2015-6-10 下午2:12:06
	 * 
	 * @param roleId
	 * @return
	 */
	List<Integer> selectPermIdsByRoleId(Integer roleId);
	
	/**
	 * 根据角色ID查询权限Ids，是否按照moduleId顺序排列
	 * @author guoyn
	 * @date 2015年8月28日 下午1:56:55
	 * 
	 * @param roleId 角色Id
	 * @param isDesc false：升序，true：降序
	 * @return List<Integer>
	 */
	List<Integer> selectPermIdsByRoleIdOrderByModuleId(Integer roleId, Boolean isDesc);
	
	/**
	 * 根据角色IDs查询权限Ids
	 * 
	 * @author zjl
	 * @date 2015年6月5日 下午3:48:18
	 * 
	 * @param roleIds
	 *            角色Ids
	 * @return List<Integer>
	 */
	List<Integer> selectPermIdsByRoleIds(List<Integer> roleIds);

}