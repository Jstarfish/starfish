package priv.starfish.mall.settle.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
 * 结算记录（对商户）
 * 
 * @author "WJJ"
 * @date 2015年10月18日 下午12:08:21
 *
 */
@Table(name = "sale_settle_rec")
public class SaleSettleRec implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(type = Types.INTEGER, desc = "主键 ")
	private Integer id;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "主题/科目 order")
	private String subject;

	@Column(nullable = false, type = Types.INTEGER, desc = "结算对象类型, 默认为0：商户，1：代理商")
	private Integer peerType;

	@Column(nullable = false, type = Types.BIGINT, desc = "结算单ID")
	private Long settleProcessId;

	@Column(nullable = false, type = Types.INTEGER, desc = "结算对象id，比如商户id")
	private Integer peerId;

	@Column(type = Types.VARCHAR, length = 30, desc = "冗余字段")
	private String peerName;

	@Column(type = Types.VARCHAR, length = 15, desc = "银行code")
	private String bankCode;

	@Column(type = Types.VARCHAR, length = 30, desc = "银行名称")
	private String bankName;

	@Column(type = Types.VARCHAR, length = 30, desc = "银行账号")
	private String acctNo;

	@Column(type = Types.VARCHAR, length = 30, desc = "账号名称")
	private String acctName;

	@Column(nullable = false, type = Types.INTEGER, desc = "方向标记： 1：打款，-1 收款")
	private Integer directFlag;

	@Column(nullable = false, type = Types.DECIMAL, precision = 18, scale = 4, desc = "结算金额")
	private BigDecimal amount;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "(请求)转账流水号")
	private String reqNo;

	@Column(type = Types.VARCHAR, length = 250, desc = "结算单据信息（手工填写）")
	private String billExtra;

	@Column(nullable = false, type = Types.INTEGER, desc = "操作人员id")
	private Integer operatorId;

	@Column(nullable = false, type = Types.VARCHAR, length = 15, desc = "操作人员realName/nickName")
	private String operatorName;

	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false", desc = "结算信息是否已确认，比如供应商确认")
	private Boolean confirmed;

	@Column(nullable = false, type = Types.INTEGER, desc = "(0：待结算，1：待打款，2：有异议，）3：已结算，4:结算失败，-1：已关闭")
	private Integer state;

	@Column(type = Types.DATE, desc = "结算日期")
	private Date settleDay;

	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date ts;

	/** 关联结算单信息 **/
	private SettleProcess settleProcess;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Integer getPeerType() {
		return peerType;
	}

	public void setPeerType(Integer peerType) {
		this.peerType = peerType;
	}

	public Integer getPeerId() {
		return peerId;
	}

	public void setPeerId(Integer peerId) {
		this.peerId = peerId;
	}

	public String getPeerName() {
		return peerName;
	}

	public void setPeerName(String peerName) {
		this.peerName = peerName;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getAcctNo() {
		return acctNo;
	}

	public void setAcctNo(String acctNo) {
		this.acctNo = acctNo;
	}

	public String getAcctName() {
		return acctName;
	}

	public void setAcctName(String acctName) {
		this.acctName = acctName;
	}

	public Integer getDirectFlag() {
		return directFlag;
	}

	public void setDirectFlag(Integer directFlag) {
		this.directFlag = directFlag;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getBillExtra() {
		return billExtra;
	}

	public void setBillExtra(String billExtra) {
		this.billExtra = billExtra;
	}

	public Integer getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(Integer operatorId) {
		this.operatorId = operatorId;
	}

	public String getOperatorName() {
		return operatorName;
	}

	public void setOperatorName(String operatorName) {
		this.operatorName = operatorName;
	}

	public Boolean getConfirmed() {
		return confirmed;
	}

	public void setConfirmed(Boolean confirmed) {
		this.confirmed = confirmed;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public String getReqNo() {
		return reqNo;
	}

	public void setReqNo(String reqNo) {
		this.reqNo = reqNo;
	}

	public Long getSettleProcessId() {
		return settleProcessId;
	}

	public void setSettleProcessId(Long settleProcessId) {
		this.settleProcessId = settleProcessId;
	}

	public SettleProcess getSettleProcess() {
		return settleProcess;
	}

	public void setSettleProcess(SettleProcess settleProcess) {
		this.settleProcess = settleProcess;
	}

	public Date getSettleDay() {
		return settleDay;
	}

	public void setSettleDay(Date settleDay) {
		this.settleDay = settleDay;
	}

	@Override
	public String toString() {
		return "SaleSettleRec [id=" + id + ", subject=" + subject + ", peerType=" + peerType + ", settleProcessId=" + settleProcessId + ", peerId=" + peerId + ", peerName=" + peerName + ", bankCode=" + bankCode + ", bankName=" + bankName
				+ ", acctNo=" + acctNo + ", acctName=" + acctName + ", directFlag=" + directFlag + ", amount=" + amount + ", reqNo=" + reqNo + ", billExtra=" + billExtra + ", operatorId=" + operatorId + ", operatorName=" + operatorName
				+ ", confirmed=" + confirmed + ", state=" + state + ", settleDay=" + settleDay + ", ts=" + ts + ", settleProcess=" + settleProcess + "]";
	}

}
