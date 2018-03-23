package priv.starfish.mall.web.base;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import priv.starfish.common.base.AppNodeInfo;
import priv.starfish.common.base.MobileAppInfo;
import priv.starfish.common.cache.Cache;
import priv.starfish.common.helper.AppHelper;
import priv.starfish.common.helper.DirFileChangeWatcher;
import priv.starfish.common.helper.FileHelper;
import priv.starfish.common.http.AppUrlPool;
import priv.starfish.common.model.Size;
import priv.starfish.common.util.JsonUtil;
import priv.starfish.common.util.RSACrypter;
import priv.starfish.common.util.StrUtil;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 应用范围常量
 *
 * @author koqiui
 * @date 2015年7月27日 下午10:12:00
 *
 */
public class AppBase {
	protected static final Log logger = LogFactory.getLog(AppBase.class);
	//
	// cookie中的session名称（spring-session默认的名称）
	public static final String COOKIE_SESSION_NAME = "SESSION";
	// cookie中alToken（自动登录Token名称）
	public static final String COOKIE_ALTOKEN_NAME = "alToken";
	// header中的session名称（供app用，因为解析Set-Cookie头较麻烦，采用header较容易）
	public static final String HEADER_SESSION_NAME = "X-Custom-session";
	// header中的alToken名称
	public static final String HEADER_ALTOKEN_NAME = "X-Custom-alToken";
	//
	public static final String REQUEST_ALTOKEN_SET_FLAG_NAME = "alToken.has.set.already";
	//
	public static final String SESSION_KEY_REDIRECT_URL = "redirect.url";
	//
	public static final String SESSION_KEY_USER_SCOPE_SITE_RES_TREE = "user.scop.site.res.tree";

	// 图片大小定义
	private static final Map<String, Size> imageSizeDefs;

	public static final String SESSION_KEY_SALE_CART = "sale.cart";

	// 配置文件变更监视器
	private static DirFileChangeWatcher confFileWatcher;

	public static void loadMobileAppsInfo() {
		AppNodeInfo nodeInfo = AppNodeInfo.getCurrent();
		if (nodeInfo.getRole().equals("web-front")) {
			String jsonFilePath = "conf/mob-apps-info.json";
			try {
				Map<String, MobileAppInfo> appsInfo = MobileAppInfo.loadFromJsonFile(jsonFilePath);
				for (Map.Entry<String, MobileAppInfo> appInfo : appsInfo.entrySet()) {
					AppHelper.setLocalObject(LocalCacheKeys.MOBILE_APP_INFO_PREFIX + appInfo.getKey(), appInfo.getValue());
				}
				logger.debug("app配置数据已加载[" + nodeInfo.getName() + "]");
			} catch (Exception ex) {
				logger.error(ex);
			}
		}
	}

	public static void startWatchConfFiles() {
		if (confFileWatcher == null) {
			try {
				confFileWatcher = new DirFileChangeWatcher("classpath:conf", "crypt.keys", "bedirect-app-urls.properties", "mob-apps-info.json");
				//
				DirFileChangeWatcher.FileChangeListener confChangeListener = new DirFileChangeWatcher.FileChangeListener() {
					@Override
					public void onFileModified(String fileName, String filePath) {
						if (fileName.equals("crypt.keys")) {
							RSACrypter.readKeyPair();
							//
							logger.debug("密钥已重新加载");
						} else if (fileName.equals("bedirect-app-urls.properties")) {
							AppUrlPool bedirectAppUrlPool = AppUrlPool.getInstance();
							bedirectAppUrlPool.loadFromConfigFile("conf/bedirect-app-urls.properties", "bedirect.app.urls");
							//
							logger.debug("银联支付服务器url已重新加载");
							//
							bedirectAppUrlPool.echo();
						} else if (fileName.equals("mob-apps-info.json")) {
							loadMobileAppsInfo();
						}
					}
				};
				//
				confFileWatcher.addFileChangeListener(confChangeListener);
				//
				confFileWatcher.start();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
	}

	public static void stopWatchConfFiles() {
		if (confFileWatcher != null) {
			confFileWatcher.stop(1);
		}
	}

	static {
		String _imageSizeDefs = FileHelper.readResourceAsAsString("/conf/image-size-defs.json");
		Map<String, Size> tmpMap = new HashMap<String, Size>();
		imageSizeDefs = JsonUtil.fromJson(_imageSizeDefs, tmpMap.getClass());
	}

	public static Size getImageSizeDef(String codeKey) {
		return imageSizeDefs.get(codeKey);
	}

	// 全局和本地缓存支持
	private static final String cacheKeySepChars = "::";

	public static String makeCacheKey(Object... keyParts) {
		assert keyParts.length > 0;
		return StrUtil.join(keyParts, cacheKeySepChars);
	}

	public static class CacheNames {
		public static final String UserCenter = "userCenterCache";
		public static final String UserPermIdsCache = "userPermIdsCache";
		public static final String RegionListCache = "regionListCache";
		public static final String XOrderCache = "xOrderCache";

	}

	//
	public static class CacheKeys {
		public static final String ALL = "all";
		public static final String ROOT_IDS = "root-ids";
	}

	public static class LocalCacheKeys {
		public static final String ALL_URL_RESOURCES = "allUrlResoures";
		public static final String PROTECTED_URL_RESOURCES = "protectedUrlResoures";
		public static final String MALL_INFO = "mallInfo";
		public static final String SETTLE_WAYS = "settleWays";
		public static final String MOBILE_APP_INFO_PREFIX = "mobileAppInfo.";
	}

	//
	public static Cache<Integer, Map<String, Object>> getUserCenterCache() {
		return (Cache<Integer, Map<String, Object>>) AppHelper.getCacheManager().getCache(CacheNames.UserCenter);
	}

	/**
	 * userId => { all => permIds, makeCacheKey(scope, entityId) => permIds }
	 * 
	 * @author koqiui
	 * @date 2015年8月10日 下午6:58:31
	 * 
	 * @return
	 */
	public static Cache<Integer, Map<String, List<Integer>>> getUserPermIdsCache() {
		return (Cache<Integer, Map<String, List<Integer>>>) AppHelper.getCacheManager().getCache(CacheNames.UserPermIdsCache);
	}

	public static void clearUserPermIdsCache() {
		Cache<Integer, Map<String, List<Integer>>> cache = getUserPermIdsCache();
		cache.clear();
	}

	/**
	 * { makeCacheKey(root-ids) => [ids] } <br/>
	 * { makeCacheKey(id) => {,, childIds : [[ids]]} }
	 * 
	 * @author koqiui
	 * @date 2015年8月10日 下午6:58:31
	 * 
	 * @return
	 */
	public static Cache<String, Object> getRegionListCache() {
		return (Cache<String, Object>) AppHelper.getCacheManager().getCache(CacheNames.RegionListCache);
	}

	public static void clearRegionListCache() {
		Cache<String, Object> cache = getRegionListCache();
		cache.clear();
	}

	public static void clearMallInfo() {
		AppHelper.removeLocalObject(LocalCacheKeys.MALL_INFO);
	}

	public static void clearSettleWays() {
		AppHelper.removeLocalObject(LocalCacheKeys.SETTLE_WAYS);
	}

	/**
	 * 订单信息缓存
	 * 
	 * @author koqiui
	 * @date 2016年2月1日 下午4:40:41
	 * 
	 * @return
	 */
	public static Cache<String, Object> getXOrderCache() {
		return (Cache<String, Object>) AppHelper.getCacheManager().getCache(CacheNames.XOrderCache);
	}

}
