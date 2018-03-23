package priv.starfish.mall.dao.settle;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.settle.entity.SettleOrder;

@IBatisSqlTarget
public interface SettleOrderDao extends BaseDao<SettleOrder, Long> {
	SettleOrder selectById(Long id);

	int insert(SettleOrder settleOrder);

	int update(SettleOrder settleOrder);

	int deleteById(Long id);

	/**
	 * 通过SettleProcessId，查找集合。
	 * 
	 * @author "WJJ"
	 * @date 2016年1月12日 下午1:49:33
	 * 
	 * @param settleProcessId
	 * @return
	 */
	List<SettleOrder> selectBySettleProcessId(Long settleProcessId);
}