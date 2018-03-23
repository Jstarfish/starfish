package priv.starfish.mall.service.impl;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import priv.starfish.common.base.TargetJudger;
import priv.starfish.common.exception.BusinessException;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.repo.FileRepository;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.CodeUtil;
import priv.starfish.common.util.CollectionUtil;
import priv.starfish.common.util.DateUtil;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.cart.dto.SaleCartInfo;
import priv.starfish.mall.cart.entity.SaleCartGoods;
import priv.starfish.mall.cart.entity.SaleCartSvc;
import priv.starfish.mall.dao.goods.GoodsDao;
import priv.starfish.mall.dao.goods.ProductDao;
import priv.starfish.mall.goods.entity.Goods;
import priv.starfish.mall.goods.entity.Product;
import priv.starfish.mall.dao.mall.MallDao;
import priv.starfish.mall.dao.market.CouponDao;
import priv.starfish.mall.dao.market.EcardActDao;
import priv.starfish.mall.dao.market.EcardActGiftDao;
import priv.starfish.mall.dao.market.EcardActRuleDao;
import priv.starfish.mall.dao.market.PrmtTagDao;
import priv.starfish.mall.dao.market.PrmtTagGoodsDao;
import priv.starfish.mall.dao.market.PrmtTagSvcDao;
import priv.starfish.mall.dao.market.SvcCouponDao;
import priv.starfish.mall.dao.market.UserCouponDao;
import priv.starfish.mall.dao.market.UserSvcCouponDao;
import priv.starfish.mall.market.dict.CouponType;
import priv.starfish.mall.market.dict.GiftType;
import priv.starfish.mall.market.entity.Coupon;
import priv.starfish.mall.market.entity.CouponNo;
import priv.starfish.mall.market.entity.EcardAct;
import priv.starfish.mall.market.entity.EcardActGift;
import priv.starfish.mall.market.entity.EcardActRule;
import priv.starfish.mall.market.entity.PrmtTag;
import priv.starfish.mall.market.entity.PrmtTagGoods;
import priv.starfish.mall.market.entity.PrmtTagSvc;
import priv.starfish.mall.market.entity.SvcCoupon;
import priv.starfish.mall.market.entity.UserCoupon;
import priv.starfish.mall.market.entity.UserSvcCoupon;
import priv.starfish.mall.service.ECardOrderService;
import priv.starfish.mall.service.GoodsService;
import priv.starfish.mall.service.SalePrmtService;
import priv.starfish.mall.dao.svcx.SvcxDao;
import priv.starfish.mall.svcx.entity.Svcx;

@Service("salePrmtService")
public class SalePrmtServiceimpl extends BaseServiceImpl implements SalePrmtService {
	/** 优惠券编号数字长度 */
	public static final int LENGTH_OF_COUPON_NO = 9;

	@Resource
	MallDao mallDao;

	@Resource
	CouponDao couponDao;

	@Resource
	UserCouponDao userCouponDao;

	@Resource
	GoodsDao goodsDao;

	@Resource
	SvcxDao carSvcDao;

	@Resource
	ProductDao productDao;

	@Resource
	SvcxDao svcxDao;

	@Resource
	SvcCouponDao svcCouponDao;

	@Resource
	UserSvcCouponDao userSvcCouponDao;

	@Resource
	PrmtTagDao prmtTagDao;

	@Resource
	FileRepository fileRepository;

	@Resource
	PrmtTagGoodsDao prmtTagGoodsDao;

	@Resource
	PrmtTagSvcDao prmtTagSvcDao;

	@Resource
	EcardActDao ecardActDao;

	@Resource
	EcardActRuleDao ecardActRuleDao;

	@Resource
	EcardActGiftDao ecardActGiftDao;

	@Resource
	ECardOrderService eCardOrderService;

	@Resource
	GoodsService goodsService;

	@Override
	public String newCouponNo(CouponType couponType) {
		String abbr = "X";
		if (couponType != null) {
			abbr = couponType.getAbbr();
		}
		Integer seqInt = mallDao.newSeqInt(CouponNo.class);
		return abbr + StrUtil.leftPadding(seqInt.toString(), LENGTH_OF_COUPON_NO, '0');
	}

	// -------------------------------优惠券-----------------------------------------------------

	@Override
	public boolean saveCoupon(Coupon coupon) {
		coupon.setCreateTime(new Date());
		coupon.setDeleted(false);
		coupon.setDisabled(false);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		coupon.setYear(calendar.get(Calendar.YEAR));
		if (coupon.getLimitAmount() == null) {
			coupon.setLimitAmount(0);
		}
		if (coupon.getPrice() == null) {
			coupon.setPrice(new BigDecimal(0));
		}
		if (coupon.getSettlePrice() == null) {
			coupon.setSettlePrice(new BigDecimal(0));
		}
		return couponDao.insert(coupon) > 0;
	}

