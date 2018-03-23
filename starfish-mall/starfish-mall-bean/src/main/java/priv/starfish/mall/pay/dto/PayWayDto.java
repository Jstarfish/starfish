package priv.starfish.mall.pay.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import priv.starfish.mall.pay.entity.PayWayParam;

public class PayWayDto implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 主键 */
	private Integer id;

	/** 名称 */
	private String name;

	/** 描述 */
	private String desc;

	private String code;

	private boolean disabled;

	/** 序号 */
	private Integer seqNo;

	/** 时间戳 */
	private Date ts;

	private List<PayWayParam> payWayParamList = new ArrayList<PayWayParam>(0);

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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
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

	public List<PayWayParam> getPayWayParamList() {
		setPWIdForParams(this.id);
		return payWayParamList;
	}

	public void setPayWayParamList(List<PayWayParam> payWayParamList) {
		this.payWayParamList = payWayParamList;
	}

	public void setPWIdForParams(Integer id) {
		for (PayWayParam p : this.payWayParamList) {
			p.setPayWayId(id);
		}
	}

	@Override
	public String toString() {
		return "PayWayDto [id=" + id + ", name=" + name + ", desc=" + desc + ", code=" + code + ", disabled=" + disabled + ", seqNo=" + seqNo + ", ts=" + ts + ", payWayParamList=" + payWayParamList + "]";
	}

}
