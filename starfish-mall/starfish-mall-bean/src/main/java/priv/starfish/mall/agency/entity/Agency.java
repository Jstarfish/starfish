package priv.starfish.mall.agency.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.*;
import priv.starfish.common.json.JsonNullDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.mall.agent.entity.Agent;
import priv.starfish.mall.comn.entity.Region;
import priv.starfish.mall.comn.misc.HasLogo;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

/**
 * 代理处实体
 * 
 * @author WJJ
 * @date 2015年9月10日 下午4:07:55
 *
 */
@Table(name = "agency", uniqueConstraints = { @UniqueConstraint(fieldNames = { "code" }) })
public class Agency implements Serializable, HasLogo {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;

	/** 编号 唯一标识 code = mall.code +"-dl-" + 8位随机字符代码 */
	@Column(nullable = false, type = Types.VARCHAR, length = 90)
	private String code;

	/** 代理处属主（结算对象） */
	@Column(type = Types.INTEGER)
	@ForeignKey(refEntityClass = Agent.class, refFieldName = "id")
	private Integer agentId;

	/** 代理商 */
	private Agent agent;

	/** 代理处名称 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String name;

	/** 代理处名称简拼，便于拼音搜索 StrUtil.chsToPy(name) */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String py;

	/** 是否企业代理处（而不是默认的个人代理） */
	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false")
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

	/** 地区Id */
	@Column(type = Types.INTEGER)
	private Integer cityId;

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

	/** 申请时间 */
	@Column(type = Types.TIMESTAMP)
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date applyTime;

	/** 申请留言 */
	@Column(type = Types.VARCHAR, length = 30)
	private String applyMsg;

	/** （企业）营业执照id（企业代理必须，个人选填） */
	@Column(type = Types.INTEGER)
	private Integer licenseId;

	/** 审核状态： 0 未审核, 1 审核通过， 2 审核未通过 */
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

	public Integer getAgentId() {
		return agentId;
	}

	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
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

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
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

	public Agent getAgent() {
		return agent;
	}

	public void setAgent(Agent agent) {
		this.agent = agent;
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

	@Override
	public String toString() {
		return "Agency [id=" + id + ", code=" + code + ", agentId=" + agentId + ", agent=" + agent + ", name=" + name + ", py=" + py + ", entpFlag=" + entpFlag + ", regionId=" + regionId + ", regionName=" + regionName + ", street=" + street
				+ ", address=" + address + ", cityId=" + cityId + ", lng=" + lng + ", lat=" + lat + ", telNo=" + telNo + ", phoneNo=" + phoneNo + ", linkMan=" + linkMan + ", bizScope=" + bizScope + ", logoUuid=" + logoUuid + ", logoUsage="
				+ logoUsage + ", logoPath=" + logoPath + ", regTime=" + regTime + ", applyTime=" + applyTime + ", applyMsg=" + applyMsg + ", licenseId=" + licenseId + ", auditStatus=" + auditStatus + ", closed=" + closed + ", disabled="
				+ disabled + ", memo=" + memo + ", region=" + region + ", fileBrowseUrl=" + fileBrowseUrl + "]";
	}

}
