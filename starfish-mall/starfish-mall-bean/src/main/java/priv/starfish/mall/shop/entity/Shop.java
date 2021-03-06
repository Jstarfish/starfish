package priv.starfish.mall.shop.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonShortDateTimeDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.mall.comn.entity.User;
import priv.starfish.mall.comn.misc.HasLogo;
import priv.starfish.mall.merchant.entity.Merchant;

@Table(name = "shop", uniqueConstraints = { @UniqueConstraint(fieldNames = { "code" }), @UniqueConstraint(fieldNames = { "merchantId", "name" }) })
public class Shop implements Serializable, HasLogo {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;

	/** 唯一标识 code = mall.code +"-" + TimeStamp的long值 */
	@Column(nullable = false, type = Types.VARCHAR, length = 90)
	private String code;

	@Column(type = Types.INTEGER)
	@ForeignKey(refEntityClass = Merchant.class, refFieldName = "id")
	private Integer merchantId;

	/** 商户名 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "商户名")
	private String merchantName;

	/** 自营标志（是否自营） */
	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false")
	private Boolean selfFlag;

	/** 店铺名称 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String name;

	/** 店铺名称简拼 */
	@Column(type = Types.VARCHAR, length = 30)
	private String py;

	/** 是否企业店铺（而不是默认的个人店铺） */
	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false")
	private Boolean entpFlag;

	/** 地区Id */
	@Column(type = Types.INTEGER)
	private Integer regionId;

	/** 地区名称 */
	@Column(type = Types.VARCHAR, length = 60)
	private String regionName;

	/** 街道 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String street;

	@Column(type = Types.VARCHAR, length = 90)
	private String address;

	@Column(type = Types.INTEGER, desc = "配送中心Id")
	private Integer distCenterId;

	/** 省级地区Id */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer provinceId;

	/** 市级地区Id */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer cityId;

	/** 县级地区Id */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer countyId;

	/** 乡级地区Id */
	@Column(type = Types.INTEGER)
	private Integer townId;
	/** 经度 */
	@Column(type = Types.NUMERIC, precision = 20, scale = 16)
	private Double lng;
	/** 纬度 */
	@Column(type = Types.NUMERIC, precision = 20, scale = 16)
	private Double lat;

	/** 电话 */
	@Column(type = Types.VARCHAR, length = 30)
	private String telNo;

	/** 手机 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String phoneNo;

	/** 联系人 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String linkMan;

	/** 营业范围 */
	@Column(nullable = false, type = Types.VARCHAR, length = 1000)
	private String bizScope;

	/** logo UUID */
	@Column(type = Types.VARCHAR, length = 60)
	private String logoUuid;

	/** logo Usage */
	@Column(type = Types.VARCHAR, length = 30)
	private String logoUsage;

	/** 店铺LOGO图片地址 */
	@Column(type = Types.VARCHAR, length = 250)
	private String logoPath;

	/** 申请时间 */
	@Column(type = Types.TIMESTAMP)
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date applyTime;

	/** 申请留言 */
	@Column(type = Types.VARCHAR, length = 60)
	private String applyMsg;

	/** 注册时间 */
	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date regTime;

	/** （企业）营业执照id（企业店铺必须，个人选填） */
	@Column(type = Types.INTEGER)
	private Integer licenseId;

	/** 审核状态： 0 未审核, 1 初审通过， 2 终审通过 ，-1 审核未通过 */
	@Column(nullable = false, type = Types.INTEGER, defaultValue = "0")
	private Integer auditStatus;

	/** 是否关闭 */
	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false")
	private Boolean closed;

	/** 是否不可用 */
	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false")
	private Boolean disabled;

	/** 备注 */
	@Column(type = Types.VARCHAR, length = 250)
	private String memo;

	/** 分值快照 */
	@Column(type = Types.INTEGER)
	private Integer point;

	/** 商户 */
	private Merchant merchant;

	/** 店铺人数 */
	@Column(type = Types.INTEGER)
	private Integer staffCount;

	/** 推荐人 */
	@Column(type = Types.VARCHAR, length = 30)
	private String referrerName;

	/** 推荐人手机 */
	@Column(type = Types.VARCHAR, length = 15)
	private String referrerPhoneNo;

	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP", desc = "更新时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date changeTime;

