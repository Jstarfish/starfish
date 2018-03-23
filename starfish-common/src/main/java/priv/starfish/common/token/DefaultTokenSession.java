package priv.starfish.common.token;

import java.io.Serializable;

public class DefaultTokenSession implements TokenSession, Serializable {

	private static final long serialVersionUID = 1L;

	private Integer appId;

	private Integer userId;

	private String token;

	private String lastIp;

	private Double lastLng;

	private Double lastLat;

	private String lastAddress;

	private long lastAccessedTime = System.currentTimeMillis();

	@Override
	public Integer getAppId() {
		return appId;
	}

	@Override
	public void setAppId(Integer appId) {
		this.appId = appId;
	}

	@Override
	public Integer getUserId() {
		return userId;
	}

	@Override
	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Override
	public String getToken() {
		return token;
	}

	@Override
	public void setToken(String token) {
		this.token = token;
	}

	@Override
	public String getLastIp() {
		return lastIp;
	}

	@Override
	public void setLastIp(String lastIp) {
		this.lastIp = lastIp;
	}

	@Override
	public Double getLastLng() {
		return lastLng;
	}

	@Override
	public void setLastLng(Double lastLng) {
		this.lastLng = lastLng;
	}

	@Override
	public Double getLastLat() {
		return lastLat;
	}

	@Override
	public void setLastLat(Double lastLat) {
		this.lastLat = lastLat;
	}

	@Override
	public String getLastAddress() {
		return lastAddress;
	}

	@Override
	public void setLastAddress(String lastAddress) {
		this.lastAddress = lastAddress;
	}

	@Override
	public long getLastAccessedTime() {
		return lastAccessedTime;
	}

	@Override
	public void setLastAccessedTime(long lastAccessTime) {
		this.lastAccessedTime = lastAccessTime;
	}

	@Override
	public String toString() {
		return "DefaultTokenSession [appId=" + appId + ", userId=" + userId + ", token=" + token + ", lastIp=" + lastIp + ", lastLng=" + lastLng + ", lastLat=" + lastLat + ", lastAddress=" + lastAddress + ", lastAccessedTime="
				+ lastAccessedTime + "]";
	}
}
