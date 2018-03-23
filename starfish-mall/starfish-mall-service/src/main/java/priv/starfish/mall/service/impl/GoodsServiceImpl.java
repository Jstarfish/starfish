package priv.starfish.mall.service.impl;

import org.springframework.stereotype.Service;
import priv.starfish.common.model.Couple;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.model.Pagination;
import priv.starfish.common.repo.FileRepository;
import priv.starfish.common.util.JsonUtil;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.common.util.TypeUtil;
import priv.starfish.common.util.TypeUtil.Types;
import priv.starfish.mall.categ.entity.*;
import priv.starfish.mall.comn.dict.VolumeUnit;
import priv.starfish.mall.comn.dict.WeightUnit;
import priv.starfish.mall.dao.cart.SaleCartGoodsDao;
import priv.starfish.mall.dao.categ.*;
import priv.starfish.mall.dao.goods.*;
import priv.starfish.mall.dao.market.PrmtTagDao;
import priv.starfish.mall.dao.market.PrmtTagGoodsDao;
import priv.starfish.mall.dao.order.SaleOrderGoodsDao;
import priv.starfish.mall.dao.shop.ShopProductDao;
import priv.starfish.mall.dao.statis.GoodsBrowseSumDao;
import priv.starfish.mall.dao.statis.GoodsBuySumDao;
import priv.starfish.mall.dao.svcx.SvcGroupDao;
import priv.starfish.mall.dao.svcx.SvcxDao;
import priv.starfish.mall.goods.dto.ProductDto;
import priv.starfish.mall.goods.entity.*;
import priv.starfish.mall.market.entity.PrmtTag;
import priv.starfish.mall.market.entity.PrmtTagGoods;
import priv.starfish.mall.order.entity.SaleOrderGoods;
import priv.starfish.mall.service.GoodsService;
import priv.starfish.mall.service.SaleOrderService;
import priv.starfish.mall.service.util.SxUtil;
import priv.starfish.mall.statis.entity.GoodsBuySum;

import javax.annotation.Resource;
import java.util.*;

@Service("goodsService")
public class GoodsServiceImpl extends BaseServiceImpl implements GoodsService {

	@Resource
	GoodsDao goodsDao;

	@Resource
	GoodsAttrValDao goodsAttrValDao;

	@Resource
	ProductDao productDao;

	@Resource
	ProductSpecValDao productSpecValDao;

	@Resource
	GoodsCatSpecItemDao goodsCatSpecItemDao;

	@Resource
	SpecRefDao specRefDao;

	@Resource
	GoodsAlbumImgDao goodsAlbumImgDao;

	@Resource
	GoodsIntroDao goodsIntroDao;

	@Resource
	GoodsIntroImgDao goodsIntroImgDao;

	@Resource
	ProductAlbumImgDao productAlbumImgDao;

	@Resource
	FileRepository fileRepository;

	@Resource
	SvcGroupDao carSvcGroupDao;

	@Resource
	SvcxDao carSvcDao;

	@Resource
	GoodsCatDao goodsCatDao;

	@Resource
	GoodsCatAttrDao goodsCatAttrDao;

	@Resource
	GoodsCatAttrItemDao goodsCatAttrItemDao;

	@Resource
	BrandDefDao brandDefDao;

	@Resource
	GoodsBuySumDao goodsBuySumDao;

	@Resource
	GoodsExDao goodsExDao;

	@Resource
	ProductExDao productExDao;

	@Resource
	GoodsColorImgDao goodsColorImgDao;

	@Resource
	ShopProductDao shopProductDao;

	@Resource
	SaleCartGoodsDao saleCartGoodsDao;

	@Resource
	SaleOrderGoodsDao saleOrderGoodsDao;

	@Resource
	AttrRefDao attrRefDao;

	@Resource
	SaleOrderService saleOrderService;

	@Resource
	PrmtTagGoodsDao prmtTagGoodsDao;

	@Resource
	PrmtTagDao prmtTagDao;

	@Resource
	GoodsBrowseSumDao goodsBrowseSumDao;
	// --------------------------------商品----------------------------------
	@Override
	public boolean saveGoodsBasisInfo(Goods goods) {
		GoodsCat goodsCat = goodsCatDao.selectById(goods.getCatId());
		Boolean hasSpec = goodsCat.getHasSpec();
		goods.setHasSpec(hasSpec);
		// 保存商品
		int goodsNum = goodsDao.insert(goods);
		// 保存商品属性值
		List<GoodsAttrVal> attrVals = goods.getAttrVals();
		for (GoodsAttrVal goodsAttrVal : attrVals) {
			goodsAttrVal.setGoodsId(goods.getId());
			goodsAttrValDao.insert(goodsAttrVal);
		}

		return goodsNum > 0;
	}

	@Override
	public boolean saveProduct(Product product) {
		// 保存货品
		int productNum = productDao.insert(product);

		if (productNum > 0) {
			// 保存货品规格值
			for (ProductSpecVal productSpecVal : product.getSpecVals()) {
				productSpecVal.setProductId(product.getId());
				productSpecValDao.insert(productSpecVal);
			}
			// 保存货品相册图片
			for (ProductAlbumImg img : product.getProductAlbumImgs()) {
				img.setProductId(product.getId());
				// img.setSeqNo(1);
				productAlbumImgDao.insert(img);
			}
		}

		return productNum > 0;
	}

	@Override
	public boolean batchSaveProduct(List<Product> products) {
		boolean status = true;
		for (Product product : products) {
			status = saveProduct(product);
			if (!status)
				break;
		}
		return status;
	}

