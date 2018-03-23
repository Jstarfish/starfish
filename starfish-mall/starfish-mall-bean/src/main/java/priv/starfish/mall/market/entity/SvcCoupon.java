package priv.starfish.mall.market.entity;

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
import priv.starfish.mall.svcx.entity.Svcx;

@Table(name = "svc_coupon", uniqueConstraints = { @UniqueConstraint(fieldNames = { "year", "type", "name" }) }, desc = "优惠券")
public class SvcCoupon implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 优惠券有效类型 */
	public static final int COUPON_VALID_TYPE_DAY = 0;
	public static final int COUPON_VALID_TYPE_TIME = 1;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(nullable = false, type = Types.INTEGER, desc = "发生的年份，取系统时间，不可更新")
	private Integer year;

	@Column(nullable = false, type = Types.VARCHAR, length = 15, desc = "优惠券类型")
	private String type;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "名称")
	private String name;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "前台显示名称")
	private String title;

	@Column(nullable = false, type = Types.INTEGER, desc = "服务id")
	@ForeignKey(refEntityClass = Svcx.class, refFieldName = "id")
	private Integer svcId;

	@Column(type = Types.DECIMAL, precision = 18, scale = 2, desc = "价额（免付0，抵金、特价）")
	private BigDecimal price;

	@Column(type = Types.DECIMAL, precision = 18, scale = 4, desc = "与商户结算金额")
	private BigDecimal settlePrice;

	@Column(nullable = false, type = Types.INTEGER, desc = "最低消费金额")
	private Integer limitAmount;

	@Column(name = "`desc`", type = Types.VARCHAR, length = 250)
	private String desc;

	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP", desc = "发放开始时间")
	private Date issueStartTime;

	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP", desc = "发放结束时间")
	private Date issueEndTime;

	@Column(nullable = false, type = Types.INTEGER, desc = "有效类型0：按天数1：按时间")
	private Integer validType;

	@Column(type = Types.INTEGER, desc = "有效天数")
	private Integer validDays;

	@Column(type = Types.TIMESTAMP, desc = "有效开始时间")
	private Date validStartTime;

	@Column(type = Types.TIMESTAMP, desc = "有效结束时间")
	private Date validEndTime;

	@Column(nullable = false, type = Types.INTEGER, desc = "发放数量限制，-1代表无限制")
	private Integer limitCount;

	@Column(nullable = false, type = Types.TIMESTAMP, desc = "创建时间", defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date createTime;

	@Column(type = Types.TIMESTAMP, desc = "最后更新时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date updateTime;

	@Column(nullable = false, type = Types.BOOLEAN, desc = "是否禁用")
	private Boolean disabled;

	@Column(nullable = false, type = Types.BOOLEAN, desc = "删除标记")
	private Boolean deleted;

	private String SvcName;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
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

	public Integer getSvcId() {
		return svcId;
	}

	public void setSvcId(Integer svcId) {
		this.svcId = svcId;
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

	public Integer getLimitAmount() {
		return limitAmount;
	}

	public void setLimitAmount(Integer limitAmount) {
		this.limitAmount = limitAmount;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Date getIssueStartTime() {
		return issueStartTime;
	}

	public void setIssueStartTime(Date issueStartTime) {
		this.issueStartTime = issueStartTime;
	}

	public Date getIssueEndTime() {
		return issueEndTime;
	}

	public void setIssueEndTime(Date issueEndTime) {
		this.issueEndTime = issueEndTime;
	}

	public Integer getValidType() {
		return validType;
	}

	public void setValidType(Integer validType) {
		this.validType = validType;
	}

	public Integer getValidDays() {
		return validDays;
	}

	public void setValidDays(Integer validDays) {
		this.validDays = validDays;
	}

	public Date getValidStartTime() {
		return validStartTime;
	}

	public void setValidStartTime(Date validStartTime) {
		this.validStartTime = validStartTime;
	}

	public Date getValidEndTime() {
		return validEndTime;
	}

	public void setValidEndTime(Date validEndTime) {
		this.validEndTime = validEndTime;
	}

	public Integer getLimitCount() {
		return limitCount;
	}

	public void setLimitCount(Integer limitCount) {
		this.limitCount = limitCount;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public String getSvcName() {
		return SvcName;
	}

	public void setSvcName(String svcName) {
		SvcName = svcName;
	}

	@Override
	public String toString() {
		return "SvcCoupon [id=" + id + ", year=" + year + ", type=" + type + ", name=" + name + ", title=" + title + ", svcId=" + svcId + ", price=" + price + ", settlePrice=" + settlePrice + ", limitAmount=" + limitAmount + ", desc="
				+ desc + ", issueStartTime=" + issueStartTime + ", issueEndTime=" + issueEndTime + ", validType=" + validType + ", validDays=" + validDays + ", validStartTime=" + validStartTime + ", validEndTime=" + validEndTime
				+ ", limitCount=" + limitCount + ", createTime=" + createTime + ", updateTime=" + updateTime + ", disabled=" + disabled + ", deleted=" + deleted + ", SvcName=" + SvcName + "]";
	}

}
