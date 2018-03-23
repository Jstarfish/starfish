package priv.starfish.mall.dao.categ.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.categ.GoodsCatSpecItemDao;
import priv.starfish.mall.categ.entity.GoodsCatSpecItem;

@Component("goodsCatSpecItemDao")
public class GoodsCatSpecItemDaoImpl extends BaseDaoImpl<GoodsCatSpecItem, Integer> implements GoodsCatSpecItemDao {
	@Override
	public GoodsCatSpecItem selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public GoodsCatSpecItem selectBySpecIdAndValue(Integer specId, String value) {
		String sqlId = this.getNamedSqlId("selectBySpecIdAndValue");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("specId", specId);
		params.put("value", value);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(GoodsCatSpecItem goodsCatSpecItem) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, goodsCatSpecItem);
	}

	@Override
	public int update(GoodsCatSpecItem goodsCatSpecItem) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, goodsCatSpecItem);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public int deleteBySpecId(Integer specId) {
		String sqlId = this.getNamedSqlId("deleteBySpecId");
		return this.getSqlSession().delete(sqlId, specId);
	}

	@Override
	public int deleteBySpecIds(List<Integer> specIds) {
		if (specIds != null && specIds.size() > 0) {
			String sqlId = this.getNamedSqlId("deleteBySpecIds");
			return this.getSqlSession().delete(sqlId, specIds);
		} else {
			return 0;
		}
	}

	@Override
	public List<GoodsCatSpecItem> selectBySpecId(Integer specId) {
		String sqlId = this.getNamedSqlId("selectBySpecId");
		//
		return this.getSqlSession().selectList(sqlId, specId);
	}

	@Override
	public List<GoodsCatSpecItem> selectBySpecId(Integer specId, List<Integer> uncontainIds) {
		String sqlId = this.getNamedSqlId("selectBySpecIdAndUncontainIds");
		Map<String, Object> params = this.newParamMap();
		params.put("specId", specId);
		params.put("uncontainIds", uncontainIds);
		return this.getSqlSession().selectList(sqlId, params);
	}

	@Override
	public int deleteBySpecIdAndUncontainIds(Integer specId, List<Integer> uncontainIds) {
		String sqlId = this.getNamedSqlId("deleteBySpecIdAndUncontainIds");
		Map<String, Object> params = this.newParamMap();
		params.put("specId", specId);
		params.put("uncontainIds", uncontainIds);
		return this.getSqlSession().delete(sqlId, params);
	}

	@Override
	public List<GoodsCatSpecItem> selectGoodsCatSpecItemBySpecItemIds(List<Integer> list) {
		String sqlId = this.getNamedSqlId("selectGoodsCatSpecItemBySpecItemIds");
		return this.getSqlSession().selectList(sqlId, list);
	}

}
