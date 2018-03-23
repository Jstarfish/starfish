package priv.starfish.mall.dao.goods;

import java.util.Date;
import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.goods.entity.Product;

@IBatisSqlTarget
public interface ProductDao extends BaseDao<Product, Long> {
	Product selectById(Long id);

	int insert(Product product);

	int update(Product product);

	int deleteById(Long id);

	/**
	 * 查询某商品下的所有货品
	 * 
	 * @author guoyn
	 * @date 2015年6月24日 下午5:53:09
	 * 
	 * @param goodsId
	 * @param deleted true:被删除的, null:查询所有的货品
	 * @return List<Product>
	 */
	List<Product> selectByGoodsIdAndDeleted(Integer goodsId, Boolean deleted);

	/**
	 * 通过上下文分页查询店铺所有货品
	 * 
	 * @author 廖晓远
	 * @date 2015年6月26日 上午11:18:19
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<Product> selectProductsByContext(PaginatedFilter paginatedFilter);

	/**
	 * 根据过滤条件查询所有产品
	 * 
	 * @author guoyn
	 * @date 2015年10月19日 下午7:15:07
	 * 
	 * @param paginatedFilter
	 *            =catId, like goodsName, like title...
	 * @return PaginatedList<Product>
	 */
	PaginatedList<Product> selectByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 批量下架产品
	 * 
	 * @author zjl
	 * @date 2015年7月1日 下午6:19:37
	 * 
	 * @param ids
	 *            货品Ids
	 * @param shelfStatus
	 *            上下架状态
	 * @param date
	 *            最新时间
	 * @return
	 */
	int batchDownShelfProducts(List<Integer> ids, Integer shelfStatus, Date date);

	/**
	 * 批量上架产品
	 * 
	 * @author zjl
	 * @date 2015年7月1日 下午6:19:37
	 * 
	 * @param ids
	 *            货品Ids
	 * @param shelfStatus
	 *            上下架状态
	 * @param date
	 *            最新时间
	 * @return
	 */
	int batchUpShelfProducts(List<Integer> ids, Integer shelfStatus, Date date);

	/**
	 * 更新产品deleted状态
	 * 
	 * @author guoyn
	 * @date 2015年10月22日 下午12:01:09
	 * 
	 * @param productId
	 *            产品id
	 * @param deleted
	 *            true:假删, false:恢复
	 * @return int
	 */
	int updateDeletedById(Long productId, boolean deleted);

	/**
	 * 获取产品列表
	 * 
	 * @author guoyn
	 * @date 2015年10月28日 下午3:52:38
	 * 
	 * @param productIds
	 * @return List<Product>
	 */
	List<Product> selectByIds(List<Long> productIds);

	/**
	 * 更新上下架状态
	 * 
	 * @author guoyn
	 * @date 2015年10月30日 下午7:06:26
	 * 
	 * @param product
	 * @return int
	 */
	int updateShelfStatus(Product product);

	/**
	 * 根据上架状态查询某上下下的所有货品id
	 * 
	 * @author guoyn
	 * @date 2015年11月7日 下午4:43:41
	 * 
	 * @param shelfStatus
	 * @param goodsId
	 * @return List<Long>
	 */
	List<Long> selectIdsByShelfStatusAndGoodsId(Integer shelfStatus, Integer goodsId);

	/**
	 * 查询某商品下未上架和已下架的货品列表
	 * 
	 * @author guoyn
	 * @date 2015年12月24日 下午3:51:12
	 * 
	 * @param goodsId
	 * @return List<Product>
	 */
	List<Product> selectUnShelvefProductsByGoodsId(Integer goodsId);
}