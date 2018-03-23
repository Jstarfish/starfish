package priv.starfish.mall.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import priv.starfish.common.exception.ValidationException;
import priv.starfish.common.jms.SimpleMessage;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.DateUtil;
import priv.starfish.common.util.JsonUtil;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.common.util.TypeUtil;
import priv.starfish.mall.dao.comn.BizLicenseDao;
import priv.starfish.mall.dao.comn.BizLicensePicDao;
import priv.starfish.mall.dao.comn.UserAccountDao;
import priv.starfish.mall.dao.comn.UserDao;
import priv.starfish.mall.dao.comn.UserRoleDao;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.comn.dto.RegionParts;
import priv.starfish.mall.comn.entity.BizLicense;
import priv.starfish.mall.comn.entity.BizLicensePic;
import priv.starfish.mall.comn.entity.User;
import priv.starfish.mall.comn.entity.UserRole;
import priv.starfish.mall.dao.goods.ProductDao;
import priv.starfish.mall.goods.entity.Product;
import priv.starfish.mall.dao.mall.MallDao;
import priv.starfish.mall.dao.merchant.MerchantDao;
import priv.starfish.mall.merchant.entity.Merchant;
import priv.starfish.mall.service.BaseConst;
import priv.starfish.mall.service.CarSvcService;
import priv.starfish.mall.service.SettingService;
import priv.starfish.mall.service.ShopService;
import priv.starfish.mall.service.UserService;
import priv.starfish.mall.service.util.CodeUtil;
import priv.starfish.mall.dao.shop.ShopAlbumImgDao;
import priv.starfish.mall.dao.shop.ShopAuditRecDao;
import priv.starfish.mall.dao.shop.ShopBizStatusDao;
import priv.starfish.mall.dao.shop.ShopDao;
import priv.starfish.mall.dao.shop.ShopGoodsCatDao;
import priv.starfish.mall.dao.shop.ShopGradeDao;
import priv.starfish.mall.dao.shop.ShopMemoDao;
import priv.starfish.mall.dao.shop.ShopNoticeDao;
import priv.starfish.mall.dao.shop.ShopParamDao;
import priv.starfish.mall.dao.shop.ShopProductDao;
import priv.starfish.mall.dao.shop.ShopSvcDao;
import priv.starfish.mall.shop.dto.ShopDto;
import priv.starfish.mall.shop.dto.ShopParamDto;
import priv.starfish.mall.shop.entity.Shop;
import priv.starfish.mall.shop.entity.ShopAlbumImg;
import priv.starfish.mall.shop.entity.ShopAuditRec;
import priv.starfish.mall.shop.entity.ShopBizStatus;
import priv.starfish.mall.shop.entity.ShopGrade;
import priv.starfish.mall.shop.entity.ShopMemo;
import priv.starfish.mall.shop.entity.ShopNotice;
import priv.starfish.mall.shop.entity.ShopParam;
import priv.starfish.mall.shop.entity.ShopProduct;
import priv.starfish.mall.shop.entity.ShopSvc;
import priv.starfish.mall.dao.svcx.SvcxDao;
import priv.starfish.mall.svcx.entity.Svcx;

@Service("shopService")
public class ShopServiceImpl extends BaseServiceImpl implements ShopService {

	@Resource
	ShopDao shopDao;

	@Resource
	ShopGradeDao shopGradeDao;

	@Resource
	ShopGoodsCatDao shopGoodsCatDao;

	@Resource
	MerchantDao merchantDao;

	@Resource
	UserAccountDao userAcctDao;

	@Resource
	UserDao userDao;

	@Resource
	MallDao mallDao;

	@Resource
	ShopParamDao shopParamDao;

	@Resource
	ShopNoticeDao shopNoticeDao;

	@Resource
	UserRoleDao userRoleDao;

	@Resource
	ShopAuditRecDao shopAuditRecDao;

	@Resource
	BizLicenseDao bizLicenseDao;

	@Resource
	BizLicensePicDao bizLicensePicDao;

	@Resource
	ShopProductDao shopProductDao;

	@Resource
	ShopSvcDao shopSvcDao;

	@Resource
	ProductDao productDao;

	@Resource
	ShopAlbumImgDao shopAlbumImgDao;

	@Resource
	ShopBizStatusDao shopBizStatusDao;

