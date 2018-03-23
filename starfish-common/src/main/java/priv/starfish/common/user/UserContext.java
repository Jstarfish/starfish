package priv.starfish.common.user;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpSession;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.json.JsonShortDateTimeDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.common.model.ScopeEntity;
import priv.starfish.common.util.StrUtil;

/**
 * 用户的上下文信息
 * 
 * @author koqiui
 * 
 */
public class UserContext implements Serializable {
	private static final long serialVersionUID = 1L;
	// 系统应用&用户
	private Integer appId;
	private Integer userId;
	private String userName;
	private String phoneNo;
	private String gender;
	private List<Integer> roleIds;
	//
	private ScopeEntity scopeEntity;

	@JsonIgnore
	private transient HttpSession httpSession;
	private String clientIp;
	// 第三方应用id&用户id
	private Integer appId3rd;
	private Integer userId3rd;
	//
	private String token;
	// 衍生的token
	private String derivedToken;
	//
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date ts = new Date();

	// 是否系统用户
	public boolean isSysUser() {
		return userId != null;
	}

	// 是否外部用户
	public boolean isOutUser() {
		return userId3rd != null;
	}

	// 是否已认证
	public boolean isAuthenticated() {
		// 已经设置了系统用户id或第三方用户id
		return isSysUser() || isOutUser();
	}

	public Integer getAppId() {
		return appId;
	}

	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = StrUtil.filterEmojiChars(userName, ".");
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public List<Integer> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<Integer> roleIds) {
		this.roleIds = roleIds;
	}

	public ScopeEntity getScopeEntity() {
		return scopeEntity;
	}

	public void setScopeEntity(ScopeEntity scopeEntity) {
		this.scopeEntity = scopeEntity;
	}

	public String getScope() {
		if (scopeEntity == null) {
			return null;
		}
		//
		return scopeEntity.getScope();
	}

	public Integer getScopeEntityId(String scope) {
		//
		if (scopeEntity == null) {
			return null;
		}
		//
		return scope == null || scope.equals(scopeEntity.getScope()) ? scopeEntity.getId() : null;
	}

	public boolean isInScopeEntity() {
		return scopeEntity != null && scopeEntity.getId() != null;
	}

	public HttpSession getHttpSession() {
		return httpSession;
	}

	public void setHttpSession(HttpSession httpSession) {
		this.httpSession = httpSession;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public Integer getAppId3rd() {
		return appId3rd;
	}

	public void setAppId3rd(Integer appId3rd) {
		this.appId3rd = appId3rd;
	}

	public Integer getUserId3rd() {
		return userId3rd;
	}

	public void setUserId3rd(Integer userId3rd) {
		this.userId3rd = userId3rd;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getDerivedToken() {
		return derivedToken;
	}

	public void setDerivedToken(String derivedToken) {
		this.derivedToken = derivedToken;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public void clear() {
		this.appId = null;
		this.userId = null;
		this.userName = null;
		this.phoneNo = null;
		this.roleIds = null;
		this.scopeEntity = null;
		this.gender = null;
		this.httpSession = null;
		this.clientIp = null;
		this.appId3rd = null;
		this.userId3rd = null;
		this.token = null;
		this.derivedToken = null;
		this.ts = new Date();
	}

	public static UserContext newOne() {
		return new UserContext();
	}

}
