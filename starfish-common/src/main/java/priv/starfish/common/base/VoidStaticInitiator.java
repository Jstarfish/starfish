package priv.starfish.common.base;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import priv.starfish.common.util.TypeUtil;

import java.util.ArrayList;
import java.util.List;


/**
 * 无参静态方法调用器
 *
 */
public class VoidStaticInitiator {
	private transient final Log logger = LogFactory.getLog(this.getClass());
	//
	private List<String> staticMethods = new ArrayList<String>();

	//
	public void setStaticMethods(List<String> staticMethods) {
		this.staticMethods = staticMethods;
	}

	public void init() {
		for (String staticMethod : staticMethods) {
			String[] pair = staticMethod.split("::");
			if (pair.length != 2) {
				logger.warn("静态方法格式应为： 类名::方法名 ，已忽略：" + staticMethod);
				continue;
			}
			String className = pair[0].trim();
			if (className.startsWith("-")) {
				continue;
			}
			String methodName = pair[1].trim();
			try {
				Class<?> clazz = Class.forName(className);
				TypeUtil.invokeStaticMethod(clazz, methodName);
			} catch (Exception e) {
				logger.error("静态方法调用出错：" + staticMethod + " >> " + e);
			}
		}
	}
}
