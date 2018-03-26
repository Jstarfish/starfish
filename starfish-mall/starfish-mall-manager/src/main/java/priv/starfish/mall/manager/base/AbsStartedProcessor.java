/*
package priv.starfish.mall.manager.base;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import priv.starfish.common.base.AppNodeInfo;
import priv.starfish.common.base.IRefreshable;
import priv.starfish.common.cache.CacheManager;
import priv.starfish.common.helper.AppHelper;
import priv.starfish.common.jms.SimpleMessageSender;
import priv.starfish.common.repo.FileRepoConfig;
import priv.starfish.common.repo.FileRepoConfig.Usage;
import priv.starfish.common.repo.FileRepository;
import priv.starfish.common.tool.MySqlDbTool;
import priv.starfish.common.util.FileSize;
import priv.starfish.common.util.OSUtil;
import priv.starfish.common.util.StrUtil;
import priv.starfish.common.web.WebEnvHelper;
import priv.starfish.common.xload.FileUploader;
import priv.starfish.common.xload.UploadConfig;
import priv.starfish.mall.comn.misc.BizParamInfo;
import priv.starfish.mall.comn.misc.SysParamInfo;
import priv.starfish.mall.service.SearchService;
import priv.starfish.mall.service.SettingService;

import java.io.IOException;

*/
/**
 * Spring应用启动完毕后执行的代码
 * 
 * @author koqiui
 * @date 2015年5月23日 下午10:03:18
 *
 *//*

@Component
public abstract class AbsStartedProcessor implements ApplicationListener<ContextRefreshedEvent> {
	protected Log logger = LogFactory.getLog(this.getClass());

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ApplicationContext applicationContext = event.getApplicationContext();
		if (applicationContext.getParent() instanceof WebApplicationContext) {
			// mvc加载完毕
			System.out.println(">> Spring应用启动完毕");
			//
			this.prevSpecificAppStarted(applicationContext);
			//
			this.whenSpecificAppStarted(applicationContext);
			//TODO 待处理：搜索问题
			//this.postSpecificAppStarted(applicationContext);
		}
	}

	*/
/** 所有应用要执行的代码 *//*

	protected void prevSpecificAppStarted(ApplicationContext applicationContext) {
		//
		WebEnvHelper.setApplicationContext(applicationContext);
		// 缓存管理器
		CacheManager cacheManager = applicationContext.getBean(CacheManager.class);
		AppHelper.setCacheManager(cacheManager);
		//
		SimpleMessageSender simpleMessageSender = WebEnvHelper.getSpringBean(SimpleMessageSender.class);
		AppHelper.setSimpleMessageSender(simpleMessageSender);
		//
		if (!AppNodeInfo.getCurrent().isRunningOnProductionServer()) {
			// 非生产环境下启动时自动同步数据库表定义
			MySqlDbTool.initDbObjects();
		}
		// 应用集群节点消息
		AppNodeInfo.getCurrent().setSimpleMessageSender(simpleMessageSender);
		AppNodeInfo.getCurrent().startClusterHeartbeating();

		// 订单action消息处理
		//TODO 订单消息待处理
		*/
