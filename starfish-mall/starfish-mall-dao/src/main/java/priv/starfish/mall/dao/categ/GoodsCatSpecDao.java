package priv.starfish.mall.dao.categ;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.categ.entity.GoodsCatSpec;

@IBatisSqlTarget
public interface GoodsCatSpecDao extends BaseDao<GoodsCatSpec, Integer> {

	GoodsCatSpec selectById(Integer id);

	GoodsCatSpec selectByCatIdAndRefId(Integer catId, Integer refId);

	int insert(GoodsCatSpec goodsCatSpec);

	int update(GoodsCatSpec goodsCatSpec);

	int deleteById(Integer id);

	/**
	 * 查询分类下规格规格数量
	 * 
	 * @author 廖晓远
	 * @date 2015-5-29 上午11:20:22
	 * 
	 * @param catId
	 * @return
	 */
	int selectCountByCatId(Integer catId);

	/**
	 * 通过分类Id查询分类下规格
	 * 
	 * @author 廖晓远
	 * @date 2015-5-29 下午5:19:49
	 * 
	 * @param catId
	 * @return
	 */
	List<GoodsCatSpec> selectByCatId(Integer catId);
	
	/**
	 * 通过规格参照Id查询分类下规格
	 * 
	 * @author 郝江奎
	 * @date 2015-5-29 下午5:19:49
	 * 
	 * @param catId
	 * @return
	 */
	List<Integer> selectIdsByRefId(Integer specId);
	
	/**
	 * 通过规格参照Id查询分类下规格
	 * 
	 * @author 郝江奎
	 * @date 2015-5-29 下午5:19:49
	 * 
	 * @param catId
	 * @return
	 */
	List<Integer> selectIdByRefIds(List<Integer> specIds);

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
	 * 通过分类Id获取分类规格关联ID
	 * 
	 * @author 廖晓远
	 * @date 2015-6-3 上午10:17:52
	 * 
	 * @param catId
	 * @return
	 */
	List<Integer> selectIdByCatId(Integer catId);
	
	/**
	 * 根据规格参数id删除相应分类规格值
	 * @author 郝江奎
	 * @date 2015年8月31日 下午10:25:47
	 * 
	 * @param goodsCatSpecId
	 * @return int
	 */
	int deleteByRefId(Integer specId);
	
	/**
	 * 根据规格参数ids批量删除相应分类规格值
	 * @author 郝江奎
	 * @date 2015年8月31日 下午10:25:47
	 * 
	 * @param goodsCatSpecId
	 * @return int
	 */
	int deleteByRefIds(List<Integer> specIds);

	/**
	 * 通过分类Id列表获取分类规格关联ID
	 * 
	 * @author 廖晓远
	 * @date 2015-6-3 上午10:17:55
	 * 
	 * @param catIds
	 * @return
	 */
	List<Integer> selectIdByCatIds(List<Integer> catIds);

	/**
	 * 获取颜色标识下的商品分类规格id集合
	 * 
	 * @author guoyn
	 * @date 2015年11月27日 下午5:23:41
	 * 
	 * @param colorFlag
	 * @return List<Integer>
	 */
	List<Integer> selectIdsByColorFlag(boolean colorFlag);
}
