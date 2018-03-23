package priv.starfish.mall.dao.demo.impl;

import java.util.Date;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.jdbc.SqlBuilder;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.dao.demo.ShopxDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.demo.entity.Shopx;

@Component("shopxDao")
public class ShopxDaoImpl extends BaseDaoImpl<Shopx, Integer> implements ShopxDao {
	@Override
	public Shopx selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public Shopx selectByName(String name) {
		String sqlId = this.getNamedSqlId("selectByName");
		//
		return this.getSqlSession().selectOne(sqlId, name);
	}

	@Override
	public int insert(Shopx shopx) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, shopx);
	}

	@Override
	public int update(Shopx shopx) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, shopx);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public PaginatedList<Shopx> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		//
		MapContext filter = paginatedFilter.getFilterItems();
		//
		String name = filter.getTypedValue("name", String.class);
		filter.remove("name");
		if (StrUtil.hasText(name)) {
			name = SqlBuilder.likeStrVal(name);
			filter.put("name", name);
		}
		String py = filter.getTypedValue("py", String.class);
		filter.remove("py");
		if (StrUtil.hasText(py)) {
			py = SqlBuilder.likeStrVal(py);
			filter.put("py", py);
		}
		String address = filter.getTypedValue("address", String.class);
		filter.remove("address");
		if (StrUtil.hasText(address)) {
			address = SqlBuilder.likeStrVal(address);
			filter.put("address", address);
		}
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination(), paginatedFilter.getSortItems());
		@SuppressWarnings({ "unchecked", "rawtypes" })
		PageList<Shopx> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public PaginatedList<Shopx> selectByScrolling(PaginatedFilter pager) {
		String sqlId = this.getNamedSqlId("selectByScrolling");
		// 分页参数
		PageBounds pageBounds = toPageBounds(pager.getPagination(), pager.getSortItems());
		@SuppressWarnings({ "unchecked", "rawtypes" })
		PageList<Shopx> PageList = (PageList) getSqlSession().selectList(sqlId, pager.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public PaginatedList<Shopx> selectByLatestChanges(PaginatedFilter pager) {
		String sqlId = this.getNamedSqlId("selectByLatestChanges");
		// 分页参数
		PageBounds pageBounds = toPageBounds(pager.getPagination(), pager.getSortItems());
		@SuppressWarnings({ "unchecked", "rawtypes" })
		PageList<Shopx> PageList = (PageList) getSqlSession().selectList(sqlId, pager.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public int updateAsIndexed(Integer id, Date indexTime) {
		String sqlId = this.getNamedSqlId("updateAsIndexed");

		Map<String, Object> params = this.newParamMap();
		params.put("id", id);
		params.put("indexTime", indexTime);

		return this.getSqlSession().update(sqlId, params);
	}
}