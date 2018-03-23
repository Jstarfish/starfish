package priv.starfish.common.http;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
//应该用下边这个
//import org.elasticsearch.common.netty.util.internal.ConcurrentHashMap;


import priv.starfish.common.config.PropertyConfigurer;
import priv.starfish.common.util.JsonUtil;
import priv.starfish.common.util.OSUtil;
import priv.starfish.common.util.StrUtil;

public class AppUrlPool {
	private static AppUrlPool instance = new AppUrlPool();

	public static AppUrlPool getInstance() {
		return instance;
	}

	private Map<String, Integer> appUrlHitMap = new ConcurrentHashMap<String, Integer>();

	//
	public AppUrlPool addAppUrl(String appUrl) {
		appUrl = appUrl.trim();
		//
		if (!appUrlHitMap.containsKey(appUrl)) {
			appUrlHitMap.put(appUrl, 0);
		}
		//
		return this;
	}

	public AppUrlPool removeAppUrl(String appUrl) {
		appUrl = appUrl.trim();
		//
		if (appUrlHitMap.containsKey(appUrl)) {
			appUrlHitMap.remove(appUrl);
		}
		//
		return this;
	}

	public int getAppUrlCount() {
		return appUrlHitMap.size();
	}
	
	public AppUrlPool loadFromConfigFile(String confFileName, String key){
		return loadFromConfigFile(confFileName, key, true);
	}

	public AppUrlPool loadFromConfigFile(String confFileName, String key, boolean osSpecific) {
		PropertyConfigurer propertyConfigurer = PropertyConfigurer.newInstance(confFileName, "UTF-8");
		if(osSpecific){
			key = OSUtil.getOsSuffixed(key);
		}
		String appUrlsStr = propertyConfigurer.get(key);
		if (StrUtil.hasText(appUrlsStr)) {
			this.appUrlHitMap.clear();
			//
			String[] appUrls = appUrlsStr.split(",");
			for (int i = 0; i < appUrls.length; i++) {
				String appUrl = appUrls[i];
				if (StrUtil.hasText(appUrl)) {
					this.addAppUrl(appUrl);
				}
			}
		}
		//
		return this;
	}

	public String getAppUrl() {
		int minHit = -1;
		String minHitAppUrl = null;
		for (Map.Entry<String, Integer> appUrlHit : this.appUrlHitMap.entrySet()) {
			String appUrl = appUrlHit.getKey();
			int hit = appUrlHit.getValue();
			if (minHitAppUrl == null) {
				minHitAppUrl = appUrl;
				minHit = hit;
			} else if (hit < minHit) {
				minHit = hit;
				minHitAppUrl = appUrl;
			}
		}
		// 增加计数
		this.appUrlHitMap.put(minHitAppUrl, minHit + 1);
		//
		return minHitAppUrl;
	}

	public void echo() {
		System.out.println(JsonUtil.toFormattedJson(appUrlHitMap));
	}

}
