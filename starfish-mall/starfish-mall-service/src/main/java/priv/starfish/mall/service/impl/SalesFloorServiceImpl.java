package priv.starfish.mall.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.dao.goods.GoodsAlbumImgDao;
import priv.starfish.mall.dao.goods.ProductAlbumImgDao;
import priv.starfish.mall.dao.goods.ProductDao;
import priv.starfish.mall.goods.entity.GoodsAlbumImg;
import priv.starfish.mall.goods.entity.Product;
import priv.starfish.mall.goods.entity.ProductAlbumImg;
import priv.starfish.mall.dao.market.SalesFloorDao;
import priv.starfish.mall.dao.market.SalesRegionDao;
import priv.starfish.mall.dao.market.SalesRegionGoodsDao;
import priv.starfish.mall.dao.market.SalesRegionSvcDao;
import priv.starfish.mall.market.entity.SalesFloor;
import priv.starfish.mall.market.entity.SalesRegion;
import priv.starfish.mall.market.entity.SalesRegionGoods;
import priv.starfish.mall.market.entity.SalesRegionSvc;
import priv.starfish.mall.service.GoodsService;
import priv.starfish.mall.service.SalesFloorService;
import priv.starfish.mall.dao.svcx.SvcxDao;
import priv.starfish.mall.svcx.entity.Svcx;

@Service("salesFloorService")
public class SalesFloorServiceImpl extends BaseServiceImpl implements SalesFloorService {

	@Resource
	SalesFloorDao salesFloorDao;
	
	@Resource
	SalesRegionDao salesRegionDao;

	@Resource
	SalesRegionGoodsDao salesRegionGoodsDao;

	@Resource
	SalesRegionSvcDao salesRegionSvcDao;

	@Resource
	SvcxDao svcxDao;

	@Resource
	ProductDao productDao;

	@Resource
	ProductAlbumImgDao productAlbumImgDao;

	@Resource
	GoodsAlbumImgDao goodsAlbumImgDao;

	@Resource
	GoodsService goodsService;
	
	@Override
	public SalesFloor getSalesFloorByNo(Integer no) {
		return salesFloorDao.selectById(no);
	}
	
	@Override
	public SalesFloor getSalesFloorByName(String name) {
		return salesFloorDao.selectByName(name);
	}

	@Override
	public boolean saveSalesFloor(SalesFloor salesFloor) {
		if (salesFloor.getId() == null) {
			//
			if (salesFloor.getDisabled() == null) {
				salesFloor.setDisabled(false);
			}
			if (salesFloor.getRegionCount() == null) {
				salesFloor.setRegionCount(0);
			}
			return this.salesFloorDao.insert(salesFloor) > 0;
		} else {
			return this.salesFloorDao.update(salesFloor) > 0;
		}
		
	}

	@Override
	public boolean delSalesFloor(Integer no) {
		return salesFloorDao.deleteById(no) > 0;
	}

	@Override
	public PaginatedList<SalesFloor> getSalesFloorsByFilter(PaginatedFilter paginatedFilter) {
		return salesFloorDao.selectSalesFloors(paginatedFilter);
	}

	@Override
	public List<SalesFloor> getSalesFloorByType(Integer type) {
		return salesFloorDao.selectByType(type);
	}
	
	//--------------------------销售专区-----------------------------
	@Override
	public SalesRegion getSalesRegionById(Integer id) {
		return salesRegionDao.selectById(id);
	}

	@Override
	public boolean saveSalesRegion(SalesRegion salesRegion) {
		boolean flag = false;
		//
		if (salesRegion.getId() == null) {
			flag = this.salesRegionDao.insert(salesRegion) > 0;
			if (flag) {
				SalesFloor salesFloor = salesFloorDao.selectById(salesRegion.getFloorNo());
				salesFloor.setRegionCount(salesFloor.getRegionCount() + 1);
				salesFloorDao.update(salesFloor);
			}
			return flag;
		} else {
			return this.salesRegionDao.update(salesRegion) > 0;
		}
	}

	@Override
	public boolean delSalesRegionById(Integer id) {
		return salesRegionDao.deleteById(id) > 0;
	}

