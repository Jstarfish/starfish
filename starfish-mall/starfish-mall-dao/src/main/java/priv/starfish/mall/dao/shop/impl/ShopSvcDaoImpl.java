package priv.starfish.mall.dao.shop.impl;

import java.util.HashMap;
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
import priv.starfish.mall.dao.shop.ShopSvcDao;
import priv.starfish.mall.shop.entity.ShopSvc;

@Component("shopSvcDao")
public class ShopSvcDaoImpl extends BaseDaoImpl<ShopSvc, Long> implements ShopSvcDao {
	@Override
	public ShopSvc selectById(Long id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public ShopSvc selectByShopIdAndSvcId(Integer shopId, Integer svcId) {
		String sqlId = this.getNamedSqlId("selectByShopIdAndSvcId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("shopId", shopId);
		params.put("svcId", svcId);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(ShopSvc shopSvc) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, shopSvc);
	}

	@Override
	public int update(ShopSvc shopSvc) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, shopSvc);
	}

	@Override
	public int deleteById(Long id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public Map<Long, ShopSvc> selectByShopIdAndSvcIds(Integer shopId, List<Long> svcIds) {
		String sqlId = this.getNamedSqlId("selectByShopIdAndSvcIds");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("shopId", shopId);
		map.put("svcIds", svcIds);
		if (svcIds == null || svcIds.size() == 0) {
			return null;
		}
		return this.getSqlSession().selectMap(sqlId, map, "svcId");
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<ShopSvc> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		MapContext filter = paginatedFilter.getFilterItems();
		String name = filter.getTypedValue("name", String.class);
		if (StrUtil.hasText(name)) {
			name = SqlBuilder.likeStrVal(name);
			filter.put("name", name);
		}
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<ShopSvc> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public int deleteByShopIdAndSvcId(Integer svcId, Integer shopId) {
		String sqlId = this.getNamedSqlId("deleteByShopIdAndSvcId");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("shopId", shopId);
		map.put("svcId", svcId);
		//
		return this.getSqlSession().delete(sqlId, map);
	}

	@Override
	public List<ShopSvc> selectByFilter(MapContext requestData) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		//
		return this.getSqlSession().selectList(sqlId, requestData);
	}

	@Override
	public int deleteBySvcId(Integer svcId) {
		String sqlId = this.getNamedSqlId("deleteBySvcId");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("svcId", svcId);
		//
		return this.getSqlSession().delete(sqlId, map);
	}

	@Override
	public List<ShopSvc> selectByShopId(Integer shopId) {
		String sqlId = this.getNamedSqlId("selectByShopId");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("shopId", shopId);
		//
		return this.getSqlSession().selectList(sqlId, map);
	}
}
