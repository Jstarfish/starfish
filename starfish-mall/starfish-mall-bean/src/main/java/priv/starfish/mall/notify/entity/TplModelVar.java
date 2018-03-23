package priv.starfish.mall.notify.entity;

import java.io.Serializable;
import java.sql.Types;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;

@Table(name = "tpl_model_var", uniqueConstraints = { @UniqueConstraint(fieldNames = { "modelCode", "expr" }) }, desc = "模板（数据）模型变量")
public class TplModelVar implements Serializable {

	private static final long serialVersionUID = 6378421150635433488L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(type = Types.VARCHAR, length = 30, nullable = false)
	@ForeignKey(refEntityClass = TplModel.class, refFieldName = "code")
	private String modelCode;

	@Column(type = Types.VARCHAR, length = 250, nullable = false)
	private String expr;

	@Column(name = "`desc`", type = Types.VARCHAR, length = 250)
	private String desc;

	@Column(type = Types.INTEGER, nullable = false, defaultValue = "1")
	private Integer seqNo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getExpr() {
		return expr;
	}

	public void setExpr(String expr) {
		this.expr = expr;
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

}
