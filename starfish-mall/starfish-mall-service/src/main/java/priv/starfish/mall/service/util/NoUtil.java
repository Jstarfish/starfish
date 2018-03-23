package priv.starfish.mall.service.util;

import priv.starfish.common.util.CodeUtil;

/**
 * 常见业务编号生成方法
 * 
 * @author koqiui
 * @date 2015年10月20日 下午8:08:45
 *
 */
public final class NoUtil {
	private NoUtil() {
		// 禁止实例化
	}

	// ------------------------------ 订单编号 ------------------------------
	private static final int LENGTH_OF_ORDER_NO = 16;
	private static final int LENGTH_OF_TRANSFER_BILL_NO = 16;
	//
	public static final String PREFIX_OF_ORDER_NO = "O";
	public static final String PREFIX_OF_SALEORDER_NO = "S";
	public static final String PREFIX_OF_ECARDORDER_NO = "E";
	// 流水号
	private static final int LENGTH_OF_SERIAL_NO = 12;

	/**
	 * 生成新的订单编号（主要批发用）
	 * 
	 * @author koqiui
	 * @date 2015年10月20日 下午5:14:28
	 * 
	 * @return
	 */
	public static String newOrderNo() {
		return PREFIX_OF_ORDER_NO + CodeUtil.generateDatedNo(LENGTH_OF_ORDER_NO);
	}

	/**
	 * 生成新的销售订单编号
	 * 
	 * @author koqiui
	 * @date 2015年10月20日 下午5:14:56
	 * 
	 * @return
	 */
	public static String newSaleOrderNo() {
		return PREFIX_OF_SALEORDER_NO + CodeUtil.generateDatedNo(LENGTH_OF_ORDER_NO);
	}

	/**
	 * 生成新的e卡订单编号
	 * 
	 * @author koqiui
	 * @date 2015年10月20日 下午5:14:56
	 * 
	 * @return
	 */
	public static String newECardOrderNo() {
		return PREFIX_OF_ECARDORDER_NO + CodeUtil.generateDatedNo(LENGTH_OF_ORDER_NO);
	}

	// ---------------------------------- 转帐单编号----------------------------------

	public static String newTransferBillNo() {
		return CodeUtil.generateDatedNo(LENGTH_OF_TRANSFER_BILL_NO);
	}

	// ---------------------------------- 流水号(12位)----------------------------------

	public static String newSerialNo() {
		return newSerialNo(LENGTH_OF_SERIAL_NO);
	}

	public static String newSerialNo(int length) {
		return CodeUtil.generateDatedNo(length);
	}
}
