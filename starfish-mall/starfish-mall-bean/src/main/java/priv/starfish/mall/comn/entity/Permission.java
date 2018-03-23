package priv.starfish.mall.comn.entity;

import java.io.Serializable;
import java.sql.Types;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.mall.comn.dict.AuthScope;

@Table(name = "permission", uniqueConstraints = { @UniqueConstraint(fieldNames = { "scope", "code" }) }, desc = "权限表")
public class Permission implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(nullable = false, type = Types.INTEGER)
	@ForeignKey(refEntityClass = Module.class, refFieldName = "id")
	private Integer moduleId;
	
	private Module module;

	@Column(nullable = false, type = Types.VARCHAR, length = 15, desc = "认证范围")
	private AuthScope scope;

	@Column(nullable = false, type = Types.VARCHAR, length = 90)
	private String code;

	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String name;

	@Column(name = "`desc`", type = Types.VARCHAR, length = 250)
	private String desc;

	@Column(type = Types.BOOLEAN, nullable = false, defaultValue = "true")
	private Boolean grantable;

	@Column(type = Types.INTEGER, nullable = false, defaultValue = "1")
	private Integer seqNo;

	@Column(type = Types.BOOLEAN, nullable = false, defaultValue = "false")
	private Boolean disabled;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getModuleId() {
		return moduleId;
	}

	public void setModuleId(Integer moduleId) {
		this.moduleId = moduleId;
	}

	public Module getModule() {
		return module;
	}

	public void setModule(Module module) {
		this.module = module;
	}

	public AuthScope getScope() {
		return scope;
	}

	public void setScope(AuthScope scope) {
		this.scope = scope;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Boolean getGrantable() {
		return grantable;
	}

	public void setGrantable(Boolean grantable) {
		this.grantable = grantable;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	@Override
	public String toString() {
		return "Permission [id=" + id + ", moduleId=" + moduleId + ", scope=" + scope + ", code=" + code + ", name=" + name + ", desc=" + desc + ", grantable=" + grantable + ", seqNo=" + seqNo + ", disabled=" + disabled + "]";
	}

}
