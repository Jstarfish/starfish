package priv.starfish.mall.service.util;

import priv.starfish.mall.service.BaseConst;

/**
 * 常见业务代码生成方法
 * 
 * @author koqiui
 * @date 2015年10月20日 下午8:09:14
 *
 */
public final class CodeUtil {
	private CodeUtil() {
		// 禁止实例化
	}

	/**
	 * 根据mall.code生成新的店铺代码
	 * 
	 * @author koqiui
	 * @date 2015年10月20日 下午5:46:08
	 * 
	 * @param mallCode
	 * @return
	 */
	public static String newShopCode() {
		return BaseConst.MALL_CODE + "-dp-" + priv.starfish.common.util.CodeUtil.generateRandomCode(8);
	}

	/**
	 * 根据mall.code生成新的代理处代码
	 * 
	 * @author koqiui
	 * @date 2015年10月20日 下午5:46:40
	 * 
	 * @param mallCode
	 * @return
	 */
	public static String newAgencyCode() {
		return BaseConst.MALL_CODE + "-dl-" + priv.starfish.common.util.CodeUtil.generateRandomCode(8);
	}

	/**
	 * 根据mall.code生成新的供应商代码
	 * 
	 * @author koqiui
	 * @date 2015年10月20日 下午5:54:25
	 * 
	 * @return
	 */
	public static String newVendorCode() {
		return BaseConst.MALL_CODE + "-gy-" + priv.starfish.common.util.CodeUtil.generateRandomCode(8);
	}

	/**
	 * 完成确认码
	 * 
	 * @author koqiui
	 * @date 2015年10月26日 上午10:44:43
	 * 
	 * @return
	 */
	public static String newDoneCode() {
		return priv.starfish.common.util.CodeUtil.randomNumCode(6);
	}

}
