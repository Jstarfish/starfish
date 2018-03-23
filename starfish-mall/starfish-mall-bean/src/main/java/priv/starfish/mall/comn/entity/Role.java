package priv.starfish.mall.comn.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.common.util.BoolUtil;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.comn.dict.AuthScope;

@Table(name = "role", uniqueConstraints = { @UniqueConstraint(fieldNames = { "scope", "entityId", "name" }) }, desc = "角色表")
public class Role implements Serializable {
	private static final long serialVersionUID = -3401932103183775309L;

	/** 内建的或系统管理员(sysadmin) 创建的角色所属entityId */
	public static final int SysOwnerId = -1;

	/** 内建管理员角色 对应的name 常量值（admin角色全部为builtIn == true） */
	public static final String ADMIN_NAME = "admin";

	/** 是否管理员 */
	public boolean isAdmin() {
		return BoolUtil.isTrue(this.builtIn) && ADMIN_NAME.equals(this.name);
	}

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(nullable = false, type = Types.VARCHAR, length = 15, desc = "角色范围")
	private AuthScope scope;

	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String name;

	@Column(type = Types.VARCHAR, length = 30, desc = "别名（主要内建角色用）")
	private String alias;

	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false", desc = "是否内建的角色(默认为 false，不可删除)")
	private Boolean builtIn;

	@Column(nullable = false, type = Types.INTEGER, desc = "角色所属实体id")
	private Integer entityId;

	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "true", desc = "是否可赋予他人")
	private Boolean grantable;

	@Column(name = "`desc`", type = Types.VARCHAR, length = 250)
	private String desc;

	@Column(nullable = false, type = Types.INTEGER)
	private Integer seqNo;

	@Column(nullable = false, type = Types.TIMESTAMP)
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date ts;

	//
	private List<Permission> perms;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public AuthScope getScope() {
		return scope;
	}

	public void setScope(AuthScope scope) {
		this.scope = scope;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	/** 显示名称 */
	public String getDispName() {
		return StrUtil.hasText(this.alias) ? this.alias : this.name;
	}

	public Boolean getBuiltIn() {
		return builtIn;
	}

	public void setBuiltIn(Boolean builtIn) {
		this.builtIn = builtIn;
	}

	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	public Boolean getGrantable() {
		return grantable;
	}

	public void setGrantable(Boolean grantable) {
		this.grantable = grantable;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public List<Permission> getPerms() {
		return perms;
	}

	public void setPerms(List<Permission> perms) {
		this.perms = perms;
	}

	@Override
	public String toString() {
		return "Role [id=" + id + ", scope=" + scope + ", name=" + name + ", alias=" + alias + ", builtIn=" + builtIn + ", entityId=" + entityId + ", grantable=" + grantable + ", desc=" + desc + ", seqNo=" + seqNo + ", ts=" + ts
				+ ", perms=" + perms + "]";
	}

}
