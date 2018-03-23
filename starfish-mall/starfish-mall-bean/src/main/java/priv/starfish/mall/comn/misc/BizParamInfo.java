package priv.starfish.mall.comn.misc;

import java.util.List;

import priv.starfish.common.util.BoolUtil;
import priv.starfish.common.util.StrUtil;

public class BizParamInfo extends XParamInfo {
	/** 参数代码定义 */
	public final static class Code {
		public static final String loginRequireImageCode = "login.require.image.code";
		public static final String loginFailLockCount = "login.fail.lock.count";
		public static final String autoConfirmReceiveDays = "auto.confirm.receive.days";
		public static final String autoConfirmRefundDays = "auto.confirm.refund.days";
		public static final String maxMerchantShopCount = "max.merchant.shop.count";
		public static final String shopAuditFirstByAgent = "shop.audit.first.by.agent";
		public static final String afterSaleProtectionDays = "after.sale.protection.days";
		public static final String autoCancelUnpaidOrderHours = "auto.cancel.unpaid.order.hours";
		public static final String autoCancelReturnGoodsHours = "auto.cancel.return.goods.hours";
		public static final String allowShopToBindEcard = "allow.shop.to.bind.ecard";
		/** 默认商户结算周期（天） */
		public static final String defaultSettleXToMerchant = "default.settleX.to.merchant";
		/** 个人最多可拥有车辆数 */
		public static final String maxOwnedCarCount = "max.owned.car.count";
		/** 预约时间最大更改次数 */
		public static final String maxPlanTimeModifyTimes = "max.plantime.modify.times";
		/** 卫星店铺是否需要审核 */
		public static final String wxShopNeedToBeAuditted = "wxShop.need.to.be.auditted";
	}

	/** 登录是否需要图形验证码 */
	public static boolean loginRequireImageCode = false;
	/** 登录失败锁定次数 */
	public static int loginFailLockCount = 3;
	/** 自动确认收货期限 */
	public static int autoConfirmReceiveDays = 7;
	/** 自动同意退款期限 */
	public static int autoConfirmRefundDays = 7;
	/** 每个商户最大店铺数量 */
	public static int maxMerchantShopCount = 10;
	/** 店铺是否需要代理商初审 */
	public static boolean shopAuditFirstByAgent = false;
	/** 统一售后保障期限 */
	public static int afterSaleProtectionDays = 7;
	/** 未付订单自动取消时间 */
	public static int autoCancelUnpaidOrderHours = 24;
	/** 退货申请自动取消时间 */
	public static int autoCancelReturnGoodsHours = 24;
	/** 是否允许店铺绑定e卡 */
	public static boolean allowShopToBindEcard = false;
	/** 默认商户结算周期（天） */
	public static int defaultSettleXToMerchant = 7;
	/** 个人最多可拥有车辆数 */
	public static int maxOwnedCarCount = 5;
	/** 预约时间最大更改次数 */
	public static int maxPlanTimeModifyTimes = 2;
	/** 卫星店铺是否需要审核 */
	public static boolean wxShopNeedToBeAuditted = true;

