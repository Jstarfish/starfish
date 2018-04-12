package priv.starfish.mall.manager.base;

import org.springframework.stereotype.Component;
import priv.starfish.common.base.AppNodeInfo;
import priv.starfish.mall.web.base.AbsClosedProcessor;

/**
 * Spring应用即将关闭前执行代码
 * 
 * @author koqiui
 * @date 2015年5月23日 下午10:03:18
 *
 */
@Component
public class AppClosedProcessor extends AbsClosedProcessor {

	// 具体要执行的代码
	protected void doBeforeAppClosed() {
		AppNodeInfo.getCurrent().stopClusterHeartbeating();
		// ...
	}

}