	@Override
	public Goods getGoodsBasisInfoById(Integer goodsId) {
		Goods goods = goodsDao.selectById(goodsId);
		List<GoodsAttrVal> attrVals = goodsAttrValDao.selectByGoodsId(goodsId);
		goods.setAttrVals(attrVals);
		return goods;
	}

	@Override
	public List<Product> getUnShelveProductsByGoodsId(Integer goodsId) {
		List<Product> products = productDao.selectUnShelvefProductsByGoodsId(goodsId);
		products = getProductsRelatedInfo(products);
		//
		return products;
	}

	@Override
	public List<Product> getProductsByGoodsId(Integer goodsId) {
		List<Product> products = productDao.selectByGoodsIdAndDeleted(goodsId, false);
		products = getProductsRelatedInfo(products);
		//
		return products;
	}

	protected List<Product> getProductsRelatedInfo(List<Product> originalProducts) {
		List<Product> products = originalProducts;
		for (Product product : products) {
			Integer goodsId = product.getGoodsId();
			List<ProductSpecVal> productSpecVals = productSpecValDao.selectByProductIdAndGoodsId(product.getId(), goodsId);
			for (ProductSpecVal productSpecVal : productSpecVals) {
				String refCode = productSpecVal.getRefCode();
				SpecRef specRef = specRefDao.selectByCode(refCode);
				productSpecVal.setSpecRef(specRef);
				Integer specItemId = productSpecVal.getSpecItemId();
				GoodsCatSpecItem goodsCatSpecItem = goodsCatSpecItemDao.selectById(specItemId);
				productSpecVal.setGoodsCatSpecItem(goodsCatSpecItem);
			}
			product.setSpecVals(productSpecVals);
			//
			List<ProductAlbumImg> productAlbumImgs = productAlbumImgDao.selectByProductId(product.getId());
			for (int i = 0; i < productAlbumImgs.size(); i++) {
				ProductAlbumImg productImg = productAlbumImgs.get(i);
				GoodsAlbumImg goodsImg = goodsAlbumImgDao.selectById(productImg.getImageId());
				if (goodsImg != null) {
					String browseUrl = fileRepository.getFileBrowseUrl(goodsImg.getImageUsage(), goodsImg.getImagePath());
					if (StrUtil.isNullOrBlank(browseUrl)) {
						browseUrl = fileRepository.getFileRepoConfig().getNoImageUrl();
					}
					productImg.setFileBrowseUrl(browseUrl);
				} else {
					productAlbumImgs.remove(i);
				}
			}
			product.setProductAlbumImgs(productAlbumImgs);
		}

		return products;
	}

	private boolean updateGoodsBasisInfo(Goods goods) {
		//
		int countGoods = goodsDao.update(goods);
		//
		List<GoodsAttrVal> goodsAttrVals = goods.getAttrVals();
		List<GoodsAttrVal> _goodsAttrVals = goodsAttrValDao.selectByGoodsId(goods.getId());
		int countAttrVal = 0;
		for (GoodsAttrVal goodsAttrVal : goodsAttrVals) {
			if (goodsAttrVal.getId() == null || StrUtil.isNullOrBlank(goodsAttrVal.getId().toString())) {
				countAttrVal += goodsAttrValDao.insert(goodsAttrVal);
			} else {
				countAttrVal += goodsAttrValDao.update(goodsAttrVal);
				//
				Iterator<GoodsAttrVal> it = _goodsAttrVals.iterator();
				while (it.hasNext()) {
					GoodsAttrVal attrVal = it.next();
					if (attrVal.getId().equals(goodsAttrVal.getId())) {
						it.remove();
					}
				}
			}
		}
		//
		if (_goodsAttrVals.size() > 0) {
			for (GoodsAttrVal attrVal : _goodsAttrVals) {
				goodsAttrValDao.deleteById(attrVal.getId());
			}
		}

		return countGoods > 0;
	}

	@Override
	public boolean batchUpdateProduct(List<Product> products) {
		List<Product> _products = productDao.selectByGoodsIdAndDeleted(products.get(0).getGoodsId(), false);
		int countProduct = 0;
		for (Product product : products) {
			if (product.getId() == null) {
				boolean status = saveProduct(product);
				if (status)
					countProduct++;
			} else {
				for (ProductSpecVal specVal : product.getSpecVals()) {
					if (specVal.getId() == null || StrUtil.isNullOrBlank(specVal.getId().toString())) {
						productSpecValDao.insert(specVal);
					} else {
						productSpecValDao.update(specVal);
					}
				}
				countProduct += productDao.update(product);
				//
				Iterator<Product> it = _products.iterator();
				while (it.hasNext()) {
					Product _product = it.next();
					if (_product.getId().equals(product.getId())) {
						it.remove();
					}
				}
			}
		}

		if (_products.size() > 0) {
			for (Product p : _products) {
				int count = productSpecValDao.deleteByProductId(p.getId());
				if (count > 0) {
					productDao.deleteById(p.getId());
				}
			}

		}
		return countProduct > 0;
	}

	@Override
	public boolean batchSaveAlbumImgs(List<GoodsAlbumImg> albumImgs) {
		int count = 0;
		//
		for (GoodsAlbumImg goodsAlbumImg : albumImgs) {
			goodsAlbumImg.setChangeTime(new Date());
			count += goodsAlbumImgDao.insert(goodsAlbumImg);
		}
		return count > 0;
	}

	@Override
	public PaginatedList<Product> getProductsByContext(PaginatedFilter paginatedFilter) {
		PaginatedList<Product> products = productDao.selectProductsByContext(paginatedFilter);
		for (Product product : products.getRows()) {
			Long productId = product.getId();
			List<ProductAlbumImg> productAlbumImgs = productAlbumImgDao.selectByProductId(productId);
			product.setProductAlbumImgs(productAlbumImgs);
			// 设置单位
			Integer catId = product.getCatId();
			GoodsCat goodsCat = goodsCatDao.selectById(catId);
			product.setUnit(goodsCat.getUnit());
			product.setWeightUnit(goodsCat.getWeightUnit());
			product.setVolumeUnit(goodsCat.getVolumeUnit());
		}
		return products;
	}

