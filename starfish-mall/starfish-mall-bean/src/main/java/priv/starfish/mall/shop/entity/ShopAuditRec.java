package priv.starfish.mall.shop.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.json.JsonShortDateTimeSerializer;

@Table(name = "shop_audit_rec", desc = "店铺审核记录表")
public class ShopAuditRec implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id(type = Types.INTEGER, desc = "主键==用户Id")
	private Integer id;

	@Column(type = Types.INTEGER, desc = "店铺Id")
	private Integer shopId;
	
	@Column(type = Types.VARCHAR, desc = "店铺名称")
	private String shopName;
	
	@Column(nullable = false, type = Types.INTEGER, defaultValue = "0", desc = "审核状态： 0 未审核, 1 审核通过， 2 审核未通过")
	private Integer auditStatus;
	
	@Column(type = Types.TIMESTAMP, desc = "审核时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	private Date auditTime;
	
	@Column(type = Types.INTEGER, desc = "审核人Id")
	private Integer auditorId;
	
	@Column(type = Types.VARCHAR, length = 30, desc = "审核人")
	private String auditorName;
	
	@Column(name = "`desc`", type = Types.VARCHAR, length = 250, desc = "描述")
	private String desc;
	
	private String phoneNo;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getShopId() {
		return shopId;
	}
	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}
	public Integer getAuditStatus() {
		return auditStatus;
	}
	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}
	public Date getAuditTime() {
		return auditTime;
	}
	public void setAuditTime(Date auditTime) {
		this.auditTime = auditTime;
	}
	public Integer getAuditorId() {
		return auditorId;
	}
	public void setAuditorId(Integer auditorId) {
		this.auditorId = auditorId;
	}
	public String getAuditorName() {
		return auditorName;
	}
	public void setAuditorName(String auditorName) {
		this.auditorName = auditorName;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public String getShopName() {
		return shopName;
	}
	
	public void setShopName(String shopName) {
		this.shopName = shopName;
	}
	
	public String getPhoneNo() {
		return phoneNo;
	}
	
	public void setPhoneNo(String phoneNo) {
		this.phoneNo = phoneNo;
	}
	
	@Override
	public String toString() {
		return "ShopAuditRec [id=" + id + ", shopId=" + shopId + ", shopName=" + shopName + ", auditStatus=" + auditStatus + ", auditTime=" + auditTime + ", auditorId=" + auditorId + ", auditorName=" + auditorName + ", desc=" + desc
				+ ", phoneNo=" + phoneNo + "]";
	}
}
