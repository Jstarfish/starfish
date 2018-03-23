package priv.starfish.common.webols;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import priv.starfish.common.jms.SimpleMessage;
import priv.starfish.common.jms.SimpleMessageSender;

public class OlsMessageProxy {
	private final Log logger = LogFactory.getLog(this.getClass());

	private OlsMessageProxy() {
		//
	}

	private String messageTopicName = null;

	public void setMessageTopicName(String messageTopicName) {
		this.messageTopicName = messageTopicName;
		//
		this.logger.debug("消息主题设置为：" + messageTopicName);
	}

	public String getMessageTopicName() {
		return messageTopicName;
	}

	private SimpleMessageSender simpleMessageSender;

	public void setSimpleMessageSender(SimpleMessageSender simpleMessageSender) {
		this.simpleMessageSender = simpleMessageSender;
		//
		this.logger.debug("消息发送者设置为：" + simpleMessageSender.getClass().getName());
	}

	private OlsSessionManager sessionManager = OlsSessionManager.getInstance();

	//
	private static OlsMessageProxy instance = new OlsMessageProxy();

	public static OlsMessageProxy getInstance() {
		return instance;
	}

	public boolean sendMessage(OlsMessage message) {
		if (this.simpleMessageSender == null) {
			// 无消息队列支持，直接分发到本地
			return this.receiveMessage(message);
		} else {
			// 如果有消息队列支持则发送Topic消息到多个应用
			SimpleMessage messageToSend = SimpleMessage.newOne();
			messageToSend.change = SimpleMessage.Created;
			messageToSend.data = message;
			simpleMessageSender.sendTopicMessage(messageTopicName, messageToSend);
			return true;
		}
	}

	public boolean receiveMessage(OlsMessage message) {
		return sessionManager.dispatchMessage(message);
	}
}
