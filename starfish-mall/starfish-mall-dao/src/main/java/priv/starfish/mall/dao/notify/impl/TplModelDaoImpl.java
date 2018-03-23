package priv.starfish.mall.dao.notify.impl;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;

import priv.starfish.common.jdbc.SqlBuilder;
import priv.starfish.mall.dao.notify.TplModelDao;
import priv.starfish.mall.dao.notify.TplModelVarDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.notify.entity.TplModel;
import priv.starfish.mall.notify.entity.TplModelVar;

@Component("tplModelDao")
public class TplModelDaoImpl extends BaseDaoImpl<TplModel, Integer> implements TplModelDao {

	@Resource
	TplModelVarDao tplModelVarDao;

	@Override
	public TplModel selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public TplModel selectByCode(String code) {
		String sqlId = this.getNamedSqlId("selectByCode");
		//
		TplModel tplModel = this.getSqlSession().selectOne(sqlId, code);
		List<TplModelVar> varList = tplModelVarDao.selectListByModelCode(code);
		tplModel.setVarList(varList);
		return tplModel;
	}

	@Override
	public int insert(TplModel tplModel) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, tplModel);
	}

	@Override
	public int update(TplModel tplModel) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, tplModel);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<TplModel> selectAll(String code) {
		String sqlId = this.getNamedSqlId("selectAll");
		Map<String, Object> params = this.newParamMap();
		code = SqlBuilder.likeStrVal(code);
		params.put("code", code);
		return this.getSqlSession().selectList(sqlId, params);
	}
}