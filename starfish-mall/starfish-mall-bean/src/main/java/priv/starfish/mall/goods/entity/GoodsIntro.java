package priv.starfish.mall.goods.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonShortDateTimeSerializer;

@Table(name = "goods_intro", uniqueConstraints = { @UniqueConstraint(fieldNames = { "goodsId" }) })
public class GoodsIntro implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;
	
	/** 商品Id */
	@Column(nullable = false, type = Types.INTEGER)
	@ForeignKey(refEntityClass = Goods.class, refFieldName = "id")
	private Integer goodsId;
	
	/** 商品介绍内容*/
	@Column(nullable = false, type = Types.CLOB)
	private String content;
	
	/** 时间戳 */
	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date ts;
	
	//
	public static GoodsIntro newOne() {
		return new GoodsIntro();
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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	@Override
	public String toString() {
		return "GoodsIntro [id=" + id + ", goodsId=" + goodsId + ", content=" + content + ", ts=" + ts + "]";
	}

}
