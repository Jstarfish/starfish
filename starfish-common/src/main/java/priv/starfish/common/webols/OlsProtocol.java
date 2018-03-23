package priv.starfish.common.webols;

/**
 * Pushlet 协议常量
 * 
 * @author koqiui
 * @date 2015年6月21日 下午5:41:00
 *
 */
public interface OlsProtocol {
	// 事件类型
	// none
	public static final String EVENT_NACK = "nack";
	// fail
	public static final String EVENT_FACK = "fack";
	// timeout
	public static final String EVENT_TOACK = "toack";
	//
	public static final String EVENT_START = "start";
	public static final String EVENT_START_ACK = "start-ack";
	//
	public static final String EVENT_END = "end";
	public static final String EVENT_END_ACK = "end-ack";
	//
	public static final String EVENT_HEARTBEAT = "heartbeat";
	public static final String EVENT_HEARTBEAT_ACK = "heartbeat-ack";
	//
	public static final String EVENT_SPEAK = "speak";
	public static final String EVENT_SPEAK_ACK = "speak-ack";
	//
	public static final String EVENT_LISTEN = "listen";
	public static final String EVENT_LISTEN_ACK = "listen-ack";
	//
	public static final String EVENT_CHECK = "check";
	public static final String EVENT_CHECK_ACK = "check-ack";
	// 参数名称
	public static final String PARAM_EVENT_TYPE = "eventType";
	public static final String PARAM_EVENT_SOURCE = "eventSource";
	// 事件/消息来源
	public static final String SOURCE_CUSTOMER = "C";
	public static final String SOURCE_SERVANT = "S";
	public static final String SOURCE_MONITOR = "M";
	//
	public static final String PARAM_TIMEOUT = "timeout";
	public static final String PARAM_REASON = "reason";

	public static final String PARAM_SESSION_ID = "snId";
	//
	public static final String PARAM_CUSTOMER_ID = "customerId";
	public static final String PARAM_SERVANT_ID = "servantId";
	public static final String PARAM_CONTENT = "content";
	public static final String PARAM_TIMESTAMP = "ts";
	//
	public static final String PARAM_MESSAGES = "messages";

	// 获取人员信息
	public static final String PARAM_ACTION = "action";
	public static final String ACTION_VALUE_INFO_FLAG = "peerInfo";
	public static final String PARAM_PEER_TYPE = "peerType";
	public static final String PARAM_PEER_ID = "peerId";
	// 获取焦点信息
	public static final String ACTION_VALUE_FOCUS_FLAG = "focusInfo";
	public static final String PARAM_FOCUS_CODE = "focusCode";
	public static final String PARAM_FOCUS_ID = "focusId";
}
