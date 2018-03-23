package priv.starfish.mall.notify.dict;

import priv.starfish.common.annotation.AsEnumVar;
import priv.starfish.common.annotation.AsSelectList;

/**
 * 邮箱类型枚举类
 * 
 * @author 毛智东
 * @date 2015年5月19日 上午10:03:38
 *
 */
@AsSelectList(name = "mailType")
@AsEnumVar()
public enum MailType {

	smtp("SMTP"), imap("IMAP"), exchange("Exchange");

	private String text;

	public String getText() {
		return text;
	}

	private MailType(String text) {
		this.text = text;
	}
}
