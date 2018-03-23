package priv.starfish.common.cms;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;


import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import priv.starfish.common.util.StrUtil;

/**
 * 基于字符串内容的分组命名的模板服务实现
 * 
 * @author koqiui
 * @date 2015年5月29日 下午5:28:47
 *
 */
public class StringFreeMarkerServiceImpl implements FreeMarkerService {
	protected final Log logger = LogFactory.getLog(this.getClass());

	//
	public static Configuration generateDefaultConfiguration() {
		Configuration configuration = new Configuration();
		configuration.setClassicCompatible(true);
		configuration.setDefaultEncoding("UTF-8");
		configuration.setOutputEncoding("UTF-8");
		configuration.setDateFormat("yyyy-MM-dd");
		configuration.setTimeFormat("HH:mm:ss");
		configuration.setDateTimeFormat("yyyy-MM-dd HH:mm:ss");
		configuration.setNumberFormat("0.##");
		configuration.setURLEscapingCharset("UTF-8");
		configuration.setLocale(Locale.CHINESE);
		//
		StringTemplateLoader templateLoader = new StringTemplateLoader();
		configuration.setTemplateLoader(templateLoader);
		//
		return configuration;
	}

	private Configuration freemarkerConfig;
	private StringTemplateLoader templateLoader;

	public StringFreeMarkerServiceImpl() {
		this.freemarkerConfig = generateDefaultConfiguration();
		this.templateLoader = (StringTemplateLoader) this.freemarkerConfig.getTemplateLoader();
	}

	public void setFreemarkerConfig(Configuration freemarkerConfig) {
		this.freemarkerConfig = freemarkerConfig;
		this.templateLoader = (StringTemplateLoader) this.freemarkerConfig.getTemplateLoader();
	}

	private String defaultTemplateGroup = StrUtil.EmptyStr;

	public void setDefaultTemplateGroup(String defaultTemplateGroup) {
		this.defaultTemplateGroup = defaultTemplateGroup;
	}

	private String makeFullTemplateName(String templateGroup, String templateName) {
		if (templateGroup == null) {
			templateGroup = StrUtil.EmptyStr;
		}
		return StrUtil.EmptyStr.equals(templateGroup) ? templateName : templateGroup + "/" + templateName;
	}

	public void setTemplateContent(String templateGroup, String templateName, String templateContent) {
		this.templateLoader.putTemplate(this.makeFullTemplateName(templateGroup, templateName), templateContent);
	}

	public void setTemplateContent(String templateName, String templateContent) {
		this.setTemplateContent(this.defaultTemplateGroup, templateName, templateContent);
	}

	@Override
	public String getDefaultTemplateGroup() {
		return defaultTemplateGroup;
	}

	@Override
	public Template getTemplate(String templateName) {
		return this.getTemplate(this.defaultTemplateGroup, templateName);
	}

	@Override
	public Template getTemplate(String templateGroup, String templateName) {
		try {
			return freemarkerConfig.getTemplate(this.makeFullTemplateName(templateGroup, templateName));
		} catch (IOException e) {
			logger.error(e);
			return null;
		}
	}

	@Override
	public String renderContent(Template template, Map<String, Object> model) {
		try {
			return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	@Override
	public String renderContent(String templateName, Map<String, Object> model) {
		Template template = this.getTemplate(templateName);
		return this.renderContent(template, model);
	}

	@Override
	public String renderContent(String templateGroup, String templateName, Map<String, Object> model) {
		Template template = this.getTemplate(templateGroup, templateName);
		return this.renderContent(template, model);
	}

}
