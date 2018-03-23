package priv.starfish.mall.dao.market.impl;


import java.util.List;
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
import priv.starfish.mall.dao.market.SalesRegionDao;
import priv.starfish.mall.market.entity.SalesFloor;
import priv.starfish.mall.market.entity.SalesRegion;

@Component("salesRegionDao")
public class SalesRegionDaoImpl extends BaseDaoImpl<SalesRegion, Integer> implements SalesRegionDao {
	@Override
	public SalesRegion selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public SalesRegion selectByNameAndFloorNo(String name, Integer floorNo) {
		String sqlId = this.getNamedSqlId("selectByNameAndFloorNo");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("name", name);
		params.put("floorNo", floorNo);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(SalesRegion salesRegion) {
		String sqlId = this.getNamedSqlId("insert");
		//
		if (salesRegion.getSeqNo() == null) {
			Integer seqNo = getEntityMaxSeqNo(SalesFloor.class, "no", salesRegion.getFloorNo()) + 1;
			salesRegion.setSeqNo(seqNo);
		}
		//
		return this.getSqlSession().insert(sqlId, salesRegion);
	}

	@Override
	public int update(SalesRegion salesRegion) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, salesRegion);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<SalesRegion> selectByFloorNo(Integer floorNo) {
		String sqlId = this.getNamedSqlId("selectByFloorNo");
		return this.getSqlSession().selectList(sqlId, floorNo);
	}

	@Override
	public int deleteByFloorNo(Integer floorNo) {
		String sqlId = this.getNamedSqlId("deleteByFloorNo");
		return this.getSqlSession().delete(sqlId, floorNo);
	}

	@Override
	public int deleteByFloorNoAndUncontainIds(Integer floorNo, List<Integer> uncontainIds) {
		if (uncontainIds.size() == 0) {
			uncontainIds = null;
		}
		String sqlId = this.getNamedSqlId("deleteByFloorNoAndUncontainIds");
		Map<String, Object> params = this.newParamMap();
		params.put("floorNo", floorNo);
		params.put("uncontainIds", uncontainIds);
		return this.getSqlSession().delete(sqlId, params);
	}

	@Override
	public PaginatedList<SalesRegion> selectSalesRegions(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectSalesRegions");
		//
		MapContext filterItems = paginatedFilter.getFilterItems();
		//
		String name = filterItems.getTypedValue("name", String.class);
		filterItems.remove("name");
		if (StrUtil.hasText(name)) {
			name = SqlBuilder.likeStrVal(name);
			filterItems.put("name", name);
		}
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		@SuppressWarnings({ "unchecked", "rawtypes" })
		PageList<SalesRegion> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public List<SalesRegion> selectByType(Integer type) {
		String sqlId = this.getNamedSqlId("selectByType");
		//
		return this.getSqlSession().selectList(sqlId, type);
	}
}
