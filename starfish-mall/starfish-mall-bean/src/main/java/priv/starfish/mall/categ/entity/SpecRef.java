package priv.starfish.mall.categ.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonDateTimeSerializer;

@Table(name = "spec_ref", uniqueConstraints = { @UniqueConstraint(fieldNames = { "code" }) })
public class SpecRef implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;

	/** 规格参照code */
	@Column(nullable = false, type = Types.VARCHAR, length = 60)
	private String code;

	/** 规格参照名称 */
	@Column(nullable = false, type = Types.VARCHAR, length = 10)
	private String name;

	/** 规格参照描述 */
	@Column(name = "`desc`", type = Types.VARCHAR, length = 250)
	private String desc;

	/** 排序号 */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer seqNo;

	/** 是否颜色 */
	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false")
	private Boolean colorFlag;

	/** 是否销售专用 */
	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false")
	private Boolean salesFlag;

	/** 日期 */
	@Column(nullable = false, type = Types.TIMESTAMP)
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date ts;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public Boolean getColorFlag() {
		return colorFlag;
	}

	public void setColorFlag(Boolean colorFlag) {
		this.colorFlag = colorFlag;
	}

	public Boolean getSalesFlag() {
		return salesFlag;
	}

	public void setSalesFlag(Boolean salesFlag) {
		this.salesFlag = salesFlag;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	@Override
	public int hashCode() {
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof SpecRef && (id == ((SpecRef) obj).id);
	}

	@Override
	public String toString() {
		return "SpecRef [id=" + id + ", code=" + code + ", name=" + name + ", desc=" + desc + ", seqNo=" + seqNo + ", colorFlag=" + colorFlag + ", salesFlag=" + salesFlag + ", ts=" + ts + "]";
	}

}
