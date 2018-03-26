package priv.starfish.mall.manager.base;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import priv.starfish.common.http.AppUrlPool;
import priv.starfish.mall.web.base.AbsStartedProcessor;
import priv.starfish.mall.web.base.CacheHelper;

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
		// 预热资源
		CacheHelper.getProtectedUrlResources();
		// 加载银企直连支付代理服务应用url列表
		AppUrlPool bedirectAppUrlPool = AppUrlPool.getInstance();
		bedirectAppUrlPool.loadFromConfigFile("conf/bedirect-app-urls.properties", "bedirect.app.urls");
		bedirectAppUrlPool.echo();
		//
	}

}