	@Resource
	SettingService settingService;

	@Resource
	UserService userService;

	@Resource
	SvcxDao svcxDao;

	@Resource
	ShopMemoDao shopMemoDao;

	@Resource
	CarSvcService carSvcService;

	private void sendShopChangeQueueMessage(Integer shopId) {
		System.out.println("*************************开始发送消息(shop)****************************");
		SimpleMessage messageToSend = SimpleMessage.newOne();
		messageToSend.subject = BaseConst.TaskFocus.SHOP;
		messageToSend.key = shopId;
		simpleMessageSender.sendQueueMessage(BaseConst.QueueNames.TASK, messageToSend);
		System.out.println("*************************发送消息完毕(shop)****************************");
	}

	// --------------------------------------- shopGrade
	// ----------------------------------------------
	@Override
	public ShopGrade getShopGradeById(Integer id) {
		return shopGradeDao.selectById(id);
	}

	@Override
	public ShopGrade getShopGradeByGradeAndLevel(Integer grade, Integer level) {
		return shopGradeDao.selectByGradeAndLevel(grade, level);
	}

	@Override
	public boolean saveShopGrade(ShopGrade shopGrade) {
		boolean success = shopGradeDao.insert(shopGrade) > 0;
		return success;
	}

	@Override
	public boolean updateShopGrades(List<ShopGrade> shopGrades) {
		boolean falg = true;
		for (ShopGrade shopGrade : shopGrades) {
			falg = shopGradeDao.update(shopGrade) > 0 && falg;
		}
		return falg;
	}

	@Override
	public boolean deleteShopGradeById(Integer id) {
		return shopGradeDao.deleteById(id) > 0;
	}

	@Override
	public PaginatedList<ShopGrade> getShopGrades() {
		PaginatedList<ShopGrade> paginatedList = new PaginatedList<ShopGrade>();
		List<ShopGrade> listShopGrade = shopGradeDao.selectShopGrades();
		for (ShopGrade shopGrade : listShopGrade) {
			this.filterFileBrowseUrl(shopGrade);
		}
		paginatedList.setRows(listShopGrade);
		return paginatedList;
	}

	@Override
	public List<ShopGrade> getShopGradesByLowerAndUpperPoint(Integer lowerPoint, Integer upperPoint) {
		return shopGradeDao.selectShopGradesByLowerPointAndUpperPoint(lowerPoint, upperPoint);
	}

	@Override
	public List<ShopGrade> getGradesByPoint(Integer point) {
		List<ShopGrade> listShopGrade = shopGradeDao.selectShopGradesByPoint(point);
		for (ShopGrade shopGrade : listShopGrade) {
			this.filterFileBrowseUrl(shopGrade);
		}
		return listShopGrade;
	}

	// --------------------------------------- shop
	// ----------------------------------------------
	@Override
	public Shop getShopByName(String name) {
		Shop shop = shopDao.selectByName(name);
		if (shop == null) {
			return null;
		}
		this.filterFileBrowseUrl(shop);
		// 获取店铺相册图片
		List<ShopAlbumImg> shopAlbumImgs = shopAlbumImgDao.selectByShopId(shop.getId());
		for (ShopAlbumImg shopAlbumImg : shopAlbumImgs) {
			this.filterFileBrowseUrl(shopAlbumImg);
		}
		shop.setShopAlbumImgs(shopAlbumImgs);

		return shop;
	}

