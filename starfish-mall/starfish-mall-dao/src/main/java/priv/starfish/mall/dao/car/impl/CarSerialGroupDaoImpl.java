package priv.starfish.mall.dao.car.impl;

import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.car.CarSerialGroupDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.car.entity.CarSerialGroup;

@Component("carSerialGroupDao")
public class CarSerialGroupDaoImpl extends BaseDaoImpl<CarSerialGroup, Integer> implements CarSerialGroupDao {
	@Override
	public CarSerialGroup selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public CarSerialGroup selectByBrandIdAndName(Integer brandId, String name) {
		String sqlId = this.getNamedSqlId("selectByBrandIdAndName");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("brandId", brandId);
		params.put("name", name);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(CarSerialGroup carSerialGroup) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, carSerialGroup);
	}

	@Override
	public int update(CarSerialGroup carSerialGroup) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, carSerialGroup);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public CarSerialGroup selectByRefId(Integer refId) {
		String sqlId = this.getNamedSqlId("selectByRefId");
		//
		return this.getSqlSession().selectOne(sqlId, refId);
	}
}