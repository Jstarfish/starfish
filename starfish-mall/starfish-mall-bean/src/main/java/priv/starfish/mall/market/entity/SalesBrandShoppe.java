package priv.starfish.mall.market.entity;

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
import priv.starfish.mall.categ.entity.BrandDef;

@Table(name = "sales_brand_shoppe", uniqueConstraints = { @UniqueConstraint(fieldNames = { "floorNo", "brandCode" }) }, desc = "销售品牌专柜")
public class SalesBrandShoppe implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;

	/** 楼层号 */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer floorNo;

	/** 品牌 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	@ForeignKey(refEntityClass = BrandDef.class, refFieldName = "code")
	private String brandCode;

	/** 链接地址 */
	@Column(nullable = false, type = Types.VARCHAR, length = 250)
	private String linkUrl;

	/** 序号 */
	@Column(nullable = false, type = Types.INTEGER, defaultValue = "1")
	private Integer seqNo;

	/** 时间戳 */
	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date ts;
	
	private BrandDef brandDef;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getFloorNo() {
		return floorNo;
	}

	public void setFloorNo(Integer floorNo) {
		this.floorNo = floorNo;
	}

	public String getBrandCode() {
		return brandCode;
	}

	public void setBrandCode(String brandCode) {
		this.brandCode = brandCode;
	}

	public String getLinkUrl() {
		return linkUrl;
	}

	public void setLinkUrl(String linkUrl) {
		this.linkUrl = linkUrl;
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

	public BrandDef getBrandDef() {
		return brandDef;
	}

	public void setBrandDef(BrandDef brandDef) {
		this.brandDef = brandDef;
	}

	@Override
	public String toString() {
		return "SalesBrandShoppe [id=" + id + ", floorNo=" + floorNo + ", brandCode=" + brandCode + ", linkUrl=" + linkUrl + ", seqNo=" + seqNo + ", ts=" + ts + "]";
	}

}
