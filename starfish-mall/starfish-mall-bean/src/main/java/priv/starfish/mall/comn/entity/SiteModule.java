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
import priv.starfish.mall.comn.dict.AuthScope;

@Table(name = "site_module", uniqueConstraints = { @UniqueConstraint(fieldNames = { "scope", "name" }) })
public class SiteModule implements Serializable {
	private static final long serialVersionUID = 1L;
	/**
	 * id 主键
	 */
	@Id(type = Types.INTEGER)
	private Integer id;

	/**
	 * 认证/授权范围
	 */
	@Column(nullable = false, type = Types.VARCHAR, length = 15, desc = "认证/授权范围")
	private AuthScope scope;
	/**
	 * 名称
	 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String name;
	/**
	 * 描述
	 */
	@Column(name = "`desc`", type = Types.VARCHAR, length = 250)
	private String desc;

	/** icon UUID */
	@Column(type = Types.VARCHAR, length = 60)
	private String iconUuid;

	/** icon Usage */
	@Column(type = Types.VARCHAR, length = 30)
	private String iconUsage;
	/**
	 * 图标连接
	 */
	@Column(nullable = false, type = Types.VARCHAR, length = 250)
	private String iconPath;

	/** 序号 */
	@Column(nullable = false, type = Types.INTEGER, defaultValue = "1")
	private Integer seqNo;

	/** 时间戳 */
	@Column(nullable = false, type = Types.TIMESTAMP)
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date ts = new Date();

	/** 功能列表 */
	private List<SiteFunction> functions;

	public SiteModule() {
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getIconPath() {
		return iconPath;
	}

	public void setIconPath(String iconPath) {
		this.iconPath = iconPath;
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

	public List<SiteFunction> getFunctions() {
		return functions;
	}

	public void setFunctions(List<SiteFunction> functions) {
		this.functions = functions;
	}

	public String getIconUuid() {
		return iconUuid;
	}

	public void setIconUuid(String iconUuid) {
		this.iconUuid = iconUuid;
	}

	public String getIconUsage() {
		return iconUsage;
	}

	public void setIconUsage(String iconUsage) {
		this.iconUsage = iconUsage;
	}

	@Override
	public String toString() {
		return "SiteModule [id=" + id + ", scope=" + scope + ", name=" + name + ", desc=" + desc + ", iconUuid=" + iconUuid + ", iconUsage=" + iconUsage + ", iconPath=" + iconPath + ", seqNo=" + seqNo + ", ts=" + ts + ", functions="
				+ functions + "]";
	}

}
