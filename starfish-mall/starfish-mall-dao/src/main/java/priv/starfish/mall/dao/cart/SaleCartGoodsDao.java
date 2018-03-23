package priv.starfish.mall.dao.cart;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.cart.entity.SaleCartGoods;

@IBatisSqlTarget
public interface SaleCartGoodsDao extends BaseDao<SaleCartGoods, Integer> {

	SaleCartGoods selectById(Integer id);

	int insert(SaleCartGoods saleCartGoods);

	int update(SaleCartGoods saleCartGoods);

	int deleteById(Integer id);

	/**
	 * 根据购物车id查询购物车商品列表
	 * 
	 * @author MCIUJavaDept
	 * @date 2015年10月16日 下午6:11:33
	 * 
	 * @param userId
	 * @return
	 */
	List<SaleCartGoods> selectByCartSvcId(Long cartSvcId);

	int deleteByCartSvcId(Integer cartSvcId);

	/**
	 * 获取某个货品的存在记录数
	 * 
	 * @author guoyn
	 * @date 2015年11月3日 下午4:42:54
	 * 
	 * @param productId
	 * @return int
	 */
	int selectCountByProductId(Long productId);

}
