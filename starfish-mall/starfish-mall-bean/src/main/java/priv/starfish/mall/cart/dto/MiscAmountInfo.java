package priv.starfish.mall.cart.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class MiscAmountInfo implements Serializable {
	private static final long serialVersionUID = 1L;

	// 小计（不扣减 当前本级小计）
	private BigDecimal saleAmount;

	// 折减金额（当前本级折减）
	private BigDecimal discAmount;

	// 金额（当前本级 saleAmount-discAmount）
	private BigDecimal amount;

	// 结算金额
	private BigDecimal settlePrice;

	public BigDecimal getSaleAmount() {
		return saleAmount;
	}

	public void setSaleAmount(BigDecimal saleAmount) {
		this.saleAmount = saleAmount;
	}

	public BigDecimal getDiscAmount() {
		return discAmount;
	}

	public void setDiscAmount(BigDecimal discAmount) {
		this.discAmount = discAmount;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getSettlePrice() {
		return settlePrice;
	}

	public void setSettlePrice(BigDecimal settlePrice) {
		this.settlePrice = settlePrice;
	}

	@Override
	public String toString() {
		return "VarietyAmountInfo [saleAmount=" + saleAmount + ", discAmount=" + discAmount + ", amount=" + amount + ", settlePrice=" + settlePrice + "]";
	}
}
