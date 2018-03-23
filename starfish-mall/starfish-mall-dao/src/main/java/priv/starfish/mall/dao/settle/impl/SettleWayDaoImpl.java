package priv.starfish.mall.dao.settle.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.common.exception.XRuntimeException;
import priv.starfish.mall.dao.settle.SettleWayDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.settle.entity.SettleWay;

@Component("settleWayDao")
public class SettleWayDaoImpl extends BaseDaoImpl<SettleWay, String> implements SettleWayDao {
	@Override
	public SettleWay selectById(String code) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, code);
	}

	@Override
	public SettleWay selectByName(String name) {
		String sqlId = this.getNamedSqlId("selectByName");
		//
		return this.getSqlSession().selectOne(sqlId, name);
	}

	@Override
	public int insert(SettleWay settleWay) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, settleWay);
	}

	@Override
	public int update(SettleWay settleWay) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, settleWay);
	}

	@Override
	public int deleteById(String code) {
		throw new XRuntimeException("结算方式不支持删除操作！");
	}

	@Override
	public List<SettleWay> selectAll(boolean includeDisabled) {
		String sqlId = this.getNamedSqlId("selectAll");
		
		Map<String, Object> params = this.newParamMap();
		params.put("includeDisabled", includeDisabled);
		//
		return this.getSqlSession().selectList(sqlId, params);
	}
}