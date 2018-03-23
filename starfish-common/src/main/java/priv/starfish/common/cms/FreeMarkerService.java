package priv.starfish.common.cms;

import java.util.Map;

import freemarker.template.Template;

/**
 * (freemarker版)分组命名的模板服务接口
 * 
 * @author koqiui
 * @date 2015年5月29日 下午10:05:34
 *
 */
public interface FreeMarkerService extends GroupedTemplateService<Template> {

	String getDefaultTemplateGroup();

	Template getTemplate(String templateName);

	Template getTemplate(String templateGroup, String templateName);

	String renderContent(Template template, Map<String, Object> model);

	String renderContent(String templateName, Map<String, Object> model);

	String renderContent(String templateGroup, String templateName, Map<String, Object> model);

}
