package priv.starfish.mall.order.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.json.JsonNullDeserializer;
import priv.starfish.common.json.JsonShortDateTimeDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.mall.comn.entity.User;
import priv.starfish.mall.merchant.entity.Merchant;
import priv.starfish.mall.settle.entity.DistSettleRec;
import priv.starfish.mall.shop.entity.Shop;

/**
 * 销售订单
 * 
 * @author 邓华锋
 * @date 2016年1月20日 下午3:39:15
 *
 */
@Table(name = "sale_order")
public class SaleOrder implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(type = Types.BIGINT)
	private Long id;

	@Column(nullable = false, type = Types.VARCHAR, length = 20, desc = "S+16位随即码")
	private String no;

	@Column(type = Types.INTEGER, desc = "服务套餐Id")
	private Integer svcPackId;

	@Column(type = Types.VARCHAR, length = 30, desc = "服务套餐名称")
	private String svcPackName;

	@Column(nullable = false, type = Types.BOOLEAN, desc = "冗余字段，是否仅含服务（纯服务，只有这样的订单可以选择合作店,，在提交订单时设置此标记）")
	private Boolean svcOnly;

	@Column(nullable = false, type = Types.INTEGER, desc = "服务次数", defaultValue = "0")
	private Integer svcTimes;

	@Column(nullable = false, type = Types.INTEGER, desc = "用户ID")
	@ForeignKey(refEntityClass = User.class, refFieldName = "id")
	private Integer userId;

	@Column(type = Types.VARCHAR, length = 30, desc = "user.realName/nickName冗余字段")
	private String userName;

	@Column(type = Types.VARCHAR, length = 15, desc = "设备类型（下单）参考: priv.starfish.mall.comn.dict.DeviceType")
	private String deviceType;

	@Column(type = Types.VARCHAR, length = 30, desc = "车型名称")
	private String carName;

	@Column(type = Types.VARCHAR, length = 90, desc = "车型")
	private String carModel;

	@Column(type = Types.INTEGER, desc = "车型ID")
	private Integer carId;

	@Column(type = Types.BOOLEAN, desc = "是否自提")
	private Boolean pickupFlag;

	@Column(type = Types.VARCHAR, length = 15, desc = "联系电话")
	private String phoneNo;

	@Column(type = Types.VARCHAR, length = 30, desc = "联系人")
	private String linkMan;

	@Column(type = Types.VARCHAR, length = 15, desc = "备用电话（手机、固话均可）")
	private String telNo;

	@Column(nullable = false, type = Types.INTEGER, desc = "区域ID")
	private Integer regionId;

	@Column(type = Types.VARCHAR, length = 60, desc = "联系地址地区全称 冗余字段")
	private String regionName;

	@Column(type = Types.VARCHAR, length = 30, desc = "联系地址街道")
	private String street;

	@Column(type = Types.VARCHAR, length = 90, desc = "regionName + street")
	private String address;

	@Column(type = Types.NUMERIC, desc = "下单时所在经度")
	private Double lng;

	@Column(type = Types.NUMERIC, desc = "下单时所在纬度")
	private Double lat;

	@Column(type = Types.VARCHAR, length = 60, desc = "联系邮箱")
	private String email;

	@Column(type = Types.TIMESTAMP, desc = "计划收货/预定时间（对于e卡残值兑换的订单可以为空）")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date planTime;

	@Column(nullable = false, type = Types.INTEGER, defaultValue = "0", desc = "计划时间修改次数（默认为：0）")
	private Integer planModTimes;

	@Column(type = Types.VARCHAR, length = 30, desc = "留言")
	private String leaveMsg;

	@Column(nullable = false, type = Types.INTEGER, desc = "商家ID")
	@ForeignKey(refEntityClass = Merchant.class, refFieldName = "id")
	private Integer merchantId;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "merchant.name/realName 冗余字段")
	private String merchantName;

	@Column(type = Types.INTEGER, desc = "代理商id")
	private Integer agentId;

	@Column(type = Types.VARCHAR, length = 30, desc = "agent.name/realName 冗余字段 代理商名称")
	private String agentName;

	@Column(nullable = false, type = Types.INTEGER, desc = "店铺ID")
	@ForeignKey(refEntityClass = Shop.class, refFieldName = "id")
	private Integer shopId;

	@Column(type = Types.VARCHAR, length = 30, desc = "店铺名称")
	private String shopName;

	@Column(nullable = false, type = Types.BOOLEAN, desc = "是否已分配给合作店（如果已分配，加盟店只能查看，不能服务）")
	private Boolean distFlag;

	@Column(type = Types.INTEGER, desc = "分销商id")
	private Integer distributorId;

	@Column(type = Types.VARCHAR, length = 30, desc = "分销商名称")
	private String distributorName;

	@Column(type = Types.VARCHAR, length = 30, desc = "分销店铺名称")
	private String distShopName;

	@Column(type = Types.DECIMAL, precision = 18, scale = 4, desc = "销售总额[ sum(svc.saleAmount) + sum(goods.saleAmount) ]")
	private BigDecimal saleAmount;

	@Column(type = Types.DECIMAL, precision = 18, scale = 4, desc = "折扣总额[ sum(svc.discAmount) + sum(goods.discAmount) ]")
	private BigDecimal discAmount;

	@Column(type = Types.DECIMAL, precision = 18, scale = 4, desc = "结算总额[ sum(svc.amount) + sum(goods.amount) ]")
	private BigDecimal amount;

	@Column(type = Types.DECIMAL, precision = 18, scale = 4, desc = "内部支付总额")
	private BigDecimal amountInner;

	@Column(type = Types.DECIMAL, precision = 18, scale = 4, desc = "外部支付总额（用户支付）")
	private BigDecimal amountOuter;

	@Column(type = Types.VARCHAR, length = 30, desc = "支付方式（参考 pay_way . code）")
	private String payWay;

	@Column(type = Types.VARCHAR, length = 30, desc = "支付状态")
	private String payState;

	@Column(type = Types.VARCHAR, length = 30, desc = "配送状态")
	private String distState;

	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "FALSE", desc = "是否已取消")
	private Boolean cancelled;

	@Column(type = Types.VARCHAR, length = 6, desc = "服务完成确认码（与订单编号配合使用）")
	private String doneCode;

	@Column(nullable = false, type = Types.BOOLEAN, desc = "是否已完成（需要finishNo）")
	private Boolean finished;

	@Column(type = Types.TIMESTAMP, desc = "服务完成时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date finishTime;

	@Column(type = Types.VARCHAR, length = 90, desc = "订单备注（后台人员使用）")
	private String memo;

	@Column(type = Types.BOOLEAN, desc = "付款是否已确认")
	private Boolean payConfirmed;

	@Column(type = Types.VARCHAR, length = 30, desc = "支付凭据单号")
	private String payProofNo;

	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "FALSE", desc = "订单是否已关闭")
	private Boolean closed;

	@Column(type = Types.TIMESTAMP, desc = "订单关闭时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date closeTime;

	@Column(type = Types.DECIMAL, precision = 18, scale = 4, desc = "结算金额")
	private BigDecimal settleAmount;

	/**
	 * sum(sale_order_svc.distProfit)（在distFlag被设置为true时计算）
	 */
	@Column(type = Types.DECIMAL, precision = 18, scale = 4, desc = "分销总金额")
	private BigDecimal distProfit;

	@Column(type = Types.INTEGER, desc = "结算状态（冗余字段）")
	private Integer settleState;

	@Column(type = Types.INTEGER, desc = "结算记录id")
	// @ForeignKey(refEntityClass = SaleSettleRec.class, refFieldName = "id")
	private Integer settleRecId;

	@Column(type = Types.INTEGER, desc = "与合作店的结算记录id")
	private Integer settleRecId2Dist;

	@Column(nullable = false, type = Types.INTEGER, defaultValue = "0", desc = "创建者标记（0：默认用户自己，1：店铺人员，2：合作/分销店")
	private Integer creatorFlag;

	@Column(nullable = false, type = Types.INTEGER, desc = "订单创建者id")
	@ForeignKey(refEntityClass = User.class, refFieldName = "id")
	private Integer creatorId;

	@Column(type = Types.VARCHAR, length = 30, desc = "订单创建者name")
	private String creatorName;

	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP", desc = "创建时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date createTime;

	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP", desc = "变更时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date changeTime;

	@Column(type = Types.TIMESTAMP, desc = "索引时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date indexTime;

	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "FALSE", desc = "是否已删除（逻辑删除已失效的）")
	private Boolean deleted;

	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "FALSE", desc = "星标记（仅对商户可用）")
	private Boolean starFlag;

	/**
	 * 关联销售订单记录
	 */
	private List<SaleOrderRecord> saleOrderRecords;

	/**
	 * 关联销售服务
	 */
	private List<SaleOrderSvc> saleOrderSvcs;

	/**
	 * 关联商品
	 */
	private List<SaleOrderGoods> saleOrderGoods;

	/**
	 * 关联的工作人员信息
	 */
	private List<SaleOrderWork> saleOrderWorks;

	/**
	 * 关联店铺
	 */
	private Shop shop;

	/**
	 * 关联客户
	 */
	private User customer;

	private String roleName;
	
	/** 合作店结算 */
	private DistSettleRec distSettleRec;

	private String distShopPhoneNo;

	private String distShopAddress;

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

	public Integer getSvcPackId() {
		return svcPackId;
	}

	public void setSvcPackId(Integer svcPackId) {
		this.svcPackId = svcPackId;
	}

	public String getSvcPackName() {
		return svcPackName;
	}

	public void setSvcPackName(String svcPackName) {
		this.svcPackName = svcPackName;
	}

	public Boolean getSvcOnly() {
		return svcOnly;
	}

	public void setSvcOnly(Boolean svcOnly) {
		this.svcOnly = svcOnly;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getDeviceType() {
		return deviceType;
	}

	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
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

	public Integer getCarId() {
		return carId;
	}

	public void setCarId(Integer carId) {
		this.carId = carId;
	}

	public Boolean getPickupFlag() {
		return pickupFlag;
	}

	public void setPickupFlag(Boolean pickupFlag) {
		this.pickupFlag = pickupFlag;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public String getLinkMan() {
		return linkMan;
	}

	public void setLinkMan(String linkMan) {
		this.linkMan = linkMan;
	}

	public String getTelNo() {
		return telNo;
	}

	public void setTelNo(String telNo) {
		this.telNo = telNo;
	}

	public Integer getRegionId() {
		return regionId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}

	public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getPlanTime() {
		return planTime;
	}

	public void setPlanTime(Date planTime) {
		this.planTime = planTime;
	}

	public Integer getPlanModTimes() {
		return planModTimes;
	}

	public void setPlanModTimes(Integer planModTimes) {
		this.planModTimes = planModTimes;
	}

	public String getLeaveMsg() {
		return leaveMsg;
	}

	public void setLeaveMsg(String leaveMsg) {
		this.leaveMsg = leaveMsg;
	}

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public Integer getAgentId() {
		return agentId;
	}

	public void setAgentId(Integer agentId) {
		this.agentId = agentId;
	}

	public String getAgentName() {
		return agentName;
	}

	public void setAgentName(String agentName) {
		this.agentName = agentName;
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

	public Integer getDistributorId() {
		return distributorId;
	}

	public void setDistributorId(Integer distributorId) {
		this.distributorId = distributorId;
	}

	public String getDistributorName() {
		return distributorName;
	}

	public void setDistributorName(String distributorName) {
		this.distributorName = distributorName;
	}

	public String getDistShopName() {
		return distShopName;
	}

	public void setDistShopName(String distShopName) {
		this.distShopName = distShopName;
	}

	public String getPayWay() {
		return payWay;
	}

	public void setPayWay(String payWay) {
		this.payWay = payWay;
	}

	public String getPayState() {
		return payState;
	}

	public void setPayState(String payState) {
		this.payState = payState;
	}

	public String getDistState() {
		return distState;
	}

	public void setDistState(String distState) {
		this.distState = distState;
	}

	public Boolean getCancelled() {
		return cancelled;
	}

	public void setCancelled(Boolean cancelled) {
		this.cancelled = cancelled;
	}

	public String getDoneCode() {
		return doneCode;
	}

	public void setDoneCode(String doneCode) {
		this.doneCode = doneCode;
	}

	public Boolean getFinished() {
		return finished;
	}

	public void setFinished(Boolean finished) {
		this.finished = finished;
	}

	public Date getFinishTime() {
		return finishTime;
	}

	public void setFinishTime(Date finishTime) {
		this.finishTime = finishTime;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public Boolean getPayConfirmed() {
		return payConfirmed;
	}

	public void setPayConfirmed(Boolean payConfirmed) {
		this.payConfirmed = payConfirmed;
	}

	public String getPayProofNo() {
		return payProofNo;
	}

	public void setPayProofNo(String payProofNo) {
		this.payProofNo = payProofNo;
	}

	public Boolean getClosed() {
		return closed;
	}

	public void setClosed(Boolean closed) {
		this.closed = closed;
	}

	public Date getCloseTime() {
		return closeTime;
	}

	public void setCloseTime(Date closeTime) {
		this.closeTime = closeTime;
	}

	public Integer getSettleState() {
		return settleState;
	}

	public void setSettleState(Integer settleState) {
		this.settleState = settleState;
	}

	public Integer getSettleRecId() {
		return settleRecId;
	}

	public void setSettleRecId(Integer settleRecId) {
		this.settleRecId = settleRecId;
	}

	public Integer getSettleRecId2Dist() {
		return settleRecId2Dist;
	}

	public void setSettleRecId2Dist(Integer settleRecId2Dist) {
		this.settleRecId2Dist = settleRecId2Dist;
	}

	public Integer getCreatorFlag() {
		return creatorFlag;
	}

	public void setCreatorFlag(Integer creatorFlag) {
		this.creatorFlag = creatorFlag;
	}

	public Integer getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Integer creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getChangeTime() {
		return changeTime;
	}

	public void setChangeTime(Date changeTime) {
		this.changeTime = changeTime;
	}

	public Date getIndexTime() {
		return indexTime;
	}

	public void setIndexTime(Date indexTime) {
		this.indexTime = indexTime;
	}

	public Boolean getDeleted() {
		return deleted;
	}

	public void setDeleted(Boolean deleted) {
		this.deleted = deleted;
	}

	public Boolean getStarFlag() {
		return starFlag;
	}

	public void setStarFlag(Boolean starFlag) {
		this.starFlag = starFlag;
	}

	public Double getLng() {
		return lng;
	}

	public void setLng(Double lng) {
		this.lng = lng;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public BigDecimal getSaleAmount() {
		return saleAmount;
	}

	public void setSaleAmount(BigDecimal saleAmount) {
		this.saleAmount = saleAmount;
	}

	public BigDecimal getDiscAmount() {
		return discAmount;
	}

	public List<SaleOrderGoods> getSaleOrderGoods() {
		return saleOrderGoods;
	}

	public void setSaleOrderGoods(List<SaleOrderGoods> saleOrderGoods) {
		this.saleOrderGoods = saleOrderGoods;
	}

	public void setDiscAmount(BigDecimal discAmount) {
		this.discAmount = discAmount;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public BigDecimal getAmountInner() {
		return amountInner;
	}

	public void setAmountInner(BigDecimal amountInner) {
		this.amountInner = amountInner;
	}

	public BigDecimal getAmountOuter() {
		return amountOuter;
	}

	public void setAmountOuter(BigDecimal amountOuter) {
		this.amountOuter = amountOuter;
	}

	public BigDecimal getSettleAmount() {
		return settleAmount;
	}

	public void setSettleAmount(BigDecimal settleAmount) {
		this.settleAmount = settleAmount;
	}

	public List<SaleOrderRecord> getSaleOrderRecords() {
		return saleOrderRecords;
	}

	public void setSaleOrderRecords(List<SaleOrderRecord> saleOrderRecords) {
		this.saleOrderRecords = saleOrderRecords;
	}

	public List<SaleOrderSvc> getSaleOrderSvcs() {
		return saleOrderSvcs;
	}

	public void setSaleOrderSvcs(List<SaleOrderSvc> saleOrderSvcs) {
		this.saleOrderSvcs = saleOrderSvcs;
	}

	public List<SaleOrderWork> getSaleOrderWorks() {
		return saleOrderWorks;
	}

	public void setSaleOrderWorks(List<SaleOrderWork> saleOrderWorks) {
		this.saleOrderWorks = saleOrderWorks;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public User getCustomer() {
		return customer;
	}

	public void setCustomer(User customer) {
		this.customer = customer;
	}

	public Integer getSvcTimes() {
		return svcTimes;
	}

	public void setSvcTimes(Integer svcTimes) {
		this.svcTimes = svcTimes;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public BigDecimal getDistProfit() {
		return distProfit;
	}

	public void setDistProfit(BigDecimal distProfit) {
		this.distProfit = distProfit;
	}

	public DistSettleRec getDistSettleRec() {
		return distSettleRec;
	}

	public void setDistSettleRec(DistSettleRec distSettleRec) {
		this.distSettleRec = distSettleRec;
	}

	public String getDistShopPhoneNo() {
		return distShopPhoneNo;
	}

	public void setDistShopPhoneNo(String distShopPhoneNo) {
		this.distShopPhoneNo = distShopPhoneNo;
	}

	public String getDistShopAddress() {
		return distShopAddress;
	}

	public void setDistShopAddress(String distShopAddress) {
		this.distShopAddress = distShopAddress;
	}

	@Override
	public String toString() {
		return "SaleOrder [id=" + id + ", no=" + no + ", svcPackId=" + svcPackId + ", svcPackName=" + svcPackName + ", svcOnly=" + svcOnly + ", svcTimes=" + svcTimes + ", userId=" + userId + ", userName=" + userName + ", deviceType="
				+ deviceType + ", carName=" + carName + ", carModel=" + carModel + ", carId=" + carId + ", pickupFlag=" + pickupFlag + ", phoneNo=" + phoneNo + ", linkMan=" + linkMan + ", telNo=" + telNo + ", regionId=" + regionId
				+ ", regionName=" + regionName + ", street=" + street + ", address=" + address + ", lng=" + lng + ", lat=" + lat + ", email=" + email + ", planTime=" + planTime + ", planModTimes=" + planModTimes + ", leaveMsg=" + leaveMsg
				+ ", merchantId=" + merchantId + ", merchantName=" + merchantName + ", agentId=" + agentId + ", agentName=" + agentName + ", shopId=" + shopId + ", shopName=" + shopName + ", distFlag=" + distFlag + ", distributorId="
				+ distributorId + ", distributorName=" + distributorName + ", distShopName=" + distShopName + ", saleAmount=" + saleAmount + ", discAmount=" + discAmount + ", amount=" + amount + ", amountInner=" + amountInner
				+ ", amountOuter=" + amountOuter + ", payWay=" + payWay + ", payState=" + payState + ", distState=" + distState + ", cancelled=" + cancelled + ", doneCode=" + doneCode + ", finished=" + finished + ", finishTime="
				+ finishTime + ", memo=" + memo + ", payConfirmed=" + payConfirmed + ", payProofNo=" + payProofNo + ", closed=" + closed + ", closeTime=" + closeTime + ", settleAmount=" + settleAmount + ", distProfit=" + distProfit
				+ ", settleState=" + settleState + ", settleRecId=" + settleRecId + ", settleRecId2Dist=" + settleRecId2Dist + ", creatorFlag=" + creatorFlag + ", creatorId=" + creatorId + ", creatorName=" + creatorName + ", createTime="
				+ createTime + ", changeTime=" + changeTime + ", indexTime=" + indexTime + ", deleted=" + deleted + ", starFlag=" + starFlag + ", saleOrderRecords=" + saleOrderRecords + ", saleOrderSvcs=" + saleOrderSvcs
				+ ", saleOrderGoods=" + saleOrderGoods + ", saleOrderWorks=" + saleOrderWorks + ", shop=" + shop + ", customer=" + customer + ", roleName=" + roleName + ", distShopPhoneNo=" + distShopPhoneNo + ", distShopAddress="
				+ distShopAddress + "]";
	}

}
