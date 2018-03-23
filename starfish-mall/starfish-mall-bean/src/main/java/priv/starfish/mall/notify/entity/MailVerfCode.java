package priv.starfish.mall.notify.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.json.JsonShortDateTimeDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;

@Table(name = "mail_verf_code", desc = "邮箱验证码表")
public class MailVerfCode implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;

	/** 请求ip地址 */
	@Column(type = Types.VARCHAR, length = 60, nullable = false)
	private String reqIp;

	/** 邮箱 */
	@Column(type = Types.VARCHAR, length = 60, nullable = false)
	private String email;

	/** 验证码 */
	@Column(type = Types.VARCHAR, length = 30, nullable = false)
	private String vfCode;

	/** 主题 */
	@Column(type = Types.VARCHAR, length = 250, nullable = false)
	private String subject;

	/** 内容 */
	@Column(type = Types.VARCHAR, length = 1000, nullable = false)
	private String content;

	/** 发送时间 */
	@Column(type = Types.TIMESTAMP, nullable = false, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date sendTime;

	/** 发件服务器id */
	@Column(type = Types.INTEGER)
	private Integer sendSvrId;

	/** 截止时间 */
	@Column(type = Types.TIMESTAMP, nullable = false, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date expireTime;

	/** 发送成功 */
	@Column(type = Types.BOOLEAN, nullable = false, defaultValue = "false")
	private Boolean sendOk;

	/** 是否有效 */
	@Column(type = Types.BOOLEAN, nullable = false, defaultValue = "false")
	private Boolean invalid;

	/** 用途 */
	@Column(name = "`usage`", type = Types.VARCHAR, length = 15)
	private String usage;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getReqIp() {
		return reqIp;
	}

	public void setReqIp(String reqIp) {
		this.reqIp = reqIp;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getVfCode() {
		return vfCode;
	}

	public void setVfCode(String vfCode) {
		this.vfCode = vfCode;
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

	public Integer getSendSvrId() {
		return sendSvrId;
	}

	public void setSendSvrId(Integer sendSvrId) {
		this.sendSvrId = sendSvrId;
	}

	public Date getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(Date expireTime) {
		this.expireTime = expireTime;
	}

	public Boolean getSendOk() {
		return sendOk;
	}

	public void setSendOk(Boolean sendOk) {
		this.sendOk = sendOk;
	}

	public Boolean getInvalid() {
		return invalid;
	}

	public void setInvalid(Boolean invalid) {
		this.invalid = invalid;
	}

	public String getUsage() {
		return usage;
	}

	public void setUsage(String usage) {
		this.usage = usage;
	}

}
