package priv.starfish.mall.dao.agency;

import priv.starfish.common.annotation.IBatisSqlTarget;
import priv.starfish.mall.dao.base.BaseDao;
import priv.starfish.mall.agency.entity.AgencyAuditRec;

@IBatisSqlTarget
public interface AgencyAuditRecDao extends BaseDao<AgencyAuditRec, Integer> {
	AgencyAuditRec selectById(Integer id);

	int insert(AgencyAuditRec agencyAuditRec);

	int update(AgencyAuditRec agencyAuditRec);

	int deleteById(Integer id);
}