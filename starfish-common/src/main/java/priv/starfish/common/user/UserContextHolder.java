package priv.starfish.common.user;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import priv.starfish.common.token.TokenSessionStore;

//
//参考DispatcherServlet && org.springframework.context.i18n.LocaleContextHolder

public class UserContextHolder {
	private static final Log logger = LogFactory.getLog(UserContextHolder.class);
	//
	public static final String SESSION_KEY_USER_CONTEXT = "global_user_context";
	public static final String SESSION_KEY_USER = "global_user";
	// ---------- UserContextHandler
	private static UserContextHandler userContextHandler;

	public static void setUserContextHandler(UserContextHandler userContextHandler) {
		if (UserContextHolder.userContextHandler == null && userContextHandler != null) {
			UserContextHolder.userContextHandler = userContextHandler;
			//
			logger.warn("设定 UserContextHandler : " + UserContextHolder.userContextHandler.getClass().getName());
		}
	}

	public static UserContextHandler getUserContextHandler() {
		return userContextHandler;
	}

	// ---------- TokenSessionStore
	private static TokenSessionStore tokenSessionStore;

	public static void setTokenSessionStore(TokenSessionStore tokenSessionStore) {
		if (UserContextHolder.tokenSessionStore == null && tokenSessionStore != null) {
			UserContextHolder.tokenSessionStore = tokenSessionStore;
			//
			logger.warn("设定 TokenSessionStore : " + UserContextHolder.tokenSessionStore.getClass().getName());
		}
	}

	public static TokenSessionStore getTokenSessionStore() {
		return tokenSessionStore;
	}

	// ---------- UserInfoSetter
	private static UserInfoSetter userInfoSetter;

	public static void setUserInfoSetter(UserInfoSetter userInfoSetter) {
		if (UserContextHolder.userInfoSetter == null && userInfoSetter != null) {
			UserContextHolder.userInfoSetter = userInfoSetter;
			//
			logger.warn("设定 UserInfoSetter : " + UserContextHolder.userInfoSetter.getClass().getName());
		}
	}

	private static ThreadLocal<UserContext> threadUserContext = new InheritableThreadLocal<UserContext>();

	public static UserContext getUserContext() {
		return threadUserContext.get();
	}

	public static void setUserContext(UserContext userContext) {
		// logger.warn("重新设置 UserContext");
		//
		threadUserContext.set(userContext);
	}

	public static void clearUserContext() {
		logger.warn("清除 UserContext");
		//
		UserContext userContext = threadUserContext.get();
		userContext.clear();
		threadUserContext.set(userContext);
	}

	public static UserContext loadUserInfoIntoContext() {
		userInfoSetter.setUserInfo(threadUserContext.get());
		return threadUserContext.get();
	}
}