	@Override
	public PaginatedList<ShopDto> getShops(PaginatedFilter paginatedFilter) {
		PaginatedList<Shop> pagListShop = shopDao.selectShops(paginatedFilter);
		PaginatedList<ShopDto> pagListShopDto = new PaginatedList<>();
		pagListShopDto.setPagination(pagListShop.getPagination());
		List<Shop> listShop = pagListShop.getRows();
		List<ShopDto> listShopDto = pagListShopDto.getRows();
		for (Shop shop : listShop) {
			this.filterFileBrowseUrl(shop);
			//
			ShopDto shopDto = new ShopDto();
			// 获取店铺相册图片
			List<ShopAlbumImg> shopAlbumImgs = shopAlbumImgDao.selectByShopId(shop.getId());
			for (ShopAlbumImg shopAlbumImg : shopAlbumImgs) {
				this.filterFileBrowseUrl(shopAlbumImg);
			}
			shop.setShopAlbumImgs(shopAlbumImgs);

			Integer merchantId = shop.getMerchantId();
			if (merchantId != null) {
				Merchant merchant = merchantDao.selectById(merchantId);
				shopDto.setMerchant(merchant);
			}

			Integer bizLicenseId = shop.getLicenseId();
			if (bizLicenseId != null) {
				BizLicense bizLicense = bizLicenseDao.selectById(bizLicenseId);
				shopDto.setBizLicense(bizLicense);
			}
			shopDto.setId(shop.getId());
			shopDto.setShop(shop);
			// List<Svcx> shopSvcs = svcxDao.selectByShopId(shop.getId());
			List<ShopSvc> shopSvcs = shopSvcDao.selectByShopId(shop.getId());
			for (ShopSvc shopSvc : shopSvcs) {
				Svcx svcx = carSvcService.getCarSvc(shopSvc.getSvcId());
				shopSvc.setSvc(svcx);
			}
			shopDto.setShopSvcs(shopSvcs);

			listShopDto.add(shopDto);
		}
		pagListShopDto.setRows(listShopDto);

		return pagListShopDto;
	}

	@Override
	public boolean updateShopCloseState(Integer id, boolean closed) {
		boolean success = shopDao.updateClosed(id, closed) > 0;
		if (success) {
			this.sendShopChangeQueueMessage(id);
		}
		return success;
	}

	@Override
	public boolean updateShopDisableState(Integer id, boolean disabled) {
		boolean success = shopDao.updateDisabled(id, disabled) > 0;
		if (success) {
			this.sendShopChangeQueueMessage(id);
		}
		return success;
	}

	@Override
	public boolean updateShopAudit(ShopAuditRec shopAuditRec, Integer auditorId, String auditorName, Date auditTime,
			Integer auditStatus) {
		boolean flag = true;
		shopAuditRec.setAuditorId(auditorId);
		shopAuditRec.setAuditorName(auditorName);
		shopAuditRec.setAuditTime(auditTime);
		flag = shopAuditRecDao.insert(shopAuditRec) > 0;
		flag = shopDao.updateAudit(shopAuditRec.getShopId(), auditorId, auditorName, auditTime, auditStatus) > 0
				&& flag;
		if (flag) {
			this.sendShopChangeQueueMessage(shopAuditRec.getShopId());
		}
		return flag;
	}

	@Override
	public boolean batchUpdateShopsAudit(List<String> ids, Integer auditStatus, ShopAuditRec shopAuditRec) {
		boolean flag = true;
		for (String id : ids) {
			shopAuditRec.setShopId(Integer.valueOf(id));
			flag = shopAuditRecDao.insert(shopAuditRec) > 0 && flag;
			if (flag) {
				flag = shopDao.updateAudit(shopAuditRec.getShopId(), shopAuditRec.getAuditorId(),
						shopAuditRec.getAuditorName(), shopAuditRec.getAuditTime(), auditStatus) > 0 && flag;
				if (auditStatus.equals(1)) {
					if (flag) {
						Shop shop = shopDao.selectById(shopAuditRec.getShopId());
						UserRole userRole = new UserRole();
						userRole.setEntityId(shopAuditRec.getShopId());
						userRole.setScope(AuthScope.shop);
						userRole.setUserId(shop.getUser().getId());
						Integer shopAdminId = 3;
						userRole.setRoleId(shopAdminId);
						flag = userRoleDao.insert(userRole) > 0 && flag;
					}
				}
			}
			if (flag) {
				this.sendShopChangeQueueMessage(shopAuditRec.getShopId());
			}
		}
		return flag;
	}

	@Override
	public boolean deleteShopById(Integer id) {
		boolean success = shopDao.deleteById(id) > 0;
		if (success) {
			this.sendShopChangeQueueMessage(id);
		}
		return success;
	}

	@Override
	public boolean batchDeleteShops(List<String> ids) {
		boolean flag = true;
		for (String id : ids) {
			flag = this.deleteShopById(Integer.valueOf(id)) && flag;
			if (flag) {
				this.sendShopChangeQueueMessage(Integer.valueOf(id));
			}
		}
		return flag;
	}

