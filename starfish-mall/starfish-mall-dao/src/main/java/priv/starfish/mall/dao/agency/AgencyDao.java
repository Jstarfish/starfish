package priv.starfish.mall.dao.agency;

import java.util.List;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.agency.entity.Agency;
import priv.starfish.mall.dao.base.BaseDao;

@IBatisSqlTarget
public interface AgencyDao extends BaseDao<Agency, Integer> {
	Agency selectById(Integer id);

	Agency selectByCode(String code);

	int insert(Agency agency);

	int update(Agency agency);

	int deleteById(Integer id);

	/**
	 * 分页查询代理处
	 * 
	 * @author guoyn
	 * @date 2015年9月10日 上午10:59:46
	 * 
	 * @param paginatedFilter
	 *            like name(代理处名称),like realName(所属代理商), =auditStatus =disabled, =code
	 * @return PaginatedList<Agency>
	 */
	PaginatedList<Agency> selectByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 查询代理处
	 * 
	 * @author guoyn
	 * @date 2015年9月10日 上午11:00:37
	 * 
	 * @param filterItems
	 *            like name(代理处名称), =phoneNo, =auditStatus, =disabled, =nickName(代理商昵称)
	 * @return List<Agency>
	 */
	List<Agency> selectByFilter(MapContext filterItems);

	/**
	 * 启用禁用代理处
	 * 
	 * @author guoyn
	 * @date 2015年9月10日 上午11:01:53
	 * 
	 * @param id
	 * @param disabled
	 *            false：启用，true：禁用
	 * @return int
	 */
	int updateDisabled(Integer id, boolean disabled);

	/**
	 * 更新代理处审核状态
	 * 
	 * @author guoyn
	 * @date 2015年9月10日 上午11:03:49
	 * 
	 * @param id
	 * @param auditStatus
	 *            0 未审核, 1 审核通过， 2，审核未通过
	 * @return int
	 */
	int updateAuditStatusById(Integer id, Integer auditStatus);

	/**
	 * 删除某个代理商下的所有代理处
	 * 
	 * @author guoyn
	 * @date 2015年9月10日 上午11:36:34
	 * 
	 * @param agentId
	 * @return int
	 */
	int deleteByAgentId(Integer agentId);

	/**
	 * 通过代理商id查询所有代理处信息
	 * 
	 * @author WJJ
	 * @date 2015年9月14日 下午3:06:45
	 * 
	 * @param agentId
	 * @return List<Agency>
	 */
	List<Agency> selectByAgentId(Integer agentId);
}