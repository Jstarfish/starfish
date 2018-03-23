package priv.starfish.mall.dao.notify.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.notify.SmsApiParamDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.notify.entity.SmsApiParam;

@Component("smsApiParamDao")
public class SmsApiParamDaoImpl extends BaseDaoImpl<SmsApiParam, Integer> implements SmsApiParamDao {

	@Override
	public SmsApiParam selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public SmsApiParam selectByApiIdAndNameAndIoFlag(Integer apiId, String name, Integer ioFlag) {
		String sqlId = this.getNamedSqlId("selectByApiIdAndNameAndIoFlag");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("apiId", apiId);
		params.put("name", name);
		params.put("ioFlag", ioFlag);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(SmsApiParam smsApiParam) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, smsApiParam);
	}

	@Override
	public int update(SmsApiParam smsApiParam) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, smsApiParam);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<SmsApiParam> selectByApiId(Integer id) {
		String sqlId = this.getNamedSqlId("selectByApiId");
		//
		return this.getSqlSession().selectList(sqlId, id);
	}

	@Override
	public int deleteByApiId(Integer apiId) {
		String sqlId = this.getNamedSqlId("deleteByApiId");
		//
		return this.getSqlSession().delete(sqlId, apiId);
	}

	@Override
	public List<Integer> selectParamIdsByApiId(Integer apiId) {
		String sqlId = this.getNamedSqlId("selectParamIdsByApiId");
		//
		return this.getSqlSession().selectList(sqlId, apiId);
	}

	@Override
	public int batchInsert(List<SmsApiParam> ps) {
		String sqlId = this.getNamedSqlId("batchInsert");
		Map<String, Object> params = this.newParamMap();
		params.put("ps", ps);
		return this.getSqlSession().update(sqlId, params);
	}

	@Override
	public int batchDelete(List<Integer> paramIds) {
		String sqlId = this.getNamedSqlId("batchDelete");
		Map<String, Object> params = this.newParamMap();
		params.put("ids", paramIds);
		return this.getSqlSession().delete(sqlId, params);
	}

}
