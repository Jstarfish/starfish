package priv.starfish.mall.comn.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonDateDeserializer;
import priv.starfish.common.json.JsonDateSerializer;
import priv.starfish.common.json.JsonShortDateTimeDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;

/**
 * 店铺营业执照
 * 
 * @author 郝江奎
 * @date 2015-10-9 上午10:30:07
 * 
 */
@Table(name = "biz_license", uniqueConstraints = { @UniqueConstraint(fieldNames = { "userId", "regNo" }) }, desc = "店铺营业执照")
public class BizLicense implements Serializable {

	private static final long serialVersionUID = -515348260548242614L;

	@Id(type = Types.INTEGER, desc="店铺ID")
	private Integer id;

	@Column(nullable = false, type = Types.INTEGER, desc="商户ID")
	private Integer userId;
	
	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc="注册号")
	private String regNo;

	@Column(type = Types.VARCHAR, length = 30, desc="组织机构编号")
	private String orgNo;
	
	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc="名称")
	private String name;
	
	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc="类型")
	private String type;
	
	@Column(nullable = false, type = Types.VARCHAR, length = 100, desc="住所")
	private String address;
	
	@Column(nullable = false, type = Types.VARCHAR, length = 30, desc="法定代表人")
	private String legalMan;
	
	@Column(nullable = false, type = Types.DECIMAL, precision = 18, scale = 2, desc="注册资本")
	private BigDecimal regCapital;
	
	@Column(nullable = false, type = Types.DATE, desc="成立日期")
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date estabDate;
	
	@Column(nullable = false, type = Types.DATE, desc="起始日期")
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date startDate;
	
	@Column(nullable = false, type = Types.DATE, desc="结束日期")
	@JsonSerialize(using = JsonDateSerializer.class)
	@JsonDeserialize(using = JsonDateDeserializer.class)
	private Date endDate;
	
	@Column(nullable = false, type = Types.VARCHAR, length = 1000, desc="经营范围")
	private String bizScope;
	
	@Column(nullable = false, type = Types.TIMESTAMP, desc="时间戳")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date ts;
	
	/** 营业执照图片 */
	private BizLicensePic bizLicensePic;

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

	public String getRegNo() {
		return regNo;
	}

	public void setRegNo(String regNo) {
		this.regNo = regNo;
	}

	public String getOrgNo() {
		return orgNo;
	}

	public void setOrgNo(String orgNo) {
		this.orgNo = orgNo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getLegalMan() {
		return legalMan;
	}

	public void setLegalMan(String legalMan) {
		this.legalMan = legalMan;
	}

	public BigDecimal getRegCapital() {
		return regCapital;
	}

	public void setRegCapital(BigDecimal regCapital) {
		this.regCapital = regCapital;
	}

	public Date getEstabDate() {
		return estabDate;
	}

	public void setEstabDate(Date estabDate) {
		this.estabDate = estabDate;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getBizScope() {
		return bizScope;
	}

	public void setBizScope(String bizScope) {
		this.bizScope = bizScope;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public BizLicensePic getBizLicensePic() {
		return bizLicensePic;
	}

	public void setBizLicensePic(BizLicensePic bizLicensePic) {
		this.bizLicensePic = bizLicensePic;
	}

	@Override
	public String toString() {
		return "BizLicense [id=" + id + ", userId=" + userId + ", regNo=" + regNo + ", orgNo=" + orgNo + ", name=" + name + ", type=" + type + ", address=" + address + ", legalMan=" + legalMan + ", regCapital=" + regCapital + ", estabDate="
				+ estabDate + ", startDate=" + startDate + ", endDate=" + endDate + ", bizScope=" + bizScope + ", ts=" + ts + ", bizLicensePic=" + bizLicensePic + "]";
	}

}
