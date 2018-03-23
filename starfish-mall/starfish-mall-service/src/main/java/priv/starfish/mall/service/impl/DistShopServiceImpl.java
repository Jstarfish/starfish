package priv.starfish.mall.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import priv.starfish.common.jms.SimpleMessage;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.repo.FileRepository;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.comn.dict.AuditStatus;
import priv.starfish.mall.comn.dto.RegionParts;
import priv.starfish.mall.comn.misc.BizParamInfo;
import priv.starfish.mall.dao.order.SaleOrderDao;
import priv.starfish.mall.dao.order.SaleOrderSvcDao;
import priv.starfish.mall.order.entity.SaleOrder;
import priv.starfish.mall.service.BaseConst;
import priv.starfish.mall.service.DistShopService;
import priv.starfish.mall.service.SettingService;
import priv.starfish.mall.dao.settle.DistSettleRecDao;
import priv.starfish.mall.settle.entity.DistSettleRec;
import priv.starfish.mall.dao.shop.DistShopDao;
import priv.starfish.mall.dao.shop.DistShopMemoDao;
import priv.starfish.mall.dao.shop.DistShopPicDao;
import priv.starfish.mall.dao.shop.DistShopSvcDao;
import priv.starfish.mall.dao.shop.ShopDao;
import priv.starfish.mall.shop.entity.DistShop;
import priv.starfish.mall.shop.entity.DistShopMemo;
import priv.starfish.mall.shop.entity.DistShopPic;
import priv.starfish.mall.shop.entity.DistShopSvc;
import priv.starfish.mall.shop.entity.Shop;
import priv.starfish.mall.dao.svcx.SvcxDao;
import priv.starfish.mall.svcx.entity.Svcx;

@Service("distShopService")
public class DistShopServiceImpl extends BaseServiceImpl implements DistShopService {
	@Resource
	DistShopDao distShopDao;

	@Resource
	DistShopPicDao distShopPicDao;

	@Resource
	SettingService settingService;

	@Resource
	FileRepository fileRepository;

	@Resource
	ShopDao shopDao;

	@Resource
	DistShopMemoDao distShopMemoDao;

	@Resource
	DistShopSvcDao distShopSvcDao;

	@Resource
	SvcxDao svcxDao;

	@Resource
	SaleOrderDao saleOrderDao;

	@Resource
	DistSettleRecDao distSettleRecDao;

	@Resource
	SaleOrderSvcDao saleOrderSvcDao;

	// ---------------------------------------
	// 卫星店铺----------------------------------------------

	private void sendDistShopChangeQueueMessage(Integer distShopId) {
		SimpleMessage messageToSend = SimpleMessage.newOne();
		messageToSend.subject = BaseConst.TaskFocus.DIST_SHOP;
		messageToSend.key = distShopId;
		simpleMessageSender.sendQueueMessage(BaseConst.QueueNames.TASK, messageToSend);
	}

	@Override
	public DistShop getById(Integer DistShopId) {
		DistShop distShop = distShopDao.selectById(DistShopId);
		this.filterFileBrowseUrl(distShop);
		//
		List<DistShopPic> distShopPics = distShopPicDao.selectByDistShopId(DistShopId);
		if (distShopPics.size() > 0) {
			for (DistShopPic distShopPic : distShopPics) {
				this.filterFileBrowseUrl(distShopPic);
			}
			distShop.setDistShopPic(distShopPics);
		}
		return distShop;
	}

	@Override
	public PaginatedList<DistShop> getDistShopsByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<DistShop> distShopList = distShopDao.selectDistShops(paginatedFilter);
		List<DistShop> listDistShop = distShopList.getRows();
		if (listDistShop != null) {
			for (DistShop distShop : listDistShop) {
				this.filterFileBrowseUrl(distShop);
				// 获取加盟店
				Shop shop = shopDao.selectById(distShop.getOwnerShopId());
				this.filterFileBrowseUrl(shop);
				distShop.setShop(shop);
			}
		}

