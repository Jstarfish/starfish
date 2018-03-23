package priv.starfish.mall.dao.order.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.order.SaleOrderRecordDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.order.entity.SaleOrderRecord;

@Component("saleOrderRecordDao")
public class SaleOrderRecordDaoImpl extends BaseDaoImpl<SaleOrderRecord, Long> implements SaleOrderRecordDao {
	@Override
	public SaleOrderRecord selectById(Long id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(SaleOrderRecord saleOrderRecord) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, saleOrderRecord);
	}

	@Override
	public int update(SaleOrderRecord saleOrderRecord) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, saleOrderRecord);
	}

	@Override
	public int deleteById(Long id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<SaleOrderRecord> selectByOrderId(Long orderId) {
		String sqlId = this.getNamedSqlId("selectByOrderId");
		//
		return this.getSqlSession().selectList(sqlId, orderId);
	}
}