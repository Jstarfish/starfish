package priv.starfish.mall.dao.goods.impl;

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
import priv.starfish.mall.dao.goods.GoodsDao;
import priv.starfish.mall.goods.entity.Goods;

@Component("goodsDao")
public class GoodsDaoImpl extends BaseDaoImpl<Goods, Integer> implements GoodsDao {
	@Override
	public Goods selectById(Integer id) {
		String sqlId = this.getNamedSqlId("selectById");
		//
		return this.getSqlSession().selectOne(sqlId, id);
	}

	@Override
	public Goods selectByCatIdAndShopIdAndNameAndVendorId(Integer catId, Integer shopId, String name, Integer vendorId) {
		String sqlId = this.getNamedSqlId("selectByCatIdAndShopIdAndNameAndVendorId");
		//
		Map<String, Object> params = this.newParamMap();
		params.put("catId", catId);
		params.put("shopId", shopId);
		params.put("name", name);
		params.put("vendorId", vendorId);
		//
		return this.getSqlSession().selectOne(sqlId, params);
	}

	@Override
	public int insert(Goods goods) {
		String sqlId = this.getNamedSqlId("insert");
		//
		return this.getSqlSession().insert(sqlId, goods);
	}

	@Override
	public int update(Goods goods) {
		String sqlId = this.getNamedSqlId("update");
		//
		return this.getSqlSession().update(sqlId, goods);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		//
		return this.getSqlSession().delete(sqlId, id);
	}

	@Override
	public int selectCountByCatId(Integer catId) {
		String sqlId = this.getNamedSqlId("selectCountByCatId");
		return this.getSqlSession().selectOne(sqlId, catId);
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public PaginatedList<Goods> selectByFilter(PaginatedFilter paginatedFilter) {
		String sqlId = this.getNamedSqlId("selectByFilter");
		
		MapContext filterItems = paginatedFilter.getFilterItems();
		//
		String goodsName = filterItems.getTypedValue("goodsName", String.class);
		filterItems.remove("goodsName");
		if (StrUtil.hasText(goodsName)) {
			goodsName = SqlBuilder.likeStrVal(goodsName);
			filterItems.put("goodsName", goodsName);
		}
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		PageList<Goods> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(),
				pageBounds);
		return this.toPaginatedList(PageList);
	}
}