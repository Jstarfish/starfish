package priv.starfish.mall.dao.interact;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.interact.dto.InteractUserDto;
import priv.starfish.mall.interact.entity.OnlineServeNo;

@IBatisSqlTarget
public interface OnlineServeNoDao extends BaseDao<OnlineServeNo, Integer> {

	/**
	 * 查询在线客服编号
	 * 
	 * @author 王少辉
	 * @date 2015年8月7日 下午5:30:25
	 * 
	 * @param paginatedFilter
	 *            过滤条件：like servantNo, like servantName, like realName
	 * @return
	 */
	PaginatedList<InteractUserDto> selectByFilter(AuthScope authScope, Integer entityId, PaginatedFilter paginatedFilter);

	OnlineServeNo selectById(Integer id);

	int insert(OnlineServeNo onlineServeNo);

	int update(OnlineServeNo onlineServeNo);

	int deleteById(Integer id);
	int deleteByIds(List<Integer> ids);

	OnlineServeNo selectByServantNo(String servantNo);

	OnlineServeNo selectByServantName(String servantName);

	List<InteractUserDto> selectByFilter(AuthScope authScope, Integer entityId);

}
