package priv.starfish.mall.dao.categ;


import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.categ.entity.GoodsCatPriceRange;

@IBatisSqlTarget
public interface GoodsCatPriceRangeDao extends BaseDao<GoodsCatPriceRange, Integer> {
	GoodsCatPriceRange selectById(Integer id);

	int insert(GoodsCatPriceRange goodsCatPriceRange);

	int update(GoodsCatPriceRange goodsCatPriceRange);

	int deleteById(Integer id);

	/**
	 * 通过分类Id删除商品分类价格区间
	 * 
	 * @author 廖晓远
	 * @date 2015年7月27日 上午10:58:31
	 * 
	 * @param catId
	 * @return
	 */
	int deleteByCatId(Integer catId);

	/**
	 * 通过分类Id列表批量删除商品分类价格区间
	 * 
	 * @author 廖晓远
	 * @date 2015年7月27日 上午10:48:00
	 * 
	 * @param catIds
	 * @return
	 */
	int deleteByCatIds(List<Integer> catIds);

	/**
	 * 通过分类Id和id列表批量删除多余的商品分类价格区间
	 * 
	 * @author 廖晓远
	 * @date 2015年7月27日 下午12:15:23
	 * 
	 * @param catId
	 * @param ids
	 * @return
	 */
	int deleteCatPriceRangeByCatIdAndNotInIds(Integer catId, List<Integer> ids);

	/**
	 * 通过分类Id查询商品分类价格区间
	 * 
	 * @author 廖晓远
	 * @date 2015年7月27日 上午10:33:08
	 * 
	 * @param catId
	 * @return
	 */
	List<GoodsCatPriceRange> selectByCatId(Integer catId);
}