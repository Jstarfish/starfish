package priv.starfish.mall.notify.dict;

import priv.starfish.common.annotation.AsEnumVar;
import priv.starfish.common.annotation.AsSelectList;

/**
 * 邮件用途
 * 
 * @author guoyn
 * @date 2015年11月13日 下午3:54:56
 *
 */
@AsSelectList(name = "mailUsage")
@AsEnumVar()
public enum MailUsage {
	normal("一般"), //
	regist("注册"), logPass("找回登录密码"), //
	saleOrder("销售订单"), ecardOrder("e卡订单"), //
	rebind("手机重新绑定"), ecardTransfer("e卡转赠"), //
	promote("促销通知"), security("安全通知"), //
	apply("审核店铺"), other("其他");

	private String text;

	public String getText() {
		return text;
	}

	private MailUsage(String text) {
		this.text = text;
	}
}