	@Override
	public PaginatedList<Product> getProductsByBrandIdAndFilter(PaginatedFilter paginatedFilter) {
		MapContext filters = paginatedFilter.getFilterItems();
		Integer brandId = filters.getTypedValue("brandId", Integer.class);
		//
		PaginatedList<Product> products = PaginatedList.newOne();
		// 如果有品牌过滤条件时，根据品牌名称过滤
		if (brandId != null) {
			filters.remove("brandId");
			BrandDef brand = brandDefDao.selectById(brandId);
			String name = brand.getName();
			products = getProductsByBrandNameAndFilter(name, paginatedFilter);
		} else {
			products = getProductsByFilter(paginatedFilter);

		}
		//
		List<Product> productList = products.getRows();
		//
		if (productList != null) {
			// 标签信息
			for (Product product : productList) {
				List<PrmtTagGoods> prmtTagGoodsList = prmtTagGoodsDao.selectByProductId(product.getId());
				for (PrmtTagGoods prmtTagGoods : prmtTagGoodsList) {
					// 设置标签信息
					Integer tagId = prmtTagGoods.getTagId();
					PrmtTag prmtTag = prmtTagDao.selectById(tagId);
					this.filterFileBrowseUrl(prmtTag);
					prmtTagGoods.setPrmtTag(prmtTag);
				}
				product.setPrmtTagGoods(prmtTagGoodsList);
			}
			products.setRows(productList);
		}
		//
		return products;
	}

	public PaginatedList<Product> getProductsByBrandNameAndFilter(String brandName, PaginatedFilter paginatedFilter) {
		MapContext filters = paginatedFilter.getFilterItems();
		Integer catId = filters.getTypedValue("catId", Integer.class);
		List<Integer> goodsIds = new ArrayList<Integer>(0);
		//
		if (catId != null) {// 商品分类下的品牌属性是唯一的
			Integer goodsCatAttrId = goodsCatAttrDao.selectIdByCatIdAndBrandFlag(catId, true);
			//
			goodsIds = goodsAttrValDao.selectGoodsIdByAttrIdAndAttrVal(goodsCatAttrId, brandName);
		} else {
			List<Integer> goodsCatAttrIds = goodsCatAttrDao.selectIdsByBrandFlag(true);
			for (Integer goodsCatAttrId : goodsCatAttrIds) {
				List<Integer> _goodsIds = goodsAttrValDao.selectGoodsIdByAttrIdAndAttrVal(goodsCatAttrId, brandName);
				goodsIds.addAll(goodsIds.size(), _goodsIds);
			}
		}
		// 无商品id时，返回空的数据结果集
		if (goodsIds == null || goodsIds.isEmpty()) {
			PaginatedList<Product> dataList = PaginatedList.newOne();
			dataList.setRows(null);
			Pagination pagination = paginatedFilter.getPagination();
			dataList.setPagination(pagination);
			return dataList;
		}
		filters.put("goodsIds", goodsIds);
		// 查询货品及相册信息
		PaginatedList<Product> products = getProductsByFilter(paginatedFilter);
		//
		return products;
	}

	@Override
	public PaginatedList<Product> getProductsByFilter(PaginatedFilter paginatedFilter) {
		//
		PaginatedList<Product> products = productDao.selectByFilter(paginatedFilter);
		if (products != null) {
			List<Product> _products = products.getRows();
			// 设置单位
			String unit = null;
			WeightUnit weightUnit = null;
			VolumeUnit volumeUnit = null;
			if (_products != null && !_products.isEmpty()) {
				Product _product = _products.get(0);
				Integer catId = _product.getCatId();
				GoodsCat goodsCat = goodsCatDao.selectById(catId);
				if (goodsCat != null) {
					unit = goodsCat.getUnit();
					weightUnit = goodsCat.getWeightUnit();
					volumeUnit = goodsCat.getVolumeUnit();
				}
			}
			// 设置相册
			for (Product product : products.getRows()) {
				Long productId = product.getId();
				List<ProductAlbumImg> productAlbumImgs = productAlbumImgDao.selectByProductId(productId);
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
				product.setProductAlbumImgs(productAlbumImgs);
				product.setUnit(unit);
				product.setWeightUnit(weightUnit);
				product.setVolumeUnit(volumeUnit);
				long buySum = goodsBuySumDao.selectBuyCountByProductId(productId);
				product.setBuySum(buySum);
			}
		}
		//
		return products;
	}

	@Override
	public List<GoodsAlbumImg> getAlbumImgsByGoodsId(Integer goodsId) {
		List<GoodsAlbumImg> albumImgs = goodsAlbumImgDao.selectByGoodsId(goodsId);
		for (GoodsAlbumImg albumImg : albumImgs) {
			String browseUrl = fileRepository.getFileBrowseUrl(albumImg.getImageUsage(), albumImg.getImagePath());
			if (StrUtil.isNullOrBlank(browseUrl)) {
				browseUrl = fileRepository.getFileRepoConfig().getNoImageUrl();
			}
			//
			String[] pathStrs = albumImg.getImagePath().split("/");
			String fileName = pathStrs[pathStrs.length - 1];
			albumImg.setFileName(fileName);
			//
			albumImg.setFileUuid(albumImg.getImageUuid());
			//
			albumImg.setFileBrowseUrl(browseUrl);

		}

		return albumImgs;
	}

