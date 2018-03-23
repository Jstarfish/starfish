package priv.starfish.mall.dao.shop.impl;

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
import priv.starfish.mall.dao.shop.DistShopDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.shop.entity.DistShop;

@Component("distShopDao")
public class DistShopDaoImpl extends BaseDaoImpl<DistShop, Integer> implements DistShopDao {
	@Override
	public DistShop selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(DistShop distShop) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, distShop);
	}

	@Override
	public int update(DistShop distShop) {
		String sqlId = this.getNamedSqlId("update");
		return this.getSqlSession().update(sqlId, distShop);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		return this.getSqlSession().delete(sqlId, id);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public PaginatedList<DistShop> selectDistShops(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectDistShops");
		MapContext filter = paginatedFilter.getFilterItems();
		
		// 根据店铺名查询使用
		String name = filter.getTypedValue("name", String.class);
		filter.remove(name);
		if(StrUtil.hasText(name)){
			name = SqlBuilder.likeStrVal(name);
			filter.put("name", name);
		}
		String regionName = filter.getTypedValue("regionName", String.class);
		filter.remove(regionName);
		if(StrUtil.hasText(regionName)){
			regionName = SqlBuilder.likeStrVal(regionName);
			filter.put("regionName", regionName);
		}
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<DistShop> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public PaginatedList<DistShop> selectByLatestChanges(PaginatedFilter pager) {
		String sqlId = this.getNamedSqlId("selectByLatestChanges");
		// 分页参数
		PageBounds pageBounds = toPageBounds(pager.getPagination(), pager.getSortItems());
		PageList<DistShop> PageList = (PageList) getSqlSession().selectList(sqlId, pager.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public int updateAsIndexed(Integer id, Date newIndexTime) {
		String sqlId = this.getNamedSqlId("updateAsIndexed");

		Map<String, Object> params = this.newParamMap();
		params.put("id", id);
		params.put("indexTime", newIndexTime);

		return this.getSqlSession().update(sqlId, params);
	}

	@Override
	public int updateChangeTime(Integer id) {
		String sqlId = this.getNamedSqlId("updateChangeTime");

		return this.getSqlSession().update(sqlId, id);
	}
}