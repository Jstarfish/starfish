package priv.starfish.mall.shop.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
import priv.starfish.mall.svcx.entity.Svcx;

/**
 * 卫星店服务
 * 
 * @author 邓华锋
 * @date 2016年02月20日 15:31:54
 *
 */
@Table(name = "dist_shop_svc", uniqueConstraints = @UniqueConstraint(fieldNames = { "distShopId", "svcId" }))
public class DistShopSvc implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(type = Types.BIGINT)
	private Long id;

	@Column(nullable = false, type = Types.INTEGER, desc = "卫星店id")
	@ForeignKey(refEntityClass = DistShop.class, refFieldName = "id")
	private Integer distShopId;

	@Column(nullable = false, type = Types.INTEGER, desc = "服务ID")
	@ForeignKey(refEntityClass = Svcx.class, refFieldName = "id")
	private Integer svcId;
	
	/**（审核通过时必须设置）**/
	@Column(type = Types.DECIMAL, precision = 18, scale = 4, desc = "派单利润")
	private BigDecimal distProfit;
	
	/** （审核通过时必须设置）*/
	@Column(type = Types.DECIMAL, precision = 18, scale = 4, desc = "下单利润")
	private BigDecimal distProfitX;

	@Column(nullable = false, type = Types.INTEGER, desc = "序列号")
	private Integer seqNo;

	@Column(nullable = false, type = Types.BOOLEAN, desc = "是否申请", defaultValue = "false")
	private Boolean applyFlag;

	@Column(nullable = false, type = Types.TIMESTAMP, desc = "申请时间", defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date applyTime;

	@Column(nullable = false, type = Types.INTEGER, desc = "审核状态：0 未审核，1审核通过，-1审核未通过", defaultValue = "0")
	private Integer auditStatus;

	@Column(nullable = false, type = Types.TIMESTAMP, desc = "审核时间", defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date auditTime;

	@Column(type = Types.INTEGER, desc = "审核者ID")
	private Integer auditorId;

	@Column(type = Types.VARCHAR, length = 30, desc = "审核者名称")
	private String auditorName;

	@Column(type = Types.VARCHAR, length = 60, desc = "审核描述")
	private String auditDesc;

	@Column(nullable = false, type = Types.TIMESTAMP, desc = "更新时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonNullDeserializer.class)
	private Date ts;

	private BigDecimal salePrice;

	private String fileBrowseUrl;

	private String fileBrowseUrlApp;

	private String fileBrowseUrlIcon;

	private String fileBrowseUrlIcon2;

	private String svcName;
	
	private String desc;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getDistShopId() {
		return distShopId;
	}

	public void setDistShopId(Integer distShopId) {
		this.distShopId = distShopId;
	}

	public Integer getSvcId() {
		return svcId;
	}

	public void setSvcId(Integer svcId) {
		this.svcId = svcId;
	}

	public Integer getSeqNo() {
		return seqNo;
	}

	public void setSeqNo(Integer seqNo) {
		this.seqNo = seqNo;
	}

	public Boolean getApplyFlag() {
		return applyFlag;
	}

	public void setApplyFlag(Boolean applyFlag) {
		this.applyFlag = applyFlag;
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

	public String getAuditDesc() {
		return auditDesc;
	}

	public void setAuditDesc(String auditDesc) {
		this.auditDesc = auditDesc;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}


	public String getFileBrowseUrl() {
		return fileBrowseUrl;
	}

	public void setFileBrowseUrl(String fileBrowseUrl) {
		this.fileBrowseUrl = fileBrowseUrl;
	}

	public String getFileBrowseUrlApp() {
		return fileBrowseUrlApp;
	}

	public void setFileBrowseUrlApp(String fileBrowseUrlApp) {
		this.fileBrowseUrlApp = fileBrowseUrlApp;
	}

	public String getFileBrowseUrlIcon() {
		return fileBrowseUrlIcon;
	}

	public void setFileBrowseUrlIcon(String fileBrowseUrlIcon) {
		this.fileBrowseUrlIcon = fileBrowseUrlIcon;
	}

	public String getFileBrowseUrlIcon2() {
		return fileBrowseUrlIcon2;
	}

	public void setFileBrowseUrlIcon2(String fileBrowseUrlIcon2) {
		this.fileBrowseUrlIcon2 = fileBrowseUrlIcon2;
	}

	public String getSvcName() {
		return svcName;
	}

	public void setSvcName(String svcName) {
		this.svcName = svcName;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public BigDecimal getDistProfit() {
		return distProfit;
	}

	public void setDistProfit(BigDecimal distProfit) {
		this.distProfit = distProfit;
	}

	public BigDecimal getDistProfitX() {
		return distProfitX;
	}

	public void setDistProfitX(BigDecimal distProfitX) {
		this.distProfitX = distProfitX;
	}

	@Override
	public String toString() {
		return "DistShopSvc [id=" + id + ", distShopId=" + distShopId + ", svcId=" + svcId + ", distProfit=" + distProfit + ", distProfitX=" + distProfitX + ", seqNo=" + seqNo + ", applyFlag=" + applyFlag + ", applyTime=" + applyTime
				+ ", auditStatus=" + auditStatus + ", auditTime=" + auditTime + ", auditorId=" + auditorId + ", auditorName=" + auditorName + ", auditDesc=" + auditDesc + ", ts=" + ts + ", salePrice=" + salePrice + ", fileBrowseUrl="
				+ fileBrowseUrl + ", fileBrowseUrlApp=" + fileBrowseUrlApp + ", fileBrowseUrlIcon=" + fileBrowseUrlIcon + ", fileBrowseUrlIcon2=" + fileBrowseUrlIcon2 + ", svcName=" + svcName + ", desc=" + desc + "]";
	}
}