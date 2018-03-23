package priv.starfish.mall.web.jms;

import priv.starfish.common.jms.MessageHandlerAdapter;
import priv.starfish.common.jms.SimpleMessage;
import priv.starfish.mall.service.BaseConst;
import priv.starfish.mall.web.base.AppBase;
import priv.starfish.mall.web.base.AppBase.CacheNames;

import java.io.Serializable;

/**
 * 服务配置信息变更消息
 * 
 * @author koqiui
 * @date 2015年6月27日 下午10:21:37
 *
 */
public class CacheMessageHandler extends MessageHandlerAdapter {
	private final int targetType = SimpleMessage.Queue;
	private final String targetQueue = BaseConst.QueueNames.CACHE;

	@Override
	public void handleMessage(Serializable object) {
		assert object instanceof SimpleMessage;
		//
		SimpleMessage message = (SimpleMessage) object;
		//
		int messageType = message.type;
		String category = message.category;
		if (targetType == messageType && targetQueue.equals(category)) {
			this.doHandleMessage(message);
		} else {
			logger.warn("未知类型消息（已忽略） : " + message);
		}
	}

	private void doHandleMessage(SimpleMessage message) {
		String subject = message.subject;
		if (BaseConst.SubjectNames.PERMISSION.equals(subject)) {
			// 权限发生变化
			// 清空对应缓存
			AppBase.clearUserPermIdsCache();
			//
			this.logger.debug("已清空缓存：" + CacheNames.UserPermIdsCache);
		} else if (BaseConst.SubjectNames.REGION_LIST.equals(subject)) {
			// 地区发生变化
			// 清空对应缓存
			AppBase.clearRegionListCache();
			//
			this.logger.debug("已清空缓存：" + CacheNames.RegionListCache);
		}
		// ...
	}
}
