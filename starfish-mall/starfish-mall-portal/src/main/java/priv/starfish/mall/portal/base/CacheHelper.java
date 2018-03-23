/*
package priv.starfish.mall.portal.base;

import priv.starfish.common.cache.Cache;
import priv.starfish.common.helper.AppHelper;
import priv.starfish.common.model.ScopeEntity;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.EnumUtil;
import priv.starfish.common.util.TypeUtil;
import priv.starfish.common.web.WebEnvHelper;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.comn.entity.Region;
import priv.starfish.mall.comn.entity.Resource;
import priv.starfish.mall.mall.dto.MallDto;
import priv.starfish.mall.service.*;
import priv.starfish.mall.settle.entity.SettleWay;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class CacheHelper {
	private static ReentrantLock MallInfoLock = new ReentrantLock();
	private static ReentrantLock SettleWaysLock = new ReentrantLock();

	public static MallDto getMallInfo() {
		MallDto mallInfo = AppHelper.getLocalObject(AppBase.LocalCacheKeys.MALL_INFO);
		if (mallInfo == null) {
			MallInfoLock.lock();
			try {
				if (mallInfo == null) {
					MallService mallService = WebEnvHelper.getSpringBean(MallService.class);
					//
					mallInfo = mallService.getMallInfo();
					//
					AppHelper.setLocalObject(AppBase.LocalCacheKeys.MALL_INFO, mallInfo);
				}
			} finally {
				MallInfoLock.unlock();
			}
		}
		return mallInfo;
	}

	public static SettleWay getSettleWay(String wayCode) {
		//
		Map<String, SettleWay> settleWays = AppHelper.getLocalObject(AppBase.LocalCacheKeys.SETTLE_WAYS);
		if (settleWays == null) {
			SettleWaysLock.lock();
			try {
				if (settleWays == null) {
					SettleService settleService = WebEnvHelper.getSpringBean(SettleService.class);
					//
					List<SettleWay> settleWayList = settleService.getSettleWays(false);
					//
					settleWays = new HashMap<String, SettleWay>();
					//
					for (int i = 0; i < settleWayList.size(); i++) {
						SettleWay settleWay = settleWayList.get(i);
						settleWays.put(settleWay.getCode(), settleWay);
					}
					//
					AppHelper.setLocalObject(AppBase.LocalCacheKeys.SETTLE_WAYS, settleWays);
				}
			} finally {
				SettleWaysLock.unlock();
			}
		}
		return settleWays.get(wayCode);
	}

	// =================================== 用户 中央数据 ======================================
	@SuppressWarnings("unchecked")
	public static <T> T getUCData(Integer userId, String key) {
		Cache<Integer, Map<String, Object>> cache = AppBase.getUserCenterCache();
		//
		Map<String, Object> userData = cache.get(userId);
		//
		return userData == null ? null : (T) userData.get(key);
	}

	public static void setUCData(Integer userId, String key, Object value) {
		Cache<Integer, Map<String, Object>> cache = AppBase.getUserCenterCache();
		//
		Map<String, Object> userData = cache.get(userId);
		if (userData == null) {
			userData = new HashMap<String, Object>();
		}
		userData.put(key, value);
		//
		cache.put(userId, userData);
	}

	// =================================== 用户资源/权限数据（用于后台） ======================================
	private static ReentrantLock ProtectedUrlResourcesLock = new ReentrantLock();

	public static List<Resource> getProtectedUrlResources() {
		List<Resource> retUrlResoures = AppHelper.getLocalObject(AppBase.LocalCacheKeys.PROTECTED_URL_RESOURCES);
		if (retUrlResoures == null) {
			ProtectedUrlResourcesLock.lock();
			try {
				if (retUrlResoures == null) {
					ResourceService resourceService = WebEnvHelper.getSpringBean(ResourceService.class);
					//
					retUrlResoures = resourceService.getSysUrlResources(true);
					//
					AppHelper.setLocalObject(AppBase.LocalCacheKeys.PROTECTED_URL_RESOURCES, retUrlResoures);
				}
			} finally {
				ProtectedUrlResourcesLock.unlock();
			}
		}
		return retUrlResoures;
	}

	public static List<Integer> getContextUserPermIds(UserContext userContext) {
		List<Integer> userPermIds = null;
		//
		if (userContext.isSysUser()) {
			Integer userId = userContext.getUserId();
			// 获取当前用户所有权限id列表（可能从redis缓存中取）
			Cache<Integer, Map<String, List<Integer>>> userPermIdsCache = AppBase.getUserPermIdsCache();
			if (userContext.isInScopeEntity()) {
				ScopeEntity scopeEntity = userContext.getScopeEntity();
				AuthScope scope = EnumUtil.valueOf(AuthScope.class, scopeEntity.getScope());
				//
				Map<String, List<Integer>> userPermIdsMap = userPermIdsCache.get(userId);
				if (userPermIdsMap == null) {
					userPermIdsMap = new HashMap<String, List<Integer>>();
				}
				// userId => {scope+"::"+entityId => List<int>}
				String cacheKey = AppBase.makeCacheKey(scope, scopeEntity.getId());
				userPermIds = userPermIdsMap.get(cacheKey);
				if (userPermIds == null || userPermIds.size() == 0) {
					AuthxService authxService = WebEnvHelper.getSpringBean(AuthxService.class);
					userPermIds = authxService.getPermIdsByUserIdAndScopeAndEntityId(userId, scope, scopeEntity.getId());
					userPermIdsMap.put(cacheKey, userPermIds);
					//
					userPermIdsCache.put(userId, userPermIdsMap);
				}
			} else {
				//
				Map<String, List<Integer>> userPermIdsMap = userPermIdsCache.get(userId);
				if (userPermIdsMap == null) {
					userPermIdsMap = new HashMap<String, List<Integer>>();
				}
				// userId => {all => List<int>}
				String cacheKey = AppBase.makeCacheKey(AppBase.CacheKeys.ALL);
				userPermIds = userPermIdsMap.get(cacheKey);
				if (userPermIds == null || userPermIds.size() == 0) {
					AuthxService authxService = WebEnvHelper.getSpringBean(AuthxService.class);
					userPermIds = authxService.getPermIdsByUserId(userId);
					userPermIdsMap.put(cacheKey, userPermIds);
					//
					userPermIdsCache.put(userId, userPermIdsMap);
				}
			}
		}
		//
		return userPermIds;
	}

	// =================================== 地区数据 ======================================
	*/
