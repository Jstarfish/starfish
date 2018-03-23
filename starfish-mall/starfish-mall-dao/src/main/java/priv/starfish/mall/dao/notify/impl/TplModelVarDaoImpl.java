package priv.starfish.mall.dao.notify.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.notify.TplModelVarDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.notify.entity.TplModelVar;

@Component("tplModelVarDao")
public class TplModelVarDaoImpl extends BaseDaoImpl<TplModelVar, Integer> implements TplModelVarDao {
	@Override
	public TplModelVar selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public TplModelVar selectByModelCodeAndExpr(String modelCode, String expr) {
		String sqlId = this.getNamedSqlId("selectByModelCodeAndExpr");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("modelCode", modelCode);
		params.put("expr", expr);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(TplModelVar tplModelVar) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, tplModelVar);
	}

	@Override
	public int update(TplModelVar tplModelVar) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, tplModelVar);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<TplModelVar> selectListByModelCode(String modelCode) {
		String sqlId = this.getNamedSqlId("selectListByModelCode");
		return this.getSqlSession().selectList(sqlId, modelCode);
	}
}