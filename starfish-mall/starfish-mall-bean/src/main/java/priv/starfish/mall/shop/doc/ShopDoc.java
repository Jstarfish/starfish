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
import priv.starfish.mall.comn.entity.User;
import priv.starfish.mall.merchant.entity.Merchant;
import priv.starfish.mall.shop.entity.Shop;
import priv.starfish.mall.shop.entity.ShopAlbumImg;
import priv.starfish.mall.shop.entity.ShopAuditRec;
import priv.starfish.mall.shop.entity.ShopBizStatus;
import priv.starfish.mall.shop.entity.ShopGrade;

/**
 * 店铺信息doc对象
 * 
 */
@IdField(name = "id")
public class ShopDoc extends EsDoc {

	public Integer id;

	/** 唯一标识 code = mall.code +"-" + TimeStamp的long值 */
	@JsonInclude(Include.NON_NULL)
	public String code;
	@JsonInclude(Include.NON_NULL)
	public Integer merchantId;

	/** 商户名 */
	@JsonInclude(Include.NON_NULL)
	public String merchantName;

	/** 自营标志（是否自营） */
	@JsonInclude(Include.NON_NULL)
	public Boolean selfFlag;

	/** 店铺名称 */
	@JsonInclude(Include.NON_NULL)
	public String name;

	/** 店铺名称简拼 */
	@JsonInclude(Include.NON_NULL)
	public String py;

	/** 是否企业店铺（而不是默认的个人店铺） */
	@JsonInclude(Include.NON_NULL)
	public Boolean entpFlag;

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

	@JsonInclude(Include.NON_NULL)
	public Integer distCenterId;

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

	/** 电话 */
	@JsonInclude(Include.NON_NULL)
	public String telNo;

	/** 手机 */
	@JsonInclude(Include.NON_NULL)
	public String phoneNo;

	/** 联系人 */
	@JsonInclude(Include.NON_NULL)
	public String linkMan;

	/** 营业范围 */
	@JsonInclude(Include.NON_NULL)
	public String bizScope;

	/** logo UUID */
	@JsonInclude(Include.NON_NULL)
	public String logoUuid;

	/** logo Usage */
	@JsonInclude(Include.NON_NULL)
	public String logoUsage;

	/** 店铺LOGO图片地址 */
	@JsonInclude(Include.NON_NULL)
	public String logoPath;

	/** 申请时间 */
	@JsonInclude(Include.NON_NULL)
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	public Date applyTime;

	/** 申请留言 */
	@JsonInclude(Include.NON_NULL)
	public String applyMsg;

	/** 注册时间 */
	@JsonInclude(Include.NON_NULL)
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	public Date regTime;

	/** （企业）营业执照id（企业店铺必须，个人选填） */
	@JsonInclude(Include.NON_NULL)
	public Integer licenseId;

	/** 审核状态： 0 未审核, 1 初审通过， 2 终审通过 ，-1 审核未通过 */
	@JsonInclude(Include.NON_NULL)
	public Integer auditStatus;

	/** 是否关闭 */
	@JsonInclude(Include.NON_NULL)
	public Boolean closed;

	/** 是否不可用 */
	@JsonInclude(Include.NON_NULL)
	public Boolean disabled;

	/** 备注 */
	@JsonInclude(Include.NON_NULL)
	public String memo;

	/** 分值快照 */
	@JsonInclude(Include.NON_NULL)
	public Integer point;

	/** 商户 */
	@JsonInclude(Include.NON_NULL)
	public Merchant merchant;

	/** 店铺人数 */
	@JsonInclude(Include.NON_NULL)
	public Integer staffCount;

	/** 推荐人 */
	@JsonInclude(Include.NON_NULL)
	public String referrerName;

	/** 推荐人手机 */
	@JsonInclude(Include.NON_NULL)
	public String referrerPhoneNo;

	@JsonInclude(Include.NON_NULL)
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	public Date changeTime;

	@JsonInclude(Include.NON_NULL)
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	public Date indexTime;

	/** 系统用户 */
	@JsonInclude(Include.NON_NULL)
	public User user;

	/** 审核记录 */
	@JsonInclude(Include.NON_NULL)
	public ShopAuditRec shopAuditRec;

	/** 图片浏览路径 */
	@JsonInclude(Include.NON_NULL)
	public String fileBrowseUrl;

	/** 店铺等级 */
	@JsonInclude(Include.NON_NULL)
	public ShopGrade shopGrade;

	/** 店铺相册图片List */
	@JsonInclude(Include.NON_NULL)
	public List<ShopAlbumImg> shopAlbumImgs;

	/** 店铺营业状态 */
	@JsonInclude(Include.NON_NULL)
	public ShopBizStatus shopBizStatus;

