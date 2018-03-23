package priv.starfish.mall.dao.comn.impl;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.comn.AgreementDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.comn.entity.Agreement;

@Component("agreementDao")
public class AgreementDaoImpl extends BaseDaoImpl<Agreement, Integer> implements AgreementDao {

	@Override
	public Agreement selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public Agreement selectByTarget(String target) {
		String sqlId = this.getNamedSqlId("selectByTarget");
		//
		return this.getSqlSession().selectOne(sqlId, target);
	}

	@Override
	public int insert(Agreement agreement) {
		String sqlId = this.getNamedSqlId("insert");

		if (agreement.getSeqNo() == null) {
			Integer seqNo = getEntityMaxSeqNo(Agreement.class) + 1;
			agreement.setSeqNo(seqNo);
		}

		return this.getSqlSession().insert(sqlId, agreement);
	}

	@Override
	public int update(Agreement agreement) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, agreement);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}
}