package priv.starfish.mall.market.entity;

import java.io.Serializable;
import java.sql.Types;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;

@Table(name = "ecard_act_gift", desc = "e卡活动馈赠商品")
public class EcardActGift implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id(type = Types.BIGINT)
	private Long id;

	@Column(nullable = false, type = Types.INTEGER, desc = "")
	@ForeignKey(refEntityClass = EcardActRule.class, refFieldName = "id")
	private Integer actRuleId;

	@Column(nullable = false, type = Types.VARCHAR, length = 15, desc = "馈赠物品类型 priv.starfish.mall.market.dict.GiftType")
	private String type;

	@Column(nullable = false, type = Types.INTEGER, desc = "标记，用以标记优惠券种类：0:商品 / 1：服务")
	private Integer flag;

	@Column(nullable = false, type = Types.BIGINT, desc = "金额数, 优惠券id, 货品id, 服务id  ==> long（注意类型转换）")
	private Long value;

	@Column(type = Types.VARCHAR, length = 60, desc = "冗余字段：优惠券、货品、服务等的名称/标题")
	private String text;

	@Column(nullable = false, type = Types.INTEGER, desc = "基于同一actRuleId")
	private Integer seqNo;

	private String couponType;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getActRuleId() {
		return actRuleId;
	}

	public void setActRuleId(Integer actRuleId) {
		this.actRuleId = actRuleId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public Long getValue() {
		return value;
	}

	public void setValue(Long value) {
		this.value = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public String getCouponType() {
		return couponType;
	}

	public void setCouponType(String couponType) {
		this.couponType = couponType;
	}

	@Override
	public String toString() {
		return "EcardActGift [id=" + id + ", actRuleId=" + actRuleId + ", type=" + type + ", flag=" + flag + ", value=" + value + ", text=" + text + ", seqNo=" + seqNo + ", couponType=" + couponType + "]";
	}

}
