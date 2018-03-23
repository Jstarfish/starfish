package priv.starfish.mall.web.jms;

import priv.starfish.common.jms.MessageHandlerAdapter;
import priv.starfish.common.jms.SimpleMessage;
import priv.starfish.common.web.WebEnvHelper;
import priv.starfish.mall.service.BaseConst;
import priv.starfish.mall.service.SearchService;

import java.io.Serializable;

/**
 * 任务队列消息
 * 
 * @author koqiui
 * @date 2015年6月27日 下午10:21:37
 *
 */
public class TaskMessageHandler extends MessageHandlerAdapter {
	private final int targetType = SimpleMessage.Queue;
	private final String targetQueue = BaseConst.QueueNames.TASK;

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
		SearchService searchService = WebEnvHelper.getSpringBean("searchService", SearchService.class);
		String taskFocus = message.subject;
		if (BaseConst.TaskFocus.PRODUCT.equals(taskFocus)) {
			// 具体商品发生变化
			// TODO 重新索引
			//
			this.logger.info(message);
		} else if (BaseConst.TaskFocus.SHOP.equals(taskFocus)) {
			// 店铺发生变化
			this.logger.info("*************************开始处理消息(shop)****************************");
			Integer shopId = (Integer) message.key;
			if (shopId != null) {
				searchService.indexShopDocById(shopId);
			}
			this.logger.info(message);
			this.logger.info("*************************处理消息(shop)完毕****************************");
		} else if (BaseConst.TaskFocus.DIST_SHOP.equals(taskFocus)) {
			// 卫星店铺发生变化
			this.logger.info("*************************开始处理消息(wxshop)****************************");
			Integer distShopId = (Integer) message.key;
			if (distShopId != null) {
				searchService.indexDistShopDocById(distShopId);
			}
			this.logger.info(message);
			this.logger.info("*************************处理消息(wxshop)完毕****************************");
		} else {
			this.logger.warn(message);
		}
		// ...
	}
}
