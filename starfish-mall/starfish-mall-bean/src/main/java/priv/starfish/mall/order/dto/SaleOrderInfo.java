package priv.starfish.mall.order.dto;

import priv.starfish.mall.cart.dto.SaleCartInfo;
import priv.starfish.mall.order.po.SaleOrderPo;

/**
 * 订单参数对象（用于下单、金额计算）
 * 
 * @author koqiui
 * @date 2015年10月20日 下午8:33:20
 *
 */
public class SaleOrderInfo {
	private Long saleOrderId;
	private String doneCode;
	private String saleOrderNo;
	private String payState;

	private SaleCartInfo saleCartInfo;

	private SaleOrderPo saleOrderPo;

	public static SaleOrderInfo newOne() {
		return new SaleOrderInfo();
	}

	public SaleCartInfo getSaleCartInfo() {
		return saleCartInfo;
	}

	public void setSaleCartInfo(SaleCartInfo saleCartInfo) {
		this.saleCartInfo = saleCartInfo;
	}

	public SaleOrderPo getSaleOrderPo() {
		return saleOrderPo;
	}

	public void setSaleOrderPo(SaleOrderPo saleOrderPo) {
		this.saleOrderPo = saleOrderPo;
	}

	public Long getSaleOrderId() {
		return saleOrderId;
	}

	public void setSaleOrderId(Long saleOrderId) {
		this.saleOrderId = saleOrderId;
	}

	public String getPayState() {
		return payState;
	}

	public void setPayState(String payState) {
		this.payState = payState;
	}

	public String getDoneCode() {
		return doneCode;
	}

	public void setDoneCode(String doneCode) {
		this.doneCode = doneCode;
	}

	public String getSaleOrderNo() {
		return saleOrderNo;
	}

	public void setSaleOrderNo(String saleOrderNo) {
		this.saleOrderNo = saleOrderNo;
	}

	@Override
	public String toString() {
		return "SaleOrderInfo [saleOrderId=" + saleOrderId + ", doneCode=" + doneCode + ", saleOrderNo=" + saleOrderNo + ", payState=" + payState + ", saleCartInfo=" + saleCartInfo + ", saleOrderPo=" + saleOrderPo + "]";
	}

}
