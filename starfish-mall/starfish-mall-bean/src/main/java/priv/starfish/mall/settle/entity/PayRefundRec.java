package priv.starfish.mall.settle.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.json.JsonDateTimeSerializer;
import priv.starfish.common.json.JsonNullDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;

/**
 * 支付退款记录表
 * 
 * @author "WJJ"
 * @date 2015年11月7日 下午4:19:19
 *
 */
@Table(name = "pay_refund_rec")
public class PayRefundRec implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(type = Types.BIGINT)
	private Long id;

	@Column(nullable = false, type = Types.BIGINT, desc = "订单号ID")
	private Long orderId;

	@Column(nullable = false, type = Types.VARCHAR, length = 20, desc = "订单编号no，冗余")
	private String no;

	@Column(type = Types.VARCHAR, length = 20, desc = "为农行跳银联实际用的，no+3位随机数，退款用(农行)")
	private String noForAbc;

	@Column(nullable = false, type = Types.INTEGER, desc = "用户ID")
	private Integer userId;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "支付金额")
	private String totalFee;

	@Column(type = Types.VARCHAR, length = 30, desc = "退款金额(微信)")
	private String refundFee;

	@Column(nullable = false, type = Types.VARCHAR, length = 256, desc = "商品的标题/交易标题/订单标题/订单关键字等")
	private String subject;

	@Column(type = Types.VARCHAR, length = 1000, desc = "该笔订单的备注、描述、明细等")
	private String orderDesc;

	@Column(nullable = false, type = Types.VARCHAR, length = 20, desc = "支付方式")
	private String payWayName;

	@Column(type = Types.VARCHAR, length = 20, desc = "退款渠道(微信)")
	private String refundChannel;

	@Column(type = Types.VARCHAR, length = 64, desc = "退款入账账户(微信)")
	private String refundReceiveAccount;

	@Column(type = Types.VARCHAR, length = 64, desc = "支付交易号")
	private String tradeNo;

	@Column(type = Types.VARCHAR, length = 30, desc = "交易状态")
	private String tradeStatus;

	@Column(type = Types.VARCHAR, length = 30, desc = "交易类型(微信：JSAPI、NATIVE、APP)")
	private String tradeType;

	@Column(type = Types.VARCHAR, length = 30, desc = "付款银行(微信)")
	private String bankType;

	@Column(type = Types.VARCHAR, length = 30, desc = "用户标识(微信)")
	private String openId;

	@Column(type = Types.VARCHAR, length = 30, desc = "支付时间，格式为yyyy-MM-dd HH:mm:ss")
	private String payTime;

	@Column(type = Types.VARCHAR, length = 34, desc = "退款批次号")
	private String batchNo;

	@Column(type = Types.VARCHAR, length = 34, desc = "商户退款订单编号(农行、微信)")
	private String refundOrderNo;

	@Column(type = Types.VARCHAR, length = 34, desc = "微信退款订单编号(微信)")
	private String refundNo;

	@Column(type = Types.VARCHAR, length = 34, desc = "退款交易凭证号(农行)")
	private String voucherNo;

	@Column(type = Types.VARCHAR, length = 34, desc = "退款银行返回交易流水号(农行)")
	private String iRspRef;

	@Column(type = Types.VARCHAR, length = 30, desc = "退款执行时间，格式为yyyy-MM-dd HH:mm:ss")
	private String refundTime;

	@Column(type = Types.TIMESTAMP, desc = "申请退款时间")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date applyRefundTime;

	@Column(type = Types.VARCHAR, length = 100, desc = "申请退款描述")
	private String applyRefundDesc;

	@Column(type = Types.TIMESTAMP, desc = "审核退款时间")
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	private Date auditRefundTime;

	@Column(type = Types.VARCHAR, length = 100, desc = "审核退款描述")
	private String auditRefundDesc;

	@Column(type = Types.DATE, desc = "冗余时间标识字段")
	private Date tempDay;

	@Column(type = Types.DATE, desc = "可退款的最大日期")
	private Date canRefundDay;

	/** 0:退款成功，1：退款失败，2：退款中，3：同意退款，4：拒绝退款，6：已申请退款, (微信7: 转入代发) **/
	@Column(type = Types.INTEGER, desc = "退款状态")
	private Integer refundStatus;

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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getOrderDesc() {
		return orderDesc;
	}

	public void setOrderDesc(String orderDesc) {
		this.orderDesc = orderDesc;
	}

	public String getTradeNo() {
		return tradeNo;
	}

	public void setTradeNo(String tradeNo) {
		this.tradeNo = tradeNo;
	}

	public String getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(String tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getBankType() {
		return bankType;
	}

	public void setBankType(String bankType) {
		this.bankType = bankType;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getRefundTime() {
		return refundTime;
	}

	public void setRefundTime(String refundTime) {
		this.refundTime = refundTime;
	}

	public Integer getRefundStatus() {
		return refundStatus;
	}

	public void setRefundStatus(Integer refundStatus) {
		this.refundStatus = refundStatus;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public String getPayWayName() {
		return payWayName;
	}

	public void setPayWayName(String payWayName) {
		this.payWayName = payWayName;
	}

	public Date getTempDay() {
		return tempDay;
	}

	public void setTempDay(Date tempDay) {
		this.tempDay = tempDay;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public Date getCanRefundDay() {
		return canRefundDay;
	}

	public void setCanRefundDay(Date canRefundDay) {
		this.canRefundDay = canRefundDay;
	}

	public Date getApplyRefundTime() {
		return applyRefundTime;
	}

	public void setApplyRefundTime(Date applyRefundTime) {
		this.applyRefundTime = applyRefundTime;
	}

	public Date getAuditRefundTime() {
		return auditRefundTime;
	}

	public void setAuditRefundTime(Date auditRefundTime) {
		this.auditRefundTime = auditRefundTime;
	}

	public String getApplyRefundDesc() {
		return applyRefundDesc;
	}

	public void setApplyRefundDesc(String applyRefundDesc) {
		this.applyRefundDesc = applyRefundDesc;
	}

	public String getAuditRefundDesc() {
		return auditRefundDesc;
	}

	public void setAuditRefundDesc(String auditRefundDesc) {
		this.auditRefundDesc = auditRefundDesc;
	}

	public String getRefundOrderNo() {
		return refundOrderNo;
	}

	public void setRefundOrderNo(String refundOrderNo) {
		this.refundOrderNo = refundOrderNo;
	}

	public String getVoucherNo() {
		return voucherNo;
	}

	public void setVoucherNo(String voucherNo) {
		this.voucherNo = voucherNo;
	}

	public String getiRspRef() {
		return iRspRef;
	}

	public void setiRspRef(String iRspRef) {
		this.iRspRef = iRspRef;
	}

	public String getNoForAbc() {
		return noForAbc;
	}

	public void setNoForAbc(String noForAbc) {
		this.noForAbc = noForAbc;
	}

	public String getRefundFee() {
		return refundFee;
	}

	public void setRefundFee(String refundFee) {
		this.refundFee = refundFee;
	}

	public String getRefundChannel() {
		return refundChannel;
	}

	public void setRefundChannel(String refundChannel) {
		this.refundChannel = refundChannel;
	}

	public String getRefundNo() {
		return refundNo;
	}

	public void setRefundNo(String refundNo) {
		this.refundNo = refundNo;
	}

	public String getRefundReciveAccount() {
		return refundReceiveAccount;
	}

	public void setRefundReceiveAccount(String refundReceiveAccount) {
		this.refundReceiveAccount = refundReceiveAccount;
	}

	@Override
	public String toString() {
		return "PayRefundRec [id=" + id + ", orderId=" + orderId + ", no=" + no + ", noForAbc=" + noForAbc + ", userId=" + userId + ", totalFee=" + totalFee + ", refundFee=" + refundFee + ", subject=" + subject + ", orderDesc=" + orderDesc
				+ ", payWayName=" + payWayName + ", refundChannel=" + refundChannel + ", refundReceiveAccount=" + refundReceiveAccount + ", tradeNo=" + tradeNo + ", tradeStatus=" + tradeStatus + ", tradeType=" + tradeType + ", bankType="
				+ bankType + ", openId=" + openId + ", payTime=" + payTime + ", batchNo=" + batchNo + ", refundOrderNo=" + refundOrderNo + ", refundNo=" + refundNo + ", voucherNo=" + voucherNo + ", iRspRef=" + iRspRef + ", refundTime="
				+ refundTime + ", applyRefundTime=" + applyRefundTime + ", applyRefundDesc=" + applyRefundDesc + ", auditRefundTime=" + auditRefundTime + ", auditRefundDesc=" + auditRefundDesc + ", tempDay=" + tempDay + ", canRefundDay="
				+ canRefundDay + ", refundStatus=" + refundStatus + ", ts=" + ts + "]";
	}

}
