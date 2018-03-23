package priv.starfish.mall.order.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.json.JsonShortDateTimeDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;

/**
 * 销售订单内部金额
 * 
 * @author 邓华锋
 * @date 2015年11月17日 14:46:13
 *
 */
@Table(name = "sale_order_inner_amount")
public class SaleOrderInnerAmount implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id(type = Types.BIGINT)
	private Long id;

	@Column(name = "orderId", nullable = false, type = Types.BIGINT, desc = "订单ID")
	private Long orderId;

	@Column(name = "srcFlag", nullable = false, type = Types.INTEGER, desc = "1:ecard，2：余额，3：积分，4：优惠券，5：服务优惠券")
	private Integer srcFlag;

	@Column(name = "srcId", nullable = false, type = Types.BIGINT, desc = "关联支付类型的ID")
	private Long srcId;

	@Column(name = "amount", nullable = false, type = Types.DECIMAL,precision = 18, scale = 4, desc = "总价")
	private BigDecimal amount;

	@Column(name = "settleAmount", nullable = false, type = Types.DECIMAL, precision = 18, scale = 4,desc = "结算金额")
	private BigDecimal settleAmount;

	@Column(name = "extValInt", nullable = true, type = Types.INTEGER, desc = "扩展")
	private Integer extValInt;

	@Column(name = "extValLong", nullable = true, type = Types.BIGINT, desc = "扩展")
	private Long extValLong;

	@Column(name = "extValStr", nullable = true, type = Types.VARCHAR, length = 30, desc = "扩展")
	private String extValStr;

	@Column(name = "seqNo", nullable = false, type = Types.INTEGER, desc = "基于orderId")
	private Integer seqNo;

	@Column(name = "ts", nullable = false, type = Types.TIMESTAMP, desc = "")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date ts;

	/**
	 * 
	 */
	public Long getId() {
		return id;
	}

	/**
	 * 
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 订单ID
	 */
	public Long getOrderId() {
		return orderId;
	}

	/**
	 * 订单ID
	 */
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	/**
	 * 内部支付类型：1:ecard，2：余额，3：积分
	 */
	public Integer getSrcFlag() {
		return srcFlag;
	}

	/**
	 * 内部支付类型：1:ecard，2：余额，3：积分
	 */
	public void setSrcFlag(Integer srcFlag) {
		this.srcFlag = srcFlag;
	}

	/**
	 * 关联支付类型的ID
	 */
	public Long getSrcId() {
		return srcId;
	}

	/**
	 * 关联支付类型的ID
	 */
	public void setSrcId(Long srcId) {
		this.srcId = srcId;
	}

	/**
	 * 总价
	 */
	public BigDecimal getAmount() {
		return amount;
	}

	/**
	 * 总价
	 */
	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	/**
	 * 扩展
	 */
	public Integer getExtValInt() {
		return extValInt;
	}

	/**
	 * 扩展
	 */
	public void setExtValInt(Integer extValInt) {
		this.extValInt = extValInt;
	}

	/**
	 * 扩展
	 */
	public Long getExtValLong() {
		return extValLong;
	}

	/**
	 * 扩展
	 */
	public void setExtValLong(Long extValLong) {
		this.extValLong = extValLong;
	}

	/**
	 * 扩展
	 */
	public String getExtValStr() {
		return extValStr;
	}

	/**
	 * 扩展
	 */
	public void setExtValStr(String extValStr) {
		this.extValStr = extValStr;
	}

	/**
	 * 基于orderId
	 */
	public Integer getSeqNo() {
		return seqNo;
	}

	/**
	 * 基于orderId
	 */
	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	/**
	 * 
	 */
	public Date getTs() {
		return ts;
	}

	/**
	 * 
	 */
	public void setTs(Date ts) {
		this.ts = ts;
	}

	public BigDecimal getSettleAmount() {
		return settleAmount;
	}

	public void setSettleAmount(BigDecimal settleAmount) {
		this.settleAmount = settleAmount;
	}

	@Override
	public String toString() {
		return "SaleOrderInnerAmount [id=" + id + ", orderId=" + orderId + ", srcFlag=" + srcFlag + ", srcId=" + srcId + ", amount=" + amount + ", settleAmount=" + settleAmount + ", extValInt=" + extValInt + ", extValLong=" + extValLong
				+ ", extValStr=" + extValStr + ", seqNo=" + seqNo + ", ts=" + ts + "]";
	}
}