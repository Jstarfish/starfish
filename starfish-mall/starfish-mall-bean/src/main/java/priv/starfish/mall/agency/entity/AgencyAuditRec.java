package priv.starfish.mall.agency.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.json.JsonNullDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;

/**
 * 代理处审核记录表
 * 
 * @author WJJ
 * @date 2015年9月10日 下午3:44:07
 *
 */
@Table(name = "agency_audit_rec", desc = "代理处审核记录表")
public class AgencyAuditRec implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键==用户Id */
	@Id(type = Types.INTEGER)
	private Integer id;

	/** 代理处Id */
	@Column(nullable = false, type = Types.INTEGER)
	@ForeignKey(refEntityClass = Agency.class, refFieldName = "id")
	private Integer agencyId;

	/** 审核状态： 0 未审核, 1 审核通过， 2 审核未通过 */
	@Column(nullable = false, type = Types.INTEGER, defaultValue = "0")
	private Integer auditStatus;

	/** 审核时间 */
	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date auditTime;

	/** 审核人Id */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer auditorId;

	/** 审核人 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String auditorName;

	/** 描述 */
	@Column(name = "`desc`", type = Types.VARCHAR, length = 250)
	private String desc;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	public Date getAuditTime() {
		return auditTime;
	}

	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}

	public Integer getAuditorId() {
		return auditorId;
	}

	public void setAuditorId(Integer auditorId) {
		this.auditorId = auditorId;
	}

	public String getAuditorName() {
		return auditorName;
	}

	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getAgencyId() {
		return agencyId;
	}

	public void setAgencyId(Integer agencyId) {
		this.agencyId = agencyId;
	}

	@Override
	public String toString() {
		return "AgencyAuditRec [id=" + id + ", agencyId=" + agencyId + ", auditStatus=" + auditStatus + ", auditTime=" + auditTime + ", auditorId=" + auditorId + ", auditorName=" + auditorName + ", desc=" + desc + "]";
	}

}
