package priv.starfish.mall.order.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonNullDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.common.util.DateUtil;
import priv.starfish.mall.comn.entity.User;

/**
 * 销售订单工作（信息）
 * 
 * @author koqiui
 * @date 2015年12月16日 下午10:55:00
 *
 */
@Table(name = "sale_order_work", desc = "销售订单工作（信息）", uniqueConstraints = { @UniqueConstraint(fieldNames = { "orderId", "workerId" }) })
public class SaleOrderWork implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(type = Types.BIGINT)
	private Long id;

	@Column(nullable = false, type = Types.BIGINT, desc = "销售订单id")
	// @ForeignKey(refEntityClass = SaleOrder.class, refFieldName = "id")
	private Long orderId;

	@Column(nullable = false, type = Types.INTEGER, desc = "工作人员id")
	@ForeignKey(refEntityClass = User.class, refFieldName = "id")
	private Integer workerId;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "工作人员姓名")
	private String workerName;

	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false", desc = "是否带头人")
	private Boolean chiefFlag;

	@Column(type = Types.VARCHAR, length = 30, desc = "工作内容")
	private String work;

	@Column(name = "`desc`", type = Types.VARCHAR, length = 250, desc = "描述")
	private String desc;

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

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Integer getWorkerId() {
		return workerId;
	}

	public void setWorkerId(Integer workerId) {
		this.workerId = workerId;
	}

	public String getWorkerName() {
		return workerName;
	}

	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}

	public Boolean getChiefFlag() {
		return chiefFlag;
	}

	public void setChiefFlag(Boolean chiefFlag) {
		this.chiefFlag = chiefFlag;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	@Override
	public String toString() {
		return "SaleOrderWork [id=" + id + ", orderId=" + orderId + ", workerId=" + workerId + ", workerName=" + workerName + ", chiefFlag=" + chiefFlag + ", work=" + work + ", desc=" + desc + ", ts=" + DateUtil.toStdTimestampStr(ts) + "]";
	}

}
