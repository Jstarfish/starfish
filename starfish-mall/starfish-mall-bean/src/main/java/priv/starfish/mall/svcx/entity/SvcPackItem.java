package priv.starfish.mall.svcx.entity;

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
import priv.starfish.common.json.JsonShortDateTimeDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.mall.cart.dto.MiscAmountInfo;

@Table(name = "svc_pack_item")
public class SvcPackItem implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(nullable = false, type = Types.INTEGER, desc = "所属套餐主键")
	@ForeignKey(refEntityClass = SvcPack.class, refFieldName = "id")
	private Integer packId;

	@Column(type = Types.INTEGER, desc = "平台定义的为-1")
	private Integer shopId;

	@Column(nullable = false, type = Types.INTEGER, desc = "所属服务主键")
	@ForeignKey(refEntityClass = Svcx.class, refFieldName = "id")
	private Integer svcId;

	@Column(type = Types.VARCHAR, length = 30, desc = "名称")
	private String svcName;

	@Column(type = Types.INTEGER, desc = "所属种类主键")
	private Integer svcKindId;

	@Column(nullable = false, type = Types.NUMERIC, precision = 8, scale = 2, desc = "")
	private Double rate;

	@Column(nullable = false, type = Types.TIMESTAMP, desc = "创建时间")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date ts;

	private BigDecimal svcSalePrice;

	private String fileBrowseUrl;
	
	private String fileBrowseUrlApp;
	
	private String fileBrowseUrlIcon;
	
	private String fileBrowseUrlIcon2;

	private String svcDesc;

	private MiscAmountInfo amountInfo;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getPackId() {
		return packId;
	}

	public void setPackId(Integer packId) {
		this.packId = packId;
	}

	public Integer getShopId() {
		return shopId;
	}

	public void setShopId(Integer shopId) {
		this.shopId = shopId;
	}

	public Integer getSvcId() {
		return svcId;
	}

	public void setSvcId(Integer svcId) {
		this.svcId = svcId;
	}

	public String getSvcName() {
		return svcName;
	}

	public void setSvcName(String svcName) {
		this.svcName = svcName;
	}

	public Integer getSvcKindId() {
		return svcKindId;
	}

	public void setSvcKindId(Integer svcKindId) {
		this.svcKindId = svcKindId;
	}

	public Double getRate() {
		return rate;
	}

	public void setRate(Double rate) {
		this.rate = rate;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public BigDecimal getSvcSalePrice() {
		return svcSalePrice;
	}

	public void setSvcSalePrice(BigDecimal svcSalePrice) {
		this.svcSalePrice = svcSalePrice;
	}

	public String getFileBrowseUrl() {
		return fileBrowseUrl;
	}

	public void setFileBrowseUrl(String fileBrowseUrl) {
		this.fileBrowseUrl = fileBrowseUrl;
	}

	public String getSvcDesc() {
		return svcDesc;
	}

	public void setSvcDesc(String svcDesc) {
		this.svcDesc = svcDesc;
	}

	public MiscAmountInfo getAmountInfo() {
		return amountInfo;
	}

	public void setAmountInfo(MiscAmountInfo amountInfo) {
		this.amountInfo = amountInfo;
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

	@Override
	public String toString() {
		return "SvcPackItem [id=" + id + ", packId=" + packId + ", shopId=" + shopId + ", svcId=" + svcId + ", svcName=" + svcName + ", svcKindId=" + svcKindId + ", rate=" + rate + ", ts=" + ts + ", svcSalePrice=" + svcSalePrice
				+ ", fileBrowseUrl=" + fileBrowseUrl + ", fileBrowseUrlApp=" + fileBrowseUrlApp + ", fileBrowseUrlIcon=" + fileBrowseUrlIcon + ", fileBrowseUrlIcon2=" + fileBrowseUrlIcon2 + ", svcDesc=" + svcDesc + ", amountInfo="
				+ amountInfo + "]";
	}
}
