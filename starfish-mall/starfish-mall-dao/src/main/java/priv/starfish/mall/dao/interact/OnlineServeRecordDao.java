package priv.starfish.mall.dao.interact;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.interact.entity.OnlineServeRecord;

@IBatisSqlTarget
public interface OnlineServeRecordDao extends BaseDao<OnlineServeRecord, Long> {

	OnlineServeRecord selectById(Long id);
	/**
	 * 
	 * @author koqiui
	 * @date 2015年8月8日 下午6:01:21
	 * 
	 * @param paginatedFilter
	 * 	= servantId , like servantName, like memberName
	 * @return
	 */
	PaginatedList<OnlineServeRecord> selectByFilter(PaginatedFilter paginatedFilter);

	List<OnlineServeRecord> selectByMemberId(Integer memberId);

	int insert(OnlineServeRecord onlineServeRecord);

	int update(OnlineServeRecord onlineServeRecord);

	int deleteById(Long id);

}
