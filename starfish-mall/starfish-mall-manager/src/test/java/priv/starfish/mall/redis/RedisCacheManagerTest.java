package priv.starfish.mall.redis;

import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import priv.starfish.common.cache.TraceCacheEventListener;
import priv.starfish.common.cache.redis.RedisCache;
import priv.starfish.common.cache.redis.RedisCacheManager;

import java.util.ArrayList;
import java.util.List;

public class RedisCacheManagerTest {
	static RedisConnectionFactory redisConnectionFactory;
	static TraceCacheEventListener cacheEventListener = new TraceCacheEventListener();

	@BeforeClass
	public static void setup() {
		String currentPkgPath = RedisCacheTest.class.getPackage().getName().toString().replace('.', '/');
		ClassPathXmlApplicationContext appContext = new ClassPathXmlApplicationContext("classpath:"  + "test-spring-redis-cache.xml");

		redisConnectionFactory = (RedisConnectionFactory) appContext.getBean("jedisConnectionFactory");
	}

	@Test
	public void test_x() {
		
	}
	
	@Test
	public void testSetCacheList() {
		RedisCacheManager cacheManager = new RedisCacheManager();
		List<RedisCache<?,?>> cacheList = new ArrayList<RedisCache<?,?>>();
		
		
		RedisCache<Integer, Object> demoCache = new RedisCache<Integer, Object>("demo");
		demoCache.setConnectionFactory(redisConnectionFactory);
		demoCache.setEventListener(cacheEventListener);
		
		
		cacheList.add(demoCache);
		
		//
		RedisCache<String, Object> demo2Cache = new RedisCache<String, Object>("demo2");
		demo2Cache.setConnectionFactory(redisConnectionFactory);
		demo2Cache.setEventListener(cacheEventListener);
		
		cacheList.add(demo2Cache);
		
		cacheManager.setCacheList(cacheList);
		
		
		System.out.println(cacheManager.getCacheNames());
	}
	
	@Test
	public void testAddCache() {
		RedisCacheManager cacheManager = new RedisCacheManager();
		RedisCache<Integer, Object> demoCache = new RedisCache<Integer, Object>("demoxx");
		demoCache.setConnectionFactory(redisConnectionFactory);
		demoCache.setEventListener(cacheEventListener);
		
		cacheManager.addCache(demoCache);
		
		//
		RedisCache<String, Object> demo2Cache = new RedisCache<String, Object>("demo2");
		demo2Cache.setConnectionFactory(redisConnectionFactory);
		demo2Cache.setEventListener(cacheEventListener);
		
		cacheManager.addCache(demo2Cache);
		
		System.out.println(cacheManager.getCacheNames());
		
		demo2Cache = (RedisCache<String, Object>) cacheManager.getCache("demo2");
		
		System.out.println(demo2Cache.getName());
	}

}
