package priv.starfish.mall.web.base;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AlTokenHelper {

	public static String setUserAlToken(Integer userId, String appType, String token, Date createTime) {
		if (userId == null || appType == null || token == null) {
			return null;
		}
		//
		AlToken alToken = AlToken.newOne();
		alToken.userId = userId;
		alToken.appType = appType;
		alToken.token = token;
		if (createTime == null) {
			createTime = new Date();
		}
		alToken.createTs = createTime.getTime();
		//
		String tokenEncoded = alToken.encode();
		//
		Map<String, AlToken> appAlTokens = CacheHelper.getUCData(userId, "appAlTokens");
		if (appAlTokens == null) {
			appAlTokens = new HashMap<String, AlToken>();
		}
		appAlTokens.put(alToken.appType, alToken);
		//
		CacheHelper.setUCData(userId, "appAlTokens", appAlTokens);
		//
		return tokenEncoded;
	}

	public static String setUserAlToken(Integer userId, String appType, String token) {
		return setUserAlToken(userId, appType, token, null);
	}

	public static AlToken getUserAlToken(Integer userId, String appType) {
		if (userId == null || appType == null) {
			return null;
		}
		//
		Map<String, AlToken> appAlTokens = CacheHelper.getUCData(userId, "appAlTokens");
		if (appAlTokens != null) {
			return appAlTokens.get(appType);
		}
		return null;
	}

	public static void removeUserAlToken(Integer userId, String appType) {
		if (userId == null || appType == null) {
			return;
		}
		Map<String, AlToken> appAlTokens = CacheHelper.getUCData(userId, "appAlTokens");
		if (appAlTokens != null) {
			appAlTokens.remove(appType);
		}
	}
}
