package priv.starfish.mall.ecard.entity;

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
import priv.starfish.common.json.JsonNullDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;

/**
 * e卡交易记录
 * 
 * @author "WJJ"
 * @date 2015年10月18日 下午12:08:21
 *
 */
@Table(name = "ecard_transact_rec")
public class ECardTransactRec implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(type = Types.BIGINT, desc = "主键 ")
	private Long id;

	@Column(nullable = false, type = Types.INTEGER, desc = "用户卡ID")
	@ForeignKey(refEntityClass = UserECard.class, refFieldName = "id")
	private Integer cardId;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "默认为订单 orderpay, valuetrans")
	private String subject;

	@Column(type = Types.BIGINT, desc = "比如订单id")
	private Long targetId;

	@Column(type = Types.VARCHAR, length = 30, desc = "比如订单编号")
	private String targetStr;

	@Column(nullable = false, type = Types.INTEGER, desc = "方向标记：1，正向, -1：反向")
	private Integer directFlag;

	@Column(nullable = false, type = Types.INTEGER, desc = "用户Id")
	private Integer userId;

	@Column(nullable = false, type = Types.DECIMAL, precision = 18, scale = 4, desc = "结算金额")
	private BigDecimal theVal;

	@Column(nullable = false, type = Types.DECIMAL, precision = 18, scale = 4, desc = "余额")
	private BigDecimal remainVal;

	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP", desc = "交易时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date theTime;

	/** 关联用户E卡 **/
	private UserECard userECard;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getCardId() {
		return cardId;
	}

	public void setCardId(Integer cardId) {
		this.cardId = cardId;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Long getTargetId() {
		return targetId;
	}

	public void setTargetId(Long targetId) {
		this.targetId = targetId;
	}

	public String getTargetStr() {
		return targetStr;
	}

	public void setTargetStr(String targetStr) {
		this.targetStr = targetStr;
	}

	public Integer getDirectFlag() {
		return directFlag;
	}

	public void setDirectFlag(Integer directFlag) {
		this.directFlag = directFlag;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public BigDecimal getTheVal() {
		return theVal;
	}

	public void setTheVal(BigDecimal theVal) {
		this.theVal = theVal;
	}

	public BigDecimal getRemainVal() {
		return remainVal;
	}

	public void setRemainVal(BigDecimal remainVal) {
		this.remainVal = remainVal;
	}

	public Date getTheTime() {
		return theTime;
	}

	public void setTheTime(Date theTime) {
		this.theTime = theTime;
	}

	public UserECard getUserECard() {
		return userECard;
	}

	public void setUserECard(UserECard userECard) {
		this.userECard = userECard;
	}

	@Override
	public String toString() {
		return "ECardTransactRec [id=" + id + ", cardId=" + cardId + ", subject=" + subject + ", targetId=" + targetId + ", targetStr=" + targetStr + ", directFlag=" + directFlag + ", userId=" + userId + ", theVal=" + theVal
				+ ", remainVal=" + remainVal + ", theTime=" + theTime + ", userECard=" + userECard + "]";
	}

}
