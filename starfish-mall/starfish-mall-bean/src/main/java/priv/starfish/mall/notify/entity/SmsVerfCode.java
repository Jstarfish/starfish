package priv.starfish.mall.notify.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonShortDateTimeDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.mall.notify.dict.SmsUsage;

@Table(name = "sms_verf_code", uniqueConstraints = { @UniqueConstraint(fieldNames = { "phoneNo", "vfCode", "invalid" }) }, desc = "短信验证码表")
public class SmsVerfCode implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;

	/** 请求ip地址 */
	@Column(type = Types.VARCHAR, length = 60, nullable = false)
	private String reqIp;

	/** 电话号码 */
	@Column(type = Types.VARCHAR, length = 30, nullable = false)
	private String phoneNo;

	/** 验证码 */
	@Column(type = Types.VARCHAR, length = 10, nullable = false)
	private String vfCode;

	/** 用途 */
	@Column(name = "`usage`", nullable = false, type = Types.VARCHAR, length = 15)
	private SmsUsage usage;

	/** 内容 */
	@Column(type = Types.VARCHAR, length = 250, nullable = false)
	private String content;

	/** 发送时间 */
	@Column(type = Types.TIMESTAMP, nullable = false, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date sendTime;

	/** 终止时间 */
	@Column(type = Types.TIMESTAMP, nullable = false, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date expireTime;

	/** 发送成功 */
	@Column(type = Types.BOOLEAN, nullable = false, defaultValue = "false")
	private Boolean sendOk;

	/** 无效的 */
	@Column(type = Types.BOOLEAN, nullable = false, defaultValue = "false")
	private Boolean invalid;

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

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getVfCode() {
		return vfCode;
	}

	public void setVfCode(String vfCode) {
		this.vfCode = vfCode;
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

	public SmsUsage getUsage() {
		return usage;
	}

	public void setUsage(SmsUsage usage) {
		this.usage = usage;
	}

}
