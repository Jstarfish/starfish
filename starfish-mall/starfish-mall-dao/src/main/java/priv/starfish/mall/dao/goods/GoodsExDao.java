package priv.starfish.mall.dao.goods;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.goods.entity.GoodsEx;

@IBatisSqlTarget
public interface GoodsExDao extends BaseDao<GoodsEx, Integer> {
	GoodsEx selectById(Integer id);

	GoodsEx selectByGoodsIdAndSpecCode(Integer goodsId, String specCode);

	int insert(GoodsEx goodsEx);

	int update(GoodsEx goodsEx);

	int deleteById(Integer id);

	/**
	 * 获取某个商品下的所有扩展信息列表
	 * 
	 * @author guoyn
	 * @date 2015年11月4日 下午3:07:34
	 * 
	 * @param goodsId
	 * @return List<GoodsEx>
	 */
	List<GoodsEx> selectByGoodsId(Integer goodsId);

	/**
	 * 删除某商品的所有扩展信息
	 * 
	 * @author guoyn
	 * @date 2015年11月5日 下午3:14:53
	 * 
	 * @param goodsId
	 * @return int
	 */
	int deleteByGoodsId(Integer goodsId);
}
