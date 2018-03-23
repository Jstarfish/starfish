package priv.starfish.mall.merchant.entity;

import java.io.Serializable;
import java.sql.Types;
import java.util.List;

import priv.starfish.common.annotation.Column;
import priv.starfish.common.annotation.Id;
import priv.starfish.common.annotation.Table;
import priv.starfish.mall.comn.entity.User;
import priv.starfish.mall.shop.entity.Shop;

@Table(name = "merchant")
public class Merchant implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id(auto = false, type = Types.INTEGER, desc = "主键==用户Id")
	private Integer id;
	
	@Column(type = Types.VARCHAR, length = 30, desc = "商户名")
	private String name;
	
	@Column(type = Types.VARCHAR, length = 30, desc = "真实姓名")
	private String realName;
	
	@Column(type = Types.VARCHAR, length = 30, desc = "身份证号")
	private String idCertNo;
	
	@Column(nullable = false, type = Types.BOOLEAN, defaultValue = "false", desc = "是否禁用")
	private Boolean disabled;

	@Column(type = Types.VARCHAR, length = 250, desc = "备注")
	private String memo;
	
	/** 系统用户 */
	private User user;
	
	/** 商户照片 */
	private List<MerchantPic> merchantPics;

	/** 商户店铺 */
	private List<Shop> shops;

	/** 开店数量 */
	private Integer shopCount;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getRealName() {
		return realName;
	}

	public void setRealName(String realName) {
		this.realName = realName;
	}

	public String getIdCertNo() {
		return idCertNo;
	}

	public void setIdCertNo(String idCertNo) {
		this.idCertNo = idCertNo;
	}

	public Boolean getDisabled() {
		return disabled;
	}

	public void setDisabled(Boolean disabled) {
		this.disabled = disabled;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public List<MerchantPic> getMerchantPics() {
		return merchantPics;
	}

	public void setMerchantPics(List<MerchantPic> merchantPics) {
		this.merchantPics = merchantPics;
	}

	public List<Shop> getShops() {
		return shops;
	}

	public void setShops(List<Shop> shops) {
		this.shops = shops;
	}

	public Integer getShopCount() {
		return shopCount;
	}

	public void setShopCount(Integer shopCount) {
		this.shopCount = shopCount;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Merchant [id=" + id + ", name=" + name + ", realName=" + realName + ", idCertNo=" + idCertNo + ", disabled=" + disabled + ", memo=" + memo + ", user=" + user + ", merchantPics=" + merchantPics + ", shops=" + shops
				+ ", shopCount=" + shopCount + "]";
	}

}
