package priv.starfish.mall.dao.comn.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.comn.ModuleDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.comn.dict.AuthScope;
import priv.starfish.mall.comn.entity.Module;

@Component("moduleDao")
public class ModuleDaoImpl extends BaseDaoImpl<Module, Integer> implements ModuleDao {
	@Override
	public Module selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public Module selectByName(String name) {
		String sqlId = this.getNamedSqlId("selectByName");
		//
		return this.getSqlSession().selectOne(sqlId, name);
	}

	@Override
	public int insert(Module module) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, module);
	}

	@Override
	public int update(Module module) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, module);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<Module> selectByScope(AuthScope scope) {
		String sqlId = this.getNamedSqlId("selectAllByScope");
		//
		return this.getSqlSession().selectList(sqlId, scope.name());
	}

}