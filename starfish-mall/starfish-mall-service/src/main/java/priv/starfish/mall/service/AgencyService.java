package priv.starfish.mall.service;

import java.util.List;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.agency.entity.Agency;
import priv.starfish.mall.agency.entity.AgencyAuditRec;

public interface AgencyService extends BaseService {

	// --------------------------------------- Agency ----------------------------------------------

	/**
	 * 分页查询代理处集合
	 * 
	 * @author guoyn
	 * @date 2015年9月10日 上午10:17:32
	 * 
	 * @param paginatedFilter
	 *            : like name(代理处名称), =phoneNo, =auditStatus, =disabled, =nickName(代理商昵称)
	 * @return PaginatedList<Agency>
	 */
	PaginatedList<Agency> getAgenciesByFilter(PaginatedFilter paginatedFilter);

	/**
	 * 查询代理处集合
	 * 
	 * @author guoyn
	 * @date 2015年9月10日 上午10:49:28
	 * 
	 * @param filterItems
	 *            like name(代理处名称), =phoneNo, =auditStatus, =disabled, =nickName(代理商昵称)
	 * @return List<Agency>
	 */
	List<Agency> getAgenciesByFilter(MapContext filterItems);

	/**
	 * 启用禁用代理处
	 * 
	 * @author guoyn
	 * @date 2015年9月10日 上午10:40:08
	 * 
	 * @param agencyId
	 *            代理处Id
	 * @param disabled
	 *            false：启用，true：禁用
	 * @return boolean
	 */
	boolean updateAgencyStatus(Integer agencyId, boolean disabled);

	/**
	 * 审核代理处
	 * 
	 * @author guoyn
	 * @date 2015年9月10日 上午10:43:13
	 * 
	 * @return boolean
	 */
	boolean updateAgencyAuditStatusById(AgencyAuditRec agencyAuditRec, UserContext userContext);

	/**
	 * 批量审核代理处
	 * 
	 * @author guoyn
	 * @date 2015年9月10日 上午10:45:10
	 * 
	 * @param agencyIds
	 *            代理处Id结合
	 * @return boolean
	 */
	boolean updateAgencyAuditStatusByIds(List<String> agencyIds, AgencyAuditRec agencyAuditRec, UserContext userContext);

	/**
	 * 删除代理处
	 * 
	 * @author guoyn
	 * @date 2015年9月10日 上午10:46:21
	 * 
	 * @param agencyId
	 * @return
	 */
	boolean deleteAgencyById(Integer agencyId);

	/**
	 * 批量删除代理处
	 * 
	 * @author guoyn
	 * @date 2015年9月10日 上午10:46:21
	 * 
	 * @return
	 */
	boolean deleteAgenciesByIds(List<String> agencyIds);

	/**
	 * 删除某个代理商下的所有代理处
	 * 
	 * @author guoyn
	 * @date 2015年9月10日 上午10:47:34
	 * 
	 * @param agentId
	 * @return boolean
	 */
	boolean deleteAgenciesByAgentId(Integer agentId);

	/**
	 * 保存代理处
	 * 
	 * @author guoyn
	 * @date 2015年9月10日 上午10:56:01
	 * 
	 * @param agency
	 * @return boolean
	 */
	boolean saveAgency(Agency agency);

	/**
	 * 更新代理处
	 * 
	 * @author guoyn
	 * @date 2015年9月10日 上午10:56:37
	 * 
	 * @param agency
	 * @return boolean
	 */
	boolean updateAgency(Agency agency);

	/**
	 * 获取代理处
	 * 
	 * @author guoyn
	 * @date 2015年9月10日 下午4:56:07
	 * 
	 * @param entityId
	 * @return Agency
	 */
	Agency getAgencyById(Integer entityId);

}
