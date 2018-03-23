package priv.starfish.mall.dao.agency.impl;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.agency.AgencyAuditRecDao;
import priv.starfish.mall.agency.entity.AgencyAuditRec;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;

@Component("agencyAuditRecDao")
public class AgencyAuditRecDaoImpl extends BaseDaoImpl<AgencyAuditRec, Integer> implements AgencyAuditRecDao {
	@Override
	public AgencyAuditRec selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(AgencyAuditRec agencyAuditRec) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, agencyAuditRec);
	}

	@Override
	public int update(AgencyAuditRec agencyAuditRec) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, agencyAuditRec);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}
}
