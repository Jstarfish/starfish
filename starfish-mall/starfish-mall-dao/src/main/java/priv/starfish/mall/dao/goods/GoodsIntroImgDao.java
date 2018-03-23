package priv.starfish.mall.dao.goods;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.goods.entity.GoodsIntroImg;

@IBatisSqlTarget
public interface GoodsIntroImgDao extends BaseDao<GoodsIntroImg, Long> {

	GoodsIntroImg selectById(Long id);

	int insert(GoodsIntroImg goodsIntroImg);

	int update(GoodsIntroImg goodsIntroImg);

	int deleteById(Long id);

	/**
	 * 查询某商品的商品介绍图片
	 * @author guoyn
	 * @date 2015年7月2日 下午2:41:56
	 * 
	 * @param goodsId
	 * @return List<GoodsIntroImg>
	 */
	List<GoodsIntroImg> selectByGoodsId(Integer goodsId);

	/**
	 * 根据uuid删除商品介绍图片 
	 * @author guoyn
	 * @date 2015年7月3日 下午3:32:15
	 * 
	 * @param uuid
	 * @return int
	 */
	int deleteByUuid(String uuid);

	/**
	 * 删除某商品下的所有介绍图片
	 * 
	 * @author guoyn
	 * @date 2015年11月5日 下午3:17:48
	 * 
	 * @param goodsId
	 * @return int
	 */
	int deleteByGoodsId(Integer goodsId);
}
