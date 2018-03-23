package priv.starfish.mall.xpay.channel.alipay.web;

import priv.starfish.common.base.AppNodeInfo;
import priv.starfish.common.config.PropertyConfigurer;
import priv.starfish.common.util.OSUtil;
import priv.starfish.mall.xpay.channel.alipay.BaseParams;

public class AlipayParams extends BaseParams {

	private static final String KEY_SUFFIX = ".web";
	//
	public static final String KEY_SERVICE = KEY_PREFIX + "service" + KEY_SUFFIX;
	public static final String KEY_SIGN_TYPE = KEY_PREFIX + "sign.type" + KEY_SUFFIX;
	public static final String KEY_GATEWAY = KEY_PREFIX + "gateway" + KEY_SUFFIX;
	public static final String KEY_SYNC_NOTIFY_URL = KEY_PREFIX + "sync.notify.url" + KEY_SUFFIX;
	public static final String KEY_ASYNC_NOTIFY_URL = KEY_PREFIX + "async.notify.url" + KEY_SUFFIX;
	public static final String KEY_REFUND_URL = KEY_PREFIX + "refund.url" + KEY_SUFFIX;
	public static final String KEY_TRANSFER_URL = KEY_PREFIX + "transfer.url" + KEY_SUFFIX;

	// ------------------------------
	public static String service;
	public static String signType;
	public static String gateway;
	private static String syncNotifyUrl;
	private static String asyncNotifyUrl;
	private static String refundUrl;
	private static String transferUrl;

	public static String getSyncNotifyUrl() {
		return AppNodeInfo.getCurrent().getAbsBaseUrl() + syncNotifyUrl;
	}

	public static String getAsyncNotifyUrl() {
		return AppNodeInfo.getCurrent().getAbsBaseUrl() + asyncNotifyUrl;
	}
	
	public static String getRefundUrl() {
		return AppNodeInfo.getCurrent().getAbsBaseUrl() + refundUrl;
	}
	
	public static String getTransferUrl() {
		return AppNodeInfo.getCurrent().getAbsBaseUrl() + transferUrl;
	}

	public static void clearConfig() {
		service = null;
		signType = null;
		gateway = null;
		syncNotifyUrl = null;
		asyncNotifyUrl = null;
		refundUrl = null;
		transferUrl = null;
	}

	public static void loadConfig(PropertyConfigurer configurer) {
		service = configurer.get(KEY_SERVICE);
		signType = configurer.get(KEY_SIGN_TYPE);
		gateway = configurer.get(KEY_GATEWAY);
		syncNotifyUrl = configurer.get(KEY_SYNC_NOTIFY_URL);
		asyncNotifyUrl = configurer.get(KEY_ASYNC_NOTIFY_URL);
		refundUrl = configurer.get(KEY_REFUND_URL);
		transferUrl = configurer.get(KEY_TRANSFER_URL);
	}

	public static void echo() {
		BaseParams.echo();
		//
		StringBuilder sb = new StringBuilder();
		sb.append(KEY_SERVICE).append("=").append(service).append(OSUtil.getLineSeparator());
		sb.append(KEY_SIGN_TYPE).append("=").append(signType).append(OSUtil.getLineSeparator());
		sb.append(KEY_GATEWAY).append("=").append(gateway).append(OSUtil.getLineSeparator());
		sb.append(KEY_SYNC_NOTIFY_URL).append("=").append(syncNotifyUrl).append(OSUtil.getLineSeparator());
		sb.append(KEY_ASYNC_NOTIFY_URL).append("=").append(asyncNotifyUrl).append(OSUtil.getLineSeparator());
		sb.append(KEY_REFUND_URL).append("=").append(refundUrl).append(OSUtil.getLineSeparator());
		sb.append(KEY_TRANSFER_URL).append("=").append(transferUrl).append(OSUtil.getLineSeparator());

		System.out.println(sb.toString());
	}
}
