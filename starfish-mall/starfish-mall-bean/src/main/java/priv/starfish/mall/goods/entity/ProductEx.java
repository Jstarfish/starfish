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
import priv.starfish.common.json.JsonShortDateTimeDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;

@Table(name = "product_ex")
public class ProductEx implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id(type = Types.BIGINT, auto = false, desc = "非自增，货品Id")
	private Long id;

	@Column(nullable = false, type = Types.INTEGER, desc = "商品Id")
	@ForeignKey(refEntityClass = Goods.class, refFieldName = "id")
	private Integer goodsId;

	@Column(nullable = false, type = Types.CLOB, desc = "排序之后的货品规格值集合")
	private String specStr;

	@Column(nullable = false, type = Types.CLOB, desc = "排序之后的货品规格值集合,json格式")
	private String specJson;

	@Column(nullable = false, type = Types.TIMESTAMP, desc = "时间戳")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date ts;

	public static ProductEx newOne() {
		return new ProductEx();
	}

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

	public String getSpecStr() {
		return specStr;
	}

	public void setSpecStr(String specStr) {
		this.specStr = specStr;
	}

	public String getSpecJson() {
		return specJson;
	}

	public void setSpecJson(String specJson) {
		this.specJson = specJson;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	@Override
	public String toString() {
		return "ProductEx [id=" + id + ", goodsId=" + goodsId + ", specStr=" + specStr + ", specJson=" + specJson + ", ts=" + ts + "]";
	}

}
