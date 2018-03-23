package priv.starfish.mall.dao.order.impl;

import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.dao.order.ECardOrderDao;
import priv.starfish.mall.order.entity.ECardOrder;

@Component("eCardOrderDao")
public class ECardOrderDaoImpl extends BaseDaoImpl<ECardOrder, Integer> implements ECardOrderDao {
	@Override
	public ECardOrder selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(ECardOrder eCardOrder) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, eCardOrder);
	}

	@Override
	public int update(ECardOrder eCardOrder) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, eCardOrder);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public PaginatedList<ECardOrder> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		@SuppressWarnings({ "unchecked", "rawtypes" })
		PageList<ECardOrder> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public PaginatedList<ECardOrder> selectByFilterBack(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilterBack");
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		@SuppressWarnings({ "unchecked", "rawtypes" })
		PageList<ECardOrder> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public ECardOrder selectOneByFilter(MapContext filter) {
		String sqlId = this.getNamedSqlId("selectOneByFilter");
		//
		return this.getSqlSession().selectOne(sqlId, filter);
	}

	@Override
	public int updateForCancel(MapContext filter) {
		String sqlId = this.getNamedSqlId("updateForCancel");
		//
		return this.getSqlSession().update(sqlId, filter);
	}

	@Override
	public ECardOrder selectByNo(String no) {
		String sqlId = this.getNamedSqlId("selectByNo");
		//
		return this.getSqlSession().selectOne(sqlId, no);
	}

	@Override
	public int updateByNo(Map<String, Object> map) {
		String sqlId = this.getNamedSqlId("updateByNo");
		//
		return this.getSqlSession().update(sqlId, map);
	}

	@Override
	public Integer selectCount(Integer userId, String code) {
		String sqlId = this.getNamedSqlId("selectCount");
		//
		MapContext filter = MapContext.newOne();
		filter.put("userId", userId);
		filter.put("code", code);
		return this.getSqlSession().selectOne(sqlId, filter);
	}

	@Override
	public List<ECardOrder> selectByFilterNormal(MapContext filter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		//
		return this.getSqlSession().selectList(sqlId, filter);
	}

	@Override
	public Integer selectCountByFilter(MapContext filter) {
		String sqlId = this.getNamedSqlId("selectCountByFilter");
		//
		return this.getSqlSession().selectOne(sqlId, filter);
	}

	@Override
	public Integer selectCountByCode(String cardCode) {
		String sqlId = this.getNamedSqlId("selectCountByCode");
		//
		return this.getSqlSession().selectOne(sqlId, cardCode);
	}
}