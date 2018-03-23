package priv.starfish.mall.web.jms;

import priv.starfish.common.jms.MessageHandlerAdapter;
import priv.starfish.common.jms.SimpleMessage;

import java.io.Serializable;

/**
 * 默认消息处理器
 * 
 * @author koqiui
 * @date 2015年6月27日 下午11:00:07
 *
 */
public class DefaultMessageHandler extends MessageHandlerAdapter {
	@Override
	public void handleMessage(Serializable object) {
		assert object instanceof SimpleMessage;
		//
		SimpleMessage message = (SimpleMessage) object;
		//
		int messageType = message.type;
		//
		if (SimpleMessage.Topic == messageType) {
			this.handleTopicMessage(message);
		} else if (SimpleMessage.Queue == messageType) {
			this.handleQueueMessage(message);
		} else {
			logger.warn("未知类型消息（已忽略） : " + message);
		}
	}

	// ----------------------------->> 主题消息 <<----------------------------
	private void handleTopicMessage(SimpleMessage message) {
		String category = message.category;
		logger.debug("Topic[" + category + "]" + message);
		// TODO
	}

	// ----------------------------->> 队列消息 <<----------------------------
	private void handleQueueMessage(SimpleMessage message) {
		String category = message.category;
		logger.debug("Queue[" + category + "]" + message);
		// TODO
	}

}
