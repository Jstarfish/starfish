package priv.starfish.mall.svcx.entity;

import java.io.Serializable;
import java.sql.Types;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;

@Table(name = "svc_kind", uniqueConstraints = { @UniqueConstraint(fieldNames = { "name" }) })
public class SvcKind implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(nullable = false, length = 15, type = Types.VARCHAR, desc = "服务名称")
	private String name;

	@Column(name = "`desc`", type = Types.VARCHAR, length = 250, desc = "服务描述")
	private String desc;

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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	@Override
	public String toString() {
		return "SvcKind [id=" + id + ", name=" + name + ", desc=" + desc + "]";
	}

}
