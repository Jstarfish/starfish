package priv.starfish.mall.demo.doc;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.json.JsonDateTimeDeserializer;
import priv.starfish.common.json.JsonDateTimeSerializer;
import priv.starfish.common.model.GeoPoint;
import priv.starfish.common.util.DateUtil;
import priv.starfish.common.xsearch.EsDoc;
import priv.starfish.mall.demo.entity.Shopx;

/**
 * 注意：对于可能为空的字段 加上注解： @JsonInclude(Include.NON_NULL)
 * 
 * @author koqiui
 * @date 2015年11月5日 下午2:29:42
 *
 */
//@EsDocType(name = "shopx", mappingFilePath = "ShopxDoc.mapping.json")
public class ShopxDoc extends EsDoc {
	public Integer id;

	/** 店铺名称 */
	@JsonInclude(Include.NON_NULL)
	public String name;

	/** 店铺名称简拼 */
	@JsonInclude(Include.NON_NULL)
	public String py;

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

	// Double lng(lon); Double lat;
	@JsonInclude(Include.NON_NULL)
	public GeoPoint location;

	/** 经度 */
	@JsonInclude(Include.NON_NULL)
	public Double lng;

	/** 纬度 */
	@JsonInclude(Include.NON_NULL)
	public Double lat;

	/** 手机 */
	@JsonInclude(Include.NON_NULL)
	public String phoneNo;

	/** 联系人 */
	@JsonInclude(Include.NON_NULL)
	public String linkMan;

	/** 营业范围 */
	@JsonInclude(Include.NON_NULL)
	public String bizScope;

	/** 申请留言 */
	@JsonInclude(Include.NON_NULL)
	public String applyMsg;

	/** 注册时间 */
	@JsonSerialize(using = JsonDateTimeSerializer.class)
	@JsonDeserialize(using = JsonDateTimeDeserializer.class)
	@JsonInclude(Include.NON_NULL)
	public Date regTime;

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

	@JsonSerialize(using = JsonDateTimeSerializer.class)
	@JsonDeserialize(using = JsonDateTimeDeserializer.class)
	@JsonInclude(Include.NON_NULL)
	public Date createTime;

	@JsonSerialize(using = JsonDateTimeSerializer.class)
	@JsonDeserialize(using = JsonDateTimeDeserializer.class)
	@JsonInclude(Include.NON_NULL)
	public Date changeTime;

	/** 浏览数 */
	@JsonInclude(Include.NON_NULL)
	public Integer browserCount;

	@Override
	public String toString() {
		return "ShopxDoc [id=" + id + ", name=" + name + ", py=" + py + ", regionId=" + regionId + ", regionName=" + regionName + ", street=" + street + ", address=" + address + ", provinceId=" + provinceId + ", cityId=" + cityId
				+ ", countyId=" + countyId + ", townId=" + townId + ", location=" + location + ", lng=" + lng + ", lat=" + lat + ", phoneNo=" + phoneNo + ", linkMan=" + linkMan + ", bizScope=" + bizScope + ", applyMsg=" + applyMsg
				+ ", regTime=" + DateUtil.toStdDateTimeStr(regTime) + ", auditStatus=" + auditStatus + ", closed=" + closed + ", disabled=" + disabled + ", memo=" + memo + ", createTime=" + DateUtil.toStdTimestampStr(createTime)
				+ ", changeTime=" + DateUtil.toStdTimestampStr(changeTime) + ", browserCount=" + browserCount + ", indexTime=" + DateUtil.toStdTimestampStr(indexTime) + ", indexTimeLong=" + indexTimeLong + "]";
	}

	public static ShopxDoc newOne() {
		return new ShopxDoc();
	}

	// 注意：从EsDoc继承的indexTime 和 indexTimeLong 不在这里设置
	public static ShopxDoc from(Shopx shopx) {
		ShopxDoc doc = ShopxDoc.newOne();
		doc.id = shopx.getId();
		doc.name = shopx.getName();
		doc.py = shopx.getPy();
		doc.regionId = shopx.getRegionId();
		doc.regionName = shopx.getRegionName();
		doc.street = shopx.getStreet();
		doc.address = shopx.getAddress();
		doc.provinceId = shopx.getProvinceId();
		doc.cityId = shopx.getCityId();
		doc.countyId = shopx.getCountyId();
		doc.townId = shopx.getTownId();
		//
		Double lat = shopx.getLat();
		Double lng = shopx.getLng();
		doc.lat = lat;
		doc.lng = lng;
		if (lat != null && lng != null) {
			doc.location = GeoPoint.newOne(lat, lng);
		}
		//
		doc.phoneNo = shopx.getPhoneNo();
		doc.linkMan = shopx.getLinkMan();
		doc.bizScope = shopx.getBizScope();
		doc.applyMsg = shopx.getApplyMsg();
		doc.regTime = shopx.getRegTime();
		doc.createTime = shopx.getCreateTime();
		doc.changeTime = shopx.getChangeTime();
		doc.auditStatus = shopx.getAuditStatus();
		doc.closed = shopx.getClosed();
		doc.disabled = shopx.getDisabled();
		doc.memo = shopx.getMemo();
		//
		return doc;
	}
}
