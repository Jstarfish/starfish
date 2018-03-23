package priv.starfish.mall.merchant.dto;

import java.util.List;

import priv.starfish.mall.comn.entity.BizLicense;
import priv.starfish.mall.comn.entity.BizLicensePic;
import priv.starfish.mall.comn.entity.Region;
import priv.starfish.mall.comn.entity.User;
import priv.starfish.mall.comn.entity.UserAccount;
import priv.starfish.mall.merchant.entity.Merchant;
import priv.starfish.mall.merchant.entity.MerchantPic;
import priv.starfish.mall.shop.entity.Shop;

public class MerchantDto {
	/** 系统用户 */
	private User user;

	/** 商户id */
	private Integer merchantId;

	/** 商户 */
	private Merchant merchant;

	/** 商户照片 */
	private List<MerchantPic> merchantPics;

	/** 资金账户id */
	private Integer accountId;

	/** 资金账户 */
	private UserAccount userAccount;

	/** 店铺 */
	private List<Shop> shops;

	/** 地区 */
	private Region region;

	/** 企业营业执照id */
	private Integer licenseId;

	/** 企业营业执照 */
	private BizLicense bizLicense;

	/** 企业营业执照 图片 */
	private BizLicensePic bizLicensePic;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public Integer getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(Integer merchantId) {
		this.merchantId = merchantId;
	}

	public Merchant getMerchant() {
		return merchant;
	}

	public void setMerchant(Merchant merchant) {
		this.merchant = merchant;
	}

	public List<MerchantPic> getMerchantPics() {
		return merchantPics;
	}

	public void setMerchantPics(List<MerchantPic> merchantPics) {
		this.merchantPics = merchantPics;
	}

	public Integer getAccountId() {
		return accountId;
	}

	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public void setUserAccount(UserAccount userAccount) {
		this.userAccount = userAccount;
	}

	public List<Shop> getShops() {
		return shops;
	}

	public void setShops(List<Shop> shops) {
		this.shops = shops;
	}

	public Region getRegion() {
		return region;
	}

	public void setRegion(Region region) {
		this.region = region;
	}

	public Integer getLicenseId() {
		return licenseId;
	}

	public void setLicenseId(Integer licenseId) {
		this.licenseId = licenseId;
	}

	public BizLicense getBizLicense() {
		return bizLicense;
	}

	public void setBizLicense(BizLicense bizLicense) {
		this.bizLicense = bizLicense;
	}

	public BizLicensePic getBizLicensePic() {
		return bizLicensePic;
	}

	public void setBizLicensePic(BizLicensePic bizLicensePic) {
		this.bizLicensePic = bizLicensePic;
	}

	@Override
	public String toString() {
		return "MerchantDto [user=" + user + ", merchantId=" + merchantId + ", merchant=" + merchant + ", merchantPics=" + merchantPics + ", accountId=" + accountId + ", userAccount=" + userAccount + ", shops=" + shops + ", region="
				+ region + ", licenseId=" + licenseId + ", bizLicense=" + bizLicense + ", bizLicensePic=" + bizLicensePic + "]";
	}

}
