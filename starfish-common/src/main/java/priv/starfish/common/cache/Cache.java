package priv.starfish.common.cache;

import java.util.Set;

public interface Cache<K, V> {
	String getName();

	V get(K key);

	void put(K key, V value);

	void evict(K key);

	void clear();
	
	Set<K> keySet();

	void setEventListener(CacheEventListener eventListener);
}
