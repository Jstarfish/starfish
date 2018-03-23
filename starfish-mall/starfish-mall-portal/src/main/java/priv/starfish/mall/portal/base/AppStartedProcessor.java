package priv.starfish.mall.portal.base;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import priv.starfish.mall.web.base.AbsStartedProcessor;
import priv.starfish.mall.web.base.AppBase;
import priv.starfish.mall.web.base.CartHelper;

/**
 * Spring应用启动完毕后执行的代码
 * 
 * @author koqiui
 * @date 2015年5月23日 下午10:03:18
 *
 */
@Component
public class AppStartedProcessor extends AbsStartedProcessor {

	// 具体要执行的代码
	protected void whenSpecificAppStarted(ApplicationContext applicationContext) {
		AppBase.loadMobileAppsInfo();
		//
		CartHelper.initRefObjects();
	}

}
