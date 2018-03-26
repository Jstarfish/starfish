package priv.starfish.mall.manager.jms;

import priv.starfish.common.jms.MessageHandlerAdapter;
import priv.starfish.common.jms.SimpleMessage;
import priv.starfish.mall.order.dto.XOrderActionResult;
import priv.starfish.mall.service.BaseConst;
import priv.starfish.mall.service.misc.XOrderMessageProxy;

import java.io.Serializable;


/**
 * 各类订单信息变更消息
 * 
 * @author koqiui
 * @date 2016年2月1日 下午2:24:01
 *
 */
public class XOrderMessageHandler extends MessageHandlerAdapter {
	private final int targetType = SimpleMessage.Topic;
	private final String targetTopic = BaseConst.TopicNames.XORDER;

	@Override
	public void handleMessage(Serializable object) {
		assert object instanceof SimpleMessage;
		//
		SimpleMessage message = (SimpleMessage) object;
		//
		int messageType = message.type;
		String category = message.category;
		if (targetType == messageType && targetTopic.equals(category)) {
			this.doHandleMessage(message);
		} else {
			logger.warn("未知类型消息（已忽略） : " + message);
		}
	}

	private XOrderMessageProxy xOrderMessageProxy = XOrderMessageProxy.getInstance();

	private void doHandleMessage(SimpleMessage message) {
		String subject = message.subject;
		if (BaseConst.SubjectNames.XORDER_ACTION.equals(subject)) {
			// 处理ecard
			Serializable data = message.data;
			if (data != null && data instanceof XOrderActionResult) {
				XOrderActionResult orderActionResult = (XOrderActionResult) data;
				//
				xOrderMessageProxy.receiveActionMessage(orderActionResult, message.source);
			}
		}
		// ...
	}
}
