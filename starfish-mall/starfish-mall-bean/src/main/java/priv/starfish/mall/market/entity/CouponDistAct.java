package priv.starfish.mall.market.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonShortDateTimeDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;

@Table(name = "coupon_dist_act", uniqueConstraints = { @UniqueConstraint(fieldNames = { "year", "name" }) }, desc = "优惠券分发活动")
public class CouponDistAct implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(nullable = false, type = Types.INTEGER, desc = "发生的年份，取系统时间，不可更新")
	private Integer year;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "")
	private String name;

	@Column(nullable = false, type = Types.INTEGER, desc = "优惠券id,coupon.id")
	private Integer couponId;

	@Column(nullable = false, type = Types.TIMESTAMP, desc = "开始时间", defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date startTime;

	@Column(nullable = false, type = Types.TIMESTAMP, desc = "结束时间", defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date endTime;

	@Column(nullable = false, type = Types.INTEGER, desc = "")
	private Integer maxCount;

	@Column(name = "`desc`", type = Types.VARCHAR, length = 250)
	private String desc;

	@Column(nullable = false, type = Types.INTEGER, desc = "已分发数量 <= maxCount ，初始为0")
	private Integer distCount;

	@Column(nullable = false, type = Types.TIMESTAMP, desc = "创建时间", defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date createTime;

	@Column(type = Types.TIMESTAMP, desc = "更新时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date updateTime;

	@Column(type = Types.BOOLEAN, desc = "是否禁用")
	private Boolean disabled;

	@Column(type = Types.BOOLEAN, desc = "删除标记")
	private Boolean deleted;

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCouponId() {
		return couponId;
	}

	public void setCouponId(Integer couponId) {
		this.couponId = couponId;
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

	public Integer getMaxCount() {
		return maxCount;
	}

	public void setMaxCount(Integer maxCount) {
		this.maxCount = maxCount;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getDistCount() {
		return distCount;
	}

	public void setDistCount(Integer distCount) {
		this.distCount = distCount;
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

	@Override
	public String toString() {
		return "CouponDistAct [id=" + id + ", year=" + year + ", name=" + name + ", couponId=" + couponId + ", startTime=" + startTime + ", endTime=" + endTime + ", maxCount=" + maxCount + ", desc=" + desc + ", distCount=" + distCount
				+ ", createTime=" + createTime + ", updateTime=" + updateTime + ", disabled=" + disabled + ", deleted=" + deleted + "]";
	}

}
