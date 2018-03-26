package priv.starfish.mall.redis;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import priv.starfish.common.cache.TraceCacheEventListener;
import priv.starfish.common.cache.redis.RedisCache;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.Assert.fail;

public class RedisCacheTest {
	static RedisConnectionFactory redisConnectionFactory;
	static TraceCacheEventListener cacheEventListener = new TraceCacheEventListener();
	private static ClassPathXmlApplicationContext appContext;

	@BeforeClass
	public static void setup() {
		String currentPkgPath = RedisCacheTest.class.getPackage().getName().toString().replace('.', '/');
		appContext = new ClassPathXmlApplicationContext("classpath:"  + "test-spring-redis-cache.xml");

		redisConnectionFactory = (RedisConnectionFactory) appContext.getBean("jedisConnectionFactory");
	}

	@Test
	public void testRedisCache() {
		RedisCache<Integer, Object> demoCache = new RedisCache<Integer, Object>("demo3");
		demoCache.setUseKeySet(true);
		demoCache.setConnectionFactory(redisConnectionFactory);
		demoCache.setEventListener(cacheEventListener);

		Map<String, Object> userInfo = new LinkedHashMap<String, Object>();
		userInfo.put("name", "koqiui");
		userInfo.put("age", 23);
		userInfo.put("marriage", true);
		userInfo.put("birthDate", new Date());
		System.out.println(userInfo);
		
		demoCache.put(12345, userInfo);
		
		Map<String, Object> userInfo2 = (Map<String, Object>) demoCache.get(12345);

		System.out.println("1 >> " + userInfo2);

		userInfo.put("xyz", 456.7);
		System.out.println(userInfo);
		
		userInfo2 = (Map<String, Object>) demoCache.get(12345);

		System.out.println("2 >> " + userInfo2);

		demoCache.put(67890, userInfo);

		userInfo = (Map<String, Object>) demoCache.get(67890);

		System.out.println("3 >> " + userInfo);
		
		System.out.println("keySet:" + demoCache.keySet());
	}

	@Test
	public void testSetConnectionFactory() {
		RedisCache<Integer, Object> demoCache = new RedisCache<Integer, Object>("demo");
		demoCache.setConnectionFactory(redisConnectionFactory);
	}

	@Test
	public void testSetEventListener() {
		RedisCache<Integer, Object> demoCache = new RedisCache<Integer, Object>("demo");
		demoCache.setEventListener(cacheEventListener);
	}

	@Test
	public void testGetName() {
		fail("Not yet implemented");
	}

	@Test
	public void testGet() {
		RedisCache<Integer, Object> demoCache = new RedisCache<Integer, Object>("demo");
		demoCache.setConnectionFactory(redisConnectionFactory);
		demoCache.setEventListener(cacheEventListener);

		Map<String, Object> userInfo = (Map<String, Object>) demoCache.get(12345);

		System.out.println(userInfo);
	}

	@Test
	public void testPut() {
		RedisCache<Integer, Object> demoCache = new RedisCache<Integer, Object>("demo");
		demoCache.setConnectionFactory(redisConnectionFactory);
		demoCache.setEventListener(cacheEventListener);

		Map<String, Object> userInfo = new LinkedHashMap<String, Object>();
		userInfo.put("name", "koqiui");
		userInfo.put("age", 23);
		userInfo.put("marriage", true);
		userInfo.put("birthDate", new Date());
		System.out.println(userInfo);

		demoCache.put(12345, userInfo);
	}

	@Test
	public void testEvict() {
		RedisCache<Integer, Object> demoCache = new RedisCache<Integer, Object>("demo");
		demoCache.setConnectionFactory(redisConnectionFactory);
		demoCache.setEventListener(cacheEventListener);

		demoCache.evict(12345);
		
		Map<String, Object> userInfo = (Map<String, Object>) demoCache.get(12345);

		System.out.println(userInfo);
	}

	@Test
	public void testClear() {
		RedisCache<Integer, Object> demoCache = new RedisCache<Integer, Object>("demo");
		demoCache.setConnectionFactory(redisConnectionFactory);
		demoCache.setEventListener(cacheEventListener);

		demoCache.clear();
	}

}
