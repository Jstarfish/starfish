package priv.starfish.mall.statis.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.mall.comn.entity.User;
import priv.starfish.mall.goods.entity.Product;

@Table(name = "goods_buy_sum", uniqueConstraints = { @UniqueConstraint(fieldNames = { "userId", "productId" }) })
public class GoodsBuySum implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.BIGINT)
	private Long id;

	/** 会员Id */
	@Column(nullable = false, type = Types.INTEGER)
	@ForeignKey(refEntityClass = User.class, refFieldName = "id")
	private Integer userId;

	@Column(type = Types.VARCHAR, length = 30, desc = "会员名称")
	private String userName;

	/** 商品Id */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer goodsId;

	/** 货品Id */
	@Column(nullable = false, type = Types.BIGINT)
	@ForeignKey(refEntityClass = Product.class, refFieldName = "id")
	private Long productId;

	@Column(type = Types.VARCHAR, length = 60, desc = "货品名称")
	private String productName;

	/** 商品分类Id */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer catId;

	/** 店铺Id */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer shopId;

	@Column(type = Types.VARCHAR, length = 30, desc = "店铺名称")
	private String shopName;

	@Column(nullable = false, type = Types.BIGINT, desc = "次数")
	private Long count;

	/** 最后购买时间 */
	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date lastTime;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public Integer getCatId() {
		return catId;
	}

	public void setCatId(Integer catId) {
		this.catId = catId;
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

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public Date getLastTime() {
		return lastTime;
	}

	public void setLastTime(Date lastTime) {
		this.lastTime = lastTime;
	}

	@Override
	public String toString() {
		return "GoodsBuySum [id=" + id + ", userId=" + userId + ", userName=" + userName + ", goodsId=" + goodsId + ", productId=" + productId + ", productName=" + productName + ", catId=" + catId + ", shopId=" + shopId + ", shopName="
				+ shopName + ", count=" + count + ", lastTime=" + lastTime + "]";
	}

}
