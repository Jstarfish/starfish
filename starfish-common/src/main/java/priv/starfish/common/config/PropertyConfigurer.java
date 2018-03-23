package priv.starfish.common.config;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import priv.starfish.common.util.StrUtil;


/**
 * 从Properties 文件（列表）中获取属性配置并缓存起来
 * 
 * @author koqiui
 * @date 2014-08-19
 */
public class PropertyConfigurer {
	private static final String fileSuffix = ".properties";
	private static final String defaultCharsetName = "UTF-8";
	//
	private final Log logger = LogFactory.getLog(PropertyConfigurer.class);

	private String[] parseFilenames(String fileNamesStr) {
		List<String> fileNameList = new ArrayList<String>();
		String[] fileNames = StrUtil.hasText(fileNamesStr) ? fileNamesStr.split(",") : null;
		if (fileNames != null) {
			for (int i = 0; i < fileNames.length; i++) {
				String fileName = fileNames[i].trim();
				if (!fileName.endsWith(fileSuffix)) {
					fileName += fileSuffix;
				}
				if (!fileNameList.contains(fileName)) {
					fileNameList.add(fileName);
				}
			}
		}
		return fileNameList.toArray(new String[fileNameList.size()]);
	}

	Map<String, String> innerMap = new ConcurrentHashMap<String, String>();

	private ReentrantLock XLock = new ReentrantLock();

	@SuppressWarnings("rawtypes")
	private void config(String[] fileNames, String charsetName) {
		XLock.lock();
		//
		innerMap.clear();
		if (charsetName == null) {
			charsetName = defaultCharsetName;
		}
		for (int i = 0, j = fileNames.length; i < j; i++) {
			try {
				Class refClass = PropertyConfigurer.class;
				String fileName = fileNames[i];
				InputStream input = null;
				if ((input = refClass.getResourceAsStream(fileName)) == null) {
					ClassLoader clsLoader = refClass.getClassLoader();
					while (clsLoader != null && (input = clsLoader.getResourceAsStream(fileName)) == null) {
						clsLoader = clsLoader.getParent();
					}
					if (clsLoader == null) {
						continue;
					}
				}
				if (input != null) {
					Properties properties = new Properties();
					BufferedReader inputReader = new BufferedReader(new InputStreamReader(input, charsetName));
					properties.load(inputReader);
					Enumeration<Object> keys = properties.keys();
					while (keys.hasMoreElements()) {
						String key = (String) keys.nextElement();
						String value = properties.getProperty(key);
						innerMap.put(key, value);
					}
					logger.debug("finished reading from file \"" + fileName + "\"");
				}
			} catch (IOException e) {
				logger.error(e);
			}
		}
		XLock.unlock();
	}

	public PropertyConfigurer(String[] fileNames, String charsetName) {
		this.config(fileNames, charsetName);
	}

	public PropertyConfigurer(String[] fileNames) {
		this(fileNames, null);
	}

	public PropertyConfigurer(String fileNamesStr, String charsetName) {
		String[] fileNames = parseFilenames(fileNamesStr);
		this.config(fileNames, charsetName);
	}

	public PropertyConfigurer(String fileNamesStr) {
		this(fileNamesStr, null);
	}

	public int size() {
		return this.innerMap.size();
	}

	public Set<String> keySet() {
		return this.innerMap.keySet();
	}

	public String get(String key) {
		return this.innerMap.get(key);
	}

	public String put(String key, String value) {
		return this.innerMap.put(key, value);
	}

	public boolean containsKey(String key) {
		return this.innerMap.containsKey(key);
	}

	public boolean containsValue(String value) {
		return this.innerMap.containsValue(value);
	}

	public String toString() {
		return this.innerMap.toString();
	}

	public static PropertyConfigurer newInstance(String fileNamesStr, String charsetName) {
		return new PropertyConfigurer(fileNamesStr, charsetName);
	}

	public static PropertyConfigurer newInstance(String fileNamesStr) {
		return new PropertyConfigurer(fileNamesStr);
	}

	public static PropertyConfigurer newInstance(String[] fileNames, String charsetName) {
		return new PropertyConfigurer(fileNames, charsetName);
	}

	public static PropertyConfigurer newInstance(String[] fileNames) {
		return new PropertyConfigurer(fileNames);
	}
}
