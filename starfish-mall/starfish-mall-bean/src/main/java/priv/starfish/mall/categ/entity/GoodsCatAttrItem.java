package priv.starfish.mall.categ.entity;

import java.io.Serializable;
import java.sql.Types;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;

@Table(name = "goods_cat_attr_item", uniqueConstraints = { @UniqueConstraint(fieldNames = { "attrId", "value" }) }, desc = "商品分类属性枚举值表")
public class GoodsCatAttrItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(type = Types.INTEGER, nullable = false)
	@ForeignKey(refEntityClass = GoodsCatAttr.class, refFieldName = "id")
	private Integer attrId;

	@Column(type = Types.VARCHAR, length = 30, nullable = false)
	private String value;

	@Column(type = Types.VARCHAR, length = 30)
	private String value2;

	@Column(type = Types.INTEGER, nullable = false)
	private Integer seqNo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAttrId() {
		return attrId;
	}

	public void setAttrId(Integer attrId) {
		this.attrId = attrId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	@Override
	public String toString() {
		return "GoodsCatAttrItem [id=" + id + ", attrId=" + attrId + ", value=" + value + ", seqNo=" + seqNo + "]";
	}

}