	@Override
	public ShopDto getShopInfoById(Integer id) {
		Shop shop = shopDao.selectById(id);
		ShopDto shopDto = new ShopDto();
		// 添加银行卡信息
		if (shop != null) {
			this.filterFileBrowseUrl(shop);
			// 获取店铺相册图片
			List<ShopAlbumImg> shopAlbumImgs = shopAlbumImgDao.selectByShopId(id);
			for (ShopAlbumImg shopAlbumImg : shopAlbumImgs) {
				this.filterFileBrowseUrl(shopAlbumImg);
			}
			shop.setShopAlbumImgs(shopAlbumImgs);
			//
			Integer merchantId = shop.getMerchantId();
			shopDto.setMerchantId(merchantId);
			if (merchantId != null) {
				Merchant merchant = merchantDao.selectById(merchantId);
				shopDto.setMerchant(merchant);
			}
			//
			Integer bizLicenseId = shop.getLicenseId();
			shopDto.setLicenseId(bizLicenseId);
			if (bizLicenseId != null) {
				BizLicense bizLicense = bizLicenseDao.selectById(bizLicenseId);
				BizLicensePic bizLicensePic = bizLicensePicDao.selectByLicenseId(bizLicenseId);
				this.filterFileBrowseUrl(bizLicensePic);
				shopDto.setBizLicensePic(bizLicensePic);
				shopDto.setBizLicense(bizLicense);

			}
			shopDto.setId(shop.getId());
			shopDto.setShop(shop);

			// 查询门店提供的服务列表
			List<ShopSvc> shopSvcs = shopSvcDao.selectByShopId(shop.getId());
			Map<String, List<Svcx>> svcxsByGroup = new HashMap<String, List<Svcx>>();
			for (ShopSvc shopSvc : shopSvcs) {
				Svcx svcx = carSvcService.getCarSvc(shopSvc.getSvcId());
				if (svcxsByGroup.containsKey(svcx.getGrounpName())) {
					List<Svcx> svcList = svcxsByGroup.get(svcx.getGrounpName());
					if (svcList == null) {
						svcList = new ArrayList<Svcx>();
					}
					svcList.add(svcx);
				} else {
					List<Svcx> svcs = new ArrayList<Svcx>();
					svcs.add(svcx);
					svcxsByGroup.put(svcx.getGrounpName(), svcs);
				}

			}
			shopDto.setShopSvcs(shopSvcs);
			shopDto.setShopSvcsByGroup(svcxsByGroup);
		}
		return shopDto;
	}

	@Override
	public Shop getShopById(Integer shopId) {
		Shop shop = shopDao.selectById(shopId);
		this.filterFileBrowseUrl(shop);
		if (shop.getPoint() != null) {
			List<ShopGrade> listShopGrade = this.getGradesByPoint(shop.getPoint());
			if (listShopGrade.size() > 0) {
				shop.setShopGrade(listShopGrade.get(0));
			}
		} else {
			List<ShopGrade> listShopGrade = this.getGradesByPoint(1);
			if (listShopGrade.size() > 0) {
				shop.setShopGrade(listShopGrade.get(0));
			}
		}
		return shop;
	}

	@Override
	public boolean updateChangeTime(Integer id, boolean updateOrNot) {
		return updateOrNot && shopDao.updateChangeTime(id) > 0;
	}

	// --------------------------------------- shopGoodsCat
	// ----------------------------------------------

	@Override
	public int getShopCountByCatId(Integer catId) {
		return shopGoodsCatDao.countShopByCatId(catId);
	}

	// ----------------------------------------------店铺参数设置----------------------------------------------

