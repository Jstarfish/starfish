package priv.starfish.mall.comn.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;

@Table(name = "user_3rd", uniqueConstraints = { @UniqueConstraint(fieldNames = { "appId", "openId" }) })
public class User3rd implements Serializable {
	private static final long serialVersionUID = 1L;
	//
	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(nullable = false, type = Types.INTEGER)
	private Integer appId;

	@Column(nullable = false, type = Types.VARCHAR, length = 60)
	private String openId;

	/** 昵称 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String nickName;

	/** 头像 */
	@Column(type = Types.VARCHAR, length = 250)
	private String headImageUrl;

	@Column(type = Types.CHAR, length = 1)
	private Character sex;

	@Column(type = Types.INTEGER)
	private Integer sysUserId;

	@Column(type = Types.VARCHAR, length = 30)
	private String country;

	@Column(type = Types.VARCHAR, length = 30)
	private String province;

	@Column(type = Types.VARCHAR, length = 30)
	private String city;

	@Column(type = Types.VARCHAR, length = 600)
	private String accessToken;

	@Column(type = Types.TIMESTAMP)
	private Date expireTime;

	@Column(type = Types.BOOLEAN, defaultValue = "false")
	private Boolean focused;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAppId() {
		return appId;
	}

	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public String getHeadImageUrl() {
		return headImageUrl;
	}

	public void setHeadImageUrl(String headImageUrl) {
		this.headImageUrl = headImageUrl;
	}

	public Character getSex() {
		return sex;
	}

	public void setSex(Character sex) {
		this.sex = sex;
	}

	public Integer getSysUserId() {
		return sysUserId;
	}

	public void setSysUserId(Integer sysUserId) {
		this.sysUserId = sysUserId;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
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

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public Boolean getFocused() {
		return focused;
	}

	public void setFocused(Boolean focused) {
		this.focused = focused;
	}

	@Override
	public String toString() {
		return "User3rd [id=" + id + ", appId=" + appId + ", openId=" + openId + ", nickName=" + nickName + ", headImageUrl=" + headImageUrl + ", sex=" + sex + ", sysUserId=" + sysUserId + ", country=" + country + ", province=" + province
				+ ", city=" + city + ", accessToken=" + accessToken + ", expireTime=" + expireTime + ", focused=" + focused + "]";
	}

}
