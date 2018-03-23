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

@Table(name = "sales_region", uniqueConstraints = { @UniqueConstraint(fieldNames = { "name", "floorNo" }) }, desc = "销售专区")
public class SalesRegion implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;

	/** 楼层号 */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer floorNo;
	
	/** 销售类型 */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer type;

	/** 名称 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String name;

	/** 序号 */
	@Column(nullable = false, type = Types.INTEGER, defaultValue = "1")
	private Integer seqNo;

	/** 时间戳 */
	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date ts;

	List<SalesRegionGoods> salesRegionGoods;
	
	List<SalesRegionSvc> salesRegionSvc;
	
	SalesFloor salesFloor;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public List<SalesRegionGoods> getSalesRegionGoods() {
		return salesRegionGoods;
	}

	public void setSalesRegionGoods(List<SalesRegionGoods> salesRegionGoods) {
		this.salesRegionGoods = salesRegionGoods;
	}

	public SalesFloor getSalesFloor() {
		return salesFloor;
	}

	public void setSalesFloor(SalesFloor salesFloor) {
		this.salesFloor = salesFloor;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public List<SalesRegionSvc> getSalesRegionSvc() {
		return salesRegionSvc;
	}

	public void setSalesRegionSvc(List<SalesRegionSvc> salesRegionSvc) {
		this.salesRegionSvc = salesRegionSvc;
	}

	@Override
	public String toString() {
		return "SalesRegion [id=" + id + ", floorNo=" + floorNo + ", type=" + type + ", name=" + name + ", seqNo=" + seqNo + ", ts=" + ts + ", salesRegionGoods=" + salesRegionGoods + ", salesRegionSvc=" + salesRegionSvc + ", salesFloor="
				+ salesFloor + "]";
	}

}
