package priv.starfish.mall.demo.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonDateTimeDeserializer;
import priv.starfish.common.json.JsonDateTimeSerializer;
import priv.starfish.common.json.JsonNullDeserializer;
import priv.starfish.common.util.DateUtil;

@Table(name = "shopx", uniqueConstraints = { @UniqueConstraint(fieldNames = { "name" }) })
public class Shopx implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;

	/** 店铺名称 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String name;

	/** 店铺名称简拼 */
	@Column(type = Types.VARCHAR, length = 30)
	private String py;

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

	/** 手机 */
	@Column(type = Types.VARCHAR, length = 30)
	private String phoneNo;

	/** 联系人 */
	@Column(type = Types.VARCHAR, length = 30)
	private String linkMan;

	/** 营业范围 */
	@Column(nullable = false, type = Types.VARCHAR, length = 1000)
	private String bizScope;

	/** 申请留言 */
	@Column(type = Types.VARCHAR, length = 60)
	private String applyMsg;

	/** 注册时间 */
	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date regTime;

	/** 审核状态： 0 未审核, 1 初审通过，-1 审核未通过 */
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

	/** 创建时间 */
	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date createTime;

	@Column(type = Types.TIMESTAMP, desc = "变更时间")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	@JsonDeserialize(using = JsonDateTimeDeserializer.class)
	private Date changeTime;

	@Column(type = Types.TIMESTAMP, desc = "索引时间")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	@JsonDeserialize(using = JsonDateTimeDeserializer.class)
	private Date indexTime;

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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	@Override
	public String toString() {
		return "Shopx [id=" + id + ", name=" + name + ", py=" + py + ", regionId=" + regionId + ", regionName=" + regionName + ", street=" + street + ", address=" + address + ", provinceId=" + provinceId + ", cityId=" + cityId
				+ ", countyId=" + countyId + ", townId=" + townId + ", lng=" + lng + ", lat=" + lat + ", phoneNo=" + phoneNo + ", linkMan=" + linkMan + ", bizScope=" + bizScope + ", applyMsg=" + applyMsg + ", regTime="
				+ DateUtil.toStdDateTimeStr(regTime) + ", auditStatus=" + auditStatus + ", closed=" + closed + ", disabled=" + disabled + ", memo=" + memo + ", createTime=" + DateUtil.toStdTimestampStr(createTime) + ", changeTime="
				+ DateUtil.toStdTimestampStr(changeTime) + ", indexTime=" + DateUtil.toStdTimestampStr(indexTime) + "]";
	}

}
