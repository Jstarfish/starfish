package priv.starfish.mall.service.misc;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import priv.starfish.common.jms.SimpleMessage;
import priv.starfish.common.jms.SimpleMessageSender;
import priv.starfish.mall.order.dict.OrderAction;
import priv.starfish.mall.order.dict.OrderType;
import priv.starfish.mall.order.dto.XOrderActionResult;
import priv.starfish.mall.service.BaseConst;

public class XOrderMessageProxy {
	private final Log logger = LogFactory.getLog(this.getClass());

	private XOrderMessageProxy() {
		//
	}

	public static interface MessageListener {
		public void onActionMessage(XOrderActionResult message, String source);
	}

	private String messageTopicName = null;
	private String messageQueueName = null;

	public void setMessageTopicName(String messageTopicName) {
		this.messageTopicName = messageTopicName;
		//
		this.logger.debug("消息主题设置为：" + messageTopicName);
	}

	public void setMessageQueueName(String messageQueueName) {
		this.messageQueueName = messageQueueName;
		//
		this.logger.debug("消息队列设置为：" + messageQueueName);
	}

	public String getMessageTopicName() {
		return messageTopicName;
	}

	public String getMessageQueueName() {
		return messageQueueName;
	}

	private SimpleMessageSender simpleMessageSender;

	public void setSimpleMessageSender(SimpleMessageSender simpleMessageSender) {
		this.simpleMessageSender = simpleMessageSender;
		//
		this.logger.debug("消息发送者设置为：" + simpleMessageSender.getClass().getName());
	}

	//
	private static XOrderMessageProxy instance = new XOrderMessageProxy();

	public static XOrderMessageProxy getInstance() {
		return instance;
	}

	public void sendActionMessage(XOrderActionResult message) {
		if (this.simpleMessageSender != null) {
			// 如果有消息队列支持则发送Topic消息到多个应用
			SimpleMessage messageToSend = SimpleMessage.newOne();
			messageToSend.subject = BaseConst.SubjectNames.XORDER_ACTION;
			messageToSend.data = message;
			simpleMessageSender.sendTopicMessage(messageTopicName, messageToSend);
			//
			if (this.messageQueueName != null) {
				messageToSend.category = null;
				simpleMessageSender.sendQueueMessage(messageQueueName, messageToSend);
			}
		}
	}

	public void sendActionMessage(OrderType orderType, Long orderId, String orderNo, Integer userId, Integer shopId, OrderAction orderAction, Boolean orderActionResult) {
		XOrderActionResult message = new XOrderActionResult();
		message.orderType = orderType;
		message.orderId = orderId;
		message.orderNo = orderNo;
		message.userId = userId;
		message.shopId = shopId;
		message.orderAction = orderAction;
		message.orderActionResult = orderActionResult;
		//
		this.sendActionMessage(message);
	}

	private List<MessageListener> messageListeners = new CopyOnWriteArrayList<MessageListener>();

	public void addMessageListener(MessageListener messageListener) {
		if (!messageListeners.contains(messageListener)) {
			messageListeners.add(messageListener);
		}
	}

	public void removeMessageListener(MessageListener messageListener) {
		if (messageListeners.contains(messageListener)) {
			messageListeners.remove(messageListener);
		}
	}

	public void receiveActionMessage(XOrderActionResult message, String source) {
		this.logger.debug("已接收：" + message.toString());
		for (int i = 0; i < this.messageListeners.size(); i++) {
			this.messageListeners.get(i).onActionMessage(message, source);
		}
	}
}
