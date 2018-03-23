package priv.starfish.mall.dao.goods;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.goods.entity.Goods;

@IBatisSqlTarget
public interface GoodsDao extends BaseDao<Goods, Integer> {
	Goods selectById(Integer id);

	Goods selectByCatIdAndShopIdAndNameAndVendorId(Integer catId, Integer shopId, String name, Integer vendorId);

	int insert(Goods goods);

	int update(Goods goods);

	int deleteById(Integer id);

	/**
	 * 通过分类Id查询商品数量
	 * 
	 * @author 廖晓远
	 * @date 2015年6月29日 下午6:49:25
	 * 
	 * @param catId
	 * @return
	 */
	int selectCountByCatId(Integer catId);
	
	/**
	 * 分页查询商品列表
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<Goods> selectByFilter(PaginatedFilter paginatedFilter);
	
	
}
