package priv.starfish.mall.social.entity;

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
import priv.starfish.mall.comn.entity.User;

/**
 * 用户反馈
 * 
 * @author 邓华锋
 * @date 2016年01月12日 11:14:22
 *
 */
@Table(name = "user_feedback")
public class UserFeedback implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(type = Types.BIGINT)
	private Long id;

	@Column(name = "userId", nullable = false, type = Types.INTEGER, desc = "发表者ID")
	@ForeignKey(refEntityClass = User.class, refFieldName = "id")
	private Integer userId;

	@Column(name = "userName", nullable = true, type = Types.VARCHAR, length = 30, desc = "用户名称")
	private String userName;

	@Column(name = "userType", nullable = false, type = Types.INTEGER, desc = "userType => 1: 会员, 2: 商户, 3:代理, 4 : 平台（商城）")
	private Integer userType;

	@Column(name = "appType", nullable = false, type = Types.VARCHAR, length = 15, desc = "应用类型")
	private String appType;

	@Column(name = "subject", nullable = false, type = Types.VARCHAR, length = 15, desc = "标题,mobapp : 手机App, webapp web站点 看SubjectType枚举")
	private String subject;

	@Column(name = "content", nullable = false, type = Types.VARCHAR, length = 250, desc = "反馈内容")
	private String content;

	@Column(name = "sendTime", nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP", desc = "创建/发送时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date sendTime;

	@Column(name = "recvTime", type = Types.TIMESTAMP, desc = "阅读/接收时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date recvTime;

	@Column(name = "handleFlag", nullable = false, type = Types.INTEGER, desc = "处理标记 0：初始（未处理），-1：不予处理，1：已处理")
	private Integer handleFlag;

	@Column(name = "handleMemo", nullable = true, type = Types.VARCHAR, length = 90, desc = "处理备忘")
	private String handleMemo;
	
	@Column(type = Types.BOOLEAN, desc = "是否已删除",defaultValue="false")
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Boolean deleted;

	@Column(type = Types.TIMESTAMP, desc = "删除时间",defaultValue="CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date deleteTime;
	

	@Column(name = "ts", nullable = false, type = Types.TIMESTAMP, desc = "发表时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date ts;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getUserType() {
		return userType;
	}

	public void setUserType(Integer userType) {
		this.userType = userType;
	}

	public String getAppType() {
		return appType;
	}

	public void setAppType(String appType) {
		this.appType = appType;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Date getRecvTime() {
		return recvTime;
	}

	public void setRecvTime(Date recvTime) {
		this.recvTime = recvTime;
	}

	public Integer getHandleFlag() {
		return handleFlag;
	}

	public void setHandleFlag(Integer handleFlag) {
		this.handleFlag = handleFlag;
	}

	public String getHandleMemo() {
		return handleMemo;
	}

	public void setHandleMemo(String handleMemo) {
		this.handleMemo = handleMemo;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}

	@Override
	public String toString() {
		return "UserFeedback [id=" + id + ", userId=" + userId + ", userName=" + userName + ", userType=" + userType + ", appType=" + appType + ", subject=" + subject + ", content=" + content + ", sendTime=" + sendTime + ", recvTime="
				+ recvTime + ", handleFlag=" + handleFlag + ", handleMemo=" + handleMemo + ", deleted=" + deleted + ", deleteTime=" + deleteTime + ", ts=" + ts + "]";
	}

}