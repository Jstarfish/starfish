package priv.starfish.mall.comn.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.*;
import priv.starfish.mall.comn.dict.Gender;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;
import java.util.List;

@Table(name = "user", uniqueConstraints = { @UniqueConstraint(fieldNames = { "phoneNo" }) })
public class User implements Serializable {
	private static final long serialVersionUID = 1L;
	//
	public static final String SYS_ADMIN_NAME = "sysadmin";
	//public static final String SYS_ADMIN_NAME = "sysadmin";
	//
	public static final int UNKNOWN_USER_ID = -1;

	public boolean isSysAdmin() {
		return this.phoneNo != null && this.phoneNo.equals(SYS_ADMIN_NAME);
	}

	public boolean isSysAdmin(String phoneNo) {
		return phoneNo != null && phoneNo.equals(SYS_ADMIN_NAME);
	}

	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "手机号码")
	private String phoneNo;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "昵称")
	private String nickName;

	@Column(type = Types.VARCHAR, length = 60, desc = "邮件")
	private String email;

	@Column(type = Types.VARCHAR, length = 30, desc = "身份证件")
	private String idCertNo;

	@Column(type = Types.VARCHAR, length = 60, desc = "密码 ")
	@JsonSerialize(using = JsonNullSerializer.class)
	private String password;

	@Column(type = Types.VARCHAR, length = 30, desc = "密码盐")
	@JsonIgnore
	private String salt;

	@Column(type = Types.VARCHAR, length = 60, desc = "支付密码")
	@JsonSerialize(using = JsonNullSerializer.class)
	private String payPassword;

	@Column(type = Types.VARCHAR, length = 30, desc = "真实姓名")
	private String realName;

	@Column(type = Types.VARCHAR, length = 1, defaultValue = "'X'", desc = "性别 ")
	private Gender gender;

	@Column(type = Types.DATE, desc = "生日")
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date birthDate;

	@Column(type = Types.VARCHAR, length = 15, desc = "qq")
	private String qq;

	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP", desc = "注册日期 ")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date regTime;

	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false", desc = "认证")
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Boolean verified;

	@Column(type = Types.INTEGER, desc = "失败次数")
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Integer failCount;

	@Column(type = Types.TIMESTAMP, desc = "失败日期")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date failTime;

	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false", desc = "锁定")
	private Boolean locked;

	@Column(type = Types.TIMESTAMP, desc = "锁定日期")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date lockTime;

	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP", desc = "时间戳")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date ts;

	// 冗余的安全等级
	private Integer secureLevel;

	/** 用户角色 */
	private List<Role> roles;

	private List<UserAccount> userAccts;

	private List<BizLicense> bizLicenses;

	private List<UserVerfStatus> userVerfStatus;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getIdCertNo() {
		return idCertNo;
	}

	public void setIdCertNo(String idCertNo) {
		this.idCertNo = idCertNo;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getQq() {
		return qq;
	}

	public void setQq(String qq) {
		this.qq = qq;
	}

	public Date getRegTime() {
		return regTime;
	}

	public void setRegTime(Date regTime) {
		this.regTime = regTime;
	}

	public Boolean getVerified() {
		return verified;
	}

	public void setVerified(Boolean verified) {
		this.verified = verified;
	}

	public Integer getFailCount() {
		return failCount;
	}

	public void setFailCount(Integer failCount) {
		this.failCount = failCount;
	}

	public Date getFailTime() {
		return failTime;
	}

	public void setFailTime(Date failTime) {
		this.failTime = failTime;
	}

	public Boolean getLocked() {
		return locked;
	}

	public void setLocked(Boolean locked) {
		this.locked = locked;
	}

	public Date getLockTime() {
		return lockTime;
	}

	public void setLockTime(Date lockTime) {
		this.lockTime = lockTime;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public String getPayPassword() {
		return payPassword;
	}

	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public List<UserAccount> getUserAccts() {
		return userAccts;
	}

	public void setUserAccts(List<UserAccount> userAccts) {
		this.userAccts = userAccts;
	}

	public List<BizLicense> getBizLicenses() {
		return bizLicenses;
	}

	public void setBizLicenses(List<BizLicense> bizLicenses) {
		this.bizLicenses = bizLicenses;
	}

	public List<UserVerfStatus> getUserVerfStatus() {
		return userVerfStatus;
	}

	public void setUserVerfStatus(List<UserVerfStatus> userVerfStatus) {
		this.userVerfStatus = userVerfStatus;
	}

	public Integer getSecureLevel() {
		return secureLevel;
	}

	public void setSecureLevel(Integer secureLevel) {
		this.secureLevel = secureLevel;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", phoneNo=" + phoneNo + ", nickName=" + nickName + ", email=" + email + ", idCertNo=" + idCertNo + ", password=" + password + ", salt=" + salt + ", payPassword=" + payPassword + ", realName=" + realName
				+ ", gender=" + gender + ", birthDate=" + birthDate + ", qq=" + qq + ", regTime=" + regTime + ", verified=" + verified + ", failCount=" + failCount + ", failTime=" + failTime + ", locked=" + locked + ", lockTime=" + lockTime
				+ ", ts=" + ts + ", secureLevel=" + secureLevel + ", roles=" + roles + ", userAccts=" + userAccts + ", bizLicenses=" + bizLicenses + ", userVerfStatus=" + userVerfStatus + "]";
	}

}
