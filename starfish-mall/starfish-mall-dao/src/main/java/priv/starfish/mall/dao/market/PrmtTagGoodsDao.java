package priv.starfish.mall.dao.market;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.market.entity.PrmtTagGoods;

@IBatisSqlTarget
public interface PrmtTagGoodsDao extends BaseDao<PrmtTagGoods, Integer> {
	PrmtTagGoods selectById(Integer id);

	PrmtTagGoods selectByTagIdAndProductId(Integer tagId, Long productId);

	int insert(PrmtTagGoods prmtTagGoods);

	int update(PrmtTagGoods prmtTagGoods);

	int deleteById(Integer id);

	/**
	 * 删除促销标签货品
	 * @param tagId
	 * @param productId
	 * @return int
	 */
	int deleteByTagIdAndProductId(Integer tagId, Long productId);

	/**
	 * 分页查询促销标签货品
	 * @param paginatedFilter like product.title, = prmtTag.id
	 * @return PaginatedList<PrmtTagGoods>
	 */
	PaginatedList<PrmtTagGoods> selectByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 根据tagId获取促销商品
	 * 
	 * @author 郝江奎
	 * @date 2016年2月1日 上午10:40:36
	 * 
	 * @param id
	 * @return
	 */
	List<PrmtTagGoods> selectByTagId(Integer tagId);

	/**
	 * 通过productId获取PrmtTagGoods
	 * 
	 * @author 郝江奎
	 * @date 2016年2月16日 下午11:10:47
	 * 
	 * @param productId
	 * @return
	 */
	List<PrmtTagGoods> selectByProductId(Long productId);
}
