package priv.starfish.common.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import priv.starfish.common.config.PropertyConfigurer;
import priv.starfish.common.helper.FileHelper;
import priv.starfish.common.repo.FileRepository;
import priv.starfish.common.repo.FileRepositoryReferer;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Web环境辅助类
 * 
 * @date 2014-08-23 modified by koqiui - 提供了多系统自适应支持
 */
public class WebEnvHelper {
	private static Log logger = LogFactory.getLog(WebEnvHelper.class);

	// properties配置文件名称列表（多个可用逗号分隔）
	private static final String confPropertyFileNames = "cluster.properties,app-node.properties,conf/repo.properties,conf/xload.properties,conf/elastic.properties";
	private static final PropertyConfigurer propertyConfigurer;

	static {
		// 静态初始化
		propertyConfigurer = PropertyConfigurer.newInstance(confPropertyFileNames, "UTF-8");
	}

	//
	public static String getConfig(String key) {
		return propertyConfigurer.get(key);
	}

	//
	// Web 相关 ------------------------------------------------------------------
	private static ServletContext _servletContext;
	private static String _contextPath, _contextRealRootPath;

	public static void setServletContext(ServletContext servletContext) {
		_servletContext = servletContext;
		//
		_contextPath = _servletContext.getContextPath();
		logger.info(">> ContextPath : " + _contextPath);
		//
		_contextRealRootPath = _servletContext.getRealPath("");
		logger.info(">> ContextRealRootPath : " + _contextRealRootPath);
	}

	public static ServletContext getServletContext() {
		return _servletContext;
	}

	public static String getContextPath() {
		return _contextPath;
	}

	public static String getContextRealRootPath() {
		return _contextRealRootPath;
	}

	public static String getWebInfoPath() {
		return _contextRealRootPath + FileHelper.FILE_SEPERATOR + "WEB-INF";
	}

	public static String getWebClassesPath() {
		return _contextRealRootPath + FileHelper.FILE_SEPERATOR + "WEB-INF" + FileHelper.FILE_SEPERATOR + "classes";
	}

	//
	private static Map<String, String> _initParams = new HashMap<String, String>();

	public static void setInitParams(Map<String, String> initParams) {
		_initParams = initParams;
	}

	public static Map<String, String> getInitParams() {
		return _initParams;
	}

	public static String getInitParam(String paramName) {
		return _initParams.get(paramName);
	}

	//
	// Spring 相关 ------------------------------------------------------------------
	private static ApplicationContext _applicationContext;

	public static void setApplicationContext(ApplicationContext applicationContext) {
		_applicationContext = applicationContext;
	}

	public static ApplicationContext getApplicationContext() {
		return _applicationContext;
	}

	@SuppressWarnings("unchecked")
	public static <T> T getSpringBean(String beanName) {
		return (T) getApplicationContext().getBean(beanName);
	}

	public static <T> T getSpringBean(Class<T> beanClass) {
		T retBean = null;
		try {
			retBean = getApplicationContext().getBean(beanClass);
		} catch (NoSuchBeanDefinitionException ex) {
			logger.error(ex.getMessage());
		}
		return retBean;
	}

	public static <T> T getSpringBean(String beanName, Class<T> beanClass) {
		T retBean = null;
		try {
			retBean = getApplicationContext().getBean(beanName, beanClass);
		} catch (NoSuchBeanDefinitionException ex) {
			logger.error(ex.getMessage());
		}
		return retBean;
	}

	// 文件资源库 ------------------------------------------------------------------
	private static FileRepository _fileRepository;
	private static List<FileRepositoryReferer> _fileRepositoryReferers = new ArrayList<FileRepositoryReferer>();

	public static void addFileRepositoryReferer(FileRepositoryReferer fileRepositoryReferer) {
		if (fileRepositoryReferer == null) {
			return;
		}
		if (!_fileRepositoryReferers.contains(fileRepositoryReferer)) {
			_fileRepositoryReferers.add(fileRepositoryReferer);
			if (_fileRepository != null) {
				fileRepositoryReferer.setFileRepository(_fileRepository);
			}
		}
	}

	public static void setFileRepository(FileRepository fileRepository) {
		_fileRepository = fileRepository;
		//
		for (FileRepositoryReferer fileRepositoryReferer : _fileRepositoryReferers) {
			fileRepositoryReferer.setFileRepository(_fileRepository);
		}
	}

	public static FileRepository getFileRepository() {
		return _fileRepository;
	}
}
