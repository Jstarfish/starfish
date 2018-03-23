package priv.starfish.mall.dao.comn.impl;

import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.comn.AreaDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.comn.entity.Area;

@Component("areaDao")
public class AreaDaoImpl extends BaseDaoImpl<Area, Integer> implements AreaDao {
	@Override
	public Area selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public Area selectByNameAndRegionId(String name, Integer regionId) {
		String sqlId = this.getNamedSqlId("selectByNameAndRegionId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("name", name);
		params.put("regionId", regionId);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(Area area) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, area);
	}

	@Override
	public int update(Area area) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, area);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}
}