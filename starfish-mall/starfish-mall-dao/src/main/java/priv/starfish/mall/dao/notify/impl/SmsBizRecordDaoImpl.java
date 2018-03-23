package priv.starfish.mall.dao.notify.impl;

import java.math.BigInteger;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.notify.SmsBizRecordDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.notify.entity.SmsBizRecord;

@Component("smsBizRecordDao")
public class SmsBizRecordDaoImpl extends BaseDaoImpl<SmsBizRecord, BigInteger> implements SmsBizRecordDao {
	@Override
	public SmsBizRecord selectById(BigInteger id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(SmsBizRecord smsBizRecord) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, smsBizRecord);
	}

	@Override
	public int update(SmsBizRecord smsBizRecord) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, smsBizRecord);
	}

	@Override
	public int deleteById(BigInteger id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}
}
