package priv.starfish.mall.comn.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.mall.merchant.entity.MerchantSettleAcct;

@Table(name = "user_account", uniqueConstraints = { @UniqueConstraint(fieldNames = { "userId", "bankCode", "acctNo" }) }, desc = "用户资金账户")
public class UserAccount implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 主键 */
	@Id(type = Types.INTEGER)
	private Integer id;

	/** 用户Id */
	@Column(nullable = false, type = Types.INTEGER)
	@ForeignKey(refEntityClass = User.class, refFieldName = "id")
	private Integer userId;

	/** 银行Code */
	@Column(nullable = false, type = Types.VARCHAR, length = 15)
	private String bankCode;

	/** 银行名称 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String bankName;

	/** 0 : 默认 对私C型， 1 对公账户B型； */
	@Column(nullable = false, type = Types.INTEGER)
	private Integer typeFlag;

	/** 账户编号 */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String acctNo;

	/** B型账户 acctName 为单位名称，C型为个人真实姓名（不一定是用户本人的） */
	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String acctName;

	/** 对公打款验证码 */
	@Column(type = Types.VARCHAR, length = 30)
	private String vfCode;
	
	/** 预留电话 */
	@Column(type = Types.VARCHAR, length = 15)
	private String phoneNo;
	
	/** 认证 */
	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false")
	private Boolean verified;
	
	/** 可用*/
	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false")
	private Boolean disabled;

	@Column(type = Types.VARCHAR, length = 30, desc="省份code")
	private String provinceCode;
	
	@Column(type = Types.VARCHAR, length = 60, desc="开户银行全名")
	private String bankFullName;
	
	@Column(type = Types.VARCHAR, length = 60, desc="开户银行行号")
	private String relatedBankNo;
	
	/** 时间戳 */
	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date ts;
	
	private MerchantSettleAcct merchantSettleAcct;

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

	public Integer getTypeFlag() {
		return typeFlag;
	}

	public void setTypeFlag(Integer typeFlag) {
		this.typeFlag = typeFlag;
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

	public String getVfCode() {
		return vfCode;
	}

	public void setVfCode(String vfCode) {
		this.vfCode = vfCode;
	}

	public String getPhoneNo() {
		return phoneNo;
	}

	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}

	public Boolean getVerified() {
		return verified;
	}

	public void setVerified(Boolean verified) {
		this.verified = verified;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public MerchantSettleAcct getMerchantSettleAcct() {
		return merchantSettleAcct;
	}

	public void setMerchantSettleAcct(MerchantSettleAcct merchantSettleAcct) {
		this.merchantSettleAcct = merchantSettleAcct;
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
		return "UserAccount [id=" + id + ", userId=" + userId + ", bankCode=" + bankCode + ", bankName=" + bankName + ", typeFlag=" + typeFlag + ", acctNo=" + acctNo + ", acctName=" + acctName + ", vfCode=" + vfCode + ", phoneNo=" + phoneNo
				+ ", verified=" + verified + ", disabled=" + disabled + ", provinceCode=" + provinceCode + ", bankFullName=" + bankFullName + ", relatedBankNo=" + relatedBankNo + ", ts=" + ts + ", merchantSettleAcct=" + merchantSettleAcct
				+ "]";
	}

}