		distShopList.setRows(listDistShop);
		return distShopList;
	}

	@Override
	public boolean saveDistShop(DistShop distShop) {
		// 通过regionId获取相关信息
		RegionParts regionParts = settingService.getRegionPartsById(distShop.getRegionId());
		distShop.setRegionName(regionParts.fullName);
		distShop.setProvinceId(regionParts.provinceId);
		distShop.setCityId(regionParts.cityId);
		distShop.setCountyId(regionParts.countyId);
		distShop.setTownId(regionParts.townId);
		distShop.setAddress(regionParts.fullName + distShop.getStreet());
		distShop.setRegTime(new Date());
		if (BizParamInfo.wxShopNeedToBeAuditted) {
			distShop.setAuditStatus(AuditStatus.unAuditted.getValue());
		} else {
			distShop.setAuditStatus(AuditStatus.auditOk.getValue());
		}
		distShop.setDisabled(false);

		boolean flag = false;
		//
		DistShop DBDistShop = distShopDao.selectById(distShop.getId());
		if (DBDistShop != null) {
			flag = distShopDao.update(distShop) > 0;
			if (flag) {
				// 删除卫星店服务
				if (DBDistShop.getOwnerShopId() != distShop.getOwnerShopId()) {
					distShopSvcDao.deleteByDistShopId(distShop.getId());
				}
				// 修改卫星店图片
				List<DistShopPic> DBDistShopPic = distShopPicDao.selectByDistShopId(distShop.getId());
				List<DistShopPic> distShopPics = distShop.getDistShopPic();
				if (distShopPics != null && distShopPics.size() > 0) {
					if (DBDistShopPic.size() == 0) {
						for (DistShopPic distShopPic : distShopPics) {
							distShopPic.setDistShopId(distShop.getId());
							distShopPicDao.insert(distShopPic);
						}
					} else {
						for (DistShopPic distShopPic : distShopPics) {
							distShopPic.setDistShopId(distShop.getId());
							distShopPicDao.update(distShopPic);
						}
					}
				}
			}
		} else {
			flag = distShopDao.insert(distShop) > 0;
			if (flag) {
				List<DistShopPic> distShopPics = distShop.getDistShopPic();
				if (distShopPics != null) {
					for (DistShopPic distShopPic : distShopPics) {
						distShopPic.setDistShopId(distShop.getId());
						distShopPicDao.insert(distShopPic);
					}
				}
			}
		}

		if (flag) {
			this.sendDistShopChangeQueueMessage(distShop.getId());
		}
		return flag;
	}

	@Override
	public boolean updateDistShop(DistShop distShop) {
		boolean success = distShopDao.update(distShop) > 0;
		if (success) {
			this.sendDistShopChangeQueueMessage(distShop.getId());
		}
		return success;
	}

	@Override
	public boolean deleteDistShopById(Integer distShopId) {
		boolean success = distShopDao.deleteById(distShopId) > 0;
		if (success) {
			this.sendDistShopChangeQueueMessage(distShopId);
		}
		return success;
	}

	@Override
	public boolean updateDistShopDisableState(Integer id, boolean disabled) {
		boolean flag = false;
		DistShop distShop = new DistShop();
		distShop.setId(id);
		distShop.setDisabled(disabled);
		flag = distShopDao.update(distShop) > 0;
		if (flag) {
			this.sendDistShopChangeQueueMessage(distShop.getId());
		}
		return flag;
	}

	@Override
	public boolean updateDistShopAudit(DistShop distShop, UserContext userContext) {
		distShop.setAuditorId(userContext.getUserId());
		distShop.setAuditorName(userContext.getUserName());
		distShop.setAuditTime(new Date());
		boolean success = distShopDao.update(distShop) > 0;
		if (success) {
			this.sendDistShopChangeQueueMessage(distShop.getId());
		}
		return success;
	}

	@Override
	public boolean batchUpdateDistShopsAudit(List<String> list, Integer auditStatus, String auditorDesc, UserContext userContext) {
		boolean flag = true;
		//
		DistShop distShop = new DistShop();
		distShop.setAuditStatus(auditStatus);
		distShop.setAuditorDesc(auditorDesc);
		//
		for (int i = 0; i < list.size(); i++) {
			distShop.setId(Integer.parseInt(list.get(i)));
			flag = updateDistShopAudit(distShop, userContext) && flag;
		}
		//
		return flag;
	}

	@Override
	public PaginatedList<DistShop> getDistShopsByFilterAndShopId(PaginatedFilter paginatedFilter, Integer shopId) {
		PaginatedList<DistShop> distShopList = distShopDao.selectDistShops(paginatedFilter);
		List<DistShop> listDistShop = distShopList.getRows();

		if (listDistShop != null && listDistShop.size() > 0) {
			for (DistShop distShop : listDistShop) {
				this.filterFileBrowseUrl(distShop);
				//
				MapContext mapContext = MapContext.newOne();
				mapContext.put("distShopId", distShop.getId());
				distShop.setDistShopSvcList(getDistShopSvcsByFilter(mapContext));
			}
		}
		distShopList.setRows(listDistShop);
		return distShopList;
	}

	// ---------------------------------------卫星店备忘录--------------------------------------------
	@Override
	public boolean saveDistShopMemo(DistShopMemo distShopMemo) {
		return distShopMemoDao.insert(distShopMemo) > 0;
	}

	@Override
	public boolean deleteDistShopMemoById(Integer id) {
		return distShopMemoDao.deleteById(id) > 0;
	}

	@Override
	public boolean updateDistShopMemo(DistShopMemo distShopMemo) {
		return distShopMemoDao.update(distShopMemo) > 0;
	}

	@Override
	public PaginatedList<DistShopMemo> getDistShopMemosByFilter(PaginatedFilter paginatedFilter) {
		return distShopMemoDao.selectByFilter(paginatedFilter);
	}

	@Override
	public DistShopMemo getDistShopMemoByIdAndDistShopId(Integer id, Integer distShopId) {
		return distShopMemoDao.selectByIdAndDistShopId(id, distShopId);
	}

	@Override
	public DistShopMemo getDistShopMemoById(Integer id) {
		return distShopMemoDao.selectById(id);
	}

	// ---------------------------------------卫星店服务--------------------------------------------
	@Override
	public boolean saveDistShopSvc(DistShopSvc distShopSvc) {
		boolean ok = false;
		if (distShopSvc != null) {
			Svcx svcx = svcxDao.selectById(distShopSvc.getSvcId());
			if (svcx != null) {
				distShopSvc.setApplyFlag(true);
				distShopSvc.setApplyTime(new Date());
				distShopSvc.setAuditStatus(0);
				distShopSvc.setTs(new Date());
				ok = distShopSvcDao.insert(distShopSvc) > 0;
				if (ok) {
					Integer distShopId = distShopSvc.getDistShopId();
					this.updateChangeTime(distShopId, true);
					this.sendDistShopChangeQueueMessage(distShopId);
				}
			}
		}
		return ok;
	}

	@Override
	public boolean deleteDistShopSvcById(Long id) {
		DistShopSvc distShopSvc = distShopSvcDao.selectById(id);
		boolean success = distShopSvc != null && distShopSvcDao.deleteById(id) > 0;
		if (success) {
			Integer distShopId = distShopSvc.getDistShopId();
			this.sendDistShopChangeQueueMessage(distShopId);
		}
		return success;
	}

	@Override
	public boolean deleteDistShopSvc(Integer distShopId, Integer svcId) {
		DistShopSvc distShopSvc = distShopSvcDao.selectByDistShopIdAndSvcId(distShopId, svcId);

		boolean ok = distShopSvc != null && distShopSvcDao.deleteById(distShopSvc.getId()) > 0;
		if (ok) {
			this.sendDistShopChangeQueueMessage(distShopId);
		}

		return ok;
	}

	@Override
	public boolean updateDistShopSvc(DistShopSvc distShopSvc) {
		distShopSvc.setTs(new Date());
		boolean success = distShopSvcDao.update(distShopSvc) > 0;
		if (success) {
			DistShopSvc shopSvc = distShopSvcDao.selectById(distShopSvc.getId());
			if (shopSvc != null) {
				int distShopId = shopSvc.getDistShopId();
				this.updateChangeTime(distShopId, true);
				this.sendDistShopChangeQueueMessage(distShopId);
			}
		}
		return success;
	}

	@Override
	public PaginatedList<DistShopSvc> getDistShopSvcsByFilter(PaginatedFilter paginatedFilter) {
		MapContext filter = paginatedFilter.getFilterItems();
		if (filter != null) {
			if (StrUtil.hasText(paginatedFilter.getKeywords())) {
				filter.put("keywords", paginatedFilter.getKeywords());
			}
		}
		PaginatedList<DistShopSvc> paginatedList = distShopSvcDao.selectByFilter(paginatedFilter);
		List<DistShopSvc> distShopSvcList = paginatedList.getRows();
		for (DistShopSvc distShopSvc : distShopSvcList) {
			setDistShopSvc(distShopSvc);
		}
		return distShopSvcDao.selectByFilter(paginatedFilter);
	}

	@Override
	public DistShopSvc getDistShopSvcById(Long id) {
		return distShopSvcDao.selectById(id);
	}

	private void setDistShopSvc(DistShopSvc distShopSvc) {
		Svcx svcx = svcxDao.selectById(distShopSvc.getSvcId());
		if (svcx != null) {
			distShopSvc.setFileBrowseUrl(getFileBrowseUrl(svcx.getImagePath(), svcx.getImageUsage()));
			String iconUsage = svcx.getIconUsage();
			distShopSvc.setSalePrice(svcx.getSalePrice());
			distShopSvc.setSvcName(svcx.getName());
			distShopSvc.setDesc(svcx.getDesc());
			distShopSvc.setFileBrowseUrlApp(getFileBrowseUrl(svcx.getIconPathApp(), iconUsage));
			distShopSvc.setFileBrowseUrlIcon(getFileBrowseUrl(svcx.getIconPath(), iconUsage));
			distShopSvc.setFileBrowseUrlIcon2(getFileBrowseUrl(svcx.getIconPath2(), iconUsage));
		}
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
	public DistShopSvc getDistShopSvcByDistShopIdAndSvcId(Integer distShopId, Integer svcId) {
		return distShopSvcDao.selectByDistShopIdAndSvcId(distShopId, svcId);
	}

	@Override
	public List<DistShopSvc> getDistShopSvcsByFilter(MapContext requestData) {
		List<DistShopSvc> distScxList = distShopSvcDao.selectByFilter(requestData);
		if (distScxList != null && distScxList.size() > 0) {
			for (DistShopSvc distShopSvc : distScxList) {
				setDistShopSvc(distShopSvc);
			}
		}
		return distScxList;
	}

	@Override
	public PaginatedList<DistShop> getDistShopsByLatestChanges(PaginatedFilter pager) {
		return distShopDao.selectByLatestChanges(pager);
	}

	@Override
	public boolean updateDistShopAsIndexed(Integer id, Date newIndexTime) {
		return newIndexTime != null && distShopDao.updateAsIndexed(id, newIndexTime) > 0;
	}

	@Override
	public boolean updateChangeTime(Integer id, boolean updateOrNot) {
		return updateOrNot && distShopDao.updateChangeTime(id) > 0;
	}

	// ---------------------------------卫星店结算相关-------------------------------------------

	@Override
	public PaginatedList<DistSettleRec> selectDistSettleRecByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<DistSettleRec> statisList = distSettleRecDao.selectByFilter(paginatedFilter);
		return statisList;
	}

	@Override
	public List<SaleOrder> selectSaleOrderByDistSettleRecId(Integer id) {
		List<SaleOrder> list = saleOrderDao.selectBySettleRecId2Dist(id);
		if (list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				SaleOrder saleOrder = list.get(0);
				if (saleOrder == null) {
					list.remove(i);
					i--;
				} else {
					saleOrder.setSaleOrderSvcs(saleOrderSvcDao.selectByOrderId(saleOrder.getId()));
				}
			}
		}
		return list;
	}

	@Override
	public boolean updateDistShopSettleByOrderIds(List<Long> orderIds, DistSettleRec distSettleRec) {
		boolean result = true;
		if (orderIds.size() < 0) {
			result = false;
		} else {
			SaleOrder saleOrder = saleOrderDao.selectById(orderIds.get(0));
			// 封装结算信息
			distSettleRec.setTheCount(orderIds.size());
			distSettleRec.setShopId(saleOrder.getShopId());
			distSettleRec.setShopName(saleOrder.getShopName());
			distSettleRec.setDistributorId(saleOrder.getDistributorId());
			distSettleRec.setDistShopName(saleOrder.getDistShopName());
			distSettleRec.setDistributorName(saleOrder.getDistributorName());
			// 添加结算记录
			distSettleRecDao.insert(distSettleRec);
			// 批量更新订单信息
			for (Long orderId : orderIds) {
				SaleOrder saleOrder1 = saleOrderDao.selectById(orderId);
				saleOrder1.setSettleRecId2Dist(distSettleRec.getId());
				saleOrderDao.update(saleOrder1);
			}
		}
		return result;
	}

	@Override
	public boolean updateDistShopSettleByOrderId(Long orderId, DistSettleRec distSettleRec) {
		List<Long> orderIds = new ArrayList<Long>();
		orderIds.add(orderId);
		return updateDistShopSettleByOrderIds(orderIds, distSettleRec);
	}
}
