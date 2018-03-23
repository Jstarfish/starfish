package priv.starfish.mall.logistic.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonDateSerializer;

/**
 * 配送方式
 * 
 * @author 毛智东
 * @date 2015年5月29日 上午10:54:31
 *
 */
@Table(name = "delivery_way", uniqueConstraints = { @UniqueConstraint(fieldNames = { "name","comId"}) }, desc = "配送方式")
public class DeliveryWay implements Serializable {

	private static final long serialVersionUID = -9014790051320274506L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(type = Types.VARCHAR, length = 30, nullable = false)
	private String name;

	@Column(type = Types.INTEGER, nullable = false, desc = "物流公司")
	@ForeignKey(refEntityClass = LogisCom.class, refFieldName = "id")
	private Integer comId;

	@Column(type = Types.BOOLEAN, nullable = false, defaultValue = "false" ,desc = "支持货到付款")
	private boolean supportPod;

	@Column(type = Types.DECIMAL, precision = 10, scale = 2,desc = "报价费率")
	private BigDecimal insureRate;

	@Column(name = "`desc`", type = Types.VARCHAR, length = 250)
	private String desc;

	@Column(type = Types.INTEGER, nullable = false)
	private Integer seqNo;

	@Column(type = Types.BOOLEAN, nullable = false, defaultValue = "false")
	private boolean disabled;

	@Column(type = Types.TIMESTAMP, nullable = false)
	@JsonSerialize(using = JsonDateSerializer.class)
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

	public Integer getComId() {
		return comId;
	}

	public void setComId(Integer comId) {
		this.comId = comId;
	}

	public boolean isSupportPod() {
		return supportPod;
	}

	public void setSupportPod(boolean supportPod) {
		this.supportPod = supportPod;
	}

	public BigDecimal getInsureRate() {
		return insureRate;
	}

	public void setInsureRate(BigDecimal insureRate) {
		this.insureRate = insureRate;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	@Override
	public String toString() {
		return "DeliveryWay [id=" + id + ", name=" + name + ", comId=" + comId + ", supportPod=" + supportPod + ", insureRate=" + insureRate + ", desc=" + desc + ", seqNo=" + seqNo + ", disabled=" + disabled + ", ts=" + ts + "]";
	}

}
