package priv.starfish.mall.dao.market.impl;


import java.util.List;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.market.AdvertLinkDao;
import priv.starfish.mall.market.entity.AdvertLink;


@Component("advertLinkDao")
public class AdvertLinkDaoImpl extends BaseDaoImpl<AdvertLink, Integer> implements AdvertLinkDao {
	@Override
	public AdvertLink selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}
	@Override
	public int insert(AdvertLink advertLink) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, advertLink);
	}
	@Override
	public int update(AdvertLink advertLink) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, advertLink);
	}
	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}
	
	@Override
	public List<AdvertLink> selectByAdvertId(Integer advertId) {
		String sqlId = this.getNamedSqlId("selectByAdvertId");
		return this.getSqlSession().selectList(sqlId, advertId);
	}
	@Override
	public AdvertLink selectMinMaxDate(Integer advertId) {
		String sqlId = this.getNamedSqlId("selectMinMaxDate");
		return this.getSqlSession().selectOne(sqlId, advertId);
	}
}