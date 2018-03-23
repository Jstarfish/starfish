package priv.starfish.mall.shop.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.json.JsonNullDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.mall.goods.entity.Product;

@Table(name = "shop_product")
public class ShopProduct implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(type = Types.BIGINT, desc="主键")
	private Long id;

	@Column(nullable = false, type = Types.INTEGER, desc="店铺id")
	@ForeignKey(refEntityClass = Shop.class, refFieldName = "id")
	private Integer shopId;
	
	@Column(nullable = false, type = Types.BIGINT, desc="产品id")
	@ForeignKey(refEntityClass = Product.class, refFieldName = "id")
	private Long productId;
	
	@Column(type = Types.VARCHAR, length = 60, desc = "显示名称：商品名车+规格")
	private String title;

	@Column(nullable = false, type = Types.INTEGER, desc="商品id")
	private Integer goodsId;

	@Column(nullable = false, type = Types.DECIMAL, precision = 18, scale = 4, desc="销售价格")
	private BigDecimal salePrice;
	
	@Column(nullable = false, type = Types.INTEGER, desc="商品分类id")
	private Integer catId;

	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false", desc="是否缺货标识")
	private Boolean lackFlag;
	
	@Column(nullable = false, type = Types.VARCHAR, length = 60, desc="商品名称")
	private String goodsName;

	@Column(nullable = false, type = Types.TIMESTAMP, desc="时间戳")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date ts;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public Integer getCatId() {
		return catId;
	}

	public void setCatId(Integer catId) {
		this.catId = catId;
	}

	public Boolean getLackFlag() {
		return lackFlag;
	}

	public void setLackFlag(Boolean lackFlag) {
		this.lackFlag = lackFlag;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		return "ShopProduct [id=" + id + ", shopId=" + shopId + ", productId=" + productId + ", title=" + title + ", goodsId=" + goodsId + ", salePrice=" + salePrice + ", catId=" + catId + ", lackFlag=" + lackFlag + ", goodsName="
				+ goodsName + ", ts=" + ts + "]";
	}

}
