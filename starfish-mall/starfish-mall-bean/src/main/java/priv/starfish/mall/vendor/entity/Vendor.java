package priv.starfish.mall.vendor.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.mall.comn.entity.Region;
import priv.starfish.mall.comn.entity.User;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonNullDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;

/**
 * 供应商实体
 * 
 * @author "WJJ"
 * @date 2015年10月13日 下午3:50:18
 *
 */
@Table(name = "vendor", uniqueConstraints = { @UniqueConstraint(fieldNames = { "code" }) })
public class Vendor implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;

	/** 编号 唯一标识 CodeUtil.newVendorCode() */
	@Column(nullable = false, type = Types.VARCHAR, length = 90)
	private String code;

	/** 供应商名称 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String name;

	/** 供应商名称简拼，便于拼音搜索 StrUtil.chsToPy(name) */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String py;

	/** 是否企业供应商（而不是默认的个人供应商） */
	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "true")
	private Boolean entpFlag;

	/** 地区Id */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer regionId;

	/** 地区名称 */
	@Column(nullable = false, type = Types.VARCHAR, length = 60)
	private String regionName;

	/** 街道 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String street;

	@Column(type = Types.VARCHAR, length = 90)
	private String address;

	/** 经度 */
	@Column(type = Types.NUMERIC, precision = 20, scale = 16)
	private Double lng;

	/** 纬度 */
	@Column(type = Types.NUMERIC, precision = 20, scale = 16)
	private Double lat;

	/** 固定电话 */
	@Column(type = Types.VARCHAR, length = 15)
	private String telNo;

	/** 手机 */
	@Column(nullable = false, type = Types.VARCHAR, length = 15)
	private String phoneNo;

	/** 联系人 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String linkMan;

	/** 业务范围 */
	@Column(nullable = false, type = Types.VARCHAR, length = 1000)
	private String bizScope;

	/** logo UUID */
	@Column(type = Types.VARCHAR, length = 60)
	private String logoUuid;

	/** 图片用途 image.logo */
	@Column(type = Types.VARCHAR, length = 30)
	private String logoUsage;

	/** 店铺LOGO图片地址 */
	@Column(type = Types.VARCHAR, length = 250)
	private String logoPath;

	/** 注册时间 */
	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date regTime;

	/** 资金账户 */
	@Column(type = Types.INTEGER)
	private Integer accountId;

	/** （企业）营业执照id（企业代理必须，个人选填） */
	@Column(type = Types.INTEGER)
	private Integer licenseId;

	/** 是否不可用 */
	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false")
	private Boolean disabled;

	/** 备注 */
	@Column(type = Types.VARCHAR, length = 250)
	private String memo;

	/** 时间戳 */
	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date ts;

	/** 系统用户 */
	private User user;

	/** 供应商照片 */
	private List<VendorPic> vendorPictures;

	/** 地区 */
	private Region region;

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

	public Boolean getEntpFlag() {
		return entpFlag;
	}

	public void setEntpFlag(Boolean entpFlag) {
		this.entpFlag = entpFlag;
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

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
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

	public Integer getLicenseId() {
		return licenseId;
	}

	public void setLicenseId(Integer licenseId) {
		this.licenseId = licenseId;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public String getFileBrowseUrl() {
		return fileBrowseUrl;
	}

	public void setFileBrowseUrl(String fileBrowseUrl) {
		this.fileBrowseUrl = fileBrowseUrl;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public List<VendorPic> getVendorPictures() {
		return vendorPictures;
	}

	public void setVendorPictures(List<VendorPic> vendorPictures) {
		this.vendorPictures = vendorPictures;
	}

	@Override
	public String toString() {
		return "Vendor [id=" + id + ", code=" + code + ", name=" + name + ", py=" + py + ", entpFlag=" + entpFlag + ", regionId=" + regionId + ", regionName=" + regionName + ", street=" + street + ", address=" + address + ", lng=" + lng
				+ ", lat=" + lat + ", telNo=" + telNo + ", phoneNo=" + phoneNo + ", linkMan=" + linkMan + ", bizScope=" + bizScope + ", logoUuid=" + logoUuid + ", logoUsage=" + logoUsage + ", logoPath=" + logoPath + ", regTime=" + regTime
				+ ", accountId=" + accountId + ", licenseId=" + licenseId + ", disabled=" + disabled + ", memo=" + memo + ", ts=" + ts + ", user=" + user + ", vendorPictures=" + vendorPictures + ", region=" + region + ", fileBrowseUrl="
				+ fileBrowseUrl + "]";
	}

}
