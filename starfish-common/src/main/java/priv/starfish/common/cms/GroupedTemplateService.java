package priv.starfish.common.cms;

import java.util.Map;

/**
 * 分组命名的模板服务接口
 * 
 * @author koqiui
 * @date 2015年5月29日 下午10:04:19
 * 
 * @param <T>
 */
public interface GroupedTemplateService<T> extends TemplateService<T> {

	String getDefaultTemplateGroup();

	T getTemplate(String templateGroup, String templateName);

	String renderContent(String templateGroup, String templateName, Map<String, Object> model);

}
