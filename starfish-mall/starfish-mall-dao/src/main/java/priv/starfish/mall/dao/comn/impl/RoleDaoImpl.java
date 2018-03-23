package priv.starfish.mall.dao.comn.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.TypeUtil;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.comn.RoleDao;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.comn.entity.Role;

@Component("roleDao")
public class RoleDaoImpl extends BaseDaoImpl<Role, Integer> implements RoleDao {
	@Override
	public Role selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public Role selectByScopeAndEntityIdAndName(AuthScope scope, Integer entityId, String name) {
		String sqlId = this.getNamedSqlId("selectByScopeAndEntityIdAndName");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("scope", scope);
		params.put("entityId", entityId);
		params.put("name", name);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(Role role) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, role);
	}

	@Override
	public int update(Role role) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, role);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public int deleteByIds(List<Integer> ids) {
		if (TypeUtil.isNullOrEmpty(ids)) {
			return 0;
		}
		//
		String sqlId = this.getNamedSqlId("deleteByIds");
		//
		return this.getSqlSession().delete(sqlId, ids);
	}

	@Override
	public Role selectBuiltInAdminByScope(AuthScope scope) {
		String sqlId = this.getNamedSqlId("selectBuiltInAdminByScope");
		//
		MapContext params = MapContext.newOne();
		params.put("scope", scope);
		params.put("name", Role.ADMIN_NAME);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public List<Role> selectByScopeAndEntityId(AuthScope scope, Integer entityId, Boolean includeSysSet) {
		String sqlId = this.getNamedSqlId("selectByScopeAndEntityId");
		//
		MapContext params = MapContext.newOne();
		params.put("scope", scope);
		params.put("entityId", entityId);
		params.put("includeSysSet", includeSysSet);
		//
		return this.getSqlSession().selectList(sqlId, params);
	}

	@Override
	public List<Role> selectByScopeAndEntityIdAndGrantable(AuthScope scope, Integer entityId, Boolean grantable) {
		String sqlId = this.getNamedSqlId("selectByScopeAndEntityId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("scope", scope);
		params.put("entityId", entityId);
		params.put("grantable", grantable);
		//
		return this.getSqlSession().selectList(sqlId, params);
	}

}