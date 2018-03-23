package priv.starfish.mall.notify.entity;

import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.mall.comn.dict.AuthScope;

@Table(name = "sms_biz_record", desc = "短信业务记录表")
public class SmsBizRecord implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id(type = Types.BIGINT)
	private BigInteger id;

	@Column(type = Types.VARCHAR, length = 15)
	private AuthScope scope;

	@Column(type = Types.INTEGER)
	private Integer entityId;

	@Column(type = Types.VARCHAR, length = 30, nullable = false)
	private String bizCode;

	@Column(type = Types.VARCHAR, length = 30, nullable = false)
	private String phoneNo;

	@Column(type = Types.VARCHAR, length = 1000, nullable = false)
	private String content;

	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date sendTime;

	@Column(type = Types.INTEGER, nullable = false)
	@ForeignKey(refEntityClass = SmsApi.class, refFieldName = "id")
	private Integer sendApiId;

	@Column(type = Types.INTEGER)
	private Integer senderId;

	@Column(type = Types.BOOLEAN, nullable = false, defaultValue = "false")
	private Boolean sendOk;

	public BigInteger getId() {
		return id;
	}

	public void setId(BigInteger id) {
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

	public String getBizCode() {
		return bizCode;
	}

	public void setBizCode(String bizCode) {
		this.bizCode = bizCode;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
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

	public Integer getSendApiId() {
		return sendApiId;
	}

	public void setSendApiId(Integer sendApiId) {
		this.sendApiId = sendApiId;
	}

	public Integer getSenderId() {
		return senderId;
	}

	public void setSenderId(Integer senderId) {
		this.senderId = senderId;
	}

	public Boolean getSendOk() {
		return sendOk;
	}

	public void setSendOk(Boolean sendOk) {
		this.sendOk = sendOk;
	}

}
