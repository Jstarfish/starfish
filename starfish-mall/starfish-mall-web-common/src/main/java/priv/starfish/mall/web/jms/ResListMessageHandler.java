package priv.starfish.mall.web.jms;

import priv.starfish.common.helper.AppHelper;
import priv.starfish.common.jms.MessageHandlerAdapter;
import priv.starfish.common.jms.SimpleMessage;
import priv.starfish.mall.service.BaseConst;
import priv.starfish.mall.web.base.AppBase;

import java.io.Serializable;

/**
 * 服务配置信息变更消息
 * 
 * @author koqiui
 * @date 2015年6月27日 下午10:21:37
 *
 */
public class ResListMessageHandler extends MessageHandlerAdapter {
	private final int targetType = SimpleMessage.Topic;
	private final String targetTopic = BaseConst.TopicNames.RESLIST;

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

	private void doHandleMessage(SimpleMessage message) {
		String subject = message.subject;
		if (BaseConst.SubjectNames.RESOURCE.equals(subject)) {
			// 系统资源（本地内存）
			AppHelper.removeLocalObject(AppBase.LocalCacheKeys.PROTECTED_URL_RESOURCES);
			// AppHelper.removeLocalObject(AppBase.LocalCacheKeys.ALL_URL_RESOURCES);
			//
		} else if (BaseConst.SubjectNames.BRAND_DEF.equals(subject)) {
			// 品牌定义

		} else if (BaseConst.SubjectNames.COLOR_DEF.equals(subject)) {
			// 品牌定义

		}
		// ...
	}
}
