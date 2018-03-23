package priv.starfish.mall.comn.entity;

import java.io.Serializable;
import java.sql.Types;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;

@Table(name = "bank", uniqueConstraints = { @UniqueConstraint(fieldNames = { "name" }) })
public class Bank implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键 银行代码 */
	@Id(auto = false, type = Types.VARCHAR, length = 15)
	private String code;

	/** 银行名称 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String name;

	/** 序号 */
	@Column(type = Types.INTEGER, defaultValue = "1")
	private Integer seqNo;

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

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	@Override
	public String toString() {
		return "Bank [code=" + code + ", name=" + name + ", seqNo=" + seqNo + "]";
	}

}
