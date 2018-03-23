package priv.starfish.common.util;

import java.util.ArrayList;
import java.util.List;

public class BoolUtil {
	private static List<String> ValuesTrue = new ArrayList<String>();
	private static List<String> ValuesFalse = new ArrayList<String>();
	static {
		ValuesTrue.add("true");
		ValuesTrue.add("t");
		ValuesTrue.add("yes");
		ValuesTrue.add("y");
		ValuesTrue.add("on");
		ValuesTrue.add("1");
		//
		ValuesFalse.add("false");
		ValuesFalse.add("f");
		ValuesFalse.add("no");
		ValuesFalse.add("n");
		ValuesFalse.add("off");
		ValuesFalse.add("0");
	}

	public static boolean isTrue(Boolean bool) {
		if (bool == null) {
			return false;
		} else {
			return bool.booleanValue() == true;
		}
	}

	public static boolean isFalse(Boolean bool) {
		if (bool == null) {
			return false;
		} else {
			return bool.booleanValue() == false;
		}
	}

	public static boolean isTrue(String boolStr, String... trueValues) {
		if (trueValues.length > 0) {
			for (String trueValue : trueValues) {
				if (boolStr == null) {
					if (trueValue == null) {
						return true;
					}
				} else if (boolStr.equalsIgnoreCase(trueValue)) {
					return true;
				}
			}
			return false;
		} else {
			if (!StrUtil.hasText(boolStr)) {
				return false;
			} else {
				for (String trueValue : ValuesTrue) {
					if (trueValue.equalsIgnoreCase(boolStr)) {
						return true;
					}
				}
				return false;
			}
		}
	}

	public static boolean isFalse(String boolStr, String... falseValues) {
		if (falseValues.length > 0) {
			for (String falseValue : falseValues) {
				if (boolStr == null) {
					if (falseValue == null) {
						return true;
					}
				} else if (boolStr.equalsIgnoreCase(falseValue)) {
					return true;
				}
			}
			return false;
		} else {
			if (!StrUtil.hasText(boolStr)) {
				return true;
			} else {
				for (String falseValue : ValuesFalse) {
					if (falseValue.equalsIgnoreCase(boolStr)) {
						return true;
					}
				}
				return false;
			}
		}
	}

	public static boolean parse(String boolStr) {
		return isTrue(boolStr);
	}
}
