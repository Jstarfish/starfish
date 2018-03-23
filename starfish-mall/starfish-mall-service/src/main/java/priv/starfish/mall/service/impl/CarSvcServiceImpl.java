package priv.starfish.mall.service.impl;

import org.springframework.stereotype.Service;
import priv.starfish.common.base.TargetJudger;
import priv.starfish.common.jms.SimpleMessage;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.model.Pagination;
import priv.starfish.common.repo.FileRepository;
import priv.starfish.common.util.CollectionUtil;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.cart.dto.MiscAmountInfo;
import priv.starfish.mall.dao.shop.DistShopSvcDao;
import priv.starfish.mall.dao.shop.ShopSvcDao;
import priv.starfish.mall.dao.svcx.*;
import priv.starfish.mall.service.BaseConst;
import priv.starfish.mall.service.CarSvcService;
import priv.starfish.mall.shop.entity.ShopSvc;
import priv.starfish.mall.svcx.entity.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Service("carSvcService")
public class CarSvcServiceImpl extends BaseServiceImpl implements CarSvcService {

	@Resource
	SvcGroupDao svcGroupDao;

	@Resource
	SvcxDao svcxDao;

	@Resource
	FileRepository fileRepository;

	@Resource
	SvcKindDao svcKindDao;

	@Resource
	SvcPackDao svcPackDao;

	@Resource
	SvcPackItemDao svcPackItemDao;

	@Resource
	ShopSvcDao shopSvcDao;

	@Resource
	DistShopSvcDao distShopSvcDao;

	private void sendShopChangeQueueMessage(Integer shopId) {
		System.out.println("*************************开始发送消息(shop)****************************");
		SimpleMessage messageToSend = SimpleMessage.newOne();
		messageToSend.subject = BaseConst.TaskFocus.SHOP;
		messageToSend.key = shopId;
		simpleMessageSender.sendQueueMessage(BaseConst.QueueNames.TASK, messageToSend);
		System.out.println("*************************发送消息完毕(shop)****************************");
	}

	// ---------------------------------------车辆服务分组--------------------------------------------

	@Override
	public List<SvcGroup> getCarSvcGroups() {
		List<SvcGroup> listCarSvcGroup = svcGroupDao.select();
		if (null != listCarSvcGroup && listCarSvcGroup.size() > 0) {
			for (SvcGroup carSvcGroup : listCarSvcGroup) {
				carSvcGroup.setCarSvcList(svcxDao.selectByGroupIdAndState(carSvcGroup.getId(), false));
			}

		}
		return listCarSvcGroup;
	}

	@Override
	public List<SvcGroup> getCarSvcGroupsFrout() {
		List<SvcGroup> listCarSvcGroup = svcGroupDao.selectFront();
		if (null != listCarSvcGroup && listCarSvcGroup.size() > 0) {
			for (SvcGroup carSvcGroup : listCarSvcGroup) {
				List<Svcx> listCarSvc = svcxDao.selectByGroupIdAndState(carSvcGroup.getId(), false);
				if (listCarSvc != null && listCarSvc.size() > 0) {
					for (Svcx svcx : listCarSvc) {
						//
						setSvcImg(svcx);
					}
				}
				carSvcGroup.setCarSvcList(listCarSvc);
			}
		}
		return listCarSvcGroup;
	}

	@Override
	public boolean saveCarSvcGroup(SvcGroup carSvcGroup) {
		carSvcGroup.setDeleted(false);
		return svcGroupDao.insert(carSvcGroup) > 0;
	}

	@Override
	public boolean updateCarSvcGroup(SvcGroup carSvcGroup) {
		List<Svcx> listCarSvc = svcxDao.selectByGroupIdAndState(carSvcGroup.getId(), null);
		if (listCarSvc != null && listCarSvc.size() > 0) {
			svcxDao.updateByGroupIdAndState(carSvcGroup.getDisabled(), carSvcGroup.getId());
		}
		return svcGroupDao.update(carSvcGroup) > 0;
	}

	@Override
	public boolean updateSvcGroupFordeleted(Integer id) {
		// svcxDao.updateForDeleteByGroupId(id);
		return svcGroupDao.updateForDelete(id) > 0;
	}

