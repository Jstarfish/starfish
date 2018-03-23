package priv.starfish.mall.order.po;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import priv.starfish.common.json.JsonNullDeserializer;
import priv.starfish.common.json.JsonShortDateTimeDeserializer;
import priv.starfish.mall.shop.dto.ShopDto;

/**
 * 代理下单涉及的上下文信息
 * 
 * @author wangdi
 * @date 2015年10月22日 上午11:38:39
 *
 */
public class SaleOrderPo implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/**
	 * 订单ID
	 */
	private Long id;
	
	/**
	 * 订单No
	 */
	private String no;
	/**
	 * 套餐ID
	 */
	private Integer packId;
	/** 用户ID */
	private Integer userId;

	/** 用户手机号 */
	private String userPhone;

	/** 用户名称 */
	private String userName;

	/** 用户关联所有车辆信息 */
	// private List<Car> userCars;

	/** 用户关联所有车型信息 */
	// private List<CarModel> userCarModels;

	/** 接受服务车辆的ID */
	private Integer carId;

	/** 接受服务车辆的名称 */
	private String carName;

	/** 接受服务车型的ID */
	// private Integer carModelId;

	/** 接受服务车型的ID */
	private String carModel;

	/** 店铺ID */
	private Integer shopId;

	/** 店铺名称 */
	private String shopName;

	/** 联系人 */
	private String linkMan;

	/** 联系电话 */
	private String linkNo;

	/** 预约时间 */
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date planTime;

	/** 用户选择的所有服务的ID */
	private List<Integer> svcIds;

	/** 自选服务订单中用户选择的所有货品的ID */
	private List<Long> productIds;

	/** 留言 */
	private String leaveMsg;

	/** 备注 */
	private String memo;

	/** 支付方式 */
	private String payWay;

	/** 代理下单操作人员ID */
	private Integer actorId;

	/** 用户优惠券ID */
	private Integer userCouponId;

	/** 选中的门店 */
	private ShopDto shopDto;

	/** 用户e卡id */
	private Integer userEcardId;

	/** 支付密码 */
	private String payPassword;
	
	/** 是否已分配给合作店（如果已分配，加盟店只能查看，不能服务） */
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Boolean distFlag;

	public Integer getUser1d() {
		return userId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
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

	public String getCarModel() {
		return carModel;
	}

	public void setCarModel(String carModel) {
		this.carModel = carModel;
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

	public String getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	public String getLinkNo() {
		return linkNo;
	}

	public void setLinkNo(String linkNo) {
		this.linkNo = linkNo;
	}

	public Date getPlanTime() {
		return planTime;
	}

	public void setPlanTime(Date planTime) {
		this.planTime = planTime;
	}

	public List<Integer> getSvcIds() {
		return svcIds;
	}

	public void setSvcIds(List<Integer> svcIds) {
		this.svcIds = svcIds;
	}

	public List<Long> getProductIds() {
		return productIds;
	}

	public void setProductIds(List<Long> productIds) {
		this.productIds = productIds;
	}

	public String getLeaveMsg() {
		return leaveMsg;
	}

	public void setLeaveMsg(String leaveMsg) {
		this.leaveMsg = leaveMsg;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getPayWay() {
		return payWay;
	}

	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}

	public Integer getActorId() {
		return actorId;
	}

	public void setActorId(Integer actorId) {
		this.actorId = actorId;
	}

	public Integer getUserCouponId() {
		return userCouponId;
	}

	public void setUserCouponId(Integer userCouponId) {
		this.userCouponId = userCouponId;
	}

	public ShopDto getShopDto() {
		return shopDto;
	}

	public void setShopDto(ShopDto shopDto) {
		this.shopDto = shopDto;
	}

	public String getPayPassword() {
		return payPassword;
	}

	public void setPayPassword(String payPassword) {
		this.payPassword = payPassword;
	}

	public Integer getUserEcardId() {
		return userEcardId;
	}

	public void setUserEcardId(Integer userEcardId) {
		this.userEcardId = userEcardId;
	}

	public Integer getPackId() {
		return packId;
	}

	public void setPackId(Integer packId) {
		this.packId = packId;
	}
	
	public Boolean getDistFlag() {
		return distFlag;
	}

	public void setDistFlag(Boolean distFlag) {
		this.distFlag = distFlag;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	@Override
	public String toString() {
		return "SaleOrderPo [id=" + id + ", no=" + no + ", packId=" + packId + ", userId=" + userId + ", userPhone=" + userPhone + ", userName=" + userName + ", carId=" + carId + ", carName=" + carName + ", carModel=" + carModel
				+ ", shopId=" + shopId + ", shopName=" + shopName + ", linkMan=" + linkMan + ", linkNo=" + linkNo + ", planTime=" + planTime + ", svcIds=" + svcIds + ", productIds=" + productIds + ", leaveMsg=" + leaveMsg + ", memo=" + memo
				+ ", payWay=" + payWay + ", actorId=" + actorId + ", userCouponId=" + userCouponId + ", shopDto=" + shopDto + ", userEcardId=" + userEcardId + ", payPassword=" + payPassword + ", distFlag=" + distFlag + "]";
	}
	
}