	/** 浏览数 */
	@JsonInclude(Include.NON_NULL)
	public Integer browserCount;

	/** 店铺提供的服务 */
	@JsonInclude(Include.NON_NULL)
	public List<Integer> svcxIds = new ArrayList<Integer>();

	/** 店铺提供的商品 */
	@JsonInclude(Include.NON_NULL)
	public List<Integer> productIds = new ArrayList<Integer>();

	public static ShopDoc newOne() {
		return new ShopDoc();
	}

	@Override
	public String toString() {
		return "ShopDoc [id=" + id + ", code=" + code + ", merchantId=" + merchantId + ", merchantName=" + merchantName + ", selfFlag=" + selfFlag + ", name=" + name + ", py=" + py + ", entpFlag=" + entpFlag + ", regionId=" + regionId
				+ ", regionName=" + regionName + ", street=" + street + ", address=" + address + ", distCenterId=" + distCenterId + ", provinceId=" + provinceId + ", cityId=" + cityId + ", countyId=" + countyId + ", townId=" + townId
				+ ", location=" + location + ", lng=" + lng + ", lat=" + lat + ", telNo=" + telNo + ", phoneNo=" + phoneNo + ", linkMan=" + linkMan + ", bizScope=" + bizScope + ", logoUuid=" + logoUuid + ", logoUsage=" + logoUsage
				+ ", logoPath=" + logoPath + ", applyTime=" + applyTime + ", applyMsg=" + applyMsg + ", regTime=" + regTime + ", licenseId=" + licenseId + ", auditStatus=" + auditStatus + ", closed=" + closed + ", disabled=" + disabled
				+ ", memo=" + memo + ", point=" + point + ", merchant=" + merchant + ", staffCount=" + staffCount + ", referrerName=" + referrerName + ", referrerPhoneNo=" + referrerPhoneNo + ", changeTime=" + changeTime + ", indexTime="
				+ indexTime + ", user=" + user + ", shopAuditRec=" + shopAuditRec + ", fileBrowseUrl=" + fileBrowseUrl + ", shopGrade=" + shopGrade + ", shopAlbumImgs=" + shopAlbumImgs + ", shopBizStatus=" + shopBizStatus
				+ ", browserCount=" + browserCount + ", svcxIds=" + svcxIds + ", productIds=" + productIds + "]";
	}

	// 注意：从EsDoc继承的indexTime 和 indexTimeLong 不在这里设置
	public static ShopDoc from(Shop shop) {
		ShopDoc doc = ShopDoc.newOne();
		doc.id = shop.getId();
		doc.code = shop.getCode();
		doc.selfFlag = shop.getSelfFlag();
		doc.entpFlag = shop.getEntpFlag();
		doc.name = shop.getName();
		doc.py = shop.getPy();
		doc.regionId = shop.getRegionId();
		doc.regionName = shop.getRegionName();
		doc.street = shop.getStreet();
		doc.address = shop.getAddress();
		doc.provinceId = shop.getProvinceId();
		doc.cityId = shop.getCityId();
		doc.countyId = shop.getCountyId();
		doc.townId = shop.getTownId();
		Double lat = shop.getLat();
		Double lng = shop.getLng();
		doc.lat = lat;
		doc.lng = lng;
		if (lat != null && lng != null) {
			doc.location = GeoPoint.newOne(lat, lng);
		}
		doc.telNo = shop.getTelNo();
		doc.phoneNo = shop.getPhoneNo();
		doc.linkMan = shop.getLinkMan();
		doc.bizScope = shop.getBizScope();
		doc.logoUuid = shop.getLogoUuid();
		doc.logoUsage = shop.getLogoUsage();
		doc.logoPath = shop.getLogoPath();
		doc.applyMsg = shop.getApplyMsg();
		doc.regTime = shop.getRegTime();
		doc.licenseId = shop.getLicenseId();
		doc.applyTime = shop.getApplyTime();
		doc.changeTime = shop.getChangeTime();
		doc.auditStatus = shop.getAuditStatus();
		doc.closed = shop.getClosed();
		doc.disabled = shop.getDisabled();
		doc.point = shop.getPoint();
		doc.merchant = shop.getMerchant();
		doc.staffCount = shop.getStaffCount();
		doc.referrerName = shop.getReferrerName();
		doc.changeTime = shop.getChangeTime();
		doc.user = shop.getUser();
		doc.shopAuditRec = shop.getShopAuditRec();
		doc.fileBrowseUrl = shop.getFileBrowseUrl();
		doc.shopGrade = shop.getShopGrade();
		doc.shopAlbumImgs = shop.getShopAlbumImgs();
		doc.shopBizStatus = shop.getShopBizStatus();

		return doc;
	}
}
