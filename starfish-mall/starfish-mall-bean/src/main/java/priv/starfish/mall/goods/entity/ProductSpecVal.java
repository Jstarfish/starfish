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
import priv.starfish.mall.categ.entity.SpecRef;

@Table(name = "product_spec_val", uniqueConstraints = { @UniqueConstraint(fieldNames = { "productId", "specId" }) })
public class ProductSpecVal implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.BIGINT)
	private Long id;
	
	/** 商品Id */
	@Column(nullable = false, type = Types.INTEGER)
	@ForeignKey(refEntityClass = Goods.class, refFieldName = "id")
	private Integer goodsId;
	
	/** 货品Id */
	@Column(nullable = false, type = Types.BIGINT)
	@ForeignKey(refEntityClass = Product.class, refFieldName = "id")
	private Long productId;
	
	/** 商品分类规格Id */
	@Column(nullable = false, type = Types.INTEGER)
	@ForeignKey(refEntityClass = GoodsCatSpec.class, refFieldName = "id")
	private Integer specId;
	
	/** 商品分类规格值 */
	@Column(nullable = false, type = Types.VARCHAR, length = 250)
	private String specVal;
	
	/** 颜色标志 */
	@Column(nullable = false, type = Types.BOOLEAN)
	private boolean colorFlag;
	
	/** 规格参照code */
	@Column(nullable = false, type = Types.VARCHAR, length = 60)
	private String refCode;
	
	/** 规格参照 */
	private SpecRef specRef;
	
	/** 商品分类规格枚举项id */
	@Column(type = Types.INTEGER)
	private Integer specItemId;
	
	/** 商品分类规格枚举项 */
	private GoodsCatSpecItem goodsCatSpecItem;

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

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public Integer getSpecId() {
		return specId;
	}

	public void setSpecId(Integer specId) {
		this.specId = specId;
	}

	public String getSpecVal() {
		return specVal;
	}

	public void setSpecVal(String specVal) {
		this.specVal = specVal;
	}

	public boolean isColorFlag() {
		return colorFlag;
	}

	public void setColorFlag(boolean colorFlag) {
		this.colorFlag = colorFlag;
	}

	public String getRefCode() {
		return refCode;
	}

	public void setRefCode(String refCode) {
		this.refCode = refCode;
	}

	public Integer getSpecItemId() {
		return specItemId;
	}

	public void setSpecItemId(Integer specItemId) {
		this.specItemId = specItemId;
	}

	public SpecRef getSpecRef() {
		return specRef;
	}

	public void setSpecRef(SpecRef specRef) {
		this.specRef = specRef;
	}

	public GoodsCatSpecItem getGoodsCatSpecItem() {
		return goodsCatSpecItem;
	}

	public void setGoodsCatSpecItem(GoodsCatSpecItem goodsCatSpecItem) {
		this.goodsCatSpecItem = goodsCatSpecItem;
	}

	@Override
	public String toString() {
		return "ProductSpecVal [id=" + id + ", goodsId=" + goodsId + ", productId=" + productId + ", specId=" + specId + ", specVal=" + specVal + ", colorFlag=" + colorFlag + ", refCode=" + refCode + ", specRef=" + specRef + ", specItemId="
				+ specItemId + ", goodsCatSpecItem=" + goodsCatSpecItem + "]";
	}

}
