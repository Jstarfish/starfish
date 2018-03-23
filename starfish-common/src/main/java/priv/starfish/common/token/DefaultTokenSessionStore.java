package priv.starfish.common.token;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultTokenSessionStore implements TokenSessionStore {
	private List<TokenSessionEventListener> tokenSessionEventListeners = new ArrayList<TokenSessionEventListener>();
	private TokenGenerator tokenGenerator;
	private TokenSessionExpireStrategy tokenSessionExpireStrategy;
	// token <=> TokenSession
	private Map<String, TokenSession> tokenSessionMap = new ConcurrentHashMap<String, TokenSession>();
	// userId <=> appId<=>Token(s)
	private Map<Integer, Map<Integer, String>> userIdTokensMap = new ConcurrentHashMap<Integer, Map<Integer, String>>();

	// 删除指定的TokenSession
	private void removeTokenSessionInner(String token) {
		TokenSession tokenSession = tokenSessionMap.remove(token);
		if (tokenSession == null) {
			return;
		}
		//
		Integer userId = tokenSession.getUserId();
		//
		Map<Integer, String> userTokens = userIdTokensMap.get(userId);
		if (userTokens != null) {
			Integer appId = tokenSession.getAppId();
			//
			userTokens.remove(appId);
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

	// TokenSession过期检查周期
	private static final int ExpirationCheckInterval = 60 * 1000;

	// 过期执行器（清除过期的TokenSession）
	class TokenSessionExpirationExecutor extends Thread {

		@Override
		public void run() {
			while (true) {
				if (tokenSessionExpireStrategy != null) {
					for (Map.Entry<String, TokenSession> tokenSessionEntry : tokenSessionMap.entrySet()) {
						String token = tokenSessionEntry.getKey();
						TokenSession tokenSession = tokenSessionEntry.getValue();
						if (tokenSessionExpireStrategy.isTokenSessionExpired(tokenSession)) {
							//
							notifyEventListeners(TokenSessionEvent.Expired, tokenSession);
							//
							removeTokenSessionInner(token);
						}
					}
				}
				//
				try {
					Thread.sleep(ExpirationCheckInterval);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public DefaultTokenSessionStore() {
		// 启动过期执行器
		new TokenSessionExpirationExecutor().start();
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
		this.tokenSessionExpireStrategy = tokenSessionExpireStrategy;
	}

	@Override
	public TokenSession getTokenSession(Integer userId, Integer appId) {
		Map<Integer, String> userTokenSessions = this.userIdTokensMap.get(userId);
		if (userTokenSessions == null) {
			return null;
		}
		for (Map.Entry<Integer, String> tokenEntry : userTokenSessions.entrySet()) {
			if (appId.equals(tokenEntry.getKey())) {
				String token = tokenEntry.getValue();
				return this.tokenSessionMap.get(token);
			}
		}
		return null;
	}

	@Override
	public TokenSession getTokenSession(String token) {
		return this.tokenSessionMap.get(token);
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
			tokenSession.setLastAccessedTime(System.currentTimeMillis());
			//
			this.tokenSessionMap.put(token, tokenSession);
			//
			Map<Integer, String> userTokens = this.userIdTokensMap.get(userId);
			if (userTokens == null) {
				userTokens = new ConcurrentHashMap<Integer, String>();
				//
				this.userIdTokensMap.put(userId, userTokens);
				//
				userTokens.put(appId, token);
			}
			//
			this.notifyEventListeners(TokenSessionEvent.Created, tokenSession);
		} else {
			this.touchTokenSession(tokenSession.getToken());
		}
		return tokenSession;
	}

	@Override
	public boolean touchTokenSession(String token) {
		TokenSession tokenSession = this.tokenSessionMap.get(token);
		if (tokenSession == null) {
			return false;
		} else {
			tokenSession.setLastAccessedTime(System.currentTimeMillis());
			//
			this.notifyEventListeners(TokenSessionEvent.Updated, tokenSession);
			return true;
		}
	}

	@Override
	public TokenSession removeTokenSession(String token) {
		TokenSession tokenSession = this.tokenSessionMap.get(token);
		if (tokenSession != null) {
			this.removeTokenSessionInner(token);
		}
		return tokenSession;
	}

	@Override
	public void __clear() {
		this.userIdTokensMap.clear();
		this.tokenSessionMap.clear();
	}
}
