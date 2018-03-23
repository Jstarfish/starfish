package priv.starfish.mall.car.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonDateTimeSerializer;
import priv.starfish.mall.comn.misc.HasLogo;

@Table(name = "car_brand", uniqueConstraints = { @UniqueConstraint(fieldNames = { "name" }) }, desc = "车辆品牌")
public class CarBrand implements Serializable, HasLogo {
	private static final long serialVersionUID = 1L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String name;

	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String py;

	@Column(nullable = false, type = Types.CHAR, length = 1, desc = "首字母")
	private Character zm;

	@Column(type = Types.VARCHAR, length = 60)
	private String logoUuid;

	@Column(type = Types.VARCHAR, length = 30, desc = "image.car / brand")
	private String logoUsage;

	@Column(type = Types.VARCHAR, length = 250)
	private String logoPath;

	@Column(nullable = false, type = Types.INTEGER)
	private Integer seqNo;

	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false")
	private Boolean disabled;

	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date ts;

	@Column(type = Types.INTEGER, desc = "外部id")
	private Integer refId;

	@Column(type = Types.VARCHAR, length = 30, desc = "外部name")
	private String refName;

	/** 图片浏览路径 */
	private String fileBrowseUrl;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPy() {
		return py;
	}

	public void setPy(String py) {
		this.py = py;
	}

	public Character getZm() {
		return zm;
	}

	public void setZm(Character zm) {
		this.zm = zm;
	}

	public String getLogoUuid() {
		return logoUuid;
	}

	public void setLogoUuid(String logoUuid) {
		this.logoUuid = logoUuid;
	}

	public String getLogoUsage() {
		return logoUsage;
	}

	public void setLogoUsage(String logoUsage) {
		this.logoUsage = logoUsage;
	}

	public String getLogoPath() {
		return logoPath;
	}

	public void setLogoPath(String logoPath) {
		this.logoPath = logoPath;
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

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public Integer getRefId() {
		return refId;
	}

	public void setRefId(Integer refId) {
		this.refId = refId;
	}

	public String getRefName() {
		return refName;
	}

	public void setRefName(String refName) {
		this.refName = refName;
	}

	public String getFileBrowseUrl() {
		return fileBrowseUrl;
	}

	public void setFileBrowseUrl(String fileBrowseUrl) {
		this.fileBrowseUrl = fileBrowseUrl;
	}

	@Override
	public String toString() {
		return "CarBrand [id=" + id + ", name=" + name + ", py=" + py + ", zm=" + zm + ", logoUuid=" + logoUuid + ", logoUsage=" + logoUsage + ", logoPath=" + logoPath + ", seqNo=" + seqNo + ", disabled=" + disabled + ", ts=" + ts
				+ ", refId=" + refId + ", refName=" + refName + ", fileBrowseUrl=" + fileBrowseUrl + "]";
	}

}
