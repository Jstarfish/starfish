package priv.starfish.mall.dao.market.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import priv.starfish.common.util.MapContext;
import priv.starfish.mall.dao.market.EcardActGiftDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.market.entity.EcardActGift;

@Component("ecardActGiftDao")
public class EcardActGiftDaoImpl extends BaseDaoImpl<EcardActGift, Long> implements EcardActGiftDao {
	@Override
	public EcardActGift selectById(Long id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(EcardActGift ecardActGift) {
		String sqlId = this.getNamedSqlId("insert");
		//
		if (ecardActGift.getSeqNo() == null) {
			Integer seqNo = getEntityMaxSeqNo(EcardActGift.class, "actRuleId", ecardActGift.getActRuleId()) + 1;
			ecardActGift.setSeqNo(seqNo);
		}
		return this.getSqlSession().insert(sqlId, ecardActGift);
	}

	@Override
	public int update(EcardActGift ecardActGift) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, ecardActGift);
	}

	@Override
	public int deleteById(Long id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<EcardActGift> selectByFilter(MapContext requestDate) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		//
		return this.getSqlSession().selectList(sqlId, requestDate);
	}

	@Override
	public int deleteByActRuleId(Integer actRuleId) {
		String sqlId = this.getNamedSqlId("deleteByActRuleId");
		//
		return this.getSqlSession().delete(sqlId, actRuleId);
	}
}