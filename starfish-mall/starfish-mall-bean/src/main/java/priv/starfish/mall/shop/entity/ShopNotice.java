package priv.starfish.mall.shop.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.json.JsonDateDeserializer;
import priv.starfish.common.json.JsonDateSerializer;
import priv.starfish.common.json.JsonShortDateTimeDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;

/**
 * 店铺公告表
 * 
 * @author 毛智东
 * @date 2015年6月8日 上午10:14:05
 *
 */
@Table(name = "shop_notice", desc = "店铺公告表")
public class ShopNotice implements Serializable {

	private static final long serialVersionUID = -8669745703095460802L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(type = Types.INTEGER, nullable = false)
	@ForeignKey(refEntityClass = Shop.class, refFieldName = "id")
	private Integer shopId;

	@Column(type = Types.VARCHAR, length = 100, nullable = false)
	private String title;

	@Column(type = Types.VARCHAR, length = 1000)
	private String content;

	// autoFlag : 是否自动发布 =》 发布方式 ： 自动发布 / 手动发布
	// 自动发布需指定发布日期
	@Column(type = Types.BOOLEAN, nullable = false, defaultValue = "false")
	private Boolean autoFlag;

	@Column(type = Types.DATE)
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date startDate;

	@Column(type = Types.DATE)
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date endDate;

	@Column(type = Types.TIMESTAMP)
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date pubTime;

	@Column(type = Types.TIMESTAMP)
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date endTime;

	// status： 0 : 未发布, 1: 发布中, 2：已停止
	@Column(type = Types.CHAR, nullable = false)
	private Integer status;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Date getPubTime() {
		return pubTime;
	}

	public void setPubTime(Date pubTime) {
		this.pubTime = pubTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Boolean getAutoFlag() {
		return autoFlag;
	}

	public void setAutoFlag(Boolean autoFlag) {
		this.autoFlag = autoFlag;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

}
