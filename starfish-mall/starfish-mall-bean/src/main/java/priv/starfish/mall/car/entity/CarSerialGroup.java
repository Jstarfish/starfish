package priv.starfish.mall.car.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonDateTimeSerializer;
import priv.starfish.common.util.DateUtil;

@Table(name = "car_serial_group", uniqueConstraints = { @UniqueConstraint(fieldNames = { "brandId", "name" }) }, desc = "车系分组")
public class CarSerialGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@ForeignKey(refEntityClass = CarBrand.class, refFieldName = "id")
	@Column(nullable = false, type = Types.INTEGER, desc = "品牌id")
	private Integer brandId;

	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String name;

	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String py;

	@Column(nullable = false, type = Types.INTEGER)
	private Integer seqNo;

	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date ts;

	@Column(type = Types.INTEGER, desc = "外部id")
	private Integer refId;

	@Column(type = Types.VARCHAR, length = 30, desc = "外部name")
	private String refName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getBrandId() {
		return brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPy() {
		return py;
	}

	public void setPy(String py) {
		this.py = py;
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

	public Integer getRefId() {
		return refId;
	}

	public void setRefId(Integer refId) {
		this.refId = refId;
	}

	public String getRefName() {
		return refName;
	}

	public void setRefName(String refName) {
		this.refName = refName;
	}

	@Override
	public String toString() {
		return "CarSerialGroup [id=" + id + ", brandId=" + brandId + ", name=" + name + ", py=" + py + ", seqNo=" + seqNo + ", ts=" + DateUtil.toStdDateTimeStr(ts) + ", refId=" + refId + ", refName=" + refName + "]";
	}

}
