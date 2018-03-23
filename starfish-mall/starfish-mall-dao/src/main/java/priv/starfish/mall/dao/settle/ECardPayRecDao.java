package priv.starfish.mall.dao.settle;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.settle.entity.ECardPayRec;

@IBatisSqlTarget
public interface ECardPayRecDao extends BaseDao<ECardPayRec, Long> {
	ECardPayRec selectById(Long id);

	int insert(ECardPayRec eCardPayRec);

	int update(ECardPayRec eCardPayRec);

	int deleteById(Long id);

	/**
	 * 根据订单号no查询e卡订单支付记录
	 * 
	 * @author "WJJ"
	 * @date 2015年11月18日 上午11:08:20
	 * 
	 * @param no
	 * @return
	 */
	ECardPayRec selectByNo(String no);

	/**
	 * 分页查询E卡支付记录
	 * 
	 * @author "WJJ"
	 * @date 2015年11月21日 下午5:06:30
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<ECardPayRec> selectByFilter(PaginatedFilter paginatedFilter);
}