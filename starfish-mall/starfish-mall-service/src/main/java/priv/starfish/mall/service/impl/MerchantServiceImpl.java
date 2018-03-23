package priv.starfish.mall.service.impl;

import org.springframework.stereotype.Service;
import priv.starfish.common.exception.BusinessException;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.repo.FileRepository;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.comn.dto.RegionParts;
import priv.starfish.mall.comn.entity.*;
import priv.starfish.mall.dao.comn.*;
import priv.starfish.mall.dao.mall.MallDao;
import priv.starfish.mall.dao.mall.OperatorDao;
import priv.starfish.mall.dao.member.MemberDao;
import priv.starfish.mall.dao.merchant.MerchantDao;
import priv.starfish.mall.dao.merchant.MerchantPicDao;
import priv.starfish.mall.dao.merchant.MerchantSettleAcctDao;
import priv.starfish.mall.dao.settle.SettleWayDao;
import priv.starfish.mall.dao.shop.ShopDao;
import priv.starfish.mall.member.entity.Member;
import priv.starfish.mall.merchant.dto.MerchantDto;
import priv.starfish.mall.merchant.entity.Merchant;
import priv.starfish.mall.merchant.entity.MerchantPic;
import priv.starfish.mall.merchant.entity.MerchantSettleAcct;
import priv.starfish.mall.service.MerchantService;
import priv.starfish.mall.service.SettingService;
import priv.starfish.mall.service.util.CodeUtil;
import priv.starfish.mall.settle.entity.SettleWay;
import priv.starfish.mall.shop.dto.ShopDto;
import priv.starfish.mall.shop.entity.Shop;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service("merchantService")
public class MerchantServiceImpl extends BaseServiceImpl implements MerchantService {
	
	@Resource
	SettingService settingService;
	
	@Resource
	MerchantDao merchantDao;
	
	@Resource
	SettleWayDao settleWayDao;

	@Resource
	MerchantPicDao merchantPicDao;

	@Resource
	UserDao userDao;

	@Resource
	UserRoleDao userRoleDao;

	@Resource
	ShopDao shopDao;

	@Resource
	UserAccountDao userAcctDao;

	@Resource
	BizLicenseDao bizLicenseDao;

	@Resource
	BizLicensePicDao bizLicensePicDao;

	@Resource
	MallDao mallDao;
	
	@Resource
	MemberDao memberDao;

	@Resource
	OperatorDao operatorDao;

	@Resource
	UserVerfStatusDao userVerfStatusDao;

	@Resource
	FileRepository fileRepository;
	
	@Resource
	BankDao bankDao;
	
	@Resource
	MerchantSettleAcctDao merchantSettleAcctDao;

	@Override
	public boolean saveMerchant(MerchantDto merchantDto) {
		// 用戶（管理員信息）
		Integer userId = merchantDto.getUser().getId();
		User user = merchantDto.getUser();
		if (userId == null) {
			userDao.insert(user);
			//添加会员信息
			Member member = new Member();
			member.setId(user.getId());
			member.setDisabled(false);
			memberDao.insert(member);
			//
			userId = user.getId();
		} else {
			userDao.update(user);
		}
		//
		Integer accountId = merchantDto.getAccountId();
		if (accountId == null) {
			// 保存资金账户
			UserAccount userAccount = merchantDto.getUserAccount();
			userAccount.setDisabled(false);
			userAccount.setVerified(false);
			userAccount.setTs(new Date());
			userAccount.setUserId(userId);
			userAcctDao.insert(userAccount);
			//
			accountId = userAccount.getId();
		}
		//
		Integer licenceId = merchantDto.getLicenseId();
		if (licenceId == null) {
			// 保存营业执照
			BizLicense bizLicense = merchantDto.getBizLicense();
			bizLicense.setUserId(userId);
			bizLicense.setTs(new Date());
			bizLicenseDao.insert(bizLicense);
			//
			// 保存营业执照图片
			BizLicensePic bizLicensepic = merchantDto.getBizLicensePic();
			bizLicensepic.setLicenseId(bizLicense.getId());
			bizLicensePicDao.insert(bizLicensepic);
			//
			licenceId = bizLicense.getId();
		}
		//
		int merchantId = userId;
		// 确保商户信息尚不存在
		Merchant dbMerchant = merchantDao.selectById(merchantId);
		if (dbMerchant != null) {
			throw new BusinessException("商戶信息添加失败：指定的商戶信息已存在");
		}
		//
		Merchant merchant = merchantDto.getMerchant();
		merchant.setId(merchantId);
		merchant.setDisabled(false);
		int count = merchantDao.insert(merchant);
		//
		boolean success = count > 0;
		if (success) {
			// 保存商户照片
			List<MerchantPic> merchantPics = merchantDto.getMerchantPics();
			for (MerchantPic pic : merchantPics) {
				pic.setMerchantId(merchant.getId());
				merchantPicDao.insert(pic);
			}
		}

		//
		return success;
	}

