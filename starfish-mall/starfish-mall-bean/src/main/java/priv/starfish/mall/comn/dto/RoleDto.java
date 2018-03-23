package priv.starfish.mall.comn.dto;

import java.util.Date;
import java.util.List;

import priv.starfish.mall.comn.dict.AuthScope;

public class RoleDto {

	private Integer id;

	private AuthScope scope;

	private Integer entityId;

	private String name;

	private String desc;

	private Boolean grantable;

	private Integer seqNo;

	private Date ts;

	List<Integer> subIdsAdded;

	List<Integer> subIdsDeleted;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public AuthScope getScope() {
		return scope;
	}

	public void setScope(AuthScope scope) {
		this.scope = scope;
	}

	public Integer getEntityId() {
		return entityId;
	}

	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
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

	public Boolean getGrantable() {
		return grantable;
	}

	public void setGrantable(Boolean grantable) {
		this.grantable = grantable;
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

	public List<Integer> getSubIdsAdded() {
		return subIdsAdded;
	}

	public void setSubIdsAdded(List<Integer> subIdsAdded) {
		this.subIdsAdded = subIdsAdded;
	}

	public List<Integer> getSubIdsDeleted() {
		return subIdsDeleted;
	}

	public void setSubIdsDeleted(List<Integer> subIdsDeleted) {
		this.subIdsDeleted = subIdsDeleted;
	}

}
