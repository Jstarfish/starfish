package priv.starfish.mall.mall.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonDateTimeDeserializer;
import priv.starfish.common.json.JsonDateTimeSerializer;
import priv.starfish.mall.comn.misc.HasLogo;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

@Table(name = "mall", uniqueConstraints = { @UniqueConstraint(fieldNames = { "code" }) }, desc = "商城")
public class Mall implements Serializable, HasLogo {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;

	/** code */
	@Column(nullable = false, type = Types.VARCHAR, length = 90)
	private String code;

	/** 运营商id */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer operatorId;

	/** 商城名称 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String name;

	/** 地区编号 */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer regionId;

	/** 地区全称 */
	@Column(nullable = false, type = Types.VARCHAR, length = 60)
	private String regionName;

	/** 街道 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String street;

	@Column(type = Types.VARCHAR, length = 90)
	private String address;

	/** 固话号码 */
	@Column(type = Types.VARCHAR, length = 30)
	private String telNo;

	/** 手机号码 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String phoneNo;

	/** 联系人 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String linkMan;

	/** 权限范围 */
	@Column(nullable = false, type = Types.VARCHAR, length = 1000)
	private String bizScope;

	@Column(type = Types.VARCHAR, length = 60)
	private String logoUuid;

	/** 图片用途 */
	@Column(type = Types.VARCHAR, length = 30)
	private String logoUsage;

	/** 头像 */
	@Column(type = Types.VARCHAR, length = 250)
	private String logoPath;

	/** 注册时间 */
	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date regTime;

	@Column(type = Types.TIMESTAMP)
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	@JsonDeserialize(using = JsonDateTimeDeserializer.class)
	private Date openTime;

	/** ICP 备案证书号 */
	@Column(type = Types.VARCHAR, length = 60)
	private String icpNo;

	@Column(type = Types.VARCHAR, length = 60)
	private String icpUuid;

	/** ICP 文件用途 */
	@Column(type = Types.VARCHAR, length = 30)
	private String icpUsage;

	/** ICP 备案证书路径 */
	@Column(type = Types.VARCHAR, length = 250)
	private String icpPath;

	/** 邮政编码 */
	@Column(type = Types.VARCHAR, length = 8)
	private String postCode;

	/** 营业执照id */
	@Column(type = Types.INTEGER)
	private Integer licenseId;

	/** 描述 */
	@Column(name = "`desc`", type = Types.VARCHAR, length = 250)
	private String desc;

	/** 商城状态是否禁用，true:1禁用；false：0启用； */
	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false")
	private Boolean disabled;

	/** 商城LOGO浏览路径 */
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

	public Integer getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getRegionId() {
		return regionId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	public String getBizScope() {
		return bizScope;
	}

	public void setBizScope(String bizScope) {
		this.bizScope = bizScope;
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

	public Date getRegTime() {
		return regTime;
	}

	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}

	public Date getOpenTime() {
		return openTime;
	}

	public void setOpenTime(Date openTime) {
		this.openTime = openTime;
	}

	public String getIcpNo() {
		return icpNo;
	}

	public void setIcpNo(String icpNo) {
		this.icpNo = icpNo;
	}

	public String getIcpUuid() {
		return icpUuid;
	}

	public void setIcpUuid(String icpUuid) {
		this.icpUuid = icpUuid;
	}

	public String getIcpUsage() {
		return icpUsage;
	}

	public void setIcpUsage(String icpUsage) {
		this.icpUsage = icpUsage;
	}

	public String getIcpPath() {
		return icpPath;
	}

	public void setIcpPath(String icpPath) {
		this.icpPath = icpPath;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public Integer getLicenseId() {
		return licenseId;
	}

	public void setLicenseId(Integer licenseId) {
		this.licenseId = licenseId;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public String getFileBrowseUrl() {
		return fileBrowseUrl;
	}

	public void setFileBrowseUrl(String fileBrowseUrl) {
		this.fileBrowseUrl = fileBrowseUrl;
	}

	@Override
	public String toString() {
		return "Mall [id=" + id + ", code=" + code + ", operatorId=" + operatorId + ", name=" + name + ", regionId=" + regionId + ", regionName=" + regionName + ", street=" + street + ", address=" + address + ", telNo=" + telNo
				+ ", phoneNo=" + phoneNo + ", linkMan=" + linkMan + ", bizScope=" + bizScope + ", logoUuid=" + logoUuid + ", logoUsage=" + logoUsage + ", logoPath=" + logoPath + ", regTime=" + regTime + ", openTime=" + openTime + ", icpNo="
				+ icpNo + ", icpUuid=" + icpUuid + ", icpUsage=" + icpUsage + ", icpPath=" + icpPath + ", postCode=" + postCode + ", licenseId=" + licenseId + ", desc=" + desc + ", disabled=" + disabled + ", fileBrowseUrl=" + fileBrowseUrl
				+ "]";
	}

}
