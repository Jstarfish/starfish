package priv.starfish.mall.cart.po;

import java.util.List;

import priv.starfish.mall.cart.dict.CartAction;

/**
 * 全选删除用
 * 
 * @author Administrator
 * @date 2016年1月12日 下午6:47:15
 *
 */
public class SaleCartCheckPo {
	public CartAction action;
	public List<SaleCartSvcPo> svcPoList;
	public List<SaleCartGoodsPo> goodsPoList;

	public static SaleCartCheckPo newOne() {
		return new SaleCartCheckPo();
	}

	@Override
	public String toString() {
		return "SaleCartAllPo [action=" + action + ", svcPoList=" + svcPoList + ", goodsPoList=" + goodsPoList + "]";
	}
}
