package priv.starfish.common.sms;

import java.util.HashMap;
import java.util.Map;

/**
 * 短信错误码
 * 
 * @author zhangjunjun
 * 
 */
public enum SmsErrorCode {

	/**
	 * Http请求异常
	 */
	HTTP_ERROR(-1, "Http请求异常"),

	/**
	 * 发送正常
	 */
	OK(100, "发送正常"),
	
	/**
	 * 没有该用户账户
	 */
	NO_ACCOUNT(101, "没有该用户账户"),

	/**
	 * 密钥不正确（不是用户密码）
	 */
	KEY_INCORRECT(102, "接口密钥不正确"),
	
	/**
	 * MD5接口密钥加密不正确
	 */
	ENCRYPT_INCORRECT(103, "MD5接口密钥加密不正确"),
	
	/**
	 * 短信数量不足
	 */
	NOT_ENOUGH(104, "短信数量不足"),

	/**
	 * 该用户被禁用
	 */
	FORBBIDDEN_ACCOUNT(105, "该用户被禁用"),
	
	/**
	 * 短信内容出现非法字符
	 */
	FORBBIDDEN_CONTENT(106, "短信内容出现非法字符'"),

	/**
	 * 手机号格式不正确
	 */
	PHONE_FORMAT_INCORRECT(107, "手机号格式不正确"),

	/**
	 * 手机号码为空
	 */
	BLANK_NUMBER(108, "手机号码为空"),

	/**
	 * 短信内容为空
	 */
	BLANK_CONTENT(109, "短信内容为空"),
	
	/**
	 * 短信签名格式不正确
	 */
	SIGN_FORMAT_INCORRECT(110, "短信签名格式不正确"),
	
	/**
	 * IP限制
	 */
	FORBBIDDEN_IP(111, "IP限制");

	
	private Integer value;
	private String message;

	public Integer getValue() {
		return value;
	}

	public String getMessage() {
		return message;
	}
	

	SmsErrorCode(Integer value, String message) {
		this.value = value;
		this.message = message;
	}

	// 缓存对象
	private static Map<Integer, SmsErrorCode> valuesMap;
	static {
		valuesMap = new HashMap<Integer, SmsErrorCode>();
		for (SmsErrorCode tmpElem : SmsErrorCode.values()) {
			valuesMap.put(tmpElem.getValue(), tmpElem);
		}
	}

	public static SmsErrorCode fromValue(Integer value) {
		return valuesMap.get(value);
	}

}
