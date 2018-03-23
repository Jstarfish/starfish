package priv.starfish.mall.dao.categ.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.common.jdbc.SqlBuilder;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.categ.GoodsCatDao;
import priv.starfish.mall.categ.entity.GoodsCat;

@Component("goodsCatDao")
public class GoodsCatDaoImpl extends BaseDaoImpl<GoodsCat, Integer> implements GoodsCatDao {
	@Override
	public GoodsCat selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public GoodsCat selectByNameAndLevel(String name, Integer level) {
		String sqlId = this.getNamedSqlId("selectByNameAndLevel");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("name", name);
		params.put("level", level);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(GoodsCat goodsCat) {
		String sqlId = this.getNamedSqlId("insert");
		//
		int maxSeqNo = this.getEntityMaxSeqNo(GoodsCat.class);
		goodsCat.setSeqNo(maxSeqNo+1);
		//
		return this.getSqlSession().insert(sqlId, goodsCat);
	}

	@Override
	public int update(GoodsCat goodsCat) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, goodsCat);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public int deleteByIds(List<Integer> ids) {
		if (null != ids && ids.size() > 0) {
			String sqlId = this.getNamedSqlId("deleteByIds");
			return this.getSqlSession().delete(sqlId, ids);
		} else {
			return 0;
		}

	}

	@Override
	public List<GoodsCat> selectByParentId(Integer parentId) {
		String sqlId = this.getNamedSqlId("selectByParentId");
		return this.getSqlSession().selectList(sqlId, parentId);
	}

	@Override
	public List<GoodsCat> selectByName(String name) {
		String sqlId = this.getNamedSqlId("selectByName");

		//
		Map<String, Object> params = this.newParamMap();
		params.put("name", SqlBuilder.likeStrVal(name));
		return this.getSqlSession().selectList(sqlId, params);
	}

	@Override
	public List<GoodsCat> selectByLevel(Integer level) {
		String sqlId = this.getNamedSqlId("selectByLevel");
		return this.getSqlSession().selectList(sqlId, level);
	}

	@Override
	public int updateLeafFlagById(boolean leafFlag, Integer id) {
		String sqlId = this.getNamedSqlId("updateLeafFlagById");
		Map<String, Object> params = this.newParamMap();
		params.put("leafFlag", leafFlag);
		params.put("id", id);
		//
		return this.getSqlSession().update(sqlId, params);
	}
}