	@Override
	public PaginatedList<Merchant> getMerchants(PaginatedFilter paginatedFilter) {
		PaginatedList<Merchant> merchants = merchantDao.selectMerchants(paginatedFilter);
		for (Merchant merchant : merchants.getRows()) {
			Integer merchId = merchant.getId();
			List<Shop> shops = shopDao.selectByMerchId(merchId);
			merchant.setShopCount(shops.size());

			List<MerchantPic> merchantPicList = merchantPicDao.selectByMerchantId(merchId);
			if (merchantPicList.size() > 0) {
				for (MerchantPic merchantPic : merchantPicList) {
					String browseUrl = fileRepository.getFileBrowseUrl(merchantPic.getImageUsage(), merchantPic.getImagePath());
					if (StrUtil.isNullOrBlank(browseUrl)) {
						browseUrl = fileRepository.getFileRepoConfig().getNoImageUrl();
					}
					merchantPic.setFileBrowseUrl(browseUrl);
				}
				merchant.setMerchantPics(merchantPicList);
			}
			List<UserAccount> userAccountList = userAcctDao.selectByUserId(merchId);
			if (userAccountList.size() > 0) {
				merchant.getUser().setUserAccts(userAccountList);
			}
			List<BizLicense> bizLicenseList = bizLicenseDao.selectByUserId(merchId);
			if (bizLicenseList.size() > 0) {
				merchant.getUser().setBizLicenses(bizLicenseList);
			}

		}
		return merchants;
	}

	@Override
	public boolean delteMerchantById(Integer merchId) {
		userAcctDao.deleteByUserId(merchId);
		shopDao.deleteByMerchId(merchId);
		userRoleDao.deleteByUserId(merchId);
		userDao.deleteById(merchId);
		int resultNum = merchantDao.deleteById(merchId);
		return resultNum > 0;
	}

	@Override
	public boolean deleteMerchantByIds(List<String> ids) {
		boolean flag = true;
		for (String id : ids) {
			flag = delteMerchantById(Integer.valueOf(id)) && flag;
		}
		return flag;
	}

	@Override
	public boolean updateMerchant(Merchant merchant) {
		boolean disabled = merchant.getDisabled();
		List<Shop> shops = shopDao.selectByMerchId(merchant.getId());
		for (Shop shop : shops) {
			shopDao.updateClosed(shop.getId(), disabled);
		}
		List<MerchantPic> merchantPics = merchant.getMerchantPics();
		if (merchantPics != null) {
			for (MerchantPic merchantPic : merchantPics) {
				merchantPicDao.update(merchantPic);
			}
		}

		User user = merchant.getUser();
		if (user != null) {
			user.setTs(new Date());
			userDao.update(user);
		}

		int resultNum = merchantDao.update(merchant);
		return resultNum > 0;
	}

	@Override
	public List<Merchant> getAllMerchant(MapContext param) {
		List<Merchant> merchants = merchantDao.selectAllMerchants(param);
		for (Merchant merchant : merchants) {
			merchant.getUser().setUserAccts(userAcctDao.selectByUserId(merchant.getId()));
		}
		return merchants;
	}

	@Override
	public Merchant getMerchantByPhoneNo(String phoneNo) {
		Merchant merchant = merchantDao.selectByPhoneNo(phoneNo);
		if (merchant != null) {
			Integer merchId = merchant.getId();
			List<Shop> list = new ArrayList<>(0);
			List<Shop> shops = shopDao.selectByMerchId(merchId);
			for (Shop shop : shops) {
				Shop newShop = new Shop();
				newShop.setName(shop.getName());
				newShop.setAuditStatus(shop.getAuditStatus());
				list.add(newShop);
			}
			merchant.setShops(list);
		}
		return merchant;
	}

