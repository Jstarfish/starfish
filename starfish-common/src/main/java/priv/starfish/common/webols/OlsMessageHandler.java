package priv.starfish.common.webols;

import priv.starfish.common.jms.MessageHandlerAdapter;
import priv.starfish.common.jms.SimpleMessage;

import java.io.Serializable;

/**
 * 在线客服消息
 * 
 * @author koqiui
 * @date 2015年6月27日 下午10:22:00
 *
 */
public class OlsMessageHandler extends MessageHandlerAdapter {
	private final int targetType = SimpleMessage.Topic;
	private String targetTopic = null;
	//
	private OlsMessageProxy olsMessageProxy = OlsMessageProxy.getInstance();

	@Override
	public void handleMessage(Serializable object) {
		assert object instanceof SimpleMessage;
		//
		if (targetTopic == null) {
			targetTopic = olsMessageProxy.getMessageTopicName();
		}
		//
		SimpleMessage message = (SimpleMessage) object;
		//
		int messageType = message.type;
		String category = message.category;
		if (targetType == messageType && targetTopic.equals(category)) {
			OlsMessage olsMsg = (OlsMessage) message.data;
			this.doHandleMessage(olsMsg);
		} else {
			logger.warn("未知消息（已忽略） : " + message);
		}
	}

	private void doHandleMessage(OlsMessage olsMsg) {
		olsMessageProxy.receiveMessage(olsMsg);
	}
}
