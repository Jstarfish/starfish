package priv.starfish.mall.dao.mall.impl;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.mall.MallDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.mall.entity.Mall;

@Component("mallDao")
public class MallDaoImpl extends BaseDaoImpl<Mall, Integer> implements MallDao {

	@Override
	public Mall selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public Mall selectByCode(String code) {
		String sqlId = this.getNamedSqlId("selectByCode");
		return this.getSqlSession().selectOne(sqlId, code);
	}

	@Override
	public Mall selectTheOne() {
		String sqlId = this.getNamedSqlId("selectTheOne");
		return this.getSqlSession().selectOne(sqlId);
	}

	@Override
	public int insert(Mall mall) {
		String sqlId = this.getNamedSqlId("insert");
		return this.getSqlSession().insert(sqlId, mall);
	}

	@Override
	public int update(Mall mall) {
		String sqlId = this.getNamedSqlId("update");
		return this.getSqlSession().update(sqlId, mall);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		return this.getSqlSession().delete(sqlId, id);
	}

}