	@Override
	public PaginatedList<SvcGroup> getCarSvcGroupsByFilter(PaginatedFilter paginatedFilter) {
		Pagination pagination = paginatedFilter.getPagination();
		pagination.setPageSize(100);
		PaginatedList<SvcGroup> paginatedList = svcGroupDao.selectByFilter(paginatedFilter);
		List<SvcGroup> listGroup = paginatedList.getRows();
		if (listGroup != null && listGroup.size() > 0) {
			for (SvcGroup svcGroup : listGroup) {
				SvcKind svcKind = svcKindDao.selectById(svcGroup.getKindId());
				if (svcKind != null) {
					svcGroup.setKindName(svcKind.getName());
				}
			}
		}
		return paginatedList;
	}

	// ---------------------------------------车辆服务--------------------------------------------

	@Override
	public List<Svcx> getCarSvcsByGroupId(Integer groupId) {
		return svcxDao.selectByGroupIdAndState(groupId, null);
	}

	@Override
	public boolean saveCarSvc(Svcx carSvc) {
		carSvc.setDeleted(false);
		Integer groupId = carSvc.getGroupId();
		if (groupId != null) {
			SvcGroup svcGroup = svcGroupDao.selectById(groupId);
			if (svcGroup != null) {
				carSvc.setKindId(svcGroup.getKindId());
				carSvc.setGrounpName(svcGroup.getName());
			}
		}
		return svcxDao.insert(carSvc) > 0;
	}

	@Override
	public boolean updateCarSvc(Svcx carSvc) {
		return svcxDao.update(carSvc) > 0;
	}

	@Override
	public boolean updateCarSvcForDeleted(Integer id) {
		boolean ok = svcxDao.updateForDelete(id) > 0;
		if (ok) {
			// 删除卫星店服务
			ok = ok && distShopSvcDao.deleteBySvcId(id) > 0;
			// 删除店铺服务
			ok = ok && deleteShopSvcBySvcId(id);
		}
		if (ok) {
			this.sendShopChangeQueueMessage(id);
		}
		return ok;
	}

