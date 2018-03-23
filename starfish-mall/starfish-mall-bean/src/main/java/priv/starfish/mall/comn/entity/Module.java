package priv.starfish.mall.comn.entity;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;

import java.io.Serializable;
import java.sql.Types;
import java.util.List;

@Table(name = "module", uniqueConstraints = { @UniqueConstraint(fieldNames = { "name" }) })
public class Module implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(auto = false, type = Types.INTEGER)
	private Integer id;

	/** 名字 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String name;

	/** 描述 */
	@Column(name = "`desc`", type = Types.VARCHAR, length = 250)
	private String desc;

	/** 序号 */
	@Column(nullable = false, type = Types.INTEGER, defaultValue = "1")
	private Integer seqNo;

	/** 系统权限集 */
	private List<Permission> permissions;

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

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public List<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<Permission> permissions) {
		this.permissions = permissions;
	}

	@Override
	public String toString() {
		return "Module [id=" + id + ", name=" + name + ", desc=" + desc + ", seqNo=" + seqNo + "]";
	}

}
