package priv.starfish.mall.order.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.json.JsonNullDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.mall.comn.entity.User;

@Table(name = "ecard_order")
public class ECardOrder implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(type = Types.VARCHAR, length = 60, desc = "订单编号")
	private String no;

	@Column(nullable = false, type = Types.INTEGER, desc = "用户ID")
	@ForeignKey(refEntityClass = User.class, refFieldName = "id")
	private Integer userId;

	@Column(type = Types.VARCHAR, length = 30, desc = "用户名称")
	private String userName;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "卡片类型")
	private String cardCode;

	@Column(type = Types.VARCHAR, length = 30, desc = "卡片名称")
	private String cardName;

	@Column(nullable = false, type = Types.DECIMAL, precision = 18, scale = 4, desc = "面额")
	private BigDecimal faceValue;

	@Column(nullable = false, type = Types.DECIMAL, precision = 18, scale = 4, desc = "单价")
	private BigDecimal price;

	@Column(nullable = false, type = Types.INTEGER, desc = "购卡数量")
	private Integer quantity;

	@Column(type = Types.VARCHAR, length = 15, desc = "购买人联系电话")
	private String phoneNo;

	@Column(type = Types.INTEGER, nullable = true, desc = "绑定的店铺ID")
	private Integer shopId;

	@Column(type = Types.VARCHAR, nullable = true, desc = "绑定的店铺名称")
	private String shopName;

	@Column(nullable = false, type = Types.DECIMAL, precision = 18, scale = 4, desc = "订单金额")
	private BigDecimal amount;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "支付渠道")
	private String payWay;

	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP", desc = "订单创建时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date createTime;

	@Column(nullable = false, type = Types.BOOLEAN, desc = "是否已支付")
	private Boolean paid;

	@Column(type = Types.VARCHAR, length = 30, desc = "支付时间，格式为yyyy-MM-dd HH:mm:ss")
	private String payTime;

	@Column(nullable = false, type = Types.BOOLEAN, desc = "是否已取消")
	private Boolean cancelled;

	@Column(type = Types.TIMESTAMP, desc = "订单取消时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date cancelTime;

	/** 订单处理记录 */
	private List<ECardOrderRecord> eCardOrderRecords;

	/** E卡图片地址 */
	private String fileBrowseUrl;

	public static ECardOrder newOne() {
		return new ECardOrder();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getCardCode() {
		return cardCode;
	}

	public void setCardCode(String cardCode) {
		this.cardCode = cardCode;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public BigDecimal getFaceValue() {
		return faceValue;
	}

	public void setFaceValue(BigDecimal faceValue) {
		this.faceValue = faceValue;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getPayWay() {
		return payWay;
	}

	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Boolean getPaid() {
		return paid;
	}

	public void setPaid(Boolean paid) {
		this.paid = paid;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public Boolean getCancelled() {
		return cancelled;
	}

	public void setCancelled(Boolean cancelled) {
		this.cancelled = cancelled;
	}

	public Date getCancelTime() {
		return cancelTime;
	}

	public void setCancelTime(Date cancelTime) {
		this.cancelTime = cancelTime;
	}

	public List<ECardOrderRecord> geteCardOrderRecords() {
		return eCardOrderRecords;
	}

	public void seteCardOrderRecords(List<ECardOrderRecord> eCardOrderRecords) {
		this.eCardOrderRecords = eCardOrderRecords;
	}

	public String getFileBrowseUrl() {
		return fileBrowseUrl;
	}

	public void setFileBrowseUrl(String fileBrowseUrl) {
		this.fileBrowseUrl = fileBrowseUrl;
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

	@Override
	public String toString() {
		return "ECardOrder [id=" + id + ", no=" + no + ", userId=" + userId + ", userName=" + userName + ", cardCode=" + cardCode + ", cardName=" + cardName + ", faceValue=" + faceValue + ", price=" + price + ", quantity=" + quantity
				+ ", phoneNo=" + phoneNo + ", shopId=" + shopId + ", shopName=" + shopName + ", amount=" + amount + ", payWay=" + payWay + ", createTime=" + createTime + ", paid=" + paid + ", payTime=" + payTime + ", cancelled=" + cancelled
				+ ", cancelTime=" + cancelTime + ", eCardOrderRecords=" + eCardOrderRecords + ", fileBrowseUrl=" + fileBrowseUrl + "]";
	}

}
