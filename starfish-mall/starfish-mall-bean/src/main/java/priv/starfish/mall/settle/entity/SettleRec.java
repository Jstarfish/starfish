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
@Table(name = "settle_rec")
public class SettleRec implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(type = Types.INTEGER, desc = "主键 ")
	private Integer id;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "主题/科目 order")
	private String subject;

	@Column(nullable = false, type = Types.INTEGER, desc = "结算对象类型, 默认为0：商户，1：代理商")
	private Integer peerType;

	@Column(nullable = false, type = Types.INTEGER, desc = "结算对象id，比如供应商id")
	private Integer peerId;

	@Column(type = Types.VARCHAR, length = 30, desc = "冗余字段")
	private String peerName;

	@Column(type = Types.VARCHAR, length = 15)
	private String bankCode;

	@Column(type = Types.VARCHAR, length = 30)
	private String bankName;

	@Column(type = Types.VARCHAR, length = 30)
	private String acctNo;

	@Column(type = Types.VARCHAR, length = 30)
	private String acctName;

	@Column(nullable = false, type = Types.INTEGER, desc = "方向标记： 1：打款，-1 收款")
	private Integer directFlag;

	@Column(nullable = false, type = Types.DECIMAL, precision = 18, scale = 4, desc = "结算金额")
	private BigDecimal amount;

	@Column(type = Types.VARCHAR, length = 250, desc = "结算单据信息（手工填写）")
	private String billExtra;

	@Column(nullable = false, type = Types.INTEGER, desc = "操作人员id")
	private Integer operatorId;

	@Column(nullable = false, type = Types.VARCHAR, length = 15, desc = "操作人员realName/nickName")
	private String operatorName;

	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false", desc = "结算信息是否已确认，比如供应商确认")
	private Boolean confirmed;

	@Column(nullable = false, type = Types.INTEGER, desc = "默认为0：未打款，1：已打款，-1：打款失败")
	private Integer state;

	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date ts;

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

	@Override
	public String toString() {
		return "SettleRec [id=" + id + ", subject=" + subject + ", peerType=" + peerType + ", peerId=" + peerId + ", peerName=" + peerName + ", bankCode=" + bankCode + ", bankName=" + bankName + ", acctNo=" + acctNo + ", acctName="
				+ acctName + ", directFlag=" + directFlag + ", amount=" + amount + ", billExtra=" + billExtra + ", operatorId=" + operatorId + ", operatorName=" + operatorName + ", confirmed=" + confirmed + ", state=" + state + ", ts=" + ts
				+ "]";
	}

}
