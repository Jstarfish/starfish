package priv.starfish.mall.dao.statis;

import java.util.List;
import java.util.Map;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.statis.entity.ShopBrowseSum;

@IBatisSqlTarget
public interface ShopBrowseSumDao extends BaseDao<ShopBrowseSum, Long> {
	ShopBrowseSum selectById(Long id);

	ShopBrowseSum selectByUserIdAndShopId(Integer userId, Integer shopId);

	int insert(ShopBrowseSum shopBrowseSum);

	int update(ShopBrowseSum shopBrowseSum);

	int deleteById(Long id);

	/**
	 * 增加店铺的浏览数量
	 * 
	 * @author koqiui
	 * @date 2016年2月29日 下午4:12:39
	 * 
	 * @param id
	 * @param count
	 * @return
	 */
	boolean addBrowseCountById(Long id, int count);

	/**
	 * 获取指定店铺的浏览次数
	 * 
	 * @author koqiui
	 * @date 2016年2月29日 下午2:38:15
	 * 
	 * @param shopId
	 * @return
	 */
	long selectBrowseCountByShopId(Integer shopId);

	/**
	 * 获取指定店铺id列表 的的浏览次数 映射（map）
	 * 
	 * @author koqiui
	 * @date 2016年2月29日 下午2:38:39
	 * 
	 * @param shopIds
	 * @return
	 */
	Map<Integer, Long> selectBrowseCountByShopIds(List<Integer> shopIds);
}