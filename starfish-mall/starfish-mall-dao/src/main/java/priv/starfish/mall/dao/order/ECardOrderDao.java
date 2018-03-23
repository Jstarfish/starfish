package priv.starfish.mall.dao.order;

import java.util.List;
import java.util.Map;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.order.entity.ECardOrder;

@IBatisSqlTarget
public interface ECardOrderDao extends BaseDao<ECardOrder, Integer> {
	ECardOrder selectById(Integer id);

	int insert(ECardOrder eCardOrder);

	int update(ECardOrder eCardOrder);

	int deleteById(Integer id);

	/**
	 * 分页查询e卡订单
	 * 
	 * @author wangdi
	 * @date 2015年11月12日 下午4:53:51
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<ECardOrder> selectByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 根据条件查询出唯一一条e卡订单记录
	 * 
	 * @author wangdi
	 * @date 2015年11月12日 下午4:56:25
	 * 
	 * @param filter
	 * @return
	 */
	ECardOrder selectOneByFilter(MapContext filter);

	/**
	 * 取消订单
	 * 
	 * @author wangdi
	 * @date 2015年11月13日 下午7:13:16
	 * 
	 * @param params
	 * @return
	 */
	int updateForCancel(MapContext filter);

	/**
	 * 通过e卡订单号no查询e卡订单
	 * 
	 * @author "WJJ"
	 * @date 2015年11月18日 上午10:30:19
	 * 
	 * @param out_trade_no
	 * @return
	 */
	ECardOrder selectByNo(String no);

	/**
	 * 根据订单号no，更新订单的支付状态与支付时间
	 * 
	 * @author "WJJ"
	 * @date 2015年11月18日 上午11:51:32
	 * 
	 * @param map
	 * @return
	 */
	int updateByNo(Map<String, Object> map);

	/**
	 * 根据用户id查询订单数量
	 * 
	 * @author "lichaojie"
	 * @date 2015年11月20日 上午11:51:32
	 * 
	 * @param map
	 * @return
	 */
	Integer selectCount(Integer userId, String code);

	/**
	 * 查询e卡订单
	 * 
	 * @author wangdi
	 * @date 2015年12月8日 下午4:56:46
	 * 
	 * @param filter
	 * @return
	 */
	List<ECardOrder> selectByFilterNormal(MapContext filter);

	/**
	 * 查询e卡订单数量
	 * 
	 * @author wangdi
	 * @date 2015年12月29日 下午5:25:25
	 * 
	 * @param filter
	 * @return
	 */
	Integer selectCountByFilter(MapContext filter);

	/**
	 * 根据code查询是否有订单
	 * 
	 * @author "WJJ"
	 * @date 2016年1月6日 上午11:21:32
	 * 
	 * @param code
	 * @return
	 */
	Integer selectCountByCode(String code);

	/**
	 * 后台——分页查询E卡订单列表
	 * 
	 * @author "WJJ"
	 * @date 2016年1月12日 下午3:47:30
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<ECardOrder> selectByFilterBack(PaginatedFilter paginatedFilter);
}