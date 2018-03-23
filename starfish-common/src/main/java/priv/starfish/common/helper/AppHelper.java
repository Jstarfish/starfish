package priv.starfish.common.helper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import priv.starfish.common.cache.CacheManager;
import priv.starfish.common.jms.SimpleMessageSender;
import priv.starfish.common.token.TokenSessionStore;

public class AppHelper {
	private static final Log logger = LogFactory.getLog(AppHelper.class);

	// ------------------------ 全局缓存支持 -------------------------------------
	private static CacheManager cacheManager;

	public static CacheManager getCacheManager() {
		return cacheManager;
	}

	public static void setCacheManager(CacheManager cacheManager) {
		AppHelper.cacheManager = cacheManager;
		if (cacheManager != null) {
			logger.debug("CacheManager 已设置");
		}
	}

	// ------------------------ token session 支持 -------------------------------
	private static TokenSessionStore tokenSessionStore;

	public static TokenSessionStore getTokenSessionStore() {
		return tokenSessionStore;
	}

	public static void setTokenSessionStore(TokenSessionStore tokenSessionStore) {
		AppHelper.tokenSessionStore = tokenSessionStore;
		if (tokenSessionStore != null) {
			logger.debug("TokenSessionStore 已设置");
		}
	}

	// ---------------------------------------------------------------------------

	private static SimpleMessageSender simpleMessageSender;

	public static SimpleMessageSender getSimpleMessageSender() {
		return simpleMessageSender;
	}

	public static void setSimpleMessageSender(SimpleMessageSender simpleMessageSender) {
		AppHelper.simpleMessageSender = simpleMessageSender;
		if (simpleMessageSender != null) {
			logger.debug("SimpleMessageSender 已设置");
		}
	}

	// ------------------------本地缓存对象 支持 ---------------------------------
	private static Map<String, Object> localObjects = new ConcurrentHashMap<String, Object>();

	public static void setLocalObject(String key, Object value) {
		localObjects.put(key, value);
	}

	@SuppressWarnings("unchecked")
	public static <T> T getLocalObject(String key) {
		return (T) localObjects.get(key);
	}

	@SuppressWarnings("unchecked")
	public static <T> T removeLocalObject(String key) {
		return (T) localObjects.remove(key);
	}

	public static void clearLocalObjects() {
		localObjects.clear();
	}

}
