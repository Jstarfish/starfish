package priv.starfish.mall.dao.comn.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.comn.SysParamDao;
import priv.starfish.mall.comn.entity.SysParam;

@Component("sysParamDao")
public class SysParamDaoImpl extends BaseDaoImpl<SysParam, String> implements SysParamDao {

	@Override
	public SysParam selectById(String code) {
		return selectByCode(code);
	}

	@Override
	public SysParam selectByCode(String code) {
		String sqlId = this.getNamedSqlId("selectByCode");
		//
		return this.getSqlSession().selectOne(sqlId, code);
	}

	@Override
	public SysParam selectByName(String name) {
		String sqlId = this.getNamedSqlId("selectByName");
		//
		return this.getSqlSession().selectOne(sqlId, name);
	}

	@Override
	public List<SysParam> selectAll() {
		String sqlId = this.getNamedSqlId("selectAll");
		//
		return this.getSqlSession().selectList(sqlId);
	}

	@Override
	public int insert(SysParam sysParam) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, sysParam);
	}

	@Override
	public int update(SysParam sysParam) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, sysParam);
	}

	@Override
	public int deleteByCode(String code) {
		String sqlId = this.getNamedSqlId("deleteByCode");
		//
		return this.getSqlSession().delete(sqlId, code);
	}

	@Override
	public int deleteById(String code) {
		return deleteByCode(code);
	}

}
