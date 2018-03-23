package priv.starfish.mall.dao.svcx.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.svcx.SvcxRankDiscDao;
import priv.starfish.mall.svcx.entity.SvcxRankDisc;

@Component("svcxRankDiscDao")
public class SvcxRankDiscDaoImpl extends BaseDaoImpl<SvcxRankDisc, Integer> implements SvcxRankDiscDao {
	@Override
	public SvcxRankDisc selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public SvcxRankDisc selectByShopIdAndSvcIdAndRank(Integer shopId, Integer svcId, Integer rank) {
		String sqlId = this.getNamedSqlId("selectByShopIdAndSvcIdAndRank");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("shopId", shopId);
		params.put("svcId", svcId);
		params.put("rank", rank);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(SvcxRankDisc svcxRankDisc) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, svcxRankDisc);
	}

	@Override
	public int update(SvcxRankDisc svcxRankDisc) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, svcxRankDisc);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<SvcxRankDisc> selectByShopIdAndSvcIdAndRank(Integer shopId, Integer svcId) {
		String sqlId = this.getNamedSqlId("selectByShopIdAndSvcId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("shopId", shopId);
		params.put("svcId", svcId);
		//
		return this.getSqlSession().selectList(sqlId, params);
	}
}