package priv.starfish.mall.notify.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.mall.notify.dict.MailTplType;

/**
 * 邮件模板
 * 
 * @author 毛智东
 * @date 2015年5月21日 下午6:06:38
 *
 */
@Table(name = "mail_template", uniqueConstraints = { @UniqueConstraint(fieldNames = { "code" }), @UniqueConstraint(fieldNames = { "name" }) }, desc = "邮件模板")
public class MailTemplate implements Serializable {

	private static final long serialVersionUID = 7384236258443941290L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String code;

	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String name;

	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private MailTplType type;

	@Column(nullable = false, type = Types.VARCHAR, length = 250)
	private String subject;

	@Column(type = Types.CLOB, nullable = false, length = 1000)
	private String content;

	@Column(name = "`desc`", type = Types.VARCHAR, length = 250)
	private String desc;

	@Column(type = Types.VARCHAR, length = 30, nullable = false)
	@ForeignKey(refEntityClass = TplModel.class, refFieldName = "code")
	private String modelCode;

	@Column(nullable = false, type = Types.TIMESTAMP)
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date ts;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public MailTplType getType() {
		return type;
	}

	public void setType(MailTplType type) {
		this.type = type;
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

}
