package priv.starfish.mall.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.model.PayStateType;
import priv.starfish.common.pay.dict.PayWayType;
import priv.starfish.common.repo.FileRepository;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.DateUtil;
import priv.starfish.common.util.EnumUtil;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.car.entity.UserCar;
import priv.starfish.mall.car.entity.UserCarSvcRec;
import priv.starfish.mall.cart.dto.MiscAmountInfo;
import priv.starfish.mall.cart.entity.SaleCart;
import priv.starfish.mall.cart.entity.SaleCartGoods;
import priv.starfish.mall.cart.entity.SaleCartSvc;
import priv.starfish.mall.dao.comn.UserDao;
import priv.starfish.mall.comn.dict.Period;
import priv.starfish.mall.comn.dict.UserType;
import priv.starfish.mall.comn.entity.User;
import priv.starfish.mall.dao.ecard.ECardTransactRecDao;
import priv.starfish.mall.dao.ecard.UserECardDao;
import priv.starfish.mall.ecard.entity.ECardTransactRec;
import priv.starfish.mall.ecard.entity.UserECard;
import priv.starfish.mall.dao.goods.GoodsAlbumImgDao;
import priv.starfish.mall.dao.goods.ProductAlbumImgDao;
import priv.starfish.mall.goods.entity.Goods;
import priv.starfish.mall.goods.entity.GoodsAlbumImg;
import priv.starfish.mall.goods.entity.Product;
import priv.starfish.mall.goods.entity.ProductAlbumImg;
import priv.starfish.mall.dao.market.CouponDao;
import priv.starfish.mall.dao.market.UserCouponDao;
import priv.starfish.mall.dao.market.UserSvcCouponDao;
import priv.starfish.mall.market.dict.DiscWay;
import priv.starfish.mall.market.entity.UserCoupon;
import priv.starfish.mall.market.entity.UserSvcCoupon;
import priv.starfish.mall.member.entity.Member;
import priv.starfish.mall.dao.order.SaleOrderDao;
import priv.starfish.mall.dao.order.SaleOrderGoodsDao;
import priv.starfish.mall.dao.order.SaleOrderInnerAmountDao;
import priv.starfish.mall.dao.order.SaleOrderRecordDao;
import priv.starfish.mall.dao.order.SaleOrderSvcDao;
import priv.starfish.mall.dao.order.SaleOrderWorkDao;
import priv.starfish.mall.dao.order.UserSvcPackTicketDao;
import priv.starfish.mall.order.dict.CreatorFlag;
import priv.starfish.mall.order.dict.OrderAction;
import priv.starfish.mall.order.dict.OrderInnerAmountFlag;
import priv.starfish.mall.order.dict.OrderType;
import priv.starfish.mall.order.dto.OrderStateTypeCountDto;
import priv.starfish.mall.order.dto.SaleOrderInfo;
import priv.starfish.mall.order.entity.SaleOrder;
import priv.starfish.mall.order.entity.SaleOrderGoods;
import priv.starfish.mall.order.entity.SaleOrderInnerAmount;
import priv.starfish.mall.order.entity.SaleOrderRecord;
import priv.starfish.mall.order.entity.SaleOrderSvc;
import priv.starfish.mall.order.entity.SaleOrderWork;
import priv.starfish.mall.order.entity.UserSvcPackTicket;
import priv.starfish.mall.order.po.SaleOrderActionPo;
import priv.starfish.mall.order.po.SaleOrderPo;
import priv.starfish.mall.service.CarService;
import priv.starfish.mall.service.CarSvcService;
import priv.starfish.mall.service.DistShopService;
import priv.starfish.mall.service.ECardService;
import priv.starfish.mall.service.GoodsService;
import priv.starfish.mall.service.MemberService;
import priv.starfish.mall.service.SaleOrderService;
import priv.starfish.mall.service.SettleService;
import priv.starfish.mall.service.ShopService;
import priv.starfish.mall.service.UserService;
import priv.starfish.mall.service.misc.CalcSaleHelper;
import priv.starfish.mall.service.util.CodeUtil;
import priv.starfish.mall.service.util.NoUtil;
import priv.starfish.mall.dao.settle.DistSettleRecDao;
import priv.starfish.mall.dao.settle.PayRefundRecDao;
import priv.starfish.mall.settle.entity.DistSettleRec;
import priv.starfish.mall.settle.entity.PayRefundRec;
import priv.starfish.mall.dao.shop.ShopDao;
import priv.starfish.mall.shop.entity.DistShop;
import priv.starfish.mall.shop.entity.DistShopSvc;
import priv.starfish.mall.shop.entity.Shop;
import priv.starfish.mall.dao.svcx.SvcPackItemDao;
import priv.starfish.mall.dao.svcx.SvcxDao;
import priv.starfish.mall.svcx.entity.SvcPack;
import priv.starfish.mall.svcx.entity.SvcPackItem;
import priv.starfish.mall.svcx.entity.Svcx;
import priv.starfish.mall.svcx.entity.SvcxRankDisc;

@Service("saleOrderService")
public class SaleOrderServiceImpl extends BaseServiceImpl implements SaleOrderService {
	@Resource
	ShopService shopService;

	@Resource
	SaleOrderDao saleOrderDao;

	@Resource
	SaleOrderGoodsDao saleOrderGoodsDao;

	@Resource
	UserService userService;

	@Resource
	GoodsService goodsService;

	@Resource
	SaleOrderSvcDao saleOrderSvcDao;

	@Resource
	UserDao userDao;

	@Resource
	ShopDao shopDao;

	@Resource
	SaleOrderRecordDao saleOrderRecordDao;

	@Resource
	CarSvcService carSvcService;

	@Resource
	ProductAlbumImgDao productAlbumImgDao;

	@Resource
	FileRepository fileRepository;

	@Resource
	GoodsAlbumImgDao goodsAlbumImgDao;

	@Resource
	UserCouponDao userCouponDao;

	@Resource
	UserSvcCouponDao userSvcCouponDao;

	@Resource
	CouponDao couponDao;

	@Resource
	UserECardDao userECardDao;

	@Resource
	SaleOrderInnerAmountDao saleOrderInnerAmountDao;

	@Resource
	SvcxDao svcxDao;

	@Resource
	SettleService settleService;

	@Resource
	ECardTransactRecDao eCardTransactRecDao;

	@Resource
	PayRefundRecDao payRefundRecDao;

	@Resource
	SaleOrderWorkDao saleOrderWorkDao;

	@Resource
	UserSvcPackTicketDao userSvcPackTicketDao;

	@Resource
	private ECardService eCardService;

	@Resource
	private MemberService memberService;

	@Resource
	private SvcPackItemDao svcPackItemDao;

	@Resource
	private CarService carService;

	@Resource
	private DistShopService distShopService;

	@Resource
	private DistSettleRecDao distSettleRecDao;

	// ---------------------------------------------------------------------------------------
	@Override
	public boolean saveSaleOrderRecord(SaleOrderRecord saleOrderRecord) {
		return saleOrderRecordDao.insert(saleOrderRecord) > 0;
	}

	@Override
	public boolean saveSaleOrderRecord(String action, UserContext userContext, PayRefundRec payRefundRec) {

		SaleOrderRecord saleOrderRecord = new SaleOrderRecord();
		PayRefundRec prr = payRefundRecDao.selectById(payRefundRec.getId());

		saleOrderRecord.setOrderId(prr.getOrderId());
		saleOrderRecord.setAction(action);
		saleOrderRecord.setActorId(userContext.getUserId());
		// TODO 角色怎么获得
		// userContext.getRoleIds();

		saleOrderRecord.setActRole("客服");
		saleOrderRecord.setActorName(userContext.getUserName());

		return saleOrderRecordDao.insert(saleOrderRecord) > 0;
	}

	@Override
	public List<SaleOrderWork> getSaleOrderWorksById(Long orderId) {
		return saleOrderWorkDao.selectByOrderId(orderId);
	}

	@Override
	public boolean saveSaleOrderWorks(Long orderId, List<SaleOrderWork> saleOrderWorks) {
		saleOrderWorkDao.deleteByOrderId(orderId);
		//
		boolean success = false;
		if (saleOrderWorks != null) {
			Date now = new Date();
			for (int i = 0; i < saleOrderWorks.size(); i++) {
				SaleOrderWork saleOrderWork = saleOrderWorks.get(i);
				saleOrderWork.setId(null);
				saleOrderWork.setOrderId(orderId);
				saleOrderWork.setTs(now);
				//
				success = saleOrderWorkDao.insert(saleOrderWork) > 0 && success;
			}
		}
		return success;
	}

