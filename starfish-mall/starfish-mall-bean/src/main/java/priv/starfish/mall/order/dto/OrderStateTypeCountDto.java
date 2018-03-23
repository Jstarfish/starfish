package priv.starfish.mall.order.dto;

public class OrderStateTypeCountDto {
	
	/**订单总数量*/
	private Integer totalCount;
	
	/**订单待处理数量*/
	private Integer unhandledCount;
	
	/**订单完成数量*/
	private Integer finishedCount;
	
	/**订单待享受数量*/
	private Integer processingCount;
	
	/**订单取消数量*/
	private Integer cancelledCount;
	

	public Integer getCancelledCount() {
		return cancelledCount;
	}
	public void setCancelledCount(Integer cancelledCount) {
		this.cancelledCount = cancelledCount;
	}
	public Integer getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(Integer totalCount) {
		this.totalCount = totalCount;
	}
	public Integer getUnhandledCount() {
		return unhandledCount;
	}
	public void setUnhandledCount(Integer unhandledCount) {
		this.unhandledCount = unhandledCount;
	}
	public Integer getFinishedCount() {
		return finishedCount;
	}
	public void setFinishedCount(Integer finishedCount) {
		this.finishedCount = finishedCount;
	}
	public Integer getProcessingCount() {
		return processingCount;
	}
	public void setProcessingCount(Integer processingCount) {
		this.processingCount = processingCount;
	}
	@Override
	public String toString() {
		return "OrderStateTypeCountDto [totalCount=" + totalCount + ", unhandledCount=" + unhandledCount
				+ ", finishedCount=" + finishedCount + ", processingCount=" + processingCount + ", cancelledCount="
				+ cancelledCount + "]";
	}

	

}
