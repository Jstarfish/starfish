package priv.starfish.mall.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import priv.starfish.mall.cart.entity.SaleCartSvc;
import priv.starfish.mall.dao.goods.GoodsAlbumImgDao;
import priv.starfish.mall.dao.goods.ProductAlbumImgDao;
import priv.starfish.mall.dao.goods.ProductDao;
import priv.starfish.mall.goods.entity.GoodsAlbumImg;
import priv.starfish.mall.goods.entity.Product;
import priv.starfish.mall.goods.entity.ProductAlbumImg;
import priv.starfish.mall.dao.cart.SaleCartSvcDao;
import priv.starfish.common.repo.FileRepository;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.dao.cart.SaleCartDao;
import priv.starfish.mall.dao.cart.SaleCartGoodsDao;
import priv.starfish.mall.cart.entity.SaleCart;
import priv.starfish.mall.cart.entity.SaleCartGoods;
import priv.starfish.mall.service.SaleCartService;
import priv.starfish.mall.dao.svcx.SvcxDao;
import priv.starfish.mall.svcx.entity.Svcx;

@Service("saleCartService")
public class SaleCartServiceImpl extends BaseServiceImpl implements SaleCartService {

	@Resource
	SvcxDao carSvcDao;

	@Resource
	SaleCartSvcDao saleCartSvcDao;

	@Resource
	SaleCartGoodsDao saleCartGoodsDao;

	@Resource
	ProductDao productDao;

	@Resource
	SaleCartDao saleCartDao;

	@Resource
	ProductAlbumImgDao productAlbumImgDao;

	@Resource
	FileRepository fileRepository;

	@Resource
	GoodsAlbumImgDao goodsAlbumImgDao;

	// ---------------------------------------服务购物车--------------------------------------------
	@Override
	public boolean saveSaleCartSvc(SaleCartSvc saleCartSvc) {
		if (null != saleCartSvc) {
			// 查询购物车是否存在该服务
			SaleCartSvc cart = saleCartSvcDao.selectByCartIdAndSvcId(saleCartSvc.getCartId(), saleCartSvc.getSvcId());
			if (null != cart) {
				// 根据保存信息判断是否是自选商品
				if (-1 == saleCartSvc.getSvcId()) {
					List<SaleCartGoods> cartGoddsList = saleCartSvc.getSaleCartGoodsList();
					if (null != cartGoddsList) {
						for (SaleCartGoods svcCartGoods : cartGoddsList) {
							//svcCartGoods.setCartId(cart.getId());
							saleCartGoodsDao.insert(svcCartGoods);
						}
					}
					return true;
				} else {
					return false;
				}
			} else {
				// 保存购物车信息
				saleCartSvcDao.insert(saleCartSvc);
				// 查询购物车id保存购物车商品用
				SaleCartSvc cartOne = saleCartSvcDao.selectByCartIdAndSvcId(saleCartSvc.getCartId(), saleCartSvc.getSvcId());
				if (null != cartOne) {
					List<SaleCartGoods> cartGoddsList = saleCartSvc.getSaleCartGoodsList();
					if (null != cartGoddsList) {
						for (SaleCartGoods svcCartGoods : cartGoddsList) {
							//svcCartGoods.setCartId(cartOne.getId());
							saleCartGoodsDao.insert(svcCartGoods);
						}
					}
					return true;
				} else {
					return false;
				}
			}
		} else {
			return false;
		}
	}

	@Override
	public boolean updateSaleCartSvc(SaleCartSvc saleCartSvc) {

		return saleCartSvcDao.update(saleCartSvc) > 0;
	}

	@Override
	public boolean deleteSaleCartSvc(List<Integer> ids) {
		// 删除购物车信息同时删除购物车商品
		if (null != ids) {
			for (Integer id : ids) {
				saleCartSvcDao.deleteById(id);
				saleCartGoodsDao.deleteByCartSvcId(id);
			}
		}
		return true;
	}

	@Override
	public List<SaleCartSvc> getSaleCartSvcsByUserId(Integer userId) {
		// 暂时这样查，后面改
		List<SaleCartSvc> svcCartList = saleCartSvcDao.selectByCartId(userId);
		if (null != svcCartList && svcCartList.size() > 0) {
			for (SaleCartSvc svcCart : svcCartList) {
				List<SaleCartGoods> listGoods = saleCartGoodsDao.selectByCartSvcId(svcCart.getId());
				for (SaleCartGoods saleCartGoods : listGoods) {
					// 查询货品
					Product product = productDao.selectById(saleCartGoods.getProductId());
					if (product != null) {
						// 获取图片
						List<ProductAlbumImg> productAlbumImgs = productAlbumImgDao.selectByProductId(product.getId());
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
						// 取第一张图片
						if (product.getProductAlbumImgs() != null && product.getProductAlbumImgs().size() > 0) {
							saleCartGoods.setProductAlbumImg(product.getProductAlbumImgs().get(0).getFileBrowseUrl());
						}
					}
					saleCartGoods.setProductAmount(product.getSalePrice());
					saleCartGoods.setProductName(product.getGoodsName());// 暂用商品名称
				}
				// 查询购物车商品
				svcCart.setSaleCartGoodsList(listGoods);
				// 查询服务
				Svcx carSvc = carSvcDao.selectById(svcCart.getSvcId());
				if (carSvc != null) {
					svcCart.setCarSvcName(carSvc.getName());
					svcCart.setSalePrice(carSvc.getSalePrice());
					svcCart.setSvcxAlbumImg(carSvc.getFileBrowseUrl());
				}
			}
		}
		return svcCartList;
	}

	// ---------------------------------------服务购物车商品--------------------------------------------
	@Override
	public boolean saveSvcCartGoods(SaleCartGoods svcCartGoods) {

		return saleCartGoodsDao.insert(svcCartGoods) > 0;
	}

	@Override
	public boolean updateSvcCartGoods(SaleCartGoods svcCartGoods) {
		return saleCartGoodsDao.update(svcCartGoods) > 0;
	}

	@Override
	public boolean deleteSvcCartGoodsById(Integer id) {
		return saleCartGoodsDao.deleteById(id) > 0;
	}

	@Override
	public List<SaleCartGoods> getSvcCartGoodsByCartSvcId(Long cartSvcId) {
		return saleCartGoodsDao.selectByCartSvcId(cartSvcId);
	}

	@Override
	public SaleCart getSaleCartByUserId(Integer userId) {
		return saleCartDao.selectById(userId);
	}

}