	@Override
	public ShopParam getShopParamByShopIdAndCode(Integer shopId, String code) {
		return shopParamDao.selectByShopIdAndCode(shopId, code);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean saveShopParamDto(ShopParamDto shopParamDto) {
		boolean flag = true;
		ShopParam shopParam = new ShopParam();
		// 商品相关设置 code : goods
		if (shopParamDto.goodsStockAlertValve != null) {
			shopParam = shopParamDto.toShopParam(ShopParam.goods.code);
			if (shopParamDto.id == null) {
				flag = shopParamDao.insert(shopParam) > 0 && flag;
			} else {
				flag = shopParamDao.update(shopParam) > 0 && flag;
			}
		}
		// 店铺发票设置code : invoice
		if (shopParamDto.invoicePlainProvided != null) {
			shopParam = shopParamDto.toShopParam(ShopParam.invoice.code);
			if (shopParamDto.id == null) {
				flag = shopParamDao.insert(shopParam) > 0 && flag;
			} else {
				flag = shopParamDao.update(shopParam) > 0 && flag;
			}
		}
		// 短信发送设置code : sms
		if (shopParamDto.smsSendToCustomerOnMerchantDispatching != null) {
			shopParam = shopParamDto.toShopParam(ShopParam.sms.code);
			if (shopParamDto.id == null) {
				flag = shopParamDao.insert(shopParam) > 0 && flag;
			} else {
				flag = shopParamDao.update(shopParam) > 0 && flag;
			}
		}
		// 配送方式设置code : delivery.way
		if (shopParamDto.deliveryWayIds != null) {
			shopParam = shopParamDto.toShopParam(ShopParam.deliveryWay.code);
			ShopParam param = shopParamDao.selectByShopIdAndCode(shopParamDto.shopId, "delivery.way");
			if (param == null) {
				flag = shopParamDao.insert(shopParam) > 0 && flag;
			} else {
				shopParam.setId(param.getId());
				String json = shopParam.getValue();
				Map<String, Object> map = JsonUtil.fromJson(json, TypeUtil.TypeRefs.StringObjectMapType);
				List<Integer> ids = (List<Integer>) map.get("ids");
				if (ids.size() == 1 && ids.get(0) == -1) {
					flag = shopParamDao.deleteById(param.getId()) > 0 && flag;
				} else {
					flag = shopParamDao.update(shopParam) > 0 && flag;
				}
			}
		}
		// 运费设置code : freight.fee
		if (shopParamDto.freightFeeFixed != null) {
			shopParam = shopParamDto.toShopParam(ShopParam.freightFee.code);
			if (shopParamDto.id == null) {
				flag = shopParamDao.insert(shopParam) > 0 && flag;
			} else {
				flag = shopParamDao.update(shopParam) > 0 && flag;
			}
		}
		return flag;
	}

	// -------------------------------------------店铺相册--------------------------------------------------------
	@Override
	public PaginatedList<ShopAlbumImg> getShopImages(PaginatedFilter paginatedFilter) {
		// TODO Auto-generated method stub
		PaginatedList<ShopAlbumImg> shopAlbumImgPageList = shopAlbumImgDao.selectList(paginatedFilter);
		List<ShopAlbumImg> shopAlbumImgs = shopAlbumImgPageList.getRows();
		for (ShopAlbumImg shopAlbumImg : shopAlbumImgs) {
			this.filterFileBrowseUrl(shopAlbumImg);
		}
		return shopAlbumImgDao.selectList(paginatedFilter);
	}

	@Override
	public boolean saveShopImage(ShopAlbumImg shopAlbumImg) {
		return shopAlbumImgDao.insert(shopAlbumImg) > 0;
	}

	@Override
	public boolean updateShopImage(ShopAlbumImg shopAlbumImg) {
		return shopAlbumImgDao.update(shopAlbumImg) > 0;
	}

	@Override
	public boolean deleteShopImageById(Integer imageId) {
		return shopAlbumImgDao.deleteById(imageId) > 0;
	}

	@Override
	public boolean deleteShopImagesByIds(List<Integer> imageIds) {
		return shopAlbumImgDao.deleteByIds(imageIds) > 0;
	}

	// -------------------------------------------店铺公告--------------------------------------------------------

	@Override
	public boolean saveShopNotice(ShopNotice shopNotice) {
		return shopNoticeDao.insert(shopNotice) > 0;
	}

	@Override
	public PaginatedList<ShopNotice> getShopNotices(PaginatedFilter paginatedFilter) {
		return shopNoticeDao.selectList(paginatedFilter);
	}

	@Override
	public boolean updateShopNotice(ShopNotice shopNotice) {
		return shopNoticeDao.update(shopNotice) > 0;
	}

	@Override
	public boolean deleteShopNoticeById(Integer noticeId) {
		return shopNoticeDao.deleteById(noticeId) > 0;
	}

	@Override
	public boolean deleteShopNoticesByIds(List<Integer> noticeIds) {
		return shopNoticeDao.deleteByIds(noticeIds) > 0;
	}

	@Override
	public PaginatedList<BizLicense> getBizLicensesByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<BizLicense> bizLicensePageList = bizLicenseDao.selectByFilter(paginatedFilter);
		List<BizLicense> bizLicenses = bizLicensePageList.getRows();
		for (BizLicense license : bizLicenses) {
			Integer licenseId = license.getId();
			BizLicensePic licensePic = bizLicensePicDao.selectByLicenseId(licenseId);
			if (licensePic != null) {
				this.filterFileBrowseUrl(licensePic);
				license.setBizLicensePic(licensePic);
			}
		}
		return bizLicensePageList;
	}

