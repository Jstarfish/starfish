package priv.starfish.mall.dao.goods;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.goods.entity.GoodsAlbumImg;

@IBatisSqlTarget
public interface GoodsAlbumImgDao extends BaseDao<GoodsAlbumImg, Long> {

	GoodsAlbumImg selectById(Long id);

	int insert(GoodsAlbumImg goodsAlbumImg);

	int update(GoodsAlbumImg goodsAlbumImg);

	int deleteById(Long id);

	/**
	 * 根据商品id和店铺id获取相册图片 
	 * @author guoyn
	 * @date 2015年6月29日 下午1:37:21
	 * 
	 * @param goodsId
	 * @param shopId
	 * @return List<GoodsAlbumImg>
	 */
	List<GoodsAlbumImg> selectByGoodsId(Integer goodsId);

	/**
	 * 根据imgUuid删除相册图片
	 * @author guoyn
	 * @date 2015年6月29日 下午3:17:44
	 * 
	 * @param uuid
	 * @return int
	 */
	int deleteByImageUuid(String uuid);

	/**
	 * 根据uuid查询
	 * @author guoyn
	 * @date 2015年7月14日 下午6:28:34
	 * 
	 * @param uuid
	 * @return GoodsAlbumImg
	 */
	GoodsAlbumImg selectByImageUuid(String uuid);

	/**
	 * 更新商品相册分组
	 * 
	 * @author guoyn
	 * @date 2015年11月2日 下午6:57:16
	 * 
	 * @param specVal
	 * @param imgId
	 * @return int
	 */
	int updateImageGroupById(String specVal, Long imgId);

	/**
	 * 清空某商品的商品相册图片分组值
	 * 
	 * @author guoyn
	 * @date 2015年11月3日 上午11:39:37
	 * 
	 * @param goodsId
	 * @return int
	 */
	int updateImageGroupAsNullByGoodsId(Integer goodsId);

	/**
	 * 删除商品下的所有相册图片
	 * 
	 * @author guoyn
	 * @date 2015年11月5日 下午3:11:11
	 * 
	 * @param goodsId
	 * @return int
	 */
	int deleteByGoodsId(Integer goodsId);

	/**
	 * 清空图片的颜色分组图片
	 * 
	 * @author guoyn
	 * @date 2015年12月25日 下午6:35:53
	 * 
	 * @param imgId
	 * @return int
	 */
	int updateImageGroupAsNullById(Long imgId);
}
