package priv.starfish.mall.comn.entity;

import java.io.Serializable;
import java.sql.Types;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;

/**
 * 银行省份代码
 * 
 * @author "WJJ"
 * @date 2016年1月26日 上午11:08:32
 *
 */
@Table(name = "bank_province_code")
public class BankProvinceCode implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(nullable = false, type = Types.VARCHAR)
	@ForeignKey(refEntityClass = Bank.class, refFieldName = "code")
	private String bankCode;

	@Column(nullable = false, type = Types.VARCHAR)
	private String province;

	@Column(type = Types.VARCHAR, length = 30, desc = "代码")
	private String code;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBankCode() {
		return bankCode;
	}

	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Override
	public String toString() {
		return "BankProvinceCode [id=" + id + ", bankCode=" + bankCode + ", province=" + province + ", code=" + code + "]";
	}

}