/**
	 * { makeCacheKey(root-ids) => [ids] } <br/>
	 * { makeCacheKey(id) => {,, childIds : [[ids]]} }
	 * 
	 * @author koqiui
	 * @date 2015年8月10日 下午6:58:31
	 * 
	 * @return
	 *//*

	public static List<Region> getRegionsByParentId(Integer parentId) {
		List<Region> retRegions = null;
		//
		if (parentId == null) {
			parentId = -1;
		}
		Cache<String, Object> regionListCache = AppBase.getRegionListCache();
		SettingService settingService = WebEnvHelper.getSpringBean(SettingService.class);
		if (parentId == -1) {
			// 获取第一级地区/省
			@SuppressWarnings("unchecked")
			List<Integer> rootIds = (List<Integer>) regionListCache.get(AppBase.CacheKeys.ROOT_IDS);
			if (rootIds == null || rootIds.size() == 0) {
				retRegions = settingService.getRegionsByParentId(parentId);
				//
				int count = retRegions.size();
				rootIds = TypeUtil.newList(Integer.class);
				for (int i = 0; i < count; i++) {
					Region region = retRegions.get(i);
					Integer id = region.getId();
					rootIds.add(region.getId());
					//
					regionListCache.put(id.toString(), region);
				}
				//
				regionListCache.put(AppBase.CacheKeys.ROOT_IDS, rootIds);
			} else {
				int count = rootIds.size();
				retRegions = TypeUtil.newList(Region.class);
				for (int i = 0; i < count; i++) {
					Integer id = rootIds.get(i);
					Region region = (Region) regionListCache.get(id.toString());
					retRegions.add(region);
				}
			}
		} else {
			Region parentRegion = (Region) regionListCache.get(parentId.toString());
			if (parentRegion == null) {
				parentRegion = settingService.getRegionById(parentId);
				//
				regionListCache.put(parentId.toString(), parentRegion);
			}
			if (parentRegion != null) {
				if (parentRegion.getLevel() < Region.MAX_LEVEL) {
					List<Integer> childIds = parentRegion.getChildIds();
					if (childIds == null || childIds.size() == 0) {
						retRegions = settingService.getRegionsByParentId(parentId);
						int count = retRegions.size();
						childIds = TypeUtil.newList(Integer.class);
						for (int i = 0; i < count; i++) {
							Region region = retRegions.get(i);
							Integer id = region.getId();
							childIds.add(region.getId());
							//
							regionListCache.put(id.toString(), region);
						}
						parentRegion.setChildIds(childIds);
						regionListCache.put(parentId.toString(), parentRegion);
					} else {
						int count = childIds.size();
						retRegions = TypeUtil.newList(Region.class);
						for (int i = 0; i < count; i++) {
							Integer id = childIds.get(i);
							Region region = (Region) regionListCache.get(id.toString());
							retRegions.add(region);
						}
					}
				}
			}
		}
		//
		return retRegions;
	}

	public static Region getRegionById(Integer regionId) {
		if (regionId == null) {
			return null;
		}
		Cache<String, Object> regionListCache = AppBase.getRegionListCache();
		SettingService settingService = WebEnvHelper.getSpringBean(SettingService.class);
		Region targetRegion = (Region) regionListCache.get(regionId.toString());
		if (targetRegion == null) {
			targetRegion = settingService.getRegionById(regionId);
			//
			regionListCache.put(regionId.toString(), targetRegion);
		}
		if (targetRegion != null) {
			if (targetRegion.getLevel() < Region.MAX_LEVEL) {
				List<Integer> childIds = targetRegion.getChildIds();
				if (childIds == null || childIds.size() == 0) {
					List<Region> childRegions = settingService.getRegionsByParentId(regionId);
					int count = childRegions.size();
					childIds = TypeUtil.newList(Integer.class);
					for (int i = 0; i < count; i++) {
						Region region = childRegions.get(i);
						Integer id = region.getId();
						childIds.add(region.getId());
						//
						regionListCache.put(id.toString(), region);
					}
					targetRegion.setChildIds(childIds);
					regionListCache.put(regionId.toString(), targetRegion);
				}
			}
		}
		return targetRegion;

	}
}
*/
