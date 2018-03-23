package priv.starfish.mall.web.base;

import priv.starfish.common.util.PasswordUtil;
import priv.starfish.common.util.StrUtil;

import java.io.Serializable;

/**
 * 自动登录token信息
 * 
 * @author koqiui
 * @date 2015年12月10日 上午12:51:24
 *
 */
public class AlToken implements Serializable {
	private static final long serialVersionUID = 1L;
	public static int MAX_AGE_IN_SECONDS = 60 * 60 * 24 * 7;
	//
	public Integer userId;
	public String appType;
	//
	public String token;
	public Long createTs;
	//
	public String salt;
	public String tokenEncrypted;

	public static AlToken newOne() {
		return new AlToken();
	}

	public static AlToken parse(String tokenEncoded) {
		if (StrUtil.isNullOrBlank(tokenEncoded)) {
			return null;
		}
		//
		int tmpIndex = tokenEncoded.indexOf('#');
		if (tmpIndex < 1) {
			return null;
		}
		//
		AlToken alToken = AlToken.newOne();
		try {
			alToken.userId = Integer.parseInt(tokenEncoded.substring(0, tmpIndex));
		} catch (NumberFormatException e) {
			return alToken;
		}
		//
		tmpIndex = tmpIndex + 1;
		int tmpIndex2 = tokenEncoded.indexOf('#', tmpIndex);
		if (tmpIndex2 == -1) {
			return alToken;
		}
		alToken.appType = tokenEncoded.substring(tmpIndex, tmpIndex2);
		//
		tmpIndex = tmpIndex2 + 1;
		if (tokenEncoded.length() <= tmpIndex) {
			return alToken;
		}
		alToken.tokenEncrypted = tokenEncoded.substring(tmpIndex);
		//
		System.out.println("parsed \n" + alToken);
		return alToken;
	}

	public String encode() {
		assert userId != null;
		assert appType != null;
		assert token != null;
		//
		if (StrUtil.isNullOrBlank(salt)) {
			salt = PasswordUtil.generateSaltStr();
			//
			tokenEncrypted = PasswordUtil.encrypt(token, salt);
		}

		//
		return userId + "#" + appType + "#" + tokenEncrypted;
	}

	public boolean validate(AlToken given) {
		return given != null && given.tokenEncrypted != null && given.tokenEncrypted.equals(this.tokenEncrypted);
	}

	public boolean validate(String tokenEncoded) {
		AlToken given = parse(tokenEncoded);
		return validate(given);
	}

	@Override
	public String toString() {
		return "AlToken [userId=" + userId + ", appType=" + appType + ", token=" + token + ", salt=" + salt + ", tokenEncrypted=" + tokenEncrypted + "]";
	}
}
