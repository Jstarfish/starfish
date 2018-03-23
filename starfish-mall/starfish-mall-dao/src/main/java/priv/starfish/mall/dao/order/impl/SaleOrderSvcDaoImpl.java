package priv.starfish.mall.dao.order.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.order.SaleOrderSvcDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.order.entity.SaleOrderSvc;

@Component("saleOrderSvcDao")
public class SaleOrderSvcDaoImpl extends BaseDaoImpl<SaleOrderSvc, Long> implements SaleOrderSvcDao {
	@Override
	public SaleOrderSvc selectById(Long id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(SaleOrderSvc saleOrderSvc) {
		String sqlId = this.getNamedSqlId("insert");
		if (saleOrderSvc.getSeqNo() == null) {
			Integer seqNo = getEntityMaxSeqNo(SaleOrderSvc.class, "orderId", saleOrderSvc.getOrderId()) + 1;
			saleOrderSvc.setSeqNo(seqNo);
		}
		return this.getSqlSession().insert(sqlId, saleOrderSvc);
	}

	@Override
	public int update(SaleOrderSvc saleOrderSvc) {
		String sqlId = this.getNamedSqlId("update");	
		return this.getSqlSession().update(sqlId, saleOrderSvc);
	}

	@Override
	public int deleteById(Long id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<SaleOrderSvc> selectByOrderId(Long orderId) {
		String sqlId=this.getNamedSqlId("selectByOrderId");
		return this.getSqlSession().selectList(sqlId,orderId);
	}
}