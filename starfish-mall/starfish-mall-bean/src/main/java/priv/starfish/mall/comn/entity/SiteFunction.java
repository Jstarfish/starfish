package priv.starfish.mall.comn.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonShortDateTimeSerializer;

@Table(name = "site_func", uniqueConstraints = { @UniqueConstraint(fieldNames = { "moduleId", "name" }) })
public class SiteFunction implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * ID
	 */
	@Id(type = Types.INTEGER)
	private Integer id;

	/**
	 * 站点模块ID
	 */
	@Column(nullable = false, type = Types.INTEGER)
	@ForeignKey(refEntityClass = SiteModule.class, refFieldName = "id")
	private Integer moduleId;

	/**
	 * 功能名称
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
	 * 图标
	 */
	@Column(nullable = false, type = Types.VARCHAR, length = 250)
	private String iconPath;

	/**
	 * 排序
	 */
	@Column(nullable = false, type = Types.INTEGER, defaultValue = "1")
	private Integer seqNo;

	/** 时间戳 */
	@Column(nullable = false, type = Types.TIMESTAMP)
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date ts = new Date();

	/** 资源列表 */
	private List<Resource> resources;

	public SiteFunction() {
		super();
	}

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

	public List<Resource> getResources() {
		return resources;
	}

	public void setResources(List<Resource> resources) {
		this.resources = resources;
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
		return "SiteFunction [id=" + id + ", moduleId=" + moduleId + ", name=" + name + ", desc=" + desc + ", iconUuid=" + iconUuid + ", iconUsage=" + iconUsage + ", iconPath=" + iconPath + ", seqNo=" + seqNo + ", ts=" + ts
				+ ", resources=" + resources + "]";
	}

}
