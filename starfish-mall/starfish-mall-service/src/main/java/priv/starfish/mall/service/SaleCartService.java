package priv.starfish.mall.service;

import java.util.List;

import priv.starfish.mall.cart.entity.SaleCart;
import priv.starfish.mall.cart.entity.SaleCartGoods;
import priv.starfish.mall.cart.entity.SaleCartSvc;

public interface SaleCartService extends BaseService {
	// ---------------------------------------购物车--------------------------------------------
	SaleCart getSaleCartByUserId(Integer userId);

	// ---------------------------------------购物车服务列表--------------------------------------------
	boolean saveSaleCartSvc(SaleCartSvc saleCartSvc);

	boolean updateSaleCartSvc(SaleCartSvc saleCartSvc);

	boolean deleteSaleCartSvc(List<Integer> ids);

	public List<SaleCartSvc> getSaleCartSvcsByUserId(Integer userId);

	// ---------------------------------------服务购物车商品--------------------------------------------
	boolean saveSvcCartGoods(SaleCartGoods svcCartGoods);

	boolean updateSvcCartGoods(SaleCartGoods svcCartGoods);

	boolean deleteSvcCartGoodsById(Integer id);

	public List<SaleCartGoods> getSvcCartGoodsByCartSvcId(Long cartId);

}
