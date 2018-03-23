package priv.starfish.mall.dao.comn.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.common.util.TypeUtil;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.comn.RolePermissionDao;
import priv.starfish.mall.comn.entity.RolePermission;

@Component("rolePermissionDao")
public class RolePermissionDaoImpl extends BaseDaoImpl<RolePermission, Integer> implements RolePermissionDao {
	@Override
	public RolePermission selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public RolePermission selectByRoleIdAndPermId(Integer roleId, Integer permId) {
		String sqlId = this.getNamedSqlId("selectByRoleIdAndPermId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("roleId", roleId);
		params.put("permId", permId);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(RolePermission rolePermission) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, rolePermission);
	}

	@Override
	public int update(RolePermission rolePermission) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, rolePermission);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public int deleteByPermId(Integer permId) {
		String sqlId = this.getNamedSqlId("deleteByPermId");
		//
		return this.getSqlSession().delete(sqlId, permId);
	}

	/**
	 * 批量删除
	 */
	@Override
	public int deleteByPermIds(List<Integer> permIds) {
		if (TypeUtil.isNullOrEmpty(permIds)) {
			return 0;
		}
		//
		String sqlId = this.getNamedSqlId("deleteByPermIds");
		//
		return this.getSqlSession().delete(sqlId, permIds);
	}

	@Override
	public List<RolePermission> selectByRoleId(Integer roleId) {
		String sqlId = this.getNamedSqlId("selectByRoleId");
		//
		return this.getSqlSession().selectList(sqlId, roleId);
	}

	@Override
	public List<Integer> selectIdsByRoleId(Integer roleId) {
		String sqlId = this.getNamedSqlId("selectIdsByRoleId");
		//
		return this.getSqlSession().selectList(sqlId, roleId);
	}

	@Override
	public int deleteByRoleIdAndPermId(Integer roleId, Integer permId) {
		String sqlId = this.getNamedSqlId("deleteByRoleIdAndPermId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("roleId", roleId);
		params.put("permId", permId);
		//
		return this.getSqlSession().delete(sqlId, params);
	}

	@Override
	public int deleteByRoleId(Integer roleId) {
		String sqlId = this.getNamedSqlId("deleteByRoleId");
		//
		return this.getSqlSession().delete(sqlId, roleId);
	}

	@Override
	public int deleteByRoleIds(List<Integer> roleIds) {
		String sqlId = this.getNamedSqlId("deleteByRoleIds");
		//
		return this.getSqlSession().delete(sqlId, roleIds);
	}

	@Override
	public List<RolePermission> selectByRoleIds(List<Integer> roleIds) {
		String sqlId = this.getNamedSqlId("selectByRoleIds");
		//
		return this.getSqlSession().selectList(sqlId, roleIds);
	}

	@Override
	public List<Integer> selectPermIdsByRoleId(Integer roleId) {
		String sqlId = this.getNamedSqlId("selectPermIdsByRoleId");
		//
		return this.getSqlSession().selectList(sqlId, roleId);
	}

	@Override
	public List<Integer> selectPermIdsByRoleIds(List<Integer> roleIds) {
		if (TypeUtil.isNullOrEmpty(roleIds)) {
			return TypeUtil.newEmptyList(Integer.class);
		}
		//
		String sqlId = this.getNamedSqlId("selectPermIdsByRoleIds");
		//
		return this.getSqlSession().selectList(sqlId, roleIds);
	}

	@Override
	public List<Integer> selectPermIdsByRoleIdOrderByModuleId(Integer roleId, Boolean isDesc) {
		String sqlId = this.getNamedSqlId("selectPermIdsByRoleIdOrderByModuleId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("roleId", roleId);
		params.put("order", !isDesc ? "ASC" : "DESC");
		//
		return this.getSqlSession().selectList(sqlId, params);
	}
}