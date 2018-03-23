package priv.starfish.mall.mall.entity;

import java.io.Serializable;
import java.sql.Types;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;

@Table(name = "operator", desc = "运营商")
public class Operator implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键， 非自增，与user.id相等 */
	@Id(auto = false, type = Types.INTEGER)
	private Integer id;

	/** 0：true启用；1：false禁用； */
	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false")
	private Boolean disabled;

	/** 备注 */
	@Column(type = Types.VARCHAR, length = 30)
	private String memo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Override
	public String toString() {
		return "Operator [id=" + id + ", disabled=" + disabled + ", memo=" + memo + "]";
	}

}
