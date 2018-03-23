package priv.starfish.mall.dao.market.impl;


import java.util.List;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.market.AdvertPosDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.market.entity.AdvertPos;


@Component("advertPosDao")
public class AdvertPosDaoImpl extends BaseDaoImpl<AdvertPos, String> implements AdvertPosDao {
	@Override
	public AdvertPos selectById(String code) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, code);
	}
	@Override
	public AdvertPos selectByName(String name) {
		String sqlId = this.getNamedSqlId("selectByName");
		//
		return this.getSqlSession().selectOne(sqlId, name);
	}
	@Override
	public int insert(AdvertPos advertPos) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, advertPos);
	}
	@Override
	public int update(AdvertPos advertPos) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, advertPos);
	}
	@Override
	public int deleteById(String code) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, code);
	}
	
	@Override
	public List<AdvertPos> selectAdvertPoss() {
		String sqlId = this.getNamedSqlId("selectAdvertPoss");
		return  this.getSqlSession().selectList(sqlId);
	}
}