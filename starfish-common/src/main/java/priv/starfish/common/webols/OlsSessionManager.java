package priv.starfish.common.webols;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * 管理 session的会话生命周期
 * 
 * @author koqiui
 * @date 2015年6月21日 下午5:44:35
 *
 */
public class OlsSessionManager {
	protected final Log logger = LogFactory.getLog(this.getClass());

	private OlsSessionManager() {
		//
	}

	private static final int DEAULT_TIMEOUT_MINUTES = 2;
	//
	private int timeoutMinutes = DEAULT_TIMEOUT_MINUTES;
	private long timeoutMillis = TimeUnit.MINUTES.toMillis(timeoutMinutes);
	// 顾客不说话超时时间
	private final long customerNoSpeakTimeout = TimeUnit.HOURS.toMillis(1);
	// 客服不说话超时时间
	private final long servantNoSpeakTimeout = TimeUnit.HOURS.toMillis(8);

	public int getTimeoutMinutes() {
		return timeoutMinutes;
	}

	public void setTimeoutMinutes(int timeoutMinutes) {
		this.timeoutMinutes = timeoutMinutes;
		this.timeoutMillis = TimeUnit.MINUTES.toMillis(timeoutMinutes);
	}

	//
	private static OlsSessionManager instance = new OlsSessionManager();

	public static OlsSessionManager getInstance() {
		return instance;
	}

	// session 缓存
	private Map<String, OlsSession> custormerSessions = new ConcurrentHashMap<String, OlsSession>();
	private Map<String, OlsSession> servantSessions = new ConcurrentHashMap<String, OlsSession>();
	private Map<String, OlsSession> mointorSessions = new ConcurrentHashMap<String, OlsSession>();

	//

	public static OlsSession createSession(OlsEvent event) {
		String snId = UUID.randomUUID().toString();
		int sessionType = OlsSession.typeBySource(event.getSource());
		OlsSession session = OlsSession.newOne(sessionType, snId);
		String customerId = String.valueOf(event.getAttr(OlsProtocol.PARAM_CUSTOMER_ID));
		String servantId = String.valueOf(event.getAttr(OlsProtocol.PARAM_SERVANT_ID));
		session.setCustomerId(customerId);
		session.setServantId(servantId);
		//
		return session;
	}

	private Map<String, OlsSession> getSessions(int sessionType) {
		if (sessionType == OlsSession.TYPE_CUSTOMER) {
			return custormerSessions;
		} else if (sessionType == OlsSession.TYPE_SERVANT) {
			return servantSessions;
		} else if (sessionType == OlsSession.TYPE_MONITOR) {
			return mointorSessions;
		}
		return null;
	}

	private List<OlsSession> getSessionsByTargetId(int targetType, String targetId) {
		OlsSession[] sessions = getSessions(targetType).values().toArray(new OlsSession[0]);
		List<OlsSession> retSessions = new ArrayList<OlsSession>();
		if (targetType == OlsSession.TYPE_CUSTOMER) {
			for (int i = 0; i < sessions.length; i++) {
				OlsSession session = sessions[i];
				String customerId = session.getCustomerId();
				if (customerId.equals(targetId)) {
					retSessions.add(session);
				}
			}
		} else {
			for (int i = 0; i < sessions.length; i++) {
				OlsSession session = sessions[i];
				String servantId = session.getServantId();
				if (servantId.equals(targetId)) {
					retSessions.add(session);
				}
			}
		}
		return retSessions;
	}

	public OlsSession getSession(int sessionType, String id) {
		Map<String, OlsSession> sessions = getSessions(sessionType);
		return sessions == null ? null : sessions.get(id);
	}

	public boolean hasSession(int sessionType, String id) {
		return getSession(sessionType, id) != null;
	}

	public void addSession(OlsSession session) {
		int sessionType = session.getType();
		Map<String, OlsSession> sessions = getSessions(sessionType);
		sessions.put(session.getId(), session);
		//
		logger.debug("加入 OlsSession : " + session.getId());
	}

