package priv.starfish.mall.market.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.*;
import priv.starfish.common.json.JsonShortDateTimeDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;

@Table(name = "user_coupon", uniqueConstraints = { @UniqueConstraint(fieldNames = { "no" }) }, desc = "用戶优惠券")
public class UserCoupon implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(nullable = false, type = Types.INTEGER, desc = "优惠券id,coupon.id")
	@ForeignKey(refEntityClass = Coupon.class, refFieldName = "id")
	private Integer refId;

	@Column(type = Types.INTEGER, desc = "分发活动id")
	private Integer distActId;

	@Column(type = Types.INTEGER, desc = "店铺发放的有效（平台发放的为null）")
	private Integer shopId;

	@Column(nullable = false, type = Types.VARCHAR, length = 15, desc = "动态生成")
	private String no;

	@Column(nullable = false, type = Types.VARCHAR, length = 15, desc = "")
	private String type;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "")
	private String name;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "")
	private String title;

	@Column(nullable = false, type = Types.BIGINT, desc = "货品id")
	private Long productId;

	@Column(nullable = false, type = Types.DECIMAL, desc = "发放时的价额")
	private BigDecimal price;

	@Column(nullable = false, type = Types.DECIMAL, precision = 18, scale = 2, desc = "发放时的价额")
	private BigDecimal settlePrice;

	@Column(type = Types.INTEGER, desc = "为空：先发放后尚未被人领用")
	private Integer userId;

	@Column(nullable = false, type = Types.VARCHAR, length = 6, desc = "校验码（随机生成的6位数，非属主使用时校验用）")
	private String checkCode;

	@Column(type = Types.TIMESTAMP, desc = "获得时间（领用时就设置此项）")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date obtainTime;

	@Column(type = Types.TIMESTAMP, desc = "有效开始时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date startTime;

	@Column(type = Types.TIMESTAMP, desc = "有效结束时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date endTime;

	@Column(nullable = false, type = Types.BOOLEAN, desc = "", defaultValue = "FALSE")
	private Boolean invalid;

	@Column(type = Types.BIGINT, desc = "使用的订单id")
	private Long orderId;

	@Column(nullable = false, type = Types.TIMESTAMP, desc = "", defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date ts;

	@Column(type = Types.BOOLEAN, desc = "删除标记", defaultValue = "FALSE")
	private Boolean deleted;

	// 关联名称
	private String productName;
	// 暂时不用
	private Integer limitAmount;

	public static UserCoupon newOne() {
		return new UserCoupon();
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getRefId() {
		return refId;
	}

	public void setRefId(Integer refId) {
		this.refId = refId;
	}

	public Integer getDistActId() {
		return distActId;
	}

	public void setDistActId(Integer distActId) {
		this.distActId = distActId;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getProductId() {
		return productId;
	}

	public void setProductId(Long productId) {
		this.productId = productId;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public BigDecimal getSettlePrice() {
		return settlePrice;
	}

	public void setSettlePrice(BigDecimal settlePrice) {
		this.settlePrice = settlePrice;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	public Date getObtainTime() {
		return obtainTime;
	}

	public void setObtainTime(Date obtainTime) {
		this.obtainTime = obtainTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Boolean getInvalid() {
		return invalid;
	}

	public void setInvalid(Boolean invalid) {
		this.invalid = invalid;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Integer getLimitAmount() {
		return limitAmount;
	}

	public void setLimitAmount(Integer limitAmount) {
		this.limitAmount = limitAmount;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	@Override
	public String toString() {
		return "UserCoupon [id=" + id + ", refId=" + refId + ", distActId=" + distActId + ", shopId=" + shopId + ", no=" + no + ", type=" + type + ", name=" + name + ", title=" + title + ", productId=" + productId + ", price=" + price
				+ ", settlePrice=" + settlePrice + ", userId=" + userId + ", checkCode=" + checkCode + ", obtainTime=" + obtainTime + ", startTime=" + startTime + ", endTime=" + endTime + ", invalid=" + invalid + ", orderId=" + orderId
				+ ", ts=" + ts + ", deleted=" + deleted + ", productName=" + productName + ", limitAmount=" + limitAmount + "]";
	}

}