/*
		XOrderMessageProxy xOrderMessageProxy = XOrderMessageProxy.getInstance();
		xOrderMessageProxy.setMessageTopicName(BaseConst.TopicNames.XORDER);
		xOrderMessageProxy.setSimpleMessageSender(simpleMessageSender);
		// 为客户端拉消息提供的redis缓存处理监听器对象
		xOrderMessageProxy.addMessageListener(XOrderMessageCacher.getInstance());
		*//*

		// ---------------------------------------------------------

		// 把文件上传配置更新到spring中

		FileUploader fileUploader = FileUploader.getInstance();
		CommonsMultipartResolver multipartResolver = WebEnvHelper.getSpringBean(CommonsMultipartResolver.class);
		if (multipartResolver != null) {
			UploadConfig uploadConfig = fileUploader.getUploadConfig();
			try {
				multipartResolver.setUploadTempDir(new FileSystemResource(uploadConfig.getTmpFileDir().getAbsolutePath()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			multipartResolver.setMaxInMemorySize(uploadConfig.getToDiskThreshold() * FileSize.K);
			multipartResolver.setMaxUploadSize(uploadConfig.getTotalSizeLimit() * FileSize.K);
		}

		// ---------------------------------------------------------
		// 文件资源库(实例)
		FileRepository fileRepository = WebEnvHelper.getSpringBean(FileRepository.class);
		if (fileRepository != null) {
			// 缓存实例
			WebEnvHelper.setFileRepository(fileRepository);
			// 文件资源库配置
			FileRepoConfig fileRepoConfig = new FileRepoConfig();
			//
			String tmpKey, tmpValue;
			tmpKey = FileRepoConfig.KEY_ROOT_URL;
			tmpValue = WebEnvHelper.getConfig(tmpKey);
			if (StrUtil.isNullOrBlank(tmpValue)) {
				System.out.println(">> 未设置 " + tmpKey + " ，将使用默认配置");
				tmpValue = AppNodeInfo.getCurrent().getAppBaseUrl();
			}
			fileRepoConfig.setRootUrl(tmpValue);
			tmpKey = FileRepoConfig.KEY_NO_IMAGE_URL;
			tmpValue = WebEnvHelper.getConfig(tmpKey);
			fileRepoConfig.setNoImageUrl(tmpValue);
			tmpKey = FileRepoConfig.KEY_DELETE_URL;
			tmpValue = WebEnvHelper.getConfig(tmpKey);
			fileRepoConfig.setDeleteUrl(tmpValue);
			//
			String osSuffix = OSUtil.getOsSuffix();
			for (String usage : Usage.all) {
				tmpKey = FileRepoConfig.CONFIG_PREFIX + usage + FileRepoConfig.SUFFIX_BASE_DIR + osSuffix;
				tmpValue = WebEnvHelper.getConfig(tmpKey);
				fileRepoConfig.setBaseDir(usage, tmpValue);
				tmpKey = FileRepoConfig.CONFIG_PREFIX + usage + FileRepoConfig.SUFFIX_BASE_URL;
				tmpValue = WebEnvHelper.getConfig(tmpKey);
				fileRepoConfig.setBaseUrl(usage, tmpValue);
			}
			fileRepository.setFileRepoConfig(fileRepoConfig);
		}
		//
		SettingService settingService = WebEnvHelper.getSpringBean(SettingService.class);
		// 同步系统/业务参数到静态变量
		SysParamInfo.fromParams(settingService.getAllSysParams());
		BizParamInfo.fromParams(settingService.getBizParams());
		//
		// 初始化freeMarkerService 所需的模板内容
		IRefreshable freeMarkerService = WebEnvHelper.getSpringBean("freeMarkerService", IRefreshable.class);
		freeMarkerService.refresh();

		// 初始化mailService 所需的服务器信息
		IRefreshable mailService = WebEnvHelper.getSpringBean("mailService", IRefreshable.class);
		mailService.refresh();

		// 初始化sms所需api配置信息
		IRefreshable smsService = WebEnvHelper.getSpringBean("smsService", IRefreshable.class);
		smsService.refresh();

		// 初始化elasticsearch
		//TODO 待解决 有问题
		*/
/*IRefreshable searchService = WebEnvHelper.getSpringBean("searchService", IRefreshable.class);
		searchService.refresh();*//*

	}

	*/
/** 具体应用要执行的代码 *//*

	protected abstract void whenSpecificAppStarted(ApplicationContext applicationContext);


	*/
/** 所有应用要执行的代码 *//*

	protected void postSpecificAppStarted(ApplicationContext applicationContext) {
		// 初始化elasticsearch
		SearchService searchService = WebEnvHelper.getSpringBean("searchService", SearchService.class);
		searchService.echoSearcherInfo();
		//
		// 开始对配置文件变更的监视
		AppBase.startWatchConfFiles();
	}
}
*/
