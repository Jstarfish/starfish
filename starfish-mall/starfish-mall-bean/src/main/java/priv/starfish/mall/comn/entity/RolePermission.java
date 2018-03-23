package priv.starfish.mall.comn.entity;

import java.io.Serializable;
import java.sql.Types;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;

@Table(name = "role_permission", uniqueConstraints = { @UniqueConstraint(fieldNames = { "roleId", "permId" }) })
public class RolePermission implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(nullable = false, type = Types.INTEGER)
	@ForeignKey(refEntityClass = Role.class, refFieldName = "id")
	private Integer roleId;

	@Column(nullable = false, type = Types.INTEGER)
	@ForeignKey(refEntityClass = Permission.class, refFieldName = "id")
	private Integer permId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRoleId() {
		return roleId;
	}

	public void setRoleId(Integer roleId) {
		this.roleId = roleId;
	}

	public Integer getPermId() {
		return permId;
	}

	public void setPermId(Integer permId) {
		this.permId = permId;
	}

	@Override
	public String toString() {
		return "RolePermission [id=" + id + ", roleId=" + roleId + ", permId=" + permId + "]";
	}

}
