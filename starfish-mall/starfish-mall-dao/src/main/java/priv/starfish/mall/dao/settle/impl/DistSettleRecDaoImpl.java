package priv.starfish.mall.dao.settle.impl;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.jdbc.SqlBuilder;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.settle.DistSettleRecDao;
import priv.starfish.mall.settle.entity.DistSettleRec;

@Component("distSettleRecDao")
public class DistSettleRecDaoImpl extends BaseDaoImpl<DistSettleRec, Integer> implements DistSettleRecDao {
	
	@Override
	public PaginatedList<DistSettleRec> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		@SuppressWarnings({ "unchecked", "rawtypes" })
		PageList<DistSettleRec> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(),
				pageBounds);
		return this.toPaginatedList(PageList);
	}
	
	@Override
	public DistSettleRec selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(DistSettleRec distSettleRec) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, distSettleRec);
	}

	@Override
	public int update(DistSettleRec distSettleRec) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, distSettleRec);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<DistSettleRec> selectByFilterP(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilterP");
		
		MapContext filter = paginatedFilter.getFilterItems();
		
		String distShopName = filter.getTypedValue("distShopName", String.class);
		filter.remove(distShopName);
		if (StrUtil.hasText(distShopName)) {
			distShopName = SqlBuilder.likeStrVal(distShopName.toString());
			filter.put("distShopName", distShopName);
		}

		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<DistSettleRec> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}
}