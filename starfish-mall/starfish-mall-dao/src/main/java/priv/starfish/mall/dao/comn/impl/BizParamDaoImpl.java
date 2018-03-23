package priv.starfish.mall.dao.comn.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.comn.BizParamDao;
import priv.starfish.mall.comn.entity.BizParam;

@Component("bizParamDao")
public class BizParamDaoImpl extends BaseDaoImpl<BizParam, String> implements BizParamDao {

	@Override
	public BizParam selectById(String code) {
		String sqlId = this.getNamedSqlId("selectById");
		return this.getSqlSession().selectOne(sqlId, code);
	}

	@Override
	public BizParam selectByName(String name) {
		String sqlId = this.getNamedSqlId("selectByName");
		return this.getSqlSession().selectOne(sqlId, name);
	}

	@Override
	public List<BizParam> selectAll() {
		String sqlId = this.getNamedSqlId("selectAll");
		return this.getSqlSession().selectList(sqlId);
	}

	@Override
	public int insert(BizParam bizParam) {
		String sqlId = this.getNamedSqlId("insert");
		return this.getSqlSession().insert(sqlId, bizParam);
	}

	@Override
	public int update(BizParam bizParam) {
		String sqlId = this.getNamedSqlId("update");
		return this.getSqlSession().update(sqlId, bizParam);
	}

	@Override
	public int deleteById(String code) {
		String sqlId = this.getNamedSqlId("deleteById");
		return this.getSqlSession().delete(sqlId, code);
	}
}