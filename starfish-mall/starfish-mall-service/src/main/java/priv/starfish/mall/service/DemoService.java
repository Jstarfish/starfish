package priv.starfish.mall.service;

import java.util.Date;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.demo.entity.Shopx;

/**
 * 示例服务
 * 
 * @author koqiui
 * @date 2015年11月3日 下午4:04:23
 *
 */
public interface DemoService extends BaseService {
	// --------------------- Shopx -------------------------------
	Shopx getShopxById(Integer id);

	Shopx getShopxByName(String name);

	boolean saveShopx(Shopx shopx);

	boolean updateShopx(Shopx shopx);

	boolean deleteShopxById(Integer id);

	boolean existShopxByName(String name);

	/**
	 * 按条件过滤店铺
	 * 
	 * @author koqiui
	 * @date 2015年11月5日 下午1:26:32
	 * 
	 * @param paginatedFilter
	 *            regionLevel 的值决定 regionId的含义(==null时, regionId => regionId, !=null[1,2,3,4]时regionId => provinceId, cityId, countyId, townId) <br/>
	 *            = (int) regionId, = (int) regionLevel, <br/>
	 *            like (string) name, like (string) py, like (string) address <br/>
	 *            = (bool) disabled, = (bool) closed, = (int) auditStatus
	 * @return
	 */
	PaginatedList<Shopx> getShopxsByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 滚动选择 (无条件)
	 * 
	 * @author koqiui
	 * @date 2015年11月5日 下午1:17:24
	 * 
	 * @param pager
	 *            分页信息
	 * @return
	 */
	PaginatedList<Shopx> getShopxsByScrolling(PaginatedFilter pager);

	/**
	 * 获取分页的最新变化/需要重新索引的(indexTime == null || changeTime != null && changeTime > indexTime) <br/>
	 * 
	 * @author koqiui
	 * @date 2015年11月5日 下午1:27:50
	 * 
	 * @param pager
	 *            分页信息
	 * @return
	 */
	PaginatedList<Shopx> getShopxsByLatestChanges(PaginatedFilter pager);

	/**
	 * 更新为已索引
	 * 
	 * @author koqiui
	 * @date 2015年11月5日 下午12:45:27
	 * 
	 * @param id
	 * @param indexTime
	 * @return
	 */
	boolean updateShopxAsIndexed(Integer id, Date indexTime);
}
