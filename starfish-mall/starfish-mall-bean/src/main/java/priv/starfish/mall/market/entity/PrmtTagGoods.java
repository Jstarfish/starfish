package priv.starfish.mall.market.entity;

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
import priv.starfish.mall.goods.entity.Product;

@Table(name = "prmt_tag_goods", uniqueConstraints = { @UniqueConstraint(fieldNames = { "tagId", "productId" }) }, desc = "促销标签商品")
public class PrmtTagGoods implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id(type = Types.BIGINT, desc="主键")
	private Long id;

	@Column(nullable = false, type = Types.INTEGER, desc="促销标签id")
	@ForeignKey(refEntityClass = PrmtTag.class, refFieldName = "id")
	private Integer tagId;

	@Column(nullable = false, type = Types.BIGINT, desc="货品id")
	@ForeignKey(refEntityClass = Product.class, refFieldName = "id")
	private Long productId;
	
	@Column(nullable = false, type = Types.INTEGER, desc="商品id")
	private Integer goodsId;
	
	@Column(nullable = false, type = Types.INTEGER, desc="店铺id")
	private Integer shopId;

	@Column(nullable = false, type = Types.INTEGER, defaultValue = "1", desc="序号")
	private Integer seqNo;

	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP", desc="时间戳")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date ts;
	
	private Product product;
	
	private PrmtTag prmtTag;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getTagId() {
		return tagId;
	}

	public void setTagId(Integer tagId) {
		this.tagId = tagId;
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

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public PrmtTag getPrmtTag() {
		return prmtTag;
	}

	public void setPrmtTag(PrmtTag prmtTag) {
		this.prmtTag = prmtTag;
	}

	@Override
	public String toString() {
		return "PrmtTagGoods [id=" + id + ", tagId=" + tagId + ", productId=" + productId + ", goodsId=" + goodsId
				+ ", shopId=" + shopId + ", seqNo=" + seqNo + ", ts=" + ts + ", product=" + product + ", prmtTag="
				+ prmtTag + "]";
	}

}
