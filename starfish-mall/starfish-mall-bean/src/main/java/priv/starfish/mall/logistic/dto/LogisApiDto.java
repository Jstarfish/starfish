package priv.starfish.mall.logistic.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import priv.starfish.mall.logistic.entity.LogisApiParam;

public class LogisApiDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 主键 */
	private Integer id;

	/** 名称 */
	private String name;

	/** 描述 */
	private String desc;

	/** 方法类型 */
	private String method;

	/** 查询接口url */
	private String url;

	/** 是否启用 */
	private Boolean enabled;

	/** 序号 */
	private Integer seqNo;

	/** 时间戳 */
	private Date ts;

	private List<LogisApiParam> params = new ArrayList<LogisApiParam>(0);

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

	public List<LogisApiParam> getParams() {
		setApiIdForParams(this.id);
		return params;
	}

	public void setParams(List<LogisApiParam> params) {
		this.params = params;
	}

	public void setApiIdForParams(Integer id) {
		for (LogisApiParam p : this.params) {
			p.setApiId(id);
		}
	}

	@Override
	public String toString() {
		return "LogisApiDto [id=" + id + ", name=" + name + ", desc=" + desc + ", method=" + method + ", url=" + url + ", enabled=" + enabled + ", seqNo=" + seqNo + ", ts=" + ts + ", params=" + params + "]";
	}

}
