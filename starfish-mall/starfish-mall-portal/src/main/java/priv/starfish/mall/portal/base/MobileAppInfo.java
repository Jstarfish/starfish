/*
package priv.starfish.mall.portal.base;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import priv.starfish.common.helper.FileHelper;
import priv.starfish.common.util.JsonUtil;
import priv.starfish.common.util.TypeUtil;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// appName => MobileAppInfo
//YtcbForCust, YtcbForDist, ...
public class MobileAppInfo {
	protected static final Log logger = LogFactory.getLog(MobileAppInfo.class);

	//
	public static class VersionInfo {
		public String versionName;
		public Integer versionCode;
		public List<String> changes;
		public String versionNameRequired;
		public Integer versionCodeRequired;
		public Boolean forceToUpgrade;
		public String downloadUrl;
		public String supportMail;
	}

	//
	public Long tsValue;
	public String serviceTelNo;
	public String serviceTime;
	// osName => VersionInfo
	public Map<String, VersionInfo> versionsInfo;
	//
	public Map<String, Object> extraInfo;

	public static Map<String, MobileAppInfo> loadFromJsonFile(String jsonFilePath) {
		try {
			BufferedReader dataReader = FileHelper.getResourceAsBufferedReader(jsonFilePath);
			String jsonStr = FileHelper.readBufferedReaderAsString(dataReader);
			logger.info(jsonStr);
			//
			Map<String, MobileAppInfo> retMap = new HashMap<String, MobileAppInfo>();
			//
			Map<String, Object> appsInfo = JsonUtil.fromJson(jsonStr, TypeUtil.TypeRefs.StringObjectMapType);
			for (Map.Entry<String, Object> appInfo : appsInfo.entrySet()) {
				Object appInfoObject = appInfo.getValue();
				String objJson = JsonUtil.toJson(appInfoObject);
				retMap.put(appInfo.getKey(), JsonUtil.fromJson(objJson, MobileAppInfo.class));
			}
			//
			return retMap;
		} catch (Exception ex) {
			logger.error(ex);
			//
			return null;
		}
	}

	//
	@Override
	public String toString() {
		return "MobileAppInfo [tsValue=" + tsValue + ", serviceTelNo=" + serviceTelNo + ", serviceTime=" + serviceTime + ", versionsInfo=" + versionsInfo + ", extraInfo=" + extraInfo + "]";
	}
}
*/
