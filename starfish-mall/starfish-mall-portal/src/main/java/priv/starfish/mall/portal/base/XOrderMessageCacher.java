/*
package priv.starfish.mall.portal.base;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import priv.starfish.common.base.AppNodeInfo;
import priv.starfish.common.cache.Cache;
import priv.starfish.common.cache.redis.RedisCache;
import priv.starfish.common.util.DateUtil;
import priv.starfish.mall.order.dict.OrderAction;
import priv.starfish.mall.order.dict.OrderType;
import priv.starfish.mall.order.dto.XOrderActionResult;
import priv.starfish.mall.service.BaseConst;
import priv.starfish.mall.service.misc.XOrderMessageProxy.MessageListener;

import java.util.Date;

*/
/**
 * 订单消息放到缓存中（便于拉取）
 * 
 * @author koqiui
 * @date 2016年2月1日 下午4:16:43
 *
 *//*

public class XOrderMessageCacher implements MessageListener {
	private final Log logger = LogFactory.getLog(this.getClass());

	private XOrderMessageCacher() {
		//
	}

	private static XOrderMessageCacher instance = new XOrderMessageCacher();

	public static XOrderMessageCacher getInstance() {
		return instance;
	}

	@Override
	public void onActionMessage(XOrderActionResult result, String source) {
		// 仅发送节点负责放到缓存（避免比必要的重复放置）
		if (AppNodeInfo.getCurrent().getName().equals(source)) {
			// 放到缓存中（目前仅需拉取支付结果）
			if (OrderAction.pay.equals(result.orderAction)) {
				this.setOrderActionResult(result);
			}
		}
	}

	private String makeCacheKey(OrderType orderType, String orderNo, OrderAction orderAction) {
		return orderType.name() + "+" + orderNo + "+" + orderAction.name();
	}

	private void setOrderActionResult(XOrderActionResult result) {
		String cacheKey = makeCacheKey(result.orderType, result.orderNo, result.orderAction);
		//
		Cache<String, Object> cache = AppBase.getXOrderCache();
		if (cache == null) {
			return;
		}
		cache.put(cacheKey, result);
		// 设置过期时间
		if (cache instanceof RedisCache) {
			((RedisCache<String, Object>) cache).expireAt(cacheKey, DateUtil.addMinutes(new Date(), BaseConst.XORDER_ACTION_EXPIRE_TIME_IN_MINUTES));
		}
		//
		this.logger.debug("订单action消息已放置到 redis中");
	}

	public XOrderActionResult getOrderActionResult(OrderType orderType, String orderNo, OrderAction orderAction) {
		String cacheKey = makeCacheKey(orderType, orderNo, orderAction);
		//
		Cache<String, Object> cache = AppBase.getXOrderCache();
		if (cache == null) {
			return null;
		}
		//
		this.logger.debug("从redis获取订单action消息");
		//
		return (XOrderActionResult) cache.get(cacheKey);
	}
}
*/
