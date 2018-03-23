package priv.starfish.mall.shop.entity;

import java.io.Serializable;
import java.sql.Types;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;

/**
 * 店铺参数表
 * 
 * @author 毛智东
 * @date 2015年6月5日 下午4:00:13
 *
 */
@Table(name = "shop_param", uniqueConstraints = { @UniqueConstraint(fieldNames = { "shopId", "code" }) }, desc = "店铺参数表")
public class ShopParam implements Serializable {

	private static final long serialVersionUID = 3604398355356400235L;

	@Id(type = Types.INTEGER)
	private Integer id;

	@Column(type = Types.INTEGER, nullable = false)
	@ForeignKey(refEntityClass = Shop.class, refFieldName = "id")
	private Integer shopId;

	@Column(type = Types.VARCHAR, length = 30, nullable = false)
	private String code;

	@Column(type = Types.VARCHAR, length = 30)
	private String name;

	@Column(type = Types.VARCHAR, length = 1000)
	private String value;

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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "ShopParam [id=" + id + ", shopId=" + shopId + ", code=" + code + ", name=" + name + ", value=" + value + "]";
	}

	// 元数据，便于前台展示
	public static class MetaInfo {
		public String code;
		public String name;

		public MetaInfo(String code, String name) {
			this.code = code;
			this.name = name;
		}

		@Override
		public String toString() {
			return "MetaInfo [code=" + code + ", name=" + name + "]";
		}
	}

	public static final MetaInfo goods = new MetaInfo("goods", "商品相关设置");
	public static final MetaInfo invoice = new MetaInfo("invoice", "发票设置");
	public static final MetaInfo sms = new MetaInfo("sms", "短信发送设置");
	public static final MetaInfo deliveryWay = new MetaInfo("delivery.way", "配送方式设置");
	public static final MetaInfo freightFee = new MetaInfo("freight.fee", "运费设置");
	//
	public static final MetaInfo[] metaInfos = new MetaInfo[] { goods, invoice, sms, deliveryWay, freightFee };
}
