package priv.starfish.mall.dao.order.impl;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.order.ECardOrderItemDao;
import priv.starfish.mall.order.entity.ECardOrderItem;

@Component("eCardOrderItemDao")
public class ECardOrderItemDaoImpl extends BaseDaoImpl<ECardOrderItem, Integer> implements ECardOrderItemDao {
	@Override
	public ECardOrderItem selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(ECardOrderItem eCardOrderItem) {
		String sqlId = this.getNamedSqlId("insert");
		//
		Integer seqNo = getEntityMaxSeqNo(ECardOrderItem.class, "orderId", eCardOrderItem.getOrderId()) + 1;
		eCardOrderItem.setSeqNo(seqNo);
		return this.getSqlSession().insert(sqlId, eCardOrderItem);
	}

	@Override
	public int update(ECardOrderItem eCardOrderItem) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, eCardOrderItem);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}
}