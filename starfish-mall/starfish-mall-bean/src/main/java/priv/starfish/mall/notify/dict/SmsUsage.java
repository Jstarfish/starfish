package priv.starfish.mall.notify.dict;

import priv.starfish.common.annotation.AsEnumVar;
import priv.starfish.common.annotation.AsSelectList;

/**
 * 短信用途
 * 
 * @author 毛智东
 * @date 2015年7月3日 下午3:21:58
 *
 */
@AsSelectList(name = "smsUsage")
@AsEnumVar()
public enum SmsUsage {
	normal("一般"), //
	regist("注册"), logPass("找回登录密码"), //
	rebind("手机重新绑定"), ecardTransfer("e卡转赠"), //
	pay("支付"), payPass("找回支付密码"), //
	saleOrder("销售订单"), ecardOrder("e卡订单"), //
	promote("促销通知"), security("安全通知"), //
	apply("审核店铺"), other("其他");

	private String text;

	public String getText() {
		return text;
	}

	private SmsUsage(String text) {
		this.text = text;
	}
}
