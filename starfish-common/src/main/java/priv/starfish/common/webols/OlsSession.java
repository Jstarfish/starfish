package priv.starfish.common.webols;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 代表来自客户端的会话状态
 * 
 * @author koqiui
 * @date 2015年6月21日 下午5:43:56
 *
 */
public class OlsSession {
	public static final int TYPE_UNKNOWN = -1;
	public static final int TYPE_CUSTOMER = 1;
	public static final int TYPE_SERVANT = 2;
	public static final int TYPE_MONITOR = 4;

	//
	private int type;
	private String id;
	private String customerId;
	private String servantId;
	private long creationTime = System.currentTimeMillis();
	private volatile long lastAccessedTime = creationTime;
	private volatile long lastSpeakingTime = creationTime;

	private OlsSession() {
		//
	}

	public static int typeBySource(String source) {
		if (source != null) {
			source = source.toUpperCase();
			if (OlsProtocol.SOURCE_CUSTOMER.equals(source)) {
				return TYPE_CUSTOMER;
			} else if (OlsProtocol.SOURCE_SERVANT.equals(source)) {
				return TYPE_SERVANT;
			} else if (OlsProtocol.SOURCE_MONITOR.equals(source)) {
				return TYPE_MONITOR;
			}
		}
		return TYPE_UNKNOWN;
	}

	public static OlsSession newOne(int type, String id) {
		OlsSession session = new OlsSession();
		session.type = type;
		session.id = id;
		return session;
	}

	public int getType() {
		return type;
	}

	public String getId() {
		return id;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public String getServantId() {
		return servantId;
	}

	public void setServantId(String servantId) {
		this.servantId = servantId;
	}

	public long getCreationTime() {
		return creationTime;
	}

	public long getLastAccessedTime() {
		return lastAccessedTime;
	}

	public long getLastSpeakingTime() {
		return lastSpeakingTime;
	}

	public void kick() {
		this.lastAccessedTime = System.currentTimeMillis();
	}

	public void kickSpeakingTime() {
		this.lastSpeakingTime = System.currentTimeMillis();
	}

	public void start() {
		OlsSessionManager.getInstance().addSession(this);
	}

	public void stop() {
		OlsSessionManager.getInstance().removeSession(this);
	}

	//
	private static final int MAX_QUEUE_SIZE = 10;
	private Map<String, LinkedBlockingQueue<OlsMessage>> peerMessages = new ConcurrentHashMap<String, LinkedBlockingQueue<OlsMessage>>();

	public boolean pushMessage(OlsMessage message) {
		String peerId = null;
		if (this.type == TYPE_CUSTOMER) {
			peerId = message.servantId;
		} else {
			peerId = message.customerId;
		}
		LinkedBlockingQueue<OlsMessage> theQueue = peerMessages.get(peerId);
		if (theQueue == null) {
			theQueue = new LinkedBlockingQueue<OlsMessage>(MAX_QUEUE_SIZE);
			peerMessages.put(peerId, theQueue);
		}
		return theQueue.offer(message);
	}

	public boolean hasMessages() {
		LinkedBlockingQueue<OlsMessage> tmpQueue;
		for (Map.Entry<String, LinkedBlockingQueue<OlsMessage>> entry : peerMessages.entrySet()) {
			tmpQueue = entry.getValue();
			if (tmpQueue != null && tmpQueue.size() > 0) {
				return true;
			}
		}
		return false;
	}

	public Map<String, List<OlsMessage>> getMessages() {
		Map<String, List<OlsMessage>> retMessages = new HashMap<String, List<OlsMessage>>();
		for (Map.Entry<String, LinkedBlockingQueue<OlsMessage>> entry : peerMessages.entrySet()) {
			LinkedBlockingQueue<OlsMessage> theQueue = entry.getValue();
			List<OlsMessage> msgList = new ArrayList<OlsMessage>();
			theQueue.drainTo(msgList);
			if (msgList.size() > 0) {
				String peerId = entry.getKey();
				retMessages.put(peerId, msgList);
			}
		}
		return retMessages;
	}

}
