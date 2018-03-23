package priv.starfish.mall.settle.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.mall.merchant.entity.MerchantSettleAcct;

/**
 * 商城/平台结算方式
 * 
 * @author koqiui
 * @date 2015年12月30日 下午10:02:27
 *
 */
@Table(name = "settle_way", uniqueConstraints = { @UniqueConstraint(fieldNames = { "name" }) }, desc = "结算方式")
public class SettleWay implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(auto = false, type = Types.VARCHAR, length = 15)
	private String code;

	@Column(nullable = false, type = Types.VARCHAR, length = 30)
	private String name;

	@Column(type = Types.VARCHAR, length = 15, desc = "比如用于标记银企直连的通道（abc：代表 接农行，cmbc：代表接招行）")
	private String codeX;

	@Column(nullable = false, type = Types.INTEGER, desc = "结算周期（单位天）")
	private Integer settleX;

	@Column(name = "`desc`", type = Types.VARCHAR, length = 250)
	private String desc;

	@Column(nullable = false, type = Types.INTEGER)
	private Integer seqNo;

	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false")
	private Boolean disabled;

	@Column(nullable = false, type = Types.TIMESTAMP, defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date ts;

	private Integer userId;

	private MerchantSettleAcct merchantSettleAcct;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCodeX() {
		return codeX;
	}

	public void setCodeX(String codeX) {
		this.codeX = codeX;
	}

	public Integer getSettleX() {
		return settleX;
	}

	public void setSettleX(Integer settleX) {
		this.settleX = settleX;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
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

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "SettleWay [code=" + code + ", name=" + name + ", codeX=" + codeX + ", settleX=" + settleX + ", desc=" + desc + ", seqNo=" + seqNo + ", disabled=" + disabled + ", ts=" + ts + ", userId=" + userId + ", merchantSettleAcct="
				+ merchantSettleAcct + "]";
	}

}
