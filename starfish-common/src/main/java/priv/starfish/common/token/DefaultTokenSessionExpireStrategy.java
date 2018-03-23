package priv.starfish.common.token;

public class DefaultTokenSessionExpireStrategy implements TokenSessionExpireStrategy {
	private static final int HourMilliSeconds = 1000 * 60 * 60;
	// 默认1天过期
	public static final int DefaultExpireTimeInHours = 24 * 1;
	// 过期时间（单位：毫秒）
	private int expireTimeInMilliSeconds = 1000 * 120;

	public DefaultTokenSessionExpireStrategy() {
		this(DefaultExpireTimeInHours);
	}

	public DefaultTokenSessionExpireStrategy(int expireTimeInHours) {
		this.expireTimeInMilliSeconds = expireTimeInHours * HourMilliSeconds;
	}

	@Override
	public boolean isTokenSessionExpired(TokenSession tokenSession) {
		if (tokenSession == null) {
			return true;
		}
		return System.currentTimeMillis() - tokenSession.getLastAccessedTime() > expireTimeInMilliSeconds;
	}
}
