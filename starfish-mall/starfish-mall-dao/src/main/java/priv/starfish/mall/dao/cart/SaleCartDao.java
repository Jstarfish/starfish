package priv.starfish.mall.dao.cart;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.cart.entity.SaleCart;

@IBatisSqlTarget
public interface SaleCartDao extends BaseDao<SaleCart, Integer> {

	SaleCart selectById(Integer id);

	int insert(SaleCart saleCart);

	int update(SaleCart saleCart);

	int deleteById(Integer id);

	/**
	 * 根据用戶id购物车列表
	 * 
	 * @author MCIUJavaDept
	 * @date 2015年10月16日 下午6:11:33
	 * 
	 * @param userId
	 * @return
	 */
	List<SaleCart> selectByUserId(Integer userId);

}