	// -------------------------------------------------------------------------------------
	@Override
	public PaginatedList<SaleOrder> getSaleOrdersByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<SaleOrder> saleOrders = saleOrderDao.selectByFilter(paginatedFilter);
		for (SaleOrder saleOrder : saleOrders.getRows()) {
			// 查询订单关联服务
			List<SaleOrderSvc> saleOrderSvcs = saleOrderSvcDao.selectByOrderId(saleOrder.getId());
			// if (saleOrderSvcs != null) {
			// for (SaleOrderSvc saleOrderSvc : saleOrderSvcs) {
			// // 查询服务关联商品
			// List<SaleOrderGoods> saleOrderGoods = saleOrderGoodsDao.selectByOrderSvcId(saleOrderSvc.getId());
			// saleOrderSvc.setSaleOrderGoods(saleOrderGoods);
			// }
			// }
			saleOrder.setSaleOrderSvcs(saleOrderSvcs);
			List<SaleOrderGoods> saleOrderGoods = saleOrderGoodsDao.selectByOrderId(saleOrder.getId());
			saleOrder.setSaleOrderGoods(saleOrderGoods);
			// 获取门店信息
			Shop shop = shopDao.selectById(saleOrder.getShopId());
			saleOrder.setShop(shop);
			// 查询订单操作记录
			List<SaleOrderRecord> saleOrderRecords = saleOrderRecordDao.selectByOrderId(saleOrder.getId());
			saleOrder.setSaleOrderRecords(saleOrderRecords);
			// 查询订单相关工作
			List<SaleOrderWork> saleOrderWorks = getSaleOrderWorksById(saleOrder.getId());
			saleOrder.setSaleOrderWorks(saleOrderWorks);
		}
		return saleOrders;
	}

	@Override
	public List<SaleOrder> getSaleOrdersByFilterNormal(MapContext filter) {
		List<SaleOrder> saleOrders = saleOrderDao.selectByFilterNormal(filter);
		if (saleOrders == null) {
			return null;
		}

		for (SaleOrder saleOrder : saleOrders) {
			// 查询订单关联服务
			List<SaleOrderSvc> saleOrderSvcs = saleOrderSvcDao.selectByOrderId(saleOrder.getId());
			// if (saleOrderSvcs != null) {
			// for (SaleOrderSvc saleOrderSvc : saleOrderSvcs) {
			// // 查询服务关联商品
			// List<SaleOrderGoods> saleOrderGoods = saleOrderGoodsDao.selectByOrderSvcId(saleOrderSvc.getId());
			// saleOrderSvc.setSaleOrderGoods(saleOrderGoods);
			// }
			// }
			saleOrder.setSaleOrderSvcs(saleOrderSvcs);

			List<SaleOrderGoods> saleOrderGoods = saleOrderGoodsDao.selectByOrderId(saleOrder.getId());
			saleOrder.setSaleOrderGoods(saleOrderGoods);
			// 查询订单操作记录
			List<SaleOrderRecord> saleOrderRecords = saleOrderRecordDao.selectByOrderId(saleOrder.getId());
			saleOrder.setSaleOrderRecords(saleOrderRecords);
			// 查询订单相关工作
			List<SaleOrderWork> saleOrderWorks = getSaleOrderWorksById(saleOrder.getId());
			saleOrder.setSaleOrderWorks(saleOrderWorks);
		}
		return saleOrders;
	}

	@Override
	public PaginatedList<SaleOrder> getSaleOrdersByUserId(PaginatedFilter paginatedFilter) {
		MapContext requestData = paginatedFilter.getFilterItems();
		String periodName = requestData.getTypedValue("periodName", String.class);
		Integer periodValue = requestData.getTypedValue("periodValue", Integer.class);
		if (periodName != null && periodValue != 0) {
			Object object = getLatestSaleOrders(periodName, periodValue);
			requestData.put("orderDate", object);
		}
		PaginatedList<SaleOrder> paginatedList = saleOrderDao.selectByUserId(paginatedFilter);
		List<SaleOrder> listSaleOrder = paginatedList.getRows();
		if (null != listSaleOrder) {
			for (SaleOrder saleOrder : listSaleOrder) {
				// 查询订单服务
				List<SaleOrderSvc> orderSvcList = this.getSaleOrderSvcsByOrderId(saleOrder.getId());
				saleOrder.setSaleOrderSvcs(orderSvcList);
				// 查看商品
				List<SaleOrderGoods> saleOrderGoodsList = this.getSaleOrderGoodsByOrderId(saleOrder.getId());
				saleOrder.setSaleOrderGoods(saleOrderGoodsList);
			}
		}
		return paginatedList;
	}

	@Override
	public List<SaleOrderSvc> getSaleOrderSvcsByOrderId(Long orderId) {
		List<SaleOrderSvc> saleOrderSvcList = saleOrderSvcDao.selectByOrderId(orderId);
		if (saleOrderSvcList != null && saleOrderSvcList.size() > 0) {
			for (SaleOrderSvc saleOrderSvc : saleOrderSvcList) {
				Svcx svcx = carSvcService.getCarSvc(saleOrderSvc.getSvcId());
				if (svcx != null) {
					saleOrderSvc.setSvcxAlbumImg(svcx.getFileBrowseUrl());
					saleOrderSvc.setSvcxAlbumImgApp(svcx.getFileBrowseUrlApp());
					saleOrderSvc.setSvcDesc(svcx.getDesc());
				}
			}
		}
		return saleOrderSvcList;
	}

	public String getProductAlbumImg(Long productId) {
		// 获取图片
		List<ProductAlbumImg> productAlbumImgs = productAlbumImgDao.selectByProductId(productId);
		if (productAlbumImgs != null && productAlbumImgs.size() > 0) {
			ProductAlbumImg productImg = productAlbumImgs.get(0);
			if (productImg != null) {
				GoodsAlbumImg goodsImg = goodsAlbumImgDao.selectById(productImg.getImageId());
				if (goodsImg != null) {
					String browseUrl = fileRepository.getFileBrowseUrl(goodsImg.getImageUsage(), goodsImg.getImagePath());
					if (StrUtil.isNullOrBlank(browseUrl)) {
						browseUrl = fileRepository.getFileRepoConfig().getNoImageUrl();
					}
					productImg.setFileBrowseUrl(browseUrl);
				} else {
					productAlbumImgs.remove(0);
				}
			}
			return productImg.getFileBrowseUrl();
		} else {
			return null;
		}
	}

	@Override
	public OrderStateTypeCountDto getSaleOrderCount(MapContext requestData) {
		return getOrderStateTypeCount(requestData);
	}

	@Override
	public OrderStateTypeCountDto getSaleOrderCountByUserIdAndStateType(MapContext requestData) {
		String periodName = requestData.getTypedValue("periodName", String.class);
		Integer periodValue = requestData.getTypedValue("periodValue", Integer.class);
		if (periodName != null && periodValue != 0) {
			Object object = getLatestSaleOrders(periodName, periodValue);
			requestData.put("orderDate", object);
		}
		return getOrderStateTypeCount(requestData);
	}

	private Integer getSaleOrderCountByUserIdAndStateType(String stateType, MapContext requestData) {
		requestData.put("orderState", stateType);
		return saleOrderDao.selectCount(requestData);
	}

	private OrderStateTypeCountDto getOrderStateTypeCount(MapContext requestData) {
		OrderStateTypeCountDto countDto = new OrderStateTypeCountDto();
		countDto.setTotalCount(getSaleOrderCountByUserIdAndStateType("all", requestData));
		countDto.setUnhandledCount(getSaleOrderCountByUserIdAndStateType("unhandled", requestData));
		countDto.setProcessingCount(getSaleOrderCountByUserIdAndStateType("processing", requestData));
		countDto.setFinishedCount(getSaleOrderCountByUserIdAndStateType("finished", requestData));
		countDto.setCancelledCount(getSaleOrderCountByUserIdAndStateType("cancelled", requestData));
		return countDto;
	}

	public Object getLatestSaleOrders(String periodName, Integer periodValue) {
		Period period = EnumUtil.valueOf(Period.class, periodName);
		if (period == null) {
			period = Period.month;
		}
		if (periodValue == null || periodValue <= 0) {
			periodValue = 1;
		}
		//
		Date targetDate = null;
		int year = -1;
		boolean forYear = false;
		if (period == Period.day) {
			targetDate = DateUtil.addDays(-periodValue);
		} else if (period == Period.week) {
			targetDate = DateUtil.addWeeks(-periodValue);
		} else if (period == Period.month) {
			targetDate = DateUtil.addMonths(-periodValue);
		} else if (period == Period.year) {
			year = periodValue;
			forYear = true;
		}
		if (forYear) {
			return year;
		} else {
			SimpleDateFormat sim = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			return sim.format(targetDate);
		}
	}

	public static void main(String[] args) {
		System.out.println(DateUtil.addDays(-1));
	}

	@Override
	public SaleOrder getSaleOrderById(Long id) {
		SaleOrder saleOrder = saleOrderDao.selectById(id);
		if (saleOrder != null) {
			// 订单服务
			saleOrder.setSaleOrderSvcs(saleOrderSvcDao.selectByOrderId(saleOrder.getId()));
		}
		return saleOrder;
	}

	@Override
	public SaleOrder getSaleOrderDetailById(Long id) {
		SaleOrder saleOrder = saleOrderDao.selectById(id);
		if (saleOrder != null) {
			// 订单服务
			saleOrder.setSaleOrderSvcs(saleOrderSvcDao.selectByOrderId(saleOrder.getId()));
			// 订单商品
			saleOrder.setSaleOrderGoods(saleOrderGoodsDao.selectByOrderId(saleOrder.getId()));
			// 门店信息
			Shop shop = shopDao.selectById(saleOrder.getShopId());
			saleOrder.setShop(shop);
			// 订单操作记录
			List<SaleOrderRecord> saleOrderRecords = saleOrderRecordDao.selectByOrderId(saleOrder.getId());
			saleOrder.setSaleOrderRecords(saleOrderRecords);
		}
		return saleOrder;
	}

	@Override
	public SaleOrder getSaleOrderDetailByOrderId(Long orderId) {
		// 订单信息和订单记录信息
		SaleOrder saleOrder = saleOrderDao.selectById(orderId);
		if (saleOrder != null) {
			// 订单记录
			List<SaleOrderRecord> listRecord = saleOrderRecordDao.selectByOrderId(saleOrder.getId());
			saleOrder.setSaleOrderRecords(listRecord);
			// 订单服务与商品
			List<SaleOrderSvc> listOrderSvc = this.getSaleOrderSvcsByOrderId(saleOrder.getId());
			saleOrder.setSaleOrderSvcs(listOrderSvc);
			// 查询商品
			List<SaleOrderGoods> saleOrderGoodsList = this.getSaleOrderGoodsByOrderId(saleOrder.getId());
			saleOrder.setSaleOrderGoods(saleOrderGoodsList);
			// 查询分销店
			if (saleOrder.getDistributorId() != null) {
				DistShop distShop = distShopService.getById(saleOrder.getDistributorId());
				if (distShop != null) {
					saleOrder.setDistShopAddress(distShop.getAddress());
					saleOrder.setDistShopPhoneNo(distShop.getPhoneNo());
				}
			}
		}
		return saleOrder;
	}

	private String getUserName(User user) {
		if (user.getRealName() != null && StrUtil.hasText(user.getRealName())) {
			return user.getRealName();
		} else if (user.getNickName() != null && StrUtil.hasText(user.getNickName())) {
			return user.getNickName();
		} else {
			return user.getPhoneNo();
		}
	}

	@Override
	public boolean saveSaleOrderAsProxy(SaleOrderPo saleOrderPo) {
		String userPhone = saleOrderPo.getUserPhone();
		if (userPhone == null || !StrUtil.hasText(userPhone)) {
			logger.warn(String.format("userPhone is null! Class : %s Method : %s", getClass().getName(), "saveAgentSaleOrder()"));
			return false;
		}
		Integer carId = saleOrderPo.getCarId();
		if (carId == null || carId.intValue() <= 0) {
			logger.warn(String.format("carId is null! Class : %s Method : %s", getClass().getName(), "saveAgentSaleOrder()"));
			return false;
		}
		// 门店相关
		Integer shopId = saleOrderPo.getShopId();
		if (shopId == null || shopId.intValue() <= 0) {
			logger.warn(String.format("shopId is null! Class : %s Method : %s", getClass().getName(), "saveSaleOrderAsProxy()"));
			return false;
		}
		Integer actorId = saleOrderPo.getActorId();
		if (actorId == null) {
			logger.warn(String.format("actorId is null! Class : %s Method : %s", getClass().getName(), "saveAgentSaleOrder()"));
			return false;
		}

		// 关联服务ID列表
		List<Integer> svcIds = saleOrderPo.getSvcIds();
		if (svcIds == null || svcIds.size() == 0) {
			logger.warn("svcIds is null or empty!");
			return false;
		}

		SaleOrder saleOrder = new SaleOrder();
		User user = userDao.selectByPhoneNo(userPhone);
		if (user == null) {
			logger.warn(String.format("user is null! Class : %s Method : %s", getClass().getName(), "saveAgentSaleOrder()"));
			return false;
		}

		// 用户相关
		saleOrder.setUserId(user.getId());
		saleOrder.setUserName(user.getNickName());
		saleOrder.setPhoneNo(user.getPhoneNo());
		saleOrder.setLinkMan(user.getNickName());
		saleOrder.setTelNo(user.getPhoneNo());
		saleOrder.setEmail(user.getEmail());
		saleOrder.setMemo(saleOrderPo.getMemo());
		// 车辆相关
		UserCar userCar = carService.getUserCarById(saleOrderPo.getCarId());
		saleOrder.setCarId(userCar.getId());
		saleOrder.setCarName(userCar.getName());
		saleOrder.setCarModel(userCar.getModelName());
		// 生成订单编号
		String orderNo = NoUtil.newSaleOrderNo();
		saleOrder.setNo(orderNo);
		// 生成服务完成确认编号
		String doneCode = CodeUtil.newDoneCode();
		saleOrder.setDoneCode(doneCode);
		saleOrder.setCreatorId(actorId);
		User actor = userDao.selectById(actorId);
		saleOrder.setCreatorName(getUserName(actor));
		// 如果是合作店
		if (saleOrderPo.getDistFlag() != null && saleOrderPo.getDistFlag()) {
			saleOrder.setDistFlag(saleOrderPo.getDistFlag());
			DistShop distShop = distShopService.getById(shopId);
			saleOrder.setDistributorId(actor.getId());
			saleOrder.setDistributorName(getUserName(actor));
			saleOrder.setDistShopName(distShop.getName());
			saleOrder.setCreatorFlag(CreatorFlag.wxshop.getValue());
			shopId = distShop.getOwnerShopId();
		} else {
			saleOrder.setDistFlag(false);
			// 订单提交方信息
			saleOrder.setCreatorFlag(CreatorFlag.shop.getValue());
		}

		Shop shop = shopDao.selectById(shopId);
		saleOrder.setShopId(shopId);
		saleOrder.setShopName(shop.getName());
		saleOrder.setMerchantId(shop.getMerchantId());
		saleOrder.setMerchantName(shop.getMerchantName());
		saleOrder.setRegionId(shop.getRegionId());
		saleOrder.setRegionName(shop.getRegionName());
		saleOrder.setStreet(shop.getStreet());
		saleOrder.setAddress(shop.getRegionName()+shop.getStreet());
		saleOrder.setLat(shop.getLat());
		saleOrder.setLng(shop.getLng());
		
		// 状态相关
		saleOrder.setPayWay(saleOrderPo.getPayWay());
		saleOrder.setPayState("unpaid");
		saleOrder.setDistState("unfilled");
		saleOrder.setCancelled(false);
		saleOrder.setClosed(false);
		saleOrder.setPickupFlag(true);
		saleOrder.setPayConfirmed(false);
		saleOrder.setFinished(false);
		saleOrder.setStarFlag(false);
		saleOrder.setPlanModTimes(0);
		saleOrder.setSvcOnly(true);
		saleOrder.setSvcTimes(0);

		// 日期相关
		saleOrder.setPlanTime(saleOrderPo.getPlanTime());
		Date now = new Date();
		saleOrder.setCreateTime(now);
		saleOrder.setDeleted(false);

		// 金额计算
		BigDecimal amount = BigDecimal.valueOf(0);
		// 分销总金额
		BigDecimal distProfit = BigDecimal.valueOf(0);
		List<SaleOrderSvc> saleOrderSvcList = new ArrayList<SaleOrderSvc>();
		for (Integer svcId : svcIds) {
			Svcx svc = svcxDao.selectById(svcId);
			if (svc == null) {
				logger.warn(String.format("svc is null! svcId:%d", svcId));
				return false;
			}
			amount = amount.add(svc.getSalePrice());

			SaleOrderSvc orderSvc = new SaleOrderSvc();
			orderSvc.setSvcId(svcId);
			orderSvc.setSalePrice(svc.getSalePrice());
			// 设置代理下单利润
			if (saleOrder.getDistFlag()) {
				DistShopSvc distShopSvc = distShopService.getDistShopSvcByDistShopIdAndSvcId(saleOrder.getDistributorId(), svc.getId());
				if (distShopSvc != null && distShopSvc.getDistProfitX() != null) {
					orderSvc.setDistProfit(distShopSvc.getDistProfitX());
					distProfit = distProfit.add(distShopSvc.getDistProfitX());
				}
			}
			orderSvc.setDiscAmount(BigDecimal.valueOf(0));
			orderSvc.setSaleAmount(svc.getSalePrice());
			orderSvc.setAmount(svc.getSalePrice());
			orderSvc.setOrderId(saleOrder.getId());
			orderSvc.setSvcName(svc.getName());
			saleOrderSvcList.add(orderSvc);
		}
		saleOrder.setSaleAmount(amount);
		saleOrder.setAmountOuter(amount);
		saleOrder.setDiscAmount(BigDecimal.valueOf(0));
		saleOrder.setAmount(amount);
		saleOrder.setSettleAmount(amount);
		saleOrder.setDistProfit(distProfit);

		// 插入销售订单记录
		boolean isSuccess = saleOrderDao.insert(saleOrder) > 0;
		if (!isSuccess) {
			return false;
		}
		saleOrderPo.setId(saleOrder.getId());
		saleOrderPo.setNo(orderNo);

		// 插入订单服务购买记录
		for (SaleOrderSvc saleOrderSvc : saleOrderSvcList) {
			saleOrderSvc.setOrderId(saleOrder.getId());
			saleOrderSvcDao.insert(saleOrderSvc);
		}

		// 插入订单相关操作记录
		SaleOrderRecord record = new SaleOrderRecord();
		record.setAction(OrderAction.submit.name());
		record.setActorId(actor.getId());
		record.setActorName(getUserName(actor));
		if (saleOrder.getDistFlag()) {
			record.setActRole(UserType.wxshop.getText());
		} else {
			record.setActRole(UserType.shop.getText());
		}
		record.setExtraInfo(saleOrder.getLeaveMsg());
		record.setOrderId(saleOrder.getId());
		record.setTs(now);
		// 插入记录
		saleOrderRecordDao.insert(record);
		return true;
	}

	@Override
	public boolean saveSaleOrder(Integer userId, SaleOrderInfo saleOrderInfo) {
		SaleOrderPo saleOrderPo = saleOrderInfo.getSaleOrderPo();
		Integer carId = saleOrderPo.getCarId();
		if (carId == null || carId.intValue() <= 0) {
			logger.warn(String.format("carId is null! Class : %s Method : %s", getClass().getName(), "saveSaleOrder()"));
			return false;
		}
		// 门店相关
		Integer shopId = saleOrderPo.getShopId();
		if (shopId == null || shopId.intValue() <= 0) {
			logger.warn(String.format("shopId is null! Class : %s Method : %s", getClass().getName(), "saveSaleOrder()"));
			return false;
		}
		List<SaleCartSvc> saleCartSvcList = saleOrderInfo.getSaleCartInfo().getSaleCartSvcList();
		List<SaleCartGoods> saleCartGoodsList = saleOrderInfo.getSaleCartInfo().getSaleCartGoods();
		Date createTime = new Date();
		SaleOrder saleOrder = new SaleOrder();
		saleOrder.setSvcPackId(saleOrderInfo.getSaleOrderPo().getPackId());

		SvcPack svcPack = null;
		if (saleOrder.getSvcPackId() != null) {
			svcPack = carSvcService.getSvcPackById(saleOrder.getSvcPackId());
			if (svcPack == null) {
				logger.error("不存在此套餐：packId:" + saleOrderInfo.getSaleOrderPo().getPackId());
				return false;
			}
			// 是否下架
			if (svcPack.getDisabled()) {
				logger.error("“" + svcPack.getName() + "”套餐已下架：packId:" + svcPack.getId());
				return false;
			}
			// 验证时间是否在有效范围
			if (svcPack.getStartTime().getTime() > createTime.getTime()) {
				logger.error("“" + svcPack.getName() + "”套餐还未上架：packId:" + svcPack.getId());
				return false;
			}
			if (svcPack.getEndTime().getTime() < createTime.getTime()) {
				logger.error("“" + svcPack.getName() + "”套餐已结束,已下架：packId:" + svcPack.getId());
				return false;
			}
			saleOrder.setSvcPackName(svcPack.getName());
		} else {
			int svcListSize = 0;
			if (saleCartSvcList != null) {
				svcListSize = saleCartSvcList.size();
			}
			int goodsListSize = 0;
			if (saleCartGoodsList != null) {
				goodsListSize = saleCartGoodsList.size();
			}
			int totalSize = svcListSize + goodsListSize;
			if (totalSize == 0) {
				logger.error("没有服务和商品，不能下单");
				return false;
			}
		}

		// 生成订单编号
		String orderNo = NoUtil.newSaleOrderNo();
		saleOrder.setNo(orderNo);
		// 生成服务完成确认编号
		String doneCode = CodeUtil.newDoneCode();
		saleOrder.setDoneCode(doneCode);
		// 联系方式
		User user = userService.getUserById(userId);
		saleOrder.setUserId(user.getId());
		saleOrder.setUserName(user.getNickName());
		saleOrder.setEmail(user.getEmail());
		saleOrder.setLinkMan(saleOrderPo.getLinkMan());
		saleOrder.setPhoneNo(saleOrderPo.getLinkNo());
		saleOrder.setTelNo(user.getPhoneNo());// 备用电话
		saleOrder.setCreatorId(userId);
		saleOrder.setCreatorName(getUserName(user));
		saleOrder.setCreatorFlag(CreatorFlag.user.getValue());// 默认用户自己
		UserCar userCar = null;
		// 车辆相关
		userCar = carService.getUserCarById(carId);
		saleOrder.setCarId(userCar.getId());
		saleOrder.setCarName(userCar.getName());
		saleOrder.setCarModel(userCar.getModelName());

		// 门店相关
		Shop shop = shopService.getShopById(shopId);
		saleOrder.setShopId(shop.getId());
		saleOrder.setShopName(shop.getName());
		saleOrder.setMerchantId(shop.getMerchantId());
		saleOrder.setMerchantName(shop.getMerchantName());
		saleOrder.setRegionId(shop.getRegionId());
		saleOrder.setRegionName(shop.getRegionName());
		saleOrder.setStreet(shop.getStreet());
		saleOrder.setAddress(shop.getRegionName()+shop.getStreet());
		saleOrder.setLat(shop.getLat());
		saleOrder.setLng(shop.getLng());
		// 代理商
		saleOrder.setAgentId(null);
		saleOrder.setAgentName(null);
		// 分销商
		saleOrder.setDistributorId(null);
		saleOrder.setDistributorName(null);
		saleOrder.setDistShopName(null);

		// 其他默认值设置
		saleOrder.setDeviceType(null);// 设备类型
		saleOrder.setPickupFlag(true);// 默认店铺自提
		saleOrder.setPlanTime(saleOrderPo.getPlanTime());
		saleOrder.setPlanModTimes(0);
		saleOrder.setLeaveMsg(saleOrderPo.getLeaveMsg());
		saleOrder.setPayState("unpaid");// 支付状态
		saleOrder.setDistState("unfilled");// 物流状态
		saleOrder.setCancelled(false);
		saleOrder.setFinished(false);
		saleOrder.setFinishTime(null);
		saleOrder.setDeleted(false);
		saleOrder.setMemo(null);
		saleOrder.setPayConfirmed(false);
		saleOrder.setPayProofNo(null);// 支付证明编号
		saleOrder.setClosed(false);
		saleOrder.setCloseTime(null);
		saleOrder.setDistFlag(false);

		saleOrder.setPayWay(saleOrderPo.getPayWay());// 支付渠道
		if (!StrUtil.hasText(saleOrderPo.getPayWay())) {
			saleOrder.setPayWay(PayWayType.alipay.name());
		}
		saleOrder.setDeleted(false);
		saleOrder.setStarFlag(false);
		saleOrder.setCreateTime(createTime);
		saleOrder.setChangeTime(null);
		saleOrder.setIndexTime(null);

		// 结算
		saleOrder.setSettleState(null);// 结算状态
		saleOrder.setSettleRecId(null);// 结算记录id
		saleOrder.setSettleAmount(null);// 结算金额

		saleOrder.setSvcOnly(false);
		// svcOnly 冗余字段，是否仅含服务（纯服务，只有这样的订单可以选择合作店，在提交订单时设置此标记）
		if (svcPack == null && (saleCartGoodsList == null || saleCartGoodsList.size() == 0) && saleCartSvcList != null && saleCartSvcList.size() > 0) {
			saleOrder.setSvcOnly(true);
		}
		saleOrder.setSvcTimes(0);
		/*********************************** 订单服务价格同步 ******************************************/
		boolean isHasCoupon = false;// 是否有优惠券
		if (svcPack == null) {
			if (saleCartSvcList != null && saleCartSvcList.size() > 0) {
				for (SaleCartSvc saleCartSvc : saleCartSvcList) {
					if (saleCartSvc.getUserCoupon() != null) {
						// 得到优惠券
						UserSvcCoupon userCoupon = saleCartSvc.getUserCoupon();
						if (userCoupon != null && userCoupon.getId() != null) {
							userCoupon = userSvcCouponDao.selectByIdAndUserId(userCoupon.getId(), userId);
							if (userCoupon != null) {
								if (saleCartSvc.getSvcId() != userCoupon.getSvcId()) {// 不匹配
									logger.error("服务对应的优惠券不匹配:" + saleCartSvc.getSvcId() + "<>" + userCoupon.getSvcId());
									return false;
								}
								isHasCoupon = true;
							}
						}
						saleCartSvc.setUserCoupon(userCoupon);
					}
					Svcx carSvc = carSvcService.getCarSvc(saleCartSvc.getSvcId());
					if (saleCartSvc != null && carSvc != null) {
						saleCartSvc.setSalePrice(carSvc.getSalePrice());
						saleCartSvc.setCarSvcName(carSvc.getName());
					}
				}
			}

			if (saleCartGoodsList != null && saleCartGoodsList.size() > 0) {
				for (SaleCartGoods saleCartGoods : saleCartGoodsList) {
					if (saleCartGoods.getUserCoupon() != null) {
						/* 得到优惠券 */
						UserCoupon userCoupon = saleCartGoods.getUserCoupon();
						if (userCoupon != null && userCoupon.getId() != null) {
							userCoupon = userCouponDao.selectByIdAndUserId(userCoupon.getId(), userId);
							if (userCoupon != null) {
								if (saleCartGoods.getProductId() != userCoupon.getProductId()) {// 不匹配
									logger.error("商品对应的优惠券不匹配:" + saleCartGoods.getProductId() + "<>" + userCoupon.getProductId());
									return false;
								}
								isHasCoupon = true;
							}
						}
						saleCartGoods.setUserCoupon(userCoupon);
					}
					Product product = goodsService.getProductById(saleCartGoods.getProductId());
					saleCartGoods.setProductAmount(product.getSalePrice());
					saleCartGoods.setProductId(product.getId());
					saleCartGoods.setProductName(product.getTitle());
					saleCartGoods.setGoods(product.getGoods());
				}
			}
		}

		/*********************************** 关联优惠券 ******************************************/

		/***********************************
		 * 计算服务小计、商品小计及对应优惠券的折扣价设置
		 ******************************************/
		SaleCart saleCart = null;
		if (svcPack != null) {
			saleCart = new SaleCart();
			List<SvcPackItem> svcPackItemList = svcPack.getPackItemList();
			saleCartSvcList = new ArrayList<SaleCartSvc>();
			for (int i = 0; i < svcPackItemList.size(); i++) {
				SvcPackItem svcPackItem = svcPackItemList.get(i);
				svcPackItem.getAmountInfo();
				SaleCartSvc saleCartSvc = new SaleCartSvc();
				saleCartSvc.setSvcId(svcPackItemList.get(i).getSvcId());
				Svcx carSvc = carSvcService.getCarSvc(saleCartSvc.getSvcId());
				if (saleCartSvc != null && carSvc != null) {
					BigDecimal salePrice = carSvc.getSalePrice();
					saleCartSvc.setSalePrice(salePrice);
					svcPackItem.setSvcSalePrice(salePrice);
					saleCartSvc.setCarSvcName(carSvc.getName());
					saleCartSvc.setDiscRate(new BigDecimal(svcPackItem.getRate()));
					saleCartSvc.setAmountInfo(svcPackItem.getAmountInfo());
					saleCartSvcList.add(saleCartSvc);
				}
			}
			saleCart.setAmountInfo(svcPack.getAmountInfo());
		} else {
			calcRankRate(saleCartSvcList, userId, -1);// 设计会员折扣
			saleCart = CalcSaleHelper.calcSaleCart(saleCartSvcList, saleCartGoodsList, "check");
		}
		/******************** 订单价格相关计算 *********************/
		MiscAmountInfo varietyAmountInfo = saleCart.getAmountInfo();
		saleOrder.setAmount(saleCart.getAmountInfo().getAmount());// 用户应付金额
		saleOrder.setSaleAmount(varietyAmountInfo.getSaleAmount());
		saleOrder.setDiscAmount(varietyAmountInfo.getDiscAmount());
		// 结算金额
		saleOrder.setSettleAmount(varietyAmountInfo.getSettlePrice());

		// 订单应付金额=saleAmount-discAmount-discAmountSub
		BigDecimal amountOuter = new BigDecimal(0.00);
		if (saleOrder.getAmount() != null) {
			amountOuter = saleOrder.getAmount();
			/*
			 * if (saleOrder.getDiscAmount() != null) { amountOuter = amountOuter.subtract(saleOrder.getDiscAmount()); }
			 */
			// 如果应付金额为0，则更改订单支付方式为coupon
			if (amountOuter.compareTo(new BigDecimal(0.00)) == 0 && isHasCoupon) {
				saleOrder.setPayWay(PayWayType.coupon.name());
				goodsService.updateGoodsBuySumByOrderNoAndUserId(orderNo, userId);// 更新用户商品购买数量
			}
		}
		// saleOrder.setAmount(amountOuter);// 用户应付金额
		/******************* 用户E卡价格计算 *********************/
		BigDecimal goodsAmount = saleCart.getGoodsAmount();
		if (saleCart.getGoodsDiscAmount() != null) {
			goodsAmount = goodsAmount.subtract(saleCart.getGoodsDiscAmount());// 商品应付金额=商品总价-商品总优惠价格
		}
		Integer userEcardId = saleOrderPo.getUserEcardId();
		BigDecimal payAmount = new BigDecimal(0.00);// 本次使用金额
		goodsAmount = goodsAmount == null ? new BigDecimal(0.00) : goodsAmount;
		UserECard userECard = null;
		if (userEcardId != null && Integer.valueOf(userEcardId) > 0) {
			userECard = userECardDao.selectById(userEcardId);
			Integer selectedShopId = saleOrder.getShopId();
			// 1.正常平台E卡或选择店铺绑定的E卡或或商品总价大于等于应付金额时：
			int result = goodsAmount.compareTo(amountOuter);
			if (userECard.getShopId() == null || userECard.getShopId().intValue() == selectedShopId.intValue() || (result == 1 || result == 0)) {
				result = userECard.getRemainVal().compareTo(amountOuter);
				if (result == 1 || result == 0) {// （1）当余额大于等于总价时，本次支付价格=总价
					payAmount = amountOuter;
				} else {// （2）当余额小于总价时，本次支付价格=余额
					payAmount = userECard.getRemainVal();
				}
			} else {// 2.使用非此店的E卡，只能支付商品的价格
				result = userECard.getRemainVal().compareTo(goodsAmount);
				if (result == 1 || result == 0) {// （1）当余额大于等于商品总价时，本次支付价格=商品总价
					payAmount = goodsAmount;
				} else {// （2）当余额小于总价时，本次支付价格=余额
					payAmount = userECard.getRemainVal();
				}
			}
			amountOuter = amountOuter.subtract(payAmount);
			// 防止本次支付为0的e卡支付 进行支付提交
			if (payAmount.compareTo(new BigDecimal(0.00)) == 0) {
				userEcardId = null;
			} else if (amountOuter.compareTo(new BigDecimal(0.00)) == 0) {// 如果应付金额为0，则更改订单支付方式为e卡支付
				saleOrder.setPayWay(PayWayType.ecardpay.name());
				goodsService.updateGoodsBuySumByOrderNoAndUserId(orderNo, userId);// 更新用户商品购买数量
			}
		}
		/**************** 外部支付总金额计算 *****************/
		saleOrder.setAmountOuter(amountOuter);
		// 如果应付金额为0，则更改订单状态为已支付。
		if (amountOuter.compareTo(new BigDecimal(0.00)) == 0) {
			saleOrder.setPayState(PayStateType.paid.name());
			// 服务套餐不需要确认完成
			if (svcPack != null) {
				saleOrder.setFinished(true);
				saleOrder.setFinishTime(createTime);
				saleOrder.setSvcTimes(1);
			}
		}
		saleOrderInfo.setPayState(saleOrder.getPayState());

		/**************** 内部支付总金额计算 *****************/
		BigDecimal amountInner = new BigDecimal(0);
		if (userECard != null) {// E卡
			amountInner = amountInner.add(payAmount);
		}
		// TODO 积分
		// TODO 余额
		saleOrder.setAmountInner(amountInner);
		// 插入销售订单记录
		boolean isSuccess = saleOrderDao.insert(saleOrder) > 0;
		if (!isSuccess) {
			logger.error("提交订单失败：" + saleOrder);
			return false;
		} else {
			// 如果订单已支付，则进行插入相关支付记录
			if (PayStateType.paid.name().equals(saleOrder.getPayState())) {
				/*************** 插入支付退款记录 ************WJJ ******************/
				if (userECard != null) {
					settleService.payFinished(saleOrder);
				}
				saleOrderInfo.setDoneCode(doneCode);
			}
			Long orderId = saleOrder.getId();
			saleOrderInfo.setSaleOrderId(orderId);
			saleOrderInfo.setSaleOrderNo(orderNo);
			/********************* 更新用户的优惠券,并插入销售订单内部支付记录 *************************/
			if (svcPack == null) {
				if (saleCartSvcList != null && saleCartSvcList.size() > 0) {
					for (SaleCartSvc saleCartSvc : saleCartSvcList) {
						if (saleCartSvc.getUserCoupon() != null) {
							// 得到优惠券
							UserSvcCoupon userCoupon = saleCartSvc.getUserCoupon();
							if (userCoupon != null) {
								userCoupon.setOrderId(orderId);
								userCoupon.setInvalid(true);
								userSvcCouponDao.update(userCoupon);

								SaleOrderInnerAmount saleOrderInnerAmount = new SaleOrderInnerAmount();
								saleOrderInnerAmount.setOrderId(orderId);
								saleOrderInnerAmount.setSrcFlag(OrderInnerAmountFlag.proCoupon.getValue());
								int srcId = userCoupon.getId();
								saleOrderInnerAmount.setSrcId((long) srcId);
								saleOrderInnerAmount.setAmount(saleCartSvc.getAmountInfo().getDiscAmount());
								saleOrderInnerAmount.setSettleAmount(userCoupon.getSettlePrice());
								saleOrderInnerAmountDao.insert(saleOrderInnerAmount);
							}
							saleCartSvc.setUserCoupon(userCoupon);
						}
					}
				}

				if (saleCartGoodsList != null && saleCartGoodsList.size() > 0) {
					for (SaleCartGoods saleCartGoods : saleCartGoodsList) {
						if (saleCartGoods.getUserCoupon() != null) {
							/* 得到优惠券 */
							UserCoupon userCoupon = saleCartGoods.getUserCoupon();
							if (userCoupon != null) {
								userCoupon.setOrderId(orderId);
								userCoupon.setInvalid(true);
								userCouponDao.update(userCoupon);

								SaleOrderInnerAmount saleOrderInnerAmount = new SaleOrderInnerAmount();
								saleOrderInnerAmount.setOrderId(orderId);
								saleOrderInnerAmount.setSrcFlag(OrderInnerAmountFlag.proCoupon.getValue());
								int srcId = userCoupon.getId();
								saleOrderInnerAmount.setSrcId((long) srcId);
								saleOrderInnerAmount.setAmount(saleCartGoods.getAmountInfo().getDiscAmount());
								saleOrderInnerAmount.setSettleAmount(userCoupon.getSettlePrice());
								saleOrderInnerAmountDao.insert(saleOrderInnerAmount);
							}
						}

					}
				}
			}

			/******************* 插入销售订单内部支付记录 *************************/
			if (userECard != null) {
				SaleOrderInnerAmount saleOrderInnerAmount = new SaleOrderInnerAmount();
				saleOrderInnerAmount.setOrderId(orderId);
				saleOrderInnerAmount.setSrcFlag(OrderInnerAmountFlag.ecard.getValue());
				int srcId = userECard.getId();
				saleOrderInnerAmount.setSrcId((long) srcId);
				saleOrderInnerAmount.setAmount(payAmount);
				saleOrderInnerAmount.setSettleAmount(payAmount);
				saleOrderInnerAmountDao.insert(saleOrderInnerAmount);

				/*************** 插入E卡交易记录 ******************************/
				BigDecimal remainVal = userECard.getRemainVal();
				remainVal = remainVal.subtract(payAmount);

				ECardTransactRec eCardTransactRec = new ECardTransactRec();
				eCardTransactRec.setUserId(userId);
				eCardTransactRec.setCardId(userECard.getId());
				eCardTransactRec.setTargetId(orderId);
				eCardTransactRec.setTargetStr(orderNo);
				eCardTransactRec.setTheVal(payAmount);
				eCardTransactRec.setSubject("orderpay");
				eCardTransactRec.setDirectFlag(-1);// 支出
				eCardTransactRec.setRemainVal(remainVal);
				eCardTransactRec.setTheTime(createTime);
				eCardTransactRecDao.insert(eCardTransactRec);

				/********************* 更新用户的E卡余额 ***********************/
				userECard.setRemainVal(remainVal);
				int result = remainVal.compareTo(new BigDecimal(0.00));
				if (result == 0) {
					userECard.setInvalid(true);
				}
				userECardDao.update(userECard);
			}
		}

		/*********************************** 插入订单服务购买记录 ******************************************/
		for (SaleCartSvc saleCartSvc : saleCartSvcList) {
			if (saleCartSvc != null) {
				SaleOrderSvc orderSvc = new SaleOrderSvc();
				orderSvc.setSvcId(saleCartSvc.getSvcId());
				orderSvc.setSalePrice(saleCartSvc.getSalePrice());
				MiscAmountInfo amountInfo = saleCartSvc.getAmountInfo();
				BigDecimal saleAmount = new BigDecimal(0.00);

				// 如果是套餐
				if (svcPack != null) {
					if (amountInfo != null) {
						orderSvc.setDiscId(saleCartSvc.getId());
						orderSvc.setDiscWay(DiscWay.pack.name());
						orderSvc.setDiscAmount(amountInfo.getDiscAmount());
						orderSvc.setDiscRate(saleCartSvc.getDiscRate());
					}
				} else {
					if (saleCartSvc.getUserCoupon() != null && amountInfo != null) {
						orderSvc.setDiscId(saleCartSvc.getId());
						orderSvc.setDiscWay(DiscWay.coupon.name());
						orderSvc.setDiscAmount(amountInfo.getDiscAmount());
					}
				}

				if (amountInfo != null) {
					saleAmount = saleCartSvc.getAmountInfo().getSaleAmount();
				}
				BigDecimal svAmount = new BigDecimal(0.00);
				orderSvc.setSaleAmount(saleAmount);
				if (orderSvc.getSaleAmount() != null) {
					svAmount = orderSvc.getSaleAmount();
					if (orderSvc.getDiscAmount() != null) {
						svAmount = svAmount.subtract((orderSvc.getDiscAmount()));
					}

				}
				orderSvc.setAmount(svAmount);
				orderSvc.setOrderId(saleOrder.getId());
				orderSvc.setSvcName(saleCartSvc.getCarSvcName());
				// 插入订单服务记录
				saleOrderSvcDao.insert(orderSvc);
				// 生成服务套餐票
				saveUserSvcPackTicket(saleOrder, orderSvc);
			}
		}
		/*********************************** 插入商品购买记录 ******************************************/
		if (svcPack == null) {
			if (saleCartGoodsList != null && saleCartGoodsList.size() > 0) {
				for (SaleCartGoods saleCartGoods : saleCartGoodsList) {
					SaleOrderGoods saleOrderGoods = new SaleOrderGoods();
					saleOrderGoods.setOrderId(saleOrder.getId());
					saleOrderGoods.setGoodsId(saleCartGoods.getGoodsId());
					Goods goods = saleCartGoods.getGoods();
					if (goods != null) {
						saleOrderGoods.setCatId(goods.getCatId());
						saleOrderGoods.setGoodsId(goods.getId());
					}
					saleOrderGoods.setProductId(saleCartGoods.getProductId());
					saleOrderGoods.setProductName(saleCartGoods.getProductName());
					saleOrderGoods.setQuantity(saleCartGoods.getQuantity());
					saleOrderGoods.setSalePrice(saleCartGoods.getProductAmount());
					saleOrderGoods.setSaleAmount(saleCartGoods.getProductAmount().multiply(new BigDecimal(saleCartGoods.getQuantity())));

					MiscAmountInfo amountInfoGoods = saleCartGoods.getAmountInfo();
					if (saleCartGoods.getUserCoupon() != null && amountInfoGoods != null) {
						saleOrderGoods.setDiscId(Long.parseLong(saleCartGoods.getUserCoupon().getId().toString()));
						saleOrderGoods.setDiscWay(DiscWay.coupon.name());
						saleOrderGoods.setDiscAmount(amountInfoGoods.getDiscAmount());
						if (saleOrderGoods.getSaleAmount() != null) {
							amountOuter = saleOrderGoods.getSaleAmount().subtract(saleOrderGoods.getDiscAmount());
						}
					}
					saleOrderGoods.setAmount(amountOuter);
					saleOrderGoodsDao.insert(saleOrderGoods);
				}
			}
		}
		/*********************************** 插入操作订单记录 ******************************************/
		if (userId != null) {
			SaleOrderRecord record = new SaleOrderRecord();
			record.setAction(OrderAction.submit.name());
			record.setActorId(user.getId());
			record.setActorName(getUserName(user));
			record.setActRole(UserType.member.getText());
			record.setExtraInfo(saleOrder.getLeaveMsg());
			record.setOrderId(saleOrder.getId());
			record.setTs(createTime);
			this.saveSaleOrderRecord(record);
		}
		return true;
	}

	/**
	 * 生成服务套餐票
	 * 
	 * @author 邓华锋
	 * @date 2016年1月27日 下午4:25:15
	 * 
	 * @param saleOrder
	 * @param orderSvc
	 * @return
	 */
	private boolean saveUserSvcPackTicket(SaleOrder saleOrder, SaleOrderSvc orderSvc) {
		if (saleOrder.getSvcPackId() != null && PayStateType.paid.name().equals(saleOrder.getPayState())) {
			UserSvcPackTicket userSvcPackTicket = new UserSvcPackTicket();
			userSvcPackTicket.setUserId(saleOrder.getUserId());
			userSvcPackTicket.setShopId(saleOrder.getShopId());
			userSvcPackTicket.setOrderId(saleOrder.getId());
			userSvcPackTicket.setOrderSvcId(orderSvc.getId());
			userSvcPackTicket.setSvcId(orderSvc.getSvcId());
			userSvcPackTicket.setSvcPackId(saleOrder.getSvcPackId());
			userSvcPackTicket.setOrderNo(saleOrder.getNo());
			userSvcPackTicket.setSvcPackName(saleOrder.getSvcPackName());
			userSvcPackTicket.setSvcName(orderSvc.getSvcName());
			userSvcPackTicket.setDoneCode(CodeUtil.newDoneCode());
			userSvcPackTicketDao.insert(userSvcPackTicket);
			// 更新套餐票ID
			orderSvc.setTicketId(userSvcPackTicket.getId());
			return saleOrderSvcDao.update(orderSvc) > 0;
		}
		return false;
	}

	@Override
	public void saveUserSvcPackTicket(SaleOrder saleOrder) {
		if (saleOrder == null || saleOrder.getId() == null || saleOrder.getSvcPackId() == null) {
			return;
		}
		List<SaleOrderSvc> saleOrderSvcList = saleOrderSvcDao.selectByOrderId(saleOrder.getId());
		for (int i = 0; i < saleOrderSvcList.size(); i++) {
			saveUserSvcPackTicket(saleOrder, saleOrderSvcList.get(i));
		}
	}

	@Override
	public SaleOrder getSaleOrderByNo(String orderNo) {
		SaleOrder saleOrder = saleOrderDao.selectByNo(orderNo);
		if (saleOrder != null) {
			saleOrder.setShop(shopService.getShopById(saleOrder.getShopId()));
		}
		return saleOrder;
	}

	@Override
	public boolean updateSaleOrder(SaleOrder saleOrder) {
		return saleOrderDao.update(saleOrder) > 0;
	}

	@Override
	public boolean checkDoneCode(String doneCode, Long orderId) {
		SaleOrder order = saleOrderDao.selectById(orderId);
		if (order == null) {
			logger.error(String.format("Order not exist! orderId : %d", orderId));
			return false;
		}
		String code = order.getDoneCode();
		if (code == null) {
			logger.error(String.format("doneCode not exist! orderId : %d", orderId));
			return false;
		}
		return doneCode.equals(code);
	}

	@Override
	public boolean updateSaleOrderForDelete(SaleOrder saleOrder, UserContext user) {
		// 删除订单（假删deleted=true为删除，暂时写死）
		saleOrder.setDeleted(true);
		saleOrder.setChangeTime(new Date());
		saleOrder.setUserId(user.getUserId());
		Integer count = saleOrderDao.updateForDelete(saleOrder);
		return count > 0;
	}

	@Override
	public boolean updateSaleOrderForCancel(SaleOrder saleOrder, UserContext user) {
		Date date = new Date();
		// 取消订单（cancelled=true为取消）
		saleOrder.setCancelled(true);
		saleOrder.setChangeTime(date);
		String roleName = saleOrder.getRoleName();
		// saleOrder.setUserId(user.getUserId());
		Integer count = saleOrderDao.updateForCancel(saleOrder);
		if (count > 0) {
			// 会员取消还原e卡和优惠券
			if (roleName.equals(UserType.member.name())) {
				this.updateForClosed(saleOrder.getId());
				this.restoreSaleOrderInfo(saleOrder.getId());
			} else if (roleName.equals(UserType.wxshop.name())) {
				SaleOrder saleOrder2 = saleOrderDao.selectById(saleOrder.getId());
				if (saleOrder2 != null) {
					if ("unpaid".equals(saleOrder2.getPayState())) {
						this.updateForClosed(saleOrder.getId());
					}

				}
			}
			// 更新订单成功后添加订单记录
			if (user != null) {
				SaleOrderRecord record = new SaleOrderRecord();
				record.setAction(OrderAction.cancel.name());
				record.setActorId(user.getUserId());
				if (user.getUserName() != null && StrUtil.hasText(user.getUserName())) {
					record.setActorName(user.getUserName());
				} else {
					record.setActorName(user.getPhoneNo());
				}
				if (roleName.equals(UserType.wxshop.name())) {
					record.setActRole(UserType.wxshop.getText());
				} else if (roleName.equals(UserType.member.name())) {
					record.setActRole(UserType.member.getText());
				}
				if (saleOrder.getMemo() != null) {
					record.setExtraInfo(saleOrder.getMemo());
				} else {
					record.setExtraInfo("其他原因");// 后定义
				}
				record.setOrderId(saleOrder.getId());
				record.setTs(date);
				this.saveSaleOrderRecord(record);
			}
		}
		return count > 0;
	}

	@Override
	public boolean updateSaleOrderForFinish(SaleOrder saleOrder, UserContext user) {
		Date date = new Date();
		//
		saleOrder.setFinished(true);
		saleOrder.setChangeTime(date);
		saleOrder.setFinishTime(date);
		// saleOrder.setUserId(user.getUserId());
		Integer count = saleOrderDao.updateForFinish(saleOrder);
		if (count > 0) {
			// 关闭订单
			this.updateForClosed(saleOrder.getId());
			// 更新订单成功后添加订单记录
			if (user != null) {
				SaleOrderRecord record = new SaleOrderRecord();
				record.setAction(OrderAction.finish.name());
				record.setActorId(user.getUserId());
				if (user.getUserName() != null && StrUtil.hasText(user.getUserName())) {
					record.setActorName(user.getUserName());
				} else {
					record.setActorName(user.getPhoneNo());
				}
				String roleName = saleOrder.getRoleName();
				if (roleName.equals(UserType.wxshop.name())) {
					record.setActRole(UserType.wxshop.getText());
				} else if (roleName.equals(UserType.member.name())) {
					record.setActRole(UserType.member.getText());
				}
				record.setExtraInfo("感谢您在亿投车吧享受服务，欢迎您再次光临！");// 后定义
				record.setOrderId(saleOrder.getId());
				record.setTs(date);
				this.saveSaleOrderRecord(record);
			}
			//
			SaleOrder saleOrder2 = saleOrderDao.selectById(saleOrder.getId());
			// 插入车辆服务记录
			if (saleOrder2.getCarId() != null) {
				UserCar userCar = carService.getUserCarById(saleOrder2.getCarId());
				if (userCar != null) {
					List<SaleOrderSvc> svcList = saleOrderSvcDao.selectByOrderId(saleOrder2.getId());
					if (svcList != null && svcList.size() > 0) {
						String svcIds = "";
						String svcNames = "";
						int i = 0;
						for (SaleOrderSvc saleOrderSvc : svcList) {
							svcIds += saleOrderSvc.getSvcId();
							svcNames += saleOrderSvc.getSvcName();
							if (i + 1 != svcList.size()) {
								svcIds += ",";
								svcNames += ",";
							}
							i++;
						}
						UserCarSvcRec userCarSvcRec = new UserCarSvcRec();
						userCarSvcRec.setSvcIds(svcIds);
						userCarSvcRec.setSvcNames(svcNames);
						Date now = new Date();
						userCarSvcRec.setUserId(saleOrder2.getUserId());
						userCarSvcRec.setCarId(userCar.getId());
						userCarSvcRec.setCarName(userCar.getName());
						userCarSvcRec.setBrandId(userCar.getBrandId());
						userCarSvcRec.setSerialId(userCar.getSerialId());
						userCarSvcRec.setModelId(userCar.getModelId());
						userCarSvcRec.setDateVal(now);
						userCarSvcRec.setDateStr(DateUtil.toDateDirStr(new Date()));
						userCarSvcRec.setOrderId(saleOrder2.getId());
						userCarSvcRec.setOrderNo(saleOrder2.getNo());
						userCarSvcRec.setShopId(saleOrder2.getShopId());
						userCarSvcRec.setShopName(saleOrder2.getShopName());
						if (saleOrder2.getDistFlag()) {
							userCarSvcRec.setDistFlag(saleOrder2.getDistFlag());
							userCarSvcRec.setDistShopName(saleOrder2.getDistShopName());
						} else {
							userCarSvcRec.setDistFlag(false);
							userCarSvcRec.setDistShopName(null);
						}
						userCarSvcRec.setTs(new Date());
						carService.saveUserCarSvcRec(userCarSvcRec);

						Integer svcTimes = saleOrder2.getSvcTimes();
						// 更新服务次数
						saleOrder = new SaleOrder();
						saleOrder.setId(saleOrder2.getId());
						svcTimes += 1;
						saleOrder.setSvcTimes(svcTimes);
						updateSaleOrder(saleOrder);
					}

				}
			}

		}
		return count > 0;
	}

	@Override
	public boolean updateSaleOrderPayStateByNo(Map<String, Object> map) {
		return saleOrderDao.updatePayStateByNo(map) > 0;
	}

	@Override
	public boolean updateSaleOrderForFinishAsProxy(SaleOrderActionPo saleOrderActionPo) {
		boolean result = false;
		Date date = new Date();
		SaleOrder saleOrder = saleOrderDao.selectById(saleOrderActionPo.getOrderId());
		if (saleOrder == null) {
			return result;
		}
		// 取消订单（cancelled=true为取消）
		saleOrder.setFinished(true);
		saleOrder.setChangeTime(date);
		saleOrder.setFinishTime(date);
		saleOrder.setCarName(saleOrderActionPo.getCarInfo());
		result = saleOrderDao.updateForFinishAsProxy(saleOrder) > 0;
		if (!result) {
			return result;
		}

		// 添加订单工作信息
		List<Integer> workerIds = saleOrderActionPo.getWorkerIds();
		if (workerIds != null && workerIds.size() > 0) {
			List<SaleOrderWork> saleOrderWorks = new ArrayList<SaleOrderWork>();
			for (Integer workerId : workerIds) {
				User worker = userDao.selectById(workerId);
				if (worker != null) {
					SaleOrderWork saleOrderWork = new SaleOrderWork();
					saleOrderWork.setOrderId(saleOrder.getId());
					saleOrderWork.setWorkerId(worker.getId());
					saleOrderWork.setWorkerName(worker.getNickName());
					saleOrderWork.setChiefFlag(false);
					// 加入列表
					saleOrderWorks.add(saleOrderWork);
				}
			}
			saveSaleOrderWorks(saleOrder.getId(), saleOrderWorks);
		}

		// 更新订单成功后添加订单记录
		User user = userDao.selectById(saleOrderActionPo.getActorId());
		if (user != null) {
			SaleOrderRecord record = new SaleOrderRecord();
			record.setAction(OrderAction.finish.name());
			record.setActorId(user.getId());
			record.setActorName(getUserName(user));
			record.setActRole(UserType.shop.getText());
			record.setOrderId(saleOrder.getId());
			record.setTs(date);
			result = this.saveSaleOrderRecord(record);
		}

		return result;
	}

	@Override
	public boolean updateSaleOrderForAddInfo(SaleOrderActionPo saleOrderActionPo) {
		boolean result = false;
		Date now = new Date();
		SaleOrder saleOrder = saleOrderDao.selectById(saleOrderActionPo.getOrderId());
		if (saleOrder == null) {
			return result;
		}

		saleOrder.setCarName(saleOrderActionPo.getCarInfo());
		saleOrder.setMemo(saleOrderActionPo.getMemo());
		saleOrder.setChangeTime(now);

		result = saleOrderDao.updateForAddInfo(saleOrder) > 0;
		if (!result) {
			return result;
		}

		// 添加订单工作信息
		List<Integer> workerIds = saleOrderActionPo.getWorkerIds();
		if (workerIds != null && workerIds.size() > 0) {
			List<SaleOrderWork> saleOrderWorks = new ArrayList<SaleOrderWork>();
			for (Integer workerId : workerIds) {
				User worker = userDao.selectById(workerId);
				if (worker != null) {
					SaleOrderWork saleOrderWork = new SaleOrderWork();
					saleOrderWork.setOrderId(saleOrder.getId());
					saleOrderWork.setWorkerId(worker.getId());
					saleOrderWork.setWorkerName(worker.getNickName());
					saleOrderWork.setChiefFlag(false);
					// 加入列表
					saleOrderWorks.add(saleOrderWork);
				}
			}
			saveSaleOrderWorks(saleOrder.getId(), saleOrderWorks);
		} else {
			saleOrderWorkDao.deleteByOrderId(saleOrder.getId());
		}

		return result;
	}

	@Override
	public boolean updateSaleOrderForCancelAsProxy(SaleOrderActionPo saleOrderActionPo) {
		boolean result = false;
		Date date = new Date();
		// 取消订单（cancelled=true为取消）
		SaleOrder saleOrder = new SaleOrder();
		saleOrder.setId(saleOrderActionPo.getOrderId());
		saleOrder.setCancelled(true);
		saleOrder.setChangeTime(date);
		saleOrder.setUserId(saleOrderActionPo.getUserId());
		result = saleOrderDao.updateForCancel(saleOrder) > 0;
		if (!result) {
			return result;
		}
		// 更新订单成功后添加订单记录
		if (saleOrderActionPo.getUserId() != null) {
			SaleOrderRecord record = new SaleOrderRecord();
			record.setAction(OrderAction.cancel.name());
			record.setActorId(saleOrderActionPo.getActorId());
			record.setActorName(saleOrderActionPo.getActionName());
			record.setActRole(UserType.shop.getText());
			record.setExtraInfo("其他原因");// 后定义
			record.setOrderId(saleOrder.getId());
			record.setTs(date);
			result = this.saveSaleOrderRecord(record);
			if (!result) {
				return result;
			}
		}
		return result;
	}

	@Override
	public void restoreSaleOrderInfo(Long id) {
		// 查询订单
		SaleOrder saleOrder = saleOrderDao.selectById(id);
		if (saleOrder != null) {
			// 查看是否有内部支付(有问题，是不是内部支付是多条)
			List<SaleOrderInnerAmount> innerAmountList = saleOrderInnerAmountDao.selectByOrderId(id);
			if (innerAmountList != null && innerAmountList.size() > 0) {
				for (SaleOrderInnerAmount innerAmount : innerAmountList) {
					// 还原e卡金额
					if (OrderInnerAmountFlag.ecard.getValue() == innerAmount.getSrcFlag().intValue()) {
						UserECard userECard = userECardDao.selectById(innerAmount.getSrcId().intValue());
						if (userECard != null) {
							if (userECard.getDeleted() == true) {
								userECard.setDeleted(false);
							}
							if (userECard.getInvalid() == true) {
								userECard.setInvalid(false);
							}
							userECard.setRemainVal(userECard.getRemainVal().add(innerAmount.getAmount()));
							boolean falg = userECardDao.updateForChangeRemainVal(userECard) > 0;
							if (falg) {
								ECardTransactRec eCardTransactRec = new ECardTransactRec();
								eCardTransactRec.setUserId(saleOrder.getUserId());
								eCardTransactRec.setCardId(userECard.getId());
								eCardTransactRec.setTargetId(saleOrder.getId());
								eCardTransactRec.setTargetStr(saleOrder.getNo());
								eCardTransactRec.setTheVal(innerAmount.getAmount());
								eCardTransactRec.setSubject("orderpay");
								eCardTransactRec.setDirectFlag(1);// 收入
								eCardTransactRec.setRemainVal(userECard.getRemainVal());
								eCardTransactRec.setTheTime(new Date());
								eCardTransactRecDao.insert(eCardTransactRec);
							}
						}
					} else if (OrderInnerAmountFlag.proCoupon.getValue() == innerAmount.getSrcFlag().intValue()) {
						UserCoupon userCoupon = userCouponDao.selectById(innerAmount.getSrcId().intValue());
						if (userCoupon != null) {
							// 还原优惠券
							if (userCoupon.getDeleted() == true) {
								userCoupon.setDeleted(false);
							}
							userCoupon.setOrderId(null);
							// 获取优惠券有效结束时间
							Date endTime = userCoupon.getEndTime();
							if (endTime != null) {
								try {
									Date end = DateUtil.fromStdDateStr(DateUtil.toStdDateStr(endTime));
									Date dateTime = DateUtil.fromStdDateStr(DateUtil.toStdDateStr(new Date()));
									if (end.getTime() - dateTime.getTime() >= 0) {
										userCoupon.setInvalid(false);
									}
								} catch (ParseException e) {
								}

							}
							userCouponDao.update(userCoupon);
						}
					} else if (OrderInnerAmountFlag.svcCoupon.getValue() == innerAmount.getSrcFlag().intValue()) {
						UserSvcCoupon userCoupon = userSvcCouponDao.selectById(innerAmount.getSrcId().intValue());
						if (userCoupon != null) {
							// 还原优惠券
							if (userCoupon.getDeleted() == true) {
								userCoupon.setDeleted(false);
							}
							userCoupon.setOrderId(null);
							// 获取优惠券有效结束时间
							Date endTime = userCoupon.getEndTime();
							if (endTime != null) {
								try {
									Date end = DateUtil.fromStdDateStr(DateUtil.toStdDateStr(endTime));
									Date dateTime = DateUtil.fromStdDateStr(DateUtil.toStdDateStr(new Date()));
									if (end.getTime() - dateTime.getTime() >= 0) {
										userCoupon.setInvalid(false);
									}
								} catch (ParseException e) {
								}

							}
							userSvcCouponDao.update(userCoupon);
						}
					}
				}
			}
		}
	}

	@Override
	public void calcRankRate(List<SaleCartSvc> svcList, Integer userId, Integer shopId) {
		if (userId != null) {
			Member member = memberService.getMemberById(userId);
			if (member != null && member.getRank() != null) {
				if (svcList != null && svcList.size() > 0) {
					for (SaleCartSvc saleCartSvc : svcList) {
						if (saleCartSvc != null) {
							SvcxRankDisc rankDisc = eCardService.getSvcRankDiscByFilter(shopId, saleCartSvc.getSvcId(), member.getRank());
							if (rankDisc != null) {
								// 扣减折扣
								saleCartSvc.setDiscRate(rankDisc.getRate());
							}
						}
					}
				}
			}
		}
	}

	@Override
	public List<SaleOrderGoods> getSaleOrderGoodssByOrderNoAndUserId(String orderNo, Integer userId) {
		return saleOrderGoodsDao.selectByOrderNoAndUserId(orderNo, userId);
	}

	@Override
	public PaginatedList<UserSvcPackTicket> getUserSvcPackTicketsByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<UserSvcPackTicket> paginatedList = userSvcPackTicketDao.selectByFilter(paginatedFilter);
		List<UserSvcPackTicket> userSvcPackTicketList = paginatedList.getRows();
		for (int i = 0; i < userSvcPackTicketList.size(); i++) {
			UserSvcPackTicket userSvcPackTicket = userSvcPackTicketList.get(i);
			// 通过svcId获取服务获取服务价格
			Svcx carSvc = svcxDao.selectById(userSvcPackTicket.getSvcId());
			if (carSvc != null) {
				userSvcPackTicket.setSvcSalePrice(carSvc.getSalePrice());
				userSvcPackTicket.setSvcName(carSvc.getName());
				userSvcPackTicket.setFileBrowseUrl(getFileBrowseUrl(carSvc.getImagePath(), carSvc.getImageUsage()));
			}
			// 通过packId和svcId获取服务套餐项目获取折扣率
			SvcPackItem svcPackItem = svcPackItemDao.selectBySvcIdAndSvcPackId(userSvcPackTicket.getSvcId(), userSvcPackTicket.getSvcPackId());
			if (svcPackItem != null) {
				userSvcPackTicket.setRate(new BigDecimal(svcPackItem.getRate()));
			}
			// 获取用户信息
			User user = userDao.selectById(userSvcPackTicket.getUserId());
			userSvcPackTicket.setUser(user);
			// 获取门店信息
			Shop shop = shopDao.selectById(userSvcPackTicket.getShopId());
			userSvcPackTicket.setShop(shop);
		}
		return paginatedList;
	}

	@Override
	public boolean updateUserSvcPackTicket(UserSvcPackTicket userSvcPackTicket) {
		return userSvcPackTicketDao.update(userSvcPackTicket) > 0;
	}

	@Override
	public UserSvcPackTicket getUserSvcPackTicketByFilter(MapContext filter) {
		UserSvcPackTicket svcPackTicket = userSvcPackTicketDao.selectOneByFilter(filter);
		if (svcPackTicket != null) {
			// 通过svcId获取服务获取服务价格
			Svcx svcx = carSvcService.getCarSvc(svcPackTicket.getSvcId());
			if (svcx != null) {
				svcPackTicket.setFileBrowseUrl(getFileBrowseUrl(svcx.getImagePath(), svcx.getImageUsage()));
				String iconUsage = svcx.getIconUsage();
				svcPackTicket.setFileBrowseUrlApp(getFileBrowseUrl(svcx.getIconPathApp(), iconUsage));
				svcPackTicket.setFileBrowseUrlIcon(getFileBrowseUrl(svcx.getIconPath(), iconUsage));
				svcPackTicket.setFileBrowseUrlIcon2(getFileBrowseUrl(svcx.getIconPath2(), iconUsage));
			}
			// 通过packId和svcId获取服务套餐项目获取折扣率
			SvcPackItem svcPackItem = svcPackItemDao.selectBySvcIdAndSvcPackId(svcPackTicket.getSvcId(), svcPackTicket.getSvcPackId());
			if (svcPackItem != null) {
				svcPackTicket.setRate(new BigDecimal(svcPackItem.getRate()));
			}
		}
		return svcPackTicket;
	}

	// 获取图片
	private String getFileBrowseUrl(String imagePath, String imageUsage) {
		// 获取图片
		String browseUrl = null;
		if (imagePath != null && imageUsage != null) {
			browseUrl = fileRepository.getFileBrowseUrl(imageUsage, imagePath);
		} else {
			browseUrl = fileRepository.getFileRepoConfig().getNoImageUrl();
		}
		return browseUrl;
	}

	@Override
	public boolean deleteUserSvcPackTicketByUserIdAndOrderNo(Integer userId, String orderNo) {
		return userSvcPackTicketDao.deleteByUserIdAndOrderNo(userId, orderNo) > 0;
	}

	@Override
	public Integer getUsedSvcPackTicketCount(Integer userId, String orderNo) {
		return userSvcPackTicketDao.selectUsedSvcPackTicketCount(userId, orderNo);
	}

	@Override
	public void sendDemoActionMessage() {
		xOrderMessageProxy.sendActionMessage(OrderType.saleOrder, 1L, "Sxxxxxxx", 2, 13, OrderAction.submit, true);

		xOrderMessageProxy.sendActionMessage(OrderType.saleOrder, 1L, "Sxxxxxxx", 2, 13, OrderAction.pay, true);
	}

	@Override
	public List<SaleOrderGoods> getSaleOrderGoodsByOrderId(Long orderId) {
		// 查看商品
		List<SaleOrderGoods> saleOrderGoodsList = saleOrderGoodsDao.selectByOrderSvcId(orderId);
		if (saleOrderGoodsList != null && saleOrderGoodsList.size() > 0) {
			for (SaleOrderGoods saleOrderGoods : saleOrderGoodsList) {
				// 获取图片
				saleOrderGoods.setProductAlbumImg(getProductAlbumImg(saleOrderGoods.getProductId()));
			}
		}
		return saleOrderGoodsList;
	}

	@Override
	public int getUnUsedSvcPackTicketCount(Integer userId) {
		return userSvcPackTicketDao.selectUnUsedSvcPackTicketCount(userId);
	}

	@Override
	public PaginatedList<SaleOrder> getSaleOrdersByDistributorId(PaginatedFilter paginatedFilter) {
		PaginatedList<SaleOrder> paginatedList = saleOrderDao.selectByDistributorId(paginatedFilter);
		List<SaleOrder> listSaleOrder = paginatedList.getRows();
		if (null != listSaleOrder) {
			for (SaleOrder saleOrder : listSaleOrder) {
				// 查询订单服务
				List<SaleOrderSvc> orderSvcList = this.getSaleOrderSvcsByOrderId(saleOrder.getId());
				saleOrder.setSaleOrderSvcs(orderSvcList);
				// 查看商品
				List<SaleOrderGoods> saleOrderGoodsList = this.getSaleOrderGoodsByOrderId(saleOrder.getId());
				saleOrder.setSaleOrderGoods(saleOrderGoodsList);
			}
		}
		return paginatedList;
	}

	@Override
	public PaginatedList<SaleOrder> selectDistShopOrderByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<SaleOrder> paginatedList = saleOrderDao.selectDistShopOrderByFilter(paginatedFilter);
		for (SaleOrder saleOrder : paginatedList.getRows()) {
			// 查询订单关联服务
			List<SaleOrderSvc> saleOrderSvcs = saleOrderSvcDao.selectByOrderId(saleOrder.getId());
			saleOrder.setSaleOrderSvcs(saleOrderSvcs);
			List<SaleOrderGoods> saleOrderGoods = saleOrderGoodsDao.selectByOrderId(saleOrder.getId());
			saleOrder.setSaleOrderGoods(saleOrderGoods);
			// 获取门店信息
			Shop shop = shopDao.selectById(saleOrder.getShopId());
			saleOrder.setShop(shop);
			// 查询订单操作记录
			List<SaleOrderRecord> saleOrderRecords = saleOrderRecordDao.selectByOrderId(saleOrder.getId());
			saleOrder.setSaleOrderRecords(saleOrderRecords);
			// 查询订单相关工作
			List<SaleOrderWork> saleOrderWorks = getSaleOrderWorksById(saleOrder.getId());
			saleOrder.setSaleOrderWorks(saleOrderWorks);
		}
		return paginatedList;
	}

	@Override
	public PaginatedList<SaleOrder> selectDistShopOrderSettleByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<SaleOrder> paginatedList = saleOrderDao.selectDistShopOrderSettleByFilter(paginatedFilter);
		if (paginatedList.getRows().size() > 0) {
			for (SaleOrder saleOrder : paginatedList.getRows()) {
				if (saleOrder.getSettleRecId2Dist() != null) {
					DistSettleRec distSettleRec = distSettleRecDao.selectById(saleOrder.getSettleRecId2Dist());
					saleOrder.setDistSettleRec(distSettleRec);
				} else {
					saleOrder.setDistSettleRec(new DistSettleRec());
				}
			}
		}
		return paginatedList;
	}

	@Override
	public Boolean updateForClosed(Long orderId) {
		boolean done = false;
		SaleOrder saleOrder = saleOrderDao.selectById(orderId);
		saleOrder.setCloseTime(new Date());
		if ( saleOrder != null ) {
			if ( !saleOrder.getClosed() ) { // 订单未关闭
				String payState = saleOrder.getPayState();
				if ( saleOrder.getCancelled() ) { // 订单取消(未支付或退款成功)
					if ( PayStateType.unpaid.name().equalsIgnoreCase(payState) || PayStateType.refunded.name().equalsIgnoreCase(payState) ) {
						saleOrderDao.updateForClosed(saleOrder);
						return true;
					}
				} else {
					if ( saleOrder.getFinished() && PayStateType.paid.name().equalsIgnoreCase(payState) ) { // 订单完成(支付成功)
						saleOrderDao.updateForClosed(saleOrder);
						return true;
					}
				}
			}
		}
		return done;
	}

	@Override
	public List<Integer> getShopIdsByHaveServedUser(Integer userId) {
		return saleOrderDao.selectShopIdsByUserId(userId);
	}

	@Override
	public Boolean updateSaleOrderForShopChange(SaleOrder saleOrder) {
		//
		List<SaleOrderSvc> listSvc = saleOrder.getSaleOrderSvcs();
		if (listSvc != null && listSvc.size() > 0) {
			for (SaleOrderSvc saleOrderSvc : listSvc) {
				saleOrderSvcDao.update(saleOrderSvc);
			}
		}
		return saleOrderDao.update(saleOrder) > 0;
	}
}
