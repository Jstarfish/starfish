package priv.starfish.mall.dao.statis;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.statis.entity.GoodsBuySum;

@IBatisSqlTarget
public interface GoodsBuySumDao extends BaseDao<GoodsBuySum, Long> {
	GoodsBuySum selectById(Long id);

	GoodsBuySum selectByUserIdAndProductId(Integer userId, Long productId);

	int insert(GoodsBuySum goodsBuySum);

	int update(GoodsBuySum goodsBuySum);

	int deleteById(Long id);

	/**
	 * 查询某店铺下的所有（购买过的）会员id列表
	 * 
	 * @author guoyn
	 * @date 2015年8月17日 下午3:08:07
	 * 
	 * @param shopId
	 * @return List<Integer>
	 */
	List<Integer> selectUserIdsByShopId(Integer shopId);

	/**
	 * 获取某个产品的总购买数量
	 * 
	 * @author guoyn
	 * @date 2015年10月28日 上午10:23:09
	 * 
	 * @param productId
	 * @return Long
	 */
	long selectBuyCountByProductId(Long productId);

	/**
	 * 删除该货品的购买记录
	 * 
	 * @author guoyn
	 * @date 2015年12月23日 下午3:55:19
	 * 
	 * @param productId
	 * @return int
	 */
	int deleteByProductId(Long productId);
}