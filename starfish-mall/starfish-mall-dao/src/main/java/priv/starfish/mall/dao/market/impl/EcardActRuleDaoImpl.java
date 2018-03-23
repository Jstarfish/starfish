package priv.starfish.mall.dao.market.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import priv.starfish.common.util.MapContext;
import priv.starfish.mall.dao.market.EcardActRuleDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.market.entity.EcardActRule;

@Component("ecardActRuleDao")
public class EcardActRuleDaoImpl extends BaseDaoImpl<EcardActRule, Integer> implements EcardActRuleDao {
	@Override
	public EcardActRule selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(EcardActRule ecardActRule) {
		String sqlId = this.getNamedSqlId("insert");
		//
		if (ecardActRule.getSeqNo() == null) {
			Integer seqNo = getEntityMaxSeqNo(EcardActRule.class, "actId", ecardActRule.getActId()) + 1;
			ecardActRule.setSeqNo(seqNo);
		}
		return this.getSqlSession().insert(sqlId, ecardActRule);
	}

	@Override
	public int update(EcardActRule ecardActRule) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, ecardActRule);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<EcardActRule> selectByFilter(MapContext requestData) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		//
		return this.getSqlSession().selectList(sqlId, requestData);
	}
}