	@Override
	public boolean createBizLicense(BizLicense bizLicense) {
		int count = bizLicenseDao.insert(bizLicense);
		if (count > 0) {
			BizLicensePic bizLicensePic = bizLicense.getBizLicensePic();
			if (bizLicensePic != null) {
				bizLicensePic.setLicenseId(bizLicense.getId());
				bizLicensePicDao.insert(bizLicensePic);
			}
		}
		return count > 0;
	}

	@Override
	public Shop getShopByMerchantIdAndName(Integer merchantId, String shopName) {
		return shopDao.selectByMerchantIdAndName(merchantId, shopName);
	}

	@Override
	public boolean saveShop(Shop shop) {
		String code = CodeUtil.newShopCode();
		// String code = mallDao.selectById(1).getCode();
		// code = code + "-dp-" + CodeUtil.generateRandomCode(8);
		shop.setCode(code);
		shop.setSelfFlag(false);
		String name = shop.getName();
		shop.setPy(StrUtil.chsToPy(name));
		boolean success = false;
		// 通过regionId获取相关信息
		RegionParts regionParts = settingService.getRegionPartsById(shop.getRegionId());
		if (regionParts != null) {
			success = true;
		}
		shop.setRegionName(regionParts.fullName);
		shop.setProvinceId(regionParts.provinceId);
		shop.setCityId(regionParts.cityId);
		shop.setCountyId(regionParts.countyId);
		shop.setTownId(regionParts.townId);
		//
		shop.setEntpFlag(false);
		shop.setRegTime(new Date());
		shop.setApplyTime(new Date());
		shop.setAuditStatus(0);
		shop.setClosed(false);
		shop.setDisabled(false);
		success = shopDao.insert(shop) > 0 && success;
		// 插入一个店铺管理员角色
		if (success) {
			UserRole userRole = new UserRole();
			userRole.setEntityId(shop.getId());
			userRole.setScope(AuthScope.shop);
			userRole.setUserId(shop.getMerchantId());
			Integer shopAdminId = 3;
			userRole.setRoleId(shopAdminId);
			success = userRoleDao.insert(userRole) > 0 && success;
		}

		if (success) {
			this.sendShopChangeQueueMessage(shop.getId());
		}

		return success;
	}

	@Override
	public boolean updateShop(Shop shop) {
		// 通过regionId获取相关信息
		RegionParts regionParts = settingService.getRegionPartsById(shop.getRegionId());
		shop.setRegionName(regionParts.fullName);
		shop.setProvinceId(regionParts.provinceId);
		shop.setCityId(regionParts.cityId);
		shop.setCountyId(regionParts.countyId);
		shop.setTownId(regionParts.townId);
		shop.setChangeTime(new Date());
		boolean success = shopDao.update(shop) > 0;
		if (success) {
			this.sendShopChangeQueueMessage(shop.getId());
		}
		return success;
	}

	// ---------------------------店铺商品处理---------------------------------------------------
	@Override
	public PaginatedList<ShopProduct> getShopProductsByFilter(PaginatedFilter paginatedFilter) {
		//
		PaginatedList<ShopProduct> shopProducts = shopProductDao.selectByFilter(paginatedFilter);
		//
		return shopProducts;
	}

	@Override
	public Boolean updateShopProductLackFlagById(Long shopProductId, Boolean lackFlag) {
		ShopProduct shopProduct = shopProductDao.selectById(shopProductId);
		boolean success = shopProduct != null && shopProductDao.updateLackFlagById(shopProductId, lackFlag) > 0;
		//
		if (success) {
			Integer shopId = shopProduct.getShopId();
			this.updateChangeTime(shopId, true);
			this.sendShopChangeQueueMessage(shopId);
		}
		//
		return success;
	}

