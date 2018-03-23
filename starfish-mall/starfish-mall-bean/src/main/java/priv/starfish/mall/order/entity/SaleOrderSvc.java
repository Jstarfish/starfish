package priv.starfish.mall.order.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Types;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;

@Table(name = "sale_order_svc")
public class SaleOrderSvc implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id(type = Types.BIGINT)
	private Long id;

	@Column(nullable = false, type = Types.BIGINT, desc = "订单ID")
	@ForeignKey(refEntityClass = SaleOrder.class, refFieldName = "id")
	private Long orderId;

	@Column(nullable = false, type = Types.INTEGER, desc = "服务ID")
	private Integer svcId;

	@Column(nullable = false, type = Types.VARCHAR, desc = "服务名称")
	private String svcName;

	@Column(nullable = false, type = Types.INTEGER, desc = "序号")
	private Integer seqNo;

	@Column(type = Types.DECIMAL, precision = 18, scale = 4, desc = "售价")
	private BigDecimal salePrice;

	@Column(nullable = false, type = Types.DECIMAL, precision = 18, scale = 4, desc = "实际销售价")
	private BigDecimal saleAmount;
	
	/**
	 * （在distFlag被设置为true时设置区分用户派单和代理下单）
	 */
	@Column(type = Types.DECIMAL, precision = 18, scale = 4, desc = "分销金额")
	private BigDecimal distProfit;

	@Column(type = Types.NUMERIC, precision = 8, scale = 2, desc = "直接打折的折扣率")
	private BigDecimal discRate;

	@Column(type = Types.VARCHAR, length = 15, desc = "扣减方式")
	private String discWay;

	@Column(type = Types.BIGINT, desc = "扣减ID")
	private Long discId;

	@Column(type = Types.DECIMAL, precision = 18, scale = 4, desc = "当前服务折扣")
	private BigDecimal discAmount;

	@Column(nullable = false, type = Types.DECIMAL, precision = 18, scale = 4, desc = "")
	private BigDecimal amount;

	@Column(type = Types.INTEGER, desc = "套餐服务票ID")
	private Integer ticketId;

	// 服务图片
	private String svcxAlbumImg;

	private String svcxAlbumImgApp;

	// 服务描述
	private String svcDesc;

	public String getSvcxAlbumImgApp() {
		return svcxAlbumImgApp;
	}

	public void setSvcxAlbumImgApp(String svcxAlbumImgApp) {
		this.svcxAlbumImgApp = svcxAlbumImgApp;
	}

	public String getSvcDesc() {
		return svcDesc;
	}

	public void setSvcDesc(String svcDesc) {
		this.svcDesc = svcDesc;
	}

	public String getSvcxAlbumImg() {
		return svcxAlbumImg;
	}

	public void setSvcxAlbumImg(String svcxAlbumImg) {
		this.svcxAlbumImg = svcxAlbumImg;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Integer getSvcId() {
		return svcId;
	}

	public void setSvcId(Integer svcId) {
		this.svcId = svcId;
	}

	public String getSvcName() {
		return svcName;
	}

	public void setSvcName(String svcName) {
		this.svcName = svcName;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public BigDecimal getSaleAmount() {
		return saleAmount;
	}

	public void setSaleAmount(BigDecimal saleAmount) {
		this.saleAmount = saleAmount;
	}

	public Long getDiscId() {
		return discId;
	}

	public void setDiscId(Long discId) {
		this.discId = discId;
	}

	public String getDiscWay() {
		return discWay;
	}

	public void setDiscWay(String discWay) {
		this.discWay = discWay;
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

	public BigDecimal getDiscRate() {
		return discRate;
	}

	public void setDiscRate(BigDecimal discRate) {
		this.discRate = discRate;
	}

	public Integer getTicketId() {
		return ticketId;
	}

	public void setTicketId(Integer ticketId) {
		this.ticketId = ticketId;
	}

	public BigDecimal getDistProfit() {
		return distProfit;
	}

	public void setDistProfit(BigDecimal distProfit) {
		this.distProfit = distProfit;
	}

	@Override
	public String toString() {
		return "SaleOrderSvc [id=" + id + ", orderId=" + orderId + ", svcId=" + svcId + ", svcName=" + svcName + ", seqNo=" + seqNo + ", salePrice=" + salePrice + ", saleAmount=" + saleAmount + ", distProfit=" + distProfit + ", discRate="
				+ discRate + ", discWay=" + discWay + ", discId=" + discId + ", discAmount=" + discAmount + ", amount=" + amount + ", ticketId=" + ticketId + ", svcxAlbumImg=" + svcxAlbumImg + ", svcxAlbumImgApp=" + svcxAlbumImgApp
				+ ", svcDesc=" + svcDesc + "]";
	}
	
}
