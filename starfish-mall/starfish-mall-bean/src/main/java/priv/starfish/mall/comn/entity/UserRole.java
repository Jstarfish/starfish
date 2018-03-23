package priv.starfish.mall.comn.entity;

import java.io.Serializable;
import java.sql.Types;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.mall.comn.dict.AuthScope;

@Table(name = "user_role", uniqueConstraints = { @UniqueConstraint(fieldNames = { "userId", "roleId", "entityId" }) })
public class UserRole implements Serializable {
	private static final long serialVersionUID = 1L;
	//
	/** 特殊的系统后台管理对象id */
	public static final int SysEntityId = 0;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(nullable = false, type = Types.INTEGER)
	@ForeignKey(refEntityClass = User.class, refFieldName = "id")
	private Integer userId;

	@Column(nullable = false, type = Types.INTEGER)
	@ForeignKey(refEntityClass = Role.class, refFieldName = "id")
	private Integer roleId;

	@Column(nullable = false, type = Types.VARCHAR, length = 15, desc = "角色应用的范围")
	private AuthScope scope;

	@Column(nullable = false, type = Types.INTEGER, desc = "角色应用的范围内对象id")
	private Integer entityId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public AuthScope getScope() {
		return scope;
	}

	public void setScope(AuthScope scope) {
		this.scope = scope;
	}

	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	@Override
	public String toString() {
		return "UserRole [id=" + id + ", userId=" + userId + ", roleId=" + roleId + ", scope=" + scope + ", entityId=" + entityId + "]";
	}

}
