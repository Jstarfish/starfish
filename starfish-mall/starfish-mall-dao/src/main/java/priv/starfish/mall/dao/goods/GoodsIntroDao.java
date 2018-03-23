package priv.starfish.mall.dao.goods;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.goods.entity.GoodsIntro;

@IBatisSqlTarget
public interface GoodsIntroDao extends BaseDao<GoodsIntro, Integer> {
	GoodsIntro selectById(Integer id);

	GoodsIntro selectByGoodsId(Integer goodsId);

	int insert(GoodsIntro goodsIntro);

	int update(GoodsIntro goodsIntro);

	int deleteById(Integer id);

	/**
	 * 删除商品的介绍信息
	 * 
	 * @author guoyn
	 * @date 2015年11月5日 下午3:16:39
	 * 
	 * @param goodsId
	 * @return int
	 */
	int deleteByGoodsId(Integer goodsId);
}