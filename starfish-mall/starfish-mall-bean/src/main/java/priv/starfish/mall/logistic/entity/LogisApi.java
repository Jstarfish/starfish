package priv.starfish.mall.logistic.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonShortDateTimeSerializer;

@Table(name = "logistic_api", uniqueConstraints = { @UniqueConstraint(fieldNames = { "name" }) })
public class LogisApi implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;

	/** 名称 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String name;

	/** 描述 */
	@Column(name = "`desc`", type = Types.VARCHAR, length = 250)
	private String desc;

	/** 方法类型 */
	@Column(nullable = false, type = Types.VARCHAR, length = 15, defaultValue = "'GET'")
	private String method;

	/** 查询接口url */
	@Column(nullable = false, type = Types.VARCHAR, length = 250)
	private String url;

	/** 是否启用 */
	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false")
	private Boolean enabled;

	/** 序号 */
	@Column(nullable = false, type = Types.INTEGER, defaultValue = "1")
	private Integer seqNo;
	
	private List<LogisApiParam> ps = new ArrayList<LogisApiParam>(0);

	/** 时间戳 */
	@Column(nullable = false, type = Types.TIMESTAMP)
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date ts;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public List<LogisApiParam> getPs() {
		return ps;
	}

	public void setPs(List<LogisApiParam> ps) {
		this.ps = ps;
	}

	@Override
	public String toString() {
		return "LogisApi [id=" + id + ", name=" + name + ", desc=" + desc + ", method=" + method + ", url=" + url + ", enabled=" + enabled + ", seqNo=" + seqNo + ", ps=" + ps + ", ts=" + ts + "]";
	}

}
