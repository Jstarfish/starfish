package priv.starfish.mall.comn.entity;

import java.io.Serializable;
import java.sql.Types;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.mall.comn.dict.AuthScope;

@Table(name = "resource", uniqueConstraints = { @UniqueConstraint(fieldNames = { "type", "pattern" }) })
public class Resource implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(nullable = false, type = Types.VARCHAR, length = 15, desc = "认证范围")
	private AuthScope scope;

	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String name;

	@Column(nullable = false, type = Types.VARCHAR, length = 15)
	private String type;

	@Column(nullable = false, type = Types.VARCHAR, length = 200)
	private String pattern;

	@Column(type = Types.BOOLEAN, defaultValue = "false")
	private Boolean pageFlag;

	@Column(name = "`desc`", type = Types.VARCHAR, length = 250)
	private String desc;

	@Column(nullable = false, type = Types.INTEGER, defaultValue = "1")
	private Integer seqNo;

	@Column(type = Types.INTEGER)
	@ForeignKey(refEntityClass = Permission.class, refFieldName = "id")
	private Integer permId;

	private Permission permission;

	public Resource() {
		super();
	}

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getPattern() {
		return pattern;
	}

	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	public Boolean getPageFlag() {
		return pageFlag;
	}

	public void setPageFlag(Boolean pageFlag) {
		this.pageFlag = pageFlag;
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

	public Integer getPermId() {
		return permId;
	}

	public void setPermId(Integer permId) {
		this.permId = permId;
	}

	public Permission getPermission() {
		return permission;
	}

	public void setPermission(Permission permission) {
		this.permission = permission;
	}

	@Override
	public String toString() {
		return "Resource [id=" + id + ", scope=" + scope + ", name=" + name + ", type=" + type + ", pattern=" + pattern + ", pageFlag=" + pageFlag + ", desc=" + desc + ", seqNo=" + seqNo + ", permId=" + permId + ", permission="
				+ permission + "]";
	}
}
