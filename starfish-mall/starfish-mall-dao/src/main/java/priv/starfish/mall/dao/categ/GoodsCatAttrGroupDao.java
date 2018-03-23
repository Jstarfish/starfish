package priv.starfish.mall.dao.categ;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.categ.entity.GoodsCatAttrGroup;

@IBatisSqlTarget
public interface GoodsCatAttrGroupDao extends BaseDao<GoodsCatAttrGroup, Integer> {

	GoodsCatAttrGroup selectById(Integer id);

	GoodsCatAttrGroup selectByCatIdAndName(Integer catId, String name);

	int insert(GoodsCatAttrGroup goodsCatAttrGroup);

	int update(GoodsCatAttrGroup goodsCatAttrGroup);

	int deleteById(Integer id);

	/**
	 * 通过分组Id和分类Id删除此分类下多余分组
	 * 
	 * @author 廖晓远
	 * @date 2015-6-8 下午7:33:44
	 * 
	 * @param ids
	 * @param catId
	 * @return
	 */
	int deleteByUncontainIdsAndCatId(List<Integer> ids, Integer catId);

	/**
	 * 查询分类下属性分组数量
	 * 
	 * @author 廖晓远
	 * @date 2015-5-29 上午11:20:22
	 * 
	 * @param catId
	 * @return
	 */
	int selectCountByCatId(Integer catId);

	/**
	 * 通过分类Id获取所有分组
	 * 
	 * @author 廖晓远
	 * @date 2015-6-6 下午6:48:36
	 * 
	 * @param catId
	 * @return
	 */
	List<GoodsCatAttrGroup> selectByCatId(Integer catId);

	/**
	 * 通过分类Id删除分组
	 * 
	 * @author 廖晓远
	 * @date 2015-6-3 上午9:55:19
	 * 
	 * @param catId
	 * @return
	 */
	int deleteByCatId(Integer catId);

	/**
	 * 通过分类Id列表删除分组
	 * 
	 * @author 廖晓远
	 * @date 2015-6-3 上午9:55:22
	 * 
	 * @param catIds
	 * @return
	 */
	int deleteByCatIds(List<Integer> catIds);
}
