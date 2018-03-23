package priv.starfish.mall.dao.market.impl;

import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.jdbc.SqlBuilder;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.market.PrmtTagSvcDao;
import priv.starfish.mall.market.entity.PrmtTagSvc;

@Component("prmtTagSvcDao")
public class PrmtTagSvcDaoImpl extends BaseDaoImpl<PrmtTagSvc, Integer> implements PrmtTagSvcDao {
	@Override
	public PrmtTagSvc selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public PrmtTagSvc selectByTagIdAndSvcId(Integer tagId, Integer svcId) {
		String sqlId = this.getNamedSqlId("selectByTagIdAndSvcId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("tagId", tagId);
		params.put("svcId", svcId);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(PrmtTagSvc prmtTagSvc) {
		//
		int maxSeqNo = this.getEntityMaxSeqNo(PrmtTagSvc.class, "tagId", prmtTagSvc.getTagId());
		prmtTagSvc.setSeqNo(maxSeqNo + 1);
		//
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, prmtTagSvc);
	}

	@Override
	public int update(PrmtTagSvc prmtTagSvc) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, prmtTagSvc);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public int deleteByTagIdAndSvcId(Integer tagId, Integer svcId) {
		String sqlId = this.getNamedSqlId("deleteByTagIdAndSvcId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("tagId", tagId);
		params.put("svcId", svcId);
		//
		return this.getSqlSession().delete(sqlId, params);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<PrmtTagSvc> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		MapContext filter = paginatedFilter.getFilterItems();
		String name = filter.getTypedValue("name", String.class);
		if(StrUtil.hasText(name)){
			name = SqlBuilder.likeStrVal(name);
			filter.put("name", name);
		}
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<PrmtTagSvc> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}
}
