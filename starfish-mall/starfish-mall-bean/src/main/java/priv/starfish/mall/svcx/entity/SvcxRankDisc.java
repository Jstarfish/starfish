package priv.starfish.mall.svcx.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonShortDateTimeDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;

@Table(name = "svc_rank_disc", uniqueConstraints = { @UniqueConstraint(fieldNames = { "shopId", "svcId", "rank" }) })
public class SvcxRankDisc implements Serializable {
	private static final long serialVersionUID = 1L;

	public static final int FREE_CHOICE_ID = -1;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(nullable = false, type = Types.INTEGER, desc = "店铺Id")
	private Integer shopId;

	@Column(nullable = false, type = Types.INTEGER, desc = "服务Id")
	@ForeignKey(refEntityClass = Svcx.class, refFieldName = "id")
	private Integer svcId;

	@Column(type = Types.VARCHAR, desc = "服务名称")
	private String svcName;

	@Column(type = Types.INTEGER, desc = "服务分组Id")
	private Integer svcKindId;

	@Column(nullable = false, type = Types.INTEGER, desc = "会员等级")
	private Integer rank;

	@Column(nullable = false, type = Types.DECIMAL, precision = 8, scale = 2, desc = "会员折扣")
	private BigDecimal rate;

	@Column(nullable = false, type = Types.BOOLEAN, desc = "是否不可用", defaultValue = "false")
	private Boolean disabled;

	@Column(name = "ts", nullable = false, type = Types.TIMESTAMP, desc = "发表时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date ts;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public Integer getSvcId() {
		return svcId;
	}

	public void setSvcId(Integer svcId) {
		this.svcId = svcId;
	}

	public String getSvcName() {
		return svcName;
	}

	public void setSvcName(String svcName) {
		this.svcName = svcName;
	}

	public Integer getSvcKindId() {
		return svcKindId;
	}

	public void setSvcKindId(Integer svcKindId) {
		this.svcKindId = svcKindId;
	}

	public Integer getRank() {
		return rank;
	}

	public void setRank(Integer rank) {
		this.rank = rank;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	@Override
	public String toString() {
		return "SvcxRankDisc [id=" + id + ", shopId=" + shopId + ", svcId=" + svcId + ", svcName=" + svcName + ", svcKindId=" + svcKindId + ", rank=" + rank + ", rate=" + rate + ", disabled=" + disabled + ", ts=" + ts + "]";
	}

}
