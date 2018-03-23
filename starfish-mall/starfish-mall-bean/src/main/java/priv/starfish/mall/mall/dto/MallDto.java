package priv.starfish.mall.mall.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.json.JsonNullSerializer;
import priv.starfish.mall.comn.entity.UserAccount;

import java.util.Date;

public class MallDto {

	/** 主键 */
	private Integer id;

	/** code */
	private String code;

	/** 运营商id */
	private Integer operatorId;

	/** 商城名称 */
	private String name;

	/** 地区编号 */
	private Integer regionId;

	/** 地区全称 */
	private String regionName;

	/** 地区全称：省 */
	private String province;

	/** 地区全称：市 */
	private String city;

	/** 地区全称：县 */
	private String county;

	/** 地区全称：区 */
	private String town;

	/** 街道 */
	private String street;

	/** 固话号码 */
	private String telNo;

	/** 手机号码 */
	private String phoneNo;

	/** 联系人 */
	private String linkMan;

	/** 权限范围 */
	private String bizScope;

	private String logoUuid;

	private String logoUsage;

	/** 头像 */
	private String logoPath;

	/** 注册时间 */
	private Date regTime;

	private Date openTime;

	/** ICP 备案证书号 */
	private String icpNo;

	/** ICP 备案证书路径 */
	private String icpPath;

	/** 邮政编码 */
	private String postCode;

	/** 允许店铺入驻的最大数量 */
	private Integer shopLimit;

	/** 商城状态是否禁用，true:1禁用；false：0启用； */
	private Boolean disabled;

	/** 描述 */
	private String desc;

	/** 商家协议 */
	private String agreement;

	/** 银行名称 */
	private String bankName;

	/** 账户编号 */
	private String acctNo;

	/** 用户昵称 */
	private String nickName;

	/** 用户手机号码 */
	private String userPhoneNo;

	/** 资金账户 */
	private UserAccount userAccount;

	/** 商城LOGO浏览路径 */
	private String fileBrowseUrl;

	/** 密码 */
	@JsonSerialize(using = JsonNullSerializer.class)
	private String password;

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

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getCounty() {
		return county;
	}

	public void setCounty(String county) {
		this.county = county;
	}

	public String getTown() {
		return town;
	}

	public void setTown(String town) {
		this.town = town;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
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

	public Integer getShopLimit() {
		return shopLimit;
	}

	public void setShopLimit(Integer shopLimit) {
		this.shopLimit = shopLimit;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getAgreement() {
		return agreement;
	}

	public void setAgreement(String agreement) {
		this.agreement = agreement;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getUserPhoneNo() {
		return userPhoneNo;
	}

	public void setUserPhoneNo(String userPhoneNo) {
		this.userPhoneNo = userPhoneNo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	public String getFileBrowseUrl() {
		return fileBrowseUrl;
	}

	public void setFileBrowseUrl(String fileBrowseUrl) {
		this.fileBrowseUrl = fileBrowseUrl;
	}

	@Override
	public String toString() {
		return "MallDto [id=" + id + ", code=" + code + ", operatorId=" + operatorId + ", name=" + name + ", regionId=" + regionId + ", regionName=" + regionName + ", province=" + province + ", city=" + city + ", county=" + county
				+ ", town=" + town + ", street=" + street + ", telNo=" + telNo + ", phoneNo=" + phoneNo + ", linkMan=" + linkMan + ", bizScope=" + bizScope + ", logoPath=" + logoPath + ", regTime=" + regTime + ", openTime=" + openTime
				+ ", icpNo=" + icpNo + ", icpPath=" + icpPath + ", postCode=" + postCode + ", shopLimit=" + shopLimit + ", disabled=" + disabled + ", desc=" + desc + ", agreement=" + agreement + ", bankName=" + bankName + ", acctNo="
				+ acctNo + ", nickName=" + nickName + ", userPhoneNo=" + userPhoneNo + ", userAccount=" + userAccount + ", password=" + password + "]";
	}

}
