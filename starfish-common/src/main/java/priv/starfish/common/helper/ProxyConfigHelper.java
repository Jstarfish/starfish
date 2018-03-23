package priv.starfish.common.helper;

import java.io.IOException;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.mysql.jdbc.StringUtils;

/**
 * ProxyConfigSetter <br/>
 * 设置http 和 socket 代理服务器 by Hu Changwei, 2013.05.27
 */
public final class ProxyConfigHelper {
	private static final Log logger = LogFactory.getLog(ProxyConfigHelper.class);

	//
	private ProxyConfigHelper() {
	}

	private final static String defaultConfigFile = "conf/proxy.properties";

	public static boolean config() {
		return config(null);
	}

	public static boolean config(String configFile) {
		try {
			if (StringUtils.isNullOrEmpty(configFile)) {
				configFile = defaultConfigFile;
			}
			Properties proxyConfig = new Properties();
			proxyConfig.load(ProxyConfigHelper.class.getClassLoader().getResourceAsStream(configFile));
			// http proxy
			boolean httpProxySet = Boolean.parseBoolean(proxyConfig.getProperty("httpProxySet", "false"));
			if (httpProxySet) {
				System.getProperties().put("httpProxySet", true);
				System.getProperties().put("httpProxyHost", proxyConfig.getProperty("httpProxyHost"));
				System.getProperties().put("httpProxyPort", Integer.parseInt(proxyConfig.getProperty("httpProxyPort")));
			}
			// socket proxy
			boolean socksProxySet = Boolean.parseBoolean(proxyConfig.getProperty("socksProxySet", "false"));
			if (socksProxySet) {
				System.getProperties().put("socksProxySet", true);
				System.getProperties().put("socksProxyHost", proxyConfig.getProperty("socksProxyHost"));
				System.getProperties().put("socksProxyPort", Integer.parseInt(proxyConfig.getProperty("socksProxyPort")));
			}
			// ftp proxy
			boolean ftpProxySet = Boolean.parseBoolean(proxyConfig.getProperty("ftpProxySet", "false"));
			if (ftpProxySet) {
				System.getProperties().put("ftpProxySet", true);
				System.getProperties().put("ftpProxyHost", proxyConfig.getProperty("ftpProxyHost"));
				System.getProperties().put("ftpProxyPort", Integer.parseInt(proxyConfig.getProperty("ftpProxyPort")));
			}
			logger.debug("ProxyConfigHelper : config finished.");
			return true;
		} catch (IOException e) {
			logger.error("ProxyConfigHelper : " + e.getMessage());
			return false;
		}
	}
}
