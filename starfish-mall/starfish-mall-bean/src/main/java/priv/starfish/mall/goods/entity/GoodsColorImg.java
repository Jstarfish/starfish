package priv.starfish.mall.goods.entity;

import java.io.Serializable;
import java.sql.Types;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.mall.categ.entity.GoodsCatSpec;
import priv.starfish.mall.categ.entity.GoodsCatSpecItem;

@Table(name = "goods_color_img", uniqueConstraints = { @UniqueConstraint(fieldNames = { "goodsId", "specId","specItemId" }) })
public class GoodsColorImg implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.BIGINT)
	private Long id;
	
	/** 商品Id */
	@Column(nullable = false, type = Types.INTEGER)
	@ForeignKey(refEntityClass = Goods.class, refFieldName = "id")
	private Integer goodsId;
	
	/** 商品分类规格Id */
	@Column(nullable = false, type = Types.INTEGER)
	@ForeignKey(refEntityClass = GoodsCatSpec.class, refFieldName = "id")
	private Integer specId;
	
	/** 商品分类规格枚举项id */
	@Column(nullable = false, type = Types.INTEGER)
	@ForeignKey(refEntityClass = GoodsCatSpecItem.class, refFieldName = "id")
	private Integer specItemId;
	
	/** 商品相册Id */
	@Column(nullable = false, type = Types.BIGINT)
	@ForeignKey(refEntityClass = GoodsAlbumImg.class, refFieldName = "id")
	private Long imageId;
	
	/** 商品分类规格枚举项 */
	private GoodsCatSpecItem specItem;
	
	/** 规格值 */
	private String specVal;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public Integer getSpecId() {
		return specId;
	}

	public void setSpecId(Integer specId) {
		this.specId = specId;
	}

	public Integer getSpecItemId() {
		return specItemId;
	}

	public void setSpecItemId(Integer specItemId) {
		this.specItemId = specItemId;
	}

	public Long getImageId() {
		return imageId;
	}

	public void setImageId(Long imageId) {
		this.imageId = imageId;
	}

	public String getSpecVal() {
		return specVal;
	}

	public void setSpecVal(String specVal) {
		this.specVal = specVal;
	}

	public GoodsCatSpecItem getSpecItem() {
		return specItem;
	}

	public void setSpecItem(GoodsCatSpecItem specItem) {
		this.specItem = specItem;
	}

	@Override
	public String toString() {
		return "GoodsColorImg [id=" + id + ", goodsId=" + goodsId + ", specId=" + specId + ", specItemId=" + specItemId + ", imageId=" + imageId + ", specItem=" + specItem + ", specVal=" + specVal + "]";
	}

}
