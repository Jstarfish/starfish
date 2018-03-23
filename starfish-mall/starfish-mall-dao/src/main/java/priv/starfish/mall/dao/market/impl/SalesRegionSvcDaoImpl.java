package priv.starfish.mall.dao.market.impl;

import java.util.List;
import java.util.Map;


import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.mall.dao.market.SalesRegionSvcDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.market.entity.SalesRegionSvc;

@Component("salesRegionSvcDao")
public class SalesRegionSvcDaoImpl extends BaseDaoImpl<SalesRegionSvc, Integer> implements SalesRegionSvcDao {
	@Override
	public SalesRegionSvc selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public SalesRegionSvc selectByRegionIdAndSvcId(Integer regionId, Integer svcId) {
		String sqlId = this.getNamedSqlId("selectByRegionIdAndSvcId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("regionId", regionId);
		params.put("svcId", svcId);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(SalesRegionSvc salesRegionSvc) {
		String sqlId = this.getNamedSqlId("insert");
		//
		if (salesRegionSvc.getSeqNo() == null) {
			Integer seqNo = getEntityMaxSeqNo(SalesRegionSvc.class);
			salesRegionSvc.setSeqNo(seqNo);
		}
		//
		return this.getSqlSession().insert(sqlId, salesRegionSvc);
	}

	@Override
	public int update(SalesRegionSvc salesRegionSvc) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, salesRegionSvc);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PaginatedList<SalesRegionSvc> selectSalesRegionSvc(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectSalesRegionSvc");
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<SalesRegionSvc> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}
	
	@Override
	public List<SalesRegionSvc> selectByRegionId(Integer regionId) {
		String sqlId = this.getNamedSqlId("selectByRegionId");
		return this.getSqlSession().selectList(sqlId, regionId);
	}

	@Override
	public int deleteByRegionId(Integer regionId) {
		String sqlId = this.getNamedSqlId("deleteByRegionId");
		return this.getSqlSession().delete(sqlId, regionId);
	}
}