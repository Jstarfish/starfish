package priv.starfish.mall.svcx.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.List;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;

@Table(name = "svc_group", uniqueConstraints = { @UniqueConstraint(fieldNames = { "name" }) })
public class SvcGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(nullable = false, type = Types.INTEGER, desc = "所属种类主键")
	@ForeignKey(refEntityClass = SvcKind.class, refFieldName = "id")
	private Integer kindId;

	@Column(nullable = false, type = Types.VARCHAR, desc = "分组名称")
	private String name;

	@Column(name = "`desc`", type = Types.VARCHAR, length = 250, desc = "分组描述")
	private String desc;

	@Column(nullable = false, type = Types.INTEGER, desc = "排序号")
	private Integer seqNo;

	@Column(nullable = false, type = Types.BOOLEAN, desc = "是否不可用", defaultValue = "false")
	private Boolean disabled;

	@Column(nullable = false, type = Types.BOOLEAN, desc = "是否删除", defaultValue = "false")
	private Boolean deleted;

	// 车辆服务
	private List<Svcx> svcxList;

	private String kindName;

	public List<Svcx> getSvcxList() {
		return svcxList;
	}

	public void setCarSvcList(List<Svcx> svcxList) {
		this.svcxList = svcxList;
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

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public void setSvcxList(List<Svcx> svcxList) {
		this.svcxList = svcxList;
	}

	public Integer getKindId() {
		return kindId;
	}

	public void setKindId(Integer kindId) {
		this.kindId = kindId;
	}

	public String getKindName() {
		return kindName;
	}

	public void setKindName(String kindName) {
		this.kindName = kindName;
	}

	@Override
	public String toString() {
		return "SvcGroup [id=" + id + ", kindId=" + kindId + ", name=" + name + ", desc=" + desc + ", seqNo=" + seqNo + ", disabled=" + disabled + ", deleted=" + deleted + ", svcxList=" + svcxList + ", kindName=" + kindName + "]";
	}

}
