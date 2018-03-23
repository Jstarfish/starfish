package priv.starfish.mall.dao.pay.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.pay.PayWayParamDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.pay.entity.PayWayParam;

@Component("PayWayParamDao")
public class PayWayParamDaoImpl extends BaseDaoImpl<PayWayParam, Integer> implements PayWayParamDao {

	@Override
	public PayWayParam selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(PayWayParam payWayParam) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, payWayParam);
	}

	@Override
	public int update(PayWayParam payWayParam) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, payWayParam);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public int batchInsertPayWayParams(List<PayWayParam> ps) {
		//
		int maxSeqNo = getEntityMaxSeqNo(PayWayParam.class);
		for (PayWayParam p : ps) {
			int seqNo = maxSeqNo++;
			p.setSeqNo(seqNo);
		}
		//
		String sqlId = this.getNamedSqlId("batchInsertPayWayParams");
		Map<String, Object> params = this.newParamMap();
		params.put("ps", ps);
		return this.getSqlSession().update(sqlId, params);
	}

	@Override
	public List<PayWayParam> selectByPWayId(Integer payWayId) {
		String sqlId = this.getNamedSqlId("selectByPWayId");
		//
		return this.getSqlSession().selectList(sqlId, payWayId);
	}

	@Override
	public int deleteByPWayId(Integer payWayId) {
		String sqlId = this.getNamedSqlId("deleteByPWayId");
		//
		return this.getSqlSession().delete(sqlId, payWayId);
	}

	@Override
	public PayWayParam selectByPWayIdAndNameAndIoFlag(Integer pWayId, String name) {
		String sqlId = this.getNamedSqlId("selectByPWayIdAndNameAndIoFlag");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("apiId", pWayId);
		params.put("name", name);
		return this.getSqlSession().selectOne(sqlId, params);
	}

}
