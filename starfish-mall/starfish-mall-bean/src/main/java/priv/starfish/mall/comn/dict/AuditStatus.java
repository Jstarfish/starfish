package priv.starfish.mall.comn.dict;

import priv.starfish.common.annotation.AsEnumVar;
import priv.starfish.common.annotation.AsSelectList;

/**
 * 一步审核状态
 * 
 * @author 邓华锋
 * @date 2016年1月11日 上午10:43:48
 *
 */
@AsSelectList(name = "auditStatus")
@AsEnumVar(valueField = "value")
public enum AuditStatus {
	unAuditted("未审核", 0), auditOk("审核通过", 1), auditFail("审核未通过", -1);

	private String text;

	private int value;

	private AuditStatus(String text, int value) {
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
