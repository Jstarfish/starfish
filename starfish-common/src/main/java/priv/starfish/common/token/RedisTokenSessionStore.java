package priv.starfish.common.token;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import priv.starfish.common.cache.redis.RedisCacheManager;
import priv.starfish.common.cache.redis.RedisCache;

public class RedisTokenSessionStore implements TokenSessionStore {
	public static final String tokenSessionMapCacheName = "tokenSessionMapCache";
	public static final String userIdTokensMapCacheName = "userIdTokensMapCache";
	private List<TokenSessionEventListener> tokenSessionEventListeners = new ArrayList<TokenSessionEventListener>();
	private TokenGenerator tokenGenerator;
	// private TokenSessionExpireStrategy tokenSessionExpireStrategy;
	// token <=> TokenSession
	private RedisCache<String, TokenSession> tokenSessionMapCache = null;
	// userId <=> appId<=>Token(s)
	private RedisCache<Integer, Map<Integer, String>> userIdTokensMapCache = null;

	@SuppressWarnings("unchecked")
	public RedisTokenSessionStore(RedisCacheManager redisCacheManager) {
		tokenSessionMapCache = (RedisCache<String, TokenSession>) redisCacheManager.getCache(tokenSessionMapCacheName);
		userIdTokensMapCache = (RedisCache<Integer, Map<Integer, String>>) redisCacheManager.getCache(userIdTokensMapCacheName);
	}

	// 删除指定的TokenSession
	private void removeTokenSessionInner(String token) {
		TokenSession tokenSession = tokenSessionMapCache.get(token);
		tokenSessionMapCache.evict(token);
		if (tokenSession == null) {
			return;
		}
		//
		Integer userId = tokenSession.getUserId();
		Map<Integer, String> userTokens = userIdTokensMapCache.get(userId);
		if (userTokens != null) {
			Integer appId = tokenSession.getAppId();
			//
			userTokens.remove(appId);
			//
			userIdTokensMapCache.put(userId, userTokens);
		}
		//
		notifyEventListeners(TokenSessionEvent.Removed, tokenSession);
	}

	// 执行TokenSessionEvent通知
	private void notifyEventListeners(TokenSessionEvent tokenSessionEvent, TokenSession tokenSession) {
		if (TokenSessionEvent.Updated.equals(tokenSessionEvent)) {
			for (TokenSessionEventListener TokenSessionEventListener : tokenSessionEventListeners) {
				TokenSessionEventListener.onSessionUpdated(tokenSession);
			}
		} else if (TokenSessionEvent.Created.equals(tokenSessionEvent)) {
			for (TokenSessionEventListener TokenSessionEventListener : tokenSessionEventListeners) {
				TokenSessionEventListener.onSessionCreated(tokenSession);
			}
		} else if (TokenSessionEvent.Expired.equals(tokenSessionEvent)) {
			for (TokenSessionEventListener TokenSessionEventListener : tokenSessionEventListeners) {
				TokenSessionEventListener.onSessionExpired(tokenSession);
			}
		} else if (TokenSessionEvent.Removed.equals(tokenSessionEvent)) {
			for (TokenSessionEventListener TokenSessionEventListener : tokenSessionEventListeners) {
				TokenSessionEventListener.onSessionRemoved(tokenSession);
			}
		}
	}

	// TokenSession过期时间
	private static final int DEAULT_TIMEOUT_MINUTES = 60 * 1;
	//
	private int timeoutMinutes = DEAULT_TIMEOUT_MINUTES;
	private long timeoutMillis = TimeUnit.MINUTES.toMillis(timeoutMinutes);

	public void setTimeoutMinutes(int timeoutMinutes) {
		this.timeoutMinutes = timeoutMinutes;
		this.timeoutMillis = TimeUnit.MINUTES.toMillis(timeoutMinutes);
	}

	@Override
	public void addTokenSessionEventListener(TokenSessionEventListener tokenSessionEventListener) {
		if (tokenSessionEventListeners.indexOf(tokenSessionEventListener) == -1) {
			this.tokenSessionEventListeners.add(tokenSessionEventListener);
		}
	}

	@Override
	public void setTokenSessionEventListener(TokenSessionEventListener tokenSessionEventListener) {
		this.addTokenSessionEventListener(tokenSessionEventListener);
	}

	@Override
	public void setTokenGenerator(TokenGenerator tokenGenerator) {
		this.tokenGenerator = tokenGenerator;
	}

	@Override
	public void setTokenSessionExpireStrategy(TokenSessionExpireStrategy tokenSessionExpireStrategy) {
		// this.tokenSessionExpireStrategy = tokenSessionExpireStrategy;
	}

	@Override
	public TokenSession getTokenSession(Integer userId, Integer appId) {
		Map<Integer, String> userTokens = this.userIdTokensMapCache.get(userId);
		if (userTokens == null) {
			return null;
		}
		for (Map.Entry<Integer, String> tokenEntry : userTokens.entrySet()) {
			if (appId.equals(tokenEntry.getKey())) {
				String token = tokenEntry.getValue();
				return this.tokenSessionMapCache.get(token);
			}
		}
		return null;
	}

	@Override
	public TokenSession getTokenSession(String token) {
		return this.tokenSessionMapCache.get(token);
	}

	@Override
	public TokenSession createTokenSession(Integer userId, Integer appId) {
		TokenSession tokenSession = this.getTokenSession(userId, appId);
		if (tokenSession == null) {
			String token = this.tokenGenerator.generateToken();
			//
			tokenSession = new DefaultTokenSession();
			tokenSession.setToken(token);
			tokenSession.setUserId(userId);
			tokenSession.setAppId(appId);
			long lastAccessedTime = System.currentTimeMillis();
			tokenSession.setLastAccessedTime(lastAccessedTime);
			this.tokenSessionMapCache.put(token, tokenSession);
			long expireTime = this.timeoutMillis + lastAccessedTime;
			this.tokenSessionMapCache.expireAt(token, new Date(expireTime));
			//
			Map<Integer, String> userTokens = this.userIdTokensMapCache.get(userId);
			if (userTokens == null) {
				userTokens = new HashMap<Integer, String>();
			}
			userTokens.put(appId, token);
			//
			this.userIdTokensMapCache.put(userId, userTokens);
			//
			this.notifyEventListeners(TokenSessionEvent.Created, tokenSession);
		} else {
			this.touchTokenSession(tokenSession.getToken());
		}
		return tokenSession;
	}

	@Override
	public boolean touchTokenSession(String token) {
		TokenSession tokenSession = this.tokenSessionMapCache.get(token);
		if (tokenSession == null) {
			return false;
		} else {
			long lastAccessedTime = System.currentTimeMillis();
			tokenSession.setLastAccessedTime(lastAccessedTime);
			this.tokenSessionMapCache.put(token, tokenSession);
			long expireTime = this.timeoutMillis + lastAccessedTime;
			this.tokenSessionMapCache.expireAt(token, new Date(expireTime));
			//
			this.notifyEventListeners(TokenSessionEvent.Updated, tokenSession);
			return true;
		}
	}

	@Override
	public TokenSession removeTokenSession(String token) {
		TokenSession tokenSession = this.tokenSessionMapCache.get(token);
		if (tokenSession != null) {
			this.removeTokenSessionInner(token);
		}
		return tokenSession;
	}

	@Override
	public void __clear() {
		this.userIdTokensMapCache.clear();
		this.tokenSessionMapCache.clear();
	}
}
