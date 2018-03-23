package priv.starfish.mall.shop.doc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.IdField;
import priv.starfish.common.json.JsonShortDateTimeDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.common.model.GeoPoint;
import priv.starfish.common.xsearch.EsDoc;
import priv.starfish.mall.shop.entity.DistShop;
import priv.starfish.mall.shop.entity.ShopAuditRec;

/**
 * 店铺信息doc对象
 * 
 */
@IdField(name = "id")
public class DistShopDoc extends EsDoc {

	public Integer id;

	/** 店铺名称 */
	@JsonInclude(Include.NON_NULL)
	public String name;

	/** 地区Id */
	@JsonInclude(Include.NON_NULL)
	public Integer regionId;

	/** 地区名称 */
	@JsonInclude(Include.NON_NULL)
	public String regionName;

	/** 街道 */
	@JsonInclude(Include.NON_NULL)
	public String street;

	@JsonInclude(Include.NON_NULL)
	public String address;

	/** 省级地区Id */
	@JsonInclude(Include.NON_NULL)
	public Integer provinceId;

	/** 市级地区Id */
	@JsonInclude(Include.NON_NULL)
	public Integer cityId;

	/** 县级地区Id */
	@JsonInclude(Include.NON_NULL)
	public Integer countyId;

	/** 乡级地区Id */
	@JsonInclude(Include.NON_NULL)
	public Integer townId;

	@JsonInclude(Include.NON_NULL)
	public GeoPoint location;
	/** 经度 */
	@JsonInclude(Include.NON_NULL)
	public Double lng;
	/** 纬度 */
	@JsonInclude(Include.NON_NULL)
	public Double lat;

	/** 隶属加盟店id */
	@JsonInclude(Include.NON_NULL)
	public Integer ownerShopId;

	/** 电话 */
	@JsonInclude(Include.NON_NULL)
	public String telNo;

	/** 手机 */
	@JsonInclude(Include.NON_NULL)
	public String phoneNo;

	/** 联系人 */
	@JsonInclude(Include.NON_NULL)
	public String linkMan;

	/** logo UUID */
	@JsonInclude(Include.NON_NULL)
	public String logoUuid;

	/** logo Usage */
	@JsonInclude(Include.NON_NULL)
	public String logoUsage;

	/** 店铺LOGO图片地址 */
	@JsonInclude(Include.NON_NULL)
	public String logoPath;

	/** 注册时间 */
	@JsonInclude(Include.NON_NULL)
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	public Date regTime;

	/** 审核状态： 0 未审核, 1 初审通过， 2 终审通过 ，-1 审核未通过 */
	@JsonInclude(Include.NON_NULL)
	public Integer auditStatus;

	/** 是否不可用 */
	@JsonInclude(Include.NON_NULL)
	public Boolean disabled;

	@JsonInclude(Include.NON_NULL)
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	public Date changeTime;

	@JsonInclude(Include.NON_NULL)
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	public Date indexTime;

	/** 审核记录 */
	@JsonInclude(Include.NON_NULL)
	public ShopAuditRec shopAuditRec;

	/** 图片浏览路径 */
	@JsonInclude(Include.NON_NULL)
	public String fileBrowseUrl;

	/** 浏览数 */
	@JsonInclude(Include.NON_NULL)
	public Integer browserCount;

	/** 店铺提供的服务 */
	@JsonInclude(Include.NON_NULL)
	public List<Integer> svcxIds = new ArrayList<Integer>();

	@Override
	public String toString() {
		return "DistShopDoc [id=" + id + ", name=" + name + ", regionId=" + regionId + ", regionName=" + regionName
				+ ", street=" + street + ", address=" + address + ", provinceId=" + provinceId + ", cityId=" + cityId
				+ ", countyId=" + countyId + ", townId=" + townId + ", location=" + location + ", lng=" + lng + ", lat="
				+ lat + ", ownerShopId=" + ownerShopId + ", telNo=" + telNo + ", phoneNo=" + phoneNo + ", linkMan="
				+ linkMan + ", logoUuid=" + logoUuid + ", logoUsage=" + logoUsage + ", logoPath=" + logoPath
				+ ", regTime=" + regTime + ", auditStatus=" + auditStatus + ", disabled=" + disabled + ", changeTime="
				+ changeTime + ", indexTime=" + indexTime + ", shopAuditRec=" + shopAuditRec + ", fileBrowseUrl="
				+ fileBrowseUrl + ", browserCount=" + browserCount + ", svcxIds=" + svcxIds + "]";
	}

	public static DistShopDoc newOne() {
		return new DistShopDoc();
	}

	// 注意：从EsDoc继承的indexTime 和 indexTimeLong 不在这里设置
	public static DistShopDoc from(DistShop distShop) {
		DistShopDoc doc = DistShopDoc.newOne();
		doc.id = distShop.getId();
		doc.name = distShop.getName();
		doc.regionId = distShop.getRegionId();
		doc.regionName = distShop.getRegionName();
		doc.street = distShop.getStreet();
		doc.address = distShop.getAddress();
		doc.provinceId = distShop.getProvinceId();
		doc.cityId = distShop.getCityId();
		doc.countyId = distShop.getCountyId();
		doc.townId = distShop.getTownId();
		Double lat = distShop.getLat();
		Double lng = distShop.getLng();
		doc.lat = lat;
		doc.lng = lng;
		if (lat != null && lng != null) {
			doc.location = GeoPoint.newOne(lat, lng);
		}
		doc.ownerShopId = distShop.getOwnerShopId();
		doc.telNo = distShop.getTelNo();
		doc.phoneNo = distShop.getPhoneNo();
		doc.linkMan = distShop.getLinkMan();
		doc.logoUuid = distShop.getLogoUuid();
		doc.logoUsage = distShop.getLogoUsage();
		doc.logoPath = distShop.getLogoPath();
		doc.regTime = distShop.getRegTime();
		doc.auditStatus = distShop.getAuditStatus();
		doc.disabled = distShop.getDisabled();
		doc.fileBrowseUrl = distShop.getFileBrowseUrl();

		return doc;
	}
}
