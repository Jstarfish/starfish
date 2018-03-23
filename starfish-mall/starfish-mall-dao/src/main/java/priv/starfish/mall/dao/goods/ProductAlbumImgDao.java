package priv.starfish.mall.dao.goods;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.goods.entity.ProductAlbumImg;

@IBatisSqlTarget
public interface ProductAlbumImgDao extends BaseDao<ProductAlbumImg, Long> {
	ProductAlbumImg selectById(Long id);

	ProductAlbumImg selectByProductIdAndImageId(Long productId, Integer imageId);

	int insert(ProductAlbumImg productAlbumImg);

	int update(ProductAlbumImg productAlbumImg);

	int deleteById(Long id);

	/**
	 * 根据货品id获取货品相册图片
	 * @author guoyn
	 * @date 2015年7月9日 下午5:18:32
	 * 
	 * @param id
	 * @return List<ProductAlbumImg>
	 */
	List<ProductAlbumImg> selectByProductId(Long productId);

	/**
	 * 根据imageId查询
	 * @author guoyn
	 * @date 2015年7月14日 下午6:31:42
	 * 
	 * @param imageId
	 * @return List<ProductAlbumImg>
	 */
	List<ProductAlbumImg> selectByImageId(Long imageId);

	/**
	 * 根据imageId查询货品相册图片ids
	 * @author guoyn
	 * @date 2015年7月14日 下午6:38:03
	 * 
	 * @param id
	 * @return List<Long>
	 */
	List<Long> selectIdsByImageId(Long id);

	/**
	 * 根据货品id删除货品相册
	 * @author guoyn
	 * @date 2015年7月20日 下午5:25:13
	 * 
	 * @param productId
	 * @return int
	 */
	int deleteByProductId(Long productId);

	/**
	 * 查询某货品下所有的图片id
	 * 
	 * @author guoyn
	 * @date 2015年11月7日 下午2:13:45
	 * 
	 * @param productId
	 * @return List<Long>
	 */
	List<Long> selectImageIdsByProductId(Long productId);

}
