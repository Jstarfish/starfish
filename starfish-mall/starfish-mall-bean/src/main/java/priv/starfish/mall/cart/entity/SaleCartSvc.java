package priv.starfish.mall.cart.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import priv.starfish.common.annotation.ForeignKey;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.common.annotation.UniqueConstraint;
import priv.starfish.common.json.JsonShortDateTimeDeserializer;
import priv.starfish.common.json.JsonShortDateTimeSerializer;
import priv.starfish.common.annotation.Column;
import priv.starfish.mall.cart.dto.MiscAmountInfo;
import priv.starfish.mall.market.entity.UserSvcCoupon;

@Table(name = "sale_cart_svc", uniqueConstraints = { @UniqueConstraint(fieldNames = { "cartId", "svcId" }) })
public class SaleCartSvc implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(type = Types.BIGINT)
	private Long id;

	@Column(nullable = false, type = Types.INTEGER, desc = "=用户ID")
	@ForeignKey(refEntityClass = SaleCart.class, refFieldName = "id")
	private Integer cartId;

	@Column(nullable = false, type = Types.INTEGER, desc = "服务id")
	private Integer svcId;

	@Column(nullable = false, type = Types.TIMESTAMP, desc = "添加时间", defaultValue = "CURRENT_TIMESTAMP")
	@JsonSerialize(using = JsonShortDateTimeSerializer.class)
	@JsonDeserialize(using = JsonShortDateTimeDeserializer.class)
	private Date ts;

	@Column(nullable = false, type = Types.BOOLEAN, desc = "是否选中", defaultValue = "true")
	private Boolean checkFlag;

	// 车辆服务名称
	private String carSvcName;
	// 车辆服务价
	private BigDecimal salePrice;

	// 折扣率
	private BigDecimal discRate;

	// 购物车商品(后删除)
	private List<SaleCartGoods> saleCartGoodsList;

	private MiscAmountInfo amountInfo;

	// 核对订单里使用优惠券
	private UserSvcCoupon userCoupon;

	// 服务图片
	private String svcxAlbumImg;

	// app图片
	private String svcxAlbumImgApp;

	// 服务描述
	private String desc;

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getSvcxAlbumImg() {
		return svcxAlbumImg;
	}

	public void setSvcxAlbumImg(String svcxAlbumImg) {
		this.svcxAlbumImg = svcxAlbumImg;
	}

	public UserSvcCoupon getUserCoupon() {
		return userCoupon;
	}

	public void setUserCoupon(UserSvcCoupon userCoupon) {
		this.userCoupon = userCoupon;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getCartId() {
		return cartId;
	}

	public void setCartId(Integer cartId) {
		this.cartId = cartId;
	}

	public Integer getSvcId() {
		return svcId;
	}

	public void setSvcId(Integer svcId) {
		this.svcId = svcId;
	}

	public Date getTs() {
		return ts;
	}

	public void setTs(Date ts) {
		this.ts = ts;
	}

	public List<SaleCartGoods> getSaleCartGoodsList() {
		return saleCartGoodsList;
	}

	public void setSaleCartGoodsList(List<SaleCartGoods> saleCartGoodsList) {
		this.saleCartGoodsList = saleCartGoodsList;
	}

	public String getCarSvcName() {
		return carSvcName;
	}

	public void setCarSvcName(String carSvcName) {
		this.carSvcName = carSvcName;
	}

	public BigDecimal getSalePrice() {
		return salePrice;
	}

	public void setSalePrice(BigDecimal salePrice) {
		this.salePrice = salePrice;
	}

	public MiscAmountInfo getAmountInfo() {
		return amountInfo;
	}

	public void setAmountInfo(MiscAmountInfo amountInfo) {
		this.amountInfo = amountInfo;
	}

	public Boolean getCheckFlag() {
		return checkFlag;
	}

	public void setCheckFlag(Boolean checkFlag) {
		this.checkFlag = checkFlag;
	}

	public String getSvcxAlbumImgApp() {
		return svcxAlbumImgApp;
	}

	public void setSvcxAlbumImgApp(String svcxAlbumImgApp) {
		this.svcxAlbumImgApp = svcxAlbumImgApp;
	}

	public BigDecimal getDiscRate() {
		return discRate;
	}

	public void setDiscRate(BigDecimal discRate) {
		this.discRate = discRate;
	}

	@Override
	public String toString() {
		return "SaleCartSvc [id=" + id + ", cartId=" + cartId + ", svcId=" + svcId + ", ts=" + ts + ", checkFlag=" + checkFlag + ", carSvcName=" + carSvcName + ", salePrice=" + salePrice + ", discRate=" + discRate + ", saleCartGoodsList="
				+ saleCartGoodsList + ", amountInfo=" + amountInfo + ", userCoupon=" + userCoupon + ", svcxAlbumImg=" + svcxAlbumImg + ", svcxAlbumImgApp=" + svcxAlbumImgApp + ", desc=" + desc + "]";
	}
}