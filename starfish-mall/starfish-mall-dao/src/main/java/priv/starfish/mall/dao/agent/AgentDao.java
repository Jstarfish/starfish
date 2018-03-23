package priv.starfish.mall.dao.agent;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.agent.entity.Agent;
import priv.starfish.mall.dao.base.BaseDao;

@IBatisSqlTarget
public interface AgentDao extends BaseDao<Agent, Integer> {
	Agent selectById(Integer id);

	int insert(Agent agent);

	int update(Agent agent);

	int deleteById(Integer id);

	/**
	 * 分页查询代理商
	 * 
	 * @author guoyn
	 * @date 2015年9月9日 下午7:28:37
	 * 
	 * @param paginatedFilter
	 *            : like realName, =disabled, =id
	 * @return PaginatedList<Agent>
	 */
	PaginatedList<Agent> selectByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 根据过滤条件查询代理商集合
	 * 
	 * @author guoyn
	 * @date 2015年9月10日 上午10:11:07
	 * 
	 * @param filterItems
	 * @return List<Agent>
	 */
	List<Agent> selectByFilter(MapContext filterItems);
}