	@Override
	public boolean deleteAlbumImgsByUuids(List<String> uuids) {
		int count = 0;
		for (String uuid : uuids) {
			count += goodsAlbumImgDao.deleteByImageUuid(uuid);
		}
		return count > 0;
	}

	@Override
	public int countGoodsByCatId(Integer catId) {
		return goodsDao.selectCountByCatId(catId);
	}

	@Override
	public boolean saveGoodsIntro(GoodsIntro goodsIntro) {
		int count = goodsIntroDao.insert(goodsIntro);
		return count > 0;
	}

	@Override
	public GoodsIntro getGoodsIntroByGoodsId(Integer goodsId) {
		return goodsIntroDao.selectByGoodsId(goodsId);
	}

	@Override
	public boolean batchDownShelfProducts(List<Long> ids, Integer shelfStatus, Date date) {
		boolean ok = true;
		for (Long productId : ids) {
			ok = updateProductShelfStatus(productId, 2) && ok;
		}
		//
		return ok;
	}

	@Override
	public boolean batchUpShelfProducts(List<Long> ids, Integer shelfStatus, Date date) {
		boolean ok = true;
		for (Long productId : ids) {
			ok = updateProductShelfStatus(productId, 1) && ok;
		}
		//
		return ok;
	}

	@Override
	public List<GoodsIntroImg> getIntroImgsByGoodsId(Integer goodsId) {
		List<GoodsIntroImg> introImgs = goodsIntroImgDao.selectByGoodsId(goodsId);
		for (GoodsIntroImg img : introImgs) {
			img.setFileUuid(img.getImageUuid());
			String browseUrl = fileRepository.getFileBrowseUrl(img.getImageUsage(), img.getImagePath());
			if (StrUtil.isNullOrBlank(browseUrl)) {
				browseUrl = fileRepository.getFileRepoConfig().getNoImageUrl();
			}
			img.setFileBrowseUrl(browseUrl);
		}
		return introImgs;
	}

	@Override
	public boolean batchSaveIntroImgs(List<GoodsIntroImg> introImgs) {
		int count = 0;
		//
		for (GoodsIntroImg img : introImgs) {
			count += goodsIntroImgDao.insert(img);
		}

		return count > 0;
	}

	@Override
	public boolean deleteIntroImgByUuid(String uuid) {
		int count = goodsIntroImgDao.deleteByUuid(uuid);
		return count > 0;
	}

	@Override
	public boolean updateGoodsIntro(GoodsIntro goodsIntro) {
		int count = goodsIntroDao.update(goodsIntro);
		return count > 0;
	}

	@Override
	public boolean saveOrUpdateGoods(Goods goods) {
		boolean status = true;
		// 商品基本信息
		Integer goodsId = goods.getId();
		if (goodsId == null) {
			GoodsCat goodsCat = goodsCatDao.selectById(goods.getCatId());
			boolean hasSpec = goodsCat.getHasSpec();
			goods.setHasSpec(hasSpec);
			status = saveGoodsBasisInfo(goods) && status;
		} else {
			status = updateGoodsBasisInfo(goods) && status;
		}
		// 产品列表
		List<Product> products = goods.getProducts();
		if (products != null && products.size() > 0) {
			for (Product product : products) {
				// 设置货品title
				String title = SxUtil.makeProductTitleX(goods.getName(), product.getSpecVals());
				product.setTitle(title);
				Long id = product.getId();
				if (id == null) {
					status = saveProduct(product) && status;
				} else {
					status = updateProduct(product) && status;
				}
				// 保存货品扩展信息
				status = saveProductExInfo(product.getId()) && status;
			}
		}
		// 商品介绍
		GoodsIntro intro = goods.getGoodsIntro();
		Integer introId = intro.getId();
		if (introId == null) {
			status = saveGoodsIntro(intro) && status;
		} else {
			status = updateGoodsIntro(intro) && status;
		}
		// 保存商品扩展信息
		status = saveGoodsExInfo(goodsId) && status;
		//
		return status;
	}

	/**
	 * 保存货品扩展信息
	 * 
	 * @author guoyn
	 * @date 2015年10月29日 下午4:20:50
	 * 
	 * @param productId
	 * @return boolean
	 */
	public boolean saveProductExInfo(Long productId) {
		Boolean ok = true;
		Product product = productDao.selectById(productId);
		Integer shelfStatus = product.getShelfStatus();
		if (shelfStatus != null && shelfStatus.equals(2)) {
			productExDao.deleteById(productId);
			return ok;
		}
		// 查询货品下所有的规格值列表
		List<ProductSpecVal> specVals = productSpecValDao.selectByProductId(productId);
		// 遍历规格值列表，筛选规格参照code和规格枚举项id
		if (specVals != null && !specVals.isEmpty()) {
			ProductEx productEx = ProductEx.newOne();
			Map<String, Integer> specCodeToItemIds = new HashMap<String, Integer>();
			for (ProductSpecVal specVal : specVals) {
				specCodeToItemIds.put(specVal.getRefCode(), specVal.getSpecItemId());
			}
			// 封装ProductEx
			productEx.setId(productId);
			productEx.setGoodsId(specVals.get(0).getGoodsId());
			productEx.setSpecStr(SxUtil.toSpecificStr(specCodeToItemIds));
			productEx.setSpecJson(SxUtil.toSpecificJson(specCodeToItemIds));
			productEx.setTs(new Date());
			// 判断是新增还是更新
			ProductEx _productEx = productExDao.selectById(productId);
			int count = 0;
			if (_productEx == null) {
				count = productExDao.insert(productEx);
			} else {
				count = productExDao.update(productEx);
			}
			ok = ok && count > 0;
		}
		//
		return ok;
	}

