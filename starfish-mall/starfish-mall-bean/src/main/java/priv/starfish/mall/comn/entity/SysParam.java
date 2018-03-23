package priv.starfish.mall.comn.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.mall.comn.dict.DataType;
import priv.starfish.mall.comn.misc.XParam;

@Table(name = "sys_param", uniqueConstraints = { @UniqueConstraint(fieldNames = { "name" }) }, desc = "系统参数")
public class SysParam implements Serializable, XParam {

	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(auto = false, type = Types.VARCHAR, length = 90)
	private String code;

	/** 参数名称 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String name;

	/** 参数类型 */
	@Column(nullable = false, type = Types.VARCHAR, length = 15)
	private DataType type;

	/** 参数值 */
	@Column(nullable = false, type = Types.VARCHAR, length = 250)
	private String value;

	@Column(name = "`desc`", type = Types.VARCHAR, length = 250)
	private String desc;

	@Column(nullable = false, type = Types.INTEGER, defaultValue = "1")
	private Integer seqNo;

	/** 时间戳 */
	@Column(nullable = false, type = Types.TIMESTAMP)
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date ts;

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

	public DataType getType() {
		return type;
	}

	public void setType(DataType type) {
		this.type = type;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
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

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	@Override
	public String toString() {
		return "SysParam [code=" + code + ", name=" + name + ", type=" + type + ", value=" + value + ", desc=" + desc + ", seqNo=" + seqNo + ", ts=" + ts + "]";
	}

}
