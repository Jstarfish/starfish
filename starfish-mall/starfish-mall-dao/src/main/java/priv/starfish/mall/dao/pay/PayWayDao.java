package priv.starfish.mall.dao.pay;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.pay.entity.PayWay;

@IBatisSqlTarget
public interface PayWayDao extends BaseDao<PayWay, Integer> {

	int insert(PayWay payWay);

	int update(PayWay payWay);

	int deleteById(Integer id);

	/**
	 * 分页获取支付接口信息
	 * 
	 * @author WJJ
	 * @date 2015年9月9日 下午6:29:06
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<PayWay> selectPayWaysByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 根据id查找支付接口
	 * 
	 * @author WJJ
	 * @date 2015年9月9日 下午6:29:16
	 * 
	 * @param payWayId
	 * @return
	 */
	PayWay selectById(Integer payWayId);

	/**
	 * 根据启用禁用状态，查询支付方式
	 * 
	 * @author "WJJ"
	 * @date 2015年11月20日 下午5:27:22
	 * 
	 * @return
	 */
	List<PayWay> selectByStatus();

}
