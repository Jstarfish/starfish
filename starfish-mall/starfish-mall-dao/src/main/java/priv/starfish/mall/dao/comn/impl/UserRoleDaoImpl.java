package priv.starfish.mall.dao.comn.impl;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;
import priv.starfish.common.jdbc.SqlBuilder;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.model.ScopeEntity;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.common.util.TypeUtil;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.comn.entity.Role;
import priv.starfish.mall.comn.entity.UserRole;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.comn.UserRoleDao;

import java.util.List;
import java.util.Map;

@Component("userRoleDao")
public class UserRoleDaoImpl extends BaseDaoImpl<UserRole, Integer> implements UserRoleDao {
	@Override
	public UserRole selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public UserRole selectByUserIdAndRoleIdAndEntityId(Integer userId, Integer roleId, Integer entityId) {
		String sqlId = this.getNamedSqlId("selectByUserIdAndRoleIdAndEntityId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("userId", userId);
		params.put("roleId", roleId);
		params.put("entityId", entityId);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(UserRole userRole) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, userRole);
	}

	@Override
	public int update(UserRole userRole) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, userRole);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<Role> selectRolesByUserId(Integer userId) {
		String sqlId = this.getNamedSqlId("selectRolesByUserId");
		//
		return this.getSqlSession().selectList(sqlId, userId);
	}

	@Override
	public List<Integer> selectRoleIdsByUserId(Integer userId) {
		String sqlId = this.getNamedSqlId("selectRoleIdsByUserId");
		//
		return this.getSqlSession().selectList(sqlId, userId);
	}

	@Override
	public List<ScopeEntity> selectScopeEntitiesByUserId(Integer userId) {
		String sqlId = this.getNamedSqlId("selectScopeEntitiesByUserId");
		//
		List<UserRole> tmpList = this.getSqlSession().selectList(sqlId, userId);
		//
		List<ScopeEntity> retList = TypeUtil.newList(ScopeEntity.class);
		for (int i = 0; i < tmpList.size(); i++) {
			UserRole userRole = tmpList.get(i);
			//
			ScopeEntity scopeEntity = ScopeEntity.newOne();
			scopeEntity.setScope(userRole.getScope().name());
			scopeEntity.setId(userRole.getEntityId());
			//
			retList.add(scopeEntity);
		}
		return retList;
	}

	@Override
	public int deleteByUserIdAndRoleIds(Integer userId, List<Integer> roleIds) {
		if (TypeUtil.isNullOrEmpty(roleIds)) {
			return 0;
		}
		String sqlId = this.getNamedSqlId("deleteByUserIdAndRoleIds");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("userId", userId);
		params.put("roleIds", roleIds);
		//
		return this.getSqlSession().delete(sqlId, params);
	}

	@Override
	public int deleteByUserIdAndScopeAndEntityId(Integer userId, AuthScope authScope, Integer entityId) {
		String sqlId = this.getNamedSqlId("deleteByUserIdAndScopeAndEntityId");
		Map<String, Object> params = this.newParamMap();
		params.put("userId", userId);
		params.put("scope", authScope);
		params.put("entityId", entityId);
		//
		return this.getSqlSession().delete(sqlId, params);
	}

	@Override
	public List<UserRole> selectByUserId(Integer userId) {
		String sqlId = this.getNamedSqlId("selectByUserId");
		//
		return this.getSqlSession().selectList(sqlId, userId);
	}

	@Override
	public List<Role> selectRolesByUserIdAndScopeAndEntityId(Integer userId, AuthScope authScope, Integer entityId) {
		String sqlId = this.getNamedSqlId("selectRolesByUserIdAndScopeAndEntityId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("userId", userId);
		params.put("scope", authScope.name());
		params.put("entityId", entityId);
		//
		return this.getSqlSession().selectList(sqlId, params);
	}

	@Override
	public boolean existsRolesByUserIdAndScopeAndEntityId(Integer userId, AuthScope authScope, Integer entityId) {
		String sqlId = this.getNamedSqlId("existsRolesByUserIdAndScopeAndEntityId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("userId", userId);
		params.put("scope", authScope.name());
		params.put("entityId", entityId);

		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<Integer> selectUserIdsByScopeAndEntityIdAndFilter(AuthScope authScope, Integer entityId, PaginatedFilter paginatedFilter) {
		//
		String sqlId = this.getNamedSqlId("selectUserIdsByScopeAndEntityIdAndFilter");
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		MapContext filterItems = paginatedFilter.getFilterItems();
		filterItems.put("scope", authScope);
		filterItems.put("entityId", entityId);
		//
		String nickName = filterItems.getTypedValue("nickName", String.class);
		filterItems.remove("nickName");
		if (StrUtil.hasText(nickName)) {
			String nickNames = SqlBuilder.likeStrVal(nickName);
			filterItems.put("nickName", nickNames);
		}
		//
		PageList<Integer> PageList = (PageList) getSqlSession().selectList(sqlId, filterItems, pageBounds);
		filterItems.remove("nickName");
		if (StrUtil.hasText(nickName)) {
			filterItems.put("nickName", nickName);
		}
		return this.toPaginatedList(PageList);
	}

	@Override
	public List<Integer> selectAllUserIdsByScopeAndEntityId(AuthScope authScope, Integer entityId) {
		String sqlId = this.getNamedSqlId("selectUserIdsByScopeAndEntityIdAndFilter");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("scope", authScope.name());
		params.put("entityId", entityId);

		return this.getSqlSession().selectList(sqlId, params);
	}

	@Override
	public int deleteByUserId(Integer userId) {
		String sqlId = this.getNamedSqlId("deleteByUserId");
		//
		return this.getSqlSession().delete(sqlId, userId);
	}

	@Override
	public List<UserRole> selectByRoleId(Integer roleId) {
		String sqlId = this.getNamedSqlId("selectByRoleId");
		//
		return this.getSqlSession().selectList(sqlId, roleId);
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
	public List<UserRole> selectByRoleIds(List<Integer> roleIds) {
		String sqlId = this.getNamedSqlId("selectByRoleIds");
		//
		return this.getSqlSession().selectList(sqlId, roleIds);
	}

}