	/*
	 * 商户日提现单笔限额（元） shop.draw.cash.amount.limit.per.trans int 商户日提现次数 shop.draw.cash.times.limit.per.day int
	 */
	/**
	 * 从实体填充
	 * 
	 * @author koqiui
	 * @date 2015年10月13日 下午3:28:06
	 * 
	 * @param bizParams
	 */
	public static void fromParams(List<? extends XParam> bizParams) {
		XParam tmpParam = null;
		String tmpValue = null;
		//
		tmpParam = findParamByCodeInList(bizParams, Code.loginRequireImageCode);
		if (tmpParam != null) {
			tmpValue = tmpParam.getValue();
			if (StrUtil.hasText(tmpValue)) {
				loginRequireImageCode = BoolUtil.isTrue(tmpValue.trim());
			}
		}
		//
		tmpParam = findParamByCodeInList(bizParams, Code.loginFailLockCount);
		if (tmpParam != null) {
			tmpValue = tmpParam.getValue();
			if (StrUtil.hasText(tmpValue)) {
				loginFailLockCount = Integer.parseInt(tmpValue.trim());
			}
		}
		//
		tmpParam = findParamByCodeInList(bizParams, Code.autoConfirmReceiveDays);
		if (tmpParam != null) {
			tmpValue = tmpParam.getValue();
			if (StrUtil.hasText(tmpValue)) {
				autoConfirmReceiveDays = Integer.parseInt(tmpValue.trim());
			}
		}
		//
		tmpParam = findParamByCodeInList(bizParams, Code.autoConfirmRefundDays);
		if (tmpParam != null) {
			tmpValue = tmpParam.getValue();
			if (StrUtil.hasText(tmpValue)) {
				autoConfirmRefundDays = Integer.parseInt(tmpValue.trim());
			}
		}
		//
		tmpParam = findParamByCodeInList(bizParams, Code.maxMerchantShopCount);
		if (tmpParam != null) {
			tmpValue = tmpParam.getValue();
			if (StrUtil.hasText(tmpValue)) {
				maxMerchantShopCount = Integer.parseInt(tmpValue.trim());
			}
		}
		//
		tmpParam = findParamByCodeInList(bizParams, Code.shopAuditFirstByAgent);
		if (tmpParam != null) {
			tmpValue = tmpParam.getValue();
			if (StrUtil.hasText(tmpValue)) {
				shopAuditFirstByAgent = BoolUtil.isTrue(tmpValue.trim());
			}
		}

		//
		tmpParam = findParamByCodeInList(bizParams, Code.afterSaleProtectionDays);
		if (tmpParam != null) {
			tmpValue = tmpParam.getValue();
			if (StrUtil.hasText(tmpValue)) {
				afterSaleProtectionDays = Integer.parseInt(tmpValue.trim());
			}
		}
		//
		tmpParam = findParamByCodeInList(bizParams, Code.autoCancelUnpaidOrderHours);
		if (tmpParam != null) {
			tmpValue = tmpParam.getValue();
			if (StrUtil.hasText(tmpValue)) {
				autoCancelUnpaidOrderHours = Integer.parseInt(tmpValue.trim());
			}
		}
		//
		tmpParam = findParamByCodeInList(bizParams, Code.autoCancelReturnGoodsHours);
		if (tmpParam != null) {
			tmpValue = tmpParam.getValue();
			if (StrUtil.hasText(tmpValue)) {
				autoCancelReturnGoodsHours = Integer.parseInt(tmpValue.trim());
			}
		}
		//
		tmpParam = findParamByCodeInList(bizParams, Code.allowShopToBindEcard);
		if (tmpParam != null) {
			tmpValue = tmpParam.getValue();
			if (StrUtil.hasText(tmpValue)) {
				allowShopToBindEcard = BoolUtil.isTrue(tmpValue.trim());
			}
		}
		//
		tmpParam = findParamByCodeInList(bizParams, Code.defaultSettleXToMerchant);
		if (tmpParam != null) {
			tmpValue = tmpParam.getValue();
			if (StrUtil.hasText(tmpValue)) {
				defaultSettleXToMerchant = Integer.parseInt(tmpValue.trim());
			}
		}
		//
		tmpParam = findParamByCodeInList(bizParams, Code.maxOwnedCarCount);
		if (tmpParam != null) {
			tmpValue = tmpParam.getValue();
			if (StrUtil.hasText(tmpValue)) {
				maxOwnedCarCount = Integer.parseInt(tmpValue.trim());
			}
		}
		//
		tmpParam = findParamByCodeInList(bizParams, Code.maxPlanTimeModifyTimes);
		if (tmpParam != null) {
			tmpValue = tmpParam.getValue();
			if (StrUtil.hasText(tmpValue)) {
				maxPlanTimeModifyTimes = Integer.parseInt(tmpValue.trim());
			}
		}
		//
		tmpParam = findParamByCodeInList(bizParams, Code.wxShopNeedToBeAuditted);
		if (tmpParam != null) {
			tmpValue = tmpParam.getValue();
			if (StrUtil.hasText(tmpValue)) {
				wxShopNeedToBeAuditted = !BoolUtil.isFalse(tmpValue.trim());
			}
		}
		// ...

	}

}
