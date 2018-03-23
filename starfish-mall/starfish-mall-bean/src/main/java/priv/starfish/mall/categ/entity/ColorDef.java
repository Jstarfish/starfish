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

@Table(name = "color_def", uniqueConstraints = { @UniqueConstraint(fieldNames = { "name" }) })
public class ColorDef implements Serializable {
	private static final long serialVersionUID = -4588847584066011477L;

	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;
	/** 规格参照名称 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String name;
	/** 规格参照名称 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String expr;

	/** 排序号 */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer seqNo;

	/** 日期 */
	@Column(nullable = false, type = Types.TIMESTAMP)
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date ts;

	public ColorDef() {
		super();
	}

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

	public String getExpr() {
		return expr;
	}

	public void setExpr(String expr) {
		this.expr = expr;
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

	@Override
	public String toString() {
		return "ColorDef [id=" + id + ", name=" + name + ", expr=" + expr + ", seqNo=" + seqNo + ", ts=" + ts + "]";
	}

}