	/**
	 * 保存商品扩展信息
	 * 
	 * @author guoyn
	 * @date 2015年10月29日 下午3:56:30
	 * 
	 * @param goodsId
	 * @return boolean
	 */
	public boolean saveGoodsExInfo(Integer goodsId) {
		Boolean ok = true;
		// 从货品-规格值中获取商品下的所有上架货品的refCode列表
		List<Long> productIds = productDao.selectIdsByShelfStatusAndGoodsId(1, goodsId);
		// 若此商品没有上架的货品时，则商品的扩展信息删除
		if (productIds == null || productIds.isEmpty()) {
			goodsExDao.deleteByGoodsId(goodsId);
			return ok;
		}
		//
		List<String> refCodes = productSpecValDao.selectRefCodeByIncludeProductIds(productIds);
		if (refCodes != null && !refCodes.isEmpty()) {
			// 遍历refCode列表,获取规格枚举值列表
			for (String refCode : refCodes) {
				List<Integer> specItemIds = productSpecValDao.selectSpecItemIdsByRefCodeAndIncludeProductIds(refCode, productIds);
				// 保存specCode下的所有specItemId信息
				GoodsEx goodsEx = GoodsEx.newOne();
				goodsEx.setGoodsId(goodsId);
				goodsEx.setSpecCode(refCode);
				goodsEx.setSpecItemIds(SxUtil.toSpecificItemIdStr(specItemIds));
				goodsEx.setTs(new Date());
				// 判断是更新还是新增
				GoodsEx _goodsEx = goodsExDao.selectByGoodsIdAndSpecCode(goodsId, refCode);
				int count = 0;
				if (_goodsEx == null) {
					count = goodsExDao.insert(goodsEx);
				} else {
					goodsEx.setId(_goodsEx.getId());
					count = goodsExDao.update(goodsEx);
				}
				ok = ok && count > 0;
			}
		}
		//
		return ok;
	}

	/**
	 * 判断某个货品在使用中的状态
	 * 
	 * @author guoyn
	 * @date 2015年11月3日 下午3:37:57
	 * 
	 * @param productId
	 * @return boolean
	 */
	private boolean isProductInUseById(Long productId) {
		// TODO:消息推送、进货业务逻辑功能需完善
		// 判断是否在销售订单中
		int saleOrderCount = saleOrderGoodsDao.selectCountByProductId(productId);
		if (saleOrderCount > 0)
			return true;

		// 判断是否在销售购物车商品中
		int saleCartCount = saleCartGoodsDao.selectCountByProductId(productId);
		if (saleCartCount > 0)
			return true;
		//
		return false;
	}

	@Override
	public boolean deleteProductRelatedById(Long id) {
		boolean ok = true;
		Product product = productDao.selectById(id);
		Integer goodsId = product.getGoodsId();
		//
		boolean inUse = isProductInUseById(id);
		if (inUse) {// 假删
			ok = updateProductDeletedById(id, true) && ok;
		} else {// 真删
				// 删除货品相册图片
			productAlbumImgDao.deleteByProductId(id);
			// 删除货品规格值
			productSpecValDao.deleteByProductId(id);
			// 删除用户浏览记录TODO

			goodsBrowseSumDao.deleteById(id);
			// 删除购买汇总记录
			goodsBuySumDao.deleteByProductId(id);
			// 删除货品
			int count = productDao.deleteById(id);
			//
			ok = count > 0 && ok;
		}
		// 更新货品、商品扩展信息
		productExDao.deleteById(id);
		saveGoodsExInfo(goodsId);
		// 检查商品下是否有货品存在
		List<Product> products = productDao.selectByGoodsIdAndDeleted(goodsId, null);
		if (products == null || products.isEmpty()) {
			deleteGoodsRelatedByGoodsId(goodsId);
		}
		//
		return ok;
	}

	@Override
	public boolean deleteProductByIds(List<Long> ids) {
		boolean result = true;
		for (Long id : ids) {
			result = result && deleteProductRelatedById(id);
		}
		return result;
	}

	@Override
	public boolean updateProduct(Product product) {
		List<ProductSpecVal> specVals = product.getSpecVals();
		List<ProductAlbumImg> imgs = product.getProductAlbumImgs();
		// 更新货品规格值
		for (ProductSpecVal specVal : specVals) {
			productSpecValDao.update(specVal);
		}
		// 货品相册图片
		for (ProductAlbumImg img : imgs) {
			if (img.getId() == null) {
				img.setProductId(product.getId());
				// img.setSeqNo(1);
				productAlbumImgDao.insert(img);
			} else {
				img.setProductId(product.getId());
				productAlbumImgDao.update(img);
			}
		}
		int count = productDao.update(product);
		return count > 0;
	}

	@Override
	public boolean deleteGoodsAlbumImgByUuid(String uuid) {
		//
		GoodsAlbumImg goodsAlbumImg = goodsAlbumImgDao.selectByImageUuid(uuid);
		//
		if (goodsAlbumImg != null) {
			Long imgId = goodsAlbumImg.getId();
			goodsColorImgDao.deleteByImgId(imgId);
		}
		//
		int count = goodsAlbumImgDao.deleteByImageUuid(uuid);
		//
		return count > 0;
	}

	@Override
	public boolean deleteProductAlbumImgById(Long id) {
		int count = productAlbumImgDao.deleteById(id);
		return count > 0;
	}

	@Override
	public List<ProductAlbumImg> getProductAlbumImgsByUuid(String uuid) {
		GoodsAlbumImg goodsImg = goodsAlbumImgDao.selectByImageUuid(uuid);
		List<ProductAlbumImg> productImgs = productAlbumImgDao.selectByImageId(goodsImg.getId());
		return productImgs;
	}

