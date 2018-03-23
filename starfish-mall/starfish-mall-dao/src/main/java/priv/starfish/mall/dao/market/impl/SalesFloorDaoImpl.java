package priv.starfish.mall.dao.market.impl;


import java.util.List;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.jdbc.SqlBuilder;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.dao.market.SalesFloorDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.market.entity.SalesFloor;


@Component("salesFloorDao")
public class SalesFloorDaoImpl extends BaseDaoImpl<SalesFloor, Integer> implements SalesFloorDao {
	@Override
	public SalesFloor selectById(Integer no) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, no);
	}
	@Override
	public SalesFloor selectByName(String name) {
		String sqlId = this.getNamedSqlId("selectByName");
		//
		return this.getSqlSession().selectOne(sqlId, name);
	}
	@Override
	public int insert(SalesFloor salesFloor) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, salesFloor);
	}
	@Override
	public int update(SalesFloor salesFloor) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, salesFloor);
	}
	@Override
	public int deleteById(Integer no) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, no);
	}

	@Override
	public PaginatedList<SalesFloor> selectSalesFloors(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectSalesFloors");
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
		PageList<SalesFloor> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}
	@Override
	public List<SalesFloor> selectByType(Integer type) {
		String sqlId = this.getNamedSqlId("selectByType");
		//
		return this.getSqlSession().selectList(sqlId, type);
	}
}