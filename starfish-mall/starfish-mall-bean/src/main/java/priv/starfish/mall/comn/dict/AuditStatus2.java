package priv.starfish.mall.comn.dict;

import priv.starfish.common.annotation.AsEnumVar;
import priv.starfish.common.annotation.AsSelectList;

/**
 * 2步审核状态 value()取数值，name()取英文标识，text()取出中文含义
 * 
 * @author 邓华锋
 * @date 2016年1月11日 上午10:43:48
 *
 */
@AsSelectList(name = "auditStatus2")
@AsEnumVar(valueField = "value")
public enum AuditStatus2 {
	unAuditted("未审核", 0), //
	initAuditOk("初审通过", 1), initAuditFail("初审未通过", -1), //
	auditOk("审核通过", 2), auditFail("审核未通过", -2);

	private String text;

	private int value;

	private AuditStatus2(String text, int value) {
		this.text = text;
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public int getValue() {
		return value;
	}
}
