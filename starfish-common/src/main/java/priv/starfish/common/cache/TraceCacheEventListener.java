package priv.starfish.common.cache;

import java.io.Serializable;

public class TraceCacheEventListener implements CacheEventListener, Serializable {
	private static final long serialVersionUID = 2192322450291875281L;

	public static TraceCacheEventListener getInstance() {
		return new TraceCacheEventListener();
	}

	@Override
	public void onEvent(String cacheName, EventType eventType, Object key, Object extra) {
		if (EventType.GET == eventType) {
			System.out.println(cacheName + ">> " + eventType + " : <" + key.toString() + "> = " + extra);
		} else if (EventType.PUT == eventType) {
			System.out.println(cacheName + ">> " + eventType + " : <" + key.toString() + "> = " + extra);
		} else if (EventType.EVICT == eventType) {
			System.out.println(cacheName + ">> " + eventType + " : <" + key.toString() + ">");
		} else {
			System.out.println(cacheName + ">> " + eventType + " : <" + key.toString() + (extra == null ? ">" : "> " + extra));
		}
	}

}
