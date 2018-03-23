package priv.starfish.mall.categ.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.mall.comn.dict.DataType;
import priv.starfish.mall.goods.entity.GoodsAttrVal;

/**
 * 属性参照表
 * 
 * @author 毛智东
 * @date 2015年5月27日 下午5:50:10
 *
 */
@Table(name = "attr_ref", uniqueConstraints = { @UniqueConstraint(fieldNames = { "code" }), @UniqueConstraint(fieldNames = { "name" }) }, desc = "属性参照表")
public class AttrRef implements Serializable {

	private static final long serialVersionUID = -4491119481667001200L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(type = Types.VARCHAR, length = 60, nullable = false)
	private String code;

	@Column(type = Types.VARCHAR, length = 10, nullable = false)
	private String name;

	@Column(type = Types.VARCHAR, length = 15, nullable = false)
	private DataType type;

	@Column(type = Types.BOOLEAN, nullable = false, defaultValue = "false")
	private Boolean enumFlag;

	@Column(name = "`desc`", type = Types.VARCHAR, length = 250)
	private String desc;

	@Column(type = Types.INTEGER, nullable = false, defaultValue = "1")
	private Integer seqNo;

	@Column(nullable = false, type = Types.TIMESTAMP)
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date ts;

	// 是否多选，当type=enum时有效
	@Column(type = Types.BOOLEAN, defaultValue = "false")
	private Boolean multiFlag;

	// 是否为品牌，有且只有一个品牌属性
	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false")
	private Boolean brandFlag;
	
	private List<GoodsAttrVal> goodsAttrVals; 

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DataType getType() {
		return type;
	}

	public void setType(DataType type) {
		this.type = type;
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

	public Boolean getEnumFlag() {
		return enumFlag;
	}

	public void setEnumFlag(Boolean enumFlag) {
		this.enumFlag = enumFlag;
	}

	public Boolean getBrandFlag() {
		return brandFlag;
	}

	public void setBrandFlag(Boolean brandFlag) {
		this.brandFlag = brandFlag;
	}

	public Boolean getMultiFlag() {
		return multiFlag;
	}

	public void setMultiFlag(Boolean multiFlag) {
		this.multiFlag = multiFlag;
	}

	public List<GoodsAttrVal> getGoodsAttrVals() {
		return goodsAttrVals;
	}

	public void setGoodsAttrVals(List<GoodsAttrVal> goodsAttrVals) {
		this.goodsAttrVals = goodsAttrVals;
	}

}
