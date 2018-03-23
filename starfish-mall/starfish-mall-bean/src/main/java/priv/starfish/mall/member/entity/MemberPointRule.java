package priv.starfish.mall.member.entity;

import java.io.Serializable;
import java.sql.Types;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;

@Table(name = "member_point_rule", uniqueConstraints = { @UniqueConstraint(fieldNames = { "code" }) })
public class MemberPointRule implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;

	/** 代码 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String code;

	/** 积分或积分比例 */
	@Column(nullable = false, type = Types.FLOAT)
	private Float value;

	/** 描述 */
	@Column(name = "`desc`", type = Types.VARCHAR, length = 250)
	private String desc;

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

	public Float getValue() {
		return value;
	}

	public void setValue(Float value) {
		this.value = value;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return "MemberPointRule [id=" + id + ", code=" + code + ", value=" + value + ", desc=" + desc + "]";
	}

}
