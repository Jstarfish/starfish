package priv.starfish.mall.merchant.entity;

import java.io.Serializable;
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
import priv.starfish.mall.comn.entity.UserAccount;

@Table(name = "merchant_settle_acct", uniqueConstraints = { @UniqueConstraint(fieldNames = { "merchantId", "settleWayCode" }) })
public class MerchantSettleAcct implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键Id */
	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(nullable = false, type = Types.INTEGER)
	@ForeignKey(refEntityClass = Merchant.class, refFieldName = "id", desc = "商户Id")
	private Integer merchantId;

	@Column(nullable = false, type = Types.INTEGER)
	@ForeignKey(refEntityClass = UserAccount.class, refFieldName = "id", desc = "账户Id")
	private Integer accountId;

	@Column(nullable = false, type = Types.VARCHAR, length = 15, desc = "结算方式code")
	private String settleWayCode;

	@Column(nullable = false, type = Types.VARCHAR, length = 15, desc = "银行code")
	private String bankCode;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "银行名称")
	private String bankName;
	
	/** 0 : 默认 对私C型， 1 对公账户B型； */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer typeFlag;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "银行账户")
	private String acctNo;

	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc = "开户人")
	private String acctName;

	@Column(nullable = false, type = Types.INTEGER)
	private Integer settleX;

	@Column(nullable = false, type = Types.INTEGER, desc = "排序")
	private Integer seqNo;
	
	@Column(type = Types.VARCHAR, length = 3, desc="省份code")
	private String provinceCode;
	
	@Column(type = Types.VARCHAR, length = 100, desc="开户银行全名")
	private String bankFullName;
	
	@Column(type = Types.VARCHAR, length = 30, desc="开户银行行号")
	private String relatedBankNo;

	@Column(nullable = false, type = Types.TIMESTAMP, desc = "添加时间", defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date ts;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
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

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public String getSettleWayCode() {
		return settleWayCode;
	}

	public void setSettleWayCode(String settleWayCode) {
		this.settleWayCode = settleWayCode;
	}

	public Integer getSettleX() {
		return settleX;
	}

	public void setSettleX(Integer settleX) {
		this.settleX = settleX;
	}

	public Integer getTypeFlag() {
		return typeFlag;
	}

	public void setTypeFlag(Integer typeFlag) {
		this.typeFlag = typeFlag;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getBankFullName() {
		return bankFullName;
	}

	public void setBankFullName(String bankFullName) {
		this.bankFullName = bankFullName;
	}

	public String getRelatedBankNo() {
		return relatedBankNo;
	}

	public void setRelatedBankNo(String relatedBankNo) {
		this.relatedBankNo = relatedBankNo;
	}

	@Override
	public String toString() {
		return "MerchantSettleAcct [id=" + id + ", merchantId=" + merchantId + ", accountId=" + accountId + ", settleWayCode=" + settleWayCode + ", bankCode=" + bankCode + ", bankName=" + bankName + ", typeFlag=" + typeFlag + ", acctNo="
				+ acctNo + ", acctName=" + acctName + ", settleX=" + settleX + ", seqNo=" + seqNo + ", provinceCode=" + provinceCode + ", bankFullName=" + bankFullName + ", relatedBankNo=" + relatedBankNo + ", ts=" + ts + "]";
	}

}