	@Override
	public Merchant getMerchantById(Integer userId) {
		Merchant merchant = merchantDao.selectById(userId);
		if (merchant != null) {
			List<MerchantPic> merchantPics = merchantPicDao.selectByMerchantId(userId);
			for (MerchantPic merchantPic : merchantPics) {
				String browseUrl = fileRepository.getFileBrowseUrl(merchantPic.getImageUsage(), merchantPic.getImagePath());
				if (StrUtil.isNullOrBlank(browseUrl)) {
					browseUrl = fileRepository.getFileRepoConfig().getNoImageUrl();
				}
				merchantPic.setFileBrowseUrl(browseUrl);
			}
			merchant.setMerchantPics(merchantPics);
		}
		return merchant;
	}

	@Override
	public List<Bank> getBanks() {
		return bankDao.selectList();
	}

	@Override
	public boolean settledMerchant(ShopDto shopDto) {
		Integer merchantId = shopDto.getMerchantId();
		int insertMerchCount = 0;
		// 判断商户是否存在
		if (merchantId == null) {
			// 保存商户信息
			Merchant merchant = shopDto.getMerchant();
			insertMerchCount = merchantDao.insert(merchant);
			merchantId = merchant.getId();
			if (insertMerchCount > 0) {
				// 保存商户照片
				List<MerchantPic> merchantPics = shopDto.getMerchantPics();
				for (MerchantPic pic : merchantPics) {
					pic.setMerchantId(merchantId);
					merchantPicDao.insert(pic);
				}
			}
		}
		// 保存店铺信息
		Shop shop = shopDto.getShop();
		String code = CodeUtil.newShopCode();
		// code = code + "-dp-" + CodeUtil.generateRandomCode(8);
		// code = CodeUtil.generateCode(code, uId);
		shop.setCode(code);
		shop.setMerchantId(merchantId);
		shop.setMerchantName(shopDto.getMerchant().getName());
		shop.setLicenseId(shopDto.getLicenseId());
		shop.setSelfFlag(false);
		String name = shop.getName();
		shop.setPy(StrUtil.chsToPy(name));
		shop.setEntpFlag(false);
		shop.setRegTime(new Date());
		shop.setApplyTime(new Date());
		shop.setAuditStatus(0);
		shop.setClosed(false);
		shop.setDisabled(false);
		//通过regionId获取相关信息
		RegionParts regionParts = settingService.getRegionPartsById(shop.getRegionId());
		shop.setRegionName(regionParts.fullName);
		shop.setProvinceId(regionParts.provinceId);
		shop.setCityId(regionParts.cityId);
		shop.setCountyId(regionParts.countyId);
		shop.setTownId(regionParts.townId);
		//
		int insertShopCount = shopDao.insert(shop);

		// 插入一个店铺管理员角色
		if (insertShopCount > 0) {
			UserRole userRole = new UserRole();
			userRole.setEntityId(shop.getId());
			userRole.setScope(AuthScope.shop);
			userRole.setUserId(merchantId);
			Integer shopAdminId = 3;
			userRole.setRoleId(shopAdminId);
			userRoleDao.insert(userRole);
		}

		//
		return insertShopCount > 0;

	}

	@Override
	public boolean creatMerchantSettleAcct(MerchantSettleAcct merchantSettleAcct) {
		merchantSettleAcct.setTs(new Date());
		int insertMerchantSettleAcct = merchantSettleAcctDao.insert(merchantSettleAcct);
		return insertMerchantSettleAcct > 0;
	}

	@Override
	public boolean deleteMerchantSettleAcct(MerchantSettleAcct merchantSettleAcct) {
		Integer merchantId = merchantSettleAcct.getMerchantId();
		String settleWayCode = merchantSettleAcct.getSettleWayCode();
		int insertMerchantSettleAcct = merchantSettleAcctDao.deleteByMerchantIdAndSettleWayCode(merchantId, settleWayCode);
		return insertMerchantSettleAcct > 0;
	}

	@Override
	public MerchantSettleAcct selectMerchantSettleAcctByMerchantIdAndAccountId(Integer merchantId, Integer accountId) {
		return merchantSettleAcctDao.selectByMerchantIdAndAccountId(merchantId, accountId);
	}

	@Override
	public MerchantSettleAcct getByMerchantIdAndSettleWayCode(Integer merchantId, String settleWayCode) {
		return merchantSettleAcctDao.selectByMerchantIdAndSettleWayCode(merchantId, settleWayCode);
	}

	@Override
	public SettleWay getSettleWayBySettleWayCode(String settleWayCode) {
		return settleWayDao.selectById(settleWayCode);
	}

	@Override
	public boolean updateMerchantSettleAcct(MerchantSettleAcct merchantSettleAcct) {
		// TODO Auto-generated method stub
		return merchantSettleAcctDao.update(merchantSettleAcct) > 0;
	}

}
