package priv.starfish.mall.dao.settle;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.settle.entity.SettleRec;

@IBatisSqlTarget
public interface SettleRecDao extends BaseDao<SettleRec, Integer> {
	SettleRec selectById(Integer id);

	int insert(SettleRec settleRec);

	int update(SettleRec settleRec);

	int deleteById(Integer id);

	/**
	 * 分页查询结算记录（对商户）
	 * 
	 * @author "WJJ"
	 * @date 2015年10月19日 下午7:48:21
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<SettleRec> selectByFilter(PaginatedFilter paginatedFilter);
}