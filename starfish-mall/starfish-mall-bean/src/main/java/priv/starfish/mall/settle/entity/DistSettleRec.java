package priv.starfish.mall.settle.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.json.JsonNullDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.mall.order.entity.SaleOrder;

/**
 * 卫星店结算记录
 * 
 * @author "WJJ"
 * @date 2016年2月22日 上午10:37:50
 *
 */
@Table(name = "dist_settle_rec")
public class DistSettleRec implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(type = Types.INTEGER, desc = "主键 ")
	private Integer id;

	@Column(nullable = false, type = Types.INTEGER)
	private Integer shopId;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "冗余字段")
	private String shopName;

	@Column(nullable = false, type = Types.INTEGER, desc = "= dist_shop.id = user.id")
	private Integer distributorId;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "冗余字段")
	private String distributorName;

	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String distShopName;

	@Column(nullable = false, type = Types.DECIMAL, precision = 18, scale = 4, desc = "应结算金额")
	private BigDecimal amount;

	@Column(nullable = false, type = Types.TIMESTAMP)
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date theTime;

	@Column(nullable = false, type = Types.DECIMAL, precision = 18, scale = 4, desc = "实际结算金额")
	private BigDecimal theAmount;

	@Column(nullable = false, type = Types.INTEGER, desc = "结算涵盖的订单数（冗余字段，保存时计算）")
	private Integer theCount;

	@Column(name = "`desc`", type = Types.VARCHAR, length = 250)
	private String desc;

	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date ts;

	/** 关联订单 */
	private List<SaleOrder> saleOrder;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public Integer getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(Integer distributorId) {
		this.distributorId = distributorId;
	}

	public String getDistributorName() {
		return distributorName;
	}

	public void setDistributorName(String distributorName) {
		this.distributorName = distributorName;
	}

	public String getDistShopName() {
		return distShopName;
	}

	public void setDistShopName(String distShopName) {
		this.distShopName = distShopName;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Date getTheTime() {
		return theTime;
	}

	public void setTheTime(Date theTime) {
		this.theTime = theTime;
	}

	public BigDecimal getTheAmount() {
		return theAmount;
	}

	public void setTheAmount(BigDecimal theAmount) {
		this.theAmount = theAmount;
	}

	public Integer getTheCount() {
		return theCount;
	}

	public void setTheCount(Integer theCount) {
		this.theCount = theCount;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public List<SaleOrder> getSaleOrder() {
		return saleOrder;
	}

	public void setSaleOrder(List<SaleOrder> saleOrder) {
		this.saleOrder = saleOrder;
	}

	@Override
	public String toString() {
		return "DistSettleRec [id=" + id + ", shopId=" + shopId + ", shopName=" + shopName + ", distributorId=" + distributorId + ", distributorName=" + distributorName + ", distShopName=" + distShopName + ", amount=" + amount
				+ ", theTime=" + theTime + ", theAmount=" + theAmount + ", theCount=" + theCount + ", desc=" + desc + ", ts=" + ts + ", saleOrder=" + saleOrder + "]";
	}

}
