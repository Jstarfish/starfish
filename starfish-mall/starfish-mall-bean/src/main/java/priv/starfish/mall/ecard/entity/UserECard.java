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
import priv.starfish.common.json.JsonShortDateTimeDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.mall.shop.entity.Shop;

/**
 * 用户_e卡
 * 
 * @author "WJJ"
 * @date 2015年10月18日 下午12:08:21
 *
 */
@Table(name = "user_ecard")
public class UserECard implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(type = Types.INTEGER, desc = "主键 ")
	private Integer id;

	@Column(nullable = false, type = Types.INTEGER, desc = "用户ID")
	private Integer userId;

	@Column(type = Types.VARCHAR, desc = "用户名称")
	private String userName;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "卡等级编码")
	@ForeignKey(refEntityClass = ECard.class, refFieldName = "code")
	private String cardCode;

	@Column(nullable = false, type = Types.VARCHAR, length = 18, desc = "卡号，比如：J000000002222")
	private String cardNo;

	@Column(nullable = false, type = Types.DECIMAL, precision = 18, scale = 4, desc = "面额")
	private BigDecimal faceValue;

	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP", desc = "购买时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date buyTime;

	@Column(type = Types.DECIMAL, precision = 18, scale = 4, desc = "余额")
	private BigDecimal remainVal;

	@Column(type = Types.INTEGER, nullable = true, desc = "绑定的店铺ID")
	@ForeignKey(refEntityClass = Shop.class, refFieldName = "id")
	private Integer shopId;

	@Column(type = Types.VARCHAR, nullable = true, desc = "绑定的店铺名称")
	private String shopName;

	@Column(nullable = false, type = Types.INTEGER, desc = "排序号", defaultValue = "1")
	private Integer seqNo;

	@Column(type = Types.BOOLEAN, defaultValue = "false", desc = "是否失效")
	private Boolean invalid;

	@Column(type = Types.BOOLEAN, defaultValue = "false", desc = "是否已删除（逻辑删除已失效的）")
	private Boolean deleted;

	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP", desc = "时间戳")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date ts;

	/** E卡图片地址 */
	private String fileBrowseUrl;

	/** 转赠给当前登录用户的E卡转赠记录(前台我的e卡页面使用) */
	private ECardTransferRec eCardTransferRec;

	/** 关联e卡 **/
	private ECard eCard;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getCardCode() {
		return cardCode;
	}

	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public BigDecimal getFaceValue() {
		return faceValue;
	}

	public void setFaceValue(BigDecimal faceValue) {
		this.faceValue = faceValue;
	}

	public Date getBuyTime() {
		return buyTime;
	}

	public void setBuyTime(Date buyTime) {
		this.buyTime = buyTime;
	}

	public BigDecimal getRemainVal() {
		return remainVal;
	}

	public void setRemainVal(BigDecimal remainVal) {
		this.remainVal = remainVal;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public Boolean getInvalid() {
		return invalid;
	}

	public void setInvalid(Boolean invalid) {
		this.invalid = invalid;
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

	public ECardTransferRec geteCardTransferRec() {
		return eCardTransferRec;
	}

	public void seteCardTransferRec(ECardTransferRec eCardTransferRec) {
		this.eCardTransferRec = eCardTransferRec;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public String getShopName() {
		return shopName;
	}

	public void setShopName(String shopName) {
		this.shopName = shopName;
	}

	public ECard geteCard() {
		return eCard;
	}

	public void seteCard(ECard eCard) {
		this.eCard = eCard;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public String toString() {
		return "UserECard [id=" + id + ", userId=" + userId + ", userName=" + userName + ", cardCode=" + cardCode + ", cardNo=" + cardNo + ", faceValue=" + faceValue + ", buyTime=" + buyTime + ", remainVal=" + remainVal + ", shopId="
				+ shopId + ", shopName=" + shopName + ", seqNo=" + seqNo + ", invalid=" + invalid + ", deleted=" + deleted + ", ts=" + ts + ", fileBrowseUrl=" + fileBrowseUrl + ", eCardTransferRec=" + eCardTransferRec + ", eCard=" + eCard
				+ "]";
	}

}
