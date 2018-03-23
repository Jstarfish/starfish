package priv.starfish.mall.market.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.json.JsonShortDateTimeDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;

@Table(name = "ecard_act_rule", desc = "e卡活动规则")
public class EcardActRule implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(nullable = false, type = Types.INTEGER, desc = "")
	@ForeignKey(refEntityClass = EcardAct.class, refFieldName = "id")
	private Integer actId;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "")
	private String cardCode;

	@Column(nullable = false, type = Types.INTEGER, desc = "条件类型 0 : 金额, 1 : 数量")
	private Integer condType;

	@Column(nullable = false, type = Types.INTEGER, desc = "条件值（根据条件类型含义不同：元，张等）")
	private Integer condValue;

	@Column(nullable = false, type = Types.VARCHAR, length = 15, desc = "活动类型 ActivityType msyhq 送优惠券 满送优惠券")
	private String actType;

	@Column(nullable = false, type = Types.BOOLEAN, desc = "是否仅限首次")
	private Boolean firstTimeOnly;

	@Column(nullable = false, type = Types.INTEGER, desc = "基于同一actId")
	private Integer seqNo;

	@Column(name = "`desc`", type = Types.VARCHAR, length = 250)
	private String desc;

	@Column(nullable = false, type = Types.TIMESTAMP, desc = "创建时间", defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date ts;

	private List<EcardActGift> ruleGiftItemList;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getActId() {
		return actId;
	}

	public void setActId(Integer actId) {
		this.actId = actId;
	}

	public String getCardCode() {
		return cardCode;
	}

	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}

	public Integer getCondType() {
		return condType;
	}

	public void setCondType(Integer condType) {
		this.condType = condType;
	}

	public Integer getCondValue() {
		return condValue;
	}

	public void setCondValue(Integer condValue) {
		this.condValue = condValue;
	}

	public String getActType() {
		return actType;
	}

	public void setActType(String actType) {
		this.actType = actType;
	}

	public Boolean getFirstTimeOnly() {
		return firstTimeOnly;
	}

	public void setFirstTimeOnly(Boolean firstTimeOnly) {
		this.firstTimeOnly = firstTimeOnly;
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

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public List<EcardActGift> getRuleGiftItemList() {
		return ruleGiftItemList;
	}

	public void setRuleGiftItemList(List<EcardActGift> ruleGiftItemList) {
		this.ruleGiftItemList = ruleGiftItemList;
	}

	@Override
	public String toString() {
		return "EcardActRule [id=" + id + ", actId=" + actId + ", cardCode=" + cardCode + ", condType=" + condType + ", condValue=" + condValue + ", actType=" + actType + ", firstTimeOnly=" + firstTimeOnly + ", seqNo=" + seqNo + ", desc="
				+ desc + ", ts=" + ts + ", ruleGiftItemList=" + ruleGiftItemList + "]";
	}

}
