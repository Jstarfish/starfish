package priv.starfish.mall.dao.cart;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.cart.entity.SaleCartSvc;

@IBatisSqlTarget
public interface SaleCartSvcDao extends BaseDao<SaleCartSvc, Integer> {

	SaleCartSvc selectById(Integer id);

	int insert(SaleCartSvc saleCartSvc);

	int update(SaleCartSvc saleCartSvc);

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
	List<SaleCartSvc> selectByCartId(Integer cart);

	SaleCartSvc selectByCartIdAndSvcId(Integer cartId, Integer svcId);

}
