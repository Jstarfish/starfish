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
import priv.starfish.mall.dao.shop.ShopProductDao;
import priv.starfish.mall.shop.entity.ShopProduct;

@Component("shopProductDao")
public class ShopProductDaoImpl extends BaseDaoImpl<ShopProduct, Long> implements ShopProductDao {
	@Override
	public ShopProduct selectById(Long id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(ShopProduct shopProduct) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, shopProduct);
	}

	@Override
	public int update(ShopProduct shopProduct) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, shopProduct);
	}

	@Override
	public int deleteById(Long id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<ShopProduct> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		MapContext filterItems = paginatedFilter.getFilterItems();
		//
		String goodsName = filterItems.getTypedValue("goodsName", String.class);
		filterItems.remove("goodsName");
		if (StrUtil.hasText(goodsName)) {
			goodsName = SqlBuilder.likeStrVal(goodsName);
			filterItems.put("goodsName", goodsName);
		}
		//
		String title = filterItems.getTypedValue("title", String.class);
		if(StrUtil.hasText(title)){
			filterItems.remove("title");
			title = SqlBuilder.likeStrVal(title);
			filterItems.put("title", title);
		}
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<ShopProduct> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		//
		PageList = this.checkAndRefetchList(paginatedFilter, sqlId, PageList);
		//
		return this.toPaginatedList(PageList);
	}

	@Override
	public int updateLackFlagById(Long id, Boolean lackFlag) {
		String sqlId = this.getNamedSqlId("updateLackFlagById");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("id", id);
		params.put("lackFlag", lackFlag);
		//
		return this.getSqlSession().update(sqlId, params);
	}

	@Override
	public List<ShopProduct> selectByFilter(MapContext filters) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		//
		return this.getSqlSession().selectList(sqlId, filters);
	}

	@Override
	public int selectCountShopIdByProductId(Long productId) {
		String sqlId = this.getNamedSqlId("selectCountShopIdByProductId");
		//
		return this.getSqlSession().selectOne(sqlId, productId);
	}

	@Override
	public int deleteByProductId(Long productId) {
		String sqlId = this.getNamedSqlId("deleteByProductId");
		//
		return this.getSqlSession().delete(sqlId, productId);
	}

	@Override
	public Map<Long, ShopProduct> selectLackFlagByShopIdAndProductIds(Integer shopId, List<Long> productIds) {
		String sqlId = this.getNamedSqlId("selectLackFlagByShopIdAndProductIds");
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("shopId", shopId);
		map.put("productIds", productIds);
		if(productIds==null||productIds.size()==0){
			return null;
		}
		return this.getSqlSession().selectMap(sqlId, map, "productId");
	}

}