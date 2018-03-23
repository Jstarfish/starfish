package priv.starfish.mall.comn.entity;

import java.io.Serializable;
import java.sql.Types;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonShortDateTimeSerializer;

/**
 * 区域表
 */
@Table(name = "area", uniqueConstraints = { @UniqueConstraint(fieldNames = { "name", "regionId" }) })
public class Area implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;

	/** code */
	@Column(type = Types.VARCHAR, length = 30)
	private String name;

	@Column(type = Types.INTEGER)
	private Integer regionId;

	/** 地区名称 */
	@Column(type = Types.VARCHAR, length = 60)
	private String regionName;

	@Column(type = Types.INTEGER)
	private Integer seqNo;

	@Column(type = Types.TIMESTAMP)
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private String ts;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getRegionId() {
		return regionId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public String getTs() {
		return ts;
	}

	public void setTs(String ts) {
		this.ts = ts;
	}

	@Override
	public String toString() {
		return "Area [id=" + id + ", name=" + name + ", regionId=" + regionId + ", regionName=" + regionName + ", seqNo=" + seqNo + ", ts=" + ts + "]";
	}

}
