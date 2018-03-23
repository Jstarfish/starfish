package priv.starfish.mall.car.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.json.JsonDateTimeSerializer;
import priv.starfish.mall.comn.entity.User;

@Table(name = "user_car_svc_rec", desc = "用户车辆服务记录")
public class UserCarSvcRec implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@ForeignKey(refEntityClass = User.class, refFieldName = "id")
	@Column(nullable = false, type = Types.INTEGER, updatable = false, desc = "用户id")
	private Integer userId;

	@ForeignKey(refEntityClass = UserCar.class, refFieldName = "id")
	@Column(nullable = false, type = Types.INTEGER, desc = "车辆id")
	private Integer carId;

	@Column(nullable = false, type = Types.VARCHAR, length = 60, desc = "车辆名称")
	private String carName;

	@Column(type = Types.INTEGER, desc = "品牌id，冗余字段")
	private Integer brandId;

	@Column(type = Types.INTEGER, desc = "车系id，冗余字段")
	private Integer serialId;

	@Column(type = Types.INTEGER, desc = "车型id，冗余字段")
	private Integer modelId;

	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP", desc = "完成时间")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date dateVal;

	@Column(nullable = false, type = Types.VARCHAR, length = 10, desc = "完成")
	private String dateStr;

	@Column(nullable = false, type = Types.BIGINT, desc = "订单Id")
	private Long orderId;

	@Column(nullable = false, type = Types.VARCHAR, length = 20, desc = "订单序号")
	private String orderNo;

	@Column(nullable = false, type = Types.INTEGER, desc = "加盟店Id")
	private Integer shopId;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "加盟店名称")
	private String shopName;

	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false", desc = "是否由合作店完成")
	private Boolean distFlag;

	@Column(type = Types.VARCHAR, length = 30, desc = "合作店名称")
	private String distShopName;

	@Column(nullable = false, type = Types.VARCHAR, length = 250, desc = "服务Ids")
	private String svcIds;

	@Column(nullable = false, type = Types.VARCHAR, length = 250, desc = "服务名称")
	private String svcNames;

	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date ts;

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

	public Integer getCarId() {
		return carId;
	}

	public void setCarId(Integer carId) {
		this.carId = carId;
	}

	public String getCarName() {
		return carName;
	}

	public void setCarName(String carName) {
		this.carName = carName;
	}

	public Integer getBrandId() {
		return brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}

	public Integer getSerialId() {
		return serialId;
	}

	public void setSerialId(Integer serialId) {
		this.serialId = serialId;
	}

	public Integer getModelId() {
		return modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}

	public Date getDateVal() {
		return dateVal;
	}

	public void setDateVal(Date dateVal) {
		this.dateVal = dateVal;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
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

	public Boolean getDistFlag() {
		return distFlag;
	}

	public void setDistFlag(Boolean distFlag) {
		this.distFlag = distFlag;
	}

	public String getDistShopName() {
		return distShopName;
	}

	public void setDistShopName(String distShopName) {
		this.distShopName = distShopName;
	}

	public String getSvcIds() {
		return svcIds;
	}

	public void setSvcIds(String svcIds) {
		this.svcIds = svcIds;
	}

	public String getSvcNames() {
		return svcNames;
	}

	public void setSvcNames(String svcNames) {
		this.svcNames = svcNames;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	@Override
	public String toString() {
		return "UserCarSvcRec [id=" + id + ", userId=" + userId + ", carId=" + carId + ", carName=" + carName + ", brandId=" + brandId + ", serialId=" + serialId + ", modelId=" + modelId + ", dateVal=" + dateVal + ", dateStr=" + dateStr
				+ ", orderId=" + orderId + ", orderNo=" + orderNo + ", shopId=" + shopId + ", shopName=" + shopName + ", distFlag=" + distFlag + ", distShopName=" + distShopName + ", svcIds=" + svcIds + ", svcNames=" + svcNames + ", ts="
				+ ts + "]";
	}

}
