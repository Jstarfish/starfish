package priv.starfish.mall.interact.entity;

import java.io.Serializable;
import java.sql.Types;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.mall.comn.dict.AuthScope;

@Table(name = "online_serve_no", desc = "在线客服号")
public class OnlineServeNo implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键，客服编号 */
	@Id(type = Types.INTEGER)
	private Integer id;

	/** 权限范围 */
	@Column(nullable = false, type = Types.VARCHAR, length = 15)
	private AuthScope scope;

	/** 对应的商户mall或店铺shop编号 */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer entityId;

	/** 对应的商户mall或店铺shop名称 */
	@Column(type = Types.VARCHAR, length = 30)
	private String entityName;

	/** 对应 user.id */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer servantId;

	/** 客服编号 */
	@Column(type = Types.VARCHAR, length = 15)
	private String servantNo;

	/** 客服名称 */
	@Column(type = Types.VARCHAR, length = 30)
	private String servantName;

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

	public String getEntityName() {
		return entityName;
	}

	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public Integer getServantId() {
		return servantId;
	}

	public void setServantId(Integer servantId) {
		this.servantId = servantId;
	}

	public String getServantNo() {
		return servantNo;
	}

	public void setServantNo(String servantNo) {
		this.servantNo = servantNo;
	}

	public String getServantName() {
		return servantName;
	}

	public void setServantName(String servantName) {
		this.servantName = servantName;
	}

	@Override
	public String toString() {
		return "OnlineServeNo [id=" + id + ", scope=" + scope + ", entityId=" + entityId + ", entityName=" + entityName + ", servantId=" + servantId + ", servantNo=" + servantNo + ", servantName=" + servantName + "]";
	}

}
