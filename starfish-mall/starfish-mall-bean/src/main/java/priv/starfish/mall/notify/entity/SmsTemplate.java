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

/**
 * 短信模板
 * 
 * @author 毛智东
 * @date 2015年5月21日 下午5:42:01
 *
 */
@Table(name = "sms_template", uniqueConstraints = { @UniqueConstraint(fieldNames = { "code" }), @UniqueConstraint(fieldNames = { "name" }) }, desc = "短信模板")
public class SmsTemplate implements Serializable {

	private static final long serialVersionUID = -4164737088244424515L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(type = Types.VARCHAR, nullable = false, length = 30)
	private String code;

	@Column(type = Types.VARCHAR, nullable = false, length = 30)
	private String name;

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
