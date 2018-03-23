package priv.starfish.mall.dao.statis.impl;

import java.math.BigDecimal;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.dao.statis.DistShopSumDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.order.entity.SaleOrder;

@Component("distShopSumDao")
public class DistShopSumDaoImpl extends BaseDaoImpl<SaleOrder, Long> implements DistShopSumDao {

	@Override
	public Long getDistShopOrderStatis(MapContext filter) {
		String sqlId = this.getNamedSqlId("getDistShopOrderStatis");
		return this.getSqlSession().selectOne(sqlId, filter);
	}

	@Override
	public Long getDistShopSvcStatis(MapContext filter) {
		String sqlId = this.getNamedSqlId("getDistShopSvcStatis");
		return this.getSqlSession().selectOne(sqlId, filter);
	}

	@Override
	public Long getDistShopVisitorStatis(MapContext filter) {
		String sqlId = this.getNamedSqlId("getDistShopVisitorStatis");
		return this.getSqlSession().selectOne(sqlId, filter);
	}

	@Override
	public BigDecimal getDistShopAmountStatis(MapContext filter) {
		String sqlId = this.getNamedSqlId("getDistShopAmountStatis");
		return this.getSqlSession().selectOne(sqlId, filter);
	}
	
	@Override
	public BigDecimal getDistShopNoAmountStatis(MapContext filter) {
		String sqlId = this.getNamedSqlId("getDistShopNoAmountStatis");
		return this.getSqlSession().selectOne(sqlId, filter);
	}

	@Override
	public PaginatedList<Map<String, Long>> selectDistShopOrderCountGroupTime(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectDistShopOrderCountGroupTime");
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		@SuppressWarnings({ "unchecked", "rawtypes" })
		PageList<Map<String, Long>> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(),
				pageBounds);
		return this.toPaginatedList(PageList);
	}
	
	@Override
	public PaginatedList<Map<String, Long>> selectDistShopScvCountGroupTime(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectDistShopScvCountGroupTime");
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		@SuppressWarnings({ "unchecked", "rawtypes" })
		PageList<Map<String, Long>> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(),
				pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public SaleOrder selectById(Long entityId) {
		return null;
	}

	@Override
	public int insert(SaleOrder entity) {
		return 0;
	}

	@Override
	public int update(SaleOrder entity) {
		return 0;
	}

	@Override
	public int deleteById(Long entityId) {
		return 0;
	}
	
}