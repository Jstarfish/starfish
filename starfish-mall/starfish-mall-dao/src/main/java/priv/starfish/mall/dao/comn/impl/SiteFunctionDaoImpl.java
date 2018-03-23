package priv.starfish.mall.dao.comn.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import priv.starfish.common.util.TypeUtil;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.comn.SiteFunctionDao;
import priv.starfish.mall.comn.entity.SiteFunction;

@Component("siteFunctionDao")
public class SiteFunctionDaoImpl extends BaseDaoImpl<SiteFunction, Integer> implements SiteFunctionDao {
	@Override
	public SiteFunction selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public SiteFunction selectByModuleIdAndName(Integer moduleId, String name) {
		String sqlId = this.getNamedSqlId("selectByModuleIdAndName");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("moduleId", moduleId);
		params.put("name", name);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(SiteFunction siteFunction) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, siteFunction);
	}

	@Override
	public int update(SiteFunction siteFunction) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, siteFunction);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<SiteFunction> selectByModuleId(Integer moduleId) {
		String sqlId = this.getNamedSqlId("selectByModuleId");
		//
		return this.getSqlSession().selectList(sqlId, moduleId);
	}

	@Override
	public List<SiteFunction> selectByFuncIds(List<Integer> funcIds) {
		if (funcIds == null || funcIds.size() == 0) {
			return TypeUtil.newEmptyList(SiteFunction.class);
		}
		String sqlId = this.getNamedSqlId("selectByFuncIds");
		//
		return this.getSqlSession().selectList(sqlId, funcIds);
	}

	@Override
	public List<Integer> selectModuleIdsByFuncIds(List<Integer> funcIds) {
		if (funcIds == null || funcIds.size() == 0) {
			return TypeUtil.newEmptyList(Integer.class);
		}
		String sqlId = this.getNamedSqlId("selectModuleIdsByFuncIds");
		//
		return this.getSqlSession().selectList(sqlId, funcIds);
	}

	@Override
	public List<SiteFunction> selectByFuncIdsAndModule(List<Integer> funcIds, Integer siteModuleId) {
		if (funcIds == null || funcIds.size() == 0) {
			return TypeUtil.newEmptyList(SiteFunction.class);
		}
		String sqlId = this.getNamedSqlId("selectByFuncIdsAndModule");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("funcIds", funcIds);
		params.put("siteModuleId", siteModuleId);
		//
		return this.getSqlSession().selectList(sqlId, params);
	}

}
