package priv.starfish.mall.goods.entity;

import java.io.Serializable;
import java.sql.Types;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.mall.categ.entity.GoodsCatAttr;

@Table(name = "goods_attr_val", uniqueConstraints = { @UniqueConstraint(fieldNames = { "goodsId", "attrId", "attrVal" }) })
public class GoodsAttrVal implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.BIGINT)
	private Long id;
	
	/** 商品Id */
	@Column(nullable = false, type = Types.INTEGER)
	@ForeignKey(refEntityClass = Goods.class, refFieldName = "id")
	private Integer goodsId;
	
	/** 商品分类属性Id */
	@Column(nullable = false, type = Types.INTEGER)
	@ForeignKey(refEntityClass = GoodsCatAttr.class, refFieldName = "id")
	private Integer attrId;

	/** 商品分类属性值 */
	@Column(nullable = false, type = Types.VARCHAR, length = 250)
	private String attrVal;
	
	/** 关键属性标志 */
	@Column(nullable = false, type = Types.BOOLEAN)
	private boolean keyFlag;
	
	/** 属性参照code */
	@Column(nullable = false, type = Types.VARCHAR, length = 60)
	private String refCode;
	
	/** 商品分类属性枚举项id */
	@Column(type = Types.INTEGER)
	private Integer attrItemId;
	
	/** 品牌标志 */
	@Column(nullable = false, type = Types.BOOLEAN)
	private boolean brandFlag;

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

	public Integer getAttrId() {
		return attrId;
	}

	public void setAttrId(Integer attrId) {
		this.attrId = attrId;
	}

	public String getAttrVal() {
		return attrVal;
	}

	public void setAttrVal(String attrVal) {
		this.attrVal = attrVal;
	}

	public boolean isKeyFlag() {
		return keyFlag;
	}

	public void setKeyFlag(boolean keyFlag) {
		this.keyFlag = keyFlag;
	}

	public String getRefCode() {
		return refCode;
	}

	public void setRefCode(String refCode) {
		this.refCode = refCode;
	}

	public Integer getAttrItemId() {
		return attrItemId;
	}

	public void setAttrItemId(Integer attrItemId) {
		this.attrItemId = attrItemId;
	}

	public boolean isBrandFlag() {
		return brandFlag;
	}

	public void setBrandFlag(boolean brandFlag) {
		this.brandFlag = brandFlag;
	}

	@Override
	public String toString() {
		return "GoodsAttrVal [id=" + id + ", goodsId=" + goodsId + ", attrId=" + attrId + ", attrVal=" + attrVal + ", keyFlag=" + keyFlag + ", refCode=" + refCode + ", attrItemId=" + attrItemId + ", brandFlag=" + brandFlag + "]";
	}

}
