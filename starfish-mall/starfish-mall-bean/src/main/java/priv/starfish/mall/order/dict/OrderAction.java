package priv.starfish.mall.order.dict;

import priv.starfish.common.annotation.AsEnumVar;

/**
 * 常用订单操作
 * 
 * @author koqiui
 * @date 2015年11月9日 下午2:34:44
 *
 */
@AsEnumVar()
public enum OrderAction {

	submit("提交"), pay("支付"), finish("完成"), cancel("取消"), postpone("推迟"), close("关闭"), //
	view("查看"), applyRefund("申请退款"), agreeRefund("同意退款"),refuseRefund("拒绝退款"), refund("退款"), //
	exStore("出库"), imStore("入库"), sendOut("发货"), takeIn("收货"), confirm("系统确认");

	private String text;

	private OrderAction(String text) {
		this.text = text;
	}

	public String getText() {
		return text;
	}

}
