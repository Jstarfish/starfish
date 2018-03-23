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

@Table(name = "goods_cat_attr_group", uniqueConstraints = { @UniqueConstraint(fieldNames = { "catId", "name" }) })
public class GoodsCatAttrGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;

	/** 商品分类id */
	@Column(nullable = false, type = Types.INTEGER)
	@ForeignKey(refEntityClass = GoodsCat.class, refFieldName = "id")
	private Integer catId;

	/** 商城分类属性组名称 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String name;

	/** 商城分类属性组描述 */
	@Column(name = "`desc`", type = Types.VARCHAR, length = 250)
	private String desc;

	/** 排序号 */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer seqNo;

	/** 日期 */
	@Column(nullable = false, type = Types.TIMESTAMP)
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date ts;
	
	private List<GoodsCatAttr> goodsCatAttrs;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
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
    
	public List<GoodsCatAttr> getGoodsCatAttrs() {
		return goodsCatAttrs;
	}

	public void setGoodsCatAttrs(List<GoodsCatAttr> goodsCatAttrs) {
		this.goodsCatAttrs = goodsCatAttrs;
	}

	@Override
	public String toString() {
		return "GoodsCatAttrGroup [id=" + id + ", catId=" + catId + ", name=" + name + ", desc=" + desc + ", seqNo=" + seqNo + ", ts=" + ts + "]";
	}

}
