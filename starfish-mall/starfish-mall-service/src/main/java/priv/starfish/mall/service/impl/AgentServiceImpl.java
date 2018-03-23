package priv.starfish.mall.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.dao.agency.AgencyDao;
import priv.starfish.mall.agency.entity.Agency;
import priv.starfish.mall.dao.agent.AgentDao;
import priv.starfish.mall.agent.entity.Agent;
import priv.starfish.mall.dao.comn.UserAccountDao;
import priv.starfish.mall.dao.comn.UserDao;
import priv.starfish.mall.dao.comn.UserRoleDao;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.comn.entity.User;
import priv.starfish.mall.comn.entity.UserAccount;
import priv.starfish.mall.comn.entity.UserRole;
import priv.starfish.mall.service.AgentService;
import priv.starfish.mall.service.UserService;

@Service("agentService")
public class AgentServiceImpl extends BaseServiceImpl implements AgentService {

	@Resource
	AgentDao agentDao;

	@Resource
	AgencyDao agencyDao;

	@Resource
	UserDao userDao;

	@Resource
	UserRoleDao userRoleDao;

	@Resource
	UserAccountDao userAccountDao;
	
	@Resource
	UserService userService;

	@Override
	public boolean saveAgent(Agent agent) {
		boolean flag = false;
		// 保存用户
		User user = agent.getUser();
		if (user != null) {
			//
			if (user.getId() == null) {
				userDao.insert(user);
				agent.setId(user.getId());
			}
			// 保存代理商
			if (agent.getDisabled() == null) {
				agent.setDisabled(false);
			}
			flag = agentDao.insert(agent) > 0;
			if (flag) {
				// 保存角色权限
				UserRole userRole = new UserRole();
				userRole.setUserId(user.getId());
				userRole.setRoleId(4);
				userRole.setScope(AuthScope.agency);
				userRole.setEntityId(1);
				userRoleDao.insert(userRole);

				// 保存代理商资金账户
				List<UserAccount> userAccts = user.getUserAccts();
				if (userAccts != null && !userAccts.isEmpty()) {
					for (UserAccount userAccount : userAccts) {
						userAccount.setUserId(user.getId());
						userAccount.setVerified(false);
						userAccount.setDisabled(false);
						userAccountDao.insert(userAccount);
					}
				}

			}
		}
		//
		return flag;
	};

	@Override
	public PaginatedList<Agent> getAgentsByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<Agent> agents = agentDao.selectByFilter(paginatedFilter);
		for (Agent agent : agents.getRows()) {
			Integer agentId = agent.getId();
			List<Agency> agencies = agencyDao.selectByAgentId(agentId);
			List<UserAccount> userAccounts = userAccountDao.selectByUserId(agentId);
			agent.setAgencies(agencies);
			agent.getUser().setUserAccts(userAccounts);
		}
		return agents;
	}

	@Override
	public boolean updateAgent(Agent agent) {
		boolean flag = false;
		if (null == agent.getDisabled()) {
			// 更新用户
			User user = agent.getUser();
			int userCount = userDao.update(user);
			// 更新代理商
			if (userCount > 0) {
				flag = agentDao.update(agent) > 0;
			}
		} else {
			flag = agentDao.update(agent) > 0;
		}

		return flag;
	}

	@Override
	public List<Agent> getAgentsByFilter(MapContext filterItems) {
		return agentDao.selectByFilter(filterItems);
	}

	@Override
	public boolean deleteAgentById(Integer agentId) {
		int count = agentDao.deleteById(agentId);
		return count > 0;
	}

	@Override
	public boolean deleteAgentsByIds(List<Integer> ids) {
		boolean ok = true;
		for (Integer agentId : ids) {
			ok = deleteAgentById(agentId) && ok;
		}
		return ok;
	}

	@Override
	public Agent getAgentById(Integer id) {
		return agentDao.selectById(id);
	}

	@Override
	public List<User> getAgentWorkersById(Integer agentId, boolean includeRoles) {
		return userService.getAllUsersByScopeAndEntityId(AuthScope.mall, agentId, includeRoles);
	}
}