	@Override
	public boolean delSalesRegionByIdAndType(SalesRegion salesRegion) {
		boolean flag = false;
		//
		Integer id = salesRegion.getId();
		//
		SalesRegion DBSalesRegion = salesRegionDao.selectById(id);
		if (DBSalesRegion != null) {
			if (salesRegion.getType() == 0) {
				salesRegionGoodsDao.deleteByRegionId(id);
			} else {
				salesRegionSvcDao.deleteByRegionId(id);
			}
			//
			flag = salesRegionDao.deleteById(id) > 0;
			if (flag) {
				SalesFloor salesFloor = salesFloorDao.selectById(DBSalesRegion.getFloorNo());
				salesFloor.setRegionCount(salesFloor.getRegionCount() - 1);
				salesFloorDao.update(salesFloor);
			}
		}

		//
		return flag;
	}

	@Override
	public PaginatedList<SalesRegion> getSalesRegionsByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<SalesRegion> salesRegionData = salesRegionDao.selectSalesRegions(paginatedFilter);
		List<SalesRegion> listSalesRegion = salesRegionData.getRows();
		if (listSalesRegion != null) {
			for (SalesRegion salesRegion : listSalesRegion) {
				SalesFloor salesFloor = salesFloorDao.selectById(salesRegion.getFloorNo());
				salesRegion.setSalesFloor(salesFloor);
			}
		}
		salesRegionData.setRows(listSalesRegion);
		return salesRegionData;
	}

	@Override
	public List<SalesRegion> getSalesRegionByType(Integer type) {
		return salesRegionDao.selectByType(type);
	}

	@Override
	public PaginatedList<SalesRegionGoods> getSalesRegionGoodsByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<SalesRegionGoods> salesRegionGoods = salesRegionGoodsDao.selectSalesRegionGoods(paginatedFilter);
		List<SalesRegionGoods> listSalesRegionGoods = salesRegionGoods.getRows();
		if (listSalesRegionGoods != null) {
			for (SalesRegionGoods salesRegionGood : listSalesRegionGoods) {
				Product product = productDao.selectById(salesRegionGood.getProductId());
				salesRegionGood.setProduct(product);
			}
		}
		salesRegionGoods.setRows(listSalesRegionGoods);
		return salesRegionGoods;
	}

	@Override
	public PaginatedList<Product> getProductsAndFilter(PaginatedFilter paginatedFilter) {
		//
		PaginatedList<Product> products = goodsService.getProductsByFilter(paginatedFilter);
		List<Product> listProducts = products.getRows();
		//
		PaginatedList<SalesRegionGoods> salesRegionGoods = salesRegionGoodsDao.selectSalesRegionGoods(paginatedFilter);
		List<SalesRegionGoods> listSalesRegionGoods = salesRegionGoods.getRows();
		//
		for (int i = listProducts.size() - 1; i >= 0; i--) {
			for (int j = 0; j < listSalesRegionGoods.size(); j++) {
				//
				if (listSalesRegionGoods.get(j).getProductId().equals(listProducts.get(i).getId())) {
					listProducts.remove(i);
					break;
				}

			}

		}
		products.setRows(listProducts);

		return products;
	}

	@Override
	public boolean saveRegionGoods(List<String> list, Integer regionId) {
		boolean flag = true;
		//
		SalesRegionGoods salesRegionGoods = new SalesRegionGoods();
		//
		for (int i = 0; i < list.size(); i++) {
			Integer productId = Integer.parseInt(list.get(i));
			salesRegionGoods.setProductId(productId.longValue());
			salesRegionGoods.setRegionId(regionId);
			Product product = productDao.selectById(productId.longValue());
			salesRegionGoods.setGoodsId(product.getGoodsId());
			//
			flag = salesRegionGoodsDao.insert(salesRegionGoods) > 0 && flag;
		}
		return flag;
	}

	@Override
	public boolean delSalesRegionGoodsById(Integer id) {
		return salesRegionGoodsDao.deleteById(id) > 0;
	}

	@Override
	public PaginatedList<SalesRegionSvc> getSalesRegionSvcsByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<SalesRegionSvc> salesRegionSvcs = salesRegionSvcDao.selectSalesRegionSvc(paginatedFilter);
		List<SalesRegionSvc> listSalesRegionSvcs = salesRegionSvcs.getRows();
		if (listSalesRegionSvcs != null) {
			for (SalesRegionSvc salesRegionSvc : listSalesRegionSvcs) {
				Svcx svcx = svcxDao.selectById(salesRegionSvc.getSvcId());
				salesRegionSvc.setSvcx(svcx);
			}
		}
		salesRegionSvcs.setRows(listSalesRegionSvcs);
		return salesRegionSvcs;
	}

	@Override
	public boolean saveRegionSvc(List<String> list, Integer regionId) {
		boolean flag = true;
		//
		SalesRegionSvc salesRegionSvc = new SalesRegionSvc();
		//
		for (int i = 0; i < list.size(); i++) {
			Integer svcId = Integer.parseInt(list.get(i));
			salesRegionSvc.setSvcId(svcId);
			salesRegionSvc.setRegionId(regionId);
			Svcx svcx = svcxDao.selectById(svcId);
			salesRegionSvc.setGroupId(svcx.getGroupId());
			;
			//
			flag = salesRegionSvcDao.insert(salesRegionSvc) > 0 && flag;
		}
		return flag;
	}

	@Override
	public boolean delSalesRegionSvcById(Integer id) {
		return salesRegionSvcDao.deleteById(id) > 0;
	}

	@Override
	public SalesRegion getSalesRegionByNameAndFloorNo(String name, Integer floorNo) {
		return salesRegionDao.selectByNameAndFloorNo(name, floorNo);
	}

	@Override
	public List<SalesRegion> getSalesRegionListByType(Integer type) {

		List<SalesRegion> salesRegionList = salesRegionDao.selectByType(type);
		if (salesRegionList != null) {
			if (type == 0) {
				for (SalesRegion salesRegion : salesRegionList) {
					//
					SalesFloor salesFloor = salesFloorDao.selectById(salesRegion.getFloorNo());
					salesRegion.setSalesFloor(salesFloor);
					//
					List<SalesRegionGoods> SalesRegionGoods = getSalesRegionGoodsByRegionId(salesRegion.getId());
					salesRegion.setSalesRegionGoods(SalesRegionGoods);
				}

			} else if (type == 1) {
				for (SalesRegion salesRegion : salesRegionList) {
					//
					SalesFloor salesFloor = salesFloorDao.selectById(salesRegion.getFloorNo());
					salesRegion.setSalesFloor(salesFloor);
					//
					List<SalesRegionSvc> SalesRegionSvc = getSalesRegionSvcByRegionId(salesRegion.getId());
					salesRegion.setSalesRegionSvc(SalesRegionSvc);
				}
			}
		}
		return salesRegionList;
	}

	private List<SalesRegionSvc> getSalesRegionSvcByRegionId(Integer id) {
		List<SalesRegionSvc> salesRegionSvcList = salesRegionSvcDao.selectByRegionId(id);
		for (SalesRegionSvc salesRegionSvc : salesRegionSvcList) {
			Svcx svcx = svcxDao.selectById(salesRegionSvc.getSvcId());
			if (svcx != null) {
				String browseUrl = fileRepository.getFileBrowseUrl(svcx.getImageUsage(), svcx.getImagePath());
				if (StrUtil.isNullOrBlank(browseUrl)) {
					browseUrl = fileRepository.getFileRepoConfig().getNoImageUrl();
				}
				svcx.setFileBrowseUrl(browseUrl);
			}
			salesRegionSvc.setSvcx(svcx);
		}
		return salesRegionSvcList;
	}

	private List<SalesRegionGoods> getSalesRegionGoodsByRegionId(Integer id) {
		List<SalesRegionGoods> salesRegionGoodsList = salesRegionGoodsDao.selectByRegionId(id);
		for (SalesRegionGoods salesRegionGoods : salesRegionGoodsList) {
			//
			Product product = productDao.selectById(salesRegionGoods.getProductId());
			salesRegionGoods.setProduct(product);
			//
			List<ProductAlbumImg> productAlbumImgs = productAlbumImgDao.selectByProductId(salesRegionGoods.getProductId());
			for (ProductAlbumImg productAlbumImg : productAlbumImgs) {
				Long imageId = productAlbumImg.getImageId();
				GoodsAlbumImg goodsAlbumImg = goodsAlbumImgDao.selectById(imageId);
				if (goodsAlbumImg != null) {
					String browseUrl = fileRepository.getFileBrowseUrl(goodsAlbumImg.getImageUsage(), goodsAlbumImg.getImagePath());
					if (StrUtil.isNullOrBlank(browseUrl)) {
						browseUrl = fileRepository.getFileRepoConfig().getNoImageUrl();
					}
					productAlbumImg.setFileBrowseUrl(browseUrl);
				}
			}
			salesRegionGoods.setProductAlbumImgs(productAlbumImgs);
		}
		return salesRegionGoodsList;
	}

}