	public void removeSession(OlsSession session) {
		int sessionType = session.getType();
		Map<String, OlsSession> sessions = getSessions(sessionType);
		sessions.remove(session.getId());
		//
		logger.debug("删除 OlsSession : " + session.getId());
	}

	public boolean dispatchMessage(OlsMessage message) {
		String source = message.source.toUpperCase();
		boolean anyIsOk = false;
		if (OlsProtocol.SOURCE_CUSTOMER.equals(source)) {
			int targetType = OlsSession.TYPE_SERVANT;
			String targetId = message.servantId;
			List<OlsSession> targetSessions = this.getSessionsByTargetId(targetType, targetId);
			for (OlsSession session : targetSessions) {
				anyIsOk = session.pushMessage(message) || anyIsOk;
			}
		} else if (OlsProtocol.SOURCE_SERVANT.equals(message.source)) {
			int targetType = OlsSession.TYPE_CUSTOMER;
			String targetId = message.customerId;
			List<OlsSession> targetSessions = this.getSessionsByTargetId(targetType, targetId);
			for (OlsSession session : targetSessions) {
				anyIsOk = session.pushMessage(message) || anyIsOk;
			}
		} else {
			this.logger.warn("丢弃消息：" + message);
		}
		return anyIsOk;
	}

	Timer taskTimer = null;

	private class ExpirationCheckTask extends TimerTask {
		private int sessionType;

		public ExpirationCheckTask(int sessionType) {
			this.sessionType = sessionType;
		}

		@Override
		public void run() {
			Map<String, OlsSession> sessions = getSessions(this.sessionType);
			OlsSession[] theSessions = sessions.values().toArray(new OlsSession[0]);
			long curTime = System.currentTimeMillis();
			if (sessionType == OlsSession.TYPE_CUSTOMER) {
				for (int i = 0; i < theSessions.length; i++) {
					OlsSession theSession = theSessions[i];
					if (curTime - theSession.getLastSpeakingTime() > customerNoSpeakTimeout) {
						theSession.stop();
						logger.debug("顾客长时间未发言已超时：" + theSession.getId());
					} else if (curTime - theSession.getLastAccessedTime() > timeoutMillis) {
						theSession.stop();
					}
				}
			} else if (sessionType == OlsSession.TYPE_SERVANT) {
				for (int i = 0; i < theSessions.length; i++) {
					OlsSession theSession = theSessions[i];
					if (curTime - theSession.getLastSpeakingTime() > servantNoSpeakTimeout) {
						theSession.stop();
						logger.debug("客服长时间未发言已超时：" + theSession.getId());
					} else if (curTime - theSession.getLastAccessedTime() > timeoutMillis) {
						theSession.stop();
					}
				}
			} else {
				for (int i = 0; i < theSessions.length; i++) {
					OlsSession theSession = theSessions[i];
					if (curTime - theSession.getLastAccessedTime() > timeoutMillis) {
						theSession.stop();
					}
				}
			}
		}
	}

	public void start() {
		if (taskTimer != null) {
			stop();
		}
		//
		taskTimer = new Timer(false);
		//
		taskTimer.schedule(new ExpirationCheckTask(OlsSession.TYPE_CUSTOMER), timeoutMillis, timeoutMillis);
		taskTimer.schedule(new ExpirationCheckTask(OlsSession.TYPE_SERVANT), timeoutMillis + TimeUnit.MINUTES.toMillis(1), timeoutMillis);
		taskTimer.schedule(new ExpirationCheckTask(OlsSession.TYPE_MONITOR), timeoutMillis + TimeUnit.MINUTES.toMillis(2), timeoutMillis);
		//
		logger.debug("启动 OlsSession 过期检查任务");
	}

	public void stop() {
		if (taskTimer != null) {
			taskTimer.cancel();
			taskTimer = null;
		}
		//
		custormerSessions.clear();
		servantSessions.clear();
		mointorSessions.clear();
		//
		logger.debug("停止 OlsSession 过期检查任务");
	}

}