	@Override
	public boolean saveProduct(Integer shopId, Long productId) {
		ShopProduct shopProduct = new ShopProduct();
		shopProduct.setShopId(shopId);
		shopProduct.setProductId(productId);
		Product product = productDao.selectById(productId);
		if (product != null) {
			shopProduct.setGoodsId(product.getGoodsId());
			shopProduct.setGoodsName(product.getGoodsName());
			shopProduct.setTitle(product.getTitle());
			shopProduct.setSalePrice(product.getSalePrice());
			shopProduct.setCatId(product.getCatId());
			shopProduct.setLackFlag(false);
			shopProduct.setTs(new Date());
			//
			int count = shopProductDao.insert(shopProduct);
			//
			this.updateChangeTime(shopId, true);
			this.sendShopChangeQueueMessage(shopId);
			//
			return count > 0;
		}
		return false;
	}

	@Override
	public boolean existShopProductByFilter(MapContext filters) {
		List<ShopProduct> shopProducts = shopProductDao.selectByFilter(filters);
		return shopProducts != null && shopProducts.size() > 0;
	}

	@Override
	public boolean deleteProductByShopProductId(Long shopProductId) {
		ShopProduct shopProduct = shopProductDao.selectById(shopProductId);
		boolean success = shopProduct != null && shopProductDao.deleteById(shopProductId) > 0;
		if (success) {
			Integer shopId = shopProduct.getShopId();
			this.updateChangeTime(shopId, true);
			this.sendShopChangeQueueMessage(shopId);
		}
		return success;
	}

	@Override
	public int getShopCountByProductId(Long productId) {
		int count = shopProductDao.selectCountShopIdByProductId(productId);
		return count;
	}

	@Override
	public Map<Long, ShopProduct> getProductLackByShopIdAndProductIds(Integer shopId, List<Long> productIds) {
		return shopProductDao.selectLackFlagByShopIdAndProductIds(shopId, productIds);
	}

	@Override
	public Map<Long, ShopSvc> getSvcStatusByShopIdAndSvcIds(Integer shopId, List<Long> svcIds) {
		return shopSvcDao.selectByShopIdAndSvcIds(shopId, svcIds);
	}

	@Override
	public boolean deleteShopProductsByIds(List<Long> shopProductIds) {
		boolean ok = true;
		for (Long id : shopProductIds) {
			ok = deleteProductByShopProductId(id) && ok;
		}
		return ok;
	}

	@Override
	public List<User> getShopWorkersById(Integer shopId, boolean includeRoles) {
		return userService.getAllUsersByScopeAndEntityId(AuthScope.shop, shopId, includeRoles);
	}

	@Override
	public ShopBizStatus getShopBizStatusById(Integer shopId) {
		return getShopBizStatusByIdAndDate(shopId, null);
	}

	@Override
	public ShopBizStatus getShopBizStatusByIdAndDate(Integer shopId, Date bizDate) {
		if (bizDate == null) {
			bizDate = new Date();
		}
		String dateStr = DateUtil.toStdDateStr(bizDate);
		return shopBizStatusDao.selectByShopIdAndDateStr(shopId, dateStr);
	}

	@Override
	public boolean saveShopBizStatus(ShopBizStatus shopBizStatus) {
		// 检查店铺id
		assert shopBizStatus != null && shopBizStatus.getShopId() != null;
		//
		String dateStr = shopBizStatus.getDateStr();
		Date bizDate = shopBizStatus.getBizDate();
		// 检查日期
		assert StrUtil.hasText(dateStr) || bizDate != null;
		// 检查自动同步时间字段
		if (bizDate == null) {
			// bizDate <= dateStr
			try {
				bizDate = DateUtil.fromStdDateStr(dateStr);
				shopBizStatus.setBizDate(bizDate);
			} catch (ParseException e) {
				throw new ValidationException("给定的日期的格式有误（应为 yyyy-MM-dd）");
			}
		} else if (dateStr == null) {
			// dateStr <= bizDate
			dateStr = DateUtil.toStdDateStr(bizDate);
			shopBizStatus.setDateStr(dateStr);
		}
		// 检查数据库是否已存在
		ShopBizStatus dbOne = null;
		Long id = shopBizStatus.getId();
		if (id != null) {
			// 按id查询
			dbOne = shopBizStatusDao.selectById(id);
		} else {
			Integer shopId = shopBizStatus.getShopId();
			dbOne = shopBizStatusDao.selectByShopIdAndDateStr(shopId, dateStr);
		}
		int count = 0;
		if (dbOne != null) {
			// 更新
			if (id == null) {
				shopBizStatus.setId(dbOne.getId());
			}
			count = shopBizStatusDao.update(shopBizStatus);
		} else {
			count = shopBizStatusDao.insert(shopBizStatus);
		}
		//
		return count > 0;
	}

