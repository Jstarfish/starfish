package priv.starfish.mall.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.repo.FileRepository;
import priv.starfish.common.user.UserContext;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.dao.agency.AgencyAuditRecDao;
import priv.starfish.mall.dao.agency.AgencyDao;
import priv.starfish.mall.agency.entity.Agency;
import priv.starfish.mall.agency.entity.AgencyAuditRec;
import priv.starfish.mall.dao.agent.AgentDao;
import priv.starfish.mall.dao.comn.UserDao;
import priv.starfish.mall.service.AgencyService;
import priv.starfish.mall.service.util.CodeUtil;

@Service("agencyService")
public class AgencyServiceImpl extends BaseServiceImpl implements AgencyService {
	@Resource
	AgencyDao agencyDao;

	@Resource
	AgentDao agentDao;

	@Resource
	UserDao userDao;

	@Resource
	AgencyAuditRecDao agencyAuditRecDao;

	@Resource
	FileRepository fileRepository;

	@Override
	public PaginatedList<Agency> getAgenciesByFilter(PaginatedFilter paginatedFilter) {
		PaginatedList<Agency> pagListAgencies = agencyDao.selectByFilter(paginatedFilter);
		// 设置LOGO查看路径
		for (Agency agency : pagListAgencies.getRows()) {
			this.filterFileBrowseUrl(agency);
		}
		return pagListAgencies;
	}

	@Override
	public List<Agency> getAgenciesByFilter(MapContext filterItems) {
		return agencyDao.selectByFilter(filterItems);
	}

	@Override
	public boolean updateAgencyStatus(Integer agencyId, boolean disabled) {
		int count = agencyDao.updateDisabled(agencyId, disabled);
		return count > 0;
	}

	@Override
	public boolean updateAgencyAuditStatusById(AgencyAuditRec agencyAuditRec, UserContext userContext) {
		// 更新代理处的审核状态
		int count = agencyDao.updateAuditStatusById(agencyAuditRec.getAgencyId(), agencyAuditRec.getAuditStatus());
		if (count > 0) {
			// 记录审核状态
			agencyAuditRec.setAuditorId(userContext.getUserId());
			agencyAuditRec.setAuditorName(userContext.getUserName());
			//
			agencyAuditRecDao.insert(agencyAuditRec);
		}
		//
		return count > 0;
	}

	@Override
	public boolean updateAgencyAuditStatusByIds(List<String> agencyIds, AgencyAuditRec agencyAuditRec, UserContext userContext) {
		boolean ok = true;
		//
		for (String agencyId : agencyIds) {
			agencyAuditRec.setAgencyId(Integer.valueOf(agencyId));
			boolean flag = updateAgencyAuditStatusById(agencyAuditRec, userContext);
			ok = flag && ok;
		}
		return ok;
	}

	@Override
	public boolean deleteAgencyById(Integer agencyId) {
		int count = agencyDao.deleteById(agencyId);
		return count > 0;
	}

	@Override
	public boolean deleteAgenciesByIds(List<String> agencyIds) {
		boolean ok = true;
		//
		for (String agencyId : agencyIds) {
			ok = deleteAgencyById(Integer.valueOf(agencyId)) && ok;
		}
		return ok;
	}

	@Override
	public boolean deleteAgenciesByAgentId(Integer agentId) {
		int count = agencyDao.deleteByAgentId(agentId);
		return count > 0;
	}

	@Override
	public boolean saveAgency(Agency agency) {

		agency.setCode(CodeUtil.newAgencyCode());
		agency.setPy(StrUtil.chsToPy(agency.getName()));
		if (agency.getAuditStatus() == null) {
			agency.setAuditStatus(0);
		}
		if (agency.getClosed() == null) {
			agency.setClosed(false);
		}
		if (agency.getDisabled() == null) {
			agency.setDisabled(false);
		}
		int count = agencyDao.insert(agency);
		return count > 0;
	}

	@Override
	public boolean updateAgency(Agency agency) {
		int count = agencyDao.update(agency);
		return count > 0;
	}

	@Override
	public Agency getAgencyById(Integer entityId) {
		return agencyDao.selectById(entityId);
	}

}
