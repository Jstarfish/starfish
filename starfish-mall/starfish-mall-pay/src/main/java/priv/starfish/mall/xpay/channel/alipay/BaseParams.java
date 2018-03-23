package priv.starfish.mall.xpay.channel.alipay;

import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import priv.starfish.common.config.PropertyConfigurer;
import priv.starfish.common.util.OSUtil;
import priv.starfish.common.util.StrUtil;

public class BaseParams {
	protected static final Log logger = LogFactory.getLog(BaseParams.class);

	protected static boolean loaded = false;

	protected static final ReentrantLock loadLock = new ReentrantLock();
	//

	protected static final String DEFAULT_CONFIG_FILE = "conf/alipay.properties";
	//
	protected static final String KEY_PREFIX = "alipay.";

	// 配置key
	public static final String KEY_PARTNER_ID = KEY_PREFIX + "partner.id";
	public static final String KEY_SELLER_ID = KEY_PREFIX + "seller.id";
	public static final String KEY_SELLER_EMAIL = KEY_PREFIX + "seller.email";
	public static final String KEY_KEY = KEY_PREFIX + "key";
	public static final String KEY_ACCOUNT_NAME = KEY_PREFIX + "account.name";
	public static final String KEY_INPUT_CHARSET = KEY_PREFIX + "input.charset";
	public static final String KEY_PAYMENT_TYPE = KEY_PREFIX + "payment.type";
	public static final String KEY_PRIVATE_KEY = KEY_PREFIX + "private.key";
	public static final String KEY_PUBLIC_KEY_OF_ALI = KEY_PREFIX + "public.key.of.ali";
	public static final String KEY_VERIFY_URL = KEY_PREFIX + "verify.url";

	// ------------------------------
	public static String parterId;

	public static String sellerId;

	public static String sellerEmail;

	public static String key;

	public static String accountName;

	public static String inputCharSet;

	public static String privateKey;

	public static String publicKeyOfAli;

	public static String verifyUrl;

	/**
	 * 清除配置
	 * 
	 * @author koqiui
	 * @date 2016年2月16日 下午6:32:33
	 *
	 */
	public static void clearConfig() {
		loadLock.lock();
		try {
			loaded = false;
			// 清除相应的配置
			parterId = null;
			sellerId = null;
			sellerEmail = null;
			key = null;
			accountName = null;
			inputCharSet = null;
			privateKey = null;
			publicKeyOfAli = null;
			verifyUrl = null;
			//
			// 同时清除web和mob配置
			priv.starfish.mall.xpay.channel.alipay.web.AlipayParams.clearConfig();
			priv.starfish.mall.xpay.channel.alipay.mob.AlipayParams.clearConfig();
			//
		} finally {
			loadLock.unlock();
		}
	}

	public static void loadConfig(String confFile) {
		if (StrUtil.isNullOrBlank(confFile)) {
			confFile = DEFAULT_CONFIG_FILE;
		}
		//
		loadLock.lock();
		try {
			PropertyConfigurer configurer = PropertyConfigurer.newInstance(confFile);
			// 加载配置
			parterId = configurer.get(KEY_PARTNER_ID);
			sellerId = configurer.get(KEY_SELLER_ID);
			sellerEmail = configurer.get(KEY_SELLER_EMAIL);
			key = configurer.get(KEY_KEY);
			accountName = configurer.get(KEY_ACCOUNT_NAME);
			inputCharSet = configurer.get(KEY_INPUT_CHARSET);
			privateKey = configurer.get(KEY_PRIVATE_KEY);
			publicKeyOfAli = configurer.get(KEY_PUBLIC_KEY_OF_ALI);
			verifyUrl = configurer.get(KEY_VERIFY_URL);
			//
			// 同时加载web和mob配置
			priv.starfish.mall.xpay.channel.alipay.web.AlipayParams.loadConfig(configurer);
			priv.starfish.mall.xpay.channel.alipay.mob.AlipayParams.loadConfig(configurer);
			//
			loaded = true;
		} catch (Exception ex) {
			logger.error(ex);
		} finally {
			loadLock.unlock();
		}
	}

	public static void loadConfig() {
		loadConfig(null);
	}

	public static void echo() {
		StringBuilder sb = new StringBuilder();
		sb.append(KEY_PARTNER_ID).append("=").append(parterId).append(OSUtil.getLineSeparator());
		sb.append(KEY_SELLER_ID).append("=").append(sellerId).append(OSUtil.getLineSeparator());
		sb.append(KEY_SELLER_EMAIL).append("=").append(sellerEmail).append(OSUtil.getLineSeparator());
		sb.append(KEY_KEY).append("=").append(key).append(OSUtil.getLineSeparator());
		sb.append(KEY_ACCOUNT_NAME).append("=").append(accountName).append(OSUtil.getLineSeparator());
		sb.append(KEY_INPUT_CHARSET).append("=").append(inputCharSet).append(OSUtil.getLineSeparator());
		sb.append(KEY_PRIVATE_KEY).append("=").append(privateKey).append(OSUtil.getLineSeparator());
		sb.append(KEY_PUBLIC_KEY_OF_ALI).append("=").append(publicKeyOfAli).append(OSUtil.getLineSeparator());
		sb.append(KEY_VERIFY_URL).append("=").append(verifyUrl).append(OSUtil.getLineSeparator());

		System.out.println(sb.toString());
	}
}