	@Override
	public List<Long> getProductAlbumImgIdsByUuid(String uuid) {
		GoodsAlbumImg goodsImg = goodsAlbumImgDao.selectByImageUuid(uuid);
		List<Long> productImgIds = productAlbumImgDao.selectIdsByImageId(goodsImg.getId());
		return productImgIds;
	}

	@Override
	public boolean deleteProductAlbumImgByIds(List<Long> ids) {
		int count = 0;
		for (Long id : ids) {
			count += productAlbumImgDao.deleteById(id);
		}
		return count > 0;
	}

	@Override
	public Product getProductById(Long productId) {
		Product product = productDao.selectById(productId);
		List<ProductSpecVal> productSpecVals = productSpecValDao.selectByProductIdAndGoodsId(product.getId(), product.getGoodsId());
		product.setSpecVals(productSpecVals);
		List<ProductAlbumImg> productAlbumImgs = productAlbumImgDao.selectByProductId(product.getId());
		for (int i = 0; i < productAlbumImgs.size(); i++) {
			ProductAlbumImg productImg = productAlbumImgs.get(i);
			GoodsAlbumImg goodsImg = goodsAlbumImgDao.selectById(productImg.getImageId());
			if (goodsImg != null) {
				String browseUrl = fileRepository.getFileBrowseUrl(goodsImg.getImageUsage(), goodsImg.getImagePath());
				if (StrUtil.isNullOrBlank(browseUrl)) {
					browseUrl = fileRepository.getFileRepoConfig().getNoImageUrl();
				}
				productImg.setFileBrowseUrl(browseUrl);
			} else {
				productAlbumImgs.remove(i);
			}
		}
		product.setProductAlbumImgs(productAlbumImgs);
		return product;
	}

	@Override
	public boolean updateProductDeletedById(Long productId, boolean deleted) {
		int count = productDao.updateDeletedById(productId, deleted);
		return count > 0;
	}

	@Override
	public List<ProductDto> getProductSaleAndMarketPrices(List<Long> productIds) {
		if (productIds == null || productIds.size() == 0)
			return null;
		// 保证productIds有值传入
		List<ProductDto> productDtos = TypeUtil.newEmptyList(ProductDto.class);
		List<Product> products = productDao.selectByIds(productIds);
		for (Product product : products) {
			ProductDto productDto = ProductDto.newOne();
			productDto.setId(product.getId());
			productDto.setSalePrice(product.getSalePrice());
			productDto.setMarketPrice(product.getMarketPrice());
			productDtos.add(productDto);
		}
		return productDtos;
	}

	@Override
	public ProductDto getProductDtoByProductId(Long productId) {
		ProductDto productDto = ProductDto.newOne();
		Product product = productDao.selectById(productId);
		// 不存在此产品
		if (product == null) {
			return null;
		}
		//
		Integer shelfStatus = product.getShelfStatus();
		// 未被上架货品
		if (!shelfStatus.equals(1)) {
			return null;
		}
		Integer goodsId = product.getGoodsId();
		productDto.setGoodsId(goodsId);
		// 货品基本信息
		if (product != null) {
			productDto.setId(productId);
			productDto.setSalePrice(product.getSalePrice());
			productDto.setMarketPrice(product.getMarketPrice());
			productDto.setTitle(product.getTitle());
			Long buySum = goodsBuySumDao.selectBuyCountByProductId(productId);
			productDto.setBuySum(buySum);
		}
		// 货品相册url集合
		List<String> imageUrls = TypeUtil.newEmptyList(String.class);
		List<Long> imageIds = productAlbumImgDao.selectImageIdsByProductId(productId);
		for (Long imageId : imageIds) {
			GoodsAlbumImg goodsImg = goodsAlbumImgDao.selectById(imageId);
			if (goodsImg != null) {
				String browseUrl = fileRepository.getFileBrowseUrl(goodsImg.getImageUsage(), goodsImg.getImagePath());
				if (StrUtil.isNullOrBlank(browseUrl)) {
					browseUrl = fileRepository.getFileRepoConfig().getNoImageUrl();
				}
				imageUrls.add(browseUrl);
			}
		}
		productDto.setAlbumImgUrls(imageUrls);
		// 商品颜色图片列表
		Map<Integer, String> goodsColorImgs = new HashMap<Integer, String>();
		List<GoodsColorImg> colorImgs = goodsColorImgDao.selectByGoodsId(goodsId);
		for (GoodsColorImg colorImg : colorImgs) {
			Long imageId = colorImg.getImageId();
			if (imageId != null) {
				GoodsAlbumImg goodsImg = goodsAlbumImgDao.selectById(imageId);
				if (goodsImg != null) {
					String browseUrl = fileRepository.getFileBrowseUrl(goodsImg.getImageUsage(), goodsImg.getImagePath());
					if (StrUtil.isNullOrBlank(browseUrl)) {
						browseUrl = fileRepository.getFileRepoConfig().getNoImageUrl();
					}
					goodsColorImgs.put(colorImg.getSpecItemId(), browseUrl);
				}
			}
		}
		productDto.setGoodsColorImgs(goodsColorImgs);
		// 商品介绍
		GoodsIntro goodsIntro = goodsIntroDao.selectByGoodsId(goodsId);
		productDto.setGoodsIntro(goodsIntro);
		// 商品属性值列表
		Map<String, String> keyAttrVals = new HashMap<String, String>();
		List<GoodsAttrVal> attrVals = goodsAttrValDao.selectByGoodsIdAndKeyFlag(goodsId, true);
		for (GoodsAttrVal attrVal : attrVals) {
			String reCode = attrVal.getRefCode();
			AttrRef attrRef = attrRefDao.selectByCode(reCode);
			if (attrRef != null) {
				String name = attrRef.getName();
				keyAttrVals.put(name, attrVal.getAttrVal());
			}
		}
		productDto.setKeyAttrVals(keyAttrVals);
		// 商品属性值列表
		Map<String, String> allAttrVals = new HashMap<String, String>();
		List<GoodsAttrVal> _allAttrVals = goodsAttrValDao.selectByGoodsId(goodsId);
		for (GoodsAttrVal _attrVal : _allAttrVals) {
			String _reCode = _attrVal.getRefCode();
			AttrRef _attrRef = attrRefDao.selectByCode(_reCode);
			if (_attrRef != null) {
				String _name = _attrRef.getName();
				allAttrVals.put(_name, _attrVal.getAttrVal());
			}
		}
		productDto.setAttrVals(allAttrVals);
		// 货品规格值
		Map<String, String> specVals = new HashMap<String, String>();
		List<ProductSpecVal> productSpecVals = productSpecValDao.selectByProductId(productId);
		for (ProductSpecVal specVal : productSpecVals) {
			String _reCode = specVal.getRefCode();
			SpecRef specRef = specRefDao.selectByCode(_reCode);
			if (specRef != null) {
				String _name = specRef.getName();
				specVals.put(_name, specVal.getSpecVal());
			}
		}
		productDto.setSpecVals(specVals);
		// 商品扩展信息
		List<GoodsEx> goodsExs = goodsExDao.selectByGoodsId(goodsId);
		if (goodsExs != null) {
			Map<String, Couple<SpecRef, List<GoodsCatSpecItem>>> goodsExMap = formGoodsExListAsMap(goodsExs);
			productDto.setGoodsEx(goodsExMap);
		}
		// 货品扩展信息
		ProductEx productEx = productExDao.selectById(productId);
		if (productEx != null) {
			Map<String, Integer> productExMap = formProductExAsMap(productEx);
			productDto.setProductEx(productExMap);
		}
		//
		return productDto;
	}

