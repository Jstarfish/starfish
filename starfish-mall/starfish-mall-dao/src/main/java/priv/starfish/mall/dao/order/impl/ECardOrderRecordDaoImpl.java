package priv.starfish.mall.dao.order.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.order.ECardOrderRecordDao;
import priv.starfish.mall.order.entity.ECardOrderRecord;

@Component("eCardOrderRecordDao")
public class ECardOrderRecordDaoImpl extends BaseDaoImpl<ECardOrderRecord, Long> implements ECardOrderRecordDao {
	@Override
	public ECardOrderRecord selectById(Long id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(ECardOrderRecord eCardOrderRecord) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, eCardOrderRecord);
	}

	@Override
	public int update(ECardOrderRecord eCardOrderRecord) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, eCardOrderRecord);
	}

	@Override
	public int deleteById(Long id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<ECardOrderRecord> selectByOrderId(Integer orderId) {
		String sqlId = this.getNamedSqlId("selectByOrderId");
		//
		return this.getSqlSession().selectList(sqlId, orderId);
	}
}