package priv.starfish.mall.dao.order.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.order.SaleOrderWorkDao;
import priv.starfish.mall.order.entity.SaleOrderWork;

@Component("saleOrderWorkDao")
public class SaleOrderWorkDaoImpl extends BaseDaoImpl<SaleOrderWork, Long> implements SaleOrderWorkDao {
	@Override
	public SaleOrderWork selectById(Long id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public SaleOrderWork selectByOrderIdAndWorkerId(Long orderId, Integer workerId) {
		String sqlId = this.getNamedSqlId("selectByOrderIdAndWorkerId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("orderId", orderId);
		params.put("workerId", workerId);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(SaleOrderWork saleOrderWork) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, saleOrderWork);
	}

	@Override
	public int update(SaleOrderWork saleOrderWork) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, saleOrderWork);
	}

	@Override
	public int deleteById(Long id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<SaleOrderWork> selectByOrderId(Long orderId) {
		String sqlId = this.getNamedSqlId("selectByOrderId");
		//
		return this.getSqlSession().selectList(sqlId, orderId);
	}

	@Override
	public int deleteByOrderId(Long orderId) {
		String sqlId = this.getNamedSqlId("deleteByOrderId");
		//
		return this.getSqlSession().delete(sqlId, orderId);
	}
}