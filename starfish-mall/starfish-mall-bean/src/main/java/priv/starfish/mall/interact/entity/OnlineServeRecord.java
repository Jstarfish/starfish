package priv.starfish.mall.interact.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.json.JsonDateTimeSerializer;
import priv.starfish.mall.comn.dict.AuthScope;

@Table(name = "online_serve_record", desc = "在线服务记录")
public class OnlineServeRecord implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.BIGINT)
	private Long id;

	/** 权限范围 */
	@Column(nullable = false, type = Types.VARCHAR, length = 15)
	private AuthScope scope;

	/** 对应的商户mall或店铺shop编号 */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer entityId;

	/** 对应的商户mall或店铺shop名称 */
	@Column(type = Types.VARCHAR, length = 30)
	private String entityName;

	/** 对应的用户编号 */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer servantId;

	/** 对应的用户名称 */
	@Column(type = Types.VARCHAR, length = 30)
	private String servantName;

	/** 会员编号 */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer memberId;

	/** 会员名称 */
	@Column(type = Types.VARCHAR, length = 30)
	private String memberName;

	/** 1 表示接收会员消息, 2 表示发送消息给会员 */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer directFlag;

	/** 内容 */
	@Column(nullable = false, type = Types.VARCHAR, length = 250)
	private String content;

	/** 接收时间 */
	@Column(type = Types.TIMESTAMP)
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date recvTime;

	/** 发送时间 */
	@Column(type = Types.TIMESTAMP)
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date sendTime;

	/** 冗余字段，便于日期过滤，格式为: yyyy-MM-dd */
	@Column(nullable = false, type = Types.VARCHAR, length = 10)
	private String dateStr;

	/** 是否已读（界面上接收到） */
	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false")
	private Boolean readFlag;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public String getServantName() {
		return servantName;
	}

	public void setServantName(String servantName) {
		this.servantName = servantName;
	}

	public Integer getMemberId() {
		return memberId;
	}

	public void setMemberId(Integer memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public Integer getDirectFlag() {
		return directFlag;
	}

	public void setDirectFlag(Integer directFlag) {
		this.directFlag = directFlag;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getRecvTime() {
		return recvTime;
	}

	public void setRecvTime(Date recvTime) {
		this.recvTime = recvTime;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public Boolean getReadFlag() {
		return readFlag;
	}

	public void setReadFlag(Boolean readFlag) {
		this.readFlag = readFlag;
	}

	@Override
	public String toString() {
		return "OnlineServeRecord [id=" + id + ", scope=" + scope + ", entityId=" + entityId + ", entityName=" + entityName + ", servantId=" + servantId + ", servantName=" + servantName + ", memberId=" + memberId + ", memberName="
				+ memberName + ", directFlag=" + directFlag + ", content=" + content + ", recvTime=" + recvTime + ", sendTime=" + sendTime + ", dateStr=" + dateStr + ", readFlag=" + readFlag + "]";
	}

}