	@Override
	public PaginatedList<Coupon> getCouponsByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<Coupon> pagListCoupon = couponDao.selectByFilter(paginatedFilter);
		List<Coupon> listCoupon = pagListCoupon.getRows();
		if (listCoupon != null && listCoupon.size() > 0) {
			for (Coupon coupon : listCoupon) {
				// setCoupon(coupon);
				Long productId = coupon.getProductId();
				if (productId != null) {
					coupon.setProductName(getProductName(productId));
				}
			}
		}
		return pagListCoupon;
	}

	@Override
	public boolean updateCouponByIdAndDeleted(Integer id, Boolean deleted) {
		return couponDao.updateDeletedById(id, deleted) > 0;
	}

	@Override
	public boolean updateCouponByIdAndDisabled(Integer id, Boolean disabled) {
		return couponDao.updateByIdAndState(id, disabled) > 0;
	}

	@Override
	public boolean updateCoupon(Coupon coupon) {
		coupon.setUpdateTime(new Date());
		return couponDao.update(coupon) > 0;
	}

	@Override
	public Coupon getCouponForFirstECardOrder() {
		// 查询优惠券
		MapContext mapContext = MapContext.newOne();
		mapContext.put("deleted", false);
		mapContext.put("disabled", false);
		List<Coupon> listCoupon = couponDao.selectByFilter(mapContext);
		if (listCoupon != null && listCoupon.size() > 0) {
			for (Coupon coupon : listCoupon) {
				CouponType couponType = CouponType.valueOf(coupon.getType());
				if (CouponType.nopay.equals(couponType)) {
					return coupon;
				}
			}
		}
		return null;
	}

	@Override
	public boolean existCouponByGroupName(String name) {
		//
		return couponDao.selectCountByName(name) > 0;
	}

	// -------------------------------------用户商品优惠券----------------------------------------------------
	@Override
	public PaginatedList<UserCoupon> getUserCouponsByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<UserCoupon> pagListCoupon = userCouponDao.selectByFilter(paginatedFilter);
		//
		List<UserCoupon> listCoupon = pagListCoupon.getRows();
		if (listCoupon != null && listCoupon.size() > 0) {
			for (UserCoupon userCoupon : listCoupon) {
				// setCoupon(coupon);
				Long productId = userCoupon.getProductId();
				if (productId != null) {
					userCoupon.setProductName(getProductName(productId));
				}
			}
		}
		return pagListCoupon;
	}

	@Override
	public boolean saveUserCoupon(UserCoupon userCoupon) {
		return userCouponDao.insert(userCoupon) > 0;
	}

	@Override
	public Integer getUserCouponCountByUserId(Integer userId) {
		return userCouponDao.selectCountByUserId(userId);
	}

	@Override
	public boolean updateUserCouponForDelete(UserCoupon userCoupon, UserContext userContext) {
		userCoupon.setDeleted(true);
		userCoupon.setUserId(userContext.getUserId());
		return userCouponDao.updateForDelete(userCoupon) > 0;
	}

	@Override
	public boolean updateUserCouponByOrderId(UserCoupon userCoupon, Integer userId) {
		if (userCoupon != null && userCoupon.getOrderId() != null && userCoupon.getId() != null) {
			return userCouponDao.update(userCoupon) > 0;
		} else {
			return false;
		}
	}

	@Override
	public boolean createUserCoupon(Coupon coupon, Integer userId) {
		if (coupon != null) {
			Date date = new Date();
			UserCoupon userCoupon = UserCoupon.newOne();
			CouponType couponType = CouponType.valueOf(coupon.getType());
			userCoupon.setNo(newCouponNo(couponType));
			userCoupon.setRefId(coupon.getId());
			userCoupon.setType(coupon.getType());
			userCoupon.setName(coupon.getName());
			userCoupon.setTitle(coupon.getTitle());
			userCoupon.setProductId(coupon.getProductId());
			userCoupon.setPrice(coupon.getPrice() == null ? new BigDecimal(0) : coupon.getPrice());
			userCoupon.setSettlePrice(coupon.getSettlePrice());
			userCoupon.setUserId(userId);
			userCoupon.setCheckCode(CodeUtil.randomNumCode(6));
			userCoupon.setObtainTime(new Date());
			if (Coupon.COUPON_VALID_TYPE_DAY == coupon.getValidType()) {
				Date endTime = DateUtil.addDays(date, coupon.getValidDays());
				userCoupon.setStartTime(date);
				userCoupon.setEndTime(endTime);
			} else if (Coupon.COUPON_VALID_TYPE_TIME == coupon.getValidType()) {
				userCoupon.setStartTime(coupon.getValidStartTime());
				userCoupon.setEndTime(coupon.getValidEndTime());
			}
			userCoupon.setInvalid(false);
			userCoupon.setTs(date);
			userCoupon.setDeleted(false);
			return userCouponDao.insert(userCoupon) > 0;
		} else {
			return false;
		}
	}

	@Override
	public List<UserCoupon> getUserCouponsByFilter(MapContext filter) {
		List<UserCoupon> listCoupon = userCouponDao.selectByFilter(filter);
		//
		if (listCoupon != null && listCoupon.size() > 0) {
			for (UserCoupon userCoupon : listCoupon) {
				// setCoupon(coupon);
				Long productId = userCoupon.getProductId();
				if (productId != null) {
					userCoupon.setProductName(getProductName(productId));
				}
			}
		}
		return listCoupon;
	}

	// ----------------------------------------------------------服务优惠券----------------------------------------
	@Override
	public boolean saveSvcCoupon(SvcCoupon svcCoupon) {
		svcCoupon.setCreateTime(new Date());
		svcCoupon.setDeleted(false);
		svcCoupon.setDisabled(false);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		svcCoupon.setYear(calendar.get(Calendar.YEAR));
		if (svcCoupon.getLimitAmount() == null) {
			svcCoupon.setLimitAmount(0);
		}
		if (svcCoupon.getPrice() == null) {
			svcCoupon.setPrice(new BigDecimal(0));
		}
		if (svcCoupon.getSettlePrice() == null) {
			svcCoupon.setSettlePrice(new BigDecimal(0));
		}
		return svcCouponDao.insert(svcCoupon) > 0;
	}

	@Override
	public PaginatedList<SvcCoupon> getSvcCouponsByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<SvcCoupon> pagListCoupon = svcCouponDao.selectByFilter(paginatedFilter);
		List<SvcCoupon> listCoupon = pagListCoupon.getRows();
		if (listCoupon != null && listCoupon.size() > 0) {
			for (SvcCoupon svcCoupon : listCoupon) {
				// setSvcCoupon(svcCoupon);
				if (svcCoupon != null) {
					Integer svcId = svcCoupon.getSvcId();
					if (svcId != null) {
						svcCoupon.setSvcName(getSvcName(svcId));
					}
				}
			}
		}
		return pagListCoupon;
	}

	@Override
	public boolean updateSvcCouponForDeleted(Integer id, Boolean deleted) {
		//
		return svcCouponDao.updateForDeleted(id, deleted) > 0;
	}

	@Override
	public boolean updateSvcCouponForDisabled(Integer id, Boolean disabled) {
		//
		return svcCouponDao.updateForDisabled(id, disabled) > 0;
	}

	@Override
	public boolean updateSvcCoupon(SvcCoupon svcCoupon) {
		//
		svcCoupon.setUpdateTime(new Date());
		//
		return svcCouponDao.update(svcCoupon) > 0;
	}

	@Override
	public boolean existSvcCouponByGroupName(String name) {
		//
		return svcCouponDao.selectCountByName(name) > 0;
	}

	// 根据不同优惠券类别查询不同商品或服务名称放入优惠券中
	private String getSvcName(Integer svcId) {
		String svcName = null;
		if (svcId != null) {
			Svcx carSvc = carSvcDao.selectById(svcId);
			if (carSvc != null) {
				svcName = carSvc.getName();
				return svcName;
			} else {
				return svcName;
			}
		} else {
			return svcName;
		}

	}

	@Override
	public PaginatedList<UserSvcCoupon> getUserSvcCouponsByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<UserSvcCoupon> pagListCoupon = userSvcCouponDao.selectByFilter(paginatedFilter);
		//
		List<UserSvcCoupon> listCoupon = pagListCoupon.getRows();
		if (listCoupon != null && listCoupon.size() > 0) {
			for (UserSvcCoupon userSvcCoupon : listCoupon) {
				// setSvcCoupon(svcCoupon);
				if (userSvcCoupon != null) {
					Integer svcId = userSvcCoupon.getSvcId();
					if (svcId != null) {
						userSvcCoupon.setSvcName(getSvcName(svcId));
					}
				}
			}
		}
		return pagListCoupon;
	}

	@Override
	public List<UserSvcCoupon> getUserSvcCouponByFilter(MapContext filter) {
		List<UserSvcCoupon> listCoupon = userSvcCouponDao.selectByFilter(filter);
		if (listCoupon != null && listCoupon.size() > 0) {
			for (UserSvcCoupon userSvcCoupon : listCoupon) {
				// setSvcCoupon(svcCoupon);
				if (userSvcCoupon != null) {
					Integer svcId = userSvcCoupon.getSvcId();
					if (svcId != null) {
						userSvcCoupon.setSvcName(getSvcName(svcId));
					}
				}
			}
		}
		return listCoupon;
	}

	@Override
	public boolean saveUserSvcCoupon(UserSvcCoupon userSvcCoupon) {
		return userSvcCouponDao.insert(userSvcCoupon) > 0;
	}

	@Override
	public Integer getUserSvcCouponCountByUserId(Integer userId) {
		return userSvcCouponDao.selectCountByUserId(userId);
	}

	@Override
	public Map<String, Object> getMatchingUserCoupons(Integer userId, SaleCartInfo saleCartInfo) {
		// TODO Auto-generated method stub
		// 存放要返回适用的优惠券
		Map<String, Object> mapResultCoupon = new HashMap<String, Object>();
		if (saleCartInfo != null) {
			// 获取服务列表
			List<SaleCartSvc> svcList = saleCartInfo.getSaleCartSvcList();
			if (svcList != null && svcList.size() > 0) {
				Map<Integer, Object> mapSvcCoupon = new HashMap<Integer, Object>();
				for (SaleCartSvc saleCartSvc : svcList) {
					if (saleCartSvc != null) {
						MapContext mapContext = MapContext.newOne();
						Integer svcId = saleCartSvc.getSvcId();
						mapContext.put("svcId", svcId);
						mapContext.put("userId", userId);
						List<UserSvcCoupon> listUserCoupon = userSvcCouponDao.selectBySvcIdAndUserId(mapContext);
						if (listUserCoupon != null && listUserCoupon.size() > 0) {
							for (UserSvcCoupon userSvcCoupon : listUserCoupon) {
								userSvcCoupon.setSvcName(getSvcName(userSvcCoupon.getSvcId()));
							}
							//
							mapSvcCoupon.put(svcId, listUserCoupon);
						}
					}
				}
				mapResultCoupon.put("svc", mapSvcCoupon);
			}
			List<SaleCartGoods> goodsList = saleCartInfo.getSaleCartGoods();
			if (goodsList != null && goodsList.size() > 0) {
				Map<Long, Object> mapGoodsCoupon = new HashMap<Long, Object>();
				for (SaleCartGoods saleCartGoods : goodsList) {
					if (saleCartGoods != null) {
						// 获取当前用户可使用的优惠券
						MapContext mapContext = MapContext.newOne();
						Long productId = saleCartGoods.getProductId();
						mapContext.put("productId", productId);
						mapContext.put("userId", userId);
						List<UserCoupon> listUserCoupon = userCouponDao.selectByProductId(mapContext);
						if (listUserCoupon != null && listUserCoupon.size() > 0) {
							for (UserCoupon userCoupon : listUserCoupon) {
								userCoupon.setProductName(getProductName(userCoupon.getProductId()));
							}
							mapGoodsCoupon.put(productId, listUserCoupon);
						}
					}
				}
				mapResultCoupon.put("goods", mapGoodsCoupon);
			}
		}
		return mapResultCoupon;
	}

	@Override
	public boolean updateUserSvcCouponForDeleted(UserSvcCoupon userSvcCoupon, UserContext userContext) {
		userSvcCoupon.setDeleted(true);
		userSvcCoupon.setUserId(userContext.getUserId());
		return userSvcCouponDao.updateForDeleted(userSvcCoupon) > 0;
	}

	@Override
	public boolean updateUserSvcCouponByOrderId(UserSvcCoupon userSvcCoupon, Integer userId) {
		if (userSvcCoupon != null && userSvcCoupon.getOrderId() != null && userSvcCoupon.getId() != null) {
			return userSvcCouponDao.update(userSvcCoupon) > 0;
		} else {
			return false;
		}
	}

	@Override
	public boolean createUserSvcCoupon(SvcCoupon svcCoupon, Integer userId) {
		if (svcCoupon != null) {
			Date date = new Date();
			UserSvcCoupon userSvcCoupon = UserSvcCoupon.newOne();
			CouponType couponType = CouponType.valueOf(svcCoupon.getType());
			userSvcCoupon.setNo(newCouponNo(couponType));
			userSvcCoupon.setRefId(svcCoupon.getId());
			userSvcCoupon.setType(svcCoupon.getType());
			userSvcCoupon.setName(svcCoupon.getName());
			userSvcCoupon.setTitle(svcCoupon.getTitle());
			userSvcCoupon.setSvcId(svcCoupon.getSvcId());
			userSvcCoupon.setPrice(svcCoupon.getPrice() == null ? new BigDecimal(0) : svcCoupon.getPrice());
			userSvcCoupon.setSettlePrice(svcCoupon.getSettlePrice());
			userSvcCoupon.setUserId(userId);
			userSvcCoupon.setCheckCode(CodeUtil.randomNumCode(6));
			userSvcCoupon.setObtainTime(new Date());
			if (SvcCoupon.COUPON_VALID_TYPE_DAY == svcCoupon.getValidType()) {
				Date endTime = DateUtil.addDays(date, svcCoupon.getValidDays());
				userSvcCoupon.setStartTime(date);
				userSvcCoupon.setEndTime(endTime);
			} else if (SvcCoupon.COUPON_VALID_TYPE_TIME == svcCoupon.getValidType()) {
				userSvcCoupon.setStartTime(svcCoupon.getValidStartTime());
				userSvcCoupon.setEndTime(svcCoupon.getValidEndTime());
			}
			userSvcCoupon.setInvalid(false);
			userSvcCoupon.setTs(date);
			userSvcCoupon.setDeleted(false);
			return userSvcCouponDao.insert(userSvcCoupon) > 0;
		} else {
			return false;
		}
	}

	// 根据不同优惠券类别查询不同商品或服务名称放入优惠券中
	private String getProductName(Long productId) {
		String productName = null;
		if (productId != null) {
			// 获取商品信息
			Product product = productDao.selectById(productId);
			if (product != null) {
				productName = product.getTitle();
				return productName;
			} else {
				return productName;
			}
		} else {
			return productName;
		}

	}

	// ----------------------------------促销标签-------------------------------------------------

	@Override
	public PaginatedList<PrmtTag> getPrmtTagsByFilter(PaginatedFilter paginatedFilter) {
		// productId、svcId只能有一个不为null
		Long productId = null;
		Integer svcId = null;
		MapContext filterItems = paginatedFilter.getFilterItems();
		if (filterItems != null) {
			productId = filterItems.getTypedValue("productId", Long.class);
			svcId = filterItems.getTypedValue("svcId", Integer.class);
		}
		//
		PaginatedList<PrmtTag> prmtTagList = prmtTagDao.selectByFilter(paginatedFilter);
		List<PrmtTag> tagList = prmtTagList.getRows();
		// 设置icon浏览路径
		if (tagList != null && !tagList.isEmpty()) {
			for (PrmtTag prmtTag : tagList) {
				// 设置图标浏览路径
				String browseUrl = fileRepository.getFileBrowseUrl(prmtTag.getIconUsage(), prmtTag.getIconPath());
				prmtTag.setFileBrowseUrl(browseUrl);
				if (productId != null) {
					// 检查是否存在
					Integer tagId = prmtTag.getId();
					PrmtTagGoods prmtTagGoods = prmtTagGoodsDao.selectByTagIdAndProductId(tagId, productId);
					boolean existFlag = !(prmtTagGoods == null);
					prmtTag.setExistFlag(existFlag);
					continue;
					//
				}
				if (svcId != null) {
					// 检查是否存在
					Integer tagId = prmtTag.getId();
					PrmtTagSvc prmtTagSvc = prmtTagSvcDao.selectByTagIdAndSvcId(tagId, svcId);
					boolean existFlag = !(prmtTagSvc == null);
					prmtTag.setExistFlag(existFlag);
					//
				}

			}
		}
		//
		return prmtTagList;
	}

	@Override
	public boolean savePrmtTagProduct(Integer prmtTagId, Long productId) {
		PrmtTagGoods prmtTagGoods = new PrmtTagGoods();
		prmtTagGoods.setTagId(prmtTagId);
		prmtTagGoods.setProductId(productId);
		Product product = productDao.selectById(productId);
		if (product == null) {
			throw new BusinessException("找不到当前货品[id=" + productId + "]信息");
		}
		Integer goodsId = product.getGoodsId();
		Goods goods = goodsDao.selectById(goodsId);
		if (goods == null) {
			throw new BusinessException("找不到当前商品[id=" + goodsId + "]信息");
		}
		prmtTagGoods.setGoodsId(goodsId);
		prmtTagGoods.setShopId(goods.getShopId());
		prmtTagGoods.setTs(new Date());
		//
		int count = prmtTagGoodsDao.insert(prmtTagGoods);
		return count > 0;
	}

	@Override
	public boolean deletePrmtTagProduct(Integer tagId, Long productId) {
		int count = prmtTagGoodsDao.deleteByTagIdAndProductId(tagId, productId);
		return count > 0;
	}

	@Override
	public boolean savePrmtTagSvc(Integer prmtTagId, Integer svcId) {
		PrmtTagSvc prmtTagSvc = new PrmtTagSvc();
		prmtTagSvc.setTagId(prmtTagId);
		prmtTagSvc.setSvcId(svcId);
		prmtTagSvc.setTs(new Date());
		int count = prmtTagSvcDao.insert(prmtTagSvc);
		return count > 0;
	}

	@Override
	public boolean deletePrmtTagSvc(Integer tagId, Integer svcId) {
		int count = prmtTagSvcDao.deleteByTagIdAndSvcId(tagId, svcId);
		return count > 0;
	}

	@Override
	public PaginatedList<PrmtTagGoods> getPrmtTagProductsByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<PrmtTagGoods> tagGoodsPageList = prmtTagGoodsDao.selectByFilter(paginatedFilter);
		List<PrmtTagGoods> prmtTagGoodsList = tagGoodsPageList.getRows();
		for (PrmtTagGoods prmtTagGoods : prmtTagGoodsList) {
			// 设置货品信息
			Long productId = prmtTagGoods.getProductId();
			Product product = productDao.selectById(productId);
			prmtTagGoods.setProduct(product);
			// 设置标签信息
			Integer tagId = prmtTagGoods.getTagId();
			PrmtTag prmtTag = prmtTagDao.selectById(tagId);
			prmtTagGoods.setPrmtTag(prmtTag);
		}
		//
		return tagGoodsPageList;
	}

	@Override
	public List<PrmtTag> getPrmtTags() {
		return prmtTagDao.selectAll();
	}

	@Override
	public PaginatedList<PrmtTagSvc> getPrmtTagSvcsByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<PrmtTagSvc> tagGoodsPageList = prmtTagSvcDao.selectByFilter(paginatedFilter);
		List<PrmtTagSvc> prmtTagGoodsList = tagGoodsPageList.getRows();
		for (PrmtTagSvc prmtTagSvc : prmtTagGoodsList) {
			// 设置货品信息
			Integer svcId = prmtTagSvc.getSvcId();
			Svcx svcx = svcxDao.selectById(svcId);
			prmtTagSvc.setSvcx(svcx);
			// 设置标签信息
			Integer tagId = prmtTagSvc.getTagId();
			PrmtTag prmtTag = prmtTagDao.selectById(tagId);
			prmtTagSvc.setPrmtTag(prmtTag);
		}
		//
		return tagGoodsPageList;
	}

	@Override
	public boolean updatePrmtTagProductSeqNos(List<Map<String, Object>> tagProductSeqNos) {
		boolean ok = true;
		for (Map<String, Object> tagProductSeqNoMap : tagProductSeqNos) {
			Long tagProductId = Long.valueOf(tagProductSeqNoMap.get("id").toString());
			Integer seqNo = Integer.valueOf(tagProductSeqNoMap.get("seqNo").toString());
			PrmtTagGoods prmtTagGoods = new PrmtTagGoods();
			prmtTagGoods.setId(tagProductId);
			prmtTagGoods.setSeqNo(seqNo);
			int count = prmtTagGoodsDao.update(prmtTagGoods);
			ok = ok && count > 0;
		}
		return ok;
	}

	@Override
	public boolean updatePrmtTagSvcSeqNos(List<Map<String, Object>> tagSvcSeqNos) {
		boolean ok = true;
		for (Map<String, Object> tagSvcSeqNoMap : tagSvcSeqNos) {
			Long tagSvcId = Long.valueOf(tagSvcSeqNoMap.get("id").toString());
			Integer seqNo = Integer.valueOf(tagSvcSeqNoMap.get("seqNo").toString());
			PrmtTagSvc prmtTagSvc = new PrmtTagSvc();
			prmtTagSvc.setId(tagSvcId);
			prmtTagSvc.setSeqNo(seqNo);
			int count = prmtTagSvcDao.update(prmtTagSvc);
			ok = ok && count > 0;
		}
		return ok;
	}

	@Override
	public PaginatedList<EcardAct> getEcardActByFilter(PaginatedFilter paginatedFilter) {
		Date date = new Date();
		//
		PaginatedList<EcardAct> actPageList = ecardActDao.selectByFilter(paginatedFilter);
		List<EcardAct> catList = actPageList.getRows();
		if (catList != null) {
			for (EcardAct ecardAct : catList) {
				Date startTime = ecardAct.getStartTime();
				Date endTime = ecardAct.getEndTime();
				Boolean disabled = ecardAct.getDisabled();
				Boolean deleted = ecardAct.getDeleted();
				if (deleted == true) {
					ecardAct.setActState("deleted");// 已删除
				} else {
					if (disabled == false) {
						Date ymdDate = DateUtil.castTo_yyyyMMdd(date);
						if (startTime != null && ymdDate.getTime() < startTime.getTime()) {
							ecardAct.setActState("unStarted");// 未开始
						} else if (endTime != null && ymdDate.getTime() > endTime.getTime()) {
							ecardAct.setActState("ended");// 已结束
						} else {
							ecardAct.setActState("onGoing");// 进行中
						}
					} else {
						ecardAct.setActState("disabled");// 已停用
					}
				}
				// 获取规则
				ecardAct.setEcardRuleList(getEcardActRulesByActId(ecardAct.getId()));
			}
		}
		return actPageList;
	}

	@Override
	public boolean saveEcardAct(EcardAct ecardAct) {
		if (ecardAct != null) {
			Date date = new Date();
			// TODO 业务逻辑
			ecardAct.setDeleted(false);
			ecardAct.setDisabled(false);
			ecardAct.setTs(date);
			Calendar calendar = Calendar.getInstance();
			ecardAct.setYear(calendar.get(Calendar.YEAR));
			boolean ok = ecardActDao.insert(ecardAct) > 0;
			if (ok) {
				// 报错规则
				List<EcardActRule> ruleList = ecardAct.getEcardRuleList();
				if (ruleList != null && ruleList.size() > 0) {
					for (EcardActRule ecardActRule : ruleList) {
						if (ecardActRule != null) {
							ecardActRule.setActId(ecardAct.getId());
							saveEcardActRule(ecardActRule);
						}
					}
				}
			}
		}
		return true;
	}

	@Override
	public boolean updateEcardAct(EcardAct ecardAct) {
		if (ecardAct != null) {
			// 获取客户端的规则列表
			List<EcardActRule> listRule = ecardAct.getEcardRuleList();
			// 查询数据库规则列表
			List<EcardActRule> dbListRule = getEcardActRulesByActId(ecardAct.getId());
			if (listRule != null && listRule.size() > 0) {
				if (dbListRule != null && dbListRule.size() > 0) {
					for (int i = 0; i < dbListRule.size();) {
						final EcardActRule dbItem = dbListRule.get(i);
						// 查找对应的新数据索引
						int ruleIndex = CollectionUtil.searchIndex(listRule, new TargetJudger<EcardActRule>() {
							@Override
							public boolean isTarget(EcardActRule toBeChecked) {
								// 按唯一键判断
								return toBeChecked.getId().equals(dbItem.getId());
							}
						});
						// 处理更新
						if (ruleIndex != -1) {
							EcardActRule itemRule = listRule.get(ruleIndex);
							// 合并数据更新
							updateEcardActRule(itemRule);
							// 删除db和客户端
							dbListRule.remove(i);
							listRule.remove(ruleIndex);
						} else {
							i++;
						}
					}

					// dbItemList中剩余的都是需要删除的
					for (int i = 0; i < dbListRule.size(); i++) {
						EcardActRule dbItem = dbListRule.get(i);
						// 从数据库中删除...
						deleteEcardActRule(dbItem.getId());
					}
				}

				// itemList中剩余的都是需要新增的
				for (int i = 0; i < listRule.size(); i++) {
					EcardActRule itemRule = listRule.get(i);
					// 添加到数据库中...
					itemRule.setActId(ecardAct.getId());
					// saveSvcPackItem(srcItem);
					saveEcardActRule(itemRule);
				}
			}
		}
		return ecardActDao.update(ecardAct) > 0;
	}

	@Override
	public boolean existEcardActByName(String name) {
		boolean ok = false;
		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		EcardAct ecardAct = ecardActDao.selectByYearAndName(year, name);
		if (ecardAct != null) {
			ok = true;
		}
		return ok;
	}

	@Override
	public boolean updateEcardActForDeleted(EcardAct ecardAct) {
		//
		return ecardActDao.update(ecardAct) > 0;
	}

	@Override
	public boolean updateEcardActForDisabled(EcardAct ecardAct) {
		// 业务逻辑
		if (ecardAct.getDisabled() == true) {
			ecardAct.setEndTime(new Date());
		}
		return ecardActDao.update(ecardAct) > 0;
	}

	@Override
	public boolean saveEcardActRule(EcardActRule ecardActRule) {
		if (ecardActRule != null) {
			ecardActRule.setTs(new Date());
			ecardActRule.setDesc(getDesc(ecardActRule));
			boolean ok = ecardActRuleDao.insert(ecardActRule) > 0;
			if (ok) {
				List<EcardActGift> giftList = ecardActRule.getRuleGiftItemList();
				if (giftList != null && giftList.size() > 0) {
					for (EcardActGift ecardActGift : giftList) {
						if (ecardActGift != null) {
							ecardActGift.setActRuleId(ecardActRule.getId());
							saveEcardActGift(ecardActGift);
						}
					}
				}
			}
			return ok;
		} else {
			return false;
		}

	}

	private String getDesc(EcardActRule ecardActRule) {
		StringBuffer desc = new StringBuffer();
		if (ecardActRule != null) {
			if (ecardActRule.getFirstTimeOnly() == true) {
				desc.append("首次购买");
			} else {
				desc.append("购买");
			}
			if (ecardActRule.getCondType() == 0) {
				desc.append("满" + ecardActRule.getCondValue() + "元，");
			} else {
				desc.append("满" + ecardActRule.getCondValue() + "张，");
			}
			desc.append("赠送：");
			List<EcardActGift> giftList = ecardActRule.getRuleGiftItemList();
			if (giftList != null && giftList.size() > 0) {
				for (EcardActGift ecardActGift : giftList) {
					desc.append(ecardActGift.getText() + "、");
				}
				desc.deleteCharAt(desc.lastIndexOf("、"));
			}
		}
		return desc.toString();
	}

	@Override
	public List<EcardActRule> getEcardActRulesByActId(Integer actId) {
		MapContext requestData = MapContext.newOne();
		requestData.put("actId", actId);
		List<EcardActRule> listRule = ecardActRuleDao.selectByFilter(requestData);
		if (listRule != null && listRule.size() > 0) {
			for (EcardActRule ecardActRule : listRule) {
				// 馈赠商品
				ecardActRule.setRuleGiftItemList(getEcardActGiftsByActRuleId(ecardActRule.getId()));
			}
		}
		return listRule;
	}

	@Override
	public boolean updateEcardActRule(EcardActRule ecardActRule) {
		if (ecardActRule != null) {
			ecardActRule.setDesc(getDesc(ecardActRule));
			// 获取客户端活动馈赠物品
			List<EcardActGift> listGift = ecardActRule.getRuleGiftItemList();
			// 获取数据库列表
			List<EcardActGift> dbListGift = getEcardActGiftsByActRuleId(ecardActRule.getId());
			if (listGift != null && listGift.size() > 0) {
				if (dbListGift != null && dbListGift.size() > 0) {
					for (int i = 0; i < dbListGift.size();) {
						final EcardActGift dbItem = dbListGift.get(i);
						// 查找对应的新数据索引
						int ruleIndex = CollectionUtil.searchIndex(listGift, new TargetJudger<EcardActGift>() {
							@Override
							public boolean isTarget(EcardActGift toBeChecked) {
								// 按唯一键判断
								return toBeChecked.getId().equals(dbItem.getId());
							}
						});
						// 处理更新
						if (ruleIndex != -1) {
							EcardActGift itemGift = listGift.get(ruleIndex);
							// 合并数据更新
							updateEcardActGift(itemGift);
							// 删除db和客户端
							dbListGift.remove(i);
							listGift.remove(ruleIndex);
						} else {
							i++;
						}
					}

					// dbItemList中剩余的都是需要删除的
					for (int i = 0; i < dbListGift.size(); i++) {
						EcardActGift dbItem = dbListGift.get(i);
						// 从数据库中删除...
						deleteEcardActGift(dbItem.getId());
					}

					// itemList中剩余的都是需要新增的
					for (int i = 0; i < listGift.size(); i++) {
						EcardActGift itemGift = listGift.get(i);
						// 添加到数据库中...
						itemGift.setActRuleId(ecardActRule.getId());
						saveEcardActGift(itemGift);
					}
				}
			}
		}
		//
		return ecardActRuleDao.update(ecardActRule) > 0;

	}

	@Override
	public boolean deleteEcardActRule(Integer ruleId) {
		boolean ok = ecardActRuleDao.deleteById(ruleId) > 0;
		if (ok) {
			ecardActGiftDao.deleteByActRuleId(ruleId);
		}
		return ok;
	}

	@Override
	public boolean saveEcardActGift(EcardActGift ecardActGift) {
		// TODO Auto-generated method stub
		return ecardActGiftDao.insert(ecardActGift) > 0;
	}

	@Override
	public boolean updateEcardActGift(EcardActGift ecardActGift) {
		// 业务逻辑
		return ecardActGiftDao.update(ecardActGift) > 0;
	}

	@Override
	public List<EcardActGift> getEcardActGiftsByActRuleId(Integer actRuleId) {
		MapContext requestDate = MapContext.newOne();
		requestDate.put("actRuleId", actRuleId);
		List<EcardActGift> listGift = ecardActGiftDao.selectByFilter(requestDate);
		if (listGift != null && listGift.size() > 0) {
			for (EcardActGift ecardActGift : listGift) {
				if (GiftType.coupon.name().equals(ecardActGift.getType())) {
					// 获取赠送id
					Long srcId = ecardActGift.getValue();
					// 判断规则中馈赠物品是否商品优惠券还是服务优惠券
					if (ecardActGift.getFlag() == 0) {
						Coupon coupon = couponDao.selectById(srcId.intValue());
						if (coupon != null) {
							ecardActGift.setCouponType(coupon.getType());
						}
					} else {
						SvcCoupon svcCoupon = svcCouponDao.selectById(srcId.intValue());
						if (svcCoupon != null) {
							ecardActGift.setCouponType(svcCoupon.getType());
						}
					}
				}
			}
		}
		return listGift;
	}

	@Override
	public boolean deleteEcardActGift(Long giftId) {
		return ecardActGiftDao.deleteById(giftId) > 0;
	}

	@Override
	public List<EcardActRule> getEcardActRulesByFilter(MapContext requestFilter) {
		return ecardActRuleDao.selectByFilter(requestFilter);
	}

	@Override
	public EcardActRule getEcardActRuleByFilter(MapContext requestFilter) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveEcardActGift(Integer userId, String code, Integer buyQuantity, BigDecimal buyAmount) {
		// TODO Auto-generated method stub
		MapContext mapContext = new MapContext();
		mapContext.put("code", code);
		List<EcardActRule> ruleList = getEcardActRulesByFilter(mapContext);
		if (ruleList != null) {
			EcardActRule actRule = null;
			Boolean falg = eCardOrderService.existFirstOrder(userId, code);
			for (EcardActRule ecardActRule : ruleList) {
				// 规则是不是首次购买
				boolean firstTimeOnly = ecardActRule.getFirstTimeOnly();
				// 判断规则条件0:金额 1：数量
				if (ecardActRule.getCondType() == 0) {
					// 获取规则金额
					BigDecimal condAmount = new BigDecimal(ecardActRule.getCondValue());
					// 判断购买金额是否满足
					if (buyAmount.compareTo(condAmount) >= 0) {
						if (firstTimeOnly) {
							// 判断购买的是不是首次
							if (falg) {
								actRule = ecardActRule;
								break;
							}
						} else {
							actRule = ecardActRule;
							break;
						}
					}
				} else {
					// 获取规则数量
					Integer quantity = ecardActRule.getCondValue();
					// 购买数量是不是满足
					if (buyQuantity >= quantity) {
						if (firstTimeOnly) {
							// 判断购买的是不是首次
							if (falg) {
								actRule = ecardActRule;
								break;
							}
						} else {
							actRule = ecardActRule;
							break;
						}
					}
				}
			}
			// 判断是否匹配活动规则上
			if (actRule != null) {
				List<EcardActGift> giftList = getEcardActGiftsByActRuleId(actRule.getId());
				if (giftList != null && giftList.size() > 0) {
					for (EcardActGift ecardActGift : giftList) {
						// 获取赠送id
						Long srcId = ecardActGift.getValue();
						// 判断规则中馈赠物品类型
						if (GiftType.coupon.name().equals(ecardActGift.getType())) {
							// 判断规则中馈赠物品是否商品优惠券还是服务优惠券
							if (ecardActGift.getFlag() == 0) {
								Coupon coupon = couponDao.selectById(srcId.intValue());
								if (coupon != null) {
									createUserCoupon(coupon, userId);
								}
							} else {
								SvcCoupon svcCoupon = svcCouponDao.selectById(srcId.intValue());
								if (svcCoupon != null) {
									createUserSvcCoupon(svcCoupon, userId);
								}
							}
						}
					}
				}
			}
		}
	}

	@Override
	public PrmtTag getPrmtTagByCode(String code) {
		PrmtTag prmtTag = prmtTagDao.selectByCode(code);
		if (prmtTag != null) {
			List<PrmtTagGoods> prmtTagGoodsList = prmtTagGoodsDao.selectByTagId(prmtTag.getId());
			for (PrmtTagGoods prmtTagGoods : prmtTagGoodsList) {
				// 设置货品信息
				Long productId = prmtTagGoods.getProductId();
				Product product = goodsService.getProductById(productId);
				prmtTagGoods.setProduct(product);
			}
			prmtTag.setPrmtTagGoods(prmtTagGoodsList);
		}
		return prmtTag;
	}
}
