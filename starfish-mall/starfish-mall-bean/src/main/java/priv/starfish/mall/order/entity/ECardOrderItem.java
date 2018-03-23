package priv.starfish.mall.order.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Types;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.mall.ecard.entity.UserECard;

@Table(name = "ecard_record_item")
public class ECardOrderItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id(type = Types.INTEGER, desc = "主键 ")
	private Integer id;

	@Column(nullable = false, type = Types.INTEGER, desc = "e卡订单ID")
	@ForeignKey(refEntityClass = ECardOrder.class, refFieldName = "id")
	private Integer orderId;

	@Column(nullable = false, type = Types.VARCHAR, length = 60, desc = "卡号")
	private String cardNo;

	@Column(nullable = false, type = Types.DECIMAL, precision = 18, scale = 4, desc = "定价")
	private BigDecimal price;

	@Column(nullable = false, type = Types.DECIMAL, precision = 18, scale = 4, desc = "面额")
	private BigDecimal faceValue;

	@Column(nullable = false, type = Types.INTEGER, desc = "顺序号")
	private Integer seqNo;

	public static ECardOrderItem newOne() {
		return new ECardOrderItem();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getFaceValue() {
		return faceValue;
	}

	public void setFaceValue(BigDecimal faceValue) {
		this.faceValue = faceValue;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	@Override
	public String toString() {
		return "ECardOrderItem [id=" + id + ", cardId=" + orderId + ", cardNo=" + cardNo + ", price=" + price + ", faceValue=" + faceValue + ", seqNo=" + seqNo + "]";
	}

}
