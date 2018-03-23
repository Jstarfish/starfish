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

@Table(name = "car_model", uniqueConstraints = { @UniqueConstraint(fieldNames = { "serialId", "name" }) }, desc = "车辆型号")
public class CarModel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@ForeignKey(refEntityClass = CarSerial.class, refFieldName = "id")
	@Column(nullable = false, type = Types.INTEGER, desc = "车系id")
	private Integer serialId;

	@Column(nullable = false, type = Types.VARCHAR, length = 60, desc = "makeYear 款 + sweptVol L +  type")
	private String name;

	@Column(nullable = false, type = Types.VARCHAR, length = 60)
	private String py;

	@Column(type = Types.VARCHAR, length = 90, desc = "全称=品牌名称+系列名称+型号名称")
	private String fullName;

	@Column(type = Types.INTEGER, desc = "生产年份")
	private Integer makeYear;

	@Column(type = Types.NUMERIC, precision = 10, scale = 2, desc = "排量（单位L）")
	private Double sweptVol;

	@Column(type = Types.VARCHAR, length = 15, desc = "款式/型号")
	private String style;

	@Column(nullable = false, type = Types.INTEGER)
	private Integer seqNo;

	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false")
	private Boolean disabled;

	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date ts;

	@Column(type = Types.INTEGER, desc = "外部id")
	private Integer refId;

	@Column(type = Types.VARCHAR, length = 60, desc = "外部name")
	private String refName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getSerialId() {
		return serialId;
	}

	public void setSerialId(Integer serialId) {
		this.serialId = serialId;
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

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public Integer getMakeYear() {
		return makeYear;
	}

	public void setMakeYear(Integer makeYear) {
		this.makeYear = makeYear;
	}

	public Double getSweptVol() {
		return sweptVol;
	}

	public void setSweptVol(Double sweptVol) {
		this.sweptVol = sweptVol;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
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
		return "CarModel [id=" + id + ", serialId=" + serialId + ", name=" + name + ", py=" + py + ", fullName=" + fullName + ", makeYear=" + makeYear + ", sweptVol=" + sweptVol + ", style=" + style + ", seqNo=" + seqNo + ", disabled="
				+ disabled + ", ts=" + ts + ", refId=" + refId + ", refName=" + refName + "]";
	}

}
