package priv.starfish.mall.car.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonDateTimeSerializer;
import priv.starfish.mall.comn.entity.User;

@Table(name = "user_car", uniqueConstraints = { @UniqueConstraint(fieldNames = { "userId", "modelId" }) }, desc = "用户车辆")
public class UserCar implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@ForeignKey(refEntityClass = User.class, refFieldName = "id")
	@Column(nullable = false, type = Types.INTEGER, updatable = false, desc = "用户id")
	private Integer userId;

	@ForeignKey(refEntityClass = CarModel.class, refFieldName = "id")
	@Column(nullable = false, type = Types.INTEGER, desc = "车型id")
	private Integer modelId;

	@Column(type = Types.VARCHAR, length = 90, desc = "CarModel.fullName")
	private String modelName;

	@Column(type = Types.VARCHAR, length = 15, desc = "车辆名称")
	private String name;

	@Column(type = Types.INTEGER, desc = "车系id，冗余字段")
	private Integer serialId;

	@Column(type = Types.INTEGER, desc = "品牌id，冗余字段")
	private Integer brandId;

	@Column(type = Types.VARCHAR, length = 15, desc = "车身颜色")
	private String color;

	@Column(type = Types.VARCHAR, length = 20, desc = "VIN（机动车身份标识）")
	private String vklIdNo;

	@Column(type = Types.VARCHAR, length = 15, desc = "车辆类型（如轿车）")
	private String vklType;

	@Column(type = Types.VARCHAR, length = 10, desc = "车牌号")
	private String plateNo;

	@Column(type = Types.VARCHAR, length = 15, desc = "发动机号")
	private String engineNo;

	@Column(type = Types.INTEGER, desc = "生产年份（冗余字段car_model.makeYear）")
	private Integer makeYear;

	@Column(type = Types.INTEGER, desc = "购买年份")
	private Integer buyYear;

	@Column(type = Types.INTEGER, desc = "购买月份")
	private Integer buyMonth;

	@Column(type = Types.NUMERIC, precision = 10, scale = 4, desc = "价格（单位：万）")
	private Double price;

	@Column(type = Types.NUMERIC, precision = 10, scale = 4, desc = "行驶公里数（单位：万）")
	private Double mileage;

	@Column(nullable = false, type = Types.INTEGER)
	private Integer seqNo;

	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false")
	private Boolean deleted;
	
	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false", desc="是否是默认地址")
	private Boolean defaulted;

	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date ts;

	private User user;
	
	private CarBrand carBrand;
	
	private CarSerial carSerial;
	
	private CarModel carModel;
	

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

	public Integer getModelId() {
		return modelId;
	}

	public void setModelId(Integer modelId) {
		this.modelId = modelId;
	}

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getSerialId() {
		return serialId;
	}

	public void setSerialId(Integer serialId) {
		this.serialId = serialId;
	}

	public Integer getBrandId() {
		return brandId;
	}

	public void setBrandId(Integer brandId) {
		this.brandId = brandId;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getVklIdNo() {
		return vklIdNo;
	}

	public void setVklIdNo(String vklIdNo) {
		this.vklIdNo = vklIdNo;
	}

	public String getVklType() {
		return vklType;
	}

	public void setVklType(String vklType) {
		this.vklType = vklType;
	}

	public String getPlateNo() {
		return plateNo;
	}

	public void setPlateNo(String plateNo) {
		this.plateNo = plateNo;
	}

	public String getEngineNo() {
		return engineNo;
	}

	public void setEngineNo(String engineNo) {
		this.engineNo = engineNo;
	}

	public Integer getMakeYear() {
		return makeYear;
	}

	public void setMakeYear(Integer makeYear) {
		this.makeYear = makeYear;
	}

	public Integer getBuyYear() {
		return buyYear;
	}

	public void setBuyYear(Integer buyYear) {
		this.buyYear = buyYear;
	}

	public Integer getBuyMonth() {
		return buyMonth;
	}

	public void setBuyMonth(Integer buyMonth) {
		this.buyMonth = buyMonth;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public Double getMileage() {
		return mileage;
	}

	public void setMileage(Double mileage) {
		this.mileage = mileage;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Boolean getDefaulted() {
		return defaulted;
	}

	public void setDefaulted(Boolean defaulted) {
		this.defaulted = defaulted;
	}

	public CarBrand getCarBrand() {
		return carBrand;
	}

	public void setCarBrand(CarBrand carBrand) {
		this.carBrand = carBrand;
	}

	public CarSerial getCarSerial() {
		return carSerial;
	}

	public void setCarSerial(CarSerial carSerial) {
		this.carSerial = carSerial;
	}

	public CarModel getCarModel() {
		return carModel;
	}

	public void setCarModel(CarModel carModel) {
		this.carModel = carModel;
	}

	@Override
	public String toString() {
		return "UserCar [id=" + id + ", userId=" + userId + ", modelId=" + modelId + ", modelName=" + modelName + ", name=" + name + ", serialId=" + serialId + ", brandId=" + brandId + ", color=" + color + ", vklIdNo=" + vklIdNo
				+ ", vklType=" + vklType + ", plateNo=" + plateNo + ", engineNo=" + engineNo + ", makeYear=" + makeYear + ", buyYear=" + buyYear + ", buyMonth=" + buyMonth + ", price=" + price + ", mileage=" + mileage + ", seqNo=" + seqNo
				+ ", deleted=" + deleted + ", defaulted=" + defaulted + ", ts=" + ts + ", user=" + user + ", carBrand=" + carBrand + ", carSerial=" + carSerial + ", carModel=" + carModel + "]";
	}

}
