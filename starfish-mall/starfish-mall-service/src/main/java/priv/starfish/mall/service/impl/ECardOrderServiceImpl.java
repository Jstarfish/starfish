package priv.starfish.mall.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.repo.FileRepository;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.dao.comn.UserDao;
import priv.starfish.mall.comn.entity.User;
import priv.starfish.mall.dao.ecard.ECardDao;
import priv.starfish.mall.ecard.entity.ECard;
import priv.starfish.mall.dao.order.ECardOrderDao;
import priv.starfish.mall.dao.order.ECardOrderItemDao;
import priv.starfish.mall.dao.order.ECardOrderRecordDao;
import priv.starfish.mall.order.dict.OrderAction;
import priv.starfish.mall.order.dto.ECardDto;
import priv.starfish.mall.order.entity.ECardOrder;
import priv.starfish.mall.order.entity.ECardOrderRecord;
import priv.starfish.mall.service.ECardOrderService;
import priv.starfish.mall.service.SalePrmtService;
import priv.starfish.mall.service.util.NoUtil;
import priv.starfish.mall.dao.shop.ShopDao;
import priv.starfish.mall.shop.entity.Shop;

@Service("eCardOrderService")
public class ECardOrderServiceImpl extends BaseServiceImpl implements ECardOrderService {
	@Resource
	ECardOrderDao eCardOrderDao;

	@Resource
	ECardOrderRecordDao eCardOrderRecordDao;

	@Resource
	ECardDao eCardDao;

	@Resource
	UserDao userDao;

	@Resource
	FileRepository fileRepository;

	@Resource
	ECardOrderItemDao eCardOrderItemDao;

	@Resource
	ShopDao shopDao;

	@Resource
	SalePrmtService salePrmtService;

