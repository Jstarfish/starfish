package priv.starfish.mall.dao.goods;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.goods.entity.ProductSpecVal;

@IBatisSqlTarget
public interface ProductSpecValDao extends BaseDao<ProductSpecVal, Long> {
	ProductSpecVal selectById(Long id);

	ProductSpecVal selectByProductIdAndSpecId(Integer productId, Integer specId);

	int insert(ProductSpecVal productSpecVal);

	int update(ProductSpecVal productSpecVal);

	int deleteById(Long id);

	/**
	 * 根据货品Id和商品Id获取货品规格值列表
	 * @author guoyn
	 * @date 2015年6月24日 下午5:57:28
	 * 
	 * @param productId
	 * @param goodsId
	 * @return List<ProductSpecVal>
	 */
	List<ProductSpecVal> selectByProductIdAndGoodsId(Long productId, Integer goodsId);

	/**
	 * 根据货品id删除相应货品规格值
	 * @author guoyn
	 * @date 2015年6月25日 下午3:11:47
	 * 
	 * @param productId
	 * @return int
	 */
	int deleteByProductId(Long productId);
	/**
	 * 根据规格分类id删除相应货品规格值
	 * @author 郝江奎
	 * @date 2015年8月31日 下午10:41:47
	 * 
	 * @param goodsCatSpecId
	 * @return int
	 */
	int deleteBySpecId(Integer goodsCatSpecId);
	/**
	 * 根据规格分类ids删除相应货品规格值
	 * @author 郝江奎
	 * @date 2015年8月31日 下午10:11:47
	 * 
	 * @param goodsCatSpecId
	 * @return int
	 */
	int deleteBySpecIds(List<Integer> goodsCatSpecId);
	
	/**
	 * @author zjl
	 * @date 2015年7月24日 上午11:48:07
	 *  
	 * @param specId 商品分类规格Id
	 * @param goodsId 商品Id
	 * @return List<Integer>
	 */
	List<Integer> selectSpecItemIdsBySpecIdAndGoodsId(Integer specId, Integer goodsId);

	/**
	 * 获取产品的所有规格值列表
	 * 
	 * @author guoyn
	 * @date 2015年10月20日 上午11:03:17
	 * 
	 * @param productId
	 * @return List<ProductSpecVal>
	 */
	List<ProductSpecVal> selectByProductId(Long productId);

	/**
	 * 查询某商品下所有的规格参照code列表
	 * 
	 * @author guoyn
	 * @date 2015年10月29日 下午3:25:04
	 * 
	 * @param goodsId
	 * @return List<String>
	 */
	List<String> selectRefCodeByGoodsId(Integer goodsId);

	/**
	 * 获取某商品的某个规格下的所有规格枚举项列表
	 * 
	 * @author guoyn
	 * @date 2015年10月29日 下午3:29:10
	 * 
	 * @param refCode
	 * @param goodsId
	 * @return List<Integer>
	 */
	List<Integer> selectSpecItemIdsByRefCodeAndGoodsId(String refCode, Integer goodsId);

	/**
	 * 查询指定的货品列表中的所有的规格参照code列表
	 * 
	 * @author guoyn
	 * @date 2015年11月7日 下午4:52:57
	 * 
	 * @param productIds
	 * @return List<String>
	 */
	List<String> selectRefCodeByIncludeProductIds(List<Long> productIds);

	/**
	 * 获取指定的货品列表中的某个规格下的所有规格枚举项列表
	 * 
	 * @author guoyn
	 * @date 2015年11月7日 下午5:10:57
	 * 
	 * @param refCode
	 * @param productIds
	 * @return List<Integer>
	 */
	List<Integer> selectSpecItemIdsByRefCodeAndIncludeProductIds(String refCode, List<Long> productIds);

}