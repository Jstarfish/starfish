package priv.starfish.mall.dao.demo;

import java.util.Date;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.demo.entity.Shopx;

@IBatisSqlTarget
public interface ShopxDao extends BaseDao<Shopx, Integer> {
	Shopx selectById(Integer id);

	Shopx selectByName(String name);

	int insert(Shopx shopx);

	int update(Shopx shopx);

	int deleteById(Integer id);

	/**
	 * 按条件过滤店铺
	 * 
	 * @author koqiui
	 * @date 2015年11月3日 下午3:16:01
	 * 
	 * @param paginatedFilter
	 *            regionLevel 的值决定 regionId的含义(==null时, regionId => regionId, !=null[1,2,3,4]时regionId => provinceId, cityId, countyId, townId) <br/>
	 *            = (int) regionId, = (int) regionLevel, <br/>
	 *            like (string) name, like (string) py, like (string) address <br/>
	 *            = (bool) disabled, = (bool) closed, = (int) auditStatus
	 * @return
	 */
	PaginatedList<Shopx> selectByFilter(PaginatedFilter paginatedFilter);

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
	PaginatedList<Shopx> selectByScrolling(PaginatedFilter pager);

	/**
	 * 获取分页的最新变化/需要重新索引的(indexTime == null || changeTime != null && changeTime > indexTime) <br/>
	 * 
	 * @author koqiui
	 * @date 2015年11月5日 下午1:17:42
	 * 
	 * @param pager
	 *            分页信息
	 * @return
	 */
	PaginatedList<Shopx> selectByLatestChanges(PaginatedFilter pager);

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
	int updateAsIndexed(Integer id, Date indexTime);

}