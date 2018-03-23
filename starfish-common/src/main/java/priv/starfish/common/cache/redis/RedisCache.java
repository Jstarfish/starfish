package priv.starfish.common.cache.redis;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import priv.starfish.common.cache.Cache;
import priv.starfish.common.cache.CacheEventListener;
import priv.starfish.common.cache.CacheEventListener.EventType;

public class RedisCache<K, V> implements Cache<K, V> {
	private static final Log logger = LogFactory.getLog(RedisCache.class);
	// 自定义缓存前缀
	protected static final String CACHE_NS_ROOT = "redis-caches";
	// 前缀分隔符
	protected static final String CACHE_NS_DELIMITER = ":";
	protected static final String CACHE_KEY_PREFIX = "<-#->";

	// 生成组合键
	private static String makeKey(Object... keyParts) {
		int keyPartCount = keyParts.length;
		//
		assert (keyPartCount > 1);
		//
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < keyPartCount; i++) {
			if (i > 0) {
				result.append(CACHE_NS_DELIMITER);
			}
			result.append(keyParts[i]);
		}
		return result.toString();
	}

	// 默认的序列化/反序列化对象
	public static final StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
	public static final JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
	// 缓存名称
	private String name;
	// 缓存键（前缀）
	private String cacheKey;
	private String cacheKeyForKey;

	public RedisCache(String name) {
		this.name = name;
		this.cacheKey = makeKey(CACHE_NS_ROOT, this.name);
		this.cacheKeyForKey = makeKey(CACHE_NS_ROOT, this.name, CACHE_KEY_PREFIX);
		//
		logger.debug("RedisCache[" + this.name + "] created, cacheKey : " + this.cacheKey + ", cacheKeyForKey : " + this.cacheKeyForKey);
	}

	//
	private Boolean useKeySet = false;
	//
	private RedisConnectionFactory connectionFactory;
	//
	private RedisTemplate<String, V> redisTemplate;
	private ValueOperations<String, V> operations;
	private RedisTemplate<String, K> redisTemplateForKey;
	private ValueOperations<String, K> operationsForKey;

	private void initForKeySet() {
		if (this.redisTemplateForKey == null) {
			this.redisTemplateForKey = new RedisTemplate<String, K>();
			this.redisTemplateForKey.setKeySerializer(stringRedisSerializer);
			this.redisTemplateForKey.setValueSerializer(jdkSerializationRedisSerializer);
			this.redisTemplateForKey.setConnectionFactory(connectionFactory);
			this.redisTemplateForKey.afterPropertiesSet();
			//
			this.operationsForKey = this.redisTemplateForKey.opsForValue();
		}
	}

	public void setUseKeySet(Boolean useKeySet) {
		this.useKeySet = useKeySet;
		//
		if (useKeySet && this.connectionFactory != null) {
			initForKeySet();
		}
	}

	public void setConnectionFactory(RedisConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
		//
		this.redisTemplate = new RedisTemplate<String, V>();
		this.redisTemplate.setKeySerializer(stringRedisSerializer);
		this.redisTemplate.setValueSerializer(jdkSerializationRedisSerializer);
		this.redisTemplate.setConnectionFactory(connectionFactory);
		this.redisTemplate.afterPropertiesSet();
		//
		this.operations = this.redisTemplate.opsForValue();
		// ---------------------------------------------------------------------
		if (useKeySet) {
			initForKeySet();
		}
		//
		logger.debug("RedisConnectionFactory set for RedisCache[" + this.name + "]");
	}

	private String getPrefixedKey(Object key) {
		assert key != null;
		return makeKey(this.cacheKey, key.toString());
	}

	private String getPrefixedKeyForKey(Object key) {
		assert key != null;
		return makeKey(this.cacheKeyForKey, key.toString());
	}

	//
	private CacheEventListener eventListener;

	@Override
	public void setEventListener(CacheEventListener eventListener) {
		this.eventListener = eventListener;
		logger.debug("RedisCache[" + this.name + "] setEventListener .");
	}

	@Override
	public String getName() {
		return this.name;
	}

	public void expireAt(K key, Date date) {
		String prefixedKey = this.getPrefixedKey(key);
		this.redisTemplate.expireAt(prefixedKey, date);
	}

	@Override
	public V get(K key) {
		String prefixedKey = this.getPrefixedKey(key);
		V value = this.operations.get(prefixedKey);
		if (this.eventListener != null) {
			this.eventListener.onEvent(this.name, EventType.GET, key, value);
		}
		return value;
	}

	@Override
	public void put(K key, V value) {
		String prefixedKey = this.getPrefixedKey(key);
		this.operations.set(prefixedKey, value);
		//
		if (this.useKeySet) {
			String prefixedKeyForKey = this.getPrefixedKeyForKey(key);
			this.operationsForKey.set(prefixedKeyForKey, key);
		}
		//
		if (this.eventListener != null) {
			this.eventListener.onEvent(this.name, EventType.PUT, key, value);
		}
	}

	@Override
	public void evict(K key) {
		String prefixedKey = this.getPrefixedKey(key);
		this.redisTemplate.delete(prefixedKey);
		//
		if (this.useKeySet) {
			String prefixedKeyForKey = this.getPrefixedKeyForKey(key);
			this.redisTemplateForKey.delete(prefixedKeyForKey);
		}
		//
		if (this.eventListener != null) {
			this.eventListener.onEvent(this.name, EventType.EVICT, key, null);
		}
	}

	@Override
	public Set<K> keySet() {
		Set<K> retSet = new HashSet<K>();
		if (this.useKeySet) {
			String prefixedKeyForKey = this.getPrefixedKeyForKey("*");
			Set<String> tmpKeys = this.redisTemplateForKey.keys(prefixedKeyForKey);
			//
			for (String tmpKey : tmpKeys) {
				K valKey = this.operationsForKey.get(tmpKey);
				retSet.add(valKey);
			}
		}
		//
		return retSet;
	}

	@Override
	public void clear() {
		String prefixedKey = this.getPrefixedKey("*");
		Set<String> allKeys = this.redisTemplate.keys(prefixedKey);
		this.redisTemplate.delete(allKeys);
		//
		if (this.useKeySet) {
			String prefixedKeyForKey = this.getPrefixedKeyForKey("*");
			Set<String> allKeysForKey = this.redisTemplateForKey.keys(prefixedKeyForKey);
			this.redisTemplateForKey.delete(allKeysForKey);
		}
		//
		if (this.eventListener != null) {
			this.eventListener.onEvent(this.name, EventType.CLEAR, "*", null);
		}
	}
}
