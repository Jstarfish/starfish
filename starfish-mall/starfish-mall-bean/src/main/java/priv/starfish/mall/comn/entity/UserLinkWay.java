package priv.starfish.mall.comn.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonShortDateTimeSerializer;

/**
 * 用户联系方式
 * 
 * @author koqiui
 * @date 2015年11月4日 上午9:28:38
 *
 */
@Table(name = "user_link_way", uniqueConstraints = { @UniqueConstraint(fieldNames = { "userId", "alias" }) })
public class UserLinkWay implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;

	/** 会员Id */
	@Column(nullable = false, type = Types.INTEGER)
	@ForeignKey(refEntityClass = User.class, refFieldName = "id")
	private Integer userId;

	/** 别名 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String alias;

	/** 手机号 */
	@Column(nullable = false, type = Types.VARCHAR, length = 15)
	private String phoneNo;

	/** 联系人 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String linkMan;

	/** 电话 */
	@Column(type = Types.VARCHAR, length = 15)
	private String telNo;

	/** 地区Id */
	@Column(type = Types.INTEGER)
	private Integer regionId;

	/** 地区名称 */
	@Column(type = Types.VARCHAR, length = 60)
	private String regionName;
	
	/** 省级地区Id */
	@Column(type = Types.INTEGER)
	private Integer provinceId;

	/** 市级地区Id */
	@Column(type = Types.INTEGER)
	private Integer cityId;

	/** 县级地区Id */
	@Column(type = Types.INTEGER)
	private Integer countyId;

	/** 乡级地区Id */
	@Column(type = Types.INTEGER)
	private Integer townId;

	/** 街道名称 */
	@Column(type = Types.VARCHAR, length = 30)
	private String street;

	@Column(type = Types.VARCHAR, length = 90)
	private String address;

	@Column(type = Types.VARCHAR, length = 8)
	private String postCode;

	/** 邮箱 */
	@Column(type = Types.VARCHAR, length = 60)
	private String email;

	/** 是否是默认地址 */
	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false")
	private Boolean defaulted;

	/** 时间戳 */
	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date ts;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
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

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
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

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getDefaulted() {
		return defaulted;
	}

	public void setDefaulted(Boolean defaulted) {
		this.defaulted = defaulted;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
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

	@Override
	public String toString() {
		return "UserLinkWay [id=" + id + ", userId=" + userId + ", alias=" + alias + ", phoneNo=" + phoneNo + ", linkMan=" + linkMan + ", telNo=" + telNo + ", regionId=" + regionId + ", regionName=" + regionName + ", provinceId="
				+ provinceId + ", cityId=" + cityId + ", countyId=" + countyId + ", townId=" + townId + ", street=" + street + ", address=" + address + ", postCode=" + postCode + ", email=" + email + ", defaulted=" + defaulted + ", ts="
				+ ts + "]";
	}
}
