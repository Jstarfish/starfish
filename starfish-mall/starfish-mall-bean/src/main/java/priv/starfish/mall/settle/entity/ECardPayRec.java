package priv.starfish.mall.settle.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.json.JsonNullDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;

/**
 * 购买E卡（E卡订单）支付记录
 * 
 * @author "WJJ"
 * @date 2015年11月18日 上午10:59:07
 *
 */
@Table(name = "ecard_pay_rec")
public class ECardPayRec implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(type = Types.BIGINT)
	private Long id;

	@Column(nullable = false, type = Types.VARCHAR, length = 20, desc = "订单号")
	private String no;

	@Column(nullable = false, type = Types.INTEGER, desc = "用户ID")
	private Integer userId;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "支付金额")
	private String totalFee;

	@Column(nullable = false, type = Types.VARCHAR, length = 256, desc = "商品的标题/交易标题/订单标题/订单关键字等")
	private String subject;

	@Column(type = Types.VARCHAR, length = 400, desc = "该笔订单的备注、描述、明细等")
	private String orderDesc;

	@Column(nullable = false, type = Types.VARCHAR, length = 20, desc = "支付方式")
	private String payWayName;

	@Column(type = Types.VARCHAR, length = 64, desc = "(支付宝)交易号")
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

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
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

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
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

	@Override
	public String toString() {
		return "ECardPayRec [id=" + id + ", no=" + no + ", userId=" + userId + ", totalFee=" + totalFee + ", subject=" + subject + ", orderDesc=" + orderDesc + ", payWayName=" + payWayName + ", tradeNo=" + tradeNo + ", tradeStatus="
				+ tradeStatus + ", tradeType=" + tradeType + ", bankType=" + bankType + ", openId=" + openId + ", payTime=" + payTime + ", ts=" + ts + "]";
	}

}
