package priv.starfish.common.base;

import priv.starfish.common.jms.MessageHandlerAdapter;
import priv.starfish.common.jms.SimpleMessage;

import java.io.Serializable;


/**
 * 应用节点消息
 * 
 * @author koqiui
 * @date 2015年6月27日 下午10:22:00
 *
 */
public class AppNodeMessageHandler extends MessageHandlerAdapter {
	private final int targetType = SimpleMessage.Topic;
	private String targetTopic = null;

	@Override
	public void handleMessage(Serializable object) {
		assert object instanceof SimpleMessage;
		//
		if (targetTopic == null) {
			targetTopic = AppNodeInfo.getCurrent().getMessageTopicName();
		}
		//
		SimpleMessage message = (SimpleMessage) object;
		//
		int messageType = message.type;
		String category = message.category;
		if (targetType == messageType && targetTopic.equals(category)) {
			AppNodeInfo appNodeInfo = (AppNodeInfo) message.data;
			this.doHandleMessage(appNodeInfo, message.timestamp);
		} else {
			logger.warn("未知消息（已忽略） : " + message);
		}
	}

	AppNodeCluster appNodeCluster = AppNodeCluster.getCurrent();

	private void doHandleMessage(AppNodeInfo appNodeInfo, long ts) {
		appNodeCluster.addAppNode(appNodeInfo, ts);
	}
}
