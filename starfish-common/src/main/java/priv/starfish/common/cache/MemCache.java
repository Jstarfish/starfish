package priv.starfish.common.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import  priv.starfish.common.cache.CacheEventListener.EventType;

public class MemCache<K, V> implements Cache<K, V> {
	private static int InitialCapacity = 10;
	private String name;
	private Map<K, V> backingMap;
	private CacheEventListener eventListener;

	public MemCache(String name, int initialCapacity) {
		this.name = name;
		this.backingMap = new HashMap<K, V>(initialCapacity);
	}

	public MemCache(String name) {
		this(name, InitialCapacity);
	}

	@Override
	public void setEventListener(CacheEventListener eventListener) {
		this.eventListener = eventListener;
		System.out.println("RedisCache[" + this.name + "] setEventListener .");
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void put(K key, V value) {
		this.backingMap.put(key, value);
		if (this.eventListener != null) {
			this.eventListener.onEvent(this.name, EventType.PUT, key, value);
		}
	}

	@Override
	public V get(K key) {
		V value = this.backingMap.get(key);
		if (this.eventListener != null) {
			this.eventListener.onEvent(this.name, EventType.GET, key, value);
		}
		return value;
	}

	@Override
	public void evict(K key) {
		this.backingMap.remove(key);
		if (this.eventListener != null) {
			this.eventListener.onEvent(this.name, EventType.EVICT, key, null);
		}
	}
	
	@Override
	public Set<K> keySet() {
		return this.backingMap.keySet();
	}

	@Override
	public void clear() {
		this.backingMap.clear();
		if (this.eventListener != null) {
			this.eventListener.onEvent(this.name, EventType.CLEAR, "*", null);
		}
	}
}
