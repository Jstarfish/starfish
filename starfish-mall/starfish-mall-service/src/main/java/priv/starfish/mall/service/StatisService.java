package priv.starfish.mall.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.goods.dto.ProductDto;
import priv.starfish.mall.statis.dto.DistShopSumDto;

/**
 * 统计、报表处理
 * 
 * @author guoyn
 * @date 2015年10月27日 下午2:47:52
 *
 */
public interface StatisService extends BaseService {

	// ---------------------------------------会员购买货品汇总--------------------------------------------

	/**
	 * 获取统计购买产品总数量
	 * 
	 * @author guoyn
	 * @date 2015年10月28日 上午10:13:27
	 * 
	 * @param productIds
	 * @return List<ProductDto>
	 */
	List<ProductDto> getProductsBuySummary(List<Long> productIds);

	// ---------------------------------------商品浏览汇总--------------------------------------------
	/**
	 * 增加指定货品的某用户的浏览次数
	 * 
	 * @author koqiui
	 * @date 2016年3月1日 上午12:45:31
	 * 
	 * @param productId
	 * @param userId
	 * @param count
	 * @return
	 */
	boolean addGoodsBrowseCount(Long productId, Integer userId, int count);

	/**
	 * 增加指定货品的某用户的浏览次数（一次）
	 * 
	 * @author koqiui
	 * @date 2016年3月1日 上午12:45:41
	 * 
	 * @param productId
	 * @param userId
	 * @return
	 */
	boolean addGoodsBrowseCount(Long productId, Integer userId);

	/**
	 * 获取某个货品的浏览次数
	 * 
	 * @author koqiui
	 * @date 2016年3月1日 上午12:45:48
	 * 
	 * @param productId
	 * @return
	 */
	long getGoodsBrowseCount(Long productId);

	/**
	 * 获取指定的多个货品(ids)的浏览数量
	 * 
	 * @author koqiui
	 * @date 2016年3月1日 上午12:45:57
	 * 
	 * @param productIds
	 * @return
	 */
	Map<Long, Long> getGoodsBrowseCounts(List<Long> productIds);

	// ---------------------------------------店铺浏览汇总--------------------------------------------
	/**
	 * 增加指定店铺的某用户的浏览次数
	 * 
	 * @author koqiui
	 * @date 2016年2月29日 下午3:20:20
	 * 
	 * @param shopId
	 * @param userId
	 * @param count
	 * @return
	 */
	boolean addShopBrowseCount(Integer shopId, Integer userId, int count);

	/**
	 * 增加指定店铺的某用户的浏览次数（一次）
	 * 
	 * @author koqiui
	 * @date 2016年2月29日 下午3:20:56
	 * 
	 * @param shopId
	 * @param userId
	 * @return
	 */
	boolean addShopBrowseCount(Integer shopId, Integer userId);

	/**
	 * 获取某个店铺的浏览次数
	 * 
	 * @author koqiui
	 * @date 2016年2月29日 下午2:18:25
	 * 
	 * @param shopId
	 * @return
	 */
	long getShopBrowseCount(Integer shopId);

	/**
	 * 
	 * 获取指定的多个店铺(ids)的浏览数量
	 * 
	 * @author koqiui
	 * @date 2016年2月29日 下午7:16:49
	 * 
	 * @param shopIds
	 * @return
	 */
	Map<Integer, Long> getShopBrowseCounts(List<Integer> shopIds);
	
	// -------------------------------- 卫星店统计方法 ---------------------------------------
	
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
	 * 获取卫星店铺结算金额统计信息
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
	 * 按时间统计代理订单、承接订单
	 * 
	 * @author 李江
	 * @date 2015年11月10日 下午4:36:23
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<Map<String, Long>> getDistShopOrderGroupTime(PaginatedFilter paginatedFilter);
	
	/**
	 * 按时间统计服务订单信息
	 * 
	 * @author 李江
	 * @date 2015年11月10日 下午4:36:23
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<Map<String, Long>> getDistShopSvcGroupTime(PaginatedFilter paginatedFilter);
	
	/**
	 * 获取卫星店铺统计信息
	 * 
	 * @author 李江
	 * @date 2015年11月10日 下午4:36:23
	 * 
	 * @param filter
	 * @return
	 */
	DistShopSumDto getDistShopStatis(MapContext filter);
	
	/**
	 * 分页获取卫星店铺统计信息
	 * 
	 * @author 李江
	 * @date 2015年11月10日 下午4:36:23
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<DistShopSumDto> selectDistShopStatisByFilter(PaginatedFilter paginatedFilter);

}
