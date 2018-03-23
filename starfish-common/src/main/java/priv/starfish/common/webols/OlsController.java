package priv.starfish.common.webols;

import priv.starfish.common.util.DateUtil;
import priv.starfish.common.util.StrUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * 处理 来自客户端的 servlet 请求
 * 
 * @author koqiui
 * @date 2015年6月21日 下午5:36:12
 *
 */
public class OlsController {
	private OlsController() {
		//
	}

	//
	private static OlsController instance = new OlsController();

	public static OlsController getInstance() {
		return instance;
	}

	private OlsMessageProxy messageProxy = OlsMessageProxy.getInstance();

	//
	public void doCommand(OlsCommand command) {
		OlsSession session = command.getSession();
		try {
			session.kick();
			//
			String eventType = command.getReqEvent().getType();
			if (eventType.equals(OlsProtocol.EVENT_START)) {
				this.doStart(command);
			} else if (eventType.equals(OlsProtocol.EVENT_HEARTBEAT)) {
				this.doHeatbeat(command);
			} else if (eventType.equals(OlsProtocol.EVENT_END)) {
				this.doEnd(command);
			} else if (eventType.equals(OlsProtocol.EVENT_SPEAK)) {
				this.doSpeak(command);
			} else if (eventType.equals(OlsProtocol.EVENT_LISTEN)) {
				this.doListen(command);
			} else if (eventType.equals(OlsProtocol.EVENT_CHECK)) {
				this.doCheck(command);
			}
		} catch (Throwable t) {
			t.printStackTrace();
			//
			session.stop();
			//
			OlsEvent rspEvent = OlsEvent.newEvent(command.getReqEvent().getSource(), OlsProtocol.EVENT_NACK, null);
			rspEvent.setAttr(OlsProtocol.PARAM_SESSION_ID, session.getId());
			rspEvent.setAttr(OlsProtocol.PARAM_REASON, "意外错误：" + t.getMessage());
			//
			try {
				command.getHttpAdapter().push(rspEvent);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	protected void doStart(OlsCommand command) throws IOException {
		OlsSession session = command.getSession();
		session.start();
		//
		OlsEvent rspEvent = OlsEvent.newEvent(command.getReqEvent().getSource(), OlsProtocol.EVENT_START_ACK, null);
		rspEvent.setAttr(OlsProtocol.PARAM_SESSION_ID, session.getId());
		rspEvent.setAttr(OlsProtocol.PARAM_TIMEOUT, OlsSessionManager.getInstance().getTimeoutMinutes());
		//
		command.getHttpAdapter().push(rspEvent);
	}

	protected void doHeatbeat(OlsCommand command) throws IOException {
		OlsSession session = command.getSession();
		OlsEvent rspEvent = OlsEvent.newEvent(command.getReqEvent().getSource(), OlsProtocol.EVENT_HEARTBEAT_ACK, null);
		rspEvent.setAttr(OlsProtocol.PARAM_SESSION_ID, session.getId());
		//
		command.getHttpAdapter().push(rspEvent);
	}

	protected void doEnd(OlsCommand command) throws IOException {
		OlsSession session = command.getSession();
		session.stop();
		//
		OlsEvent rspEvent = OlsEvent.newEvent(command.getReqEvent().getSource(), OlsProtocol.EVENT_END_ACK, null);
		rspEvent.setAttr(OlsProtocol.PARAM_SESSION_ID, session.getId());
		//
		command.getHttpAdapter().push(rspEvent);
	}

	protected void doSpeak(OlsCommand command) throws IOException {
		OlsSession session = command.getSession();
		OlsEvent rspEvent = null;
		OlsEvent reqEvent = command.getReqEvent();
		//
		String source = reqEvent.getSource();
		String customerId = String.valueOf(reqEvent.getAttr(OlsProtocol.PARAM_CUSTOMER_ID));
		String servantId = String.valueOf(reqEvent.getAttr(OlsProtocol.PARAM_SERVANT_ID));
		if (StrUtil.isNullOrBlank(customerId) || StrUtil.isNullOrBlank(servantId)) {
			rspEvent = OlsEvent.newEvent(source, OlsProtocol.EVENT_FACK, null);
			rspEvent.setAttr(OlsProtocol.PARAM_REASON, "交谈失败：缺乏双方信息");
		} else {
			session.kickSpeakingTime();
			//
			String content = String.valueOf(reqEvent.getAttr(OlsProtocol.PARAM_CONTENT, ""));
			//
			OlsMessage message = new OlsMessage();
			message.source = source;
			message.customerId = customerId;
			message.servantId = servantId;
			message.content = content;
			// message.ts
			//
			boolean result = messageProxy.sendMessage(message);
			//
			if (result) {
				rspEvent = OlsEvent.newEvent(source, OlsProtocol.EVENT_SPEAK_ACK, null);
				rspEvent.setAttr(OlsProtocol.PARAM_CONTENT, content);
				rspEvent.setAttr(OlsProtocol.PARAM_TIMESTAMP, DateUtil.toStdDateTimeStr(message.ts));
			} else {
				rspEvent = OlsEvent.newEvent(source, OlsProtocol.EVENT_FACK, null);
				rspEvent.setAttr(OlsProtocol.PARAM_REASON, "交谈失败：对方无应答");
			}
		}
		rspEvent.setAttr(OlsProtocol.PARAM_SESSION_ID, session.getId());
		rspEvent.setAttr(OlsProtocol.PARAM_CUSTOMER_ID, customerId);
		rspEvent.setAttr(OlsProtocol.PARAM_SERVANT_ID, servantId);
		//
		command.getHttpAdapter().push(rspEvent);
	}

	protected void doListen(OlsCommand command) throws IOException {
		OlsSession session = command.getSession();
		OlsEvent rspEvent = null;
		OlsEvent reqEvent = command.getReqEvent();
		//
		String source = reqEvent.getSource();
		String customerId = String.valueOf(reqEvent.getAttr(OlsProtocol.PARAM_CUSTOMER_ID));
		String servantId = String.valueOf(reqEvent.getAttr(OlsProtocol.PARAM_SERVANT_ID));
		if (OlsProtocol.SOURCE_CUSTOMER.equalsIgnoreCase(source)) {
			if (StrUtil.isNullOrBlank(customerId)) {
				rspEvent = OlsEvent.newEvent(source, OlsProtocol.EVENT_FACK, null);
				rspEvent.setAttr(OlsProtocol.PARAM_REASON, "监听失败：缺乏customerId信息");
			} else {
				Map<String, List<OlsMessage>> messages = session.getMessages();
				rspEvent = OlsEvent.newEvent(source, OlsProtocol.EVENT_LISTEN_ACK, null);
				rspEvent.setAttr(OlsProtocol.PARAM_MESSAGES, messages);
			}
		} else if (OlsProtocol.SOURCE_SERVANT.equalsIgnoreCase(source)) {
			if (StrUtil.isNullOrBlank(servantId)) {
				rspEvent = OlsEvent.newEvent(source, OlsProtocol.EVENT_FACK, null);
				rspEvent.setAttr(OlsProtocol.PARAM_REASON, "监听失败：缺乏servantId信息");
			} else {
				Map<String, List<OlsMessage>> messages = session.getMessages();
				rspEvent = OlsEvent.newEvent(source, OlsProtocol.EVENT_LISTEN_ACK, null);
				rspEvent.setAttr(OlsProtocol.PARAM_MESSAGES, messages);
			}
		} else {
			rspEvent = OlsEvent.newEvent(source, OlsProtocol.EVENT_FACK, null);
			rspEvent.setAttr(OlsProtocol.PARAM_REASON, "监听失败：不支持");
		}
		rspEvent.setAttr(OlsProtocol.PARAM_SESSION_ID, session.getId());
		//
		command.getHttpAdapter().push(rspEvent);
	}

	protected void doCheck(OlsCommand command) throws IOException {
		OlsSession session = command.getSession();
		OlsEvent rspEvent = null;
		OlsEvent reqEvent = command.getReqEvent();
		//
		String source = reqEvent.getSource();
		String servantId = String.valueOf(reqEvent.getAttr(OlsProtocol.PARAM_SERVANT_ID));
		if (OlsProtocol.SOURCE_MONITOR.equalsIgnoreCase(source)) {
			if (StrUtil.isNullOrBlank(servantId)) {
				rspEvent = OlsEvent.newEvent(source, OlsProtocol.EVENT_FACK, null);
				rspEvent.setAttr(OlsProtocol.PARAM_REASON, "检查失败：缺乏servantId信息");
			} else {
				boolean hasMessages = session.hasMessages();
				rspEvent = OlsEvent.newEvent(source, OlsProtocol.EVENT_CHECK_ACK, null);
				rspEvent.setAttr(OlsProtocol.PARAM_MESSAGES, hasMessages);
			}
		} else {
			rspEvent = OlsEvent.newEvent(source, OlsProtocol.EVENT_FACK, null);
			rspEvent.setAttr(OlsProtocol.PARAM_REASON, "检查失败：不支持");
		}
		rspEvent.setAttr(OlsProtocol.PARAM_SESSION_ID, session.getId());
		//
		command.getHttpAdapter().push(rspEvent);
	}
}