	@Override
	public boolean updateShopAsIndexed(Integer id, Date newIndexTime) {
		return newIndexTime != null && shopDao.updateAsIndexed(id, newIndexTime) > 0;
	}

	@Override
	public PaginatedList<ShopDto> getShopsByLatestChanges(PaginatedFilter pager) {
		PaginatedList<Shop> pagListShop = shopDao.selectByLatestChanges(pager);
		PaginatedList<ShopDto> pagListShopDto = new PaginatedList<>();
		pagListShopDto.setPagination(pagListShop.getPagination());
		List<Shop> listShop = pagListShop.getRows();
		List<ShopDto> listShopDto = pagListShopDto.getRows();
		for (Shop shop : listShop) {
			this.filterFileBrowseUrl(shop);
			//
			ShopDto shopDto = new ShopDto();
			// 获取店铺相册图片
			List<ShopAlbumImg> shopAlbumImgs = shopAlbumImgDao.selectByShopId(shop.getId());
			for (ShopAlbumImg shopAlbumImg : shopAlbumImgs) {
				this.filterFileBrowseUrl(shopAlbumImg);
			}
			shop.setShopAlbumImgs(shopAlbumImgs);

			Integer merchantId = shop.getMerchantId();
			if (merchantId != null) {
				Merchant merchant = merchantDao.selectById(merchantId);
				shopDto.setMerchant(merchant);
			}

			Integer bizLicenseId = shop.getLicenseId();
			if (bizLicenseId != null) {
				BizLicense bizLicense = bizLicenseDao.selectById(bizLicenseId);
				shopDto.setBizLicense(bizLicense);
			}
			shopDto.setId(shop.getId());
			shopDto.setShop(shop);
			// List<Svcx> shopSvcs = svcxDao.selectByShopId(shop.getId());
			List<ShopSvc> shopSvcs = shopSvcDao.selectByShopId(shop.getId());
			for (ShopSvc shopSvc : shopSvcs) {
				Svcx svcx = carSvcService.getCarSvc(shopSvc.getSvcId());
				shopSvc.setSvc(svcx);
			}
			shopDto.setShopSvcs(shopSvcs);

			listShopDto.add(shopDto);
		}
		pagListShopDto.setRows(listShopDto);

		return pagListShopDto;
	}

	@Override
	public List<Integer> getProductIdsByShopIdAndLackFlag(Integer shopId) {
		return shopDao.selectProductIdsByShopIdAndLackFlag(shopId);
	}

	// ---------------------------------------店铺备忘录--------------------------------------------
	@Override
	public boolean saveShopMemo(ShopMemo shopMemo) {
		return shopMemoDao.insert(shopMemo) > 0;
	}

	@Override
	public boolean deleteShopMemoById(Integer id) {
		return shopMemoDao.deleteById(id) > 0;
	}

	@Override
	public boolean updateShopMemo(ShopMemo shopMemo) {
		return shopMemoDao.update(shopMemo) > 0;
	}

	@Override
	public PaginatedList<ShopMemo> getShopMemosByFilter(PaginatedFilter paginatedFilter) {
		MapContext filter = paginatedFilter.getFilterItems();
		if (filter != null) {
			if (StrUtil.hasText(paginatedFilter.getKeywords())) {
				filter.put("keywords", paginatedFilter.getKeywords());
			}
		}
		return shopMemoDao.selectByFilter(paginatedFilter);
	}

	@Override
	public ShopMemo getShopMemoById(Integer id) {
		return shopMemoDao.selectById(id);
	}

	@Override
	public ShopMemo getShopMemoByIdAndShopId(Integer id, Integer shopId) {
		return shopMemoDao.selectByIdAndShopId(id, shopId);
	}

	// -----------------------------------------------------------------------------------
}
