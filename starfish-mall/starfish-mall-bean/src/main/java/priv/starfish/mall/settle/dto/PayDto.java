package priv.starfish.mall.settle.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class PayDto {

	/** 订单编号 */
	private String orderNo;
	/** 订单主题 || 名称 || 商品名称 */
	private String subject;
	/** 订单描述 */
	private String body;
	/** 付款金额 */
	private String amount;
	/** 交易状态 */
	private String tradeStatus;
	/** 支付宝交易号 */
	private String tradeNo;
	/** 付款时间 */
	private String payTime;
	/** 公共回传参数 */
	private String extraParam;

	/** 商家名称 */
	private String merchantName;
	/** 预约时间 */
	private Date planTime;
	/** 商品展示地址 */
	private String showUrl;

	/** 网银支付方法所选银行简码 */
	private String bkcode;

	/** 退款笔数 */
	private String refundNum;
	/** 退款订单号 */
	private List<String> refundNos;

	/** 转账笔数 */
	private String transferNum;
	/** 转账总金额 */
	private BigDecimal settleAmount;
	/** 转账流程表ids */
	private List<Long> transferIds;

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getExtraParam() {
		return extraParam;
	}

	public void setExtraParam(String extraParam) {
		this.extraParam = extraParam;
	}

	public String getMerchantName() {
		return merchantName;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
	}

	public Date getPlanTime() {
		return planTime;
	}

	public void setPlanTime(Date planTime) {
		this.planTime = planTime;
	}

	public String getShowUrl() {
		return showUrl;
	}

	public void setShowUrl(String showUrl) {
		this.showUrl = showUrl;
	}

	public String getBkcode() {
		return bkcode;
	}

	public void setBkcode(String bkcode) {
		this.bkcode = bkcode;
	}

	public String getRefundNum() {
		return refundNum;
	}

	public void setRefundNum(String refundNum) {
		this.refundNum = refundNum;
	}

	public List<String> getRefundNos() {
		return refundNos;
	}

	public void setRefundNos(List<String> refundNos) {
		this.refundNos = refundNos;
	}

	public String getTransferNum() {
		return transferNum;
	}

	public void setTransferNum(String transferNum) {
		this.transferNum = transferNum;
	}

	public BigDecimal getSettleAmount() {
		return settleAmount;
	}

	public void setSettleAmount(BigDecimal settleAmount) {
		this.settleAmount = settleAmount;
	}

	public List<Long> getTransferIds() {
		return transferIds;
	}

	public void setTransferIds(List<Long> transferIds) {
		this.transferIds = transferIds;
	}

}
