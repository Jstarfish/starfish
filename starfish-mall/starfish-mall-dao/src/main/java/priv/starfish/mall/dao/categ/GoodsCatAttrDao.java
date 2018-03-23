package priv.starfish.mall.dao.categ;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.categ.entity.GoodsCatAttr;

/**
 * 商品分类属性DAO
 * 
 * @author 毛智东
 * @date 2015年5月27日 下午7:29:18
 * 
 */
@IBatisSqlTarget
public interface GoodsCatAttrDao extends BaseDao<GoodsCatAttr, Integer> {
	GoodsCatAttr selectById(Integer id);

	GoodsCatAttr selectByCatIdAndRefId(Integer catId, Integer refId);

	int insert(GoodsCatAttr goodsCatAttr);

	int update(GoodsCatAttr goodsCatAttr);

	int deleteById(Integer id);

	/**
	 * 查询分类下属性数量
	 * 
	 * @author 廖晓远
	 * @date 2015-5-29 上午11:20:22
	 * 
	 * @param catId
	 * @return
	 */
	int selectCountByCatId(Integer catId);

	/**
	 * 通过分类Id查询分类下属性
	 * 
	 * @author 廖晓远
	 * @date 2015-5-29 下午5:19:49
	 * 
	 * @param catId
	 * @return
	 */
	List<GoodsCatAttr> selectByCatId(Integer catId);
	
	/**
	 * 通过属性Id查询分类下属性
	 * 
	 * @author 郝江奎
	 * @date 2015-5-29 下午5:19:49
	 * 
	 * @param catId
	 * @return
	 */
	List<Integer> selectIdsByRefId(Integer refId);
	/**
	 * 通过属性Ids查询分类下属性
	 * 
	 * @author 郝江奎
	 * @date 2015-5-29 下午5:19:49
	 * 
	 * @param catId
	 * @return
	 */
	List<Integer> selectIdsByRefIds(List<Integer> refIds);

	/**
	 * 批量删除
	 * 
	 * @author 廖晓远
	 * @date 2015-5-28 下午7:33:26
	 * 
	 * @param ids
	 * @return
	 */
	int deleteByIds(List<Integer> ids);
	/**
	 * 通过属性refId删除属性
	 * 
	 * @author 郝江奎
	 * @date 2015-8-26 下午18:33:26
	 * 
	 * @param ids
	 * @return
	 */
	int deleteByRefId(Integer refId);
	
	/**
	 * 通过属性refIds删除属性
	 * 
	 * @author 郝江奎
	 * @date 2015-8-26 下午18:33:26
	 * 
	 * @param ids
	 * @return
	 */
	int deleteByRefIds(List<Integer> refIds);

	/**
	 * 通过分类Id获取分类属性关联ID
	 * 
	 * @author 廖晓远
	 * @date 2015-6-3 上午10:17:52
	 * 
	 * @param catId
	 * @return
	 */
	List<Integer> selectIdByCatId(Integer catId);

	/**
	 * 通过分类Id列表获取分类属性关联ID
	 * 
	 * @author 廖晓远
	 * @date 2015-6-3 上午10:17:55
	 * 
	 * @param catIds
	 * @return
	 */
	List<Integer> selectIdByCatIds(List<Integer> catIds);

	/**
	 * 通过分类Id和品牌标志获取分类属性关联ID
	 * 
	 * @author 廖晓远
	 * @date 2015年7月20日 下午5:21:54
	 * 
	 * @param catId
	 * @param brandFlag
	 * @return
	 */
	Integer selectIdByCatIdAndBrandFlag(Integer catId, Boolean brandFlag);
	
	/**通过分组groupId查询分类下属性
	 * @author zjl
	 * @date 2015年7月29日 上午11:29:42
	 * 
	 * @param groupId 分组Id
	 * @return List<GoodsCatAttr>
	 */
	List<GoodsCatAttr> selectByGroupId(Integer groupId);

	/**
	 * 获取是否为品牌的所有分类属性id列表
	 * 
	 * @author guoyn
	 * @date 2015年10月31日 下午3:08:12
	 * 
	 * @param brandFlag
	 * @return List<Integer>
	 */
	List<Integer> selectIdsByBrandFlag(boolean brandFlag);

}