package priv.starfish.mall.categ.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonShortDateTimeSerializer;

@Table(name = "brand_def", uniqueConstraints = { @UniqueConstraint(fieldNames = { "code" }) }, desc = "品牌定义表")
public class BrandDef implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(type = Types.VARCHAR, length = 30, nullable = false)
	private String code;

	@Column(type = Types.VARCHAR, length = 10, nullable = false)
	private String name;

	@Column(type = Types.VARCHAR, length = 10)
	private String py;

	/** logo UUID */
	@Column(type = Types.VARCHAR, length = 60)
	private String logoUuid;

	/** logo Usage */
	@Column(type = Types.VARCHAR, length = 30)
	private String logoUsage;

	/** 品牌LOGO图片地址 */
	@Column(type = Types.VARCHAR, length = 250)
	private String logoPath;

	@Column(type = Types.INTEGER, nullable = false, defaultValue = "1")
	private Integer seqNo;

	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date ts;

	/** 图片浏览路径 */
	private String fileBrowseUrl;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
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

	public String getPy() {
		return py;
	}

	public void setPy(String py) {
		this.py = py;
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

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public String getFileBrowseUrl() {
		return fileBrowseUrl;
	}

	public void setFileBrowseUrl(String fileBrowseUrl) {
		this.fileBrowseUrl = fileBrowseUrl;
	}

	@Override
	public String toString() {
		return "BrandDef [id=" + id + ", code=" + code + ", name=" + name + ", py=" + py + ", logoUuid=" + logoUuid + ", logoUsage=" + logoUsage + ", logoPath=" + logoPath + ", seqNo=" + seqNo + ", ts=" + ts + "]";
	}

}
