package priv.starfish.common.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

/**
 * 
 * @author koqiui 图形验证码 Helper
 */
public class CheckCodeHelper {
	private CheckCodeHelper() {
		// ...
	}

	public static final String DEFAULT_CODE_NAME = "default";

	private static final String CHECK_CODE_MAP_NAME = CheckCodeHelper.class.getName() + ".checkCodes";

	public static void saveCode(HttpSession session, String codeText) {
		saveCode(session, DEFAULT_CODE_NAME, codeText);
	}

	@SuppressWarnings("unchecked")
	public static void saveCode(HttpSession session, String codeName, String codeText) {
		Map<String, String> codeMap = (Map<String, String>) session.getAttribute(CHECK_CODE_MAP_NAME);
		if (codeMap == null) {
			codeMap = new HashMap<String, String>();
		}
		codeMap.put(codeName, codeText);
		session.setAttribute(CHECK_CODE_MAP_NAME, codeMap);
	}

	public static boolean isValidCode(HttpSession session, String codeText) {
		return isValidCode(session, DEFAULT_CODE_NAME, codeText);
	}

	@SuppressWarnings("unchecked")
	public static boolean isValidCode(HttpSession session, String codeName, String codeText) {
		Map<String, String> codeMap = (Map<String, String>) session.getAttribute(CHECK_CODE_MAP_NAME);
		if (codeMap == null) {
			return false;
		}
		String savedText = codeMap.get(codeName);
		return savedText != null && savedText.equalsIgnoreCase(codeText);
	}

}
