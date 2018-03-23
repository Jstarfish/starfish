package priv.starfish.mall.shop.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import priv.starfish.mall.merchant.dto.MerchantDto;
import priv.starfish.mall.shop.entity.Shop;
import priv.starfish.mall.shop.entity.ShopGrade;
import priv.starfish.mall.shop.entity.ShopSvc;
import priv.starfish.mall.svcx.entity.Svcx;

public class ShopDto extends MerchantDto implements Serializable {
	private static final long serialVersionUID = 1L;

	/** 商户id */
	private Integer merchantId;

	/** 店铺Id */
	private Integer id;

	/** 店铺 */
	private Shop shop;

	/** 店铺等级 */
	private ShopGrade shopGrade;

	/** 店铺提供的服务 */
	private List<ShopSvc> shopSvcs;

	/** 店铺提供的服务(分组) */
	private Map<String, List<Svcx>> shopSvcsByGroup;

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public Shop getShop() {
		return shop;
	}

	public void setShop(Shop shop) {
		this.shop = shop;
	}

	public ShopGrade getShopGrade() {
		return shopGrade;
	}

	public void setShopGrade(ShopGrade shopGrade) {
		this.shopGrade = shopGrade;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<ShopSvc> getShopSvcs() {
		return shopSvcs;
	}

	public void setShopSvcs(List<ShopSvc> shopSvcs) {
		this.shopSvcs = shopSvcs;
	}

	public Map<String, List<Svcx>> getShopSvcsByGroup() {
		return shopSvcsByGroup;
	}

	public void setShopSvcsByGroup(Map<String, List<Svcx>> shopSvcsByGroup) {
		this.shopSvcsByGroup = shopSvcsByGroup;
	}

	@Override
	public String toString() {
		return "ShopDto [merchantId=" + merchantId + ", id=" + id + ", shop=" + shop + ", shopGrade=" + shopGrade
				+ ", shopSvcs=" + shopSvcs + ", shopSvcsByGroup=" + shopSvcsByGroup + "]";
	}

}
