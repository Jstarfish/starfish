package priv.starfish.mall.dao.statis;

import java.math.BigDecimal;
import java.util.Map;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.order.entity.SaleOrder;

@IBatisSqlTarget
public interface DistShopSumDao extends BaseDao<SaleOrder, Long> {
	/**
	 * 获取卫星店铺订单数统计信息
	 * 
	 * @author 李江
	 * @date 2015年11月10日 下午4:36:23
	 * 
	 * @param filter
	 * @return
	 */
	Long getDistShopOrderStatis(MapContext filter);
	
	/**
	 * 获取卫星店铺服务数统计信息
	 * 
	 * @author 李江
	 * @date 2015年11月10日 下午4:36:23
	 * 
	 * @param filter
	 * @return
	 */
	Long getDistShopSvcStatis(MapContext filter);
	
	/**
	 * 获取卫星店铺访客数统计信息
	 * 
	 * @author 李江
	 * @date 2015年11月10日 下午4:36:23
	 * 
	 * @param filter
	 * @return
	 */
	Long getDistShopVisitorStatis(MapContext filter);
	
	/**
	 * 获取卫星店铺金额统计信息
	 * 
	 * @author 李江
	 * @date 2015年11月10日 下午4:36:23
	 * 
	 * @param filter
	 * @return
	 */
	BigDecimal getDistShopAmountStatis(MapContext filter);
	
	/**
	 * 获取卫星店铺未结算金额统计信息
	 * 
	 * @author 李江
	 * @date 2015年11月10日 下午4:36:23
	 * 
	 * @param filter
	 * @return
	 */
	BigDecimal getDistShopNoAmountStatis(MapContext filter);
	
	/**
	 * 按时间聚合获取卫星店铺订单统计信息
	 * 
	 * @author 李江
	 * @date 2015年11月10日 下午4:36:23
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<Map<String, Long>> selectDistShopOrderCountGroupTime(PaginatedFilter paginatedFilter);
	
	/**
	 * 按时间聚合获取卫星店铺服务统计信息
	 * 
	 * @author 李江
	 * @date 2015年11月10日 下午4:36:23
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<Map<String, Long>> selectDistShopScvCountGroupTime(PaginatedFilter paginatedFilter);
}
