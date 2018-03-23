package priv.starfish.mall.dao.svcx.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.svcx.SvcKindDao;
import priv.starfish.mall.svcx.entity.SvcKind;

@Component("svcKindDao")
public class SvcKindDaoImpl extends BaseDaoImpl<SvcKind, Integer> implements SvcKindDao {
	@Override
	public SvcKind selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public SvcKind selectByName(String name) {
		String sqlId = this.getNamedSqlId("selectByName");
		//
		return this.getSqlSession().selectOne(sqlId, name);
	}

	@Override
	public int insert(SvcKind svcKind) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, svcKind);
	}

	@Override
	public int update(SvcKind svcKind) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, svcKind);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<SvcKind> selectByFilter() {
		String sqlId = this.getNamedSqlId("selectByFilter");
		//
		return this.getSqlSession().selectList(sqlId);
	}
}
