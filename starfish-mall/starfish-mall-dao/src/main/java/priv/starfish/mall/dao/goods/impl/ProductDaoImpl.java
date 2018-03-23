package priv.starfish.mall.dao.goods.impl;

import java.util.Date;
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
import priv.starfish.mall.dao.goods.ProductDao;
import priv.starfish.mall.goods.entity.Product;

@Component("productDao")
public class ProductDaoImpl extends BaseDaoImpl<Product, Long> implements ProductDao {
	@Override
	public Product selectById(Long id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public int insert(Product product) {
		String sqlId = this.getNamedSqlId("insert");
		//默认为为被删除的货品
		product.setDeleted(false);
		int maxSeqNo = this.getEntityMaxSeqNo(Product.class);
		product.setSeqNo(maxSeqNo + 1);
		//
		product.setCreateTime(new Date());
		//
		return this.getSqlSession().insert(sqlId, product);
	}

	@Override
	public int update(Product product) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, product);
	}

	@Override
	public int deleteById(Long id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public List<Product> selectByGoodsIdAndDeleted(Integer goodsId, Boolean deleted) {
		String sqlId = this.getNamedSqlId("selectByGoodsIdAndDeleted");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("goodsId", goodsId);
		params.put("deleted", deleted);
		//
		return this.getSqlSession().selectList(sqlId, params);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<Product> selectProductsByContext(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectProductsByContext");

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
		filterItems.remove("title");
		if (StrUtil.hasText(title)) {
			title = SqlBuilder.likeStrVal(title);
			filterItems.put("title", title);
		}
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<Product> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<Product> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");

		MapContext filterItems = paginatedFilter.getFilterItems();
		//商品名称
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
		//关键字设置
		String keywords = paginatedFilter.getKeywords();
		if(StrUtil.hasText(keywords)){
			filterItems.remove("title");
			title = SqlBuilder.likeStrVal(keywords);
			filterItems.put("title", title);
		}
		
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination(), paginatedFilter.getSortItems());
		PageList<Product> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public int batchDownShelfProducts(List<Integer> ids, Integer shelfStatus, Date date) {
		String sqlId = this.getNamedSqlId("batchDownShelfProducts");
		Map<String, Object> params = this.newParamMap();
		params.put("shelfStatus", shelfStatus);
		// params.put("shelfTime", date);
		params.put("changeTime", date);
		params.put("list", ids);
		return this.getSqlSession().update(sqlId, params);
	}

	@Override
	public int batchUpShelfProducts(List<Integer> ids, Integer shelfStatus, Date date) {
		String sqlId = this.getNamedSqlId("batchUpShelfProducts");
		Map<String, Object> params = this.newParamMap();
		params.put("shelfStatus", shelfStatus);
		params.put("shelfTime", date);
		params.put("changeTime", date);
		params.put("list", ids);
		return this.getSqlSession().update(sqlId, params);
	}

	@Override
	public int updateDeletedById(Long productId, boolean deleted) {
		String sqlId = this.getNamedSqlId("updateDeletedById");
		Map<String, Object> params = this.newParamMap();
		params.put("id", productId);
		params.put("deleted", deleted);
		//
		return this.getSqlSession().update(sqlId, params);
	}

	@Override
	public List<Product> selectByIds(List<Long> productIds) {
		String sqlId = this.getNamedSqlId("selectByIds");
		Map<String, Object> params = this.newParamMap();
		params.put("list", productIds);
		return this.getSqlSession().selectList(sqlId, params);
	}

	@Override
	public int updateShelfStatus(Product product) {
		String sqlId = this.getNamedSqlId("updateShelfStatus");
		//
		return this.getSqlSession().update(sqlId, product);
	}

	@Override
	public List<Long> selectIdsByShelfStatusAndGoodsId(Integer shelfStatus, Integer goodsId) {
		String sqlId = this.getNamedSqlId("selectIdsByShelfStatusAndGoodsId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("shelfStatus", shelfStatus);
		params.put("goodsId", goodsId);
		//
		return this.getSqlSession().selectList(sqlId, params);
	}

	@Override
	public List<Product> selectUnShelvefProductsByGoodsId(Integer goodsId) {
		String sqlId = this.getNamedSqlId("selectUnShelvefProductsByGoodsId");
		//
		return this.getSqlSession().selectList(sqlId, goodsId);
	}
}
