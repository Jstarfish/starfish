package priv.starfish.mall.order.dto;

import java.io.Serializable;

import priv.starfish.mall.order.dict.OrderAction;
import priv.starfish.mall.order.dict.OrderType;

/**
 * 订单action结果信息（消息）
 * 
 * @author koqiui
 * @date 2016年2月1日 下午3:11:18
 *
 */
public class XOrderActionResult implements Serializable {
	private static final long serialVersionUID = 1L;
	//
	public OrderType orderType = OrderType.unknown;
	public Long orderId;
	public String orderNo;
	public Integer userId;
	public Integer shopId;
	public OrderAction orderAction;
	public Boolean orderActionResult;

	@Override
	public String toString() {
		return "XOrderActionResult [orderType=" + orderType + ", orderId=" + orderId + ", orderNo=" + orderNo + ", userId=" + userId + ", shopId=" + shopId + ", orderAction=" + orderAction + ", orderActionResult=" + orderActionResult + "]";
	}

}
