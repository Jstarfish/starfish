package priv.starfish.mall.notify.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonShortDateTimeSerializer;

@Table(name = "tpl_model", uniqueConstraints = { @UniqueConstraint(fieldNames = { "code" }) }, desc = "模板（数据）模型")
public class TplModel implements Serializable {

	private static final long serialVersionUID = -3936296610683169643L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(type = Types.VARCHAR, length = 30, nullable = false)
	private String code;

	@Column(type = Types.VARCHAR, length = 30, nullable = false)
	private String name;

	// sample 为 示例模型的 json 字符串
	@Column(type = Types.CLOB, nullable = false)
	private String sample;

	@Column(type = Types.INTEGER, nullable = false, defaultValue = "1")
	private Integer seqNo;

	@Column(nullable = false, type = Types.TIMESTAMP)
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date ts;

	private List<TplModelVar> varList = new ArrayList<TplModelVar>(0);

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

	public String getSample() {
		return sample;
	}

	public void setSample(String sample) {
		this.sample = sample;
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

	public List<TplModelVar> getVarList() {
		return varList;
	}

	public void setVarList(List<TplModelVar> varList) {
		this.varList = varList;
	}

}
