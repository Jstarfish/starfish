package priv.starfish.mall.service;

import java.util.List;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.agent.entity.Agent;
import priv.starfish.mall.comn.entity.User;

public interface AgentService extends BaseService {

	/**
	 * 保存代理商
	 * 
	 * @author guoyn
	 * @date 2015年9月9日 下午7:22:47
	 * 
	 * @param agent
	 * @return boolean
	 */
	boolean saveAgent(Agent agent);

	/**
	 * 分页查询代理商
	 * 
	 * @author guoyn
	 * @date 2015年9月9日 下午7:23:01
	 * 
	 * @param paginatedFilter
	 *            : like realName, =disabled, =id
	 * @return PaginatedList<Agent>
	 */
	PaginatedList<Agent> getAgentsByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 根据过滤条件查询代理商集合
	 * 
	 * @author guoyn
	 * @date 2015年9月10日 上午10:05:12
	 * 
	 * @param filterItems
	 *            : like realName, =disabled, =id
	 * @return List<Agent>
	 */
	List<Agent> getAgentsByFilter(MapContext filterItems);

	/**
	 * 更新代理商
	 * 
	 * @author guoyn
	 * @date 2015年9月10日 上午9:53:11
	 * 
	 * @param agent
	 * @return boolean
	 */
	boolean updateAgent(Agent agent);

	/**
	 * 根据id删除代理商
	 * 
	 * @author guoyn
	 * @date 2015年9月10日 上午9:55:21
	 * 
	 * @param id
	 * @return boolean
	 */
	boolean deleteAgentById(Integer id);

	/**
	 * 批量删除代理商
	 * 
	 * @author guoyn
	 * @date 2015年9月10日 上午10:02:07
	 * 
	 * @param ids
	 * @return boolean
	 */
	boolean deleteAgentsByIds(List<Integer> ids);

	/**
	 * 根据用户id获取代理商
	 * 
	 * @author guoyn
	 * @date 2015年9月10日 下午1:43:37
	 * 
	 * @param id
	 * @return Agent
	 */
	Agent getAgentById(Integer id);

	// ----------------------------------- 代理处人员 ---------------------------------------
	/**
	 * 获取代理处全部工作人员
	 * 
	 * @author koqiui
	 * @date 2015年12月17日 上午1:18:32
	 * 
	 * @param agentId
	 * @param includeRoles
	 * @return
	 */
	List<User> getAgentWorkersById(Integer agentId, boolean includeRoles);

}
