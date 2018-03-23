package priv.starfish.mall.market.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.mall.goods.entity.Goods;
import priv.starfish.mall.goods.entity.Product;
import priv.starfish.mall.goods.entity.ProductAlbumImg;

@Table(name = "sales_region_goods", uniqueConstraints = { @UniqueConstraint(fieldNames = { "regionId", "productId" }) }, desc = "销售专区商品")
public class SalesRegionGoods implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;

	/** 销售专区ID */
	@Column(type = Types.INTEGER, nullable = false)
	@ForeignKey(refEntityClass = SalesRegion.class, refFieldName = "id")
	private Integer regionId;

	/** 商品ID */
	@Column(type = Types.INTEGER, nullable = false)
	@ForeignKey(refEntityClass = Goods.class, refFieldName = "id")
	private Integer goodsId;
	
	/** 货品ID */
	@Column(type = Types.BIGINT, nullable = false)
	@ForeignKey(refEntityClass = Product.class, refFieldName = "id")
	private Long productId;
	
	/** 序号 */
	@Column(nullable = false, type = Types.INTEGER, defaultValue = "1")
	private Integer seqNo;

	/** 时间戳 */
	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date ts;

	private Product product;
	
	/** 货品相册图片List */
	private List<ProductAlbumImg> productAlbumImgs;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRegionId() {
		return regionId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
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

	public List<ProductAlbumImg> getProductAlbumImgs() {
		return productAlbumImgs;
	}

	public void setProductAlbumImgs(List<ProductAlbumImg> productAlbumImgs) {
		this.productAlbumImgs = productAlbumImgs;
	}

	@Override
	public String toString() {
		return "SalesRegionGoods [id=" + id + ", regionId=" + regionId + ", goodsId=" + goodsId + ", productId=" + productId + ", seqNo=" + seqNo + ", ts=" + ts + ", product=" + product + ", productAlbumImgs=" + productAlbumImgs + "]";
	}

}
