package priv.starfish.mall.dao.settle;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.settle.entity.DistSettleRec;

@IBatisSqlTarget
public interface DistSettleRecDao extends BaseDao<DistSettleRec, Integer> {
	
	/**
	 * 根据条件查询结算记录信息
	 * 
	 * @author 李江
	 * @date 2015年11月10日 下午4:36:23
	 * 
	 * @param filter
	 * @return
	 */
	PaginatedList<DistSettleRec> selectByFilter(PaginatedFilter paginatedFilter);
	
	DistSettleRec selectById(Integer id);

	int insert(DistSettleRec distSettleRec);

	int update(DistSettleRec distSettleRec);

	int deleteById(Integer id);

	/**
	 * 分页查询商户对合作店的结算记录
	 * 
	 * @author "WJJ"
	 * @date 2016年2月25日 上午11:30:47
	 * 
	 * @param paginatedFilter
	 * @return
	 */
	PaginatedList<DistSettleRec> selectByFilterP(PaginatedFilter paginatedFilter);
}