	@Override
	public PaginatedList<Svcx> getCarSvcsByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<Svcx> paginatedList = svcxDao.selectByFilter(paginatedFilter);
		//
		List<Svcx> listCarSvc = paginatedList.getRows();
		// 检查是否为店铺选择服务列表
		MapContext filters = paginatedFilter.getFilterItems();
		Integer shopId = filters.getTypedValue("shopId", Integer.class);
		//
		if (null != listCarSvc && listCarSvc.size() > 0) {
			for (Svcx carSvc : listCarSvc) {
				carSvc.setCarSvcGroup(svcGroupDao.selectById(carSvc.getGroupId()));
				setSvcImg(carSvc);
				// 设置服务是否存在于当前店铺的标志
				if (shopId != null) {
					Integer svcId = carSvc.getId();
					ShopSvc shopSvc = shopSvcDao.selectByShopIdAndSvcId(shopId, svcId);
					if (shopSvc != null) {
						carSvc.setExistFlag(true);
					}
					//
				}
			}
		}
		return paginatedList;
	}

	@Override
	public boolean updateSvcForDeleteByGroupId(Integer groupId) {
		return svcxDao.updateForDeleteByGroupId(groupId) > 0;
	}

	@Override
	public boolean existCarSvcGroupByNameAndKindId(String groupName, Integer kindId) {
		return svcGroupDao.selectCountByNameAndKindId(groupName, kindId) > 0;
	}

	@Override
	public List<Svcx> getCarSvcsByGroupIdAndState(Integer groupId) {
		return svcxDao.selectByGroupIdAndState(groupId, null);
	}

	@Override
	public Svcx getCarSvc(Integer id) {
		Svcx svcx = svcxDao.selectById(id);
		if (svcx != null) {
			setSvcImg(svcx);
		}
		return svcx;
	}

	@Override
	public List<Svcx> getCarSvcs() {
		// 排序
		List<Svcx> listSvcx = svcxDao.select();
		if (listSvcx != null && listSvcx.size() > 0) {
			for (Svcx svcx : listSvcx) {
				setSvcImg(svcx);
			}
		}
		return listSvcx;
	}

	// 图片配置
	private void setSvcImg(Svcx svcx) {
		if (svcx != null) {
			svcx.setFileBrowseUrl(getFileBrowseUrl(svcx.getImagePath(), svcx.getImageUsage()));
			//
			String iconUsage = svcx.getIconUsage();
			svcx.setFileBrowseUrlApp(getFileBrowseUrl(svcx.getIconPathApp(), iconUsage));
			svcx.setFileBrowseUrlIcon(getFileBrowseUrl(svcx.getIconPath(), iconUsage));
			svcx.setFileBrowseUrlIcon2(getFileBrowseUrl(svcx.getIconPath2(), iconUsage));
		}
	}

	@Override
	public List<SvcKind> getSvcKindsByFilter() {
		//
		return svcKindDao.selectByFilter();
	}

	@Override
	public boolean saveSvcPack(SvcPack svcPack) {
		// 业务逻辑
		if (svcPack != null) {
			svcPack.setDisabled(true);
			svcPack.setDeleted(false);
			boolean ok = svcPackDao.insert(svcPack) > 0;
			if (ok) {
				List<SvcPackItem> listItem = svcPack.getPackItemList();
				if (listItem != null) {
					for (SvcPackItem svcPackItem : listItem) {
						// 放入套餐id
						svcPackItem.setPackId(svcPack.getId());
						saveSvcPackItem(svcPackItem);
					}
				}
			}
			return ok;
		} else {
			return false;
		}
	}

	@Override
	public boolean updateSvcPack(SvcPack svcPack) {
		// 业务逻辑
		if (svcPack != null) {
			// 从数据库取出套餐明细
			List<SvcPackItem> dbItemList = svcPackItemDao.selectByPackId(svcPack.getId());
			// 从客户端提交的新数据
			List<SvcPackItem> itemList = svcPack.getPackItemList();
			if (itemList != null && itemList.size() > 0) {
				//
				if (dbItemList != null && dbItemList.size() > 0) {
					for (int i = 0; i < dbItemList.size();) {
						final SvcPackItem dbItem = dbItemList.get(i);
						// 查找对应的新数据索引
						int srcIndex = CollectionUtil.searchIndex(itemList, new TargetJudger<SvcPackItem>() {
							@Override
							public boolean isTarget(SvcPackItem toBeChecked) {
								// 按唯一键判断
								return toBeChecked.getPackId().equals(dbItem.getPackId()) && toBeChecked.getSvcId().equals(dbItem.getSvcId());
							}
						});
						// 处理更新
						if (srcIndex != -1) {
							SvcPackItem srcItem = itemList.get(srcIndex);
							// 合并数据更新
							dbItem.setRate(srcItem.getRate() / 100);
							svcPackItemDao.update(dbItem);
							// 删除db和客户端
							dbItemList.remove(i);
							itemList.remove(srcIndex);
						} else {
							i++;
						}
					}
					// dbItemList中剩余的都是需要删除的
					for (int i = 0; i < dbItemList.size(); i++) {
						SvcPackItem dbItem = dbItemList.get(i);
						// 从数据库中删除...
						svcPackItemDao.deleteById(dbItem.getId());
					}
				}

				// itemList中剩余的都是需要新增的
				for (int i = 0; i < itemList.size(); i++) {
					SvcPackItem srcItem = itemList.get(i);
					// 添加到数据库中...
					srcItem.setPackId(svcPack.getId());
					saveSvcPackItem(srcItem);
				}
			}
		}
		return svcPackDao.update(svcPack) > 0;
	}

	@Override
	public SvcPack getSvcPackById(Integer packId) {
		SvcPack svcPack = svcPackDao.selectById(packId);
		if (svcPack != null) {
			svcPack.setPackItemList(getSvcPackItemsByPackId(svcPack.getId()));
			svcPack.setFileBrowseUrl(getFileBrowseUrl(svcPack.getImagePath(), svcPack.getImageUsage()));
			svcPack.setAmountInfo(getMiscAmount(svcPack.getPackItemList()));
		}
		return svcPack;
	}

	@Override
	public PaginatedList<SvcPack> getSvcPacksByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<SvcPack> paginatedList = svcPackDao.selectByFilter(paginatedFilter);
		List<SvcPack> listCarSvc = paginatedList.getRows();
		if (null != listCarSvc && listCarSvc.size() > 0) {
			// 业务处理
			for (SvcPack svcPack : listCarSvc) {
				List<SvcPackItem> svcPackItems = getSvcPackItemsByPackId(svcPack.getId());
				svcPack.setPackItemList(svcPackItems);
				MiscAmountInfo amountInfo = getMiscAmount(svcPackItems);
				svcPack.setAmountInfo(amountInfo);
				svcPack.setFileBrowseUrl(getFileBrowseUrl(svcPack.getImagePath(), svcPack.getImageUsage()));
			}
		}
		return paginatedList;
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
	public List<SvcPackItem> getSvcPackItemsByPackId(Integer packId) {
		List<SvcPackItem> itemList = svcPackItemDao.selectByPackId(packId);
		if (itemList != null && itemList.size() > 0) {
			for (SvcPackItem svcPackItem : itemList) {
				Svcx svcx = svcxDao.selectById(svcPackItem.getSvcId());
				if (svcx != null) {
					svcPackItem.setSvcDesc(svcx.getDesc());
					BigDecimal salePrice = svcx.getSalePrice();
					svcPackItem.setSvcSalePrice(salePrice);
					svcPackItem.setFileBrowseUrl(getFileBrowseUrl(svcx.getImagePath(), svcx.getImageUsage()));
					String iconUsage = svcx.getIconUsage();
					svcPackItem.setFileBrowseUrlApp(getFileBrowseUrl(svcx.getIconPathApp(), iconUsage));
					svcPackItem.setFileBrowseUrlIcon(getFileBrowseUrl(svcx.getIconPath(), iconUsage));
					svcPackItem.setFileBrowseUrlIcon2(getFileBrowseUrl(svcx.getIconPath2(), iconUsage));

					//
					MiscAmountInfo amountInfo = new MiscAmountInfo();
					BigDecimal amount = new BigDecimal(0.00);// 应付
					//
					amount = salePrice.multiply(new BigDecimal(svcPackItem.getRate()));
					amountInfo.setAmount(amount);
					amountInfo.setSaleAmount(svcPackItem.getSvcSalePrice());
					amountInfo.setDiscAmount(salePrice.subtract(amount));
					amountInfo.setSettlePrice(salePrice);// 暂定售价
					//
					svcPackItem.setAmountInfo(amountInfo);
				}
			}
		}
		return itemList;
	}

	@Override
	public boolean saveSvcPackItem(SvcPackItem svcPackItem) {
		boolean ok = false;
		if (svcPackItem != null) {
			Integer svcId = svcPackItem.getSvcId();
			Svcx svcx = svcxDao.selectById(svcId);
			if (svcx != null) {
				Double rate = svcPackItem.getRate();
				svcPackItem.setRate(rate / 100);
				svcPackItem.setSvcName(svcx.getName());
				svcPackItem.setShopId(-1);
				svcPackItem.setTs(new Date());
				svcPackItem.setSvcKindId(svcx.getKindId());
				ok = svcPackItemDao.insert(svcPackItem) > 0;
			}
		}
		return ok;
	}

	@Override
	public boolean updateSvcPackItem(SvcPackItem svcPackItem) {
		//
		return svcPackItemDao.update(svcPackItem) > 0;
	}

	@Override
	public boolean updateSvcPackForDeleted(SvcPack svcPack) {
		//
		return svcPackDao.update(svcPack) > 0;
	}

	@Override
	public boolean updateSvcPackForDisabled(SvcPack svcPack) {
		SvcPack svcPack2 = svcPackDao.selectById(svcPack.getId());
		if (svcPack2 != null) {
			Date pubTime = svcPack2.getPubTime();
			if (pubTime == null) {
				svcPack.setPubTime(new Date());
			}
		}
		return svcPackDao.update(svcPack) > 0;
	}

	@Override
	public boolean existSvcPackKindIdByName(Integer kindId, String name) {
		SvcPack svcPack = svcPackDao.selectByKindIdAndName(kindId, name);
		boolean exist = false;
		if (svcPack != null) {
			exist = true;
		}
		return exist;
	}

	// ---------------------------------店铺服务--------------------------------------------------

	@Override
	public PaginatedList<ShopSvc> getShopCarSvcsByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<ShopSvc> shopSvcPageList = shopSvcDao.selectByFilter(paginatedFilter);
		List<ShopSvc> shopSvcList = shopSvcPageList.getRows();
		// 设置服务对象信息
		for (ShopSvc shopSvc : shopSvcList) {
			Integer svcId = shopSvc.getSvcId();
			Svcx svc = svcxDao.selectById(svcId);
			Integer groupId = svc.getGroupId();
			SvcGroup svcGroup = svcGroupDao.selectById(groupId);
			svc.setCarSvcGroup(svcGroup);
			setSvcImg(svc);
			shopSvc.setSvc(svc);
			shopSvc.setSvcName(svc.getName());
			shopSvc.setSalePrice(svc.getSalePrice());
			shopSvc.setDesc(svc.getDesc());
		}
		//
		return shopSvcPageList;
	}

	@Override
	public boolean saveShopSvcBySvcIdAndShopId(Integer svcId, Integer shopId) {
		ShopSvc shopSvc = new ShopSvc();
		shopSvc.setShopId(shopId);
		//
		shopSvc.setSvcId(svcId);
		Svcx svc = svcxDao.selectById(svcId);
		String svcName = svc.getName();
		Integer kindId = svc.getKindId();
		//
		shopSvc.setSvcName(svcName);
		shopSvc.setKindId(kindId);
		//
		int count = shopSvcDao.insert(shopSvc);
		return count > 0;
	}

	@Override
	public boolean deleteShopSvcBySvcIdAndShopId(Integer svcId, Integer shopId) {
		boolean success = shopSvcDao.deleteByShopIdAndSvcId(svcId, shopId) > 0;
		if (success) {
			this.sendShopChangeQueueMessage(shopId);
		}
		return success;
	}

	@Override
	public boolean deleteShopSvcById(Long shopSvcId) {
		int count = shopSvcDao.deleteById(shopSvcId);
		return count > 0;
	}

	@Override
	public List<SvcPack> getSvcPacksByFilter(MapContext requestData) {
		List<SvcPack> listCarSvc = svcPackDao.selectByFilter(requestData);
		if (null != listCarSvc && listCarSvc.size() > 0) {
			// 业务处理
			for (SvcPack svcPack : listCarSvc) {
				svcPack.setPackItemList(getSvcPackItemsByPackId(svcPack.getId()));
				svcPack.setFileBrowseUrl(getFileBrowseUrl(svcPack.getImagePath(), svcPack.getImageUsage()));
				//
				svcPack.setAmountInfo(getMiscAmount(svcPack.getPackItemList()));
			}
		}
		return listCarSvc;
	}

	private MiscAmountInfo getMiscAmount(List<SvcPackItem> items) {
		BigDecimal settlePrice = new BigDecimal(0.00);// 结算金额
		BigDecimal amount = new BigDecimal(0.00);// 应付
		BigDecimal saleAmount = new BigDecimal(0.00);// 销售总金额
		BigDecimal discAmount = new BigDecimal(0.00);// 总扣减
		if (items != null && items.size() > 0) {
			for (SvcPackItem svcPackItem : items) {
				//
				MiscAmountInfo miscAmountInfo = svcPackItem.getAmountInfo();
				if (miscAmountInfo != null) {
					//
					BigDecimal svcAmount = miscAmountInfo.getAmount();
					amount = amount.add(svcAmount);
					//
					BigDecimal svcSaleAmount = miscAmountInfo.getSaleAmount();
					saleAmount = saleAmount.add(svcSaleAmount);
					//
					BigDecimal svcDiscAmount = miscAmountInfo.getDiscAmount();
					discAmount = discAmount.add(svcDiscAmount);
					//
					BigDecimal svcSettlePrice = miscAmountInfo.getSettlePrice();
					settlePrice = settlePrice.add(svcSettlePrice);
				}
			}
		}
		MiscAmountInfo amountInfo = new MiscAmountInfo();
		amountInfo.setAmount(amount);
		amountInfo.setSaleAmount(saleAmount);
		amountInfo.setSettlePrice(settlePrice);
		amountInfo.setDiscAmount(discAmount);
		return amountInfo;
	}

	@Override
	public List<Svcx> getCarSvcsByShopId(Integer shopId) {
		List<Svcx> listSvcx = svcxDao.selectByShopId(shopId);
		if (listSvcx != null && listSvcx.size() > 0) {
			for (Svcx svcx : listSvcx) {
				setSvcImg(svcx);
			}
		}
		return listSvcx;
	}

	@Override
	public List<ShopSvc> getShopSvcsByFilter(MapContext requestData) {
		List<ShopSvc> listShopSvc = shopSvcDao.selectByFilter(requestData);
		if (listShopSvc != null && listShopSvc.size() > 0) {
			for (ShopSvc shopSvc : listShopSvc) {
				shopSvc.setSvc(getCarSvc(shopSvc.getSvcId()));
			}
		}
		return shopSvcDao.selectByFilter(requestData);
	}

	@Override
	public boolean deleteShopSvcBySvcId(Integer svcId) {
		//
		return shopSvcDao.deleteBySvcId(svcId) > 0;
	}

	@Override
	public boolean existCarSvcByGroupIdAndName(Integer groupId, String name) {
		SvcGroup svcGroup = svcGroupDao.selectById(groupId);
		boolean exist = false;
		if (svcGroup != null) {
			Svcx svc = svcxDao.selectByKindIdAndName(svcGroup.getKindId(), name);
			if (svc != null) {
				exist = true;
			}
		}
		return exist;
	}
}
