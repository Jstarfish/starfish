package priv.starfish.mall.dao.shop;

import java.util.List;
import java.util.Map;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.shop.entity.ShopProduct;

@IBatisSqlTarget
public interface ShopProductDao extends BaseDao<ShopProduct, Long> {
	ShopProduct selectById(Long id);

	int insert(ShopProduct shopProduct);

	int update(ShopProduct shopProduct);

	int deleteById(Long id);

	/**
	 * 根据条件查询店铺中的产品列表
	 * 
	 * @author guoyn
	 * @date 2015年10月17日 下午4:02:44
	 * 
	 * @param paginatedFilter
	 *            like goodsName, =catId, =lackFlag, =productId
	 * @return PaginatedList<ShopProduct>
	 */
	PaginatedList<ShopProduct> selectByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 更新店铺的商品中的缺货状态
	 * 
	 * @author guoyn
	 * @date 2015年10月17日 下午6:19:37
	 * 
	 * @param id
	 * @param lackFlag
	 * @return int
	 */

	int updateLackFlagById(Long id, Boolean lackFlag);

	/**
	 * 根据条件查询店铺中的产品列表
	 * 
	 * @author guoyn
	 * @date 2015年10月20日 下午5:08:52
	 * 
	 * @param filters
	 *            like goodsName, =catId, =lackFlag, =productId
	 * @return List<ShopProduct>
	 */
	List<ShopProduct> selectByFilter(MapContext filters);

	/**
	 * 获取某个货品被店铺使用中的店铺数量
	 * 
	 * @author guoyn
	 * @date 2015年11月3日 下午3:24:56
	 * 
	 * @param productId
	 * @return int
	 */
	int selectCountShopIdByProductId(Long productId);

	/**
	 * 删除某个货品下的所有数据
	 * 
	 * @author guoyn
	 * @date 2015年11月3日 下午3:45:06
	 * 
	 * @param productId
	 * @return int
	 */
	int deleteByProductId(Long productId);

	/**
	 * 获取指定店铺指定产品的缺货状态
	 * 
	 * @author 邓华锋
	 * @date 2015年11月3日 上午10:09:43
	 * 
	 * @param shopId
	 * @param productIds
	 * @return
	 */
	Map<Long, ShopProduct> selectLackFlagByShopIdAndProductIds(Integer shopId, List<Long> productIds);
}