	@Column(type = Types.TIMESTAMP, desc = "时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date indexTime;

	/** 系统用户 */
	private User user;

	/** 审核记录 */
	private ShopAuditRec shopAuditRec;

	/** 图片浏览路径 */
	private String fileBrowseUrl;

	/** 店铺等级 */
	private ShopGrade shopGrade;

	/** 店铺相册图片List */
	private List<ShopAlbumImg> shopAlbumImgs;

	/** 店铺营业状态 */
	private ShopBizStatus shopBizStatus;

	/** 用户到店铺的距离 */
	private Double distance;

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

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public Boolean getSelfFlag() {
		return selfFlag;
	}

	public void setSelfFlag(Boolean selfFlag) {
		this.selfFlag = selfFlag;
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

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public String getApplyMsg() {
		return applyMsg;
	}

	public void setApplyMsg(String applyMsg) {
		this.applyMsg = applyMsg;
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

	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	public Boolean getClosed() {
		return closed;
	}

	public void setClosed(Boolean closed) {
		this.closed = closed;
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

	public Integer getPoint() {
		return point;
	}

	public void setPoint(Integer point) {
		this.point = point;
	}

	public Merchant getMerchant() {
		return merchant;
	}

	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getFileBrowseUrl() {
		return fileBrowseUrl;
	}

	public void setFileBrowseUrl(String fileBrowseUrl) {
		this.fileBrowseUrl = fileBrowseUrl;
	}

	public ShopGrade getShopGrade() {
		return shopGrade;
	}

	public void setShopGrade(ShopGrade shopGrade) {
		this.shopGrade = shopGrade;
	}

	public Integer getStaffCount() {
		return staffCount;
	}

	public void setStaffCount(Integer staffCount) {
		this.staffCount = staffCount;
	}

	public String getReferrerName() {
		return referrerName;
	}

	public void setReferrerName(String referrerName) {
		this.referrerName = referrerName;
	}

	public String getReferrerPhoneNo() {
		return referrerPhoneNo;
	}

	public void setReferrerPhoneNo(String referrerPhoneNo) {
		this.referrerPhoneNo = referrerPhoneNo;
	}

	public Integer getProvinceId() {
		return provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getCountyId() {
		return countyId;
	}

	public void setCountyId(Integer countyId) {
		this.countyId = countyId;
	}

	public Integer getTownId() {
		return townId;
	}

	public void setTownId(Integer townId) {
		this.townId = townId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public Integer getDistCenterId() {
		return distCenterId;
	}

	public void setDistCenterId(Integer distCenterId) {
		this.distCenterId = distCenterId;
	}

	public Date getChangeTime() {
		return changeTime;
	}

	public void setChangeTime(Date changeTime) {
		this.changeTime = changeTime;
	}

	public Date getIndexTime() {
		return indexTime;
	}

	public void setIndexTime(Date indexTime) {
		this.indexTime = indexTime;
	}

	public ShopAuditRec getShopAuditRec() {
		return shopAuditRec;
	}

	public void setShopAuditRec(ShopAuditRec shopAuditRec) {
		this.shopAuditRec = shopAuditRec;
	}

	public List<ShopAlbumImg> getShopAlbumImgs() {
		return shopAlbumImgs;
	}

	public void setShopAlbumImgs(List<ShopAlbumImg> shopAlbumImgs) {
		this.shopAlbumImgs = shopAlbumImgs;
	}

	public ShopBizStatus getShopBizStatus() {
		return shopBizStatus;
	}

	public void setShopBizStatus(ShopBizStatus shopBizStatus) {
		this.shopBizStatus = shopBizStatus;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	@Override
	public String toString() {
		return "Shop [id=" + id + ", code=" + code + ", merchantId=" + merchantId + ", merchantName=" + merchantName + ", selfFlag=" + selfFlag + ", name=" + name + ", py=" + py + ", entpFlag=" + entpFlag + ", regionId=" + regionId
				+ ", regionName=" + regionName + ", street=" + street + ", address=" + address + ", distCenterId=" + distCenterId + ", provinceId=" + provinceId + ", cityId=" + cityId + ", countyId=" + countyId + ", townId=" + townId
				+ ", lng=" + lng + ", lat=" + lat + ", telNo=" + telNo + ", phoneNo=" + phoneNo + ", linkMan=" + linkMan + ", bizScope=" + bizScope + ", logoUuid=" + logoUuid + ", logoUsage=" + logoUsage + ", logoPath=" + logoPath
				+ ", applyTime=" + applyTime + ", applyMsg=" + applyMsg + ", regTime=" + regTime + ", licenseId=" + licenseId + ", auditStatus=" + auditStatus + ", closed=" + closed + ", disabled=" + disabled + ", memo=" + memo + ", point="
				+ point + ", merchant=" + merchant + ", staffCount=" + staffCount + ", referrerName=" + referrerName + ", referrerPhoneNo=" + referrerPhoneNo + ", changeTime=" + changeTime + ", indexTime=" + indexTime + ", user=" + user
				+ ", shopAuditRec=" + shopAuditRec + ", fileBrowseUrl=" + fileBrowseUrl + ", shopGrade=" + shopGrade + ", shopAlbumImgs=" + shopAlbumImgs + ", shopBizStatus=" + shopBizStatus + ", distance=" + distance + "]";
	}

}
