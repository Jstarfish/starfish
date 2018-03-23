package priv.starfish.mall.dao.goods;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.goods.entity.GoodsAttrVal;

@IBatisSqlTarget
public interface GoodsAttrValDao extends BaseDao<GoodsAttrVal, Long> {
	GoodsAttrVal selectById(Long id);
	
	/**
	 * 根据商品 id和商品分类属性Id查询商品属性值列表
	 * @author zjl
	 * @date 2015年7月29日 下午5:35:38
	 * 
	 * @param goodsId 商品 id
	 * @param attrId  商品分类属性Id
	 * @return List<GoodsAttrVal>
	 */
	List<GoodsAttrVal> selectByGoodsIdAndAttrId(Integer goodsId, Integer attrId);

	int insert(GoodsAttrVal goodsAttrVal);

	int update(GoodsAttrVal goodsAttrVal);

	int deleteById(Long id);
	/**
	 * 通过属性关联Id删除
	 * 
	 * @author 郝江奎
	 * @date 2015-6-3 上午9:55:19
	 * 
	 * @param catId
	 * @return
	 */
	int deleteByAttrId(Integer attrId);
	
	/**
	 * 通过属性关联Id删除
	 * 
	 * @author 郝江奎
	 * @date 2015-6-3 上午9:55:19
	 * 
	 * @param catId
	 * @return
	 */
	int deleteByAttrIds(List<Integer> attrId);

	/**
	 * 根据商品 id查询商品属性值列表
	 * @author guoyn
	 * @date 2015年6月24日 下午4:57:51
	 * 
	 * @param goodsId
	 * @return List<GoodsAttrVal>
	 */
	List<GoodsAttrVal> selectByGoodsId(Integer goodsId);

	/**
	 * 根据商品分类id和属性值获取商品id集合
	 * 
	 * @author guoyn
	 * @date 2015年10月26日 下午2:17:59
	 * 
	 * @param goodsCatAttrId
	 * @param attrVal
	 * @return List<Integer>
	 */
	List<Integer> selectGoodsIdByAttrIdAndAttrVal(Integer goodsCatAttrId, String attrVal);

	/**
	 * 删除某商品的所有属性值
	 * 
	 * @author guoyn
	 * @date 2015年11月5日 下午3:13:12
	 * 
	 * @param goodsId
	 * @return int
	 */
	int deleteByGoodsId(Integer goodsId);

	/**
	 * 获取某商品的指定keyflag的属性值列表
	 * 
	 * @author guoyn
	 * @date 2015年11月9日 下午2:56:44
	 * 
	 * @param goodsId
	 * @param keyFlag
	 * @return List<GoodsAttrVal> 
	 */
	List<GoodsAttrVal> selectByGoodsIdAndKeyFlag(Integer goodsId, boolean keyFlag);
}