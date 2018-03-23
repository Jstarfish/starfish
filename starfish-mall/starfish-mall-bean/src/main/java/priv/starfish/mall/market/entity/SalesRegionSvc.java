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
import priv.starfish.mall.svcx.entity.SvcGroup;
import priv.starfish.mall.svcx.entity.Svcx;

@Table(name = "sales_region_svc", uniqueConstraints = { @UniqueConstraint(fieldNames = { "regionId", "svcId" }) }, desc = "销售专区服务")
public class SalesRegionSvc implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;

	/** 销售专区ID */
	@Column(type = Types.INTEGER, nullable = false)
	@ForeignKey(refEntityClass = SalesRegion.class, refFieldName = "id")
	private Integer regionId;

	/** 服务种类ID */
	@Column(type = Types.INTEGER, nullable = false)
	@ForeignKey(refEntityClass = SvcGroup.class, refFieldName = "id")
	private Integer groupId;

	/** 服务ID */
	@Column(type = Types.INTEGER, nullable = false)
	@ForeignKey(refEntityClass = Svcx.class, refFieldName = "id")
	private Integer svcId;

	/** 序号 */
	@Column(nullable = false, type = Types.INTEGER, defaultValue = "1")
	private Integer seqNo;

	/** 时间戳 */
	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date ts;

	private Svcx svcx;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRegionId() {
		return regionId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}

	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}

	public Integer getSvcId() {
		return svcId;
	}

	public void setSvcId(Integer svcId) {
		this.svcId = svcId;
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

	public Svcx getSvcx() {
		return svcx;
	}

	public void setSvcx(Svcx svcx) {
		this.svcx = svcx;
	}

	@Override
	public String toString() {
		return "SalesRegionSvc [id=" + id + ", regionId=" + regionId + ", groupId=" + groupId + ", svcId=" + svcId + ", seqNo=" + seqNo + ", ts=" + ts + ", svcx=" + svcx + "]";
	}

}
