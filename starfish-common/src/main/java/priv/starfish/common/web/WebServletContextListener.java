package priv.starfish.common.web;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import priv.starfish.common.base.AppNodeInfo;
import priv.starfish.common.base.ClusterInfo;
import priv.starfish.common.helper.ProxyConfigHelper;
import priv.starfish.common.util.BoolUtil;
import priv.starfish.common.util.NumUtil;
import priv.starfish.common.util.OSUtil;
import priv.starfish.common.xload.*;

public class WebServletContextListener implements ServletContextListener {
	protected final Log logger = LogFactory.getLog(getClass());
	//
	private static final String InitParamName_UseProxyHost = "useProxyHost";
	private static final String InitParamName_ProxyConfigFile = "proxyConfigFile";

	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		// 先设置此至关重要
		WebEnvHelper.setServletContext(context);
		// 1
		// 获取Web应用上下文的初始参数，并保存到启动参数列表里。
		Map<String, String> initParams = new HashMap<String, String>();
		String paramName, paramValue;
		Enumeration<String> ctxInitParams = context.getInitParameterNames();
		while (ctxInitParams.hasMoreElements()) {
			paramName = ctxInitParams.nextElement();
			paramValue = context.getInitParameter(paramName);
			initParams.put(paramName, paramValue);
		}
		WebEnvHelper.setInitParams(initParams);

		// 代理服务器设置
		if (initParams.containsKey(InitParamName_UseProxyHost)) {
			if (Boolean.parseBoolean(initParams.get(InitParamName_UseProxyHost))) {
				String proxyConfigFile = initParams.get(InitParamName_ProxyConfigFile);
				ProxyConfigHelper.config(proxyConfigFile);
			}
		}

		// 2
		String tmpKey, tmpValue;
		// 集群配置(默认的单例)
		ClusterInfo clusterInfo = ClusterInfo.getCurrent();

		tmpKey = ClusterInfo.KEY_PRODUCTION_SERVER_IPS;
		tmpValue = WebEnvHelper.getConfig(tmpKey);
		clusterInfo.setProductionServerIps(tmpValue);

		tmpKey = ClusterInfo.KEY_HEARTBEAT_INTERVAL;
		tmpValue = WebEnvHelper.getConfig(tmpKey);
		Integer tmpInt = NumUtil.parseInteger(tmpValue);
		if (tmpInt != null) {
			clusterInfo.setClusterHeartbeatInterval(tmpInt.intValue());
		}

		tmpKey = ClusterInfo.KEY_MESSAGE_TOPIC_NAME;
		tmpValue = WebEnvHelper.getConfig(tmpKey);
		clusterInfo.setMessageTopicName(tmpValue);

		// 应用节点配置(默认的单例)
		AppNodeInfo appNodeInfo = AppNodeInfo.getCurrent();

		tmpKey = AppNodeInfo.KEY_ROLE;
		tmpValue = WebEnvHelper.getConfig(tmpKey);
		appNodeInfo.setRole(tmpValue);

		tmpKey = AppNodeInfo.KEY_NAME;
		tmpValue = WebEnvHelper.getConfig(tmpKey);
		appNodeInfo.setName(tmpValue);

		tmpKey = AppNodeInfo.KEY_MASTER;
		tmpValue = WebEnvHelper.getConfig(tmpKey);
		if (BoolUtil.isTrue(tmpValue)) {
			appNodeInfo.setMaster(true);
		}

		tmpKey = AppNodeInfo.KEY_SERVER_BASE;
		tmpValue = WebEnvHelper.getConfig(tmpKey);
		appNodeInfo.setServerBase(tmpValue);

		appNodeInfo.setAppBaseUrl(WebEnvHelper.getContextPath());

		tmpKey = AppNodeInfo.KEY_RES_BASE_URL;
		tmpValue = WebEnvHelper.getConfig(tmpKey);
		appNodeInfo.setResBaseUrl(tmpValue);

		appNodeInfo.setProductionServerIps(clusterInfo.getProductionServerIps());

		// 3 上传下载设置
		UploadConfig uploadConfig = new UploadConfig();
		tmpKey = UploadConfig.KEY_TMP_FILE_DIR + OSUtil.getOsSuffix();
		tmpValue = WebEnvHelper.getConfig(tmpKey);
		uploadConfig.setTmpFileDir(tmpValue);

		tmpKey = UploadConfig.KEY_TOTAL_SIZE_LIMIT;
		tmpValue = WebEnvHelper.getConfig(tmpKey);
		uploadConfig.setTotalSizeLimit(tmpValue);

		tmpKey = UploadConfig.KEY_FILE_SIZE_LIMIT;
		tmpValue = WebEnvHelper.getConfig(tmpKey);
		uploadConfig.setFileSizeLimit(tmpValue);

		tmpKey = UploadConfig.KEY_TO_DISK_THRESHOLD;
		tmpValue = WebEnvHelper.getConfig(tmpKey);
		uploadConfig.setToDiskThreshold(tmpValue);

		tmpKey = UploadConfig.KEY_ALLOWED_FILE_EXTS;
		tmpValue = WebEnvHelper.getConfig(tmpKey);
		uploadConfig.setAllowedFileExts(tmpValue);

		// 文件接收者管理器
		FileReceiverMgr fileReceiverMgr = new FileReceiverMgr();
		fileReceiverMgr.setName("默认文件接收者管理器");
		tmpKey = FileReceiver.CONFIG_PREFIX + "class.names";
		tmpValue = WebEnvHelper.getConfig(tmpKey);
		if (tmpValue != null) {
			fileReceiverMgr.setReceiverClassNames(tmpValue.split(",", -1));
		}

		// 文件上传代理对象(默认的单例)
		FileUploader fileUploader = FileUploader.getInstance();
		fileUploader.setUploadConfig(uploadConfig);
		fileUploader.setFileReceiverMgr(fileReceiverMgr);

		// 文件提供者管理器
		FileProviderMgr fileProviderMgr = new FileProviderMgr();
		fileProviderMgr.setName("默认文件提供者管理器");
		tmpKey = FileProvider.CONFIG_PREFIX + "class.names";
		tmpValue = WebEnvHelper.getConfig(tmpKey);
		if (tmpValue != null) {
			fileProviderMgr.setProviderClassNames(tmpValue.split(",", -1));
		}

		// 文件下载代理对象（默认的单例）
		FileDownloader fileDownloader = FileDownloader.getInstance();
		fileDownloader.setFileProviderMgr(fileProviderMgr);
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {

	}
}
