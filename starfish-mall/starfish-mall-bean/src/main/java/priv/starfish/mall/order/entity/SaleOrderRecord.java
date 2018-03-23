package priv.starfish.mall.order.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.json.JsonShortDateTimeSerializer;

@Table(name = "sale_order_record")
public class SaleOrderRecord implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id(type = Types.BIGINT)
	private Long id;

	@Column(nullable = false, type = Types.BIGINT, desc = "订单编号")
	@ForeignKey(refEntityClass = SaleOrder.class, refFieldName = "id")
	private Long orderId;

	@Column(type = Types.VARCHAR, length = 15, desc = "操作")
	private String action;

	@Column(type = Types.VARCHAR, length = 30, desc = "操作人角色")
	private String actRole;

	@Column(type = Types.VARCHAR, length = 250, desc = "附加信息")
	private String extraInfo;

	@Column(nullable = false, type = Types.INTEGER, desc = "操作人ID")
	private Integer actorId;

	@Column(type = Types.VARCHAR, length = 30, desc = "操作人名字")
	private String actorName;

	@Column(nullable = false,type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP", desc = "时间戳")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date ts;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getActRole() {
		return actRole;
	}

	public void setActRole(String actRole) {
		this.actRole = actRole;
	}

	public String getExtraInfo() {
		return extraInfo;
	}

	public void setExtraInfo(String extraInfo) {
		this.extraInfo = extraInfo;
	}

	public Integer getActorId() {
		return actorId;
	}

	public void setActorId(Integer actorId) {
		this.actorId = actorId;
	}

	public String getActorName() {
		return actorName;
	}

	public void setActorName(String actorName) {
		this.actorName = actorName;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	@Override
	public String toString() {
		return "SaleOrderRecord [id=" + id + ", orderId=" + orderId + ", action=" + action + ", actRole=" + actRole + ", extraInfo=" + extraInfo + ", actorId=" + actorId + ", actorName=" + actorName + ", ts=" + ts + "]";
	}

}
