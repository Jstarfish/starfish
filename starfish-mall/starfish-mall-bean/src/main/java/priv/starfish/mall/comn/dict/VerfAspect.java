package priv.starfish.mall.comn.dict;

import priv.starfish.common.annotation.AsEnumVar;
import priv.starfish.common.annotation.AsSelectList;

/**
 * 用户验证状态aspect枚举类
 * 
 * @author koqiui
 * @date 2016年3月5日 上午1:50:12
 *
 */
@AsSelectList(name = "verfAspect")
@AsEnumVar()
public enum VerfAspect {

	phoneNo("手机号码"), email("邮箱"), idCertNo("身份证号码"), realName("真实姓名"), birthDate("出生日期"), qq("QQ号码"), logPass("登录密码"), payPass("支付密码");

	private String text;

	public String getText() {
		return text;
	}

	private VerfAspect(String text) {
		this.text = text;
	}
}
