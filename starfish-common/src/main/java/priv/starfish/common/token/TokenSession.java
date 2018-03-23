package priv.starfish.common.token;


public interface TokenSession {
	public static final String TOKEN_PARAM_NAME = "token";

	Integer getAppId();

	void setAppId(Integer appId);

	Integer getUserId();

	void setUserId(Integer userId);

	String getToken();

	void setToken(String token);

	String getLastIp();

	void setLastIp(String lastIp);

	Double getLastLng();

	void setLastLng(Double lastLng);

	Double getLastLat();

	void setLastLat(Double lastLat);

	String getLastAddress();

	void setLastAddress(String lastAddress);

	long getLastAccessedTime();

	void setLastAccessedTime(long lastAccessedTime);
}