package priv.starfish.mall.dao.mall.impl;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.mall.OperatorDao;
import priv.starfish.mall.mall.entity.Operator;

@Component("operatorDao")
public class OperatorDaoImpl extends BaseDaoImpl<Operator, Integer> implements OperatorDao {

	@Override
	public Operator selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public Operator selectTheOne() {
		String sqlId = this.getNamedSqlId("selectTheOne");
		//
		return this.getSqlSession().selectOne(sqlId);
	}

	@Override
	public int insert(Operator operator) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, operator);
	}

	@Override
	public int update(Operator operator) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, operator);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

}
