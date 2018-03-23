package priv.starfish.common.cache;

import java.util.Set;

public interface CacheManager {
	Set<String> getCacheNames();

	Cache<?, ?> getCache(String name);
}
