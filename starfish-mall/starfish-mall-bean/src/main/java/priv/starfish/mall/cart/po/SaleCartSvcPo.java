package priv.starfish.mall.cart.po;

import priv.starfish.mall.cart.dict.CartAction;

/**
 * 服务加入（减少、更新、单个删除）购物车用
 * 
 * @author Administrator
 * @date 2016年1月12日 下午6:47:15
 *
 */
public class SaleCartSvcPo {
	public CartAction action;
	public Integer svcId;
	public Boolean checkFlag;

	public static SaleCartSvcPo newOne() {
		return new SaleCartSvcPo();
	}

	@Override
	public String toString() {
		return "SaleCartSvcPo [action=" + action + ", svcId=" + svcId + ", checkFlag=" + checkFlag + "]";
	}
}
