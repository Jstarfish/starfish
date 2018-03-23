package priv.starfish.mall.dao.settle.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.settle.SettleOrderDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.settle.entity.SettleOrder;

@Component("settleOrderDao")
public class SettleOrderDaoImpl extends BaseDaoImpl<SettleOrder, Long> implements SettleOrderDao {
	@Override
	public SettleOrder selectById(Long id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(SettleOrder settleOrder) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, settleOrder);
	}

	@Override
	public int update(SettleOrder settleOrder) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, settleOrder);
	}

	@Override
	public int deleteById(Long id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<SettleOrder> selectBySettleProcessId(Long processId) {
		String sqlId = this.getNamedSqlId("selectBySettleProcessId");
		//
		return this.getSqlSession().selectList(sqlId, processId);
	}
}