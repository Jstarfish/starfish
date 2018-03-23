package priv.starfish.mall.market.entity;

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

@Table(name = "sales_floor", uniqueConstraints = { @UniqueConstraint(fieldNames = { "name" }) }, desc = "销售楼层")
public class SalesFloor implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Integer id;

	@Id(auto = false, type = Types.INTEGER, desc = "主键")
	private Integer no;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "名称")
	private String name;

	@Column(nullable = false, type = Types.INTEGER, desc = "专区类型")
	private Integer type;

	@Column(type = Types.INTEGER, desc = "分区数量")
	private Integer regionCount;

	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false", desc = "是否禁用")
	private Boolean disabled;

	/** 时间戳 */
	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date ts;

	/** 销售专区 */
	List<SalesRegion> salesRegions;
	
	/** 销售品牌专柜 */
	List<SalesBrandShoppe> salesBrandShoppes;

	public Integer getNo() {
		return no;
	}

	public void setNo(Integer no) {
		this.no = no;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getRegionCount() {
		return regionCount;
	}

	public void setRegionCount(Integer regionCount) {
		this.regionCount = regionCount;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public List<SalesRegion> getSalesRegions() {
		return salesRegions;
	}

	public void setSalesRegions(List<SalesRegion> salesRegions) {
		this.salesRegions = salesRegions;
	}

	public List<SalesBrandShoppe> getSalesBrandShoppes() {
		return salesBrandShoppes;
	}

	public void setSalesBrandShoppes(List<SalesBrandShoppe> salesBrandShoppes) {
		this.salesBrandShoppes = salesBrandShoppes;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "SalesFloor [id=" + id + ", no=" + no + ", name=" + name + ", type=" + type + ", regionCount=" + regionCount + ", disabled=" + disabled + ", ts=" + ts + ", salesRegions=" + salesRegions + ", salesBrandShoppes="
				+ salesBrandShoppes + "]";
	}

}
