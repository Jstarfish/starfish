package priv.starfish.mall.notify.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import priv.starfish.mall.notify.entity.SmsApiParam;

public class SmsApiDto implements Serializable {

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

	/** 是否禁用 */
	private Boolean disabled;

	/** 序号 */
	private Integer seqNo;

	/** 时间戳 */
	private Date ts;

	private List<SmsApiParam> params = new ArrayList<SmsApiParam>(0);

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

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
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

	public List<SmsApiParam> getParams() {
		setApiIdForParams(this.id);
		return params;
	}

	public void setParams(List<SmsApiParam> params) {
		this.params = params;
	}

	public void setApiIdForParams(Integer id) {
		for (SmsApiParam p : this.params) {
			p.setApiId(id);
		}
	}

	@Override
	public String toString() {
		return "SmsApiDto [id=" + id + ", name=" + name + ", desc=" + desc + ", method=" + method + ", url=" + url + ", disabled=" + disabled + ", seqNo=" + seqNo + ", ts=" + ts + ", params=" + params + "]";
	}

}
