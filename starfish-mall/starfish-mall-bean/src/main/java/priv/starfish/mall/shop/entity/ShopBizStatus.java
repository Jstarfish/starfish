package priv.starfish.mall.shop.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonDateDeserializer;
import priv.starfish.common.json.JsonDateSerializer;
import priv.starfish.common.json.JsonNullDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.common.util.DateUtil;

/**
 * 店铺营业状态
 * 
 * @author koqiui
 * @date 2015年12月16日 下午10:52:11
 *
 */
@Table(name = "shop_biz_status", desc = "店铺营业状态", uniqueConstraints = { @UniqueConstraint(fieldNames = { "shopId", "dateStr" }) })
public class ShopBizStatus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(type = Types.BIGINT)
	private Long id;

	@Column(nullable = false, type = Types.INTEGER, desc = "店铺id")
	// @ForeignKey(refEntityClass = Shop.class, refFieldName = "id")
	private Integer shopId;

	@Column(nullable = false, type = Types.VARCHAR, length = 10, desc = "业务日期(str)")
	private String dateStr;

	@Column(nullable = false, type = Types.DATE, desc = "业务日期(date)")
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date bizDate;

	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false", desc = "是否不再接单")
	private Boolean sealed;

	@Column(type = Types.VARCHAR, length = 90, desc = "不再接单原因")
	private String cause;

	@Column(type = Types.INTEGER, desc = "当日最大接单量")
	private Integer maxOrders;

	@Column(nullable = false, type = Types.INTEGER, desc = "操作人员id")
	private Integer operatorId;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "操作人员名称")
	private String operatorName;

	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date ts;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public Date getBizDate() {
		return bizDate;
	}

	public void setBizDate(Date bizDate) {
		this.bizDate = bizDate;
	}

	public Boolean getSealed() {
		return sealed;
	}

	public void setSealed(Boolean sealed) {
		this.sealed = sealed;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public Integer getMaxOrders() {
		return maxOrders;
	}

	public void setMaxOrders(Integer maxOrders) {
		this.maxOrders = maxOrders;
	}

	public Integer getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	@Override
	public String toString() {
		return "ShopBizStatus [id=" + id + ", shopId=" + shopId + ", dateStr=" + dateStr + ", bizDate=" + DateUtil.toStdDateStr(bizDate) + ", sealed=" + sealed + ", cause=" + cause + ", maxOrders=" + maxOrders + ", operatorId="
				+ operatorId + ", operatorName=" + operatorName + ", ts=" + DateUtil.toStdTimestampStr(ts) + "]";
	}
}
