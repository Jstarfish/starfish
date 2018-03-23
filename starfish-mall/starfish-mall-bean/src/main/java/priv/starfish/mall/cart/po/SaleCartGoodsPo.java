package priv.starfish.mall.cart.po;

import priv.starfish.mall.cart.dict.CartAction;

/**
 * 商品加入（减少、更新、单个删除）购物车用
 * 
 * @author Administrator
 * @date 2016年1月12日 下午6:48:11
 *
 */
public class SaleCartGoodsPo {
	public CartAction action;
	public Long productId;
	public Integer quantity;
	public Boolean checkFlag;

	public static SaleCartGoodsPo newOne() {
		return new SaleCartGoodsPo();
	}

	@Override
	public String toString() {
		return "SaleCartGoodsPo [action=" + action + ", productId=" + productId + ", quantity=" + quantity + ", checkFlag=" + checkFlag + "]";
	}

}
