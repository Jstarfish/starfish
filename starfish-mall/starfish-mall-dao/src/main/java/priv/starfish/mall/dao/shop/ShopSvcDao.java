package priv.starfish.mall.dao.shop;

import java.util.List;
import java.util.Map;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.shop.entity.ShopSvc;

@IBatisSqlTarget
public interface ShopSvcDao extends BaseDao<ShopSvc, Long> {
	ShopSvc selectById(Long id);

	ShopSvc selectByShopIdAndSvcId(Integer shopId, Integer svcId);

	int insert(ShopSvc shopSvc);

	int update(ShopSvc shopSvc);

	int deleteById(Long id);

	/**
	 * 获取指定店铺指定服务状态
	 * 
	 * @author 邓华锋
	 * @date 2015年11月3日 上午10:09:43
	 * 
	 * @param shopId
	 * @param svcIds
	 * @return
	 */
	Map<Long, ShopSvc> selectByShopIdAndSvcIds(Integer shopId, List<Long> svcIds);

	/**
	 * 分页查询店铺服务列表
	 * 
	 * @param paginatedFilter：
	 *            like name
	 * @return PaginatedList<ShopSvc>
	 */
	PaginatedList<ShopSvc> selectByFilter(PaginatedFilter paginatedFilter);

	List<ShopSvc> selectByFilter(MapContext requestData);

	/**
	 * 删除店铺服务信息
	 * 
	 * @param svcId
	 * @param shopId
	 * @return int
	 */
	int deleteByShopIdAndSvcId(Integer svcId, Integer shopId);

	int deleteBySvcId(Integer svcId);

	List<ShopSvc> selectByShopId(Integer shopId);
}