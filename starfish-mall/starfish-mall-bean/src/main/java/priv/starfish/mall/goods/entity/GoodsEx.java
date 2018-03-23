package priv.starfish.mall.goods.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonShortDateTimeDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;

@Table(name = "goods_ex", uniqueConstraints = { @UniqueConstraint(fieldNames = { "goodsId", "specCode"}) })
public class GoodsEx implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(nullable = false, type = Types.INTEGER, desc = "商品Id")
	@ForeignKey(refEntityClass = Goods.class, refFieldName = "id")
	private Integer goodsId;

	@Column(nullable = false, type = Types.VARCHAR, length = 60, desc = "商品相关的规格code")
	private String specCode;

	@Column(nullable = false, type = Types.VARCHAR, length = 250, desc = "商品相关的规格枚举项id集合")
	private String specItemIds;

	@Column(nullable = false, type = Types.TIMESTAMP, desc = "时间戳")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date ts;
	
	public static GoodsEx newOne(){
		return new GoodsEx();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	public String getSpecCode() {
		return specCode;
	}

	public void setSpecCode(String specCode) {
		this.specCode = specCode;
	}

	public String getSpecItemIds() {
		return specItemIds;
	}

	public void setSpecItemIds(String specItemIds) {
		this.specItemIds = specItemIds;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	@Override
	public String toString() {
		return "GoodsEx [id=" + id + ", goodsId=" + goodsId + ", specCode=" + specCode + ", specItemIds=" + specItemIds + ", ts=" + ts + "]";
	}
	
}