	/**
	 * 将产品扩展对象转化为key为specCode,value为specItmId的map
	 * 
	 * @author guoyn
	 * @date 2015年11月4日 下午3:39:16
	 * 
	 * @param productEx
	 * @return Map<String, Integer>
	 */
	@SuppressWarnings("unchecked")
	private Map<String, Integer> formProductExAsMap(ProductEx productEx) {
		String specJson = productEx.getSpecJson();
		Map<String, Integer> productExMap = JsonUtil.fromJson(specJson, Types.StringIntegerMap.getClass());
		return productExMap;
	}

	/**
	 * 将商品扩展信息列表转化为key为specRef,value为List<GoodsCatSpecItem>的map对象,
	 * 
	 * @author guoyn
	 * @date 2015年11月4日 下午3:11:28
	 * 
	 * @param goodsExs
	 * @return Map<SpecRef, List<GoodsCatSpecItem>>
	 */
	private Map<String, Couple<SpecRef, List<GoodsCatSpecItem>>> formGoodsExListAsMap(List<GoodsEx> goodsExs) {
		Map<String, Couple<SpecRef, List<GoodsCatSpecItem>>> goodsExMap = new HashMap<String, Couple<SpecRef, List<GoodsCatSpecItem>>>();
		//
		for (GoodsEx goodsEx : goodsExs) {
			Couple<SpecRef, List<GoodsCatSpecItem>> dataMap = Couple.newOne();
			String specCode = goodsEx.getSpecCode();
			String specItemIds = goodsEx.getSpecItemIds();
			String[] specItemIdArr = specItemIds.split(",");
			//
			SpecRef specRef = specRefDao.selectByCode(specCode);
			dataMap.setFirst(specRef);
			//
			List<GoodsCatSpecItem> goodsCatSpecItems = TypeUtil.newEmptyList(GoodsCatSpecItem.class);
			for (String specItemIdStr : specItemIdArr) {
				Integer specItemId = Integer.valueOf(specItemIdStr);
				GoodsCatSpecItem goodsCatSpecItem = goodsCatSpecItemDao.selectById(specItemId);
				goodsCatSpecItems.add(goodsCatSpecItem);
			}
			dataMap.setSecond(goodsCatSpecItems);
			// 设置到map中
			goodsExMap.put(specCode, dataMap);
		}
		return goodsExMap;
	}

	@Override
	public boolean updateProductShelfStatus(Long productId, Integer shelfStatus) {
		Product product = new Product();
		product.setId(productId);
		product.setShelfStatus(shelfStatus); // shelfStatus:上下架状态:0 - 未上架, 1 - 已上架， 2 - 已下架
		product.setShelfTime(new Date());
		product.setChangeTime(new Date());
		int count = productDao.updateShelfStatus(product);
		// 如果是下架操作，删除所有店铺和此货品的关联数据
		if (shelfStatus.equals(2)) {
			shopProductDao.deleteByProductId(productId);
		}
		if (count > 0) {
			// 同步货品扩展信息
			saveProductExInfo(productId);
			// 同步商品信息
			Product _product = productDao.selectById(productId);
			Integer goodsId = _product.getGoodsId();
			saveGoodsExInfo(goodsId);
		}
		// TODO:消息推送
		return count > 0;
	}

	@Override
	public List<GoodsCatSpecItem> getColorSpecItemsByGoodsId(Integer goodsId) {
		List<GoodsCatSpecItem> specItems = TypeUtil.newEmptyList(GoodsCatSpecItem.class);
		// 获取颜色规格参照
		SpecRef specRef = specRefDao.selectColorSpecRef();
		// 获取货品规格枚举项id集合
		List<Integer> specItemIds = productSpecValDao.selectSpecItemIdsByRefCodeAndGoodsId(specRef.getCode(), goodsId);
		//
		for (Integer specItemId : specItemIds) {
			GoodsCatSpecItem goodsCatSpecItem = goodsCatSpecItemDao.selectById(specItemId);
			specItems.add(goodsCatSpecItem);
		}
		return specItems;
	}

