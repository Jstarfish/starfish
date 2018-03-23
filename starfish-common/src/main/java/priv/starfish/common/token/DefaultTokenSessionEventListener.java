package priv.starfish.common.token;

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import priv.starfish.common.util.DateUtil;


public class DefaultTokenSessionEventListener implements TokenSessionEventListener {
	private final Log logger = LogFactory.getLog(this.getClass());

	//
	@Override
	public void onSessionCreated(TokenSession tokenSession) {
		logger.info("Token 已创建 >> userId: " + tokenSession.getUserId() + ", appId: " + tokenSession.getAppId() + ", token: " + tokenSession.getToken() + ", 最后访问时间：" + DateUtil.toStdTimestampStr(new Date(tokenSession.getLastAccessedTime())));
	}

	@Override
	public void onSessionUpdated(TokenSession tokenSession) {
		logger.info("Token 已更新 >> userId: " + tokenSession.getUserId() + ", appId: " + tokenSession.getAppId() + ", token: " + tokenSession.getToken() + ", 最后访问时间：" + DateUtil.toStdTimestampStr(new Date(tokenSession.getLastAccessedTime())));
	}

	@Override
	public void onSessionExpired(TokenSession tokenSession) {
		logger.info("Token 已过期 >> userId: " + tokenSession.getUserId() + ", appId: " + tokenSession.getAppId() + ", token: " + tokenSession.getToken() + ", 最后访问时间：" + DateUtil.toStdTimestampStr(new Date(tokenSession.getLastAccessedTime())));
	}

	@Override
	public void onSessionRemoved(TokenSession tokenSession) {
		logger.info("Token 已删除 >> userId: " + tokenSession.getUserId() + ", appId: " + tokenSession.getAppId() + ", token: " + tokenSession.getToken() + ", 最后访问时间：" + DateUtil.toStdTimestampStr(new Date(tokenSession.getLastAccessedTime())));
	}

}
