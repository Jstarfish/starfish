package priv.starfish.mall.dao.logistic.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.logistic.LogisApiParamDao;
import priv.starfish.mall.logistic.entity.LogisApiParam;

@Component("LogisApiParamDao")
public class LogisApiParamDaoImpl extends BaseDaoImpl<LogisApiParam, Integer> implements LogisApiParamDao {

	@Override
	public LogisApiParam selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(LogisApiParam logisApiParam) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, logisApiParam);
	}

	@Override
	public int update(LogisApiParam logisApiParam) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, logisApiParam);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public int batchInsertLogisApiParams(List<LogisApiParam> ps) {
		//
		int maxSeqNo = getEntityMaxSeqNo(LogisApiParam.class);
		for(LogisApiParam p : ps){
			int seqNo = maxSeqNo++;
			p.setSeqNo(seqNo);
		}
		//
		String sqlId = this.getNamedSqlId("batchInsert");
		Map<String, Object> params = this.newParamMap();
		params.put("ps", ps);
		return this.getSqlSession().update(sqlId, params);
	}

	@Override
	public List<LogisApiParam> selectByApiId(Integer logisApiId) {
		String sqlId = this.getNamedSqlId("selectByApiId");
		//
		return this.getSqlSession().selectList(sqlId, logisApiId);
	}

	@Override
	public int deleteByApiId(Integer apiId) {
		String sqlId = this.getNamedSqlId("deleteByApiId");
		//
		return this.getSqlSession().delete(sqlId, apiId);
	}

	@Override
	public LogisApiParam selectByApiIdAndNameAndIoFlag(Integer apiId, String name, Integer ioFlag) {
		String sqlId = this.getNamedSqlId("selectByApiIdAndNameAndIoFlag");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("apiId", apiId);
		params.put("name", name);
		params.put("ioFlag", ioFlag);
		return this.getSqlSession().selectOne(sqlId, params);
	}

}
