package priv.starfish.mall.dao.shop.impl;

import java.util.HashMap;
import java.util.Map;


import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.stereotype.Component;

import priv.starfish.common.jdbc.SqlBuilder;
import priv.starfish.common.model.PaginatedFilter;
import priv.starfish.common.model.PaginatedList;
import priv.starfish.common.util.MapContext;
import priv.starfish.common.util.StrUtil;
import priv.starfish.mall.dao.shop.ShopMemoDao;
import priv.starfish.mall.dao.base.impl.BaseDaoImpl;
import priv.starfish.mall.shop.entity.ShopMemo;

@Component("shopMemoDao")
public class ShopMemoDaoImpl extends BaseDaoImpl<ShopMemo, Integer> implements
        ShopMemoDao {
	
	@Override
	public int insert(ShopMemo shopMemo) {
		String sqlId = this.getNamedSqlId("insert");
		return this.getSqlSession().insert(sqlId, shopMemo);
	}

	@Override
	public int deleteById(Integer id) {
		String sqlId = this.getNamedSqlId("deleteById");
		return this.getSqlSession().delete(sqlId, id);
	}
	
	@Override
	public int update(ShopMemo shopMemo) {
		String sqlId = this.getNamedSqlId("update");
		return this.getSqlSession().update(sqlId, shopMemo);
	}
	
	@Override
	public PaginatedList<ShopMemo> selectByFilter(PaginatedFilter paginatedFilter){
		String sqlId = this.getNamedSqlId("selectByFilter");
		MapContext filter = paginatedFilter.getFilterItems();
		String keywords = filter.getTypedValue("keywords", String.class);
		filter.remove(keywords);
		if (StrUtil.hasText(keywords)) {
			keywords = SqlBuilder.likeStrVal(keywords);
			filter.put("keywords", keywords);
		}
		String  shopName = filter.getTypedValue("shopName", String.class);
		filter.remove( shopName);
		if (StrUtil.hasText( shopName)) {
			shopName = SqlBuilder.likeStrVal(shopName);
			filter.put("shopName",  shopName);
		}
		// 分页参数
		PageBounds pageBounds = toPageBounds(paginatedFilter.getPagination());
		@SuppressWarnings({ "unchecked", "rawtypes" })
		PageList<ShopMemo> PageList = (PageList) getSqlSession().selectList(sqlId, paginatedFilter.getFilterItems(), pageBounds);
		return this.toPaginatedList(PageList);
	}

	@Override
	public ShopMemo selectById(Integer id){
		String sqlId = this.getNamedSqlId("selectById");
		return this.getSqlSession().selectOne(sqlId, id);
	}
	
	@Override
	public ShopMemo selectByIdAndShopId(Integer id,Integer shopId){
		String sqlId = this.getNamedSqlId("selectByIdAndShopId");
		Map<String,Integer> map=new HashMap<String,Integer>();
		map.put("id", id);
		map.put("shopId", shopId);
		return this.getSqlSession().selectOne(sqlId, map);
	}
}