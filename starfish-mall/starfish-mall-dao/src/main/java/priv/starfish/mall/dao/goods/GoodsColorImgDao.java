package priv.starfish.mall.dao.goods;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.goods.entity.GoodsColorImg;

@IBatisSqlTarget
public interface GoodsColorImgDao extends BaseDao<GoodsColorImg, Long> {
	GoodsColorImg selectById(Long id);

	GoodsColorImg selectByGoodsIdAndSpecIdAndSpecItemId(Integer goodsId, Integer specId, Integer specItemId);

	int insert(GoodsColorImg goodsColorImg);

	int update(GoodsColorImg goodsColorImg);

	int deleteById(Long id);

	/**
	 * 获取某商品的颜色图片集合
	 * 
	 * @author guoyn
	 * @date 2015年11月3日 上午10:28:54
	 * 
	 * @param goodsId
	 * @return List<GoodsColorImg>
	 */
	List<GoodsColorImg> selectByGoodsId(Integer goodsId);

	/**
	 * 删除某商品下的所有颜色图片
	 * 
	 * @author guoyn
	 * @date 2015年11月3日 上午11:35:47
	 * 
	 * @param goodsId
	 * @return int
	 */
	int deleteByGoodsId(Integer goodsId);

	/**
	 * 删除某图片的颜色记录
	 * 
	 * @author guoyn
	 * @date 2015年12月25日 下午6:33:02
	 * 
	 * @param imgId
	 * @return int
	 */
	int deleteByImgId(Long imgId);
}