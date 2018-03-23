package priv.starfish.mall.categ.entity;

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

/**
 * 商品分类属性表
 * 
 * @author 毛智东
 * @date 2015年5月27日 下午7:06:50
 * 
 */
@Table(name = "goods_cat_attr", uniqueConstraints = { @UniqueConstraint(fieldNames = { "catId", "refId" }) }, desc = "商品分类属性表")
public class GoodsCatAttr implements Serializable {

	private static final long serialVersionUID = -515348260548242614L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(type = Types.INTEGER, nullable = false)
	@ForeignKey(refEntityClass = GoodsCat.class, refFieldName = "id")
	private Integer catId;

	@Column(type = Types.INTEGER, nullable = false)
	@ForeignKey(refEntityClass = AttrRef.class, refFieldName = "id")
	private Integer refId;

	@Column(type = Types.VARCHAR, length = 15)
	private String unit;

	@Column(type = Types.BOOLEAN, nullable = false, defaultValue = "false")
	private Boolean keyFlag;

	@Column(type = Types.INTEGER, nullable = false)
	private Integer seqNo;

	@Column(type = Types.BOOLEAN, nullable = false, defaultValue = "false")
	private Boolean searchFlag;

	@Column(type = Types.BOOLEAN)
	private Boolean multiFlag;

	@Column(type = Types.BOOLEAN)
	private Boolean brandFlag;

	@Column(type = Types.INTEGER)
	private Integer groupId;

	@Column(type = Types.TIMESTAMP, nullable = false)
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date ts;

	private AttrRef attrRef;

	private List<GoodsCatAttrItem> goodsCatAttrItems;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCatId() {
		return catId;
	}

	public void setCatId(Integer catId) {
		this.catId = catId;
	}

	public Integer getRefId() {
		return refId;
	}

	public void setRefId(Integer refId) {
		this.refId = refId;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public Boolean getKeyFlag() {
		return keyFlag;
	}

	public void setKeyFlag(Boolean keyFlag) {
		this.keyFlag = keyFlag;
	}

	public Boolean getSearchFlag() {
		return searchFlag;
	}

	public void setSearchFlag(Boolean searchFlag) {
		this.searchFlag = searchFlag;
	}

	public Boolean getMultiFlag() {
		return multiFlag;
	}

	public void setMultiFlag(Boolean multiFlag) {
		this.multiFlag = multiFlag;
	}

	public Boolean getBrandFlag() {
		return brandFlag;
	}

	public void setBrandFlag(Boolean brandFlag) {
		this.brandFlag = brandFlag;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public AttrRef getAttrRef() {
		return attrRef;
	}

	public void setAttrRef(AttrRef attrRef) {
		this.attrRef = attrRef;
	}

	public List<GoodsCatAttrItem> getGoodsCatAttrItems() {
		return goodsCatAttrItems;
	}

	public void setGoodsCatAttrItems(List<GoodsCatAttrItem> goodsCatAttrItems) {
		this.goodsCatAttrItems = goodsCatAttrItems;
	}
}
