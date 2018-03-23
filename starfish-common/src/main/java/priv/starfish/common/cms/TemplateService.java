package priv.starfish.common.cms;

import java.util.Map;

/**
 * 简单模板服务接口
 * 
 * @author koqiui
 * @date 2015年5月29日 下午10:04:48
 * 
 * @param <T>
 */
public interface TemplateService<T> {

	T getTemplate(String templateName);

	String renderContent(T template, Map<String, Object> model);

	String renderContent(String templateName, Map<String, Object> model);

}
