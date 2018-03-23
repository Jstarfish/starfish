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
import priv.starfish.mall.comn.entity.User;

/**
 * e卡转赠记录
 * 
 * @author "WJJ"
 * @date 2015年10月18日 下午12:08:21
 *
 */
@Table(name = "ecard_transfer_rec")
public class ECardTransferRec implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(type = Types.INTEGER, desc = "主键 ")
	private Integer id;

	@Column(nullable = false, type = Types.INTEGER, desc = "用户e卡ID")
	@ForeignKey(refEntityClass = UserECard.class, refFieldName = "id")
	private Integer cardId;

	@Column(nullable = false, type = Types.VARCHAR, length = 18, desc = "用户e卡编号")
	private String cardNo;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "用户e卡类型代码")
	@ForeignKey(refEntityClass = ECard.class, refFieldName = "code")
	private String cardCode;
	
	@Column(nullable = false, type = Types.DECIMAL, precision = 18, scale = 4, desc = "面额(冗余字段)")
	private BigDecimal faceValue;

	@Column(nullable = false, type = Types.INTEGER, desc = "转出用户ID")
	@ForeignKey(refEntityClass = User.class, refFieldName = "id")
	private Integer userIdFrom;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "转出用户名称")
	private String userNameFrom;

	@Column(nullable = false, type = Types.VARCHAR, length = 15, desc = "转出用户手机号")
	private String userPhoneFrom;

	@Column(nullable = false, type = Types.INTEGER, desc = "受赠用户ID")
	@ForeignKey(refEntityClass = User.class, refFieldName = "id")
	private Integer userIdTo;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "受赠用户名称")
	private String userNameTo;

	@Column(nullable = false, type = Types.VARCHAR, length = 15, desc = "受赠用户手机号")
	private String userPhoneTo;

	@Column(type = Types.DECIMAL, precision = 18, scale = 4, desc = "余额")
	private BigDecimal remainVal;
	
	@Column(type = Types.BOOLEAN, desc = "是否已删除（逻辑删除已失效的）")
	private Boolean deleted;

	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP", desc = "时间戳")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date ts;

	/** E卡图片地址 */
	private String fileBrowseUrl;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getCardId() {
		return cardId;
	}

	public void setCardId(Integer cardId) {
		this.cardId = cardId;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getCardCode() {
		return cardCode;
	}

	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}

	public BigDecimal getFaceValue() {
		return faceValue;
	}

	public void setFaceValue(BigDecimal faceValue) {
		this.faceValue = faceValue;
	}

	public Integer getUserIdFrom() {
		return userIdFrom;
	}

	public void setUserIdFrom(Integer userIdFrom) {
		this.userIdFrom = userIdFrom;
	}

	public Integer getUserIdTo() {
		return userIdTo;
	}

	public void setUserIdTo(Integer userIdTo) {
		this.userIdTo = userIdTo;
	}

	public String getUserNameFrom() {
		return userNameFrom;
	}

	public void setUserNameFrom(String userNameFrom) {
		this.userNameFrom = userNameFrom;
	}

	public String getUserPhoneFrom() {
		return userPhoneFrom;
	}

	public void setUserPhoneFrom(String userPhoneFrom) {
		this.userPhoneFrom = userPhoneFrom;
	}

	public String getUserNameTo() {
		return userNameTo;
	}

	public void setUserNameTo(String userNameTo) {
		this.userNameTo = userNameTo;
	}

	public String getUserPhoneTo() {
		return userPhoneTo;
	}

	public void setUserPhoneTo(String userPhoneTo) {
		this.userPhoneTo = userPhoneTo;
	}

	public BigDecimal getRemainVal() {
		return remainVal;
	}

	public void setRemainVal(BigDecimal remainVal) {
		this.remainVal = remainVal;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public String getFileBrowseUrl() {
		return fileBrowseUrl;
	}

	public void setFileBrowseUrl(String fileBrowseUrl) {
		this.fileBrowseUrl = fileBrowseUrl;
	}

	@Override
	public String toString() {
		return "ECardTransferRec [id=" + id + ", cardId=" + cardId + ", cardNo=" + cardNo + ", cardCode=" + cardCode + ", userIdFrom=" + userIdFrom + ", userNameFrom=" + userNameFrom + ", userPhoneFrom=" + userPhoneFrom + ", userIdTo="
				+ userIdTo + ", userNameTo=" + userNameTo + ", userPhoneTo=" + userPhoneTo + ", remainVal=" + remainVal + ", ts=" + ts + ", fileBrowseUrl=" + fileBrowseUrl + "]";
	}

}
