package priv.starfish.mall.web.base;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;

import priv.starfish.mall.service.misc.XOrderMessageProxy;
import priv.starfish.mall.web.misc.XOrderMessageCacher;

/**
 * Spring应用即将关闭前执行代码
 * 
 * @author koqiui
 * @date 2015年5月23日 下午10:03:18
 *
 */
public abstract class AbsClosedProcessor implements ApplicationListener<ContextClosedEvent> {
	protected Log logger = LogFactory.getLog(this.getClass());

	@Override
	public void onApplicationEvent(ContextClosedEvent event) {
		System.out.println(">> Spring应用即将关闭");
		//
		this.doBeforeAppClosed();
		// 停止对配置文件变更的监视
		AppBase.stopWatchConfFiles();
		//
		// 订单action消息处理
		XOrderMessageProxy xOrderMessageProxy = XOrderMessageProxy.getInstance();
		// 为客户端拉消息提供的redis缓存处理监听器对象
		xOrderMessageProxy.removeMessageListener(XOrderMessageCacher.getInstance());
	}

	// 具体要执行的代码
	protected abstract void doBeforeAppClosed();
}