	@Override
	public PaginatedList<ECardOrder> getECardOrdersByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<ECardOrder> eCardOrders = eCardOrderDao.selectByFilter(paginatedFilter);
		// 设置LOGO查看路径
		for (ECardOrder eCardOrder : eCardOrders.getRows()) {
			ECard eCard = eCardDao.selectById(eCardOrder.getCardCode());
			String browseUrl = fileRepository.getFileBrowseUrl(eCard.getLogoUsage(), eCard.getLogoPath());
			if (StrUtil.isNullOrBlank(browseUrl)) {
				browseUrl = fileRepository.getFileRepoConfig().getNoImageUrl();
			}
			eCardOrder.setFileBrowseUrl(browseUrl);
		}
		return eCardOrders;
	}

	@Override
	public ECardOrder getECardOrderByOrderIdAndUserId(Integer orderId, Integer userId) {
		MapContext filter = MapContext.newOne();
		filter.put("orderId", orderId);
		filter.put("userId", userId);
		ECardOrder eCardOrder = eCardOrderDao.selectOneByFilter(filter);
		if (eCardOrder == null) {
			return null;
		}
		// 设置LOGO查看路径
		ECard eCard = eCardDao.selectById(eCardOrder.getCardCode());
		String browseUrl = fileRepository.getFileBrowseUrl(eCard.getLogoUsage(), eCard.getLogoPath());
		if (StrUtil.isNullOrBlank(browseUrl)) {
			browseUrl = fileRepository.getFileRepoConfig().getNoImageUrl();
		}
		eCardOrder.setFileBrowseUrl(browseUrl);
		// 获取订单操作记录
		List<ECardOrderRecord> eCardOrderRecords = eCardOrderRecordDao.selectByOrderId(orderId);
		eCardOrder.seteCardOrderRecords(eCardOrderRecords);

		return eCardOrder;
	}

	@Override
	public Boolean updateECardOrderForCancel(Integer orderId, Integer userId) {
		Boolean result = false;
		MapContext filter = MapContext.newOne();
		filter.put("orderId", orderId);
		filter.put("userId", userId);
		result = eCardOrderDao.updateForCancel(filter) > 0;
		if (!result) {
			return result;
		}

		// 记录取消操作
		User user = userDao.selectById(userId);
		ECardOrderRecord eCardOrderRecord = new ECardOrderRecord();
		eCardOrderRecord.setAction("cancel");
		eCardOrderRecord.setActorId(userId);
		eCardOrderRecord.setActorName(user.getRealName());
		eCardOrderRecord.setActRole("用户");
		eCardOrderRecord.setOrderId(orderId);
		result = eCardOrderRecordDao.insert(eCardOrderRecord) > 0;

		return result;
	}

	@Override
	public Integer createECardOrder(UserContext userContext, ECardDto eCardDto) {
		Date date = new Date();
		ECard eCard = eCardDao.selectById(eCardDto.getCode());
		// 保存订单信息
		ECardOrder eCardOrder = ECardOrder.newOne();
		eCardOrder.setNo(NoUtil.newECardOrderNo());
		eCardOrder.setUserId(userContext.getUserId());
		eCardOrder.setUserName(userContext.getUserName());
		eCardOrder.setCardCode(eCard.getCode());
		eCardOrder.setCardName(eCard.getName());
		eCardOrder.setFaceValue(eCard.getFaceVal());
		eCardOrder.setPrice(eCard.getPrice());
		eCardOrder.setQuantity(eCardDto.getQuantity());
		eCardOrder.setPhoneNo(userContext.getPhoneNo());
		// 计算金额
		Integer quantity = eCardDto.getQuantity();
		BigDecimal price = eCard.getPrice();
		BigDecimal amount = price.multiply(BigDecimal.valueOf(quantity));
		eCardOrder.setAmount(amount);
		//
		eCardOrder.setPayWay(eCardDto.getPayWay());
		eCardOrder.setCreateTime(date);
		eCardOrder.setPaid(false);
		eCardOrder.setCancelled(false);
		if (eCardDto.getShopId() != null) {
			Shop shop = shopDao.selectById(eCardDto.getShopId());
			if (shop != null) {
				eCardOrder.setShopId(shop.getId());
				eCardOrder.setShopName(shop.getName());
			}
		}
		// 订单保存成功赠送优惠券
		Integer count = eCardOrderDao.insert(eCardOrder);
		if (count != null && count > 0) {
			// 保存订单记录
			ECardOrderRecord eCardOrderRecord = ECardOrderRecord.newOne();
			eCardOrderRecord.setAction(OrderAction.submit.toString());
			eCardOrderRecord.setActRole("会员");
			eCardOrderRecord.setActorId(userContext.getUserId());
			eCardOrderRecord.setActorName("客户");
			eCardOrderRecord.setExtraInfo("您提交了订单，请等待系统确认");
			eCardOrderRecord.setOrderId(eCardOrder.getId());
			eCardOrderRecord.setTs(date);
			eCardOrderRecordDao.insert(eCardOrderRecord);
			saveECardOrderRecord(eCardOrder.getId());
			// saveEcardActGift
			//salePrmtService.saveEcardActGift(userContext.getUserId(), eCard.getCode(), eCardDto.getQuantity(), amount);
			// 测试
			// Boolean falg=existFirstOrder(userContext.getUserId());
			// if (falg != null && falg == true) {
			// // 查询e卡首单赠送那个优惠券
			// Coupon coupon = salePrmtService.getCouponForFirstECardOrder();
			// if (coupon != null) {
			// // 保存用户优惠券
			// salePrmtService.createUserCoupon(coupon, eCardOrder.getUserId());
			// }
			// }
		}
		return eCardOrder.getId();
	}

	// 系统确认记录
	private void saveECardOrderRecord(Integer orderId) {
		ECardOrderRecord eCardOrderRecord = ECardOrderRecord.newOne();
		eCardOrderRecord.setAction(OrderAction.confirm.toString());
		eCardOrderRecord.setActRole("平台");
		eCardOrderRecord.setActorId(-1);
		eCardOrderRecord.setActorName("平台");
		eCardOrderRecord.setExtraInfo("");
		eCardOrderRecord.setOrderId(orderId);
		eCardOrderRecord.setTs(new Date());
		eCardOrderRecordDao.insert(eCardOrderRecord);
	}

	@Override
	public ECardOrder getECardOrderByOrderId(Integer orderId, Integer userId) {
		MapContext filter = MapContext.newOne();
		filter.put("orderId", orderId);
		filter.put("userId", userId);
		ECardOrder eCardOrder = eCardOrderDao.selectOneByFilter(filter);
		return eCardOrder;
	}

	@Override
	public ECardOrder getECardOrderByNo(String no) {
		return eCardOrderDao.selectByNo(no);
	}

	@Override
	public Boolean updateECardOrderByNo(Map<String, Object> map) {
		return eCardOrderDao.updateByNo(map) > 0;
	}

	@Override
	public boolean existFirstOrder(Integer userId, String code) {
		// 判断此单是否首单
		Integer orderCount = eCardOrderDao.selectCount(userId,code);
		if (orderCount != null && orderCount <= 1) {
			return true;
		}
		return false;
	}

	@Override
	public List<ECardOrder> getECardOrders(MapContext filter) {
		List<ECardOrder> eCardOrders = eCardOrderDao.selectByFilterNormal(filter);
		if (eCardOrders != null) {
			for (ECardOrder eCardOrder : eCardOrders) {
				ECard eCard = eCardDao.selectById(eCardOrder.getCardCode());
				String browseUrl = fileRepository.getFileBrowseUrl(eCard.getLogoUsage(), eCard.getLogoPath());
				if (StrUtil.isNullOrBlank(browseUrl)) {
					browseUrl = fileRepository.getFileRepoConfig().getNoImageUrl();
				}
				eCardOrder.setFileBrowseUrl(browseUrl);
			}
		}
		return eCardOrders;
	}

	@Override
	public Integer getECardOrderCount(MapContext filter) {
		return eCardOrderDao.selectCountByFilter(filter);
	}

	@Override
	public boolean getECardOrderCountByCode(String code) {
		return eCardOrderDao.selectCountByCode(code) > 0;
	}

	@Override
	public PaginatedList<ECardOrder> getByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<ECardOrder> paginatedList = eCardOrderDao.selectByFilterBack(paginatedFilter);
		//
		return paginatedList;
	}
}
