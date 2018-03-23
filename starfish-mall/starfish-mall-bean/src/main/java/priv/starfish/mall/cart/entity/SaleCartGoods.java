package priv.starfish.mall.cart.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.mall.goods.entity.Goods;
import priv.starfish.common.annotation.*;
import priv.starfish.common.json.JsonShortDateTimeDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.mall.cart.dto.MiscAmountInfo;
import priv.starfish.mall.market.entity.UserCoupon;

@Table(name = "sale_cart_goods", uniqueConstraints = { @UniqueConstraint(fieldNames = { "cartId", "productId" }) })
public class SaleCartGoods implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(type = Types.BIGINT)
	private Long id;

	@Column(nullable = false, type = Types.INTEGER, desc = "购物车ID")
	@ForeignKey(refEntityClass = SaleCart.class, refFieldName = "id")
	private Integer cartId;

	@Column(nullable = false, type = Types.INTEGER, desc = "商品id")
	private Integer goodsId;

	@Column(nullable = false, type = Types.BIGINT, desc = "货品id")
	private Long productId;

	@Column(nullable = false, type = Types.INTEGER, desc = "数量")
	private Integer quantity;

	@Column(nullable = false, type = Types.BOOLEAN, desc = "是否选中", defaultValue = "true")
	private Boolean checkFlag;

	@Column(nullable = false, type = Types.TIMESTAMP, desc = "添加时间", defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date ts;

	// 商品信息
	private Goods goods;

	// 货品销售价
	private BigDecimal productAmount;

	// 货品名称
	private String productName;

	private String productAlbumImg;

	private MiscAmountInfo amountInfo;

	//核对订单里使用优惠券
	private UserCoupon userCoupon;

	public UserCoupon getUserCoupon() {
		return userCoupon;
	}

	public void setUserCoupon(UserCoupon userCoupon) {
		this.userCoupon = userCoupon;
	}

	public MiscAmountInfo getAmountInfo() {
		return amountInfo;
	}

	public void setAmountInfo(MiscAmountInfo amountInfo) {
		this.amountInfo = amountInfo;
	}

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

	public Integer getCartId() {
		return cartId;
	}

	public void setCartId(Integer cartId) {
		this.cartId = cartId;
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

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public Goods getGoods() {
		return goods;
	}

	public void setGoods(Goods goods) {
		this.goods = goods;
	}

	public BigDecimal getProductAmount() {
		return productAmount;
	}

	public void setProductAmount(BigDecimal productAmount) {
		this.productAmount = productAmount;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Boolean getCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(Boolean checkFlag) {
		this.checkFlag = checkFlag;
	}

	@Override
	public String toString() {
		return "SaleCartGoods [id=" + id + ", cartSvcId=" + cartId + ", goodsId=" + goodsId + ", productId=" + productId + ", quantity=" + quantity + ", checkBlag=" + checkFlag + ", ts=" + ts + ", goods=" + goods + ", productAmount="
				+ productAmount + ", productName=" + productName + ", productAlbumImg=" + productAlbumImg + ", amountInfo=" + amountInfo + ", userCoupon=" + userCoupon + "]";
	}

}