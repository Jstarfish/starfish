package priv.starfish.mall.service;

import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.notify.dict.SmsUsage;

/**
 * 常用常量
 * 
 * @author koqiui
 * @date 2015年11月12日 上午11:56:39
 *
 */
public class BaseConst {

	public static final boolean SITE_OPEN_TO_PUBLIC = true;
	/** 商城代码（初始化的固定值） */
	public static final String MALL_CODE = "ytcb";
	// 认证范围列表
	public static final AuthScope[] AUTH_SCOPE_LIST = new AuthScope[] { AuthScope.sys, AuthScope.mall, AuthScope.shop, AuthScope.agency, AuthScope.wxshop };

	// 短信验证码过期时间（按分钟）
	public static final int SMS_CODE_EXPIRE_TIME_IN_MINUTES = 30;

	// 每天每ip允许发送的短信条数
	public static final int SMS_MAX_COUNT_PER_DAY_PER_IP = 10;
	public static final SmsUsage[] SMS_LIMIT_USAGE_LIST = new SmsUsage[] { SmsUsage.regist, SmsUsage.logPass, SmsUsage.payPass, SmsUsage.rebind };

	// 订单action信息有效时间（按分钟）
	public static final int XORDER_ACTION_EXPIRE_TIME_IN_MINUTES = 10;

	public static class TopicNames {
		public static final String NODES = "ezmall.appNodes";
		public static final String CONFIG = "ezmall.config";
		public static final String RESLIST = "ezmall.resList";
		public static final String XORDER = "ezmall.xorder";
	}

	public static class QueueNames {
		public static final String CACHE = "ezmall.cache";
		public static final String XORDER = "ezmall.xorder";
		public static final String TASK = "ezmall.task";
	}
	
	public static class TaskFocus {
		public static final String PRODUCT = "product";
		public static final String GOODS_CAT = "goodsCat";
		public static final String SHOP = "shop";
		//
		public static final String DIST_SHOP = "distShop";
		//
		public static final String SALE_ORDER = "saleOrder";
		
	}

	public static class SubjectNames {
		public static final String MAIL = "mail";
		public static final String SMS = "sms";
		public static final String SETTLE_WAY = "settle-way";
		public static final String FREEMARKER = "freemarker";
		//

		public static final String PERMISSION = "permission";
		public static final String REGION_LIST = "region-list";
		public static final String RESOURCE = "resource";
		public static final String BRAND_DEF = "brand-def";
		public static final String COLOR_DEF = "color-def";
		//
		public static final String SYS_PARAM = "sys-param";
		public static final String BIZ_PARAM = "biz-param";
		public static final String MALL_INFO = "mall-info";
		//
		public static final String XORDER_ACTION = "xorder-action";
		//
	}

	public static class SmsCodes {
		public static final String NORMAL = "sms.normal";// 一般用途
		public static final String REGIST = "sms.regist";// 注册
		public static final String LOG_PASS = "sms.logPass";// 找回登录密码
		public static final String PAY_PASS = "sms.payPass";// 找回支付密码
		public static final String REBIND = "sms.rebind";// 重新绑定
		public static final String SALE_ORDER = "sms.sale.order";// 销售订单通知
		public static final String ECARD_ORDER = "sms.ecard.order";// e卡订单通知
		public static final String ECARD_TRANSFER = "sms.ecard.transfer";// e卡转赠
		public static final String ECARD_TRANSFER_NOTIFY_FROM = "sms.ecard.transfer.notify.from";// e卡转赠通知（赠送人）
		public static final String ECARD_TRANSFER_NOTIFY_TO = "sms.ecard.transfer.notify.to";// e卡转赠通知（受赠人）
		public static final String PROMOTE = "sms.promote";// 促销通知，无模板
		public static final String SECURITY = "sms.security";// 安全提醒，无模板
		public static final String SHOP_APPLY = "sms.shop.apply.result";// 申请店铺结果
		public static final String ORDER_PAY_RESULT = "sms.order.pay.result";// 订单支付结果
		public static final String HELP_REGIST_RESULT = "sms.regist.result";// 代理注册结果
	}

	public static class MailCodes {
		public static final String NORMAL = "mail.normal";// 一般用途
		public static final String REGIST = "mail.regist";// 注册
		public static final String VERIFY = "mail.verify";// 验证
		public static final String LOG_PASS = "mail.logPass";// 找回登录密码
		public static final String REBIND = "mail.rebind";// 重新绑定
		public static final String SALE_ORDER = "mail.sale.order";// 销售订单通知
		public static final String ECARD_ORDER = "mail.ecard.order";// e卡订单通知
		public static final String ECARD_TRANSFER = "mail.ecard.transfer";// e卡转赠
		public static final String PROMOTE = "mail.promote";// 促销通知
		public static final String SECURITY = "mail.security";// 安全提醒
		public static final String SHOP_APPLY = "mail.shop.apply.result";// 申请店铺结果
	}

	/** 常用模型变量名称 */
	public static class TplModelVars {
		public static final String CODE = "code";
		public static final String COMPANY = "company";
		public static final String EXPIRE_MINUTES = "expireMinutes";
		public static final String EXPIRE_TIME = "expireTime";
		public static final String ORDER_NO = "orderCode";
		public static final String SALE_ORDER_NO = "saleOrderNo";
		public static final String ECARD_ORDER_NO = "ecardOrderNo";
		public static final String ECARD_TYPE = "ecardType";
		// ..

	}
}
