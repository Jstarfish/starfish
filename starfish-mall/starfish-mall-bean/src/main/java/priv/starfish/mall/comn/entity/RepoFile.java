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
import priv.starfish.mall.comn.dict.AuthScope;

/**
 * 资源库文件（注册表）
 * 
 * @author koqiui
 * @date 2015年5月24日 下午9:10:17
 *
 */
@Table(name = "repo_file", uniqueConstraints = { @UniqueConstraint(fieldNames = { "uuid" }), @UniqueConstraint(fieldNames = { "usage", "relPath" }) })
public class RepoFile implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id(type = Types.BIGINT)
	private Long id;

	@Column(name = "`uuid`", nullable = false, type = Types.VARCHAR, length = 60, desc = "UIID")
	private String uuid;

	@Column(name = "`usage`", nullable = false, type = Types.VARCHAR, length = 30, desc = "文件用途")
	private String usage;

	@Column(nullable = false, type = Types.VARCHAR, length = 250, desc = "文件相对路径")
	private String relPath;

	@Column(type = Types.VARCHAR, length = 30, desc = "显示名称")
	private String dispName;

	@Column(type = Types.INTEGER, desc = "上传用户id")
	private Integer userId;

	@Column(type = Types.VARCHAR, length = 15)
	private AuthScope scope;

	@Column(type = Types.INTEGER)
	private Integer entityId;

	@Column(nullable = false, type = Types.TIMESTAMP)
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date ts;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

	public String getRelPath() {
		return relPath;
	}

	public void setRelPath(String relPath) {
		this.relPath = relPath;
	}

	public String getDispName() {
		return dispName;
	}

	public void setDispName(String dispName) {
		this.dispName = dispName;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
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

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	@Override
	public String toString() {
		return "RepoFile [id=" + id + ", uuid=" + uuid + ", usage=" + usage + ", relPath=" + relPath + ", dispName=" + dispName + ", userId=" + userId + ", scope=" + scope + ", entityId=" + entityId + ", ts=" + ts + "]";
	}

}
