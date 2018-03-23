package priv.starfish.mall.categ.entity;

import java.io.Serializable;
import java.sql.Types;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;

@Table(name = "goods_cat_spec_item", uniqueConstraints = { @UniqueConstraint(fieldNames = { "specId", "value" }) }, desc = "商品分类规格枚举值表")
public class GoodsCatSpecItem implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(type = Types.INTEGER, nullable = false)
	@ForeignKey(refEntityClass = GoodsCatSpec.class, refFieldName = "id")
	private Integer specId;

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

	public Integer getSpecId() {
		return specId;
	}

	public void setSpecId(Integer specId) {
		this.specId = specId;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue2() {
		return value2;
	}

	public void setValue2(String value2) {
		this.value2 = value2;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	@Override
	public String toString() {
		return "GoodsCatSpecItem [id=" + id + ", specId=" + specId + ", value=" + value + ", value2=" + value2 + ", seqNo=" + seqNo + "]";
	}

}
