package priv.starfish.mall.dao.comn.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.common.util.TypeUtil;
import priv.starfish.mall.dao.comn.PermissionDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.comn.entity.Permission;

@Component("permissionDao")
public class PermissionDaoImpl extends BaseDaoImpl<Permission, Integer> implements PermissionDao {
	@Override
	public Permission selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public Permission selectByScopeAndCode(AuthScope scope, String code) {
		String sqlId = this.getNamedSqlId("selectByScopeAndCode");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("scope", scope);
		params.put("code", code);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(Permission permission) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, permission);
	}

	@Override
	public int update(Permission permission) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, permission);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	/**
	 * 根据模块id获取权限集合
	 */
	@Override
	public List<Permission> selectByModuleId(Integer moduleId) {
		String sqlId = this.getNamedSqlId("selectByModuleId");
		//
		return this.getSqlSession().selectList(sqlId, moduleId);
	}

	@Override
	public Integer updateStatusById(Integer id, Boolean disabled) {
		String sqlId = this.getNamedSqlId("updateStatusById");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("id", id);
		params.put("disabled", disabled);
		//
		return this.getSqlSession().update(sqlId, params);
	}

	/**
	 * 获取系统资源列表
	 * 
	 * @author 郭营
	 * @date 2015年5月13日 下午7:45:30
	 * @param tscope
	 * @return 系统资源列表
	 */
	@Override
	public List<Permission> selectByScope(AuthScope scope) {
		String sqlId = this.getNamedSqlId("selectByScope");
		//
		return this.getSqlSession().selectList(sqlId, scope.name());
	}

	@Override
	public List<Integer> selectModuleIdsByScope(AuthScope scope) {
		String sqlId = this.getNamedSqlId("selectModuleIdsByScope");
		//
		return this.getSqlSession().selectList(sqlId, scope.name());
	}

	/**
	 * 批量更新权限状态
	 */
	@Override
	public Integer updateStatusByIds(List<Integer> ids, Boolean disabled) {
		if (TypeUtil.isNullOrEmpty(ids)) {
			return 0;
		}
		//
		String sqlId = this.getNamedSqlId("updateStatusByIds");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("ids", ids);
		params.put("status", disabled);
		//
		return this.getSqlSession().update(sqlId, params);
	}

}