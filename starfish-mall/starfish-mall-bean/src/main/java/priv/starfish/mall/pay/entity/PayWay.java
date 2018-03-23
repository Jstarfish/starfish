package priv.starfish.mall.pay.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonDateSerializer;
import priv.starfish.mall.comn.entity.UserAccount;

/**
 * @ProjectName:rs-ezmall-bean
 * @ClassName:PayWay
 * @Description:支付方式
 * @date: 2015年9月2日
 * @author: WJJ
 * @version: V1.0
 */
@Table(name = "pay_way", uniqueConstraints = { @UniqueConstraint(fieldNames = { "code" }) }, desc = "支付方式")
public class PayWay implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(type = Types.VARCHAR, length = 30, nullable = false)
	private String code;

	@Column(type = Types.VARCHAR, length = 30, nullable = false)
	private String name;

	@Column(type = Types.BOOLEAN, nullable = false, defaultValue = "false")
	private boolean outerWay;

	@Column(type = Types.INTEGER)
	private Integer accountId;

	@Column(name = "`desc`", type = Types.VARCHAR, length = 250)
	private String desc;

	@Column(type = Types.INTEGER, nullable = false, defaultValue = "1")
	private Integer seqNo;

	/** 启用/禁用 */
	@Column(type = Types.BOOLEAN, nullable = false, defaultValue = "false")
	private boolean disabled;

	@Column(type = Types.TIMESTAMP, nullable = false)
	@JsonSerialize(using = JsonDateSerializer.class)
	private Date ts;

	private UserAccount userAccount;

	private List<PayWayParam> payWayParamList = new ArrayList<PayWayParam>(0);

	public List<PayWayParam> getPayWayParamList() {
		return payWayParamList;
	}

	public void setPayWayParamList(List<PayWayParam> payWayParamList) {
		this.payWayParamList = payWayParamList;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public boolean isDisabled() {
		return disabled;
	}

	public void setDisabled(boolean disabled) {
		this.disabled = disabled;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public boolean isOuterWay() {
		return outerWay;
	}

	public void setOuterWay(boolean outerWay) {
		this.outerWay = outerWay;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	@Override
	public String toString() {
		return "PayWay [id=" + id + ", code=" + code + ", name=" + name + ", outerWay=" + outerWay + ", accountId=" + accountId + ", desc=" + desc + ", seqNo=" + seqNo + ", disabled=" + disabled + ", ts=" + ts + ", userAccount="
				+ userAccount + ", payWayParamList=" + payWayParamList + "]";
	}

}
