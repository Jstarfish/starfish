package priv.starfish.mall.logistic.dict;

import java.util.HashMap;
import java.util.Map;

import priv.starfish.common.annotation.AsEnumVar;

@AsEnumVar(valueField = "value")
public enum LogisErrorCode {

	/**
	 * 验证不通过
	 */
	INVALID(-1, "验证不通过"),

	/**
	 * 无错误
	 */
	OK(0, "无错误"),

	/**
	 * 单号不存在
	 */
	EXNO_ERROR(1, "单号不存在"),

	/**
	 * 验证码错误
	 */
	VERF_CODE_ERROR(2, "验证码错误"),

	/**
	 * 链接查询服务器失败
	 */
	SERVER_ERROR(3, "链接查询服务器失败"),

	/**
	 * 程序内部错误
	 */
	PROGRAME_INNER_ERROR(4, "程序内部错误"),

	/**
	 * 程序内部错误
	 */
	RUNTIME_ERROR(5, "程序执行错误"),

	/**
	 * 快递单号格式错误
	 */
	EXNO_FORMAT_ERROR(6, "快递单号格式错误"),

	/**
	 * 快递公司错误
	 */
	EXCOM_CODE_ERROR(7, "快递公司错误"),

	/**
	 * 未知错误
	 */
	UNKNOWN_ERROR(10, "未知错误"),

	/**
	 * API错误
	 */
	API_ERROR(20, "API错误"),

	/**
	 * API被禁用
	 */
	FORBBIDDEN_API(21, "API被禁用"),

	/**
	 * API查询量耗尽
	 */
	NOT_ENOUGH(21, "API查询量耗尽");

	private Integer value;
	private String text;

	public Integer getValue() {
		return value;
	}

	public String getText() {
		return text;
	}

	LogisErrorCode(Integer value, String text) {
		this.value = value;
		this.text = text;
	}

	// 缓存对象
	private static Map<Integer, LogisErrorCode> valuesMap;

	static {
		valuesMap = new HashMap<Integer, LogisErrorCode>();
		for (LogisErrorCode tmpElem : LogisErrorCode.values()) {
			valuesMap.put(tmpElem.getValue(), tmpElem);
		}
	}

	public static LogisErrorCode fromValue(Integer value) {
		return valuesMap.get(value);
	}
}
