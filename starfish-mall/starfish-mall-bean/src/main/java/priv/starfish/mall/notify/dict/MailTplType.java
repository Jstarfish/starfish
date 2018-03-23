package priv.starfish.mall.notify.dict;

import priv.starfish.common.annotation.AsEnumVar;
import priv.starfish.common.annotation.AsSelectList;

/**
 * 邮件模板类型
 * 
 * @author 毛智东
 * @date 2015年5月21日 下午6:39:07
 *
 */
@AsSelectList(name = "mailTplType")
@AsEnumVar()
public enum MailTplType {

	txt("纯文本邮件"), html("HTML邮件");

	private String text;

	public String getText() {
		return text;
	}

	private MailTplType(String text) {
		this.text = text;
	}
}