	@Override
	public boolean deleteGoodsColorImgs(Integer goodsId) {
		// --清空商品颜色图片表所有数据
		int count = goodsColorImgDao.deleteByGoodsId(goodsId);
		// --清空商品相册分组值
		goodsAlbumImgDao.updateImageGroupAsNullByGoodsId(goodsId);
		//
		return count > 0;
	}

	@Override
	public boolean saveGoodsColorImgs(List<GoodsColorImg> goodsColorImgs) {
		boolean ok = true;
		// 清空某商品的数据
		GoodsColorImg _goodsColorImg = goodsColorImgs.get(0);
		Integer goodsId = _goodsColorImg.getGoodsId();
		//
		deleteGoodsColorImgs(goodsId);
		// 更新数据
		for (GoodsColorImg goodsColorImg : goodsColorImgs) {
			Long imageId = goodsColorImg.getImageId();
			if (imageId == null) {
				// TODO
			}
			goodsColorImgDao.insert(goodsColorImg);
			/*
			 * Long colorImgId = goodsColorImg.getId(); if(colorImgId == null){ goodsColorImgDao.insert(goodsColorImg); }else{ goodsColorImgDao.update(goodsColorImg); }
			 */
			// 更新商品相册imageGroup
			String specVal = goodsColorImg.getSpecVal();
			Long imgId = goodsColorImg.getImageId();
			int count = goodsAlbumImgDao.updateImageGroupById(specVal, imgId);
			ok = count > 0 && ok;
		}
		return ok;
	}

	@Override
	public List<GoodsColorImg> getGoodsColorImgsByGoodsId(Integer goodsId) {
		List<GoodsColorImg> goodsColorImgs = goodsColorImgDao.selectByGoodsId(goodsId);
		// 获取商品颜色规格枚举项
		for (GoodsColorImg colorImg : goodsColorImgs) {
			Integer specItemId = colorImg.getSpecItemId();
			GoodsCatSpecItem goodsCatSpecItem = goodsCatSpecItemDao.selectById(specItemId);
			colorImg.setSpecItem(goodsCatSpecItem);
		}
		//
		return goodsColorImgs;
	}

	@Override
	public PaginatedList<Goods> getGoodsByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<Goods> goods = goodsDao.selectByFilter(paginatedFilter);
		return goods;
	}

	@Override
	public ProductDto getProductDtoBySpecStrAndGoodsId(String specStr, Integer goodsId) {
		Long productId = productExDao.selectIdBySpecStrAndGoodsId(specStr, goodsId);
		if (productId == null)
			return null;
		ProductDto productDto = getProductDtoByProductId(productId);
		return productDto;
	}

	@Override
	public boolean deleteGoodsRelatedByGoodsId(Integer goodsId) {
		// 删除商品颜色分组图片
		goodsColorImgDao.deleteByGoodsId(goodsId);
		// 删除商品相册图片
		goodsAlbumImgDao.deleteByGoodsId(goodsId);
		// 删除商品属性值
		goodsAttrValDao.deleteByGoodsId(goodsId);
		// 删除商品介绍
		goodsIntroDao.deleteByGoodsId(goodsId);
		// 删除商品介绍图片
		goodsIntroImgDao.deleteByGoodsId(goodsId);
		// 删除商品扩展表信息
		goodsExDao.deleteByGoodsId(goodsId);
		//
		int count = goodsDao.deleteById(goodsId);
		return count > 0;
	}

	@Override
	public Long getProductIdBySpecStrAndGoodsId(String specStr, Integer goodsId) {
		Long productId = productExDao.selectIdBySpecStrAndGoodsId(specStr, goodsId);
		//
		return productId;
	}

	@Override
	public boolean existGoodName(String goodsName, Integer categId, Integer vendorId, Integer shopId) {
		Goods goods = goodsDao.selectByCatIdAndShopIdAndNameAndVendorId(categId, shopId, goodsName, vendorId);
		return goods != null;
	}

	@Override
	public void updateGoodsBuySumByOrderNoAndUserId(String orderNo, Integer userId) {
		List<SaleOrderGoods> saleOrderGoodsList = saleOrderService.getSaleOrderGoodssByOrderNoAndUserId(orderNo, userId);
		for (int i = 0; i < saleOrderGoodsList.size(); i++) {
			SaleOrderGoods saleOrderGoods = saleOrderGoodsList.get(i);
			Long productId = saleOrderGoods.getProductId();
			GoodsBuySum goodsBuySum = goodsBuySumDao.selectByUserIdAndProductId(userId, productId);
			if (goodsBuySum == null) {
				goodsBuySum = new GoodsBuySum();
				goodsBuySum.setUserId(userId);
				goodsBuySum.setProductId(productId);
				goodsBuySum.setGoodsId(saleOrderGoods.getGoodsId());
				goodsBuySum.setCatId(saleOrderGoods.getCatId());
				goodsBuySum.setShopId(saleOrderGoods.getShopId());
				goodsBuySum.setCount(Long.valueOf(saleOrderGoods.getQuantity()));
				goodsBuySum.setLastTime(new Date());
				goodsBuySumDao.insert(goodsBuySum);
			} else {
				long count = goodsBuySum.getCount() + saleOrderGoods.getQuantity();
				goodsBuySum.setCount(count);
				goodsBuySumDao.update(goodsBuySum);
			}
		}
	}

}
