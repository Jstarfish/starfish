package priv.starfish.mall.order.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Types;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.mall.categ.entity.GoodsCat;
import priv.starfish.mall.goods.entity.Goods;
import priv.starfish.mall.goods.entity.Product;

@Table(name = "sale_order_goods")
public class SaleOrderGoods implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id(type = Types.BIGINT)
	private Long id;

	@Column(nullable = false, type = Types.BIGINT, desc = "销售订单服务ID")
	@ForeignKey(refEntityClass = SaleOrder.class, refFieldName = "id")
	private Long orderId;
	
	@Column(nullable = false, type = Types.BIGINT, desc = "货品ID")
	@ForeignKey(refEntityClass = Product.class, refFieldName = "id")
	private Long productId;

	@Column(nullable = false, type = Types.INTEGER, desc = "商品分类ID")
	@ForeignKey(refEntityClass = GoodsCat.class, refFieldName = "id")
	private Integer catId;
	
	@Column(nullable = false, type = Types.INTEGER, desc = "商品ID")
	@ForeignKey(refEntityClass = Goods.class, refFieldName = "id")
	private Integer goodsId;

	@Column(nullable = false, type = Types.VARCHAR, desc = "货品名称")
	private String productName;

	//单价
	@Column(type = Types.DECIMAL, precision = 18, scale = 4, desc = "售价")
	private BigDecimal salePrice;
	
	@Column(nullable = false, type = Types.INTEGER, desc = "数量")
	private Integer quantity;
	//单价*数量
	@Column(type = Types.DECIMAL, precision = 18, scale = 4, desc = "销售金额")
	private BigDecimal saleAmount;
	
	@Column(type = Types.NUMERIC, precision = 8, scale = 2, desc = "直接打折的折扣率")
	private Double discRate;
	
	@Column(type = Types.VARCHAR, length = 15, desc = "扣减方式")
	private String discWay;

	@Column(type = Types.BIGINT, desc = "扣减ID")
	private Long discId;

	@Column(type = Types.DECIMAL, precision = 18, scale = 4, desc = "折扣金额")
	private BigDecimal discAmount;
	//=saleAmount-discAmount
	@Column(type = Types.DECIMAL, precision = 18, scale = 4, desc = "实际金额")
	private BigDecimal amount;

	@Column(nullable = false, type = Types.INTEGER, desc = "序号")
	private Integer seqNo;
	
	private Integer shopId;
	
	private String productAlbumImg;

	public String getProductAlbumImg() {
		return productAlbumImg;
	}

	public void setProductAlbumImg(String productAlbumImg) {
		this.productAlbumImg = productAlbumImg;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getCatId() {
		return catId;
	}

	public void setCatId(Integer catId) {
		this.catId = catId;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
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

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Double getDiscRate() {
		return discRate;
	}

	public void setDiscRate(Double discRate) {
		this.discRate = discRate;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	@Override
	public String toString() {
		return "SaleOrderGoods [id=" + id + ", orderId=" + orderId + ", productId=" + productId + ", catId=" + catId + ", goodsId=" + goodsId + ", productName=" + productName + ", salePrice=" + salePrice + ", quantity=" + quantity
				+ ", saleAmount=" + saleAmount + ", discRate=" + discRate + ", discWay=" + discWay + ", discId=" + discId + ", discAmount=" + discAmount + ", amount=" + amount + ", seqNo=" + seqNo + ", shopId=" + shopId
				+ ", productAlbumImg=" + productAlbumImg + "]";
	}
	
}
