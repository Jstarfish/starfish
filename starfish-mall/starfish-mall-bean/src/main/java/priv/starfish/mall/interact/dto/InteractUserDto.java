package priv.starfish.mall.interact.dto;

import java.util.List;

import priv.starfish.mall.comn.dict.Gender;
import priv.starfish.mall.comn.entity.Role;

public class InteractUserDto {

	// --------------------------------在线客服------------------------------------
	/** 客服编号，自动生成 */
	private Integer id;
	/** 对应 user.id */
	private Integer servantId;
	/** 客服编号，手动设置 */
	private String servantNo;
	/** 客服名称 */
	private String servantName;
	// --------------------------------人员------------------------------------
	/** 昵称 */
	private String nickName;
	/** 性别 */
	private Gender gender;
	/** 邮箱 */
	private String email;
	/** 真实名称 */
	private String realName;
	/** 手机号码 */
	private String phoneNo;
	/** 用户角色 */
	private List<Role> roles;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public List<Role> getRoles() {
		return roles;
	}

	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	public Integer getServantId() {
		return servantId;
	}

	public void setServantId(Integer servantId) {
		this.servantId = servantId;
	}

	public String getServantNo() {
		return servantNo;
	}

	public void setServantNo(String servantNo) {
		this.servantNo = servantNo;
	}

	public String getServantName() {
		return servantName;
	}

	public void setServantName(String servantName) {
		this.servantName = servantName;
	}

	@Override
	public String toString() {
		return "InteractUserDto [id=" + id + ", servantId=" + servantId
				+ ", servantNo=" + servantNo + ", servantName=" + servantName
				+ ", nickName=" + nickName + ", gender=" + gender + ", email="
				+ email + ", realName=" + realName + ", phoneNo=" + phoneNo
				+ ", roles=" + roles + "]";
	}

}
