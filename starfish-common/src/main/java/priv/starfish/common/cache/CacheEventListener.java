package priv.starfish.common.cache;

public interface CacheEventListener {
	public enum EventType {
		GET, PUT, EVICT, CLEAR, EXPIRE, OTHER
	}

	void onEvent(String cacheName, EventType eventType, Object key, Object extra);
}
