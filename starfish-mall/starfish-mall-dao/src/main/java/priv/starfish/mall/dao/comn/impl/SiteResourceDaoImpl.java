package priv.starfish.mall.dao.comn.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import priv.starfish.common.util.TypeUtil;
import priv.starfish.mall.dao.comn.SiteResourceDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.comn.entity.Resource;
import priv.starfish.mall.comn.entity.SiteResource;

@Service("siteResourceDao")
public class SiteResourceDaoImpl extends BaseDaoImpl<SiteResource, Integer> implements SiteResourceDao {
	@Override
	public SiteResource selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public SiteResource selectByFuncIdAndResId(Integer funcId, Integer resId) {
		String sqlId = this.getNamedSqlId("selectByFuncIdAndResId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("funcId", funcId);
		params.put("resId", resId);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(SiteResource siteResource) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, siteResource);
	}

	@Override
	public int update(SiteResource siteResource) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, siteResource);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<SiteResource> selectByFuncIdAndResIds(Integer funcId, List<Integer> resIds) {
		if (resIds == null || resIds.size() == 0) {
			return TypeUtil.newEmptyList(SiteResource.class);
		}
		String sqlId = this.getNamedSqlId("selectByFuncIdAndResIds");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("funcId", funcId);
		params.put("resIds", resIds);
		//
		return this.getSqlSession().selectList(sqlId, params);
	}

	@Override
	public int deleteByFuncIdAndResIds(Integer funcId, List<Integer> resIds) {
		if (TypeUtil.isNullOrEmpty(resIds)) {
			return 0;
		}
		String sqlId = this.getNamedSqlId("deleteByFuncIdAndResIds");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("funcId", funcId);
		params.put("resIds", resIds);
		//
		return this.getSqlSession().delete(sqlId, params);
	}

	@Override
	public List<SiteResource> selectByFuncId(Integer funcId) {
		String sqlId = this.getNamedSqlId("selectByFuncId");
		return this.getSqlSession().selectList(sqlId, funcId);
	}

	@Override
	public int deleteByFuncId(Integer funcId) {
		String sqlId = this.getNamedSqlId("deleteByFuncId");
		//
		return this.getSqlSession().delete(sqlId, funcId);
	}

	@Override
	public List<SiteResource> selectByResId(Integer resId) {
		String sqlId = this.getNamedSqlId("selectByResId");
		//
		return this.getSqlSession().selectList(sqlId, resId);
	}

	@Override
	public List<SiteResource> selectByResIds(List<Integer> resIds) {
		if (TypeUtil.isNullOrEmpty(resIds)) {
			return TypeUtil.newEmptyList(SiteResource.class);
		}
		String sqlId = this.getNamedSqlId("selectByResIds");
		//
		return this.getSqlSession().selectList(sqlId, resIds);
	}

	@Override
	public List<Integer> selectFuncIdsByResIds(List<Integer> resIds) {
		if (TypeUtil.isNullOrEmpty(resIds)) {
			return TypeUtil.newEmptyList(Integer.class);
		}
		String sqlId = this.getNamedSqlId("selectFuncIdsByResIds");
		//
		return this.getSqlSession().selectList(sqlId, resIds);
	}

	@Override
	public List<Resource> selectResourcesByFuncId(Integer funcId) {
		String sqlId = this.getNamedSqlId("selectResourcesByFuncId");
		//
		return this.getSqlSession().selectList(sqlId, funcId);
	}

	@Override
	public int deleteByResId(Integer resId) {
		String sqlId = this.getNamedSqlId("deleteByResId");
		return this.getSqlSession().delete(sqlId, resId);
	}

	@Override
	public int deleteByResIds(List<Integer> resIds) {
		String sqlId = this.getNamedSqlId("deleteByResIds");
		return this.getSqlSession().delete(sqlId, resIds);
	}

}