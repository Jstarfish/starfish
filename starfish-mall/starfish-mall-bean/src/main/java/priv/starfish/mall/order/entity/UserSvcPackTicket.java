package priv.starfish.mall.order.entity;

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
import priv.starfish.common.json.JsonNullDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.mall.comn.entity.User;
import priv.starfish.mall.shop.entity.Shop;
import priv.starfish.mall.svcx.entity.SvcPack;

@Table(name = "user_svc_pack_ticket", uniqueConstraints = { @UniqueConstraint(fieldNames = { "userId", "orderId", "orderSvcId" }) })
public class UserSvcPackTicket implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(nullable = false, type = Types.INTEGER, desc = "用户ID")
	@ForeignKey(refEntityClass = User.class, refFieldName = "id")
	private Integer userId;

	@Column(nullable = false, type = Types.INTEGER, desc = "绑定门店ID")
	@ForeignKey(refEntityClass = Shop.class, refFieldName = "id")
	private Integer shopId;

	@Column(nullable = false, type = Types.BIGINT, desc = "订单ID")
	@ForeignKey(refEntityClass = SaleOrder.class, refFieldName = "id")
	private Long orderId;
	
	@Column(nullable = false, type = Types.VARCHAR, length = 20, desc = "订单号，S+16位随即码")
	private String orderNo;
	
	@Column(nullable = false, type = Types.BIGINT, desc = "销售订单服务ID")
	@ForeignKey(refEntityClass = SaleOrderSvc.class, refFieldName = "id")
	private Long orderSvcId;
	
	@Column(nullable = false, type = Types.INTEGER, desc = "套餐ID")
	@ForeignKey(refEntityClass = SvcPack.class, refFieldName = "id")
	private Integer svcPackId;
	
	@Column(type = Types.VARCHAR, length = 30, desc = "服务套餐名称")
	private String svcPackName;

	@Column(nullable = false, type = Types.INTEGER, desc = "服务ID")
	private Integer svcId;
	
	@Column(type = Types.VARCHAR, length = 30, desc = "服务名称")
	private String svcName;

	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "FALSE", desc = "是否已取消")
	private Boolean cancelled;

	@Column(nullable = false,type = Types.VARCHAR, length = 16, desc = "服务完成确认码")
	private String doneCode;

	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "FALSE", desc = "服务是否已确认完成")
	private Boolean finished;

	@Column(type = Types.TIMESTAMP, desc = "服务完成确认时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date finishTime;

	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "FALSE", desc = "服务票是否无效")
	private Boolean invalid;
	
	@Column(type = Types.INTEGER, desc = "操作人ID")
	private Integer actorId;

	@Column(type = Types.VARCHAR, length = 30, desc = "操作人名字")
	private String actorName;
	
	@Column(type = Types.VARCHAR, length = 30, desc = "操作人角色")
	private String actRole;

	private BigDecimal svcSalePrice;
	
	private BigDecimal rate;
	
	private User user;
	
	private Shop shop;
	
	// 服务图片
	private String fileBrowseUrl;
	
	private String fileBrowseUrlApp;
	
	private String fileBrowseUrlIcon;
	
	private String fileBrowseUrlIcon2;
	
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

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Long getOrderSvcId() {
		return orderSvcId;
	}

	public void setOrderSvcId(Long orderSvcId) {
		this.orderSvcId = orderSvcId;
	}

	public String getSvcPackName() {
		return svcPackName;
	}

	public void setSvcPackName(String svcPackName) {
		this.svcPackName = svcPackName;
	}

	public Integer getSvcId() {
		return svcId;
	}

	public void setSvcId(Integer svcId) {
		this.svcId = svcId;
	}

	public String getSvcName() {
		return svcName;
	}

	public void setSvcName(String svcName) {
		this.svcName = svcName;
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

	public Boolean getInvalid() {
		return invalid;
	}

	public void setInvalid(Boolean invalid) {
		this.invalid = invalid;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public BigDecimal getSvcSalePrice() {
		return svcSalePrice;
	}

	public void setSvcSalePrice(BigDecimal svcSalePrice) {
		this.svcSalePrice = svcSalePrice;
	}

	public BigDecimal getRate() {
		return rate;
	}

	public void setRate(BigDecimal rate) {
		this.rate = rate;
	}

	public Integer getSvcPackId() {
		return svcPackId;
	}

	public void setSvcPackId(Integer svcPackId) {
		this.svcPackId = svcPackId;
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

	public String getActRole() {
		return actRole;
	}

	public void setActRole(String actRole) {
		this.actRole = actRole;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public String getFileBrowseUrl() {
		return fileBrowseUrl;
	}

	public void setFileBrowseUrl(String fileBrowseUrl) {
		this.fileBrowseUrl = fileBrowseUrl;
	}

	public String getFileBrowseUrlApp() {
		return fileBrowseUrlApp;
	}

	public void setFileBrowseUrlApp(String fileBrowseUrlApp) {
		this.fileBrowseUrlApp = fileBrowseUrlApp;
	}

	public String getFileBrowseUrlIcon() {
		return fileBrowseUrlIcon;
	}

	public void setFileBrowseUrlIcon(String fileBrowseUrlIcon) {
		this.fileBrowseUrlIcon = fileBrowseUrlIcon;
	}

	public String getFileBrowseUrlIcon2() {
		return fileBrowseUrlIcon2;
	}

	public void setFileBrowseUrlIcon2(String fileBrowseUrlIcon2) {
		this.fileBrowseUrlIcon2 = fileBrowseUrlIcon2;
	}

	@Override
	public String toString() {
		return "UserSvcPackTicket [id=" + id + ", userId=" + userId + ", shopId=" + shopId + ", orderId=" + orderId + ", orderNo=" + orderNo + ", orderSvcId=" + orderSvcId + ", svcPackId=" + svcPackId + ", svcPackName=" + svcPackName
				+ ", svcId=" + svcId + ", svcName=" + svcName + ", cancelled=" + cancelled + ", doneCode=" + doneCode + ", finished=" + finished + ", finishTime=" + finishTime + ", invalid=" + invalid + ", actorId=" + actorId
				+ ", actorName=" + actorName + ", actRole=" + actRole + ", svcSalePrice=" + svcSalePrice + ", rate=" + rate + ", user=" + user + ", shop=" + shop + ", fileBrowseUrl=" + fileBrowseUrl + ", fileBrowseUrlApp="
				+ fileBrowseUrlApp + ", fileBrowseUrlIcon=" + fileBrowseUrlIcon + ", fileBrowseUrlIcon2=" + fileBrowseUrlIcon2 + "]";
	}
}
