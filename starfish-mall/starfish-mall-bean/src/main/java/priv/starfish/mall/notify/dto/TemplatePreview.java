package priv.starfish.mall.notify.dto;

import priv.starfish.mall.notify.dict.MailTplType;

/**
 * 模板预览
 * 
 * @author 毛智东
 * @date 2015年6月23日 下午6:09:01
 *
 */
public class TemplatePreview {
	/** 模板内容 */
	private String content;

	/** 模板类型sample */
	private String sample;

	/** 模板类型 */
	private MailTplType type;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSample() {
		return sample;
	}

	public void setSample(String sample) {
		this.sample = sample;
	}

	public MailTplType getType() {
		return type;
	}

	public void setType(MailTplType type) {
		this.type = type;
	}

}
