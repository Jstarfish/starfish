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
import priv.starfish.common.json.JsonDateTimeSerializer;

@Table(name = "goods_cat_spec", uniqueConstraints = { @UniqueConstraint(fieldNames = { "catId", "refId" }) })
public class GoodsCatSpec implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;

	/** 商品分类id */
	@Column(nullable = false, type = Types.INTEGER)
	@ForeignKey(refEntityClass = GoodsCat.class, refFieldName = "id")
	private Integer catId;

	/** 规格参照id */
	@Column(nullable = false, type = Types.INTEGER)
	@ForeignKey(refEntityClass = SpecRef.class, refFieldName = "id")
	private Integer refId;

	/** 商品规格排序号 */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer seqNo;

	/** 商品分类属性分组id */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer groupId;

	/** 颜色标志 */
	@Column(type = Types.BOOLEAN)
	private Boolean colorFlag;

	/** 是否搜索 */
	@Column(type = Types.BOOLEAN, nullable = false, defaultValue = "false")
	private Boolean searchFlag;

	/** 日期 */
	@Column(nullable = false, type = Types.TIMESTAMP)
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date ts;

	private SpecRef specRef;

	private List<GoodsCatSpecItem> goodsCatSpecItems;

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

	public Boolean getColorFlag() {
		return colorFlag;
	}

	public void setColorFlag(Boolean colorFlag) {
		this.colorFlag = colorFlag;
	}

	public Boolean getSearchFlag() {
		return searchFlag;
	}

	public void setSearchFlag(Boolean searchFlag) {
		this.searchFlag = searchFlag;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public SpecRef getSpecRef() {
		return specRef;
	}

	public void setSpecRef(SpecRef specRef) {
		this.specRef = specRef;
	}

	public List<GoodsCatSpecItem> getGoodsCatSpecItems() {
		return goodsCatSpecItems;
	}

	public void setGoodsCatSpecItems(List<GoodsCatSpecItem> goodsCatSpecItems) {
		this.goodsCatSpecItems = goodsCatSpecItems;
	}

	@Override
	public String toString() {
		return "GoodsCatSpec [id=" + id + ", catId=" + catId + ", refId=" + refId + ", seqNo=" + seqNo + ", groupId=" + groupId + ", colorFlag=" + colorFlag + ", searchFlag=" + searchFlag + ", ts=" + ts + ", specRef=" + specRef
				+ ", goodsCatSpecItems=" + goodsCatSpecItems + "]";
	}

}
