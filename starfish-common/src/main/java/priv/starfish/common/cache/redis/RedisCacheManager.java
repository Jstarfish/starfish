package priv.starfish.common.cache.redis;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import priv.starfish.common.cache.CacheManager;


public final class RedisCacheManager implements CacheManager {
	private final Log logger = LogFactory.getLog(this.getClass());
	//
	List<RedisCache<?, ?>> cacheList = new ArrayList<RedisCache<?, ?>>();
	Map<String, Integer> cacheNameIndexMap = new LinkedHashMap<String, Integer>();

	public void setCacheList(List<RedisCache<?, ?>> cacheList) {
		if (cacheList != null) {
			this.cacheList.clear();
			this.cacheNameIndexMap.clear();
			for (int i = 0, c = cacheList.size(); i < c; i++) {
				RedisCache<?, ?> cache = cacheList.get(i);
				String name = cache.getName();
				if (!this.cacheNameIndexMap.containsKey(name)) {
					this.cacheNameIndexMap.put(name, this.cacheList.size());
					this.cacheList.add(cache);
					//
					logger.debug("已加入Redis缓存：" + name);
				}
			}
		}
	}

	public void addCache(RedisCache<?, ?> cache) {
		String name = cache.getName();
		if (!this.cacheNameIndexMap.containsKey(name)) {
			this.cacheNameIndexMap.put(name, this.cacheList.size());
			this.cacheList.add(cache);
			//
			logger.debug("已加入Redis缓存：" + name);
		}
	}

	@Override
	public Set<String> getCacheNames() {
		return this.cacheNameIndexMap.keySet();
	}

	@Override
	public RedisCache<?, ?> getCache(String name) {
		Integer index = this.cacheNameIndexMap.get(name);
		return index == null ? null : this.cacheList.get(index);
	}
}
