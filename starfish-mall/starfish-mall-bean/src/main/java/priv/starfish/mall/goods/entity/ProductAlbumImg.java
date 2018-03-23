package priv.starfish.mall.goods.entity;

import java.io.Serializable;
import java.sql.Types;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;

@Table(name = "product_album_img", uniqueConstraints = { @UniqueConstraint(fieldNames = { "productId", "imageId"}) }, desc = "货品相册图片")
public class ProductAlbumImg implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.BIGINT)
	private Long id;

	/** 具体商品id */
	@Column(nullable = false, type = Types.BIGINT)
	@ForeignKey(refEntityClass = Product.class, refFieldName = "id")
	private Long productId;

	/** 商品相册图片id */
	@Column(nullable = false, type = Types.BIGINT)
	@ForeignKey(refEntityClass = GoodsAlbumImg.class, refFieldName = "id")
	private Long imageId;

	/** 序号 */
	@Column(nullable = false, type = Types.INTEGER, defaultValue = "1")
	private Integer seqNo;
	
	/** 图片浏览路径 */
	private String fileBrowseUrl;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}
	
	public String getFileBrowseUrl() {
		return fileBrowseUrl;
	}

	public void setFileBrowseUrl(String fileBrowseUrl) {
		this.fileBrowseUrl = fileBrowseUrl;
	}

	@Override
	public String toString() {
		return "ProductAlbumImg [id=" + id + ", productId=" + productId + ", imageId=" + imageId + ", seqNo=" + seqNo + ", fileBrowseUrl=" + fileBrowseUrl + "]";
	}

}
