package priv.starfish.mall.dao.categ;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.categ.entity.GoodsCatAttrItem;

@IBatisSqlTarget
public interface GoodsCatAttrItemDao extends BaseDao<GoodsCatAttrItem, Integer> {
	GoodsCatAttrItem selectById(Integer id);

	GoodsCatAttrItem selectByAttrIdAndValue(Integer attrId, String value);

	int insert(GoodsCatAttrItem goodsCatAttrItem);

	int update(GoodsCatAttrItem goodsCatAttrItem);

	int deleteById(Integer id);

	/**
	 * 通过属性关联Id删除
	 * 
	 * @author 廖晓远
	 * @date 2015-6-3 上午9:55:19
	 * 
	 * @param catId
	 * @return
	 */
	int deleteByAttrId(Integer attrId);

	/**
	 * 通过属性关联Id列表删除
	 * 
	 * @author 廖晓远
	 * @date 2015-6-3 上午9:55:22
	 * 
	 * @param catIds
	 * @return
	 */
	int deleteByAttrIds(List<Integer> attrIds);

	/**
	 * 通过属性关联Id获取枚举值列表
	 * 
	 * @author 廖晓远
	 * @date 2015-6-3 上午11:43:22
	 * 
	 * @param attrId
	 * @return
	 */
	List<GoodsCatAttrItem> selectByAttrId(Integer attrId);

	/**
	 * 通过属性关联Id获取枚举值列表
	 * 
	 * @author 廖晓远
	 * @date 2015-6-5 下午4:12:12
	 * 
	 * @param attrId
	 * @param uncontainIds
	 *            不包含的Id列表，实现增量
	 * @return
	 */
	List<GoodsCatAttrItem> selectByAttrId(Integer attrId, List<Integer> uncontainIds);
	
	/**
	 * 通过属性关联Id和需要排除的Id删除
	 * 
	 * @author 廖晓远
	 * @date 2015年6月24日 下午7:56:18
	 * 
	 * @param attrId
	 *            属性关联ID
	 * @param uncontainIds
	 *            需要排除的Id
	 * @return
	 */
	int deleteByAttrIdAndUncontainIds(Integer attrId, List<Integer> uncontainIds);

	/**
	 * 通过属性关联Id查询品牌Code
	 * 
	 * @author 廖晓远
	 * @date 2015年7月20日 下午5:17:27
	 * 
	 * @return
	 */
	List<String> selectCodesByAttrId(Integer attrId);

	/**
	 * 分页获取商品分类属性的value2列表
	 * 
	 * @author guoyn
	 * @date 2015年10月31日 下午3:17:07
	 * 
	 * @param paginatedFilter =attrIds
	 * @return PaginatedList<String>
	 */
	PaginatedList<String> selectValue2ByFilter(PaginatedFilter paginatedFilter);

}