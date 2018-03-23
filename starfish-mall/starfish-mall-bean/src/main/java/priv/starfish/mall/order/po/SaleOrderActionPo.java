package priv.starfish.mall.order.po;

import java.io.Serializable;
import java.util.List;

public class SaleOrderActionPo implements Serializable {

	private static final long serialVersionUID = 1L;

	/** action名称 */
	private String actionName;

	/** 订单ID */
	private Long orderId;

	/** 用户ID */
	private Integer userId;

	/** 服务完成确认码 */
	private String doneCode;

	/** 执行用户ID */
	private Integer actorId;

	/** 相关门店的ID */
	private Integer shopId;

	/** 用户车辆信息 */
	private String carInfo;

	/** 备注 */
	private String memo;

	/** 订单工作信息 */
	private List<Integer> workerIds;

	private String roleName;

	public String getActionName() {
		return actionName;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getDoneCode() {
		return doneCode;
	}

	public void setDoneCode(String doneCode) {
		this.doneCode = doneCode;
	}

	public Integer getActorId() {
		return actorId;
	}

	public void setActorId(Integer actorId) {
		this.actorId = actorId;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public String getCarInfo() {
		return carInfo;
	}

	public void setCarInfo(String carInfo) {
		this.carInfo = carInfo;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public List<Integer> getWorkerIds() {
		return workerIds;
	}

	public void setWorkerIds(List<Integer> workerIds) {
		this.workerIds = workerIds;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Override
	public String toString() {
		return "SaleOrderActionPo [actionName=" + actionName + ", orderId=" + orderId + ", userId=" + userId + ", doneCode=" + doneCode + ", actorId=" + actorId + ", shopId=" + shopId + ", carInfo=" + carInfo + ", memo=" + memo
				+ ", workerIds=" + workerIds + ", roleName=" + roleName + "]";
	}

}
