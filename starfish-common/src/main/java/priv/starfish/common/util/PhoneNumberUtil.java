package priv.starfish.common.util;

import java.util.Random;
import java.util.regex.Pattern;

public class PhoneNumberUtil {

	public static String[] PREFIX = { "132", "133", "134", "135", "136", "137", "138", "186", "187", "189" };

	public static String generateBlurPhoneNumber() {
		Random random = new Random();
		int nextInt = random.nextInt(10);
		return PREFIX[nextInt] + "XXXX" + CodeUtil.randomNumCode(4);
	}

	public static String generatePhoneNumber() {
		Random random = new Random();
		int nextInt = random.nextInt(10);
		return PREFIX[nextInt] + CodeUtil.randomNumCode(8);
	}

	private static Pattern PatternMobile = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$");

	/**
	 * 手机号验证
	 * 
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isMobile(String str) {
		return str != null && PatternMobile.matcher(str).matches();
	}

	private static Pattern PatternTelNo = Pattern.compile("(^([0][1-9][0-9]-?)?[0-9]{8}$)|(^([0][1-9]{3}-?)?[0-9]{7}$)");

	/**
	 * 固定电话号码验证
	 * 
	 * @param str
	 * @return 验证通过返回true
	 */
	public static boolean isTelNo(String str) {
		return str != null && PatternTelNo.matcher(str).matches();
	}

	/**
	 * 是否为移动或固定电话号码
	 * 
	 * @author koqiui
	 * @date 2015年10月28日 上午10:13:25
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isPhoneNumber(String str) {
		return isMobile(str) || isTelNo(str);
	}

	/**
	 * 转换为可读格式 13718809116 -> 137 1880 9116
	 * 
	 * @author koqiui
	 * @date 2015年10月28日 上午10:12:32
	 * 
	 * @param phoneNo
	 * @return
	 */
	public static String toReadablePhone(String phoneNo) {
		if (phoneNo == null) {
			return null;
		}
		//
		StringBuffer sb = new StringBuffer();
		if (isMobile(phoneNo)) {
			sb.append(phoneNo.substring(0, 3));
			sb.append(" ");
			sb.append(phoneNo.substring(3, 7));
			sb.append(" ");
			sb.append(phoneNo.substring(7));
		} else {
			sb.append(phoneNo);
		}
		return sb.toString();
	}

	/**
	 * 转换为掩码过的格式 13718809116 -> 137*****166
	 * 
	 * @author koqiui
	 * @date 2015年10月28日 上午10:14:06
	 * 
	 * @param phoneNo
	 * @return
	 */
	public static String asMaskedPhone(String phoneNo) {
		if (phoneNo == null) {
			return null;
		}
		//
		StringBuffer sb = new StringBuffer();
		if (isMobile(phoneNo)) {
			sb.append(phoneNo.substring(0, 3));
			sb.append("*****");
			sb.append(phoneNo.substring(8));
		} else {
			sb.append(phoneNo);
		}
		return sb.toString();
	}
}
