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
import priv.starfish.common.json.JsonNullDeserializer;
import priv.starfish.common.json.JsonShortDateTimeDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.mall.comn.misc.HasLogo;

@Table(name = "dist_shop")
public class DistShop implements Serializable, HasLogo {
	private static final long serialVersionUID = 1L;

	@Id(auto = false, type = Types.INTEGER, desc = "主键Id")
	private Integer id;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "卫星店名称")
	private String name;

	@Column(nullable = false, type = Types.INTEGER, desc = "地区Id")
	private Integer regionId;

	@Column(nullable = false, type = Types.VARCHAR, length = 60, desc = "地区名称")
	private String regionName;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "街道")
	private String street;

	@Column(type = Types.INTEGER, desc = "省级地区Id")
	private Integer provinceId;

	@Column(type = Types.INTEGER, desc = "市级地区Id")
	private Integer cityId;

	@Column(type = Types.INTEGER, desc = "县级地区Id")
	private Integer countyId;

	@Column(type = Types.INTEGER, desc = "乡级地区Id")
	private Integer townId;

	@Column(nullable = false, type = Types.VARCHAR, length = 90, desc = "地址")
	private String address;

	@Column(type = Types.NUMERIC, precision = 20, scale = 16, desc = "经度")
	private Double lng;

	@Column(type = Types.NUMERIC, precision = 20, scale = 16, desc = "纬度")
	private Double lat;

	@Column(type = Types.VARCHAR, length = 30, desc = "真实名称")
	private String realName;

	@Column(type = Types.VARCHAR, length = 30, desc = "身份证")
	private String idCertNo;

	@Column(nullable = false, type = Types.INTEGER, desc = "隶属加盟店id")
	@ForeignKey(refEntityClass = Shop.class, refFieldName = "id")
	private Integer ownerShopId;

	@Column(type = Types.VARCHAR, length = 30, desc = "隶属加盟店名称")
	private String ownerShopName;

	@Column(type = Types.VARCHAR, length = 15, desc = "电话")
	private String telNo;

	@Column(type = Types.VARCHAR, length = 15, desc = "手机")
	private String phoneNo;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "联系人")
	private String linkMan;

	@Column(type = Types.VARCHAR, length = 60, desc = "logo UUID")
	private String logoUuid;

	@Column(type = Types.VARCHAR, length = 30, desc = "logo Usage")
	private String logoUsage;

	@Column(type = Types.VARCHAR, length = 250, desc = "店铺LOGO图片地址")
	private String logoPath;

	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP", desc = "注册时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date regTime;

	/** 审核状态： 0:未审核，-1：审核不通过，1：审核通过 */
	@Column(nullable = false, type = Types.INTEGER, defaultValue = "0")
	private Integer auditStatus;

	@Column(type = Types.TIMESTAMP, desc = "审核时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date auditTime;

	@Column(type = Types.INTEGER, desc = "审核人Id")
	private Integer auditorId;

	@Column(type = Types.VARCHAR, length = 30, desc = "审核人")
	private String auditorName;

	@Column(name = "`auditorDesc`", type = Types.VARCHAR, length = 250, desc = "描述")
	private String auditorDesc;

	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false", desc = "是否不可用")
	private Boolean disabled;

	@Column(nullable = false, type = Types.TIMESTAMP, desc = "时间戳")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date ts;

	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP", desc = "更新时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date changeTime;

	@Column(type = Types.TIMESTAMP, desc = "索引时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date indexTime;

	/** 图片浏览路径 */
	private String fileBrowseUrl;

	// 是否需要审核
	private Boolean needAuditor;

	private List<DistShopSvc> distShopSvcList;

	// 身份证信息
	List<DistShopPic> distShopPic;

	// 隶属加盟店
	private Shop shop;

	/** 用户到店铺的距离 */
	private Double distance;

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

	public Integer getOwnerShopId() {
		return ownerShopId;
	}

	public void setOwnerShopId(Integer ownerShopId) {
		this.ownerShopId = ownerShopId;
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

	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public Integer getAuditorId() {
		return auditorId;
	}

	public void setAuditorId(Integer auditorId) {
		this.auditorId = auditorId;
	}

	public String getAuditorName() {
		return auditorName;
	}

	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}

	public String getAuditorDesc() {
		return auditorDesc;
	}

	public void setAuditorDesc(String auditorDesc) {
		this.auditorDesc = auditorDesc;
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

	public String getFileBrowseUrl() {
		return fileBrowseUrl;
	}

	public void setFileBrowseUrl(String fileBrowseUrl) {
		this.fileBrowseUrl = fileBrowseUrl;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIdCertNo() {
		return idCertNo;
	}

	public void setIdCertNo(String idCertNo) {
		this.idCertNo = idCertNo;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public Boolean getNeedAuditor() {
		return needAuditor;
	}

	public void setNeedAuditor(Boolean needAuditor) {
		this.needAuditor = needAuditor;
	}

	public List<DistShopSvc> getDistShopSvcList() {
		return distShopSvcList;
	}

	public void setDistShopSvcList(List<DistShopSvc> distShopSvcList) {
		this.distShopSvcList = distShopSvcList;
	}

	public String getOwnerShopName() {
		return ownerShopName;
	}

	public void setOwnerShopName(String ownerShopName) {
		this.ownerShopName = ownerShopName;
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

	public List<DistShopPic> getDistShopPic() {
		return distShopPic;
	}

	public void setDistShopPic(List<DistShopPic> distShopPic) {
		this.distShopPic = distShopPic;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	@Override
	public String toString() {
		return "DistShop [id=" + id + ", name=" + name + ", regionId=" + regionId + ", regionName=" + regionName + ", street=" + street + ", provinceId=" + provinceId + ", cityId=" + cityId + ", countyId=" + countyId + ", townId=" + townId
				+ ", address=" + address + ", lng=" + lng + ", lat=" + lat + ", realName=" + realName + ", idCertNo=" + idCertNo + ", ownerShopId=" + ownerShopId + ", ownerShopName=" + ownerShopName + ", telNo=" + telNo + ", phoneNo="
				+ phoneNo + ", linkMan=" + linkMan + ", logoUuid=" + logoUuid + ", logoUsage=" + logoUsage + ", logoPath=" + logoPath + ", regTime=" + regTime + ", auditStatus=" + auditStatus + ", auditTime=" + auditTime + ", auditorId="
				+ auditorId + ", auditorName=" + auditorName + ", auditorDesc=" + auditorDesc + ", disabled=" + disabled + ", ts=" + ts + ", changeTime=" + changeTime + ", indexTime=" + indexTime + ", fileBrowseUrl=" + fileBrowseUrl
				+ ", needAuditor=" + needAuditor + ", distShopSvcList=" + distShopSvcList + ", distShopPic=" + distShopPic + ", shop=" + shop + ", distance=" + distance + "]";
	}

}
