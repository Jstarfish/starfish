package priv.starfish.mall.statis.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonDateTimeSerializer;
import priv.starfish.common.json.JsonNullDeserializer;
import priv.starfish.mall.goods.entity.Product;

/**
 * 店铺浏览数汇总
 * 
 * @author koqiui
 * @date 2016年1月22日 上午10:44:57
 *
 */
@Table(name = "goods_browse_sum", desc = "商品浏览汇总", uniqueConstraints = { @UniqueConstraint(fieldNames = { "userId", "productId" }) })
public class GoodsBrowseSum implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(type = Types.BIGINT)
	private Long id;

	/** 不要加外键引用，因为可能是未登录用户 */
	@Column(nullable = false, type = Types.INTEGER, desc = "会员Id（未登录用户为-1）")
	private Integer userId;

	@Column(type = Types.VARCHAR, length = 30, desc = "会员名称")
	private String userName;

	@Column(type = Types.INTEGER, desc = "商品Id")
	private Integer goodsId;

	@Column(nullable = false, type = Types.BIGINT, desc = "货品Id")
	@ForeignKey(refEntityClass = Product.class, refFieldName = "id")
	private Long productId;

	@Column(type = Types.VARCHAR, length = 60, desc = "货品名称")
	private String productName;

	@Column(type = Types.INTEGER, desc = "分类Id")
	private Integer catId;

	@Column(nullable = false, type = Types.BIGINT, desc = "次数", defaultValue = "0")
	private Long count;

	/** 最后购买时间 */
	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
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
		return "GoodsBrowseSum [id=" + id + ", userId=" + userId + ", userName=" + userName + ", goodsId=" + goodsId + ", productId=" + productId + ", productName=" + productName + ", catId=" + catId + ", count=" + count + ", lastTime="
				+ lastTime + "]";
	}

}
